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
package org.lukashian;

import org.apache.commons.numbers.fraction.BigFraction;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.lukashian.store.MillisecondStore;
import org.lukashian.store.TestMillisecondStoreDataProvider;

import static org.junit.jupiter.api.Assertions.*;
import static org.lukashian.LukashianAssert.*;
import static org.lukashian.store.CalendarKeys.EARTH;
import static org.lukashian.store.TestMillisecondStoreDataProvider.TEST;

/**
 * Unit tests for the {@link Day} class.
 */
public class DayTest {

	@BeforeAll
	public static void setUp() {
		MillisecondStore.store().registerProvider(TEST, new TestMillisecondStoreDataProvider());
		MillisecondStore.store().setDefaultCalendarKey(TEST);
	}

	@Test
	public void testMinusYears() {
		Day day = Day.of(4, 4);

		assertLukashianException(() -> day.minusYears(1));
		assertLukashianException(() -> day.minusYears(4));
		assertDay(4, TEST, day.minusYears(3));
	}

	@Test
	public void testPlusYears() {
		Day day = Day.of(1, 4);

		assertLukashianException(() -> day.plusYears(1));
		assertDay(14, TEST, day.plusYears(3));
	}

	@Test
	public void testMinusDays() {
		Day day = Day.ofEpoch(2, TEST);

		assertLukashianException(() -> day.minusDays(3));
		assertLukashianException(() -> day.minusDays(2));
		assertDay(1, TEST, day.minusDays(1));
		assertDay(2, TEST, day.minusDays(0));
		assertDay(3, TEST, day.minusDays(-1));
		assertDay(4, TEST, day.minusDays(-2));
		assertLukashianException(() -> day.minusDays(-17));
	}

	@Test
	public void testPlusDays() {
		Day day = Day.ofEpoch(2, TEST);

		assertLukashianException(() -> day.plusDays(17));
		assertDay(4, TEST, day.plusDays(2));
		assertDay(3, TEST, day.plusDays(1));
		assertDay(2, TEST, day.plusDays(0));
		assertDay(1, TEST, day.plusDays(-1));
		assertLukashianException(() -> day.plusDays(-2));
		assertLukashianException(() -> day.plusDays(-3));
	}

	@Test
	public void testPrevious() {
		assertDay(1, TEST, Day.ofEpoch(2, TEST).previous());
	}

	@Test
	public void testNext() {
		assertDay(3, TEST, Day.ofEpoch(2, TEST).next());
	}

	@Test
	public void testAtTime() {
		assertLukashianException(() -> Day.ofEpoch(1, TEST).atTime(BigFraction.of(-1)));
		assertLukashianException(() -> Day.ofEpoch(1, TEST).atTime(BigFraction.of(1)));
		assertLukashianException(() -> Day.ofEpoch(1, TEST).atTime(BigFraction.of(2)));

		assertLukashianException(() -> Day.ofEpoch(1, TEST).atTime(-1));
		assertLukashianException(() -> Day.ofEpoch(1, TEST).atTime(10000));
		assertLukashianException(() -> Day.ofEpoch(1, TEST).atTime(20000));

		assertInstant(1, TEST, Day.ofEpoch(1, TEST).atTime(BigFraction.ZERO));
		assertInstant(151, TEST, Day.ofEpoch(1, TEST).atTime(BigFraction.of(5, 10)));
		assertInstant(300, TEST, Day.ofEpoch(1, TEST).atTime(BigFraction.of(999999999, 1000000000)));

		assertInstant(1, TEST, Day.ofEpoch(1, TEST).atTime(0));
		assertInstant(151, TEST, Day.ofEpoch(1, TEST).atTime(5000));
		assertInstant(300, TEST, Day.ofEpoch(1, TEST).atTime(9999));
	}

	@Test
	public void testFirstInstant() {
		assertInstant(1, TEST, Day.ofEpoch(1, TEST).firstInstant());
		assertInstant(301, TEST, Day.ofEpoch(2, TEST).firstInstant());

		assertDay(1, TEST, Day.ofEpoch(1, TEST).firstInstant().getDay());
		assertDay(2, TEST, Day.ofEpoch(2, TEST).firstInstant().getDay());
	}

