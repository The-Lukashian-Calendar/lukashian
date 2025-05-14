/*
 * Copyright (c) 2018-2025 (5918-5925 in Lukashian years)
 * All rights reserved.
 *
 * The Lukashian Calendar and The Lukashian Calendar Mechanism are registered
 * at the Benelux Office for Intellectual Property, registration number 120712.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, the above registration notice, this list of conditions
 *    and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, the above registration notice, this list of conditions
 *    and the following disclaimer in the documentation and/or other materials
 *    provided with the distribution.
 * 3. All materials mentioning features or use of this software,
 *    the Lukashian Calendar or the underlying Lukashian Calendar Mechanism,
 *    with or without modification, must refer to the Calendar as "The
 *    Lukashian Calendar" and to the Calendar Mechanism as "The Lukashian
 *    Calendar Mechanism".
 * 4. Renaming of source code, binary form, the Lukashian Calendar or the
 *    Lukashian Calendar Mechanism, with or without modification, is explicitly
 *    disallowed. Any copies, extracts, code excerpts, forks, redistributions
 *    or translations into other languages of source code, binary form,
 *    the functional behaviour of the Lukashian Calendar as defined by source code or
 *    the functional behaviour of the Lukashian Calendar Mechanism as defined by source
 *    code, with or without modification, must refer to the Calendar
 *    as "The Lukashian Calendar" and to the Calendar Mechanism as "The
 *    Lukashian Calendar Mechanism".
 * 5. Any copies, extracts, code excerpts, forks, redistributions
 *    or translations into other languages of source code, binary form,
 *    the functional behaviour of the Lukashian Calendar as defined by source code or
 *    the functional behaviour of the Lukashian Calendar Mechanism as defined by source
 *    code, with or without modification, may not include modifications that
 *    change the functional behaviour of the Lukashian Calendar Mechanism as
 *    implemented by source code.
 *
 * THIS SOFTWARE IS PROVIDED BY COPYRIGHT HOLDER ''AS IS'' AND ANY
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL COPYRIGHT HOLDER BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.lukashian.store.external;

import org.lukashian.store.MillisecondStoreDataProvider;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.LongBuffer;

/**
 * This class provides base functionality for loading numbers of milliseconds from an external resource, like a file or a url. This allows for centralized
 * maintenance and governance of the exact numbers that define an instance of the calendar.
 * <p>
 * A {@link MillisecondStoreDataProvider} needs to provide three things: the unix epoch offset, the milliseconds of the years and the milliseconds of the days.
 * Please see the javadoc of {@link MillisecondStoreDataProvider} for more details.
 * <p>
 * An {@link ExternalResourceMillisecondStoreDataProvider} requires a base location and three extensions, one for each method of @{@link MillisecondStoreDataProvider}.
 * It then loads the required values by combining the base location with each extension. The base location can, for example, be a directory or a url. The
 * extensions then point to individual files at that base location. <b>The base location should always end with a path separator, because the extensions are
 * added to the base location as is</b>.
 * <p>
 * Default values are given for the extensions corresponding to the three things that need to be provided by the {@link MillisecondStoreDataProvider}. Implementations
 * that use these defaults then only need to specify the base location.
 * <p>
 * Loading of the milliseconds is done by loading a binary stream from the specified location. This binary stream should encode the long values that represent the
 * milliseconds. <b>Each consecutive 8 bytes of the stream should correspond to an individual millisecond</b>. This binary stream is then decoded into an array of longs.
 * For the unix epoch offset, only a single long value (i.e. 8 bytes) should be returned in the binary stream.
 * <p>
 * The actual mechanism for loading the binary stream is left to subclasses of this class, so that various implementations can provide functionality for loading the
 * milliseconds from various kinds of external resources, such as files, http urls, etc. See {@link #loadMilliseconds(String)} for details.
 */
public abstract class ExternalResourceMillisecondStoreDataProvider implements MillisecondStoreDataProvider {

	public static final String DEFAULT_UNIX_EPOCH_OFFSET_EXTENSION = "unixEpochOffset";
	public static final String DEFAULT_YEAR_EPOCH_MILLISECONDS_EXTENSION = "yearEpochMilliseconds";
	public static final String DEFAULT_DAY_EPOCH_MILLISECONDS_EXTENSION = "dayEpochMilliseconds";

	private String baseLocation;
	private String unixEpochOffsetExtension;
	private String yearEpochMillisecondsExtension;
	private String dayEpochMillisecondsExtension;

	/**
	 * Creates an {@link ExternalResourceMillisecondStoreDataProvider} that loads arrays of milliseconds from the specified base location and extensions.
	 * <b>The base location should always end with a path separator, because the extensions are added to the base location as is</b>.
	 */
	public ExternalResourceMillisecondStoreDataProvider(String baseLocation, String unixEpochOffsetExtension, String yearEpochMillisecondsExtension, String dayEpochMillisecondsExtension) {
		this.baseLocation = baseLocation;
		this.unixEpochOffsetExtension = unixEpochOffsetExtension;
		this.yearEpochMillisecondsExtension = yearEpochMillisecondsExtension;
		this.dayEpochMillisecondsExtension = dayEpochMillisecondsExtension;
	}

	/**
	 * Creates an {@link ExternalResourceMillisecondStoreDataProvider} that loads arrays of milliseconds from the specified base location, using the
	 * default extensions. <b>The base location should always end with a path separator, because the extensions are added to the base location as is</b>.
	 */
	public ExternalResourceMillisecondStoreDataProvider(String baseLocation) {
		this(baseLocation, DEFAULT_UNIX_EPOCH_OFFSET_EXTENSION, DEFAULT_YEAR_EPOCH_MILLISECONDS_EXTENSION, DEFAULT_DAY_EPOCH_MILLISECONDS_EXTENSION);
	}

	@Override
	public long loadUnixEpochOffsetMilliseconds() {
		long[] array = this.loadMilliseconds(unixEpochOffsetExtension);
		if (array.length != 1) {
			throw new IllegalStateException("Expected exactly one unix epoch offset");
		}
		return array[0];
	}

	@Override
	public long[] loadYearEpochMilliseconds() {
		return this.loadMilliseconds(yearEpochMillisecondsExtension);
	}

	@Override
	public long[] loadDayEpochMilliseconds(long[] yearEpochMilliseconds) {
		return this.loadMilliseconds(dayEpochMillisecondsExtension);
	}

	private long[] loadMilliseconds(String extension) {
		try {
			byte[] byteArray = this.loadMillisecondsByteArray(baseLocation + extension);
			if (byteArray == null || byteArray.length == 0) {
				throw new IOException("No bytes could be loaded from '" + baseLocation + extension + "'");
			}

			LongBuffer longBuffer = ByteBuffer.wrap(byteArray).asLongBuffer();
			long[] longs = new long[longBuffer.capacity()];
			longBuffer.get(longs);
            return longs;
		} catch (Exception e) {
			throw new RuntimeException("Exception occurred during loading of milliseconds from External Resource", e);
		}
	}

	/**
	 * Loads an array of bytes from the specified location. This location is a concatenation of the base location and one of the extensions, depending
	 * on which of the values needs loading.
	 * <p>
	 * This method needs to return a binary stream from the specified location. This binary stream should encode the long values that represent the
	 * milliseconds. <b>Each consecutive 8 bytes of the stream should correspond to an individual millisecond</b>. This binary stream is then decoded
	 * into an array of longs. For the unix epoch offset, only a single long value (i.e. 8 bytes) should be returned in the binary stream.
	 */
	protected abstract byte[] loadMillisecondsByteArray(String location) throws Exception;
}
