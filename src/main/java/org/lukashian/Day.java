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
import org.lukashian.store.StandardEarthMillisecondStoreDataProvider;

import java.io.Serializable;

import static org.lukashian.Instant.BEEPS_PER_DAY;
import static org.lukashian.store.MillisecondStore.store;

/**
 * Represents a day in the Lukashian Calendar Mechanism. For the meaning of a day in the standard implementation of the Lukashian Calendar, see
 * {@link StandardEarthMillisecondStoreDataProvider}.
 * <p>
 * The first day of every year is day 1. A day is part of the year that it starts in, even if it finishes in the next year.
 * <p>
 * By definition, the exact point at which a day starts belongs to that day, not to the previous day, which ends at that point. So, a day
 * runs from its start (inclusive) to its end (exclusive).
 * <p>
 * In the Lukashian Calendar, the length of a day in milliseconds is not constant. Instead, it accurately represents the actual length of a particular day in
 * astronomical terms. Due to astronomical and planetary developments, this duration is not constant. Since the same is true for years (a year accurately
 * represents the actual length of a particular year in astronomical terms), a day is not necessarily contained in a single year. In fact, it is very unlikely
 * that the turn of a year coincides with the turn of a day. Therefore, a day is defined to be part of the year in which it started.
 * <p>
 * The very first day of the calendar starts at the same point as the very first year. This means that the turn of every single day happens at the position
 * of the planet at the start of the calendar.
 * <p>
 * A {@link Day} keeps track of the total number of milliseconds that have taken place from the start of the Lukashian Calendar (Lukashian epoch) until
 * the final point of this day. This value is called "epoch milliseconds". This information allows for determining which year a day is in and how to
 * do various calculations. A {@link Day} also keeps track of the epoch milliseconds of the previous {@link Day}, for calculation purposes.
 * This means that a {@link Day} knows how many milliseconds it lasts, by subtracting the two.
 * <p>
 * Internally, a {@link Day} keeps track of its epoch value ("epoch day"). This value represents which day this is since the start of the Lukashian
 * Calendar, regardless of the year. This value also starts at 1, like years and days within years, 1 being the very first day since the start of
 * the calendar.
 * <p>
 * {@link Day} is an immutable object. New instances are always created when calling one of the mutation methods.
 */
public final class Day implements Comparable<Day>, Serializable {

	private int epochDay;
	private long epochMilliseconds;
	private long epochMillisecondsPreviousDay;

	private Day(int epochDay) {
		if (epochDay < 1) {
			throw new LukashianException(epochDay + " is not a valid epoch day, the minimum is 1");
		}
		this.epochDay = epochDay;
		this.epochMilliseconds = store().getEpochMillisecondsForEpochDay(epochDay);
		this.epochMillisecondsPreviousDay = epochDay == 1 ? 0 : store().getEpochMillisecondsForEpochDay(epochDay - 1);
	}

	/**
	 * Returns a new {@link Day} that represents this day's number on this day's year minus the given amount of years, for example,
	 * if this day represents day number 10 of its year, then calling this method will return an day that represents day number 10 of the
	 * resulting year.
	 *
	 * @throws LukashianException when the result would be before the start of the Lukashian Calendar or when the resulting year does not have a day with this day's number
	 */
	public Day minusYears(int yearsToSubtract) {
		return Day.of(this.getYear().minusYears(yearsToSubtract), this.getDayNumber());
	}

	/**
	 * Returns a new {@link Day} that represents this day's number on this day's year plus the given amount of years, for example,
	 * if this day represents day number 10 of its year, then calling this method will return an day that represents day number 10 of the
	 * resulting year.
	 *
	 * @throws LukashianException when the resulting year does not have a day with this day's number
	 */
	public Day plusYears(int yearsToAdd) {
		return Day.of(this.getYear().plusYears(yearsToAdd), this.getDayNumber());
	}

	/**
	 * Returns a new {@link Day} that represents this day minus the given amount of days. This might result in a {@link Day} that is in a different year.
	 *
	 * @throws LukashianException when the result would be before the start of the Lukashian Calendar
	 */
	public Day minusDays(int daysToSubtract) {
		if (daysToSubtract < 0) { //To not have to deal with negatives
			return this.plusDays(Math.negateExact(daysToSubtract));
		}
		return Day.of(Math.subtractExact(epochDay, daysToSubtract));
	}