	@Test
	public void testLastInstant() {
		assertInstant(300, TEST, Day.ofEpoch(1, TEST).lastInstant());
		assertInstant(600, TEST, Day.ofEpoch(2, TEST).lastInstant());

		assertDay(1, TEST, Day.ofEpoch(1, TEST).lastInstant().getDay());
		assertDay(2, TEST, Day.ofEpoch(2, TEST).lastInstant().getDay());
	}

	@Test
	public void testIsBefore() {
		assertTrue(Day.ofEpoch(1, TEST).isBefore(Day.ofEpoch(2, TEST)));
		assertTrue(Day.ofEpoch(1, TEST).isBefore(Day.ofEpoch(5, TEST)));
		assertFalse(Day.ofEpoch(2, TEST).isBefore(Day.ofEpoch(1, TEST)));
		assertFalse(Day.ofEpoch(5, TEST).isBefore(Day.ofEpoch(1, TEST)));
		assertFalse(Day.ofEpoch(1, TEST).isBefore(Day.ofEpoch(1, TEST)));
		assertLukashianException(() -> Day.ofEpoch(1).isBefore(Day.ofEpoch(2, EARTH)));
	}

	@Test
	public void testIsSameOrBefore() {
		assertTrue(Day.ofEpoch(1, TEST).isSameOrBefore(Day.ofEpoch(2, TEST)));
		assertTrue(Day.ofEpoch(1, TEST).isSameOrBefore(Day.ofEpoch(5, TEST)));
		assertFalse(Day.ofEpoch(2, TEST).isSameOrBefore(Day.ofEpoch(1, TEST)));
		assertFalse(Day.ofEpoch(5, TEST).isSameOrBefore(Day.ofEpoch(1, TEST)));
		assertTrue(Day.ofEpoch(1, TEST).isSameOrBefore(Day.ofEpoch(1, TEST)));
		assertLukashianException(() -> Day.ofEpoch(1).isSameOrBefore(Day.ofEpoch(2, EARTH)));
	}

	@Test
	public void testIsAfter() {
		assertFalse(Day.ofEpoch(1, TEST).isAfter(Day.ofEpoch(2, TEST)));
		assertFalse(Day.ofEpoch(1, TEST).isAfter(Day.ofEpoch(5, TEST)));
		assertTrue(Day.ofEpoch(2, TEST).isAfter(Day.ofEpoch(1, TEST)));
		assertTrue(Day.ofEpoch(5, TEST).isAfter(Day.ofEpoch(1, TEST)));
		assertFalse(Day.ofEpoch(1, TEST).isAfter(Day.ofEpoch(1, TEST)));
		assertLukashianException(() -> Day.ofEpoch(1).isAfter(Day.ofEpoch(2, EARTH)));
	}

	@Test
	public void testIsSameOrAfter() {
		assertFalse(Day.ofEpoch(1, TEST).isSameOrAfter(Day.ofEpoch(2, TEST)));
		assertFalse(Day.ofEpoch(1, TEST).isSameOrAfter(Day.ofEpoch(5, TEST)));
		assertTrue(Day.ofEpoch(2, TEST).isSameOrAfter(Day.ofEpoch(1, TEST)));
		assertTrue(Day.ofEpoch(5, TEST).isSameOrAfter(Day.ofEpoch(1, TEST)));
		assertTrue(Day.ofEpoch(1, TEST).isSameOrAfter(Day.ofEpoch(1, TEST)));
		assertLukashianException(() -> Day.ofEpoch(1).isSameOrAfter(Day.ofEpoch(2, EARTH)));
	}

