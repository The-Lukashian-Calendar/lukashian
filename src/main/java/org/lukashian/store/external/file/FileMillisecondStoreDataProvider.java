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
package org.lukashian.store.external.file;

import org.lukashian.store.external.ExternalResourceMillisecondStoreDataProvider;
import org.lukashian.store.external.http.StandardEarthHttpMillisecondStoreDataProvider;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * This implementation of {@link ExternalResourceMillisecondStoreDataProvider} loads binary streams of long values from a file.
 * <p>
 * Please see {@link ExternalResourceMillisecondStoreDataProvider} for more details regarding the external resource mechanism.
 * <p>
 * The {@link FileMillisecondStoreDataProvider} is useful for applications that want to load the numbers of milliseconds from
 * the official lukashian.org server, but don't have access to the Internet. If you want to use this class to load the exact same
 * numbers as the {@link StandardEarthHttpMillisecondStoreDataProvider}, then you can simply perform requests to the location specified
 * in the {@link StandardEarthHttpMillisecondStoreDataProvider}, extended with the default locations specified in
 * {@link ExternalResourceMillisecondStoreDataProvider}, for example:
 * <pre>
 *     curl -L https://lukashian.org/millisecondstore/standardearth/unixEpochOffset -o unixEpochOffset
 * </pre>
 * Alternatively, you can simply use a browser to obtain the file at the above example url.
 */
public class FileMillisecondStoreDataProvider extends ExternalResourceMillisecondStoreDataProvider {

	public FileMillisecondStoreDataProvider(String basePath, String unixEpochOffsetPathExtension, String yearEpochMillisecondsPathExtension, String dayEpochMillisecondsPathExtension) {
		super(basePath, unixEpochOffsetPathExtension, yearEpochMillisecondsPathExtension, dayEpochMillisecondsPathExtension);
	}

	public FileMillisecondStoreDataProvider(String basePath) {
		super(basePath);
	}

	protected byte[] loadMillisecondsByteArray(String path) throws IOException {
		try (FileInputStream fis = new FileInputStream(path)) {
			return fis.readAllBytes();
		}
	}
}
