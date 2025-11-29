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
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.lukashian.store.MillisecondStore;
import org.lukashian.store.MillisecondStoreData;
import org.lukashian.store.MillisecondStoreDataProvider;
import org.lukashian.store.TestMillisecondStoreDataProvider;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.lukashian.LukashianAssert.*;
import static org.lukashian.store.CalendarKeys.EARTH;
import static org.lukashian.store.TestMillisecondStoreDataProvider.TEST;

/**
 * Unit tests for the {@link Instant} class.
 */
public class InstantTest {

	@BeforeAll
	public static void setUp() {
		MillisecondStore.store().registerProvider(TEST, new TestMillisecondStoreDataProvider());
		MillisecondStore.store().setDefaultCalendarKey(TEST);
	}

	@Test
	public void testMinusYears() {
		Instant instant = Instant.ofEpoch(4050, TEST);

		assertLukashianException(() -> instant.minusYears(1));
		assertLukashianException(() -> instant.minusYears(4));
		assertInstant(1050, TEST, instant.minusYears(3));
	}

	@Test
	public void testPlusYears() {
		Instant instant = Instant.ofEpoch(1050, TEST);

		assertLukashianException(() -> instant.plusYears(1));
		assertInstant(4050, TEST, instant.plusYears(3));
	}

	@Test
	public void testMinusDays() {
		assertInstant(150, TEST, Instant.ofEpoch(450, TEST).minusDays(1));
		assertLukashianException(() -> Instant.ofEpoch(450, TEST).minusDays(2));
	}

	@Test
	public void testPlusDays() {
		assertInstant(750, TEST, Instant.ofEpoch(450, TEST).plusDays(1));
		assertLukashianException(() -> Instant.ofEpoch(450, TEST).plusDays(17));
	}

	@Test
	public void testAtPreviousDay() {
		assertInstant(150, TEST, Instant.ofEpoch(450, TEST).atPreviousDay());
	}

	@Test
	public void testAtNextDay() {
		assertInstant(750, TEST, Instant.ofEpoch(450, TEST).atNextDay());
	}

	@Test
	public void testMinusMilliseconds() {
		Instant instant = Instant.ofEpoch(450, TEST);

		assertLukashianException(() -> instant.minusMilliseconds(450));
		assertInstant(1, TEST, instant.minusMilliseconds(449));
		assertInstant(450, TEST, instant.minusMilliseconds(0));
		assertInstant(6450, TEST, instant.minusMilliseconds(-6000));
	}

	@Test
	public void testMinusSeconds() {
		assertInstant(1000, TEST, Instant.ofEpoch(2000, TEST).minusSeconds(1));
	}

	@Test
	public void testPlusMilliseconds() {
		Instant instant = Instant.ofEpoch(450, TEST);

		assertLukashianException(() -> instant.plusMilliseconds(-450));
		assertInstant(650, TEST, instant.plusMilliseconds(200));
		assertInstant(6450, TEST, instant.plusMilliseconds(6000));
		assertInstant(250, TEST, instant.plusMilliseconds(-200));
	}

	@Test
	public void testPlusSeconds() {
		assertInstant(3000, TEST, Instant.ofEpoch(2000, TEST).plusSeconds(1));
	}

