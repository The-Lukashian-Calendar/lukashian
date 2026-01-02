/*
 * Copyright (c) 2018-2026 (5918-5926 in Lukashian years)
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
package org.lukashian.store;

import org.lukashian.Day;
import org.lukashian.Instant;
import org.lukashian.LukashianException;
import org.lukashian.Year;
import org.lukashian.store.provider.StandardEarthMillisecondStoreDataProvider;
import org.lukashian.store.provider.StandardMarsMillisecondStoreDataProvider;
import org.lukashian.store.provider.external.http.StandardEarthHttpMillisecondStoreDataProvider;
import org.lukashian.store.provider.external.http.StandardMarsHttpMillisecondStoreDataProvider;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static org.lukashian.LukashianException.check;
import static org.lukashian.store.CalendarKeys.*;

/**
 * This singleton class manages the various instances of the Lukashian Calendar. Each instance is implemented by a {@link MillisecondStoreDataProvider}.
 * <p>
 * The int constants in the {@link CalendarKeys} class represent the instances that are supported by default. It is also possible to register
 * your own implementation of {@link MillisecondStoreDataProvider} and register it.
 * <p>
 * The default calendar instance will be used when {@link Year}, {@link Day} and {@link Instant} methods do not specify a calendar instance. By default,
 * the default is EARTH. It is possible to set a different default.
 * <p>
 * Data will only be requested from a {@link MillisecondStoreDataProvider} when the mechanism needs it, so no eager loading is done. Whenever data is
 * requested, it is stored in a {@link MillisecondStoreData} object for repeated use, so {@link MillisecondStoreDataProvider}s don't need to implement
 * caching themselves.
 *
 * @see MillisecondStoreData
 * @see MillisecondStoreDataProvider
 */
public final class MillisecondStore {

	private static final MillisecondStore INSTANCE = new MillisecondStore();

	private int defaultCalendarKey = EARTH;

	private final Map<Integer, MillisecondStoreDataProvider> providers = new ConcurrentHashMap<>();
	private final Map<Integer, MillisecondStoreData> data = new ConcurrentHashMap<>();

	private MillisecondStore() {
		this.registerProvider(EARTH, new StandardEarthMillisecondStoreDataProvider());
		this.registerProvider(EARTH_HTTP_LUKASHIAN_ORG, new StandardEarthHttpMillisecondStoreDataProvider());
		this.registerProvider(MARS, new StandardMarsMillisecondStoreDataProvider());
		this.registerProvider(MARS_HTTP_LUKASHIAN_ORG, new StandardMarsHttpMillisecondStoreDataProvider());
	}

	/**
	 * Gets the singleton instance of the {@link MillisecondStore}.
	 */
	public static MillisecondStore store() {
		return INSTANCE;
	}

	/**
	 * Shortcut method to call {@link #getDefaultCalendarKey()} on the singleton {@link #INSTANCE}.
	 */
	public static int defaultCalendarKey() {
		return INSTANCE.getDefaultCalendarKey();
	}

	/**
	 * Shortcut method to call {@link #getData(int)} on the singleton {@link #INSTANCE}.
	 */
	public static MillisecondStoreData data(int calendarKey) {
		return INSTANCE.getData(calendarKey);
	}

	/**
	 * Get the default calendar key, for when no explicit calendar key is provided.
	 */
	public int getDefaultCalendarKey() {
		return defaultCalendarKey;
	}

	/**
	 * Set the default calendar key, for when no explicit calendar key is provided.
	 */
	public void setDefaultCalendarKey(int defaultCalendarKey) {
		this.defaultCalendarKey = defaultCalendarKey;
	}

	/**
	 * Registers the given {@link MillisecondStoreDataProvider} under the given key. It is not advisable to overwrite the
	 * standard keys, i.e. the int constants in the {@link CalendarKeys} class, as other classes might depend on those constants to represent
	 * what they are intended to represent.
	 */
	public void registerProvider(int key, MillisecondStoreDataProvider provider) {
		providers.put(key, provider);
	}

	/**
	 * Gets the {@link MillisecondStoreData} generated by the {@link MillisecondStoreDataProvider} with the given key.
	 * <p>
	 * Data is requested from the {@link MillisecondStoreDataProvider} only once, after which it is stored for future use.
	 *
	 * @throws LukashianException when the given key is not mapped to a {@link MillisecondStoreDataProvider}
	 */
	public MillisecondStoreData getData(int calendarKey) {
		check(providers.get(calendarKey) != null, () -> "Please register provider for key " + calendarKey + " before calling this method with key " + calendarKey);

		return data.computeIfAbsent(calendarKey, k -> new MillisecondStoreData(providers.get(k)));
	}

	/**
	 * Clears the {@link MillisecondStoreData} corresponding to the given key, so that, upon the next call to {@link #data(int)} with
	 * that key, the data is re-requested from the {@link MillisecondStoreDataProvider}.
	 */
	public void clearData(int key) {
		data.remove(key);
	}

	/**
	 * Clears all {@link MillisecondStoreData}, so that, upon the next call to {@link #data(int)}, the data is re-requested
	 * from the {@link MillisecondStoreDataProvider}.
	 */
	public void clearAllData() {
		data.clear();
	}
}
