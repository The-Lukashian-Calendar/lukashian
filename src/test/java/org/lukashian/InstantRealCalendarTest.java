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
package org.lukashian;

import org.apache.commons.numbers.fraction.BigFraction;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.lukashian.store.MillisecondStore;
import org.lukashian.store.StandardEarthMillisecondStoreDataProvider;
import org.lukashian.store.TestMillisecondStoreDataProvider;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit tests for the {@link Instant} class that use the {@link StandardEarthMillisecondStoreDataProvider} instead of the @{@link TestMillisecondStoreDataProvider}.
 */
public class InstantRealCalendarTest {

	@BeforeAll
	public static void setUp() {
		//Running the tests in this file with the actual calendar
		MillisecondStore.store().setMillisecondStoreDataProvider(new StandardEarthMillisecondStoreDataProvider());
	}

	@AfterAll
	public static void tearDown() {
		//After this test, switch back to the test calendar again
		MillisecondStore.store().setMillisecondStoreDataProvider(new TestMillisecondStoreDataProvider());
	}

	@Test
	public void testInstantEquality() {
		//For the first thousand milliseconds of the calendar and a bit more
		for (int i = 1; i <= 100000; i++) {
			this.testInstantEquality(i);
		}

		//For the periods overlapping the start and end of this day
		Day today = Day.now();
		long start = today.getEpochMillisecondsAtStartOfDay();
		long end = today.getEpochMilliseconds();
		for (long i = start - 100000; i <= start + 100000; i++) {
			this.testInstantEquality(i);
		}
		for (long i = end - 100000; i <= end + 100000; i++) {
			this.testInstantEquality(i);
		}

		//For the beeps of today
		for (int i = 0; i < 10000; i++) {
			Instant instant = today.atTime(i);
			assertEquals(i, instant.getBeeps());
		}
	}

	private void testInstantEquality(long epochMillis) {
		Instant instant = Instant.of(epochMillis);
		assertEquals(epochMillis, instant.getEpochMilliseconds());

		Instant copy = Instant.of(instant.getDay(), instant.getProportionOfDay());
		assertEquals(instant.getEpochMilliseconds(), copy.getEpochMilliseconds());
		assertEquals(instant.getProportionOfDay(), copy.getProportionOfDay());
		assertEquals(instant.getBeeps(), copy.getBeeps());
		assertEquals(instant, copy);

		Instant beepCopy = Instant.of(instant.getDay(), instant.getBeeps());
		assertEquals(instant.getBeeps(), beepCopy.getBeeps());
	}

	@Test
	public void testMinusProportionOfDay() {
		Instant realInstant = Instant.of(Day.of(4), 5000);
		assertEquals(Instant.of(Day.of(4), 0), realInstant.minusProportionOfDay(BigFraction.of(499999999999999999L, 1000000000000000000L)));
		assertEquals(Instant.of(Day.of(3), 5001), realInstant.minusProportionOfDay(BigFraction.of(9999, 10000)));
		assertEquals(Instant.of(Day.of(3), 5000), realInstant.minusProportionOfDay(BigFraction.of(999999999999999999L, 1000000000000000000L)));
	}

	@Test
	public void testMinusBeeps() {
		Instant realInstant = Instant.of(Day.of(4), 5000);
		assertEquals(Instant.of(Day.of(1), 9999), realInstant.minusBeeps(25001));
		assertEquals(Instant.of(Day.of(2), 0), realInstant.minusBeeps(25000));
		assertEquals(Instant.of(Day.of(2), 1), realInstant.minusBeeps(24999));
		assertEquals(Instant.of(Day.of(2), 5000), realInstant.minusBeeps(20000));
		assertEquals(Instant.of(Day.of(3), 5000), realInstant.minusBeeps(10000));
		assertEquals(Instant.of(Day.of(3), 9999), realInstant.minusBeeps(5001));
		assertEquals(Instant.of(Day.of(4), 0), realInstant.minusBeeps(5000));
		assertEquals(Instant.of(Day.of(4), 1), realInstant.minusBeeps(4999));
		assertEquals(Instant.of(Day.of(4), 4000), realInstant.minusBeeps(1000));
		assertEquals(Instant.of(Day.of(4), 4999), realInstant.minusBeeps(1));
		assertEquals(Instant.of(Day.of(4), 5000), realInstant.minusBeeps(0));
	}

	@Test
	public void testPlusProportionOfDay() {
		Instant realInstant = Instant.of(Day.of(4), 5000);
		assertEquals(Instant.of(Day.of(5), 4999), realInstant.plusProportionOfDay(BigFraction.of(9999, 10000)));
		//Can't test the 'plus' version of the testcases with many BigDecimals, because overflow into the next beep is prevented and therefore the resulting Instant cannot be expressed with a whole number of beeps
	}

