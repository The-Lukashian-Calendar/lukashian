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
package org.lukashian.store.provider;

import org.lukashian.store.MillisecondStoreDataProvider;

/**
 * An implementation of the {@link MillisecondStoreDataProvider} that implements the Lukashian Calendar Mechanism, resulting in a Lukashian Calendar:
 *
 * <ul>
 * 	<li>For <a href="https://en.wikipedia.org/wiki/Tropical_year">Solar Mars Years</a> that run from
 * 		<a href="https://en.wikipedia.org/wiki/Southern_solstice">Southern Solstice</a> to
 * 		<a href="https://en.wikipedia.org/wiki/Southern_solstice">Southern Solstice</a></li>
 * 	<li>For <a href="https://en.wikipedia.org/wiki/Solar_time">True (or apparent) Solar Mars Days (not Mean Solar Mars Days)</a></li>
 * </ul>
 *
 * The standard Mars implementation of the Lukashian Calendar Mechanism defines a year as a Solar Mars Year
 * (or Tropical Mars Year), i.e. a single rotation of the Mars around the Sun, in terms of the cycle of the seasons.
 * <p>
 * Similarly, it defines a day as a True (or apparent) Solar Mars Day, i.e. a single rotation of the Mars around its own axis, in terms of its angle towards
 * the Sun.
 * <p>
 * The reason that <a href="https://en.wikipedia.org/wiki/Southern_solstice">Southern Solstice</a> was chosen for the turn of the year is because it,
 * intuitively, seems a better starting point than any other point of the year.
 * <p>
 * By definition, the very first instant of the Lukashian Calendar (Lukashian Epoch), is also the very first instant of the very first day AND the very
 * first year. Therefore, the very first day starts at exactly the same instant as the very first year. Since there's no whole number of Solar Days per
 * Solar Year, the Lukashian Epoch is likely to be the only case where a day starts at the same instant as a year.
 * <p>
 * By definition, the Lukashian Epoch is at the exact instant of a particular southern solstice. So, which southern solstice was chosen to be the first one?
 * In other words: when does the Mars instance of the Lukashian Calendar start?
 * <p>
 * Since the very first day starts at the same instant as the very first year, the southern solstice that is chosen as the start of the calendar also
 * determines when the turn of the day will be, since there are no time zones in the Lukashian Calendar. The turn of every single day happens at the position
 * of the planet at the start of the calendar.
 * <p>
 * The southern solstice that was chosen to be the Lukashian Epoch is the one TODO
 * This southern solstice was chosen for the following reasons:
 *
 * <ul>
 * 	<li>All of human history involving Mars can be expressed in the Lukashian Calendar (see <a href="https://en.wikipedia.org/wiki/Human_mission_to_Mars">here</a>).</li>
 * 	<li>The turn of day is at or around nighttime for the inhabitants of the first Human Settlement on Mars.</li>
 * </ul>
 *
 * This class uses the following NASA documentation to implement the calculations for the days and years:
 * <ul>
 * 	<li><a href="https://www.giss.nasa.gov/pubs/abs/al04000r.html">Allison 1997</a></li>
 * 	<li><a href="https://www.giss.nasa.gov/tools/mars24/help/notes.html">Technical Notes on Mars Solar Time as Adopted by the Mars24 Sunclock</a></li>
 * </ul>
 *
 * For more information, also see <a href="https://www.planetary.org/articles/mars-calendar">this article</a> of the Planetary Society.
 */
public class StandardMarsMillisecondStoreDataProvider implements MillisecondStoreDataProvider {

	@Override
	public long loadUnixEpochOffsetMilliseconds() {
		return 0; //TODO
	}

	@Override
	public long[] loadYearEpochMilliseconds() {
		return new long[]{}; //TODO
	}

	@Override
	public long[] loadDayEpochMilliseconds(long[] yearEpochMilliseconds) {
		return new long[]{}; //TODO
	}
}