	/**
	 * Returns a new {@link Day} that represents this day plus the given amount of days. This might result in a {@link Day} that is in a different year.
	 */
	public Day plusDays(int daysToAdd) {
		if (daysToAdd < 0) { //To not have to deal with negatives
			return this.minusDays(Math.negateExact(daysToAdd));
		}
		return Day.of(Math.addExact(epochDay, daysToAdd));
	}

	/**
	 * Returns a new {@link Day} that represents the day preceding this day.
	 *
	 * @throws LukashianException when the result would be before the start of the Lukashian Calendar
	 */
	public Day previous() {
		return this.minusDays(1);
	}

	/**
	 * Returns a new {@link Day} that represents the day succeeding this day.
	 */
	public Day next() {
		return this.plusDays(1);
	}

	/**
	 * Returns a new {@link Instant} that represents the instant that occurs after the given proportion of this day has passed.
	 *
	 * @throws LukashianException when the given proportion is not between 0 (inclusive) and 1 (exclusive).
	 */
	public Instant atTime(BigFraction proportionOfDay) {
		return Instant.of(this, proportionOfDay);
	}

	/**
	 * Returns a new {@link Instant} that represents the instant that occurs after the given proportion of this day has passed.
	 *
	 * @throws LukashianException when the given proportion is not between 0 (inclusive) and 9999 (inclusive).
	 */
	public Instant atTime(int beeps) {
		return Instant.of(this, beeps);
	}

	/**
	 * Returns a new {@link Instant} that represents the first {@link Instant} of this day.
	 */
	public Instant firstInstant() {
		return Instant.of(this.getEpochMillisecondsAtStartOfDay());
	}

	/**
	 * Returns a new {@link Instant} that represents the last {@link Instant} of this day.
	 */
	public Instant lastInstant() {
		return Instant.of(this.getEpochMilliseconds());
	}

	/**
	 * Returns whether this day is before the given non-null {@link Day}.
	 */
	public boolean isBefore(Day other) {
		return epochDay < other.epochDay;
	}

	/**
	 * Returns whether this day is the same or before the given non-null {@link Day}.
	 */
	public boolean isSameOrBefore(Day other) {
		return epochDay <= other.epochDay;
	}

	/**
	 * Returns whether this day is after the given non-null {@link Day}.
	 */
	public boolean isAfter(Day other) {
		return epochDay > other.epochDay;
	}

	/**
	 * Returns whether this day is the same or after the given non-null {@link Day}.
	 */
	public boolean isSameOrAfter(Day other) {
		return epochDay >= other.epochDay;
	}

	/**
	 * Returns whether the given non-null {@link Instant}, is inside this day.
	 */
	public boolean contains(Instant instant) {
		return this.equals(instant.getDay());
	}

	/**
	 * Returns whether the given non-null {@link Instant}, is not inside this day.
	 */
	public boolean containsNot(Instant instant) {
		return !this.contains(instant);
	}

	/**
	 * Returns whether this day is in the given non-null {@link Year}.
	 */
	public boolean isIn(Year year) {
		return year.contains(this);
	}

	/**
	 * Returns whether this day is not in the given non-null {@link Year}.
	 */
	public boolean isNotIn(Year year) {
		return year.containsNot(this);
	}

	/**
	 * Gets the total number of milliseconds from the start of this day, up to the final point of this day. This number varies from day to day, depending on
	 * astronomical and planetary developments.
	 */
	public long lengthInMilliseconds() {
		return epochMilliseconds - epochMillisecondsPreviousDay;
	}

	/**
	 * Returns which day this is since the start of the Lukashian Calendar, regardless of the year. This value starts at 1, consistent with years and days within years.
	 * Epoch day 1 is the very first day of the calendar.
	 */
	public int getEpochDay() {
		return epochDay;
	}

	/**
	 * Gets the total number of milliseconds from the start of the Lukashian Calendar, up to the final point of this day.
	 */
	public long getEpochMilliseconds() {
		return epochMilliseconds;
	}

	/**
	 * Gets the total number of milliseconds from the start of the Lukashian Calendar, up to the final point of the previous day.
	 */
	public long getEpochMillisecondsPreviousDay() {
		return epochMillisecondsPreviousDay;
	}

	/**
	 * Gets the total number of milliseconds from the start of the Lukashian Calendar, up to the first point of this day or 1 if this is the very first day of the calendar.
	 */
	public long getEpochMillisecondsAtStartOfDay() {
		return epochMillisecondsPreviousDay + 1;
	}

