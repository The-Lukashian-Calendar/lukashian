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
package org.lukashian.store;

/**
 * An implementation of the {@link MillisecondStoreDataProvider} that implements the Lukashian Calendar Mechanism, resulting in a Lukashian
 * Calendar for unit test purposes. The Calendar implemented in this file looks like:
 *
 * Years:	            1                         2                         3                         4                   5          6         7        8
 * 			0 -------------------- 1000 -------------------- 2000 -------------------- 3000 -------------------- 4000 ------- 4499 ---- 4800 ---- 5000 ---- 40000
 *
 * Days:	   1      2      3      4       5        6       7       8      9       10      11      12      13      14      15      16      17       18
 * 			0 -- 300 -- 600 -- 900 -- 1200 --- 1500 -- 1800 -- 2100 -- 2400 -- 2700 -- 3000 -- 3300 -- 3600 -- 3900 -- 4200 -- 4500 -- 4799 -- 4900 ---- 39000
 *
 * Now:		                                ^ (1350)
 */
public class TestMillisecondStoreDataProvider implements MillisecondStoreDataProvider {

	@Override
	public long loadUnixEpochOffsetMilliseconds() {
		return 1350 - System.currentTimeMillis(); //For testing purposes, it's not a fixed value, to always know what time it is now
	}

	@Override
	public long[] loadYearEpochMilliseconds() {
		return new long[] {1000, 2000, 3000, 4000, 4499, 4800, 5000, 40000};
	}

	@Override
	public long[] loadDayEpochMilliseconds(long[] yearEpochMilliseconds) {
		return new long[] {300, 600, 900, 1200, 1500, 1800, 2100, 2400, 2700, 3000, 3300, 3600, 3900, 4200, 4500, 4799, 4900, 39000};
	}
}