/*

## Setup

library(dplyr)
library(lubridate)

script.dir <- dirname(sys.frame(1)$ofile)
source(paste0(script.dir, '/trig_degrees.R'))

## Example arguments

datetime = '2000-01-06 00:00:00'
tzone = 'UTC'

millis <- datetime  %>%
  ymd_hms(tz = tzone) %>%
  as.integer() * 1000

## Mars24 algorithm

Mars24 <- function(millis) {

     ## A-2: Convert millis to Julian Date (UT)

     jdUT <- 2440587.5 + (millis / (8.64 * 1e7))

     ## A-3: Determine time offset from J2000 epoch (UT)

     epoch.J2000 <- ymd_hms('2000-01-01 00:00:00')

     T <- ifelse(as_datetime(millis/1000) < epoch.J2000, (jdUT - 2451545.0) / 36525, 0)

     ## A-4: Determine UTC to TT conversion

     tt.minus.ut <- 64.184 + (59 * T) - (51.2 * T^2) - (67.1 * T^3) - (16.4 * T^4)

     ## A-5: Determine Julian Date (TT)

     jdTT <- jdUT + (tt.minus.ut / 86400)

     ## A-6: Determine time offset from J2000 Epoch (TT)

     deltaTJ2000 <- jdTT - 2451545.0

     ## B-1: Determine Mars mean anomaly

     M <- 19.3871 + (0.52402073 * deltaTJ2000)

     ## B-2: Determine angle of Fiction Mean Sun

     alphaFMS <- 270.3871 + (0.524038496 * deltaTJ2000)

     ## B-3: Determine perturbers

     ### Note use of custom trig functions. Default trig functions in R only accept
     ### arguments in radians.

     alpha <- c(0.0071, 0.0057, 0.0039, 0.0037, 0.0021, 0.0020, 0.0018)
     tau <- c(2.2353, 2.7543, 1.1177, 15.7866, 2.1354, 2.4694, 32.8493)
     phi <- c(49.409, 168.173, 191.837, 21.736, 15.704, 95.528, 49.095)

     PBS <- sum(alpha * cos_deg(((0.985626 * deltaTJ2000 / tau) + phi)))

     ## B-4: Determine Equation of Center

     v.minus.M <- (10.691 + 3.0 * 1e-7 * deltaTJ2000) * sin_deg(M) +
          0.623 * sin_deg(2 * M) + 0.050 * sin_deg(3 * M) + 0.005 * sin_deg(4 * M) +
          0.0005 * sin_deg(5 * M) + PBS

     ## B-5: Determine aerocentric solar longitude

     Ls <- alphaFMS + v.minus.M

     ## C-1: Determine Equation of Time

     EOT <- 2.861 * sin_deg(2 * Ls) - 0.071 * sin_deg(4 * Ls) +
          0.002 * sin_deg(6 * Ls) - v.minus.M

     ## C-2: Determine Coordinated Mars Time (ie Airy Mean Time)

     MTC <- (24 * (((jdTT - 2451549.5) / 1.0274912517) + 44796.0 - 0.0009626)) %% 24

     ## Return dataframe

     equation <- c('A-1', 'A-2', 'A-3', 'A-4', 'A-5', 'A-6', 'B-1', 'B-2',
                   'B-3', 'B-4', 'B-5', 'C-1', 'C-2')

     description <- c('Get a starting Earth time in millis',
                      'Convert millis to Julian Date (UT)',
                      'Determine time offset from J2000 epoch (UT)',
                      'Determine UTC to TT conversion',
                      'Determine Julian Date (TT)',
                      'Determine time offset from JS2000 epoch (TT)',
                      'Determine Mars mean anomaly',
                      'Determine angle of Fiction Mean Sun',
                      'Determine perturbers', 'Determine Equation of Center',
                      'Determine aerocentric solar longitude',
                      'Determine Equation of Time', 'Determine Coordinated Mars Time')

     value <- c(millis, jdUT, T, tt.minus.ut, jdTT, deltaTJ2000, M, alphaFMS,
                PBS, v.minus.M, Ls, EOT, MTC)

         output <- data.frame(equation = equation, description = description,
                               value = value)
         return(output)

}

## Convert Earth to Mars

earth2mars_convert <- function(millis, verbose=FALSE) {
  calculations <- Mars24(millis)

  if(verbose==TRUE) {
    print(calculations)
  }

  return(calculations$value[13])
}

 */
