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

import java.util.ArrayList;
import java.util.Arrays;

import static java.lang.Math.*;

/**
 * An implementation of the {@link MillisecondStoreDataProvider} that implements the Lukashian Calendar Mechanism, resulting in a Lukashian Calendar:
 *
 * <ul>
 * 	<li>For <a href="https://en.wikipedia.org/wiki/Tropical_year">Solar Earth Years</a> that run from
 * 		<a href="https://en.wikipedia.org/wiki/Southern_solstice">Southern Solstice</a> to
 * 		<a href="https://en.wikipedia.org/wiki/Southern_solstice">Southern Solstice</a></li>
 * 	<li>For <a href="https://en.wikipedia.org/wiki/Solar_time">True (or apparent) Solar Earth Days (not Mean Solar Earth Days)</a></li>
 * 	<li>With the year number approximately 3900 higher than the Gregorian Calendar</li>
 * 	<li>All measured according to <a href="https://en.wikipedia.org/wiki/Terrestrial_Time">Terrestrial Time</a></li>
 * </ul>
 *
 * The standard implementation of the Lukashian Calendar Mechanism ("The Lukashian Calendar") defines a year as a Solar Earth Year
 * (or Tropical Earth Year), i.e. a single rotation of the Earth around the Sun, in terms of the cycle of the seasons. See
 * <a href="https://en.m.wikipedia.org/wiki/Tropical_year">here</a> for more details.
 * <p>
 * Similarly, it defines a day as a True (or apparent) Solar Earth Day, i.e. a single rotation of the Earth around its own axis, in terms of its angle towards
 * the Sun. See <a href="https://en.wikipedia.org/wiki/Solar_time">here</a> for more details.
 * <p>
 * The reason that <a href="https://en.wikipedia.org/wiki/Southern_solstice">Southern Solstice</a> was chosen for the turn of the year is because it,
 * intuitively, seems a better starting point than any other point of the year. Also, it almost coincides with the turn of the year in the Gregorian Calendar,
 * which is a nice side effect to have.
 * <p>
 * By definition, the very first instant of the Lukashian Calendar (Lukashian Epoch), is also the very first instant of the very first day AND the very
 * first year. Therefore, the very first day starts at exactly the same instant as the very first year. Since there's no whole number of Solar Days per
 * Solar Year, the Lukashian Epoch is likely to be the only case where a day starts at the same instant as a year.
 * <p>
 * By definition, the Lukashian Epoch is at the exact instant of a particular southern solstice. So, which southern solstice was chosen to be the first one?
 * In other words: when does the Lukashian Calendar start?
 * <p>
 * Since the very first day starts at the same instant as the very first year, the southern solstice that is chosen as the start of the calendar also
 * determines when the turn of the day will be, since there are no time zones in the Lukashian Calendar. The turn of every single day happens at the position
 * of the planet at the start of the calendar.
 * <p>
 * The southern solstice that was chosen to be the Lukashian Epoch is the one with the current year approximately 3900 higher than the current year in the
 * Gregorian Calendar. Approximately, because the turn of the year of the Gregorian Calendar does not coincide with Southern Solstice. This southern solstice
 * was chosen for the following reasons:
 *
 * <ul>
 * 	<li>All of human history for which there exists a known, accurate time can be expressed in the Lukashian Calendar (see
 * 		<a href="https://en.m.wikipedia.org/wiki/Timeline_of_human_prehistory">here</a>).</li>
 * 	<li>The last 2 digits of the Lukashian year are the same as the Gregorian year for most of the year (simply change the '20' into a '59').</li>
 * 	<li>The turn of day is at or around nighttime for the vast majority of the world's population (from westernmost Europe to easternmost Asia).</li>
 * </ul>
 *
 * This class uses the book "Astronomical Algorithms" by Jean Meeus to implement the calculations for the days and years.
 */
public class StandardMarsMillisecondStoreDataProvider implements MillisecondStoreDataProvider {

	private static final double[] A = new double[] {485, 203, 199, 182, 156, 136, 77, 74, 70, 58, 52, 50, 45, 44, 29, 18, 17, 16, 14, 12, 12, 12, 9, 8};
	private static final double[] B = new double[] {324.96, 337.23, 342.08, 27.85, 73.14, 171.52, 222.54, 296.72, 243.58, 119.81, 297.17, 21.02,
													247.54, 325.15, 60.93, 155.12, 288.79, 198.04, 199.76, 95.39, 287.11, 320.81, 227.73, 15.45};
	private static final double[] C = new double[] {1934.136, 32964.467, 20.186, 445267.112, 45036.886, 22518.443, 65928.934, 3034.906, 9037.513,
													33718.147, 150.678, 2281.226, 29929.562, 31555.956, 4443.417, 67555.328, 4562.452, 62894.029,
													31436.921, 14577.848, 31931.756, 34777.259, 1222.114, 16859.074};

