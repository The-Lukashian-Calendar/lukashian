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

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.lukashian.store.MillisecondStore.store;

/**
 * Represents a unique millisecond on the timeline. This means that the very first millisecond on the timeline is millisecond 1. This is consistent with the
 * numbering of {@link Day}s and {@link Year}s, that also start at 1.
 * <p>
 * This class is also used to handle the concept of "time of day". In the Lukashian Calendar, the time of day is simply a {@link BigDecimal} that expresses
 * <i>the proportion of all milliseconds of the day that have <b>fully passed</b></i>. This {@link BigDecimal} can then be represented as, for example,
 * <a href="https://en.wikipedia.org/wiki/Basis_point">Basis Points</a>.
 * <p>
 * This means that the time of day is 0 during the first millisecond of the day, since that first millisecond hasn't fully passed yet. This also means that
 * millisecond 1 actually represents the start of the calendar, not one millisecond after the start and that the first millisecond of a {@link Day} or
 * {@link Year} represents the start of that {@link Day} or {@link Year}, not one millisecond after the start.
 * <p>
 * It also means that the proportion can never be 1 (or 10000 beeps), because by then, the next day has already started. After all, a day runs from its start
 * (inclusive), to its end (exclusive).
 * <p>
 * Note that this ONLY applies to the determination of the {@link BigDecimal} that expresses the time of day, it does not apply other parts of the calendar,
 * which are not affected by this matter. For example, when calling {@link Instant#getEpochMilliseconds()}, which returns the number of milliseconds since the
 * start of the calendar, it includes the millisecond of the instant itself.
 */
public final class Instant implements Comparable<Instant>, Serializable {

	/**
	 * The amount of beeps per day, see <a href="https://en.wikipedia.org/wiki/Basis_point">Basis Points</a>.
	 */
	public static final int BEEPS_PER_DAY = 10000;

	/**
	 * The scale to use for {@link BigDecimal} divide operations. This is high enough to make sure that a {@link BigDecimal} representing a proportion of a day will have a granularity
	 * that is higher than 0.1 millisecond. Since the Lukashian Calendar has a resolution of a millisecond, this should then never lead to rounding issues.
	 */
	private static final int BIGDECIMAL_DIV_SCALE = 12;

	private long epochMilliseconds;

	private Instant(long epochMilliseconds) {
		if (epochMilliseconds < 1) {
			throw new LukashianException(epochMilliseconds + " is not a valid value, the minimum is 1");
		}
		this.epochMilliseconds = epochMilliseconds;
	}

	/**
	 * Returns a new {@link Instant} that represents the passed proportion of this instant's day on this instant's day minus the given amount of days, for
	 * example, if this instant represents a point at one third of its day, then calling this method will return an instant that represents one third of the
	 * resulting day.
	 * <p>
	 * Calling this method might result in a {@link Instant} that is in a different year.
	 *
	 * @throws LukashianException when the result would be before the start of the Lukashian Calendar
	 */
	public Instant minusDays(int daysToSubtract) {
		return Instant.of(this.getDay().minusDays(daysToSubtract), this.getProportionOfDay());
	}

	/**
	 * Returns a new {@link Instant} that represents the passed proportion of this instant's day on this instant's day plus the given amount of days, for
	 * example, if this instant represents a point at one third of its day, then calling this method will return an instant that represents one third of the
	 * resulting day.
	 * <p>
	 * Calling this method might result in a {@link Instant} that is in a different year.
	 */
	public Instant plusDays(int daysToAdd) {
		return Instant.of(this.getDay().plusDays(daysToAdd), this.getProportionOfDay());
	}

	/**
	 * Returns a new {@link Instant} that represents the passed proportion of this instant's day on the previous day, for example, if this instant represents a
	 * point at one third of its day, then calling this method will return an instant that represents one third of the previous day.
	 * <p>
	 * Calling this method might result in a {@link Instant} that is in a different year.
	 *
	 * @throws LukashianException when the result would be before the start of the Lukashian Calendar
	 */
	public Instant atPreviousDay() {
		return this.minusDays(1);
	}

