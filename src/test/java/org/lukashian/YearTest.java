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

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.lukashian.LukashianAssert.*;

/**
 * Unit tests for the {@link Year} class.
 */
public class YearTest {

	@Test
	public void testMinusYears() {
		Year year = Year.of(2);

		assertLukashianException(() -> year.minusYears(3));
		assertLukashianException(() -> year.minusYears(2));
		assertYear(1, year.minusYears(1));
		assertYear(2, year.minusYears(0));
		assertYear(3, year.minusYears(-1));
		assertYear(4, year.minusYears(-2));
		assertLukashianException(() -> year.minusYears(-7));
	}

	@Test
	public void testPlusYears() {
		Year year = Year.of(2);

		assertLukashianException(() -> year.plusYears(7));
		assertYear(4, year.plusYears(2));
		assertYear(3, year.plusYears(1));
		assertYear(2, year.plusYears(0));
		assertYear(1, year.plusYears(-1));
		assertLukashianException(() -> year.plusYears(-2));
		assertLukashianException(() -> year.plusYears(-3));
	}

	@Test
	public void testPrevious() {
		assertYear(1, Year.of(2).previous());
	}

	@Test
	public void testNext() {
		assertYear(3, Year.of(2).next());
	}

	@Test
	public void testAtDay() {
		assertLukashianException(() -> Year.of(1).atDay(-1));
		assertLukashianException(() -> Year.of(1).atDay(0));
		assertDay(1, Year.of(1).atDay(1));
		assertDay(2, Year.of(1).atDay(2));
		assertDay(3, Year.of(1).atDay(3));
		assertDay(4, Year.of(1).atDay(4));
		assertLukashianException(() -> Year.of(1).atDay(5));
		assertDay(5, Year.of(2).atDay(1));
		assertDay(6, Year.of(2).atDay(2));
		assertDay(7, Year.of(2).atDay(3));
		assertLukashianException(() -> Year.of(2).atDay(4));
		assertDay(8, Year.of(3).atDay(1));
		assertDay(9, Year.of(3).atDay(2));
		assertDay(10, Year.of(3).atDay(3));
		assertLukashianException(() -> Year.of(3).atDay(4));
		assertDay(11, Year.of(4).atDay(1));
		assertDay(12, Year.of(4).atDay(2));
		assertDay(13, Year.of(4).atDay(3));
		assertDay(14, Year.of(4).atDay(4));
		assertLukashianException(() -> Year.of(4).atDay(5));
		assertDay(15, Year.of(5).atDay(1));
		assertLukashianException(() -> Year.of(5).atDay(2));
		assertDay(16, Year.of(6).atDay(1));
		assertDay(17, Year.of(6).atDay(2));
		assertLukashianException(() -> Year.of(6).atDay(3));
		assertDay(18, Year.of(7).atDay(1));
		assertLukashianException(() -> Year.of(7).atDay(2));
		assertLukashianException(() -> Year.of(8).atDay(1));
	}

	@Test
	public void testFirstDay() {
		assertDay(1, Year.of(1).firstDay());
		assertDay(5, Year.of(2).firstDay());
		assertDay(8, Year.of(3).firstDay());
		assertDay(11, Year.of(4).firstDay());
		assertDay(15, Year.of(5).firstDay());
		assertDay(16, Year.of(6).firstDay());
		assertDay(18, Year.of(7).firstDay());
		assertLukashianException(() -> Year.of(8).firstDay());

		assertYear(1, Year.of(1).firstDay().getYear());
		assertYear(2, Year.of(2).firstDay().getYear());
		assertYear(3, Year.of(3).firstDay().getYear());
		assertYear(4, Year.of(4).firstDay().getYear());
		assertYear(5, Year.of(5).firstDay().getYear());
		assertYear(6, Year.of(6).firstDay().getYear());
		assertYear(7, Year.of(7).firstDay().getYear());
	}

