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
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.lukashian.LukashianAssert.*;

/**
 * Unit tests for the {@link Day} class.
 */
public class DayTest {

	@Test
	public void testMinusYears() {
		Day day = Day.of(4, 4);

		assertLukashianException(() -> day.minusYears(1));
		assertLukashianException(() -> day.minusYears(4));
		assertDay(4, day.minusYears(3));
	}

	@Test
	public void testPlusYears() {
		Day day = Day.of(1, 4);

		assertLukashianException(() -> day.plusYears(1));
		assertDay(14, day.plusYears(3));
	}

	@Test
	public void testMinusDays() {
		Day day = Day.of(2);

		assertLukashianException(() -> day.minusDays(3));
		assertLukashianException(() -> day.minusDays(2));
		assertDay(1, day.minusDays(1));
		assertDay(2, day.minusDays(0));
		assertDay(3, day.minusDays(-1));
		assertDay(4, day.minusDays(-2));
		assertLukashianException(() -> day.minusDays(-17));
	}

	@Test
	public void testPlusDays() {
		Day day = Day.of(2);

		assertLukashianException(() -> day.plusDays(17));
		assertDay(4, day.plusDays(2));
		assertDay(3, day.plusDays(1));
		assertDay(2, day.plusDays(0));
		assertDay(1, day.plusDays(-1));
		assertLukashianException(() -> day.plusDays(-2));
		assertLukashianException(() -> day.plusDays(-3));
	}

	@Test
	public void testPrevious() {
		assertDay(1, Day.of(2).previous());
	}

	@Test
	public void testNext() {
		assertDay(3, Day.of(2).next());
	}

	@Test
	public void testAtTime() {
		assertLukashianException(() -> Day.of(1).atTime(BigFraction.of(-1)));
		assertLukashianException(() -> Day.of(1).atTime(BigFraction.of(1)));
		assertLukashianException(() -> Day.of(1).atTime(BigFraction.of(2)));

		assertLukashianException(() -> Day.of(1).atTime(-1));
		assertLukashianException(() -> Day.of(1).atTime(10000));
		assertLukashianException(() -> Day.of(1).atTime(20000));

		assertInstant(1, Day.of(1).atTime(BigFraction.ZERO));
		assertInstant(151, Day.of(1).atTime(BigFraction.of(5, 10)));
		assertInstant(300, Day.of(1).atTime(BigFraction.of(999999999, 1000000000)));

		assertInstant(1, Day.of(1).atTime(0));
		assertInstant(151, Day.of(1).atTime(5000));
		assertInstant(300, Day.of(1).atTime(9999));
	}

	@Test
	public void testFirstInstant() {
		assertInstant(1, Day.of(1).firstInstant());
		assertInstant(301, Day.of(2).firstInstant());

		assertDay(1, Day.of(1).firstInstant().getDay());
		assertDay(2, Day.of(2).firstInstant().getDay());
	}

	@Test
	public void testLastInstant() {
		assertInstant(300, Day.of(1).lastInstant());
		assertInstant(600, Day.of(2).lastInstant());

		assertDay(1, Day.of(1).lastInstant().getDay());
		assertDay(2, Day.of(2).lastInstant().getDay());
	}

	@Test
	public void testIsBefore() {
		assertTrue(Day.of(1).isBefore(Day.of(2)));
		assertTrue(Day.of(1).isBefore(Day.of(5)));
		assertFalse(Day.of(2).isBefore(Day.of(1)));
		assertFalse(Day.of(5).isBefore(Day.of(1)));
		assertFalse(Day.of(1).isBefore(Day.of(1)));
	}

	@Test
	public void testIsSameOrBefore() {
		assertTrue(Day.of(1).isSameOrBefore(Day.of(2)));
		assertTrue(Day.of(1).isSameOrBefore(Day.of(5)));
		assertFalse(Day.of(2).isSameOrBefore(Day.of(1)));
		assertFalse(Day.of(5).isSameOrBefore(Day.of(1)));
		assertTrue(Day.of(1).isSameOrBefore(Day.of(1)));
	}