	/**
	 * Returns a new {@link Instant} that represents the passed proportion of this instant's day on the next day, for example, if this instant represents a
	 * point at one third of its day, then calling this method will return an instant that represents one third of the next day.
	 * <p>
	 * Calling this method might result in a {@link Instant} that is in a different year.
	 *
	 * @throws LukashianException when the result would be before the start of the Lukashian Calendar
	 */
	public Instant atNextDay() {
		return this.plusDays(1);
	}

	/**
	 * Returns a new {@link Instant} that represents this instant minus the given amount of milliseconds. This might result in an {@link Instant} that is in a
	 * different year.
	 * <p>
	 * Please note that if the resulting {@link Instant} is formatted with the default options in {@link Formatter}, it may look the same as the original if the change is less than 1 beep and
	 * rounding will lead to the same beep.
	 *
	 * @throws LukashianException when the result would be before the start of the Lukashian Calendar
	 */
	public Instant minusMilliseconds(long millisecondsToSubtract) {
		if (millisecondsToSubtract < 0) { //To not have to deal with negatives
			return this.plusMilliseconds(Math.negateExact(millisecondsToSubtract));
		}
		return Instant.of(Math.subtractExact(this.getEpochMilliseconds(), millisecondsToSubtract));
	}

	/**
	 * Returns a new {@link Instant} that represents this instant minus the given amount of seconds. This might result in an {@link Instant} that is in a
	 * different year.
	 * <p>
	 * Please note that if the resulting {@link Instant} is formatted with the default options in {@link Formatter}, it may look the same as the original if the change is less than 1 beep and
	 * rounding will lead to the same beep.
	 *
	 * @throws LukashianException when the result would be before the start of the Lukashian Calendar
	 */
	public Instant minusSeconds(long secondsToSubtract) {
		return this.minusMilliseconds(secondsToSubtract * 1000);
	}

	/**
	 * Returns a new {@link Instant} that represents this instant plus the given amount of milliseconds. This might result in an {@link Instant} that is in a
	 * different year.
	 * <p>
	 * Please note that if the resulting {@link Instant} is formatted with the default options in {@link Formatter}, it may look the same as the original if the change is less than 1 beep and
	 * rounding will lead to the same beep.
	 */
	public Instant plusMilliseconds(long millisecondsToAdd) {
		if (millisecondsToAdd < 0) { //To not have to deal with negatives
			return this.minusMilliseconds(Math.negateExact(millisecondsToAdd));
		}
		return Instant.of(Math.addExact(this.getEpochMilliseconds(), millisecondsToAdd));
	}

	/**
	 * Returns a new {@link Instant} that represents this instant plus the given amount of seconds. This might result in an {@link Instant} that is in a
	 * different year.
	 * <p>
	 * Please note that if the resulting {@link Instant} is formatted with the default options in {@link Formatter}, it may look the same as the original if the change is less than 1 beep and
	 * rounding will lead to the same beep.
	 */
	public Instant plusSeconds(long secondsToAdd) {
		return this.plusMilliseconds(secondsToAdd * 1000);
	}

	/**
	 * Returns a new {@link Instant} that represents this instant minus the given proportion of a {@link Day}. This might result in an {@link Instant} that is in a
	 * different day or year. The value of the passed {@link BigDecimal} will be interpreted in such a way that 1 is a full day. So 0.5 will represent half a day and 2.5 will represent two
	 * and a half days.
	 * <p>
	 * Please note that since the duration of a day is not constant, the durations of the various parts of the proportion that are subtracted may not be constant either if subtracting that proportion will
	 * lead to an {@link Instant} that is on a different day. For example, if this {@link Instant} is at 0.2 of the current day and 0.4 is subtracted, this will lead to an {@link Instant} that is
	 * at 0.8 of the previous day. This means that the first 0.2 day that was subtracted has the duration of 20% of the current day and the last 0.2 day that was subtracted has the duration of 20%
	 * of the previous day.
	 * <p>
	 * The same principle applies if more than 1 day is subtracted.
	 * <p>
	 * Please note that if the resulting {@link Instant} is formatted with the default options in {@link Formatter}, it may look the same as the original if the change is less than 1 beep and
	 * rounding will lead to the same beep.
	 *
	 * @throws LukashianException when the result would be before the start of the Lukashian Calendar
	 */
	public Instant minusProportionOfDay(BigDecimal proportionToSubtract) {
		if (proportionToSubtract.compareTo(BigDecimal.ZERO) < 0) { //To not have to deal with negatives
			return this.plusProportionOfDay(proportionToSubtract.negate());
		}
		BigDecimal totalProportion = this.getProportionOfDay().subtract(proportionToSubtract);
		int daysToSubtract = totalProportion.divideToIntegralValue(BigDecimal.ONE.negate()).intValue();
		BigDecimal remainingProportion = totalProportion.remainder(BigDecimal.ONE.negate());

		if (remainingProportion.compareTo(BigDecimal.ZERO) < 0) {
			daysToSubtract++; //Add one on top of the amount of whole days
			remainingProportion = remainingProportion.add(BigDecimal.ONE);
		}
		return Instant.of(this.getDay().minusDays(daysToSubtract), remainingProportion);
	}