	@Test
	public void testContains() {
		Day day = Day.ofEpoch(2, TEST);

		assertTrue(day.contains(day.firstInstant()));
		assertTrue(day.contains(day.lastInstant()));

		assertTrue(Day.ofEpoch(1, TEST).contains(Instant.ofEpoch(1, TEST)));
		assertTrue(Day.ofEpoch(1, TEST).contains(Instant.ofEpoch(300, TEST)));
		assertFalse(Day.ofEpoch(1, TEST).contains(Instant.ofEpoch(301, TEST)));

		assertFalse(Day.ofEpoch(18, TEST).contains(Instant.ofEpoch(4899, TEST)));
		assertFalse(Day.ofEpoch(18, TEST).contains(Instant.ofEpoch(4900, TEST)));
		assertTrue(Day.ofEpoch(18, TEST).contains(Instant.ofEpoch(39000, TEST)));

		assertLukashianException(() -> Day.ofEpoch(1).contains(Instant.ofEpoch(2, EARTH)));
	}

	@Test
	public void testContainsNot() {
		Day day = Day.ofEpoch(2, TEST);

		assertFalse(day.containsNot(day.firstInstant()));
		assertFalse(day.containsNot(day.lastInstant()));

		assertFalse(Day.ofEpoch(1, TEST).containsNot(Instant.ofEpoch(1, TEST)));
		assertFalse(Day.ofEpoch(1, TEST).containsNot(Instant.ofEpoch(300, TEST)));
		assertTrue(Day.ofEpoch(1, TEST).containsNot(Instant.ofEpoch(301, TEST)));

		assertTrue(Day.ofEpoch(18, TEST).containsNot(Instant.ofEpoch(4899, TEST)));
		assertTrue(Day.ofEpoch(18, TEST).containsNot(Instant.ofEpoch(4900, TEST)));
		assertFalse(Day.ofEpoch(18, TEST).containsNot(Instant.ofEpoch(39000, TEST)));

		assertLukashianException(() -> Day.ofEpoch(1).containsNot(Instant.ofEpoch(2, EARTH)));
	}

	@Test
	public void testIsIn() {
		assertTrue(Day.ofEpoch(1, TEST).isIn(Year.of(1)));
		assertTrue(Day.ofEpoch(4, TEST).isIn(Year.of(1)));
		assertFalse(Day.ofEpoch(5, TEST).isIn(Year.of(1)));

		assertLukashianException(() -> Day.ofEpoch(1).isIn(Year.of(2, EARTH)));
	}

	@Test
	public void testIsNotIn() {
		assertFalse(Day.ofEpoch(1, TEST).isNotIn(Year.of(1)));
		assertFalse(Day.ofEpoch(4, TEST).isNotIn(Year.of(1)));
		assertTrue(Day.ofEpoch(5, TEST).isNotIn(Year.of(1)));

		assertLukashianException(() -> Day.ofEpoch(1).isNotIn(Year.of(2, EARTH)));
	}

	@Test
	public void testLenghtInMilliseconds() {
		assertEquals(300, Day.ofEpoch(1, TEST).lengthInMilliseconds());
		assertEquals(300, Day.ofEpoch(2, TEST).lengthInMilliseconds());
		assertEquals(300, Day.ofEpoch(3, TEST).lengthInMilliseconds());
		assertEquals(300, Day.ofEpoch(4, TEST).lengthInMilliseconds());
		assertEquals(300, Day.ofEpoch(5, TEST).lengthInMilliseconds());
		assertEquals(300, Day.ofEpoch(6, TEST).lengthInMilliseconds());
		assertEquals(300, Day.ofEpoch(7, TEST).lengthInMilliseconds());
		assertEquals(300, Day.ofEpoch(8, TEST).lengthInMilliseconds());
		assertEquals(300, Day.ofEpoch(9, TEST).lengthInMilliseconds());
		assertEquals(300, Day.ofEpoch(10, TEST).lengthInMilliseconds());
		assertEquals(300, Day.ofEpoch(11, TEST).lengthInMilliseconds());
		assertEquals(300, Day.ofEpoch(12, TEST).lengthInMilliseconds());
		assertEquals(300, Day.ofEpoch(13, TEST).lengthInMilliseconds());
		assertEquals(300, Day.ofEpoch(14, TEST).lengthInMilliseconds());
		assertEquals(300, Day.ofEpoch(15, TEST).lengthInMilliseconds());
		assertEquals(299, Day.ofEpoch(16, TEST).lengthInMilliseconds());
		assertEquals(101, Day.ofEpoch(17, TEST).lengthInMilliseconds());
		assertEquals(34100, Day.ofEpoch(18, TEST).lengthInMilliseconds());
	}