	@Test
	public void testIsAfter() {
		assertFalse(Day.of(1).isAfter(Day.of(2)));
		assertFalse(Day.of(1).isAfter(Day.of(5)));
		assertTrue(Day.of(2).isAfter(Day.of(1)));
		assertTrue(Day.of(5).isAfter(Day.of(1)));
		assertFalse(Day.of(1).isAfter(Day.of(1)));
	}

	@Test
	public void testIsSameOrAfter() {
		assertFalse(Day.of(1).isSameOrAfter(Day.of(2)));
		assertFalse(Day.of(1).isSameOrAfter(Day.of(5)));
		assertTrue(Day.of(2).isSameOrAfter(Day.of(1)));
		assertTrue(Day.of(5).isSameOrAfter(Day.of(1)));
		assertTrue(Day.of(1).isSameOrAfter(Day.of(1)));
	}

	@Test
	public void testContains() {
		Day day = Day.of(2);

		assertTrue(day.contains(day.firstInstant()));
		assertTrue(day.contains(day.lastInstant()));

		assertTrue(Day.of(1).contains(Instant.of(1)));
		assertTrue(Day.of(1).contains(Instant.of(300)));
		assertFalse(Day.of(1).contains(Instant.of(301)));

		assertFalse(Day.of(18).contains(Instant.of(4899)));
		assertFalse(Day.of(18).contains(Instant.of(4900)));
		assertTrue(Day.of(18).contains(Instant.of(39000)));
	}

	@Test
	public void testContainsNot() {
		Day day = Day.of(2);

		assertFalse(day.containsNot(day.firstInstant()));
		assertFalse(day.containsNot(day.lastInstant()));

		assertFalse(Day.of(1).containsNot(Instant.of(1)));
		assertFalse(Day.of(1).containsNot(Instant.of(300)));
		assertTrue(Day.of(1).containsNot(Instant.of(301)));

		assertTrue(Day.of(18).containsNot(Instant.of(4899)));
		assertTrue(Day.of(18).containsNot(Instant.of(4900)));
		assertFalse(Day.of(18).containsNot(Instant.of(39000)));
	}

	@Test
	public void testIsIn() {
		assertTrue(Day.of(1).isIn(Year.of(1)));
		assertTrue(Day.of(4).isIn(Year.of(1)));
		assertFalse(Day.of(5).isIn(Year.of(1)));
	}

	@Test
	public void testIsNotIn() {
		assertFalse(Day.of(1).isNotIn(Year.of(1)));
		assertFalse(Day.of(4).isNotIn(Year.of(1)));
		assertTrue(Day.of(5).isNotIn(Year.of(1)));
	}

	@Test
	public void testLenghtInMilliseconds() {
		assertEquals(300, Day.of(1).lengthInMilliseconds());
		assertEquals(300, Day.of(2).lengthInMilliseconds());
		assertEquals(300, Day.of(3).lengthInMilliseconds());
		assertEquals(300, Day.of(4).lengthInMilliseconds());
		assertEquals(300, Day.of(5).lengthInMilliseconds());
		assertEquals(300, Day.of(6).lengthInMilliseconds());
		assertEquals(300, Day.of(7).lengthInMilliseconds());
		assertEquals(300, Day.of(8).lengthInMilliseconds());
		assertEquals(300, Day.of(9).lengthInMilliseconds());
		assertEquals(300, Day.of(10).lengthInMilliseconds());
		assertEquals(300, Day.of(11).lengthInMilliseconds());
		assertEquals(300, Day.of(12).lengthInMilliseconds());
		assertEquals(300, Day.of(13).lengthInMilliseconds());
		assertEquals(300, Day.of(14).lengthInMilliseconds());
		assertEquals(300, Day.of(15).lengthInMilliseconds());
		assertEquals(299, Day.of(16).lengthInMilliseconds());
		assertEquals(101, Day.of(17).lengthInMilliseconds());
		assertEquals(34100, Day.of(18).lengthInMilliseconds());
	}

