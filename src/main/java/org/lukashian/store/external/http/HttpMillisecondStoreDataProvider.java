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
package org.lukashian.store.external.http;

import org.lukashian.store.external.ExternalResourceMillisecondStoreDataProvider;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static java.net.http.HttpClient.Redirect.NORMAL;

/**
 * This implementation of {@link ExternalResourceMillisecondStoreDataProvider} loads binary streams of long values from an HTTP location.
 * <p>
 * Please see {@link ExternalResourceMillisecondStoreDataProvider} for more details regarding the external resource mechanism.
 */
public class HttpMillisecondStoreDataProvider extends ExternalResourceMillisecondStoreDataProvider {

	public HttpMillisecondStoreDataProvider(String baseUrl, String unixEpochOffsetUrlExtension, String yearEpochMillisecondsUrlExtension, String dayEpochMillisecondsUrlExtension) {
		super(baseUrl, unixEpochOffsetUrlExtension, yearEpochMillisecondsUrlExtension, dayEpochMillisecondsUrlExtension);
	}

	public HttpMillisecondStoreDataProvider(String baseUrl) {
		super(baseUrl);
	}

	protected byte[] loadMillisecondsByteArray(String url) throws IOException, InterruptedException {
		try (HttpClient client = HttpClient.newBuilder().followRedirects(NORMAL).build()) {
			HttpRequest request = HttpRequest.newBuilder()
				.GET()
				.header("Accept", "application/octet-stream")
				.uri(URI.create(url))
				.build();

			HttpResponse<byte[]> response = client.send(request, HttpResponse.BodyHandlers.ofByteArray());

			if (response.statusCode() == 200) {
				return response.body();
			} else {
				throw new IOException("Expected response code 200, instead received response code " + response.statusCode());
			}
		}
	}
}