	@Test
	public void testLastDay() {
		assertDay(4, Year.of(1).lastDay());
		assertDay(7, Year.of(2).lastDay());
		assertDay(10, Year.of(3).lastDay());
		assertDay(14, Year.of(4).lastDay());
		assertDay(15, Year.of(5).lastDay());
		assertDay(17, Year.of(6).lastDay());
		assertDay(18, Year.of(7).lastDay());
		assertLukashianException(() -> Year.of(8).lastDay());

		assertYear(1, Year.of(1).lastDay().getYear());
		assertYear(2, Year.of(2).lastDay().getYear());
		assertYear(3, Year.of(3).lastDay().getYear());
		assertYear(4, Year.of(4).lastDay().getYear());
		assertYear(5, Year.of(5).lastDay().getYear());
		assertYear(6, Year.of(6).lastDay().getYear());
		assertYear(7, Year.of(7).lastDay().getYear());
	}

	@Test
	public void testFirstInstant() {
		assertInstant(1, Year.of(1).firstInstant());
		assertInstant(1001, Year.of(2).firstInstant());
		assertInstant(2001, Year.of(3).firstInstant());
		assertInstant(3001, Year.of(4).firstInstant());
		assertInstant(4001, Year.of(5).firstInstant());
		assertInstant(4500, Year.of(6).firstInstant());
		assertInstant(4801, Year.of(7).firstInstant());
		assertInstant(5001, Year.of(8).firstInstant());

		assertYear(1, Year.of(1).firstInstant().getYear());
		assertYear(2, Year.of(2).firstInstant().getYear());
		assertYear(3, Year.of(3).firstInstant().getYear());
		assertYear(4, Year.of(4).firstInstant().getYear());
		assertYear(5, Year.of(5).firstInstant().getYear());
		assertYear(6, Year.of(6).firstInstant().getYear());
		assertYear(7, Year.of(7).firstInstant().getYear());
		assertYear(8, Year.of(8).firstInstant().getYear());
	}

	@Test
	public void testLastInstant() {
		assertInstant(1000, Year.of(1).lastInstant());
		assertInstant(2000, Year.of(2).lastInstant());
		assertInstant(3000, Year.of(3).lastInstant());
		assertInstant(4000, Year.of(4).lastInstant());
		assertInstant(4499, Year.of(5).lastInstant());
		assertInstant(4800, Year.of(6).lastInstant());
		assertInstant(5000, Year.of(7).lastInstant());

		assertYear(1, Year.of(1).lastInstant().getYear());
		assertYear(2, Year.of(2).lastInstant().getYear());
		assertYear(3, Year.of(3).lastInstant().getYear());
		assertYear(4, Year.of(4).lastInstant().getYear());
		assertYear(5, Year.of(5).lastInstant().getYear());
		assertYear(6, Year.of(6).lastInstant().getYear());
		assertYear(7, Year.of(7).lastInstant().getYear());
	}

	@Test
	public void testIsBefore() {
		assertTrue(Year.of(1).isBefore(Year.of(2)));
		assertFalse(Year.of(1).isBefore(Year.of(1)));
		assertFalse(Year.of(2).isBefore(Year.of(1)));
	}

	@Test
	public void testIsSameOrBefore() {
		assertTrue(Year.of(1).isSameOrBefore(Year.of(2)));
		assertTrue(Year.of(1).isSameOrBefore(Year.of(1)));
		assertFalse(Year.of(2).isSameOrBefore(Year.of(1)));
	}

	@Test
	public void testIsAfter() {
		assertFalse(Year.of(1).isAfter(Year.of(2)));
		assertFalse(Year.of(1).isAfter(Year.of(1)));
		assertTrue(Year.of(2).isAfter(Year.of(1)));
	}

	@Test
	public void testIsSameOrAfter() {
		assertFalse(Year.of(1).isSameOrAfter(Year.of(2)));
		assertTrue(Year.of(1).isSameOrAfter(Year.of(1)));
		assertTrue(Year.of(2).isSameOrAfter(Year.of(1)));
	}