	@Test
	public void testGetEpochDay() {
		assertEquals(1, Day.of(1).getEpochDay());
		assertEquals(2, Day.of(2).getEpochDay());
		assertEquals(3, Day.of(3).getEpochDay());
		assertEquals(4, Day.of(4).getEpochDay());
		assertEquals(5, Day.of(5).getEpochDay());
		assertEquals(6, Day.of(6).getEpochDay());
		assertEquals(7, Day.of(7).getEpochDay());
		assertEquals(8, Day.of(8).getEpochDay());
		assertEquals(9, Day.of(9).getEpochDay());
		assertEquals(10, Day.of(10).getEpochDay());
		assertEquals(11, Day.of(11).getEpochDay());
		assertEquals(12, Day.of(12).getEpochDay());
		assertEquals(13, Day.of(13).getEpochDay());
		assertEquals(14, Day.of(14).getEpochDay());
		assertEquals(15, Day.of(15).getEpochDay());
		assertEquals(16, Day.of(16).getEpochDay());
		assertEquals(17, Day.of(17).getEpochDay());
		assertEquals(18, Day.of(18).getEpochDay());
	}

	@Test
	public void testGetEpochMilliseconds() {
		assertEquals(300, Day.of(1).getEpochMilliseconds());
		assertEquals(600, Day.of(2).getEpochMilliseconds());
		assertEquals(900, Day.of(3).getEpochMilliseconds());
		assertEquals(1200, Day.of(4).getEpochMilliseconds());
		assertEquals(1500, Day.of(5).getEpochMilliseconds());
		assertEquals(1800, Day.of(6).getEpochMilliseconds());
		assertEquals(2100, Day.of(7).getEpochMilliseconds());
		assertEquals(2400, Day.of(8).getEpochMilliseconds());
		assertEquals(2700, Day.of(9).getEpochMilliseconds());
		assertEquals(3000, Day.of(10).getEpochMilliseconds());
		assertEquals(3300, Day.of(11).getEpochMilliseconds());
		assertEquals(3600, Day.of(12).getEpochMilliseconds());
		assertEquals(3900, Day.of(13).getEpochMilliseconds());
		assertEquals(4200, Day.of(14).getEpochMilliseconds());
		assertEquals(4500, Day.of(15).getEpochMilliseconds());
		assertEquals(4799, Day.of(16).getEpochMilliseconds());
		assertEquals(4900, Day.of(17).getEpochMilliseconds());
		assertEquals(39000, Day.of(18).getEpochMilliseconds());
	}

	@Test
	public void testGetEpochMillisecondsPreviousDay() {
		assertEquals(0, Day.of(1).getEpochMillisecondsPreviousDay());
		assertEquals(300, Day.of(2).getEpochMillisecondsPreviousDay());
		assertEquals(600, Day.of(3).getEpochMillisecondsPreviousDay());
		assertEquals(900, Day.of(4).getEpochMillisecondsPreviousDay());
		assertEquals(1200, Day.of(5).getEpochMillisecondsPreviousDay());
		assertEquals(1500, Day.of(6).getEpochMillisecondsPreviousDay());
		assertEquals(1800, Day.of(7).getEpochMillisecondsPreviousDay());
		assertEquals(2100, Day.of(8).getEpochMillisecondsPreviousDay());
		assertEquals(2400, Day.of(9).getEpochMillisecondsPreviousDay());
		assertEquals(2700, Day.of(10).getEpochMillisecondsPreviousDay());
		assertEquals(3000, Day.of(11).getEpochMillisecondsPreviousDay());
		assertEquals(3300, Day.of(12).getEpochMillisecondsPreviousDay());
		assertEquals(3600, Day.of(13).getEpochMillisecondsPreviousDay());
		assertEquals(3900, Day.of(14).getEpochMillisecondsPreviousDay());
		assertEquals(4200, Day.of(15).getEpochMillisecondsPreviousDay());
		assertEquals(4500, Day.of(16).getEpochMillisecondsPreviousDay());
		assertEquals(4799, Day.of(17).getEpochMillisecondsPreviousDay());
		assertEquals(4900, Day.of(18).getEpochMillisecondsPreviousDay());
	}

