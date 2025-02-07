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
 * Unit tests for the {@link Instant} class.
 */
public class InstantTest {

	@Test
	public void testMinusYears() {
		Instant instant = Instant.of(4050);

		assertLukashianException(() -> instant.minusYears(1));
		assertLukashianException(() -> instant.minusYears(4));
		assertInstant(1050, instant.minusYears(3));
	}

	@Test
	public void testPlusYears() {
		Instant instant = Instant.of(1050);

		assertLukashianException(() -> instant.plusYears(1));
		assertInstant(4050, instant.plusYears(3));
	}

	@Test
	public void testMinusDays() {
		assertInstant(150, Instant.of(450).minusDays(1));
		assertLukashianException(() -> Instant.of(450).minusDays(2));
	}

	@Test
	public void testPlusDays() {
		assertInstant(750, Instant.of(450).plusDays(1));
		assertLukashianException(() -> Instant.of(450).plusDays(17));
	}

	@Test
	public void testAtPreviousDay() {
		assertInstant(150, Instant.of(450).atPreviousDay());
	}

	@Test
	public void testAtNextDay() {
		assertInstant(750, Instant.of(450).atNextDay());
	}

	@Test
	public void testMinusMilliseconds() {
		Instant instant = Instant.of(450);

		assertLukashianException(() -> instant.minusMilliseconds(450));
		assertInstant(1, instant.minusMilliseconds(449));
		assertInstant(450, instant.minusMilliseconds(0));
		assertInstant(6450, instant.minusMilliseconds(-6000));
	}

	@Test
	public void testMinusSeconds() {
		assertInstant(1000, Instant.of(2000).minusSeconds(1));
	}

	@Test
	public void testPlusMilliseconds() {
		Instant instant = Instant.of(450);

		assertLukashianException(() -> instant.plusMilliseconds(-450));
		assertInstant(650, instant.plusMilliseconds(200));
		assertInstant(6450, instant.plusMilliseconds(6000));
		assertInstant(250, instant.plusMilliseconds(-200));
	}

	@Test
	public void testPlusSeconds() {
		assertInstant(3000, Instant.of(2000).plusSeconds(1));
	}

	@Test
	public void testMinusBeeps() {
		Instant instant = Instant.of(1651);

		assertLukashianException(() -> instant.minusBeeps(55034));
		assertInstant(1, instant.minusBeeps(54999));
		assertInstant(1199, instant.minusBeeps(15066));
		assertInstant(1200, instant.minusBeeps(15010));
		assertInstant(1201, instant.minusBeeps(14999));
		assertInstant(1351, instant.minusBeeps(10000));
		assertInstant(1499, instant.minusBeeps(5066));
		assertInstant(1500, instant.minusBeeps(5010));
		assertInstant(1501, instant.minusBeeps(4999));
		assertInstant(1621, instant.minusBeeps(1000));
		assertInstant(1649, instant.minusBeeps(66));
		assertInstant(1651, instant.minusBeeps(0));
		assertInstant(1652, instant.minusBeeps(-34));
		assertInstant(1681, instant.minusBeeps(-1000));
		assertInstant(1799, instant.minusBeeps(-4934));
		assertInstant(1800, instant.minusBeeps(-4999));
		assertInstant(1801, instant.minusBeeps(-5000));
		assertInstant(1951, instant.minusBeeps(-10000));
		assertInstant(2099, instant.minusBeeps(-14934));
		assertInstant(2100, instant.minusBeeps(-14999));
		assertInstant(2101, instant.minusBeeps(-15000));
		assertInstant(2551, instant.minusBeeps(-30000));
	}

	@Test
	public void testPlusBeeps() {
		Instant instant = Instant.of(1651);

		assertLukashianException(() -> instant.plusBeeps(-55034));
		assertInstant(1, instant.plusBeeps(-54999));
		assertInstant(1199, instant.plusBeeps(-15066));
		assertInstant(1200, instant.plusBeeps(-15010));
		assertInstant(1201, instant.plusBeeps(-14999));
		assertInstant(1351, instant.plusBeeps(-10000));
		assertInstant(1499, instant.plusBeeps(-5066));
		assertInstant(1500, instant.plusBeeps(-5010));
		assertInstant(1501, instant.plusBeeps(-4999));
		assertInstant(1621, instant.plusBeeps(-1000));
		assertInstant(1649, instant.plusBeeps(-66));
		assertInstant(1651, instant.plusBeeps(0));
		assertInstant(1652, instant.plusBeeps(34));
		assertInstant(1681, instant.plusBeeps(1000));
		assertInstant(1799, instant.plusBeeps(4934));
		assertInstant(1800, instant.plusBeeps(4999));
		assertInstant(1801, instant.plusBeeps(5000));
		assertInstant(1951, instant.plusBeeps(10000));
		assertInstant(2099, instant.plusBeeps(14934));
		assertInstant(2100, instant.plusBeeps(14999));
		assertInstant(2101, instant.plusBeeps(15000));
		assertInstant(2551, instant.plusBeeps(30000));
	}

