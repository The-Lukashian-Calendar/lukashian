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

import java.io.Serializable;
import java.math.RoundingMode;

import static org.lukashian.store.MillisecondStore.store;

/**
 * Represents a unique millisecond on the timeline. This means that the very first millisecond on the timeline is millisecond 1. This is consistent with the
 * numbering of {@link Day}s and {@link Year}s, that also start at 1.
 * <p>
 * This class is also used to handle the concept of "time of day". In the Lukashian Calendar, the time of day is simply a {@link BigFraction} that expresses
 * <i>the proportion of the day that has passed</i>. This {@link BigFraction} can then be represented as, for example,
 * <a href="https://en.wikipedia.org/wiki/Basis_point">Basis Points</a>.
 * <p>
 * The fact that an Instant is used to represent a unique millisecond on the timeline, as well as the proportion of the day that has passed, means that we
 * need to specify how to translate between those two:
 * <ul>
 *     <li>
 *         <b>From a proportion (as {@link BigFraction}) to a specific millisecond:</b>
 *         The thing to consider here is that a specific proportion (for example 0.12345th of the day), may not actually lead to a whole number of milliseconds
 *         to have passed on that day. The millisecond that is chosen as the result of the translation is the millisecond that the proportion "points to". The
 *         best way to visualize this is a wheel of fortune: the arrow is the proportion and the segments on the wheel are the milliseconds. It does not matter
 *         at which part of a segment the arrow points. If a certain proportion means that, for example, 120.01 milliseconds have passed on a certain day, then
 *         millisecond 121 of that day is chosen.
 *         <p>
 *         If a certain proportion points <i>exactly</i> at a boundary between milliseconds, then the millisecond <i>after</i> the boundary is chosen. For example,
 *         if a certain proportion means that exactly 120 milliseconds have <i>fully passed</i> (and hence we are at the start of millisecond 121), then millisecond
 *         121 of that day is chosen.
 *         <p>
 *         This effectively means that an Instant represents a specific millisecond <i>including</i> its start, but <i>excluding</i> its end, or in mathematical
 *         notation: <pre>[m></pre>. This also effectively means that the calendar does have a start, but does not have an end, which is as expected.
 *     </li>
 *     <li>
 *         <b>From a specific millisecond to a proportion (as {@link BigFraction}):</b>
 *         This is not a problem, because a BigFraction has exact precision for any rational number, so for a specific millisecond, we can always exactly calculate
 *         the proportion of the day that has passed. The only questions is: is the millisecond itself considered to have fully passed?
 *         <p>
 *         The answer is "no", the millisecond for which we calculate the proportion of the day that has passed, is itself <i>not</i> considered to have fully passed.
 *         In fact, this millisecond is considered to not have passed at all, meaning that we calculate the proportion of the day from the start of the day until the
 *         start of this millisecond. This means that the first millisecond of the day results in a proportion of 0 and the final millisecond of the day results in a
 *         proportion smaller than 1. This corresponds to the fact that a {@link Day} runs from its start (inclusive) to its end (exclusive).
 *     </li>
 * </ul>
 * Internally, an Instant stores its {@link Day} and the BigFraction that represents the proportion of its day that has passed. It does *not* store the unique millisecond
 * on the timeline that this Instant represents.
 * <p>
 * When creating an Instant using one of the constructors or methods that specifies the proportion of the day, the Instant stores the proportion with which it was created.
 * <p>
 * When creating an Instant using one of the constructors or methods that specifies the unique millisecond on the timeline, the translation to the corresponding
 * proportion is done as outlined above, after which the resulting {@link Day} and proportion (as BigFraction) will be stored.
 * <p>
 * The standard Java methods {@link #equals(Object)}, {@link #hashCode()} and {@link #compareTo(Instant)} look at the unique millisecond on the timeline that this Instant
 * represents, not the exact proportion of the day.
 * <p>
 * So, why not store the unique millisecond on the timeline that this Instant represents, instead of the {@link Day} and the proportion as a BigFraction? Remember the
 * translations: if we create an Instant at, say, 5000 beeps of a certain day, this proportion will "point" to a certain millisecond. It is unlikely that this proportion
 * will point exactly to a boundary between milliseconds. If we would then store said millisecond as the representation of the Instant, then what would happen if we
 * then calculate the number of beeps that have passed?
 * <p>
 * First, we would have to translate back to the proportion of the day that has passed. The millisecond itself is not considered to have passed at all (see the explanation
 * of the translations above), meaning that we won't get 0.5, but something like 0.499999999....... If this then gets truncated to beeps, we get 4999 instead of the
 * original 5000 (rounding cannot solve the problem, see {@link #getBeeps()} for details).
 * <p>
 * By storing the {@link Day} and the proportion with which the Instant was created, we do not have this problem.
 * <p>
 * The other way around is not a problem either. BigFraction has exact precision for any rational number, so translating a unique millisecond on the timeline to a
 * proportion, and then back again, is guaranteed to yield the original value.
 * <p>
 * {@link Instant} is an immutable object. New instances are always created when calling one of the mutation methods.
 */