	@Test
	public void testMinusBeeps() {
		Instant instant = Instant.ofEpoch(1651, TEST);

		assertLukashianException(() -> instant.minusBeeps(55034));
		assertInstant(1, TEST, instant.minusBeeps(54999));
		assertInstant(1199, TEST, instant.minusBeeps(15066));
		assertInstant(1200, TEST, instant.minusBeeps(15010));
		assertInstant(1201, TEST, instant.minusBeeps(14999));
		assertInstant(1351, TEST, instant.minusBeeps(10000));
		assertInstant(1499, TEST, instant.minusBeeps(5066));
		assertInstant(1500, TEST, instant.minusBeeps(5010));
		assertInstant(1501, TEST, instant.minusBeeps(4999));
		assertInstant(1621, TEST, instant.minusBeeps(1000));
		assertInstant(1649, TEST, instant.minusBeeps(66));
		assertInstant(1651, TEST, instant.minusBeeps(0));
		assertInstant(1652, TEST, instant.minusBeeps(-34));
		assertInstant(1681, TEST, instant.minusBeeps(-1000));
		assertInstant(1799, TEST, instant.minusBeeps(-4934));
		assertInstant(1800, TEST, instant.minusBeeps(-4999));
		assertInstant(1801, TEST, instant.minusBeeps(-5000));
		assertInstant(1951, TEST, instant.minusBeeps(-10000));
		assertInstant(2099, TEST, instant.minusBeeps(-14934));
		assertInstant(2100, TEST, instant.minusBeeps(-14999));
		assertInstant(2101, TEST, instant.minusBeeps(-15000));
		assertInstant(2551, TEST, instant.minusBeeps(-30000));
	}

	@Test
	public void testPlusBeeps() {
		Instant instant = Instant.ofEpoch(1651, TEST);

		assertLukashianException(() -> instant.plusBeeps(-55034));
		assertInstant(1, TEST, instant.plusBeeps(-54999));
		assertInstant(1199, TEST, instant.plusBeeps(-15066));
		assertInstant(1200, TEST, instant.plusBeeps(-15010));
		assertInstant(1201, TEST, instant.plusBeeps(-14999));
		assertInstant(1351, TEST, instant.plusBeeps(-10000));
		assertInstant(1499, TEST, instant.plusBeeps(-5066));
		assertInstant(1500, TEST, instant.plusBeeps(-5010));
		assertInstant(1501, TEST, instant.plusBeeps(-4999));
		assertInstant(1621, TEST, instant.plusBeeps(-1000));
		assertInstant(1649, TEST, instant.plusBeeps(-66));
		assertInstant(1651, TEST, instant.plusBeeps(0));
		assertInstant(1652, TEST, instant.plusBeeps(34));
		assertInstant(1681, TEST, instant.plusBeeps(1000));
		assertInstant(1799, TEST, instant.plusBeeps(4934));
		assertInstant(1800, TEST, instant.plusBeeps(4999));
		assertInstant(1801, TEST, instant.plusBeeps(5000));
		assertInstant(1951, TEST, instant.plusBeeps(10000));
		assertInstant(2099, TEST, instant.plusBeeps(14934));
		assertInstant(2100, TEST, instant.plusBeeps(14999));
		assertInstant(2101, TEST, instant.plusBeeps(15000));
		assertInstant(2551, TEST, instant.plusBeeps(30000));
	}

	@Test
	public void testIsBefore() {
		assertTrue(Instant.ofEpoch(1, TEST).isBefore(Instant.ofEpoch(2, TEST)));
		assertFalse(Instant.ofEpoch(1, TEST).isBefore(Instant.ofEpoch(1, TEST)));
		assertFalse(Instant.ofEpoch(2, TEST).isBefore(Instant.ofEpoch(1, TEST)));
		assertLukashianException(() -> Instant.ofEpoch(1).isBefore(Instant.ofEpoch(2, EARTH)));
	}

	@Test
	public void testIsSameOrBefore() {
		assertTrue(Instant.ofEpoch(1, TEST).isSameOrBefore(Instant.ofEpoch(2, TEST)));
		assertTrue(Instant.ofEpoch(1, TEST).isSameOrBefore(Instant.ofEpoch(1, TEST)));
		assertFalse(Instant.ofEpoch(2, TEST).isSameOrBefore(Instant.ofEpoch(1, TEST)));
		assertLukashianException(() -> Instant.ofEpoch(1).isSameOrBefore(Instant.ofEpoch(2, EARTH)));
	}

	@Test
	public void testIsAfter() {
		assertFalse(Instant.ofEpoch(1, TEST).isAfter(Instant.ofEpoch(2, TEST)));
		assertFalse(Instant.ofEpoch(1, TEST).isAfter(Instant.ofEpoch(1, TEST)));
		assertTrue(Instant.ofEpoch(2, TEST).isAfter(Instant.ofEpoch(1, TEST)));
		assertLukashianException(() -> Instant.ofEpoch(1).isAfter(Instant.ofEpoch(2, EARTH)));
	}