	@Test
	public void testContains() {
		Year year = Year.of(2);

		assertTrue(year.contains(year.firstInstant()));
		assertTrue(year.contains(year.lastInstant()));

		assertTrue(Year.of(1).contains(Day.of(1)));
		assertTrue(Year.of(1).contains(Day.of(4)));
		assertFalse(Year.of(1).contains(Day.of(5)));

		assertTrue(Year.of(1).contains(Instant.of(1)));
		assertTrue(Year.of(1).contains(Instant.of(999)));
		assertTrue(Year.of(1).contains(Instant.of(1000)));
		assertFalse(Year.of(1).contains(Instant.of(1001)));

		assertTrue(Year.of(3).contains(Instant.of(2999)));
		assertTrue(Year.of(3).contains(Instant.of(3000)));
		assertFalse(Year.of(3).contains(Instant.of(3001)));

		assertFalse(Year.of(4).contains(Instant.of(2999)));
		assertFalse(Year.of(4).contains(Instant.of(3000)));
		assertTrue(Year.of(4).contains(Instant.of(3001)));
		assertTrue(Year.of(4).contains(Instant.of(4000)));
		assertFalse(Year.of(4).contains(Instant.of(4001)));
	}

	@Test
	public void testContainsNot() {
		Year year = Year.of(2);

		assertFalse(year.containsNot(year.firstInstant()));
		assertFalse(year.containsNot(year.lastInstant()));

		assertFalse(Year.of(1).containsNot(Day.of(1)));
		assertFalse(Year.of(1).containsNot(Day.of(4)));
		assertTrue(Year.of(1).containsNot(Day.of(5)));

		assertFalse(Year.of(1).containsNot(Instant.of(1)));
		assertFalse(Year.of(1).containsNot(Instant.of(999)));
		assertFalse(Year.of(1).containsNot(Instant.of(1000)));
		assertTrue(Year.of(1).containsNot(Instant.of(1001)));

		assertFalse(Year.of(3).containsNot(Instant.of(2999)));
		assertFalse(Year.of(3).containsNot(Instant.of(3000)));
		assertTrue(Year.of(3).containsNot(Instant.of(3001)));

		assertTrue(Year.of(4).containsNot(Instant.of(2999)));
		assertTrue(Year.of(4).containsNot(Instant.of(3000)));
		assertFalse(Year.of(4).containsNot(Instant.of(3001)));
		assertFalse(Year.of(4).containsNot(Instant.of(4000)));
		assertTrue(Year.of(4).containsNot(Instant.of(4001)));
	}

	@Test
	public void testLenghtInMilliseconds() {
		assertEquals(1000, Year.of(1).lengthInMilliseconds());
		assertEquals(1000, Year.of(2).lengthInMilliseconds());
		assertEquals(1000, Year.of(3).lengthInMilliseconds());
		assertEquals(1000, Year.of(4).lengthInMilliseconds());
		assertEquals(499, Year.of(5).lengthInMilliseconds());
		assertEquals(301, Year.of(6).lengthInMilliseconds());
		assertEquals(200, Year.of(7).lengthInMilliseconds());
		assertEquals(35000, Year.of(8).lengthInMilliseconds());
	}

	@Test
	public void testGetNumberOfDays() {
		assertEquals(4, Year.of(1).getNumberOfDays());
		assertEquals(3, Year.of(2).getNumberOfDays());
		assertEquals(3, Year.of(3).getNumberOfDays());
		assertEquals(4, Year.of(4).getNumberOfDays());
		assertEquals(1, Year.of(5).getNumberOfDays());
		assertEquals(2, Year.of(6).getNumberOfDays());
		assertEquals(1, Year.of(7).getNumberOfDays());
		assertLukashianException(() -> Year.of(8).getNumberOfDays());
	}

	@Test
	public void testGetYearNumber() {
		assertEquals(2, Year.of(2).getYearNumber());
	}

