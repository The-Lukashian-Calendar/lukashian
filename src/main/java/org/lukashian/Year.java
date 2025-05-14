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

import org.lukashian.store.StandardEarthMillisecondStoreDataProvider;

import java.io.Serializable;

import static org.lukashian.store.MillisecondStore.store;

/**
 * Represents a year in the Lukashian Calendar Mechanism. For the meaning of a year in the standard implementation of the Lukashian Calendar, see
 * {@link StandardEarthMillisecondStoreDataProvider}.
 * <p>
 * The first year is year 1 and years before year 1 are not defined. This is consistent with the numbering of days, since they also start at 1. Also, there's
 * a subtle difference between numbering and counting and when it comes to years, numbering is more appropriate, since numbering is giving each year a numeric
 * designation.
 * <p>
 * By definition, the exact point at which a year starts belongs to that year, not to the previous year, which ends at that point. So, a year
 * runs from its start (inclusive) to its end (exclusive).
 * <p>
 * In the Lukashian Calendar, the length of a year in milliseconds is not constant. Instead, it accurately represents the actual length of a particular year in
 * astronomical terms. Due to astronomical and planetary developments, this duration is not constant. Since the same is true for days (a day accurately represents
 * the actual length of a particular day in astronomical terms), a day is not necessarily contained in a single year. In fact, it is very unlikely that the turn
 * of a year coincides with the turn of a day. Therefore, a day is defined to be part of the year in which it started.
 * <p>
 * A {@link Year} keeps track of the total number of milliseconds that have taken place from the start of the Lukashian Calendar (Lukashian epoch) until
 * the final point of this year. This value is called "epoch milliseconds". This information allows for determining which year a day is in and how to
 * do various calculations. A {@link Year} also keeps track of the epoch milliseconds of the previous {@link Year}, for calculation purposes.
 * This means that a {@link Year} knows how many milliseconds it lasts, by subtracting the two.
 * <p>
 * {@link Year} is an immutable object. New instances are always created when calling one of the mutation methods.
 */
public final class Year implements Comparable<Year>, Serializable {

	private int year;
	private long epochMilliseconds;
	private long epochMillisecondsPreviousYear;

	private Year(int year) {
		if (year < 1) {
			throw new LukashianException(year + " is not a valid year, the minimum is 1");
		}
		this.year = year;
		this.epochMilliseconds = store().getEpochMillisecondsForYear(year);
		this.epochMillisecondsPreviousYear = year == 1 ? 0 : store().getEpochMillisecondsForYear(year - 1);
	}

	/**
	 * Returns a new {@link Year} that represents this year minus the given amount of years.
	 *
	 * @throws LukashianException when the result would be year 0 or lower
	 */
	public Year minusYears(int yearsToSubtract) {
		if (yearsToSubtract < 0) { //To not have to deal with negatives
			return this.plusYears(Math.negateExact(yearsToSubtract));
		}
		return Year.of(Math.subtractExact(year, yearsToSubtract));
	}

	/**
	 * Returns a new {@link Year} that represents this year plus the given amount of years.
	 */
	public Year plusYears(int yearsToAdd) {
		if (yearsToAdd < 0) { //To not have to deal with negatives
			return this.minusYears(Math.negateExact(yearsToAdd));
		}
		return Year.of(Math.addExact(year, yearsToAdd));
	}

	/**
	 * Returns a new {@link Year} that represents the year preceding this year.
	 *
	 * @throws LukashianException when the result would be year 0 or lower
	 */
	public Year previous() {
		return this.minusYears(1);
	}

	/**
	 * Returns a new {@link Year} that represents the year succeeding this year.
	 */
	public Year next() {
		return this.plusYears(1);
	}

	/**
	 * Returns a new {@link Day} that represents the {@link Day} of this year that is denoted by the given day number.
	 *
	 * @throws LukashianException when the given day number does not exist for this {@link Year}
	 */
	public Day atDay(int day) {
		return Day.of(this, day);
	}

	/**
	 * Returns a new {@link Day} that represents the first {@link Day} of this year.
	 */
	public Day firstDay() {
		return Day.of(this, 1);
	}

	/**
	 * Returns a new {@link Day} that represents the last {@link Day} of this year. Please note that this {@link Day} may end in the next year.
	 */
	public Day lastDay() {
		return Day.of(store().getEpochDayForEpochMilliseconds(this.getEpochMilliseconds()));
	}

	/**
	 * Returns a new {@link Instant} that represents the first {@link Instant} of this year. This is not necessarily at the start of a day.
	 */
	public Instant firstInstant() {
		return Instant.of(this.getEpochMillisecondsAtStartOfYear());
	}