	@Test
	public void testIsSameOrAfter() {
		assertFalse(Instant.ofEpoch(1, TEST).isSameOrAfter(Instant.ofEpoch(2, TEST)));
		assertTrue(Instant.ofEpoch(1, TEST).isSameOrAfter(Instant.ofEpoch(1, TEST)));
		assertTrue(Instant.ofEpoch(2, TEST).isSameOrAfter(Instant.ofEpoch(1, TEST)));
		assertLukashianException(() -> Instant.ofEpoch(1).isSameOrAfter(Instant.ofEpoch(2, EARTH)));
	}

	@Test
	public void testIsIn() {
		assertTrue(Instant.ofEpoch(1, TEST).isIn(Year.of(1)));
		assertTrue(Instant.ofEpoch(500, TEST).isIn(Year.of(1)));
		assertTrue(Instant.ofEpoch(1000, TEST).isIn(Year.of(1)));
		assertFalse(Instant.ofEpoch(1001, TEST).isIn(Year.of(1)));
		assertFalse(Instant.ofEpoch(1500, TEST).isIn(Year.of(1)));

		assertTrue(Instant.ofEpoch(1, TEST).isIn(Day.ofEpoch(1, TEST)));
		assertTrue(Instant.ofEpoch(150, TEST).isIn(Day.ofEpoch(1, TEST)));
		assertTrue(Instant.ofEpoch(300, TEST).isIn(Day.ofEpoch(1, TEST)));
		assertFalse(Instant.ofEpoch(301, TEST).isIn(Day.ofEpoch(1, TEST)));
		assertFalse(Instant.ofEpoch(450, TEST).isIn(Day.ofEpoch(1, TEST)));

		assertLukashianException(() -> Instant.ofEpoch(1).isIn(Day.ofEpoch(2, EARTH)));
		assertLukashianException(() -> Instant.ofEpoch(1).isIn(Year.of(2, EARTH)));
	}

	@Test
	public void testIsNotIn() {
		assertFalse(Instant.ofEpoch(1, TEST).isNotIn(Year.of(1)));
		assertFalse(Instant.ofEpoch(500, TEST).isNotIn(Year.of(1)));
		assertFalse(Instant.ofEpoch(1000, TEST).isNotIn(Year.of(1)));
		assertTrue(Instant.ofEpoch(1001, TEST).isNotIn(Year.of(1)));
		assertTrue(Instant.ofEpoch(1500, TEST).isNotIn(Year.of(1)));

		assertFalse(Instant.ofEpoch(1, TEST).isNotIn(Day.ofEpoch(1, TEST)));
		assertFalse(Instant.ofEpoch(150, TEST).isNotIn(Day.ofEpoch(1, TEST)));
		assertFalse(Instant.ofEpoch(300, TEST).isNotIn(Day.ofEpoch(1, TEST)));
		assertTrue(Instant.ofEpoch(301, TEST).isNotIn(Day.ofEpoch(1, TEST)));
		assertTrue(Instant.ofEpoch(450, TEST).isNotIn(Day.ofEpoch(1, TEST)));

		assertLukashianException(() -> Instant.ofEpoch(1).isNotIn(Day.ofEpoch(2, EARTH)));
		assertLukashianException(() -> Instant.ofEpoch(1).isNotIn(Year.of(2, EARTH)));
	}

	@Test
	public void testGetEpochMilliseconds() {
		assertEquals(1, Instant.ofEpoch(1, TEST).getEpochMilliseconds());
		assertEquals(10000, Instant.ofEpoch(10000, TEST).getEpochMilliseconds());
		assertEquals(39000, Instant.ofEpoch(39000, TEST).getEpochMilliseconds());
	}