	/**
	 * Returns a new {@link Instant} that represents this instant minus the given amount of beeps. This might result in an {@link Instant} that is in a different day or year.
	 * <p>
	 * Please note that the duration of a beep is 1/10000th of a day. Since the duration of a day is not constant, the duration of the beeps that are subtracted may not be constant either if
	 * subtracting those beeps will lead to an {@link Instant} that is on a different day. For example, if this {@link Instant} is at 2000 beeps of the current day and 4000 beeps are subtracted,
	 * this will lead to an {@link Instant} that is at 8000 beeps of the previous day. This means that the first 2000 beeps that were subtracted have the duration of 2000/10000th of the current day
	 * and the last 2000 beeps that were subtracted have the duration of 2000/10000th of the previous day.
	 * <p>
	 * The same principle applies if more than 1 day's worth of beeps are subtracted.
	 * <p>
	 * Please note that if the resulting {@link Instant} is formatted with the default options in {@link Formatter}, it may look the same as the original if the change is less than 1 beep and
	 * rounding will lead to the same beep.
	 *
	 * @throws LukashianException when the result would be before the start of the Lukashian Calendar
	 */
	public Instant minusBeeps(int beepsToSubtract) {
		return this.minusProportionOfDay(new BigDecimal(beepsToSubtract).divide(new BigDecimal(BEEPS_PER_DAY), BIGDECIMAL_DIV_SCALE, RoundingMode.HALF_UP));
	}

	/**
	 * Returns a new {@link Instant} that represents this instant plus the given proportion of a {@link Day}. This might result in an {@link Instant} that is in a
	 * different day or year. The value of the passed {@link BigDecimal} will be interpreted in such a way that 1 is a full day. So 0.5 will represent half a day and 2.5 will represent two
	 * and a half days.
	 * <p>
	 * Please note that since the duration of a day is not constant, the durations of the various parts of the proportion that are added may not be constant either if adding that proportion will
	 * lead to an {@link Instant} that is on a different day. For example, if this {@link Instant} is at 0.8 of the current day and 0.4 is added, this will lead to an {@link Instant} that is
	 * at 0.2 of the next day. This means that the first 0.2 day that was added has the duration of 20% of the current day and the last 0.2 day that was added has the duration of 20% of the next day.
	 * <p>
	 * The same principle applies if more than 1 day is added.
	 * <p>
	 * Please note that if the resulting {@link Instant} is formatted with the default options in {@link Formatter}, it may look the same as the original if the change is less than 1 beep and
	 * rounding will lead to the same beep.
	 */
	public Instant plusProportionOfDay(BigDecimal proportionToAdd) {
		if (proportionToAdd.compareTo(BigDecimal.ZERO) < 0) { //To not have to deal with negatives
			return this.minusProportionOfDay(proportionToAdd.negate());
		}
		BigDecimal totalProportion = this.getProportionOfDay().add(proportionToAdd);
		int daysToAdd = totalProportion.divideToIntegralValue(BigDecimal.ONE).intValue();
		BigDecimal remainingProportion = totalProportion.remainder(BigDecimal.ONE);
		return Instant.of(this.getDay().plusDays(daysToAdd), remainingProportion);
	}