	/**
	 * Returns a new {@link Instant} that represents the last {@link Instant} of this year. This is not necessarily at the end of a day.
	 */
	public Instant lastInstant() {
		return Instant.of(this.getEpochMilliseconds());
	}

	/**
	 * Returns whether this year is before the given non-null {@link Year}.
	 */
	public boolean isBefore(Year other) {
		return year < other.year;
	}

	/**
	 * Returns whether this year is the same or before the given non-null {@link Year}.
	 */
	public boolean isSameOrBefore(Year other) {
		return year <= other.year;
	}

	/**
	 * Returns whether this year is after the given non-null {@link Year}.
	 */
	public boolean isAfter(Year other) {
		return year > other.year;
	}

	/**
	 * Returns whether this year is the same or after the given non-null {@link Year}.
	 */
	public boolean isSameOrAfter(Year other) {
		return year >= other.year;
	}

	/**
	 * Returns whether the given non-null {@link Day}, is part of this year. A {@link Day} is part of a year if it started in that year.
	 */
	public boolean contains(Day day) {
		return this.equals(day.getYear());
	}

	/**
	 * Returns whether the given non-null {@link Day}, is not part of this year. A {@link Day} is part of a year if it started in that year.
	 */
	public boolean containsNot(Day day) {
		return !this.contains(day);
	}

	/**
	 * Returns whether the given non-null {@link Instant}, is inside this year.
	 * <p>
	 * Please note that, even when the {@link Day} of the given {@link Instant} starts within this year, the time component of the {@link Instant} may still
	 * cause the {@link Instant} to not be part of this year, returning false.
	 */
	public boolean contains(Instant instant) {
		long instantEpochMilliseconds = instant.getEpochMilliseconds();
		return instantEpochMilliseconds >= this.getEpochMillisecondsAtStartOfYear() && instantEpochMilliseconds <= epochMilliseconds;
	}

	/**
	 * Returns whether the given non-null {@link Instant}, is not inside this year.
	 * <p>
	 * Please note that, even when the {@link Day} of the given {@link Instant} does not start within this year, the time component of the {@link Instant} may still
	 * cause the {@link Instant} to be part of this year, returning false.
	 */
	public boolean containsNot(Instant instant) {
		return !this.contains(instant);
	}

	/**
	 * Gets the total number of milliseconds from the start of this year, up to the final point of this year. This number varies from year to year, depending
	 * on astronomical and planetary developments.
	 */
	public long lengthInMilliseconds() {
		return epochMilliseconds - epochMillisecondsPreviousYear;
	}

	/**
	 * Gets the number of days in this year.
	 */
	public int getNumberOfDays() {
		return this.lastDay().getDayNumber();
	}

	/**
	 * Returns the integer value of this {@link Year}, i.e. which year this is.
	 */
	public int getYearNumber() {
		return year;
	}

	/**
	 * Gets the total number of milliseconds from the start of the Lukashian Calendar, up to the final point of this year.
	 */
	public long getEpochMilliseconds() {
		return epochMilliseconds;
	}

	/**
	 * Gets the total number of milliseconds from the start of the Lukashian Calendar, up to the final point of the previous year.
	 */
	public long getEpochMillisecondsPreviousYear() {
		return epochMillisecondsPreviousYear;
	}

	/**
	 * Gets the total number of milliseconds from the start of the Lukashian Calendar, up to the first point of this year or 1 if this is the very first year.
	 */
	public long getEpochMillisecondsAtStartOfYear() {
		return epochMillisecondsPreviousYear + 1;
	}

	/**
	 * Returns the difference of the numerical year values of this year and the given non-null {@link Year}. Therefore, if this year has a higher numerical
	 * year value than the other year, the result will be a positive number. If this year has a lower numerical year value than the other year, the result
	 * will be a negative number. If they represent the same year, the result will be 0.
	 */
	public int differenceWith(Year other) {
		return Math.subtractExact(year, other.year);
	}

	/**
	 * Creates a new {@link Year} representing the given year.
	 *
	 * @throws LukashianException when the given year is 0 or lower
	 */
	public static Year of(int year) {
		return new Year(year);
	}

	/**
	 * Returns the current {@link Year}.
	 */
	public static Year now() {
		return Year.of(store().getYearForEpochMilliseconds(store().getCurrentEpochMilliseconds()));
	}

	@Override
	public int compareTo(Year other) {
		return this.differenceWith(other);
	}

	@Override
	public int hashCode() {
		return year;
	}

	@Override
	public boolean equals(Object object) {
		return object instanceof Year && ((Year) object).year == year;
	}

	@Override
	public String toString() {
		return "[Year: " + Formatter.format(this) + "]";
	}
}