	@Override
	public long loadUnixEpochOffsetMilliseconds() {
		//This was calculated as follows, with this method returning 0 and no leap seconds in the MillisecondStore (put this code in some main method):
		//ZonedDateTime gregorianWinterSolstice1970 = ZonedDateTime.of(1970, 12, 22, 6, 35, 43, 0, ZoneId.of("Z")); //A known value
		//Instant lukashianWinterSolstice5870 = Year.of(5870).lastInstant();
		//long gregorianWinterSolstice1970UnixEpochMillis = gregorianWinterSolstice1970.toInstant().toEpochMilli();
		//long lukashianWinterSolstice5870UnixEpochMillis = lukashianWinterSolstice5870.getUnixEpochMilliseconds();
		//long unixEpochOffsetMilliseconds = lukashianWinterSolstice5870UnixEpochMillis - gregorianWinterSolstice1970UnixEpochMillis;
		//System.out.println("UNIX Epoch Offset Milliseconds: " + unixEpochOffsetMilliseconds);
		//if (true) return 0L; //For initial Unix offset calculation

		return 185208761225352L;
	}

	@Override
	public long[] loadYearEpochMilliseconds() {
		//See https://stellafane.org/misc/equinox.html
		//See http://www.astropixels.com/ephemeris/soleq2001.html
		//See Chapter 27 of "Astronomical Algorithms" by Jean Meeus

		long jdeMillisAtStartOfCalendar = this.getJdeMillisAtEndOfYear(0);
		long[] yearEpochMilliseconds = new long[7000];
		for (int year = 1; year <= 7000; year++) {
			yearEpochMilliseconds[year - 1] = this.getJdeMillisAtEndOfYear(year) - jdeMillisAtStartOfCalendar;
		}
		return yearEpochMilliseconds;
	}

	@Override
	public long[] loadDayEpochMilliseconds(long[] yearEpochMilliseconds) {
		//See https://en.wikipedia.org/wiki/Day#Leap_seconds
		//See https://en.wikipedia.org/wiki/Leap_second#Slowing_rotation_of_the_Earth
		//See https://en.wikipedia.org/wiki/Earth%27s_rotation
		//See https://en.wikipedia.org/wiki/Solar_time
		//See https://en.wikipedia.org/wiki/Equation_of_time#Alternative_calculation
		//See Chapter 38 of "Astronomical Algorithms" by Jean Meeus

		//Calculating the true solar day length is done by using the mean solar day length as a basis and then adjusting each day with the Equation of Time.
		//When determining the mean solar day length, we take into account lengthening of the days due to tidal forces.
		//This method of calculation achieves a certain level of stability, because any discrepancy in the equation-of-time-adjustment will only affect
		//the respective day and will never accumulate throughout the calendar; the next day will have a "fresh" calculation starting from its mean solar day length again.

		//Initialize perihelion array
		long jdeMillisAtStartOfCalendar = this.getJdeMillisAtEndOfYear(0);
		long[] baryCenterPerihelionEpochMilliseconds = new long[yearEpochMilliseconds.length + 2]; //+2 to make sure that last few days of last year also have a perihelion
		for (int year = 0; year < baryCenterPerihelionEpochMilliseconds.length; year++) { //Start at 0, because we want the days of the first year to also have a most recent perihelion
			baryCenterPerihelionEpochMilliseconds[year] = this.getJdeMillisForBaryCenterPerihelion(year) - jdeMillisAtStartOfCalendar;
		}

		//Initialize variables for calculating the duration of a Mean Solar Day, taking into account the slowing rotation of the Earth, but not taking into account the Equation of Time
		long centurialIncreaseInNanos = 1_700_000L; //Known value
		double dailyIncreaseInNanos = centurialIncreaseInNanos / (100 * 365.25);

		long lengthOfMeanSolarDayAtYear5900InNanos = 86_400_002_000_000L; //Known value
		long increaseBetweenEpochAndYear5900InNanos = centurialIncreaseInNanos * 59;
		long lengthOfMeanSolarDayAtEpochInNanos = lengthOfMeanSolarDayAtYear5900InNanos - increaseBetweenEpochAndYear5900InNanos;

		//Initialize ArrayList that will hold the days
		ArrayList<Long> dayEpochMilliseconds = new ArrayList<>(yearEpochMilliseconds.length * 370); //Make sure there's enough capacity

		//Initialize day loop
		int currentDay = 1;
		double epochNanosOfCurrentMeanSolarDay = 0;
		long epochMillisOfEndOfFinalYear = yearEpochMilliseconds[yearEpochMilliseconds.length - 1];
		while (dayEpochMilliseconds.isEmpty() || dayEpochMilliseconds.getLast() < epochMillisOfEndOfFinalYear) {
			//Calculate mean solar day length for this day
			double increaseSinceEpochInNanos = dailyIncreaseInNanos * (currentDay - 1);
			double lengthOfCurrentMeanSolarDayInNanos = lengthOfMeanSolarDayAtEpochInNanos + increaseSinceEpochInNanos;

			epochNanosOfCurrentMeanSolarDay += lengthOfCurrentMeanSolarDayInNanos;
			long epochMillisOfCurrentMeanSolarDay = (long) (epochNanosOfCurrentMeanSolarDay / 1_000_000);

			//Get the most recent solstice
			int index = Arrays.binarySearch(yearEpochMilliseconds, epochMillisOfCurrentMeanSolarDay);
			index = index >= 0 ? index - 1 : -index - 2;
			long epochMillisOfMostRecentSolstice = index < 0 ? 0L : yearEpochMilliseconds[index];

			//Get the most recent perihelion
			index = Arrays.binarySearch(baryCenterPerihelionEpochMilliseconds, epochMillisOfCurrentMeanSolarDay);
			index = index >= 0 ? index - 1 : -index - 2;
			long epochMillisOfMostRecentBaryCenterPerihelion = baryCenterPerihelionEpochMilliseconds[index]; //index should always be >= 0 for perihelion array

			//Calculate Equation of Time and calculate true solar day

			//Note: whenever there's a rollover to the next most recent solstice or perihelion, there's a slight hiccup of around 300ms in the calculated epochMillisOfCurrentTrueSolarDay.
			//This is because the decimal part of daysSinceSolstice and daysSincePerihelion will shift by quite a bit (sometimes almost half a day), e.g. it goes from [363.48..., 364.48..., 365.48...] to [0.15..., 1.15..., 2.15...].
			//This discrepancy will then propagate through the calculation. Truncating the decimal part, or setting it to a fixed value (eg. 0.5) makes the hiccup larger and therefore does not provide a solution.
			//This issue is expected to disappear when we switch to a more comprehensive EOT based calculation or to a VSOP based calculation.
			//Another potential way to solve it, is not to rollover to the next most recent solstice or perihelion once a year, but interpolating the current position of the solstice and perihelion for every individual day.

			long millisSinceSolstice = epochMillisOfCurrentMeanSolarDay - epochMillisOfMostRecentSolstice;
			long millisSincePerihelion = epochMillisOfCurrentMeanSolarDay - epochMillisOfMostRecentBaryCenterPerihelion;

			double daysSinceSolstice = ((double) millisSinceSolstice) / (1000D * 3600 * 24);
			double daysSincePerihelion = ((double) millisSincePerihelion) / (1000D * 3600 * 24);

			double n = 360D / 365.24;
			double a = n * daysSinceSolstice;
			double b = a + 1.914 * sin(toRadians(n * daysSincePerihelion));
			double c = (a - (toDegrees(atan(tan(toRadians(b)) / cos(toRadians(23.44D)))))) / 180;
			double eotMinutes = 720D * (c - round(c));
			long eotMillis = (long) (eotMinutes * 60 * 1000);

			//Subtract eot, rather than add, because if eot is positive, apparent solar time is *ahead* of mean, which means that the *duration* of apparent is *shorter*, not longer
			long epochMillisOfCurrentTrueSolarDay = epochMillisOfCurrentMeanSolarDay - eotMillis;

			//Use this code if you want to display details of certain days
//			if (currentDay >= 2162615 && currentDay <= 2162619) {
//				System.out.println(
//					"Epoch day: " + currentDay + ", " +
//					"epochMillisOfCurrentMeanSolarDay: " + epochMillisOfCurrentMeanSolarDay + ", " +
//					"daysSinceSolstice: " + daysSinceSolstice + ", " +
//					"daysSincePerihelion: " + daysSincePerihelion + ", " +
//					"epochMillisOfCurrentTrueSolarDay: " + epochMillisOfCurrentTrueSolarDay
//				);
//			}

			//Add to List
			dayEpochMilliseconds.add(epochMillisOfCurrentTrueSolarDay);
			currentDay++;
		}
		return dayEpochMilliseconds.stream().mapToLong(Long::longValue).toArray();
	}