	@Test
	public void testGetEpochDay() {
		assertEquals(1, Day.ofEpoch(1, TEST).getEpochDay());
		assertEquals(2, Day.ofEpoch(2, TEST).getEpochDay());
		assertEquals(3, Day.ofEpoch(3, TEST).getEpochDay());
		assertEquals(4, Day.ofEpoch(4, TEST).getEpochDay());
		assertEquals(5, Day.ofEpoch(5, TEST).getEpochDay());
		assertEquals(6, Day.ofEpoch(6, TEST).getEpochDay());
		assertEquals(7, Day.ofEpoch(7, TEST).getEpochDay());
		assertEquals(8, Day.ofEpoch(8, TEST).getEpochDay());
		assertEquals(9, Day.ofEpoch(9, TEST).getEpochDay());
		assertEquals(10, Day.ofEpoch(10, TEST).getEpochDay());
		assertEquals(11, Day.ofEpoch(11, TEST).getEpochDay());
		assertEquals(12, Day.ofEpoch(12, TEST).getEpochDay());
		assertEquals(13, Day.ofEpoch(13, TEST).getEpochDay());
		assertEquals(14, Day.ofEpoch(14, TEST).getEpochDay());
		assertEquals(15, Day.ofEpoch(15, TEST).getEpochDay());
		assertEquals(16, Day.ofEpoch(16, TEST).getEpochDay());
		assertEquals(17, Day.ofEpoch(17, TEST).getEpochDay());
		assertEquals(18, Day.ofEpoch(18, TEST).getEpochDay());
	}

	@Test
	public void testGetEpochMilliseconds() {
		assertEquals(300, Day.ofEpoch(1, TEST).getEpochMilliseconds());
		assertEquals(600, Day.ofEpoch(2, TEST).getEpochMilliseconds());
		assertEquals(900, Day.ofEpoch(3, TEST).getEpochMilliseconds());
		assertEquals(1200, Day.ofEpoch(4, TEST).getEpochMilliseconds());
		assertEquals(1500, Day.ofEpoch(5, TEST).getEpochMilliseconds());
		assertEquals(1800, Day.ofEpoch(6, TEST).getEpochMilliseconds());
		assertEquals(2100, Day.ofEpoch(7, TEST).getEpochMilliseconds());
		assertEquals(2400, Day.ofEpoch(8, TEST).getEpochMilliseconds());
		assertEquals(2700, Day.ofEpoch(9, TEST).getEpochMilliseconds());
		assertEquals(3000, Day.ofEpoch(10, TEST).getEpochMilliseconds());
		assertEquals(3300, Day.ofEpoch(11, TEST).getEpochMilliseconds());
		assertEquals(3600, Day.ofEpoch(12, TEST).getEpochMilliseconds());
		assertEquals(3900, Day.ofEpoch(13, TEST).getEpochMilliseconds());
		assertEquals(4200, Day.ofEpoch(14, TEST).getEpochMilliseconds());
		assertEquals(4500, Day.ofEpoch(15, TEST).getEpochMilliseconds());
		assertEquals(4799, Day.ofEpoch(16, TEST).getEpochMilliseconds());
		assertEquals(4900, Day.ofEpoch(17, TEST).getEpochMilliseconds());
		assertEquals(39000, Day.ofEpoch(18, TEST).getEpochMilliseconds());
	}