	@Test
	public void testGetMillisecond() {
		assertEquals(1, Instant.ofEpoch(1, TEST).getMillisecond());
		assertEquals(10000, Instant.ofEpoch(10000, TEST).getMillisecond());
		assertEquals(39000, Instant.ofEpoch(39000, TEST).getMillisecond());
	}

	@Test
	public void testGetDay() {
		assertDay(1, TEST, Instant.ofEpoch(1, TEST).getDay());
		assertDay(1, TEST, Instant.ofEpoch(150, TEST).getDay());
		assertDay(1, TEST, Instant.ofEpoch(300, TEST).getDay());
		assertDay(2, TEST, Instant.ofEpoch(301, TEST).getDay());

		assertDay(15, TEST, Instant.ofEpoch(4498, TEST).getDay());
		assertDay(15, TEST, Instant.ofEpoch(4499, TEST).getDay());
		assertDay(15, TEST, Instant.ofEpoch(4500, TEST).getDay());
		assertDay(16, TEST, Instant.ofEpoch(4501, TEST).getDay());

		assertDay(16, TEST, Instant.ofEpoch(4798, TEST).getDay());
		assertDay(16, TEST, Instant.ofEpoch(4799, TEST).getDay());
		assertDay(17, TEST, Instant.ofEpoch(4800, TEST).getDay());
		assertDay(17, TEST, Instant.ofEpoch(4801, TEST).getDay());

		assertDay(17, TEST, Instant.ofEpoch(4899, TEST).getDay());
		assertDay(17, TEST, Instant.ofEpoch(4900, TEST).getDay());
		assertDay(18, TEST, Instant.ofEpoch(4901, TEST).getDay());

		assertDay(18, TEST, Instant.ofEpoch(38999, TEST).getDay());
		assertDay(18, TEST, Instant.ofEpoch(39000, TEST).getDay());
	}

	@Test
	public void testGetYear() {
		assertYear(1, TEST, Instant.ofEpoch(1, TEST).getYear());
		assertYear(1, TEST, Instant.ofEpoch(999, TEST).getYear());
		assertYear(1, TEST, Instant.ofEpoch(1000, TEST).getYear());
		assertYear(2, TEST, Instant.ofEpoch(1001, TEST).getYear());

		assertYear(3, TEST, Instant.ofEpoch(2999, TEST).getYear());
		assertYear(3, TEST, Instant.ofEpoch(3000, TEST).getYear());
		assertYear(4, TEST, Instant.ofEpoch(3001, TEST).getYear());

		assertYear(5, TEST, Instant.ofEpoch(4498, TEST).getYear());
		assertYear(5, TEST, Instant.ofEpoch(4499, TEST).getYear());
		assertYear(6, TEST, Instant.ofEpoch(4500, TEST).getYear());
		assertYear(6, TEST, Instant.ofEpoch(4501, TEST).getYear());

		assertYear(6, TEST, Instant.ofEpoch(4798, TEST).getYear());
		assertYear(6, TEST, Instant.ofEpoch(4799, TEST).getYear());
		assertYear(6, TEST, Instant.ofEpoch(4800, TEST).getYear());
		assertYear(7, TEST, Instant.ofEpoch(4801, TEST).getYear());

		assertYear(8, TEST, Instant.ofEpoch(38999, TEST).getYear());
		assertYear(8, TEST, Instant.ofEpoch(39000, TEST).getYear());

		assertLukashianException(() -> Instant.ofEpoch(39001, TEST).getYear());
	}

	@Test
	public void testGetProportionOfDay() {
		assertEquals(BigFraction.of(0), Instant.ofEpoch(1, TEST).getProportionOfDay());
		assertEquals(BigFraction.of(5, 10), Instant.ofEpoch(151, TEST).getProportionOfDay());
		assertEquals(BigFraction.of(149, 150), Instant.ofEpoch(299, TEST).getProportionOfDay());
		assertEquals(BigFraction.of(299, 300), Instant.ofEpoch(300, TEST).getProportionOfDay());

		assertEquals(BigFraction.of(0), Instant.ofEpoch(301, TEST).getProportionOfDay());
		assertEquals(BigFraction.of(5, 10), Instant.ofEpoch(451, TEST).getProportionOfDay());
		assertEquals(BigFraction.of(149, 150), Instant.ofEpoch(599, TEST).getProportionOfDay());
		assertEquals(BigFraction.of(299, 300), Instant.ofEpoch(600, TEST).getProportionOfDay());

		assertEquals(BigFraction.of(0), Instant.ofEpoch(601, TEST).getProportionOfDay());
	}