	/**
	 * Returns a new {@link Instant} that represents this instant plus the given amount of beeps. This might result in an {@link Instant} that is in a different day or year.
	 * <p>
	 * Please note that the duration of a beep is 1/10000th of a day. Since the duration of a day is not constant, the duration of the beeps that are added may not be constant either if
	 * adding those beeps will lead to an {@link Instant} that is on a different day. For example, if this {@link Instant} is at 8000 beeps of the current day and 4000 beeps are added,
	 * this will lead to an {@link Instant} that is at 2000 beeps of the next day. This means that the first 2000 beeps that were added have the duration of 2000/10000th of the current day
	 * and the last 2000 beeps that were added have the duration of 2000/10000th of the next day.
	 * <p>
	 * The same principle applies if more than 1 day's worth of beeps are added.
	 * <p>
	 * Please note that if the resulting {@link Instant} is formatted with the default options in {@link Formatter}, it may look the same as the original if the change is less than 1 beep and
	 * rounding will lead to the same beep.
	 */
	public Instant plusBeeps(int beepsToAdd) {
		return this.plusProportionOfDay(new BigDecimal(beepsToAdd).divide(new BigDecimal(BEEPS_PER_DAY), BIGDECIMAL_DIV_SCALE, RoundingMode.HALF_UP));
	}

	/**
	 * Returns whether this instant is before the given {@link Instant}, not null.
	 */
	public boolean isBefore(Instant other) {
		return epochMilliseconds < other.epochMilliseconds;
	}

	/**
	 * Returns whether this instant is the same or before the given {@link Instant}, not null.
	 */
	public boolean isSameOrBefore(Instant other) {
		return epochMilliseconds <= other.epochMilliseconds;
	}

	/**
	 * Returns whether this instant is after the given {@link Instant}, not null.
	 */
	public boolean isAfter(Instant other) {
		return epochMilliseconds > other.epochMilliseconds;
	}

	/**
	 * Returns whether this instant is the same or after the given {@link Instant}, not null.
	 */
	public boolean isSameOrAfter(Instant other) {
		return epochMilliseconds >= other.epochMilliseconds;
	}

	/**
	 * Returns whether this instant is in the given {@link Year}.
	 */
	public boolean isIn(Year year) {
		return year.contains(this);
	}

	/**
	 * Returns whether this instant is in the given {@link Day}.
	 */
	public boolean isIn(Day day) {
		return day.contains(this);
	}

	/**
	 * Gets the total number of milliseconds from the start of the Lukashian Calendar, up to the final point of the millisecond represented by this
	 * instant. This is, by definition, the number of the very millisecond that this instant represents. This method therefore returns the same
	 * result as {@link #getMillisecond()}.
	 */
	public long getEpochMilliseconds() {
		return epochMilliseconds;
	}

	/**
	 * Gets the total number of milliseconds from the start of the Lukashian Calendar, up to the final point of the millisecond represented by this
	 * instant. This is, by definition, the number of the very millisecond that this instant represents. This method therefore returns the same
	 * result as {@link #getEpochMilliseconds()}.
	 */
	public long getMillisecond() {
		return epochMilliseconds;
	}

	/**
	 * Returns the {@link Day} of this instant.
	 */
	public Day getDay() {
		return Day.of(store().getEpochDayForEpochMilliseconds(this.getEpochMilliseconds()));
	}

	/**
	 * Returns the year that this instant is in. Note that this might be different from the year that the {@link Day} of this instant is in,
	 * because a {@link Day} is part of the {@link Year} in which it starts, but it does not necessarily end in that year. If the year of the
	 * day of this instant is needed, please call getDay().getYear().
	 */
	public Year getYear() {
		return Year.of(store().getYearForEpochMilliseconds(this.getEpochMilliseconds()));
	}

	/**
	 * For the {@link Day} of this instant, gets the <i>the proportion of all milliseconds that has <b>fully passed</b></i> at the time of the
	 * <i>start of this instant</i>, so, not including the millisecond represented by this instant itself.
	 * <p>
	 * This is in order to achieve a value from 0 (inclusive) and 1 (exclusive). Please see the javadoc of {@link Instant} for an explanation.
	 */
	public BigDecimal getProportionOfDay() {
		Day day = this.getDay();
		long millisecondsOfDay = day.lengthInMilliseconds();
		long millisecondsPassed = epochMilliseconds - day.getEpochMillisecondsAtStartOfDay();
		return BigDecimal.valueOf(millisecondsPassed).divide(BigDecimal.valueOf(millisecondsOfDay), BIGDECIMAL_DIV_SCALE, RoundingMode.HALF_UP);
	}