	@Test
	public void testIsBefore() {
		assertTrue(Instant.of(1).isBefore(Instant.of(2)));
		assertFalse(Instant.of(1).isBefore(Instant.of(1)));
		assertFalse(Instant.of(2).isBefore(Instant.of(1)));
	}

	@Test
	public void testIsSameOrBefore() {
		assertTrue(Instant.of(1).isSameOrBefore(Instant.of(2)));
		assertTrue(Instant.of(1).isSameOrBefore(Instant.of(1)));
		assertFalse(Instant.of(2).isSameOrBefore(Instant.of(1)));
	}

	@Test
	public void testIsAfter() {
		assertFalse(Instant.of(1).isAfter(Instant.of(2)));
		assertFalse(Instant.of(1).isAfter(Instant.of(1)));
		assertTrue(Instant.of(2).isAfter(Instant.of(1)));
	}

	@Test
	public void testIsSameOrAfter() {
		assertFalse(Instant.of(1).isSameOrAfter(Instant.of(2)));
		assertTrue(Instant.of(1).isSameOrAfter(Instant.of(1)));
		assertTrue(Instant.of(2).isSameOrAfter(Instant.of(1)));
	}

	@Test
	public void testIsIn() {
		assertTrue(Instant.of(1).isIn(Year.of(1)));
		assertTrue(Instant.of(500).isIn(Year.of(1)));
		assertTrue(Instant.of(1000).isIn(Year.of(1)));
		assertFalse(Instant.of(1001).isIn(Year.of(1)));
		assertFalse(Instant.of(1500).isIn(Year.of(1)));

		assertTrue(Instant.of(1).isIn(Day.of(1)));
		assertTrue(Instant.of(150).isIn(Day.of(1)));
		assertTrue(Instant.of(300).isIn(Day.of(1)));
		assertFalse(Instant.of(301).isIn(Day.of(1)));
		assertFalse(Instant.of(450).isIn(Day.of(1)));
	}

	@Test
	public void testIsNotIn() {
		assertFalse(Instant.of(1).isNotIn(Year.of(1)));
		assertFalse(Instant.of(500).isNotIn(Year.of(1)));
		assertFalse(Instant.of(1000).isNotIn(Year.of(1)));
		assertTrue(Instant.of(1001).isNotIn(Year.of(1)));
		assertTrue(Instant.of(1500).isNotIn(Year.of(1)));

		assertFalse(Instant.of(1).isNotIn(Day.of(1)));
		assertFalse(Instant.of(150).isNotIn(Day.of(1)));
		assertFalse(Instant.of(300).isNotIn(Day.of(1)));
		assertTrue(Instant.of(301).isNotIn(Day.of(1)));
		assertTrue(Instant.of(450).isNotIn(Day.of(1)));
	}

	@Test
	public void testGetEpochMilliseconds() {
		assertEquals(1, Instant.of(1).getEpochMilliseconds());
		assertEquals(10000, Instant.of(10000).getEpochMilliseconds());
		assertEquals(39000, Instant.of(39000).getEpochMilliseconds());
	}

	@Test
	public void testGetMillisecond() {
		assertEquals(1, Instant.of(1).getMillisecond());
		assertEquals(10000, Instant.of(10000).getMillisecond());
		assertEquals(39000, Instant.of(39000).getMillisecond());
	}