	@Test
	public void testGetBeeps() {
		assertEquals(0, Instant.ofEpoch(1, TEST).getBeeps());
		assertEquals(5000, Instant.ofEpoch(151, TEST).getBeeps());
		assertEquals(9933, Instant.ofEpoch(299, TEST).getBeeps());
		assertEquals(9966, Instant.ofEpoch(300, TEST).getBeeps());

		assertEquals(0, Instant.ofEpoch(301, TEST).getBeeps());
		assertEquals(5000, Instant.ofEpoch(451, TEST).getBeeps());
		assertEquals(9933, Instant.ofEpoch(599, TEST).getBeeps());
		assertEquals(9966, Instant.ofEpoch(600, TEST).getBeeps());

		assertEquals(0, Instant.ofEpoch(601, TEST).getBeeps());
	}

	@Test
	public void testGetUnixEpochMilliseconds() {
		long currentTimeMillis = System.currentTimeMillis();
		assertEquals(currentTimeMillis, Instant.ofUnixEpochMilliseconds(currentTimeMillis).getUnixEpochMilliseconds());
	}

	@Test
	public void testToJavaInstant() {
		long currentTimeMillis = System.currentTimeMillis();
		assertEquals(
			java.time.Instant.ofEpochMilli(currentTimeMillis),
			Instant.ofUnixEpochMilliseconds(currentTimeMillis).toJavaInstant()
		);
	}