	@Test
	public void testGetEpochMillisecondsPreviousDay() {
		assertEquals(0, Day.ofEpoch(1, TEST).getEpochMillisecondsPreviousDay());
		assertEquals(300, Day.ofEpoch(2, TEST).getEpochMillisecondsPreviousDay());
		assertEquals(600, Day.ofEpoch(3, TEST).getEpochMillisecondsPreviousDay());
		assertEquals(900, Day.ofEpoch(4, TEST).getEpochMillisecondsPreviousDay());
		assertEquals(1200, Day.ofEpoch(5, TEST).getEpochMillisecondsPreviousDay());
		assertEquals(1500, Day.ofEpoch(6, TEST).getEpochMillisecondsPreviousDay());
		assertEquals(1800, Day.ofEpoch(7, TEST).getEpochMillisecondsPreviousDay());
		assertEquals(2100, Day.ofEpoch(8, TEST).getEpochMillisecondsPreviousDay());
		assertEquals(2400, Day.ofEpoch(9, TEST).getEpochMillisecondsPreviousDay());
		assertEquals(2700, Day.ofEpoch(10, TEST).getEpochMillisecondsPreviousDay());
		assertEquals(3000, Day.ofEpoch(11, TEST).getEpochMillisecondsPreviousDay());
		assertEquals(3300, Day.ofEpoch(12, TEST).getEpochMillisecondsPreviousDay());
		assertEquals(3600, Day.ofEpoch(13, TEST).getEpochMillisecondsPreviousDay());
		assertEquals(3900, Day.ofEpoch(14, TEST).getEpochMillisecondsPreviousDay());
		assertEquals(4200, Day.ofEpoch(15, TEST).getEpochMillisecondsPreviousDay());
		assertEquals(4500, Day.ofEpoch(16, TEST).getEpochMillisecondsPreviousDay());
		assertEquals(4799, Day.ofEpoch(17, TEST).getEpochMillisecondsPreviousDay());
		assertEquals(4900, Day.ofEpoch(18, TEST).getEpochMillisecondsPreviousDay());
	}

	@Test
	public void testGetEpochMillisecondsAtStartOfDay() {
		assertEquals(1, Day.ofEpoch(1, TEST).getEpochMillisecondsAtStartOfDay());
		assertEquals(301, Day.ofEpoch(2, TEST).getEpochMillisecondsAtStartOfDay());
		assertEquals(601, Day.ofEpoch(3, TEST).getEpochMillisecondsAtStartOfDay());
		assertEquals(901, Day.ofEpoch(4, TEST).getEpochMillisecondsAtStartOfDay());
		assertEquals(1201, Day.ofEpoch(5, TEST).getEpochMillisecondsAtStartOfDay());
		assertEquals(1501, Day.ofEpoch(6, TEST).getEpochMillisecondsAtStartOfDay());
		assertEquals(1801, Day.ofEpoch(7, TEST).getEpochMillisecondsAtStartOfDay());
		assertEquals(2101, Day.ofEpoch(8, TEST).getEpochMillisecondsAtStartOfDay());
		assertEquals(2401, Day.ofEpoch(9, TEST).getEpochMillisecondsAtStartOfDay());
		assertEquals(2701, Day.ofEpoch(10, TEST).getEpochMillisecondsAtStartOfDay());
		assertEquals(3001, Day.ofEpoch(11, TEST).getEpochMillisecondsAtStartOfDay());
		assertEquals(3301, Day.ofEpoch(12, TEST).getEpochMillisecondsAtStartOfDay());
		assertEquals(3601, Day.ofEpoch(13, TEST).getEpochMillisecondsAtStartOfDay());
		assertEquals(3901, Day.ofEpoch(14, TEST).getEpochMillisecondsAtStartOfDay());
		assertEquals(4201, Day.ofEpoch(15, TEST).getEpochMillisecondsAtStartOfDay());
		assertEquals(4501, Day.ofEpoch(16, TEST).getEpochMillisecondsAtStartOfDay());
		assertEquals(4800, Day.ofEpoch(17, TEST).getEpochMillisecondsAtStartOfDay());
		assertEquals(4901, Day.ofEpoch(18, TEST).getEpochMillisecondsAtStartOfDay());
	}