	/**
	 * Returns the year of this {@link Day}, which is the year this day starts in.
	 */
	public Year getYear() {
		return Year.of(store().getYearForEpochMilliseconds(this.getEpochMillisecondsAtStartOfDay()));
	}

	/**
	 * Returns the year that this {@link Day} ends in. This is not necessarily the same year as the one in which this day starts.
	 */
	public Year getEndYear() {
		return Year.of(store().getYearForEpochMilliseconds(this.getEpochMilliseconds()));
	}

	/**
	 * Returns the integer value of this {@link Day}, i.e. which day of the year this is.
	 */
	public int getDayNumber() {
		int firstEpochDayOfYear = getFirstDayOfYearInEpochForm(this.getYear());
		return (epochDay - firstEpochDayOfYear) + 1;
	}

	/**
	 * Returns the length of one beep on this {@link Day}, i.e. one-ten-thousandth of this {@link Day}, in milliseconds.
	 * Since the result is not a whole number, is it represented as a {@link BigFraction}.
	 */
	public BigFraction getLengthOfBeepInMilliseconds() {
		return BigFraction.of(this.lengthInMilliseconds(), BEEPS_PER_DAY);
	}

	/**
	 * Returns the amount of days between this day and the given non-null {@link Day}, directionally. Therefore, if this day is after the other day,
	 * the result will be a positive number. If this day is before the other day, the result will be a negative number. If they represent the same day,
	 * the result will be 0.
	 */
	public int differenceWith(Day other) {
		return Math.subtractExact(epochDay, other.epochDay);
	}

	/**
	 * Creates a new {@link Day} representing the given epoch day, i.e. the number of the day since the start of the calendar, irrespective of the year that
	 * the day is in, e.g. 'day 5000 since the epoch'.
	 *
	 * @throws LukashianException when the given epoch day is 0 or lower
	 */
	public static Day of(int epochDay) {
		return new Day(epochDay);
	}

	/**
	 * Creates a new {@link Day} representing the given day in the given year.
	 *
	 * @throws LukashianException when the given day does not exist for the given year
	 */
	public static Day of(Year year, int day) {
		if (year == null) {
			throw new LukashianException("The year of a day cannot be null");
		}
		if (day < 1) {
			throw new LukashianException(day + " is not a valid day, the minimum is 1");
		}
		if (day > year.getNumberOfDays()) {
			throw new LukashianException(day + " is not a valid day in year " + year.getYearNumber());
		}
		int firstEpochDayOfYear = getFirstDayOfYearInEpochForm(year);
		return Day.of((firstEpochDayOfYear + day) - 1);
	}

	/**
	 * Creates a new {@link Day} representing the given day in the given year.
	 *
	 * @throws LukashianException when the given year is 0 or lower or when the given day does not exist for the given year
	 */
	public static Day of(int year, int day) {
		return Day.of(Year.of(year), day);
	}

	/**
	 * Returns the current {@link Day}.
	 */
	public static Day now() {
		return Day.of(store().getEpochDayForEpochMilliseconds(store().getCurrentEpochMilliseconds()));
	}

	@Override
	public int compareTo(Day other) {
		return this.differenceWith(other);
	}

	@Override
	public int hashCode() {
		return epochDay;
	}

	@Override
	public boolean equals(Object object) {
		return object instanceof Day && ((Day) object).epochDay == epochDay;
	}

	@Override
	public String toString() {
		return "[Day: " + Formatter.format(this) + "]";
	}

	private static int getFirstDayOfYearInEpochForm(Year year) {
		long epochMillisecondsAtStartOfYear = year.getEpochMillisecondsAtStartOfYear();
		int runningEpochDayAtStartOfYear = store().getEpochDayForEpochMilliseconds(epochMillisecondsAtStartOfYear);

		long epochMillisecondsAtStartOfRunningDay = runningEpochDayAtStartOfYear == 1 ? 1 :
			store().getEpochMillisecondsForEpochDay(runningEpochDayAtStartOfYear - 1) + 1;

		if (epochMillisecondsAtStartOfRunningDay < epochMillisecondsAtStartOfYear) { //Present day at start of year started in previous year
			return runningEpochDayAtStartOfYear + 1;
		}
		return runningEpochDayAtStartOfYear;
	}
}