	@Test
	public void testToCalendar() {
		MillisecondStoreDataProvider oneSecondBehind = new TestMillisecondStoreDataProvider() {
			@Override
			public long loadUnixEpochOffsetMilliseconds() {
				try {
					MillisecondStoreData testData = MillisecondStore.data(TEST);
					Field f = testData.getClass().getDeclaredField("unixEpochOffsetMilliseconds");
					f.setAccessible(true);
					long unixEpochOffsetMilliseconds = (long) f.get(testData);

					return unixEpochOffsetMilliseconds + 1000;
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		};
		MillisecondStore.store().registerProvider(TEST + 1, oneSecondBehind);

		Instant test = Instant.now(TEST);
		Instant oneSecondBehindInstant = test.toCalendar(TEST + 1);

		assertEquals(test.getEpochMilliseconds() + 1000, oneSecondBehindInstant.getEpochMilliseconds());
		assertEquals(TEST + 1, oneSecondBehindInstant.getCalendarKey());
	}

	@Test
	public void testDifferenceWith() {
		assertEquals(0, Instant.ofEpoch(1, TEST).differenceWith(Instant.ofEpoch(1, TEST)));
		assertEquals(-1, Instant.ofEpoch(1, TEST).differenceWith(Instant.ofEpoch(2, TEST)));
		assertEquals(-2, Instant.ofEpoch(1, TEST).differenceWith(Instant.ofEpoch(3, TEST)));
		assertEquals(-3, Instant.ofEpoch(1, TEST).differenceWith(Instant.ofEpoch(4, TEST)));
		assertEquals(3, Instant.ofEpoch(4, TEST).differenceWith(Instant.ofEpoch(1, TEST)));
		assertEquals(2, Instant.ofEpoch(4, TEST).differenceWith(Instant.ofEpoch(2, TEST)));
		assertEquals(1, Instant.ofEpoch(4, TEST).differenceWith(Instant.ofEpoch(3, TEST)));
		assertEquals(0, Instant.ofEpoch(4, TEST).differenceWith(Instant.ofEpoch(4, TEST)));

		assertLukashianException(() -> Instant.ofEpoch(4, TEST).differenceWith(Instant.ofEpoch(4, EARTH)));
		assertLukashianException(() -> Instant.ofEpoch(4, TEST).differenceInBeepsWith(Instant.ofEpoch(4, EARTH)));
	}

	@Test
	public void testOf() {
		assertLukashianException(() -> Instant.ofEpoch(0, TEST));
		assertLukashianException(() -> Instant.of(Day.ofEpoch(2, TEST), BigFraction.of(-1)));
		assertLukashianException(() -> Instant.of(Day.ofEpoch(2, TEST), BigFraction.of(1)));
		assertLukashianException(() -> Instant.of(Day.ofEpoch(2, TEST), BigFraction.of(2)));

		assertLukashianException(() -> Instant.ofEpoch(0, TEST));
		assertLukashianException(() -> Instant.of(Day.ofEpoch(2, TEST), -1));
		assertLukashianException(() -> Instant.of(Day.ofEpoch(2, TEST), 10000));
		assertLukashianException(() -> Instant.of(Day.ofEpoch(2, TEST), 20000));

		assertInstant(1, TEST, Instant.of(Day.ofEpoch(1, TEST), BigFraction.of(0)));
		assertInstant(4, TEST, Instant.of(Day.ofEpoch(1, TEST), BigFraction.of(1, 100)));
		assertInstant(151, TEST, Instant.of(Day.ofEpoch(1, TEST), BigFraction.of(5, 10)));
		assertInstant(298, TEST, Instant.of(Day.ofEpoch(1, TEST), BigFraction.of(99, 100)));
		assertInstant(300, TEST, Instant.of(Day.ofEpoch(1, TEST), BigFraction.of(997, 1000)));
		assertInstant(300, TEST, Instant.of(Day.ofEpoch(1, TEST), BigFraction.of(999999999, 1000000000)));

		assertInstant(1, TEST, Instant.of(Day.ofEpoch(1, TEST), 0));
		assertInstant(4, TEST, Instant.of(Day.ofEpoch(1, TEST), 100));
		assertInstant(151, TEST, Instant.of(Day.ofEpoch(1, TEST), 5000));
		assertInstant(298, TEST, Instant.of(Day.ofEpoch(1, TEST), 9900));
		assertInstant(300, TEST, Instant.of(Day.ofEpoch(1, TEST), 9970));
		assertInstant(300, TEST, Instant.of(Day.ofEpoch(1, TEST), 9999));

		assertInstant(301, TEST, Instant.of(Day.ofEpoch(2, TEST), BigFraction.of(0)));
		assertInstant(304, TEST, Instant.of(Day.ofEpoch(2, TEST), BigFraction.of(1, 100)));
		assertInstant(451, TEST, Instant.of(Day.ofEpoch(2, TEST), BigFraction.of(5, 10)));
		assertInstant(598, TEST, Instant.of(Day.ofEpoch(2, TEST), BigFraction.of(99, 100)));
		assertInstant(600, TEST, Instant.of(Day.ofEpoch(2, TEST), BigFraction.of(997, 1000)));
		assertInstant(600, TEST, Instant.of(Day.ofEpoch(2, TEST), BigFraction.of(999999999, 1000000000)));

		assertInstant(301, TEST, Instant.of(Day.ofEpoch(2, TEST), 0));
		assertInstant(304, TEST, Instant.of(Day.ofEpoch(2, TEST), 100));
		assertInstant(451, TEST, Instant.of(Day.ofEpoch(2, TEST), 5000));
		assertInstant(598, TEST, Instant.of(Day.ofEpoch(2, TEST), 9900));
		assertInstant(600, TEST, Instant.of(Day.ofEpoch(2, TEST), 9970));
		assertInstant(600, TEST, Instant.of(Day.ofEpoch(2, TEST), 9999));

		assertInstant(151, TEST, Instant.of(Year.of(1), 1, BigFraction.of(5, 10)));
		assertInstant(151, TEST, Instant.of(1, 1, BigFraction.of(5, 10)));

		assertInstant(151, TEST, Instant.of(Year.of(1), 1, 5000));
		assertInstant(151, TEST, Instant.of(1, 1, 5000));

		assertInstant(1, EARTH, Instant.ofEpoch(1, EARTH));
		assertInstant(1, EARTH, Instant.of(1, 1, BigFraction.ZERO, EARTH));
		assertInstant(1, EARTH, Instant.of(1, 1, 0, EARTH));

		assertEquals(EARTH, Instant.ofJavaInstant(java.time.Instant.now(), EARTH).getCalendarKey());
	}

	@Test
	public void testOfUnixEpochMilliseconds() {
		assertNotNull(Instant.ofUnixEpochMilliseconds(System.currentTimeMillis()));
	}

	@Test
	public void testOfJavaInstant() {
		assertNotNull(Instant.ofJavaInstant(java.time.Instant.now()));
	}

	@Test
	public void testNow() {
		assertEquals(TEST, Instant.now().getCalendarKey());
		assertEquals(EARTH, Instant.now(EARTH).getCalendarKey());
	}

	@Test
	public void testCompareTo() {
		assertEquals(0, Instant.ofEpoch(1, TEST).compareTo(Instant.ofEpoch(1, TEST)));
		assertEquals(-1, Instant.ofEpoch(1, TEST).compareTo(Instant.ofEpoch(2, TEST)));
		assertEquals(-1, Instant.ofEpoch(1, TEST).compareTo(Instant.ofEpoch(3, TEST)));
		assertEquals(-1, Instant.ofEpoch(1, TEST).compareTo(Instant.ofEpoch(4, TEST)));
		assertEquals(1, Instant.ofEpoch(4, TEST).compareTo(Instant.ofEpoch(1, TEST)));
		assertEquals(1, Instant.ofEpoch(4, TEST).compareTo(Instant.ofEpoch(2, TEST)));
		assertEquals(1, Instant.ofEpoch(4, TEST).compareTo(Instant.ofEpoch(3, TEST)));
		assertEquals(0, Instant.ofEpoch(4, TEST).compareTo(Instant.ofEpoch(4, TEST)));
		assertLukashianException(() -> Instant.ofEpoch(8).compareTo(Instant.ofEpoch(4, EARTH)));
	}

	@Test
	public void testHashCode() {
		assertEquals(Instant.ofEpoch(2, TEST).hashCode(), Instant.ofEpoch(2, TEST).hashCode());
		assertNotEquals(Instant.ofEpoch(2, TEST).hashCode(), Instant.ofEpoch(2, EARTH).hashCode());
		assertNotEquals(Instant.ofEpoch(2, TEST).hashCode(), Instant.ofEpoch(3, TEST).hashCode());
	}

	@SuppressWarnings({"unlikely-arg-type", "SimplifiableAssertion", "EqualsBetweenInconvertibleTypes"})
	@Test
	public void testEquals() {
		assertTrue(Instant.ofEpoch(1, TEST).equals(Instant.ofEpoch(1, TEST)));
		assertFalse(Instant.ofEpoch(1, TEST).equals(Instant.ofEpoch(1, EARTH)));
		assertFalse(Instant.ofEpoch(1, TEST).equals(Instant.ofEpoch(2, TEST)));
		assertFalse(Instant.ofEpoch(1, TEST).equals(Year.of(1)));

		Instant i1 = Instant.of(1, 1, BigFraction.of(600000000, 1000000000));
		Instant i2 = Instant.of(1, 1, BigFraction.of(600000000, 1000000000));
		assertTrue(i1.equals(i2));

		i1 = Instant.of(1, 1, BigFraction.of(600000000, 1000000000));
		i2 = Instant.of(1, 1, BigFraction.of(600000001, 1000000000));
		assertTrue(i1.equals(i2)); //equals looks at exact millisecond, not proportion
	}

	@Test
	public void testToString() {
		assertEquals("[Instant: 1-1 0033]", Instant.ofEpoch(2, TEST).toString());
	}
}