	@Test
	public void testGetEpochMillisecondsAtStartOfDay() {
		assertEquals(1, Day.of(1).getEpochMillisecondsAtStartOfDay());
		assertEquals(301, Day.of(2).getEpochMillisecondsAtStartOfDay());
		assertEquals(601, Day.of(3).getEpochMillisecondsAtStartOfDay());
		assertEquals(901, Day.of(4).getEpochMillisecondsAtStartOfDay());
		assertEquals(1201, Day.of(5).getEpochMillisecondsAtStartOfDay());
		assertEquals(1501, Day.of(6).getEpochMillisecondsAtStartOfDay());
		assertEquals(1801, Day.of(7).getEpochMillisecondsAtStartOfDay());
		assertEquals(2101, Day.of(8).getEpochMillisecondsAtStartOfDay());
		assertEquals(2401, Day.of(9).getEpochMillisecondsAtStartOfDay());
		assertEquals(2701, Day.of(10).getEpochMillisecondsAtStartOfDay());
		assertEquals(3001, Day.of(11).getEpochMillisecondsAtStartOfDay());
		assertEquals(3301, Day.of(12).getEpochMillisecondsAtStartOfDay());
		assertEquals(3601, Day.of(13).getEpochMillisecondsAtStartOfDay());
		assertEquals(3901, Day.of(14).getEpochMillisecondsAtStartOfDay());
		assertEquals(4201, Day.of(15).getEpochMillisecondsAtStartOfDay());
		assertEquals(4501, Day.of(16).getEpochMillisecondsAtStartOfDay());
		assertEquals(4800, Day.of(17).getEpochMillisecondsAtStartOfDay());
		assertEquals(4901, Day.of(18).getEpochMillisecondsAtStartOfDay());
	}

	@Test
	public void testGetYear() {
		assertYear(1, Day.of(1).getYear());
		assertYear(1, Day.of(2).getYear());
		assertYear(1, Day.of(3).getYear());
		assertYear(1, Day.of(4).getYear());
		assertYear(2, Day.of(5).getYear());
		assertYear(2, Day.of(6).getYear());
		assertYear(2, Day.of(7).getYear());
		assertYear(3, Day.of(8).getYear());
		assertYear(3, Day.of(9).getYear());
		assertYear(3, Day.of(10).getYear());
		assertYear(4, Day.of(11).getYear());
		assertYear(4, Day.of(12).getYear());
		assertYear(4, Day.of(13).getYear());
		assertYear(4, Day.of(14).getYear());
		assertYear(5, Day.of(15).getYear());
		assertYear(6, Day.of(16).getYear());
		assertYear(6, Day.of(17).getYear());
		assertYear(7, Day.of(18).getYear());
	}

	@Test
	public void testGetEndYear() {
		assertYear(1, Day.of(1).getEndYear());
		assertYear(1, Day.of(2).getEndYear());
		assertYear(1, Day.of(3).getEndYear());
		assertYear(2, Day.of(4).getEndYear());
		assertYear(2, Day.of(5).getEndYear());
		assertYear(2, Day.of(6).getEndYear());
		assertYear(3, Day.of(7).getEndYear());
		assertYear(3, Day.of(8).getEndYear());
		assertYear(3, Day.of(9).getEndYear());
		assertYear(3, Day.of(10).getEndYear());
		assertYear(4, Day.of(11).getEndYear());
		assertYear(4, Day.of(12).getEndYear());
		assertYear(4, Day.of(13).getEndYear());
		assertYear(5, Day.of(14).getEndYear());
		assertYear(6, Day.of(15).getEndYear());
		assertYear(6, Day.of(16).getEndYear());
		assertYear(7, Day.of(17).getEndYear());
		assertYear(8, Day.of(18).getEndYear());
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

		assertLukashianException(()-> Day.of(0));
		assertEquals(1, Day.of(1).getDayNumber());
		assertEquals(2, Day.of(2).getDayNumber());
		assertEquals(3, Day.of(3).getDayNumber());
		assertEquals(4, Day.of(4).getDayNumber());
		assertEquals(1, Day.of(5).getDayNumber());
		assertEquals(2, Day.of(6).getDayNumber());
		assertEquals(3, Day.of(7).getDayNumber());
		assertEquals(1, Day.of(8).getDayNumber());
		assertEquals(2, Day.of(9).getDayNumber());
		assertEquals(3, Day.of(10).getDayNumber());
		assertEquals(1, Day.of(11).getDayNumber());
		assertEquals(2, Day.of(12).getDayNumber());
		assertEquals(3, Day.of(13).getDayNumber());
		assertEquals(4, Day.of(14).getDayNumber());
		assertEquals(1, Day.of(15).getDayNumber());
		assertEquals(1, Day.of(16).getDayNumber());
		assertEquals(2, Day.of(17).getDayNumber());
		assertEquals(1, Day.of(18).getDayNumber());
		assertLukashianException(()-> Day.of(19));
	}