	@Test
	public void testGetYear() {
		assertYear(1, TEST, Day.ofEpoch(1, TEST).getYear());
		assertYear(1, TEST, Day.ofEpoch(2, TEST).getYear());
		assertYear(1, TEST, Day.ofEpoch(3, TEST).getYear());
		assertYear(1, TEST, Day.ofEpoch(4, TEST).getYear());
		assertYear(2, TEST, Day.ofEpoch(5, TEST).getYear());
		assertYear(2, TEST, Day.ofEpoch(6, TEST).getYear());
		assertYear(2, TEST, Day.ofEpoch(7, TEST).getYear());
		assertYear(3, TEST, Day.ofEpoch(8, TEST).getYear());
		assertYear(3, TEST, Day.ofEpoch(9, TEST).getYear());
		assertYear(3, TEST, Day.ofEpoch(10, TEST).getYear());
		assertYear(4, TEST, Day.ofEpoch(11, TEST).getYear());
		assertYear(4, TEST, Day.ofEpoch(12, TEST).getYear());
		assertYear(4, TEST, Day.ofEpoch(13, TEST).getYear());
		assertYear(4, TEST, Day.ofEpoch(14, TEST).getYear());
		assertYear(5, TEST, Day.ofEpoch(15, TEST).getYear());
		assertYear(6, TEST, Day.ofEpoch(16, TEST).getYear());
		assertYear(6, TEST, Day.ofEpoch(17, TEST).getYear());
		assertYear(7, TEST, Day.ofEpoch(18, TEST).getYear());
	}

	@Test
	public void testGetEndYear() {
		assertYear(1, TEST, Day.ofEpoch(1, TEST).getEndYear());
		assertYear(1, TEST, Day.ofEpoch(2, TEST).getEndYear());
		assertYear(1, TEST, Day.ofEpoch(3, TEST).getEndYear());
		assertYear(2, TEST, Day.ofEpoch(4, TEST).getEndYear());
		assertYear(2, TEST, Day.ofEpoch(5, TEST).getEndYear());
		assertYear(2, TEST, Day.ofEpoch(6, TEST).getEndYear());
		assertYear(3, TEST, Day.ofEpoch(7, TEST).getEndYear());
		assertYear(3, TEST, Day.ofEpoch(8, TEST).getEndYear());
		assertYear(3, TEST, Day.ofEpoch(9, TEST).getEndYear());
		assertYear(3, TEST, Day.ofEpoch(10, TEST).getEndYear());
		assertYear(4, TEST, Day.ofEpoch(11, TEST).getEndYear());
		assertYear(4, TEST, Day.ofEpoch(12, TEST).getEndYear());
		assertYear(4, TEST, Day.ofEpoch(13, TEST).getEndYear());
		assertYear(5, TEST, Day.ofEpoch(14, TEST).getEndYear());
		assertYear(6, TEST, Day.ofEpoch(15, TEST).getEndYear());
		assertYear(6, TEST, Day.ofEpoch(16, TEST).getEndYear());
		assertYear(7, TEST, Day.ofEpoch(17, TEST).getEndYear());
		assertYear(8, TEST, Day.ofEpoch(18, TEST).getEndYear());
	}