	/**
	 * Gets the {@link #getProportionOfDay()}, represented as <a href="https://en.wikipedia.org/wiki/Basis_point">beeps</a>.
	 * This will result in an int between 0 (inclusive) and 9999 (inclusive). Please see the javadoc of {@link Instant} for an explanation.
	 * <p>
	 * Rounding is not performed here, because that would require a correction to prevent overflowing into the next day, which in turn will lead to the first beep of a
	 * day lasting for only half a beep and the last one lasting for one and a half (because the last half would otherwise be rounded to the first beep of the following day).
	 * <p>
	 * As a result, the amount of beeps that is returned by this method is the amount of beeps that have <b>fully passed</b> at the time of the <i>start of this instant</i>.
	 */
	public int getBeeps() {
		return this.getProportionOfDay().multiply(new BigDecimal(BEEPS_PER_DAY)).intValue();
	}

	/**
	 * Gets the total number of milliseconds from the UNIX Epoch, up to the final point of the millisecond represented by this
	 * instant. For instants that occurred before the UNIX Epoch, a negative number is returned.
	 */
	public long getUnixEpochMilliseconds() {
		return store().getUnixEpochMilliseconds(epochMilliseconds);
	}

	/**
	 * Gets the Java {@link java.time.Instant} that represents the same point in time as this {@link Instant}.
	 */
	public java.time.Instant toJavaInstant() {
		return java.time.Instant.ofEpochMilli(this.getUnixEpochMilliseconds());
	}

	/**
	 * Returns the amount of milliseconds between this instant and the given {@link Instant}, directionally. Therefore, if this instant is after the other
	 * instant, the result will be a positive number. If this instant is before the other instant, the result will be a negative number. If they represent the
	 * same {@link Instant} on the timeline, the result will be 0.
	 */
	public long differenceWith(Instant other) {
		return Math.subtractExact(epochMilliseconds, other.epochMilliseconds);
	}

	/**
	 * Returns the amount of beeps between this instant and the given {@link Instant}, directionally. Therefore, if this instant is after the other
	 * instant, the result will be a positive number. If this instant is before the other instant, the result will be a negative number. If they represent the
	 * same {@link Instant} on the timeline, the result will be 0.
	 * <p>
	 * Please note that the duration of a beep is 1/10000th of a day. Since the duration of a day is not constant, the duration of the beeps that constitute the returned difference
	 * may not be constant either if this instant is compared to an {@link Instant} that is on a different day. For example, if this {@link Instant} is at 2000 beeps of the current day and
	 * it is compared to an {@link Instant} that is at 8000 beeps of the previous day, the result will be 4000. 2000 of these beeps have the duration of 2000/10000th of the current day
	 * and the other 2000 beeps have the duration of 2000/10000th of the previous day.
	 * <p>
	 * The same principle applies if there is more than 1 day between the compared {@link Instant}s.
	 * <p>
	 * If the exact difference between 2 {@link Instant}s is needed, please use {@link #differenceWith(Instant)}.
	 */
	public int differenceInBeepsWith(Instant other) {
		//Calculate the difference of the proportions, to keep as much information as possible
		BigDecimal proportionDifference = this.getProportionOfDay().subtract(other.getProportionOfDay());

		//Multiply and round, to keep as much information as possible
		int beepDifference = proportionDifference.multiply(new BigDecimal(BEEPS_PER_DAY)).setScale(0, RoundingMode.HALF_UP).intValue();

		//Calculate the difference of the days
		int dayDifference = this.getDay().differenceWith(other.getDay()) * BEEPS_PER_DAY;

		return beepDifference + dayDifference;
	}

	/**
	 * Creates a new {@link Instant} representing the given number of milliseconds since the start of the Lukashian Calendar.
	 *
	 * @throws LukashianException when the given number of milliseconds is lower than 0
	 */
	public static Instant of(long epochMilliseconds) {
		return new Instant(epochMilliseconds);
	}