	@Test
	public void testGetDay() {
		assertDay(1, Instant.of(1).getDay());
		assertDay(1, Instant.of(150).getDay());
		assertDay(1, Instant.of(300).getDay());
		assertDay(2, Instant.of(301).getDay());

		assertDay(15, Instant.of(4498).getDay());
		assertDay(15, Instant.of(4499).getDay());
		assertDay(15, Instant.of(4500).getDay());
		assertDay(16, Instant.of(4501).getDay());

		assertDay(16, Instant.of(4798).getDay());
		assertDay(16, Instant.of(4799).getDay());
		assertDay(17, Instant.of(4800).getDay());
		assertDay(17, Instant.of(4801).getDay());

		assertDay(17, Instant.of(4899).getDay());
		assertDay(17, Instant.of(4900).getDay());
		assertDay(18, Instant.of(4901).getDay());

		assertDay(18, Instant.of(38999).getDay());
		assertDay(18, Instant.of(39000).getDay());
	}

	@Test
	public void testGetYear() {
		assertYear(1, Instant.of(1).getYear());
		assertYear(1, Instant.of(999).getYear());
		assertYear(1, Instant.of(1000).getYear());
		assertYear(2, Instant.of(1001).getYear());

		assertYear(3, Instant.of(2999).getYear());
		assertYear(3, Instant.of(3000).getYear());
		assertYear(4, Instant.of(3001).getYear());

		assertYear(5, Instant.of(4498).getYear());
		assertYear(5, Instant.of(4499).getYear());
		assertYear(6, Instant.of(4500).getYear());
		assertYear(6, Instant.of(4501).getYear());

		assertYear(6, Instant.of(4798).getYear());
		assertYear(6, Instant.of(4799).getYear());
		assertYear(6, Instant.of(4800).getYear());
		assertYear(7, Instant.of(4801).getYear());

		assertYear(8, Instant.of(38999).getYear());
		assertYear(8, Instant.of(39000).getYear());

		assertLukashianException(() -> Instant.of(39001).getYear());
	}

	@Test
	public void testGetProportionOfDay() {
		assertEquals(BigFraction.of(0), Instant.of(1).getProportionOfDay());
		assertEquals(BigFraction.of(5, 10), Instant.of(151).getProportionOfDay());
		assertEquals(BigFraction.of(149, 150), Instant.of(299).getProportionOfDay());
		assertEquals(BigFraction.of(299, 300), Instant.of(300).getProportionOfDay());

		assertEquals(BigFraction.of(0), Instant.of(301).getProportionOfDay());
		assertEquals(BigFraction.of(5, 10), Instant.of(451).getProportionOfDay());
		assertEquals(BigFraction.of(149, 150), Instant.of(599).getProportionOfDay());
		assertEquals(BigFraction.of(299, 300), Instant.of(600).getProportionOfDay());

		assertEquals(BigFraction.of(0), Instant.of(601).getProportionOfDay());
	}

	@Test
	public void testGetBeeps() {
		assertEquals(0, Instant.of(1).getBeeps());
		assertEquals(5000, Instant.of(151).getBeeps());
		assertEquals(9933, Instant.of(299).getBeeps());
		assertEquals(9966, Instant.of(300).getBeeps());

		assertEquals(0, Instant.of(301).getBeeps());
		assertEquals(5000, Instant.of(451).getBeeps());
		assertEquals(9933, Instant.of(599).getBeeps());
		assertEquals(9966, Instant.of(600).getBeeps());

		assertEquals(0, Instant.of(601).getBeeps());
	}

