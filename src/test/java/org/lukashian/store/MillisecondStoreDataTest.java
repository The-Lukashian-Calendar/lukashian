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
 * THIS SOFTWARE IS PROVIDED BY COPYRIGHT HOLDER "AS IS" AND ANY
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

import org.junit.jupiter.api.Test;
import org.lukashian.store.provider.StandardEarthMillisecondStoreDataProvider;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MillisecondStoreDataTest {

	private MillisecondStoreDataProvider provider = new StandardEarthMillisecondStoreDataProvider();
	private MillisecondStoreData data = new MillisecondStoreData(provider);

	@Test
	public void testGetUnixEpochMilliseconds() {
		//At the lukashian earth epoch, no leap seconds were added yet
		this.testDifferenceBetweenUnixAndLukashianTime(0, 0);

		//At the UNIX epoch, no leap seconds were added yet
		this.testDifferenceBetweenUnixAndLukashianTime(provider.loadUnixEpochOffsetMilliseconds(), 0);

		//At 78796800000 milliseconds after the UNIX epoch, the first leap second was added
		long lukashianEpochMillisecondsToTest = provider.loadUnixEpochOffsetMilliseconds() + 78796800000L;
		this.testDifferenceBetweenUnixAndLukashianTime(lukashianEpochMillisecondsToTest - 2, 0);
		this.testDifferenceBetweenUnixAndLukashianTime(lukashianEpochMillisecondsToTest - 1, 0);
		this.testDifferenceBetweenUnixAndLukashianTime(lukashianEpochMillisecondsToTest,     -1000);
		this.testDifferenceBetweenUnixAndLukashianTime(lukashianEpochMillisecondsToTest + 1, -1000);
		this.testDifferenceBetweenUnixAndLukashianTime(lukashianEpochMillisecondsToTest + 2, -1000);

		//At 362793600000 milliseconds after the UNIX epoch, the 10th leap second was added
		lukashianEpochMillisecondsToTest = provider.loadUnixEpochOffsetMilliseconds() + 362793600000L;
		this.testDifferenceBetweenUnixAndLukashianTime(lukashianEpochMillisecondsToTest - 2, -9000);
		this.testDifferenceBetweenUnixAndLukashianTime(lukashianEpochMillisecondsToTest - 1, -9000);
		this.testDifferenceBetweenUnixAndLukashianTime(lukashianEpochMillisecondsToTest,     -10000);
		this.testDifferenceBetweenUnixAndLukashianTime(lukashianEpochMillisecondsToTest + 1, -10000);
		this.testDifferenceBetweenUnixAndLukashianTime(lukashianEpochMillisecondsToTest + 2, -10000);

		//What we see here is that, whenever you increment the lukashian timestamp past the point of a leap second addition,
		//the unix timestamp will "jump back" by a second, effectively doing the same second again, because that certain unix
		//timestamp second represents two seconds in reality.

		//As of right now, 27 leap seconds have been added since the UNIX epoch
		this.testDifferenceBetweenUnixAndLukashianTime(provider.loadUnixEpochOffsetMilliseconds() + System.currentTimeMillis(), -27000);
	}

	private void testDifferenceBetweenUnixAndLukashianTime(long lukashianEpochMillisecondsToTest, long expectedDifference) {
		//Get the UNIX epoch milliseconds at the lukashian epoch
		long unixEpochMillisecondsAtLukashianEpoch = data.getUnixEpochMilliseconds(0);

		//Get the UNIX epoch milliseconds at the lukashian timestamp to test
		long unixEpochMillisecondsAtTimeToTest = data.getUnixEpochMilliseconds(lukashianEpochMillisecondsToTest);

		//Calculate the difference in unix epoch milliseconds
		long elapsedUnixMilliseconds = unixEpochMillisecondsAtTimeToTest - unixEpochMillisecondsAtLukashianEpoch;

		//Calculate the difference between elapsed unix time and elapsed lukashian time
		long difference = elapsedUnixMilliseconds - lukashianEpochMillisecondsToTest;
		assertEquals(expectedDifference, difference);
	}

	@Test
	public void testGetLukashianEpochMilliseconds() {
		//At the lukashian earth epoch, no leap seconds were added yet
		this.testDifferenceBetweenLukashianAndUnixTime(provider.loadUnixEpochOffsetMilliseconds() * -1, 0);

		//At the UNIX epoch, no leap seconds were added yet
		this.testDifferenceBetweenLukashianAndUnixTime(0, 0);

		//At 78796800000 milliseconds after the UNIX epoch, the first leap second was added
		long unixEpochMillisecondsToTest = 78796800000L;
		this.testDifferenceBetweenLukashianAndUnixTime(unixEpochMillisecondsToTest - 2, 0);
		this.testDifferenceBetweenLukashianAndUnixTime(unixEpochMillisecondsToTest - 1, 0);
		this.testDifferenceBetweenLukashianAndUnixTime(unixEpochMillisecondsToTest,     1000);
		this.testDifferenceBetweenLukashianAndUnixTime(unixEpochMillisecondsToTest + 1, 1000);
		this.testDifferenceBetweenLukashianAndUnixTime(unixEpochMillisecondsToTest + 2, 1000);

		//At 362793600000 milliseconds after the UNIX epoch, the 10th leap second was added
		unixEpochMillisecondsToTest = 362793600000L;
		this.testDifferenceBetweenLukashianAndUnixTime(unixEpochMillisecondsToTest - 1, 9000);
		this.testDifferenceBetweenLukashianAndUnixTime(unixEpochMillisecondsToTest - 2, 9000);
		this.testDifferenceBetweenLukashianAndUnixTime(unixEpochMillisecondsToTest,     10000);
		this.testDifferenceBetweenLukashianAndUnixTime(unixEpochMillisecondsToTest + 1, 10000);
		this.testDifferenceBetweenLukashianAndUnixTime(unixEpochMillisecondsToTest + 2, 10000);

		//What we see here is that, whenever you increment the unix timestamp past the point of a leap second addition,
		//the lukashian timestamp will "jump ahead" by a second, because that certain unix timestamp second represents
		//two seconds in reality.

		//As of right now, 27 leap seconds have been added since the UNIX epoch
		this.testDifferenceBetweenLukashianAndUnixTime(System.currentTimeMillis(), 27000);
	}

	private void testDifferenceBetweenLukashianAndUnixTime(long unixEpochMillisecondsToTest, long expectedDifference) {
		//Get the lukashian epoch milliseconds at the UNIX epoch
		long lukashianEpochMillisecondsAtUnixEpoch = data.getLukashianEpochMilliseconds(0);

		//Get the lukashian epoch milliseconds at the UNIX timestamp to test
		long lukashianEpochMillisecondsAtTimeToTest = data.getLukashianEpochMilliseconds(unixEpochMillisecondsToTest);

		//Calculate the difference in lukashian epoch milliseconds
		long elapsedLukashianMilliseconds = lukashianEpochMillisecondsAtTimeToTest - lukashianEpochMillisecondsAtUnixEpoch;

		//Calculate the difference between elapsed lukashian time and elapsed unix time
		long difference = elapsedLukashianMilliseconds - unixEpochMillisecondsToTest;
		assertEquals(expectedDifference, difference);
	}
}