	/**
	 * Creates a new {@link Instant} that represents the millisecond that occurs after the given proportion of the given day has passed.
	 *
	 * @throws LukashianException when the given proportion is not between 0 (inclusive) and 1 (exclusive)
	 */
	public static Instant of(Day day, BigDecimal proportionOfDay) {
		if (proportionOfDay.compareTo(BigDecimal.ZERO) < 0 || proportionOfDay.compareTo(BigDecimal.ONE) >= 0) {
			throw new LukashianException("Proportion of day must be between 0 (inclusive) and 1 (exclusive)");
		}
		long millisecondsOfDay = day.lengthInMilliseconds();

		//Multiply and round, to keep as much information as possible
		long millisecondsPassed = BigDecimal.valueOf(millisecondsOfDay).multiply(proportionOfDay).setScale(0, RoundingMode.HALF_UP).longValue();

		//Should the rounding inadvertently lead to a value that is not on the Day anymore, use the last millisecond of the Day
		millisecondsPassed = Math.min(millisecondsPassed, millisecondsOfDay - 1);

		return Instant.of(day.getEpochMillisecondsAtStartOfDay() + millisecondsPassed);
	}

	/**
	 * Creates a new {@link Instant} that represents the millisecond that occurs after the given proportion of the given day has passed.
	 *
	 * @throws LukashianException when the given day does not exist for the given year or when the given proportion is not between 0 (inclusive) and 1 (exclusive)
	 */
	public static Instant of(Year year, int day, BigDecimal proportionOfDay) {
		return Instant.of(Day.of(year, day), proportionOfDay);
	}

	/**
	 * Creates a new {@link Instant} that represents the millisecond that occurs after the given proportion of the given day has passed.
	 *
	 * @throws LukashianException when the given year is 0 or lower or when the given day does not exist for the given year or when the given proportion is not
	 * between 0 (inclusive) and 1 (exclusive)
	 */
	public static Instant of(int year, int day, BigDecimal proportionOfDay) {
		return Instant.of(Day.of(year, day), proportionOfDay);
	}

	/**
	 * Creates a new {@link Instant} that represents the millisecond that occurs after the given proportion of the given day has passed.
	 *
	 * @throws LukashianException when the given proportion is not between 0 (inclusive) and 9999 (inclusive)
	 */
	public static Instant of(Day day, int beeps) {
		return Instant.of(day, new BigDecimal(beeps).divide(new BigDecimal(BEEPS_PER_DAY)));
	}

	/**
	 * Creates a new {@link Instant} that represents the millisecond that occurs after the given proportion of the given day has passed.
	 *
	 * @throws LukashianException when the given day does not exist for the given year or when the given proportion is not between 0 (inclusive) and 9999 (inclusive)
	 */
	public static Instant of(Year year, int day, int beeps) {
		return Instant.of(Day.of(year, day), beeps);
	}

	/**
	 * Creates a new {@link Instant} that represents the millisecond that occurs after the given proportion of the given day has passed.
	 *
	 * @throws LukashianException when the given year is 0 or lower or when the given day does not exist for the given year or when the given proportion is not
	 * between 0 (inclusive) and 9999 (inclusive)
	 */
	public static Instant of(int year, int day, int beeps) {
		return Instant.of(Day.of(year, day), beeps);
	}

	/**
	 * Creates a new {@link Instant} representing the amount of milliseconds since the UNIX Epoch. Negative numbers are allowed.
	 *
	 * @throws LukashianException when the given value would result in a point before the start of the Lukashian Calendar
	 */
	public static Instant ofUnixEpochMilliseconds(long unixEpochMilliseconds) {
		return Instant.of(store().getLukashianEpochMilliseconds(unixEpochMilliseconds));
	}

	/**
	 * Creates a new {@link Instant} representing the same point in time as the given Java {@link java.time.Instant}.
	 *
	 * @throws LukashianException when the given value would result in a point before the start of the Lukashian Calendar
	 */
	public static Instant ofJavaInstant(java.time.Instant javaInstant) {
		return Instant.ofUnixEpochMilliseconds(javaInstant.toEpochMilli());
	}

	/**
	 * Returns the current {@link Instant}.
	 */
	public static Instant now() {
		return Instant.of(store().getCurrentEpochMilliseconds());
	}

	@Override
	public int compareTo(Instant other) {
		return Long.compare(epochMilliseconds, other.epochMilliseconds);
	}

	@Override
	public int hashCode() {
		return Long.valueOf(epochMilliseconds).hashCode();
	}

	@Override
	public boolean equals(Object object) {
		return object instanceof Instant && ((Instant) object).epochMilliseconds == epochMilliseconds;
	}

	@Override
	public String toString() {
		return "[Instant: " + Formatter.format(this) + "]";
	}
}