	@Test
	public void testGetUnixEpochMilliseconds() {
		assertEquals(System.currentTimeMillis(), Instant.ofUnixEpochMilliseconds(System.currentTimeMillis()).getUnixEpochMilliseconds());
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
	public void testDifferenceWith() {
		assertEquals(0, Instant.of(1).differenceWith(Instant.of(1)));
		assertEquals(-1, Instant.of(1).differenceWith(Instant.of(2)));
		assertEquals(-2, Instant.of(1).differenceWith(Instant.of(3)));
		assertEquals(-3, Instant.of(1).differenceWith(Instant.of(4)));
		assertEquals(3, Instant.of(4).differenceWith(Instant.of(1)));
		assertEquals(2, Instant.of(4).differenceWith(Instant.of(2)));
		assertEquals(1, Instant.of(4).differenceWith(Instant.of(3)));
		assertEquals(0, Instant.of(4).differenceWith(Instant.of(4)));
	}

	@Test
	public void testOf() {
		assertLukashianException(() -> Instant.of(0));
		assertLukashianException(() -> Instant.of(Day.of(2), BigFraction.of(-1)));
		assertLukashianException(() -> Instant.of(Day.of(2), BigFraction.of(1)));
		assertLukashianException(() -> Instant.of(Day.of(2), BigFraction.of(2)));

		assertLukashianException(() -> Instant.of(0));
		assertLukashianException(() -> Instant.of(Day.of(2), -1));
		assertLukashianException(() -> Instant.of(Day.of(2), 10000));
		assertLukashianException(() -> Instant.of(Day.of(2), 20000));

		assertInstant(1, Instant.of(Day.of(1), BigFraction.of(0)));
		assertInstant(4, Instant.of(Day.of(1), BigFraction.of(1, 100)));
		assertInstant(151, Instant.of(Day.of(1), BigFraction.of(5, 10)));
		assertInstant(298, Instant.of(Day.of(1), BigFraction.of(99, 100)));
		assertInstant(300, Instant.of(Day.of(1), BigFraction.of(997, 1000)));
		assertInstant(300, Instant.of(Day.of(1), BigFraction.of(999999999, 1000000000)));

		assertInstant(1, Instant.of(Day.of(1), 0));
		assertInstant(4, Instant.of(Day.of(1), 100));
		assertInstant(151, Instant.of(Day.of(1), 5000));
		assertInstant(298, Instant.of(Day.of(1), 9900));
		assertInstant(300, Instant.of(Day.of(1), 9970));
		assertInstant(300, Instant.of(Day.of(1), 9999));

		assertInstant(301, Instant.of(Day.of(2), BigFraction.of(0)));
		assertInstant(304, Instant.of(Day.of(2), BigFraction.of(1, 100)));
		assertInstant(451, Instant.of(Day.of(2), BigFraction.of(5, 10)));
		assertInstant(598, Instant.of(Day.of(2), BigFraction.of(99, 100)));
		assertInstant(600, Instant.of(Day.of(2), BigFraction.of(997, 1000)));
		assertInstant(600, Instant.of(Day.of(2), BigFraction.of(999999999, 1000000000)));

		assertInstant(301, Instant.of(Day.of(2), 0));
		assertInstant(304, Instant.of(Day.of(2), 100));
		assertInstant(451, Instant.of(Day.of(2), 5000));
		assertInstant(598, Instant.of(Day.of(2), 9900));
		assertInstant(600, Instant.of(Day.of(2), 9970));
		assertInstant(600, Instant.of(Day.of(2), 9999));

		assertInstant(151, Instant.of(Year.of(1), 1, BigFraction.of(5, 10)));
		assertInstant(151, Instant.of(1, 1, BigFraction.of(5, 10)));

		assertInstant(151, Instant.of(Year.of(1), 1, 5000));
		assertInstant(151, Instant.of(1, 1, 5000));
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
		assertNotNull(Instant.now());
	}

	@Test
	public void testCompareTo() {
		assertEquals(0, Instant.of(1).compareTo(Instant.of(1)));
		assertEquals(-1, Instant.of(1).compareTo(Instant.of(2)));
		assertEquals(-1, Instant.of(1).compareTo(Instant.of(3)));
		assertEquals(-1, Instant.of(1).compareTo(Instant.of(4)));
		assertEquals(1, Instant.of(4).compareTo(Instant.of(1)));
		assertEquals(1, Instant.of(4).compareTo(Instant.of(2)));
		assertEquals(1, Instant.of(4).compareTo(Instant.of(3)));
		assertEquals(0, Instant.of(4).compareTo(Instant.of(4)));
	}

	@Test
	public void testHashCode() {
		assertEquals(2, Instant.of(2).hashCode());
	}

	@SuppressWarnings({"unlikely-arg-type", "SimplifiableAssertion", "EqualsBetweenInconvertibleTypes"})
	@Test
	public void testEquals() {
		assertTrue(Instant.of(1).equals(Instant.of(1)));
		assertFalse(Instant.of(1).equals(Instant.of(2)));
		assertFalse(Instant.of(1).equals(Year.of(1)));

		Instant i1 = Instant.of(1, 1, BigFraction.of(600000000, 1000000000));
		Instant i2 = Instant.of(1, 1, BigFraction.of(600000000, 1000000000));
		assertTrue(i1.equals(i2));

		i1 = Instant.of(1, 1, BigFraction.of(600000000, 1000000000));
		i2 = Instant.of(1, 1, BigFraction.of(600000001, 1000000000));
		assertTrue(i1.equals(i2)); //equals looks at exact millisecond, not proportion
	}

	@Test
	public void testToString() {
		assertEquals("[Instant: 1-1 0033]", Instant.of(2).toString());
	}
}
