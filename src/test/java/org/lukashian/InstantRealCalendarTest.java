/*
 * Copyright (c) 2018-2023 (5918-5923 in Lukashian years)
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

import java.lang.reflect.Field;
import java.math.BigDecimal;

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
  public static void setUp() throws NoSuchFieldException, IllegalAccessException {
		//Running with the actual calendar, in order to not have millisecond rounding issues, due to the low number of milliseconds per day in the TestMillisecondStoreDataProvider
		Field provider = MillisecondStore.class.getDeclaredField("provider");
		provider.setAccessible(true);
		MillisecondStore store = MillisecondStore.store();
		provider.set(store, new StandardEarthMillisecondStoreDataProvider());
		store.reload();
  }

  @AfterAll
  public static void tearDown() throws NoSuchFieldException, IllegalAccessException {
		//Running with the test calendar, in order to have exact predictability
		Field provider = MillisecondStore.class.getDeclaredField("provider");
		provider.setAccessible(true);
		MillisecondStore store = MillisecondStore.store();
		provider.set(store, new TestMillisecondStoreDataProvider());
		store.reload();
  }

	@Test
	public void testMinusProportionOfDay() {
		Instant realInstant = Instant.of(Day.of(4), 5000);
		assertEquals(Instant.of(Day.of(4), 0), realInstant.minusProportionOfDay(new BigDecimal("0.499999999999999999999999999999999999999999999999999999999999999999")));
		assertEquals(Instant.of(Day.of(3), 5001), realInstant.minusProportionOfDay(new BigDecimal("0.9999")));
		assertEquals(Instant.of(Day.of(3), 5000), realInstant.minusProportionOfDay(new BigDecimal("0.999999999999999999999999999999999999999999999999999999999999999999")));
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
		assertEquals(Instant.of(Day.of(5), 4999), realInstant.plusProportionOfDay(new BigDecimal("0.9999")));
		//Can't test the 'plus' version of the testcases with many BigDecimals, because overflow into the next beep is prevented and therefore the resulting beep cannot be expressed as a literal
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
	}
}