	@Test
	public void testGetDayNumber() {
		assertLukashianException(()-> Day.of(null, 1));
		assertLukashianException(() -> Day.of(1, 0));
		assertEquals(1, Day.of(1, 1).getDayNumber());
		assertEquals(2, Day.of(1, 2).getDayNumber());
		assertEquals(3, Day.of(1, 3).getDayNumber());
		assertEquals(4, Day.of(1, 4).getDayNumber());
		assertLukashianException(() -> Day.of(1, 5));
		assertEquals(1, Day.of(2, 1).getDayNumber());
		assertEquals(2, Day.of(2, 2).getDayNumber());
		assertEquals(3, Day.of(2, 3).getDayNumber());
		assertLukashianException(() -> Day.of(2, 4));
		assertEquals(1, Day.of(3, 1).getDayNumber());
		assertEquals(2, Day.of(3, 2).getDayNumber());
		assertEquals(3, Day.of(3, 3).getDayNumber());
		assertLukashianException(() -> Day.of(3, 4));
		assertEquals(1, Day.of(4, 1).getDayNumber());
		assertEquals(2, Day.of(4, 2).getDayNumber());
		assertEquals(3, Day.of(4, 3).getDayNumber());
		assertEquals(4, Day.of(4, 4).getDayNumber());
		assertLukashianException(() -> Day.of(4, 5));
		assertEquals(1, Day.of(5, 1).getDayNumber());
		assertLukashianException(() -> Day.of(5, 2));
		assertEquals(1, Day.of(6, 1).getDayNumber());
		assertEquals(2, Day.of(6, 2).getDayNumber());
		assertLukashianException(() -> Day.of(6, 3));
		assertEquals(1, Day.of(7, 1).getDayNumber());
		assertLukashianException(() -> Day.of(7, 2));
		assertLukashianException(() -> Day.of(8, 1));

		assertLukashianException(()-> Day.ofEpoch(0, TEST));
		assertEquals(1, Day.ofEpoch(1, TEST).getDayNumber());
		assertEquals(2, Day.ofEpoch(2, TEST).getDayNumber());
		assertEquals(3, Day.ofEpoch(3, TEST).getDayNumber());
		assertEquals(4, Day.ofEpoch(4, TEST).getDayNumber());
		assertEquals(1, Day.ofEpoch(5, TEST).getDayNumber());
		assertEquals(2, Day.ofEpoch(6, TEST).getDayNumber());
		assertEquals(3, Day.ofEpoch(7, TEST).getDayNumber());
		assertEquals(1, Day.ofEpoch(8, TEST).getDayNumber());
		assertEquals(2, Day.ofEpoch(9, TEST).getDayNumber());
		assertEquals(3, Day.ofEpoch(10, TEST).getDayNumber());
		assertEquals(1, Day.ofEpoch(11, TEST).getDayNumber());
		assertEquals(2, Day.ofEpoch(12, TEST).getDayNumber());
		assertEquals(3, Day.ofEpoch(13, TEST).getDayNumber());
		assertEquals(4, Day.ofEpoch(14, TEST).getDayNumber());
		assertEquals(1, Day.ofEpoch(15, TEST).getDayNumber());
		assertEquals(1, Day.ofEpoch(16, TEST).getDayNumber());
		assertEquals(2, Day.ofEpoch(17, TEST).getDayNumber());
		assertEquals(1, Day.ofEpoch(18, TEST).getDayNumber());
		assertLukashianException(()-> Day.ofEpoch(19, TEST));
	}

	@Test
	public void testDifferenceWith() {
		assertEquals(0, Day.ofEpoch(1, TEST).differenceWith(Day.ofEpoch(1, TEST)));
		assertEquals(-1, Day.ofEpoch(1, TEST).differenceWith(Day.ofEpoch(2, TEST)));
		assertEquals(-2, Day.ofEpoch(1, TEST).differenceWith(Day.ofEpoch(3, TEST)));
		assertEquals(-3, Day.ofEpoch(1, TEST).differenceWith(Day.ofEpoch(4, TEST)));
		assertEquals(3, Day.ofEpoch(4, TEST).differenceWith(Day.ofEpoch(1, TEST)));
		assertEquals(2, Day.ofEpoch(4, TEST).differenceWith(Day.ofEpoch(2, TEST)));
		assertEquals(1, Day.ofEpoch(4, TEST).differenceWith(Day.ofEpoch(3, TEST)));
		assertEquals(0, Day.ofEpoch(4, TEST).differenceWith(Day.ofEpoch(4, TEST)));
		assertLukashianException(() -> Day.ofEpoch(4, TEST).differenceWith(Day.ofEpoch(4, EARTH)));
	}