public final class Instant implements Comparable<Instant>, Serializable {

	/**
	 * The amount of beeps per day, see <a href="https://en.wikipedia.org/wiki/Basis_point">Basis Points</a>.
	 */
	public static final int BEEPS_PER_DAY = 10000;

	private Day day;
	private BigFraction proportionOfDay;

	private Instant(Day day, BigFraction proportionOfDay) {
		if (proportionOfDay.compareTo(BigFraction.ZERO) < 0 || proportionOfDay.compareTo(BigFraction.ONE) >= 0) {
			throw new LukashianException("Proportion of day must be between 0 (inclusive) and 1 (exclusive)");
		}
		this.day = day;
		this.proportionOfDay = proportionOfDay;
	}

	/**
	 * Returns a new {@link Instant} that represents the passed proportion of this instant's day on this instant's day minus the given amount of years, for
	 * example, if this instant represents a point at one third of its day, then calling this method will return an instant that represents one third of the
	 * resulting day.
	 * <p>
	 * To see what the resulting day will be, see {@link Day#minusYears(int)}, which will be applied to this instant's day.
	 *
	 * @throws LukashianException when the result would be before the start of the Lukashian Calendar or when the resulting year does not have a day with this instant's day's number
	 */
	public Instant minusYears(int yearsToSubtract) {
		return Instant.of(day.minusYears(yearsToSubtract), proportionOfDay);
	}

	/**
	 * Returns a new {@link Instant} that represents the passed proportion of this instant's day on this instant's day plus the given amount of years, for
	 * example, if this instant represents a point at one third of its day, then calling this method will return an instant that represents one third of the
	 * resulting day.
	 * <p>
	 * To see what the resulting day will be, see {@link Day#plusYears(int)}, which will be applied to this instant's day.
	 *
	 * @throws LukashianException when the resulting year does not have a day with this instant's day's number
	 */
	public Instant plusYears(int yearsToAdd) {
		return Instant.of(day.plusYears(yearsToAdd), proportionOfDay);
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
		return Instant.of(day.minusDays(daysToSubtract), proportionOfDay);
	}

