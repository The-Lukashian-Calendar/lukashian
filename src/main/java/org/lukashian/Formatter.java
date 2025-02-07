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

import java.util.function.Function;

/**
 * Provides methods for formatting instances of the Lukashian Calendar classes. This implementation is not as sophisticated as the Java formatting classes, but
 * should provide sufficient functionality for most use cases.
 */
public final class Formatter {

	/**
	 * Represents the different ways to format a {@link Day}.
	 */
	public enum DayFormat {

		/**
		 * Format the epoch day, i.e. the how manieth day it is since the start of the Lukashian Calendar, irrespective of the year, for example: '15592'.
		 */
		EPOCH,

		/**
		 * Format the year first, then the day inside the year, for example: '5919-43'.
		 */
		YEAR_FIRST,

		/**
		 * Format the day first, then the year of the day, for example: '43-5919'.
		 */
		DAY_FIRST,

		/**
		 * Format the day only, for example: '43'.
		 */
		DAY_ONLY
	}

	/**
	 * Formats the given year by simply returning the year as a {@link String}.
	 */
	public static String format(Year year) {
		return Integer.toString(year.getYearNumber());
	}

	/**
	 * Formats the given {@link Day} according to the given {@link DayFormat}, using the given separator between the day and the year. If
	 * {@link DayFormat#EPOCH} or {@link DayFormat#DAY_ONLY} is chosen, then the separator is not used.
	 */
	public static String format(Day day, DayFormat format, String separator) {
		if (format == DayFormat.EPOCH) {
			return Integer.toString(day.getEpochDay());

		} else if (format == DayFormat.YEAR_FIRST) {
			return format(day.getYear()) + separator + day.getDayNumber();

		} else if (format == DayFormat.DAY_FIRST) {
			return day.getDayNumber() + separator + format(day.getYear());

		} else if (format == DayFormat.DAY_ONLY) {
			return Integer.toString(day.getDayNumber());

		} else {
			throw new IllegalStateException();
		}
	}

	/**
	 * Formats the given {@link Day}, according to the given {@link DayFormat}, with '-' as a separator.
	 * If {@link DayFormat#EPOCH} or {@link DayFormat#DAY_ONLY} is chosen, then the separator is not used.
	 */
	public static String format(Day day, DayFormat format) {
		return format(day, format, "-");
	}

	/**
	 * Formats the given {@link Day} with {@link DayFormat#YEAR_FIRST} and '-' as a separator.
	 */
	public static String format(Day day) {
		return format(day, DayFormat.YEAR_FIRST);
	}

	/**
	 * Formats the given time, using the given {@link Function}.
	 */
	public static String format(BigFraction proportionOfDay, Function<BigFraction, String> formatter) {
		return formatter.apply(proportionOfDay);
	}

	/**
	 * Formats the given time by transforming it into an integer number between 0 (inclusive) and 9999 (inclusive), see
	 * <a href="https://en.wikipedia.org/wiki/Basis_point">Basis Points</a>.
	 * <p>
	 * For a detailed explanation on how the proportion is interpreted, see {@link Instant#getBeeps()}.
	 */
	public static String format(BigFraction proportionOfDay) {
		return format(proportionOfDay, bd -> String.format("%04d", proportionOfDay.multiply(Instant.BEEPS_PER_DAY).intValue()));
	}

	/**
	 * Formats the given {@link Instant} using {@link #format(Day, DayFormat, String)} and {@link #format(BigFraction, Function)}, using a space to separate
	 * the two.
	 */
	public static String format(Instant instant, DayFormat dayFormat, String daySeparator, Function<BigFraction, String> formatter) {
		return format(instant.getDay(), dayFormat, daySeparator) + " " + format(instant.getProportionOfDay(), formatter);
	}

	/**
	 * Formats the given {@link Instant} using {@link #format(Day, DayFormat)} and {@link #format(BigFraction, Function)}, using a space to separate the
	 * two.
	 */
	public static String format(Instant instant, DayFormat dayFormat, Function<BigFraction, String> formatter) {
		return format(instant.getDay(), dayFormat) + " " + format(instant.getProportionOfDay(), formatter);
	}

	/**
	 * Formats the given {@link Instant} using {@link #format(Day)} and {@link #format(BigFraction)}, using a space to separate the two.
	 */
	public static String format(Instant instant) {
		return format(instant.getDay()) + " " + format(instant.getProportionOfDay());
	}
}