	private long getJdeMillisAtEndOfYear(int year) {
		/*
		 *				<-		GY		-><-  GY - 2000   ->
		 *
		 *	GY:	-2000	-1000	0		1000	2000	3000
		 *	LY:	1900	2900	3900	4900	5900	6900
		 */

		double jde0;
		if (year < 4900) {
			double y = (double) (year - 3900) / 1000; //Correct for Lukashian year offset
			jde0 =
				1721414.39987	+
				365242.88257	* y -
				0.00769			* y * y -
				0.00933			* y * y * y -
				0.00006			* y * y * y * y;
		} else {
			double y = (double) (year - 5900) / 1000; //Correct for Lukashian year offset
			jde0 =
				2451900.05952	+
				365242.74049	* y -
				0.06223			* y * y -
				0.00823			* y * y * y +
				0.00032			* y * y * y * y;
		}

		double t = (jde0 - 2451545.0) / 36525;
		double w = (t * 35999.373) - 2.47;
		double dL = (0.0334 * cos(toRadians(w))) + (0.0007 * cos(toRadians(2 * w))) + 1;

		double s = 0;
		for (int i = 0; i < 24; i++) {
			s += A[i] * cos(toRadians(B[i] + (C[i] * t)));
		}

		double jde = jde0 + ((0.00001 * s) / dL);
		return (long) (jde * 24 * 3600 * 1000);
	}

	private long getJdeMillisForBaryCenterPerihelion(int year) {
		long k = round(0.99997 * ((double) year - 5900.01));
		double jde = 2451547.507D + 365.2596358D * k + 0.0000000156D * k * k;
		return (long) (jde * 24 * 3600 * 1000);
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