	@Test
	public void testDifferenceWith() {
		assertEquals(0, Day.of(1).differenceWith(Day.of(1)));
		assertEquals(-1, Day.of(1).differenceWith(Day.of(2)));
		assertEquals(-2, Day.of(1).differenceWith(Day.of(3)));
		assertEquals(-3, Day.of(1).differenceWith(Day.of(4)));
		assertEquals(3, Day.of(4).differenceWith(Day.of(1)));
		assertEquals(2, Day.of(4).differenceWith(Day.of(2)));
		assertEquals(1, Day.of(4).differenceWith(Day.of(3)));
		assertEquals(0, Day.of(4).differenceWith(Day.of(4)));
	}

	@Test
	public void testOf() {
		assertDay(1, Day.of(1, 1));
		assertDay(2, Day.of(1, 2));
		assertDay(3, Day.of(1, 3));
		assertDay(4, Day.of(1, 4));
		assertDay(5, Day.of(2, 1));
		assertDay(6, Day.of(2, 2));
		assertDay(7, Day.of(2, 3));
		assertDay(8, Day.of(3, 1));
		assertDay(9, Day.of(3, 2));
		assertDay(10, Day.of(3, 3));
		assertDay(11, Day.of(4, 1));
		assertDay(12, Day.of(4, 2));
		assertDay(13, Day.of(4, 3));
		assertDay(14, Day.of(4, 4));
		assertDay(15, Day.of(5, 1));
		assertDay(16, Day.of(6, 1));
		assertDay(17, Day.of(6, 2));
		assertDay(18, Day.of(7, 1));

		assertLukashianException(() -> Day.of(0));
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
	}

	@Test
	public void testNow() {
		assertNotNull(Day.now());
	}

	@Test
	public void testCompareTo() {
		assertEquals(0, Day.of(1).compareTo(Day.of(1)));
		assertEquals(-1, Day.of(1).compareTo(Day.of(2)));
		assertEquals(-2, Day.of(1).compareTo(Day.of(3)));
		assertEquals(-3, Day.of(1).compareTo(Day.of(4)));
		assertEquals(3, Day.of(4).compareTo(Day.of(1)));
		assertEquals(2, Day.of(4).compareTo(Day.of(2)));
		assertEquals(1, Day.of(4).compareTo(Day.of(3)));
		assertEquals(0, Day.of(4).compareTo(Day.of(4)));
	}

	@Test
	public void testHashCode() {
		assertEquals(2, Day.of(2).hashCode());
	}

	@SuppressWarnings({"unlikely-arg-type", "SimplifiableAssertion", "EqualsBetweenInconvertibleTypes"})
	@Test
	public void testEquals() {
		assertTrue(Day.of(1).equals(Day.of(1)));
		assertFalse(Day.of(1).equals(Day.of(2)));
		assertFalse(Day.of(1).equals(Year.of(1)));
	}

	@Test
	public void testToString() {
		assertEquals("[Day: 1-2]", Day.of(2).toString());
	}
}
