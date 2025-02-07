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

/**
 * For each {@link Day} and {@link Year}, an instance of this class provices the number of milliseconds between the start of the calendar
 * (the Lukashian epoch) and the end of that day or year (we call this 'epoch milliseconds'). It also stores the offset between the Lukashian
 * epoch and the UNIX epoch. Using these numbers, the actual years and days of the Lukashian Calendar are defined. An implementation of
 * {@link MillisecondStoreDataProvider} has to provide the following data:
 *
 * <ul>
 * 	<li>The number of epoch milliseconds for each year in an array where the index + 1 is the year and the value is the number of
 * 		epoch milliseconds for that year, i.e. how many milliseconds have passed from the beginning of the calendar up until the final
 * 		point of that year.</li>
 * 	<li>The number of epoch milliseconds for each day in an array where the index + 1 is the epoch day and the value is the number of
 * 		epoch milliseconds for that day, i.e. how many milliseconds have passed from the beginning of the calendar up until the final
 * 		point of that day. This does not take into account the year that each day is in, it simply lists all days in the calendar
 * 		(epoch days).</li>
 * 	<li>The offset in milliseconds between the Lukashian epoch (i.e. the start of the Lukashian Calendar) and the UNIX epoch in such a way
 * 		that "unixEpochOffsetMilliseconds + millisecondsSinceUnixEpoch = millisecondsSinceLukashianEpoch", in other words, when the UNIX
 * 		epoch is AFTER the Lukashian epoch, it needs to be a positive number. It needs to be measured around the point of the UNIX epoch,
 * 		before any UNIX leap seconds were added.</li>
 * </ul>
 *
 * Please note that this allows for various implementations of the Lukashian Calendar Mechanism. The standard implementation ("The
 * Lukashian Calendar"), represented by the {@link StandardEarthMillisecondStoreDataProvider}, stores data that makes the Lukashian
 * Calendar:
 *
 * <ul>
 * 	<li>For <a href="https://en.wikipedia.org/wiki/Tropical_year">Solar Earth Years</a> that run from
 * 		<a href="https://en.wikipedia.org/wiki/Southern_solstice">Southern Solstice</a> to
 * 		<a href="https://en.wikipedia.org/wiki/Southern_solstice">Southern Solstice</a></li>
 * 	<li>For <a href="https://en.wikipedia.org/wiki/Solar_time">True Solar Earth Days</a></li>
 * 	<li>With the year number approximately 3900 higher than the Gregorian Calendar</li>
 * 	<li>All measured according to <a href="https://en.wikipedia.org/wiki/Terrestrial_Time">Terrestrial Time</a></li>
 * </ul>
 *
 * It is also, for example, perfectly possibly to create a {@link MillisecondStoreDataProvider} that loads the data for Astronomical Mars years,
 * Astronomical Mars days, starting 10000 Mars years in the past and measured according to Barycentric Coordinate Time. Please refer to
 * {@link MillisecondStore} for information on how to specify which instance of {@link MillisecondStoreDataProvider} to use.
 * <p>
 * This implementation of The Lukashian Calendar Mechanism assumes that each year and each day is at least 3 milliseconds long, i.e.
 * there's a first millisecond, a last one and at least one in between. It might work for years and days that last less than 3 milliseconds,
 * but this has not been tested and is not guaranteed.
 * <p>
 * Following the previous constraint, this implementation of The Lukashian Calendar Mechanism also assumes that each year is at least 3
 * days long, i.e. there's a first day, a last one and at least one in between.
 * <p>
 * Implementations of this interface do not have to provide any caching functionality: each method is called only once and the result is
 * stored for future reference by the {@link MillisecondStore}. This happens when the singleton instance of the {@link MillisecondStore} is
 * initialized, which happens during most interactions with any of the calendar classes.
 */
public interface MillisecondStoreDataProvider {

	long loadUnixEpochOffsetMilliseconds();

	long[] loadYearEpochMilliseconds();

	long[] loadDayEpochMilliseconds(long[] yearEpochMilliseconds);
}