	@Test
	public void testOf() {
		assertDay(1, TEST, Day.of(1, 1));
		assertDay(2, TEST, Day.of(1, 2));
		assertDay(3, TEST, Day.of(1, 3));
		assertDay(4, TEST, Day.of(1, 4));
		assertDay(5, TEST, Day.of(2, 1));
		assertDay(6, TEST, Day.of(2, 2));
		assertDay(7, TEST, Day.of(2, 3));
		assertDay(8, TEST, Day.of(3, 1));
		assertDay(9, TEST, Day.of(3, 2));
		assertDay(10, TEST, Day.of(3, 3));
		assertDay(11, TEST, Day.of(4, 1));
		assertDay(12, TEST, Day.of(4, 2));
		assertDay(13, TEST, Day.of(4, 3));
		assertDay(14, TEST, Day.of(4, 4));
		assertDay(15, TEST, Day.of(5, 1));
		assertDay(16, TEST, Day.of(6, 1));
		assertDay(17, TEST, Day.of(6, 2));
		assertDay(18, TEST, Day.of(7, 1));

		assertLukashianException(() -> Day.ofEpoch(0));
		assertLukashianException(() -> Day.of(null, 1));
		assertLukashianException(() -> Day.of(1, 0));
		assertLukashianException(() -> Day.of(1, 5));
		assertLukashianException(() -> Day.of(2, 4));
		assertLukashianException(() -> Day.of(3, 4));
		assertLukashianException(() -> Day.of(4, 5));
		assertLukashianException(() -> Day.of(5, 2));
		assertLukashianException(() -> Day.of(6, 3));
		assertLukashianException(() -> Day.of(7, 2));
		assertLukashianException(() -> Day.of(8, 1));

		assertDay(1, EARTH, Day.ofEpoch(1, EARTH));
		assertDay(1, EARTH, Day.of(1, 1, EARTH));
	}

	@Test
	public void testNow() {
		assertEquals(TEST, Day.now().getCalendarKey());
		assertEquals(EARTH, Day.now(EARTH).getCalendarKey());
	}

	@Test
	public void testCompareTo() {
		assertEquals(0, Day.ofEpoch(1, TEST).compareTo(Day.ofEpoch(1, TEST)));
		assertEquals(-1, Day.ofEpoch(1, TEST).compareTo(Day.ofEpoch(2, TEST)));
		assertEquals(-2, Day.ofEpoch(1, TEST).compareTo(Day.ofEpoch(3, TEST)));
		assertEquals(-3, Day.ofEpoch(1, TEST).compareTo(Day.ofEpoch(4, TEST)));
		assertEquals(3, Day.ofEpoch(4, TEST).compareTo(Day.ofEpoch(1, TEST)));
		assertEquals(2, Day.ofEpoch(4, TEST).compareTo(Day.ofEpoch(2, TEST)));
		assertEquals(1, Day.ofEpoch(4, TEST).compareTo(Day.ofEpoch(3, TEST)));
		assertEquals(0, Day.ofEpoch(4, TEST).compareTo(Day.ofEpoch(4, TEST)));
		assertLukashianException(() -> Day.ofEpoch(8).compareTo(Day.ofEpoch(4, EARTH)));
	}

	@Test
	public void testHashCode() {
		assertEquals(Day.ofEpoch(2, TEST).hashCode(), Day.ofEpoch(2, TEST).hashCode());
		assertNotEquals(Day.ofEpoch(2, TEST).hashCode(), Day.ofEpoch(2, EARTH).hashCode());
		assertNotEquals(Day.ofEpoch(2, TEST).hashCode(), Day.ofEpoch(3, TEST).hashCode());
	}

	@SuppressWarnings({"unlikely-arg-type", "SimplifiableAssertion", "EqualsBetweenInconvertibleTypes"})
	@Test
	public void testEquals() {
		assertTrue(Day.ofEpoch(1, TEST).equals(Day.ofEpoch(1, TEST)));
		assertFalse(Day.ofEpoch(1, TEST).equals(Day.ofEpoch(1, EARTH)));
		assertFalse(Day.ofEpoch(1, TEST).equals(Day.ofEpoch(2, TEST)));
		assertFalse(Day.ofEpoch(1, TEST).equals(Year.of(1)));
	}

	@Test
	public void testToString() {
		assertEquals("[Day: 1-2]", Day.ofEpoch(2, TEST).toString());
	}
}
