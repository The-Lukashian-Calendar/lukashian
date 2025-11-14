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

import org.lukashian.Day;
import org.lukashian.Year;

import java.io.Serializable;
import java.util.Arrays;

import static org.lukashian.LukashianException.check;

/**
 * For each {@link Day} and {@link Year}, this class stores the number of milliseconds between the start of the calendar (the Lukashian epoch)
 * and the end of that day or year. It also stores the offset between the Lukashian epoch and the UNIX epoch. An implementation of
 * {@link MillisecondStoreDataProvider}, passed into the constructor, is used to obtain this data.
 * <p>
 * {@link MillisecondStoreData} is an immutable object.
 *
 * @see MillisecondStore
 * @see MillisecondStoreDataProvider
 */
public final class MillisecondStoreData implements Serializable {

	private final long unixEpochOffsetMilliseconds;
	private final long[] yearEpochMilliseconds;
	private final long[] dayEpochMilliseconds;
	private final long[] unixTimestampsWithLeapSecond;

	public MillisecondStoreData(MillisecondStoreDataProvider provider) {
		unixTimestampsWithLeapSecond = getUnixTimestampsWithLeapSecond();

		unixEpochOffsetMilliseconds = provider.loadUnixEpochOffsetMilliseconds();
		yearEpochMilliseconds = provider.loadYearEpochMilliseconds();
		dayEpochMilliseconds = provider.loadDayEpochMilliseconds(yearEpochMilliseconds);
	}

	/**
	 * Gets the number of milliseconds from the UNIX Epoch until the given number of milliseconds from the Lukashian Epoch.
	 */
	public long getUnixEpochMilliseconds(long lukashianEpochMilliseconds) {
		long unixEpochMilliseconds = Math.subtractExact(lukashianEpochMilliseconds, unixEpochOffsetMilliseconds);

		//We have the correct value, now we need to make it incorrect, so that it matches the incorrect UNIX time standard
		int index = Arrays.binarySearch(unixTimestampsWithLeapSecond, unixEpochMilliseconds);
		int numberOfLeapSeconds = index >= 0 ? index + 1 : -index - 1;

		return unixEpochMilliseconds - (numberOfLeapSeconds * 1000L);
	}

	/**
	 * Gets the number of milliseconds from the start of the Lukashian Calendar until the given number of milliseconds from the UNIX Epoch.
	 */
	public long getLukashianEpochMilliseconds(long unixEpochMilliseconds) {
		//We have the incorrect value, now we need to make it correct, to compensate for the the incorrect UNIX time standard
		int index = Arrays.binarySearch(unixTimestampsWithLeapSecond, unixEpochMilliseconds);
		int numberOfLeapSeconds = index >= 0 ? index + 1 : -index - 1;

		return Math.addExact(unixEpochMilliseconds + (numberOfLeapSeconds * 1000L), unixEpochOffsetMilliseconds);
	}

	/**
	 * Gets the number of milliseconds from the start of the Lukashian Calendar until now.
	 */
	public long getCurrentEpochMilliseconds() {
		return this.getLukashianEpochMilliseconds(System.currentTimeMillis());
	}

	/**
	 * Gets the number of milliseconds from the start of the Lukashian Calendar until the final point of the given year.
	 */
	public long getEpochMillisecondsForYear(int year) {
		check(year <= yearEpochMilliseconds.length, () -> "Year " + year + " isn't supported yet by this Lukashian Calendar instance");

		return yearEpochMilliseconds[year - 1];
	}

	/**
	 * Gets the number of milliseconds from the start of the Lukashian Calendar until the final point of the given day. The day is specified in epoch
	 * form, i.e. the how manieth day it is since the start of the Lukashian Calendar, irrespective of the year of the day.
	 */
	public long getEpochMillisecondsForEpochDay(int epochDay) {
		check(epochDay <= dayEpochMilliseconds.length, () -> "Epoch day " + epochDay + " isn't supported yet by this Lukashian Calendar instance");

		return dayEpochMilliseconds[epochDay - 1];
	}

	/**
	 * Gets the year that overlaps with the point where the given number of milliseconds have passed since the start of the Lukashian Calendar.
	 */
	public int getYearForEpochMilliseconds(long epochMilliseconds) {
		check(epochMilliseconds <= yearEpochMilliseconds[yearEpochMilliseconds.length - 1], () -> "Epoch millisecond " + epochMilliseconds + " isn't supported yet by this Lukashian Calendar instance");

		int index = Arrays.binarySearch(yearEpochMilliseconds, epochMilliseconds);
		return index >= 0 ? index + 1 : -index;
	}

	/**
	 * Gets the epoch day that overlaps with the point where the given number of milliseconds have passed since the start of the Lukashian Calendar.
	 */
	public int getEpochDayForEpochMilliseconds(long epochMilliseconds) {
		check(epochMilliseconds <= dayEpochMilliseconds[dayEpochMilliseconds.length - 1], () -> "Epoch millisecond " + epochMilliseconds + " isn't supported yet by this Lukashian Calendar instance");

		int index = Arrays.binarySearch(dayEpochMilliseconds, epochMilliseconds);
		return index >= 0 ? index + 1 : -index;
	}

	private static long[] getUnixTimestampsWithLeapSecond() {
		//if (true) return new long[] {}; //For initial Unix offset calculation

		//See https://github.com/eggert/tz/blob/master/leap-seconds.list
		long[] secondsSince1900WithLeapSecond = new long[] {
			2287785600L,
			2303683200L,
			2335219200L,
			2366755200L,
			2398291200L,
			2429913600L,
			2461449600L,
			2492985600L,
			2524521600L,
			2571782400L,
			2603318400L,
			2634854400L,
			2698012800L,
			2776982400L,
			2840140800L,
			2871676800L,
			2918937600L,
			2950473600L,
			2982009600L,
			3029443200L,
			3076704000L,
			3124137600L,
			3345062400L,
			3439756800L,
			3550089600L,
			3644697600L,
			3692217600L
		};

		long[] unixTimestampsWithLeapSecond = new long[secondsSince1900WithLeapSecond.length];
		for (int i = 0; i < unixTimestampsWithLeapSecond.length; i++ ) {
			unixTimestampsWithLeapSecond[i] = (secondsSince1900WithLeapSecond[i] - 2208988800L) * 1000;
		}
		return unixTimestampsWithLeapSecond;
	}
}