	@Test
	public void testPlusBeeps() {
		Instant realInstant = Instant.of(Day.of(4), 5000);
		assertEquals(Instant.of(Day.of(7), 1), realInstant.plusBeeps(25001));
		assertEquals(Instant.of(Day.of(7), 0), realInstant.plusBeeps(25000));
		assertEquals(Instant.of(Day.of(6), 9999), realInstant.plusBeeps(24999));
		assertEquals(Instant.of(Day.of(6), 5000), realInstant.plusBeeps(20000));
		assertEquals(Instant.of(Day.of(5), 5000), realInstant.plusBeeps(10000));
		assertEquals(Instant.of(Day.of(5), 1), realInstant.plusBeeps(5001));
		assertEquals(Instant.of(Day.of(5), 0), realInstant.plusBeeps(5000));
		assertEquals(Instant.of(Day.of(4), 9999), realInstant.plusBeeps(4999));
		assertEquals(Instant.of(Day.of(4), 6000), realInstant.plusBeeps(1000));
		assertEquals(Instant.of(Day.of(4), 5001), realInstant.plusBeeps(1));
		assertEquals(Instant.of(Day.of(4), 5000), realInstant.plusBeeps(0));
	}

	@Test
	public void testDifferenceInBeepsWith() {
		assertEquals(-24999, Instant.of(Day.of(3), 5000).differenceInBeepsWith(Instant.of(Day.of(5), 9999)));
		assertEquals(-20001, Instant.of(Day.of(3), 5000).differenceInBeepsWith(Instant.of(Day.of(5), 5001)));
		assertEquals(-20000, Instant.of(Day.of(3), 5000).differenceInBeepsWith(Instant.of(Day.of(5), 5000)));
		assertEquals(-19999, Instant.of(Day.of(3), 5000).differenceInBeepsWith(Instant.of(Day.of(5), 4999)));
		assertEquals(-15000, Instant.of(Day.of(3), 5000).differenceInBeepsWith(Instant.of(Day.of(5), 0)));

		assertEquals(-14999, Instant.of(Day.of(3), 5000).differenceInBeepsWith(Instant.of(Day.of(4), 9999)));
		assertEquals(-10001, Instant.of(Day.of(3), 5000).differenceInBeepsWith(Instant.of(Day.of(4), 5001)));
		assertEquals(-10000, Instant.of(Day.of(3), 5000).differenceInBeepsWith(Instant.of(Day.of(4), 5000)));
		assertEquals(-9999, Instant.of(Day.of(3), 5000).differenceInBeepsWith(Instant.of(Day.of(4), 4999)));
		assertEquals(-5000, Instant.of(Day.of(3), 5000).differenceInBeepsWith(Instant.of(Day.of(4), 0)));

		assertEquals(-4999, Instant.of(Day.of(3), 5000).differenceInBeepsWith(Instant.of(Day.of(3), 9999)));
		assertEquals(-1000, Instant.of(Day.of(3), 5000).differenceInBeepsWith(Instant.of(Day.of(3), 6000)));
		assertEquals(-1, Instant.of(Day.of(3), 5000).differenceInBeepsWith(Instant.of(Day.of(3), 5001)));
		assertEquals(0, Instant.of(Day.of(3), 5000).differenceInBeepsWith(Instant.of(Day.of(3), 5000)));
		assertEquals(1, Instant.of(Day.of(3), 5000).differenceInBeepsWith(Instant.of(Day.of(3), 4999)));
		assertEquals(1000, Instant.of(Day.of(3), 5000).differenceInBeepsWith(Instant.of(Day.of(3), 4000)));
		assertEquals(4999, Instant.of(Day.of(3), 5000).differenceInBeepsWith(Instant.of(Day.of(3), 1)));
		assertEquals(5000, Instant.of(Day.of(3), 5000).differenceInBeepsWith(Instant.of(Day.of(3), 0)));

		assertEquals(5001, Instant.of(Day.of(3), 5000).differenceInBeepsWith(Instant.of(Day.of(2), 9999)));
		assertEquals(9999, Instant.of(Day.of(3), 5000).differenceInBeepsWith(Instant.of(Day.of(2), 5001)));
		assertEquals(10000, Instant.of(Day.of(3), 5000).differenceInBeepsWith(Instant.of(Day.of(2), 5000)));
		assertEquals(10001, Instant.of(Day.of(3), 5000).differenceInBeepsWith(Instant.of(Day.of(2), 4999)));
		assertEquals(15000, Instant.of(Day.of(3), 5000).differenceInBeepsWith(Instant.of(Day.of(2), 0)));

		assertEquals(15001, Instant.of(Day.of(3), 5000).differenceInBeepsWith(Instant.of(Day.of(1), 9999)));
		assertEquals(19999, Instant.of(Day.of(3), 5000).differenceInBeepsWith(Instant.of(Day.of(1), 5001)));
		assertEquals(20000, Instant.of(Day.of(3), 5000).differenceInBeepsWith(Instant.of(Day.of(1), 5000)));
		assertEquals(20001, Instant.of(Day.of(3), 5000).differenceInBeepsWith(Instant.of(Day.of(1), 4999)));
		assertEquals(25000, Instant.of(Day.of(3), 5000).differenceInBeepsWith(Instant.of(Day.of(1), 0)));

		assertEquals(-2000, Instant.of(Day.of(3), 3000).differenceInBeepsWith(Instant.of(Day.of(3), BigFraction.of(500099999, 1000000000))));
		assertEquals(0, Instant.of(Day.of(3), 3000).differenceInBeepsWith(Instant.of(Day.of(3), BigFraction.of(300099999, 1000000000))));
	}
}