	@Test
	public void testGetEpochMilliseconds() {
		assertEquals(1000, Year.of(1).getEpochMilliseconds());
		assertEquals(2000, Year.of(2).getEpochMilliseconds());
		assertEquals(3000, Year.of(3).getEpochMilliseconds());
		assertEquals(4000, Year.of(4).getEpochMilliseconds());
		assertEquals(4499, Year.of(5).getEpochMilliseconds());
		assertEquals(4800, Year.of(6).getEpochMilliseconds());
		assertEquals(5000, Year.of(7).getEpochMilliseconds());
		assertEquals(40000, Year.of(8).getEpochMilliseconds());
	}

	@Test
	public void testGetEpochMillisecondsPreviousYear() {
		assertEquals(0, Year.of(1).getEpochMillisecondsPreviousYear());
		assertEquals(1000, Year.of(2).getEpochMillisecondsPreviousYear());
		assertEquals(2000, Year.of(3).getEpochMillisecondsPreviousYear());
		assertEquals(3000, Year.of(4).getEpochMillisecondsPreviousYear());
		assertEquals(4000, Year.of(5).getEpochMillisecondsPreviousYear());
		assertEquals(4499, Year.of(6).getEpochMillisecondsPreviousYear());
		assertEquals(4800, Year.of(7).getEpochMillisecondsPreviousYear());
		assertEquals(5000, Year.of(8).getEpochMillisecondsPreviousYear());
	}

	@Test
	public void testGetEpochMillisecondsAtStartOfYear() {
		assertEquals(1, Year.of(1).getEpochMillisecondsAtStartOfYear());
		assertEquals(1001, Year.of(2).getEpochMillisecondsAtStartOfYear());
		assertEquals(2001, Year.of(3).getEpochMillisecondsAtStartOfYear());
		assertEquals(3001, Year.of(4).getEpochMillisecondsAtStartOfYear());
		assertEquals(4001, Year.of(5).getEpochMillisecondsAtStartOfYear());
		assertEquals(4500, Year.of(6).getEpochMillisecondsAtStartOfYear());
		assertEquals(4801, Year.of(7).getEpochMillisecondsAtStartOfYear());
		assertEquals(5001, Year.of(8).getEpochMillisecondsAtStartOfYear());
	}

	@Test
	public void testDifferenceWith() {
		assertEquals(0, Year.of(1).differenceWith(Year.of(1)));
		assertEquals(-1, Year.of(1).differenceWith(Year.of(2)));
		assertEquals(-2, Year.of(1).differenceWith(Year.of(3)));
		assertEquals(-3, Year.of(1).differenceWith(Year.of(4)));
		assertEquals(3, Year.of(4).differenceWith(Year.of(1)));
		assertEquals(2, Year.of(4).differenceWith(Year.of(2)));
		assertEquals(1, Year.of(4).differenceWith(Year.of(3)));
		assertEquals(0, Year.of(4).differenceWith(Year.of(4)));
	}

	@Test
	public void testNow() {
		assertNotNull(Year.now());
	}

	@Test
	public void testCompareTo() {
		assertEquals(0, Year.of(1).compareTo(Year.of(1)));
		assertEquals(-1, Year.of(1).compareTo(Year.of(2)));
		assertEquals(-2, Year.of(1).compareTo(Year.of(3)));
		assertEquals(-3, Year.of(1).compareTo(Year.of(4)));
		assertEquals(3, Year.of(4).compareTo(Year.of(1)));
		assertEquals(2, Year.of(4).compareTo(Year.of(2)));
		assertEquals(1, Year.of(4).compareTo(Year.of(3)));
		assertEquals(0, Year.of(4).compareTo(Year.of(4)));
	}

	@Test
	public void testHashCode() {
		assertEquals(2, Year.of(2).hashCode());
	}

	@SuppressWarnings({"unlikely-arg-type", "SimplifiableAssertion", "EqualsBetweenInconvertibleTypes"})
	@Test
	public void testEquals() {
		assertTrue(Year.of(1).equals(Year.of(1)));
		assertFalse(Year.of(1).equals(Year.of(2)));
		assertFalse(Year.of(1).equals(Day.of(1)));
	}

	@Test
	public void testToString() {
		assertEquals("[Year: 2]", Year.of(2).toString());
	}
}