	/**
	 * Returns a new {@link Instant} that represents the passed proportion of this instant's day on this instant's day plus the given amount of days, for
	 * example, if this instant represents a point at one third of its day, then calling this method will return an instant that represents one third of the
	 * resulting day.
	 * <p>
	 * Calling this method might result in a {@link Instant} that is in a different year.
	 */
	public Instant plusDays(int daysToAdd) {
		return Instant.of(day.plusDays(daysToAdd), proportionOfDay);
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
	 * different day or year.
	 * <p>
	 * Please note that if the resulting {@link Instant} is formatted with the default options in {@link Formatter}, it may look the same as the original if the change is less than 1 beep.
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
	 * different day or year.
	 * <p>
	 * Please note that if the resulting {@link Instant} is formatted with the default options in {@link Formatter}, it may look the same as the original if the change is less than 1 beep.
	 *
	 * @throws LukashianException when the result would be before the start of the Lukashian Calendar
	 */
	public Instant minusSeconds(long secondsToSubtract) {
		return this.minusMilliseconds(secondsToSubtract * 1000);
	}

	/**
	 * Returns a new {@link Instant} that represents this instant plus the given amount of milliseconds. This might result in an {@link Instant} that is in a
	 * different day or year.
	 * <p>
	 * Please note that if the resulting {@link Instant} is formatted with the default options in {@link Formatter}, it may look the same as the original if the change is less than 1 beep.
	 */
	public Instant plusMilliseconds(long millisecondsToAdd) {
		if (millisecondsToAdd < 0) { //To not have to deal with negatives
			return this.minusMilliseconds(Math.negateExact(millisecondsToAdd));
		}
		return Instant.of(Math.addExact(this.getEpochMilliseconds(), millisecondsToAdd));
	}

	/**
	 * Returns a new {@link Instant} that represents this instant plus the given amount of seconds. This might result in an {@link Instant} that is in a
	 * different day or year.
	 * <p>
	 * Please note that if the resulting {@link Instant} is formatted with the default options in {@link Formatter}, it may look the same as the original if the change is less than 1 beep.
	 */
	public Instant plusSeconds(long secondsToAdd) {
		return this.plusMilliseconds(secondsToAdd * 1000);
	}

	/**
	 * Returns a new {@link Instant} that represents this instant minus the given proportion of a {@link Day}. This might result in an {@link Instant} that is in a
	 * different day or year. The value of the passed {@link BigFraction} will be interpreted in such a way that 1 is a full day. So 0.5 will represent half a day and 2.5 will represent two
	 * and a half days.
	 * <p>
	 * Please note that since the duration of a day is not constant, the durations of the various parts of the proportion that are subtracted may vary if subtracting that proportion will
	 * lead to an {@link Instant} that is on a different day. For example, if this {@link Instant} is at 0.2 of the current day and 0.4 is subtracted, this will lead to an {@link Instant} that is
	 * at 0.8 of the previous day. This means that the first 0.2 day that was subtracted has the duration of 20% of the current day and the last 0.2 day that was subtracted has the duration of 20%
	 * of the previous day.
	 * <p>
	 * The same principle applies if more than 1 day is subtracted.
	 * <p>
	 * Please note that if the resulting {@link Instant} is formatted with the default options in {@link Formatter}, it may look the same as the original if the change is less than 1 beep.
	 *
	 * @throws LukashianException when the result would be before the start of the Lukashian Calendar
	 */
	public Instant minusProportionOfDay(BigFraction proportionToSubtract) {
		if (proportionToSubtract.compareTo(BigFraction.ZERO) < 0) { //To not have to deal with negatives
			return this.plusProportionOfDay(proportionToSubtract.negate());
		}
		BigFraction totalProportion = proportionOfDay.subtract(proportionToSubtract);
		int daysToSubtract = totalProportion.negate().intValue();
		BigFraction remainingProportion = totalProportion.add(daysToSubtract);

		if (remainingProportion.compareTo(BigFraction.ZERO) < 0) {
			daysToSubtract++; //Add one on top of the amount of whole days
			remainingProportion = remainingProportion.add(BigFraction.ONE);
		}
		return Instant.of(day.minusDays(daysToSubtract), remainingProportion);
	}

	/**
	 * Returns a new {@link Instant} that represents this instant minus the given amount of beeps. This might result in an {@link Instant} that is in a different day or year.
	 * <p>
	 * Please note that the duration of a beep is 1/10000th of a day. Since the duration of a day is not constant, the duration of the beeps that are subtracted may vary if
	 * subtracting those beeps will lead to an {@link Instant} that is on a different day. For example, if this {@link Instant} is at 2000 beeps of the current day and 4000 beeps are subtracted,
	 * this will lead to an {@link Instant} that is at 8000 beeps of the previous day. This means that the first 2000 beeps that were subtracted have the duration of 2000/10000th of the current day
	 * and the last 2000 beeps that were subtracted have the duration of 2000/10000th of the previous day.
	 * <p>
	 * The same principle applies if more than 1 day's worth of beeps are subtracted.
	 * <p>
	 * Please note that if the resulting {@link Instant} is formatted with the default options in {@link Formatter}, it may look the same as the original if the change is less than 1 beep.
	 *
	 * @throws LukashianException when the result would be before the start of the Lukashian Calendar
	 */
	public Instant minusBeeps(int beepsToSubtract) {
		return this.minusProportionOfDay(BigFraction.of(beepsToSubtract, BEEPS_PER_DAY));
	}

	/**
	 * Returns a new {@link Instant} that represents this instant plus the given proportion of a {@link Day}. This might result in an {@link Instant} that is in a
	 * different day or year. The value of the passed {@link BigFraction} will be interpreted in such a way that 1 is a full day. So 0.5 will represent half a day and 2.5 will represent two
	 * and a half days.
	 * <p>
	 * Please note that since the duration of a day is not constant, the durations of the various parts of the proportion that are added may vary if adding that proportion will
	 * lead to an {@link Instant} that is on a different day. For example, if this {@link Instant} is at 0.8 of the current day and 0.4 is added, this will lead to an {@link Instant} that is
	 * at 0.2 of the next day. This means that the first 0.2 day that was added has the duration of 20% of the current day and the last 0.2 day that was added has the duration of 20% of the next day.
	 * <p>
	 * The same principle applies if more than 1 day is added.
	 * <p>
	 * Please note that if the resulting {@link Instant} is formatted with the default options in {@link Formatter}, it may look the same as the original if the change is less than 1 beep.
	 */
	public Instant plusProportionOfDay(BigFraction proportionToAdd) {
		if (proportionToAdd.compareTo(BigFraction.ZERO) < 0) { //To not have to deal with negatives
			return this.minusProportionOfDay(proportionToAdd.negate());
		}
		BigFraction totalProportion = proportionOfDay.add(proportionToAdd);
		int daysToAdd = totalProportion.intValue();
		BigFraction remainingProportion = totalProportion.subtract(daysToAdd);
		return Instant.of(day.plusDays(daysToAdd), remainingProportion);
	}

	/**
	 * Returns a new {@link Instant} that represents this instant plus the given amount of beeps. This might result in an {@link Instant} that is in a different day or year.
	 * <p>
	 * Please note that the duration of a beep is 1/10000th of a day. Since the duration of a day is not constant, the duration of the beeps that are added may vary if
	 * adding those beeps will lead to an {@link Instant} that is on a different day. For example, if this {@link Instant} is at 8000 beeps of the current day and 4000 beeps are added,
	 * this will lead to an {@link Instant} that is at 2000 beeps of the next day. This means that the first 2000 beeps that were added have the duration of 2000/10000th of the current day
	 * and the last 2000 beeps that were added have the duration of 2000/10000th of the next day.
	 * <p>
	 * The same principle applies if more than 1 day's worth of beeps are added.
	 * <p>
	 * Please note that if the resulting {@link Instant} is formatted with the default options in {@link Formatter}, it may look the same as the original if the change is less than 1 beep.
	 */
	public Instant plusBeeps(int beepsToAdd) {
		return this.plusProportionOfDay(BigFraction.of(beepsToAdd, BEEPS_PER_DAY));
	}

	/**
	 * Returns whether this instant is before the given non-null {@link Instant}. This will compare the unique milliseconds on the timeline that the Instants represent. It will not compare
	 * the proportions of the respective days that the Instants represent.
	 */
	public boolean isBefore(Instant other) {
		return this.getEpochMilliseconds() < other.getEpochMilliseconds();
	}

	/**
	 * Returns whether this instant is the same or before the given non-null {@link Instant}. This will compare the unique milliseconds on the timeline that the Instants represent. It will not compare
	 * the proportions of the respective days that the Instants represent.
	 */
	public boolean isSameOrBefore(Instant other) {
		return this.getEpochMilliseconds() <= other.getEpochMilliseconds();
	}

	/**
	 * Returns whether this instant is after the given non-null {@link Instant}. This will compare the unique milliseconds on the timeline that the Instants represent. It will not compare
	 * the proportions of the respective days that the Instants represent.
	 */
	public boolean isAfter(Instant other) {
		return this.getEpochMilliseconds() > other.getEpochMilliseconds();
	}

	/**
	 * Returns whether this instant is the same or after the given non-null {@link Instant}. This will compare the unique milliseconds on the timeline that the Instants represent. It will not compare
	 * the proportions of the respective days that the Instants represent.
	 */
	public boolean isSameOrAfter(Instant other) {
		return this.getEpochMilliseconds() >= other.getEpochMilliseconds();
	}

	/**
	 * Returns whether this instant is in the given non-null {@link Year}.
	 */
	public boolean isIn(Year year) {
		return year.contains(this);
	}

	/**
	 * Returns whether this instant is not in the given non-null {@link Year}.
	 */
	public boolean isNotIn(Year year) {
		return year.containsNot(this);
	}

	/**
	 * Returns whether this instant is in the given non-null {@link Day}.
	 */
	public boolean isIn(Day day) {
		return day.contains(this);
	}

	/**
	 * Returns whether this instant is not in the given non-null {@link Day}.
	 */
	public boolean isNotIn(Day day) {
		return day.containsNot(this);
	}

	/**
	 * Gets the unique millisecond on the timeline that this Instant represents. This is essentially the how-manieth millisecond on the
	 * entire calendar this Instant represents. See the javadoc of {@link Instant} for an explanation of how a proportion of a day is translated
	 * to a specific millisecond.
	 * <p>
	 * This method returns the same result as {@link #getMillisecond()}.
	 */
	public long getEpochMilliseconds() {
		long millisecondsOfDay = day.lengthInMilliseconds();

		//Calculate the millisecond that this proportion points to
		BigFraction resultingMillisecond = BigFraction.of(millisecondsOfDay).multiply(proportionOfDay);

		//If the proportion points to exact boundary between milliseconds, go to next millisecond
		if (resultingMillisecond.equals(BigFraction.of(resultingMillisecond.longValue()))) {
			resultingMillisecond = resultingMillisecond.add(BigFraction.ONE);
		}

		//Convert to long, round UP, so that it picks the millisecond that this proportion points at
		long resultingMillisecondLong = resultingMillisecond.bigDecimalValue(0, RoundingMode.UP).longValue();

		return day.getEpochMillisecondsPreviousDay() + resultingMillisecondLong;
	}

	/**
	 * Gets the unique millisecond on the timeline that this Instant represents. This is essentially the how-manieth millisecond on the
	 * entire calendar this Instant represents. See the javadoc of {@link Instant} for an explanation of how a proportion of a day is translated
	 * to a specific millisecond.
	 * <p>
	 * This method returns the same result as {@link #getEpochMilliseconds()}.
	 */
	public long getMillisecond() {
		return this.getEpochMilliseconds();
	}

	/**
	 * Returns the proportion of the {@link Day} of this instant that has passed.
	 */
	public BigFraction getProportionOfDay() {
		return proportionOfDay;
	}

	/**
	 * Returns the {@link Day} of this instant.
	 */
	public Day getDay() {
		return day;
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
	 * Gets the {@link #getProportionOfDay()}, represented as <a href="https://en.wikipedia.org/wiki/Basis_point">beeps</a>.
	 * This will result in an int between 0 (inclusive) and 9999 (inclusive). Please see the javadoc of {@link Instant} for an explanation.
	 * <p>
	 * Rounding is not performed here, because that would require a correction to prevent overflowing into the next day, which in turn will lead to the first beep of a
	 * day lasting for only half a beep and the last one lasting for one and a half (because the last half would otherwise be rounded to the first beep of the following day).
	 * <p>
	 * As a result, the amount of beeps that is returned by this method is the amount of beeps that have <b>fully passed</b> at the time of the <i>start of this instant</i>.
	 * This means that everything after the first 4 significant digits of the proportion of the day that this instant represents, will be truncated.
	 */
	public int getBeeps() {
		return proportionOfDay.multiply(BigFraction.of(BEEPS_PER_DAY)).intValue();
	}

	/**
	 * Gets the total number of milliseconds from the UNIX Epoch, up to the final point of the millisecond represented by this
	 * instant. For instants that occurred before the UNIX Epoch, a negative number is returned.
	 */
	public long getUnixEpochMilliseconds() {
		return store().getUnixEpochMilliseconds(this.getEpochMilliseconds());
	}

	/**
	 * Gets the Java {@link java.time.Instant} that represents the same point in time as this {@link Instant}.
	 */
	public java.time.Instant toJavaInstant() {
		return java.time.Instant.ofEpochMilli(this.getUnixEpochMilliseconds());
	}

	/**
	 * Returns the amount of milliseconds between this instant and the given non-null {@link Instant}, directionally. Therefore, if this instant is after the other
	 * instant, the result will be a positive number. If this instant is before the other instant, the result will be a negative number. If they represent the
	 * same {@link Instant} on the timeline, the result will be 0.
	 */
	public long differenceWith(Instant other) {
		return Math.subtractExact(this.getEpochMilliseconds(), other.getEpochMilliseconds());
	}

	/**
	 * Returns the amount of beeps between this instant and the given non-null {@link Instant}, directionally. Therefore, if this instant is after the other
	 * instant, the result will be a positive number. If this instant is before the other instant, the result will be a negative number. If they represent the
	 * same {@link Instant} on the timeline, the result will be 0.
	 * <p>
	 * Please note that the duration of a beep is 1/10000th of a day. Since the duration of a day is not constant, the duration of the beeps that constitute the returned difference
	 * may vary if this instant is compared to an {@link Instant} that is on a different day. For example, if this {@link Instant} is at 2000 beeps of the current day and
	 * it is compared to an {@link Instant} that is at 8000 beeps of the previous day, the result will be 4000. 2000 of these beeps have the duration of 2000/10000th of the current day
	 * and the other 2000 beeps have the duration of 2000/10000th of the previous day.
	 * <p>
	 * The same principle applies if there is more than 1 day between the compared {@link Instant}s.
	 * <p>
	 * If the difference in beeps between the the compared {@link Instant}s is not a whole number, then the decimal part of the result will be truncated.
	 * <p>
	 * If the exact difference between 2 {@link Instant}s is needed, please use {@link #differenceWith(Instant)}.
	 */
	public int differenceInBeepsWith(Instant other) {
		//Calculate the difference of the exact proportions
		BigFraction proportionDifference = proportionOfDay.subtract(other.getProportionOfDay());

		//Calculate the difference of the beeps
		int beepDifference = proportionDifference.multiply(BEEPS_PER_DAY).intValue();

		//Calculate the difference of the days
		int dayDifference = day.differenceWith(other.getDay()) * BEEPS_PER_DAY;

		return beepDifference + dayDifference;
	}

	/**
	 * Creates a new {@link Instant} representing the given number of milliseconds since the start of the Lukashian Calendar. See the javadoc of
	 * {@link Instant} for an explanation of how a millisecond is translated to a proportion of a day.
	 *
	 * @throws LukashianException when the given number of milliseconds is lower than 0
	 */
	public static Instant of(long epochMilliseconds) {
		Day day =  Day.of(store().getEpochDayForEpochMilliseconds(epochMilliseconds));
		long millisecondsOfDay = day.lengthInMilliseconds();

		long millisecondsPassed = epochMilliseconds - day.getEpochMillisecondsAtStartOfDay(); //Use getEpochMillisecondsAtStartOfDay in order not to count the millisecond itself as having passed
		BigFraction proportionOfDay = BigFraction.of(millisecondsPassed, millisecondsOfDay);
		return Instant.of(day, proportionOfDay);
	}

	/**
	 * Creates a new {@link Instant} that represents the millisecond at the point in time when the given proportion of the given day has passed.
	 * For more information on how this mechanism works, see the javadoc of {@link Instant}.
	 *
	 * @throws LukashianException when the given proportion is not between 0 (inclusive) and 1 (exclusive)
	 */
	public static Instant of(Day day, BigFraction proportionOfDay) {
		return new Instant(day, proportionOfDay);
	}

	/**
	 * Creates a new {@link Instant} that represents the millisecond at the point in time when the given proportion of the given day has passed.
	 *
	 * @throws LukashianException when the given day does not exist for the given year or when the given proportion is not between 0 (inclusive) and 1 (exclusive)
	 */
	public static Instant of(Year year, int day, BigFraction proportionOfDay) {
		return Instant.of(Day.of(year, day), proportionOfDay);
	}

	/**
	 * Creates a new {@link Instant} that represents the millisecond at the point in time when the given proportion of the given day has passed.
	 *
	 * @throws LukashianException when the given year is 0 or lower or when the given day does not exist for the given year or when the given proportion is not
	 * between 0 (inclusive) and 1 (exclusive)
	 */
	public static Instant of(int year, int day, BigFraction proportionOfDay) {
		return Instant.of(Day.of(year, day), proportionOfDay);
	}

	/**
	 * Creates a new {@link Instant} that represents the millisecond at the point in time when the given proportion of the given day has passed.
	 *
	 * @throws LukashianException when the given proportion is not between 0 (inclusive) and 9999 (inclusive)
	 */
	public static Instant of(Day day, int beeps) {
		return Instant.of(day, BigFraction.of(beeps, BEEPS_PER_DAY));
	}

	/**
	 * Creates a new {@link Instant} that represents the millisecond at the point in time when the given proportion of the given day has passed.
	 *
	 * @throws LukashianException when the given day does not exist for the given year or when the given proportion is not between 0 (inclusive) and 9999 (inclusive)
	 */
	public static Instant of(Year year, int day, int beeps) {
		return Instant.of(Day.of(year, day), beeps);
	}

	/**
	 * Creates a new {@link Instant} that represents the millisecond at the point in time when the given proportion of the given day has passed.
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
		return Long.compare(this.getEpochMilliseconds(), other.getEpochMilliseconds());
	}

	@Override
	public int hashCode() {
		return Long.valueOf(this.getEpochMilliseconds()).hashCode();
	}

	@Override
	public boolean equals(Object object) {
		return object instanceof Instant && ((Instant) object).getEpochMilliseconds() == this.getEpochMilliseconds();
	}

	@Override
	public String toString() {
		return "[Instant: " + Formatter.format(this) + "]";
	}
}
