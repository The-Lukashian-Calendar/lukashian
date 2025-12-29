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
 * The standard Earth implementation of the Lukashian Calendar Mechanism defines a year as a Solar Earth Year
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
 * In other words: when does the Earth instance of the Lukashian Calendar start?
 * <p>
 * Since the very first day starts at the same instant as the very first year, the southern solstice that is chosen as the start of the calendar also
 * determines when the turn of the day will be, since there are no time zones in the Lukashian Calendar. The turn of every single day happens at the position
 * of the planet at the start of the calendar.
 * <p>
 * The southern solstice that was chosen to be the Lukashian Epoch for the Earth instance is the one with the current year approximately 3900 higher than the
 * current year in the Gregorian Calendar. Approximately, because the turn of the year of the Gregorian Calendar does not coincide with Southern Solstice.
 * This southern solstice was chosen for the following reasons:
 *
 * <ul>
 * 	<li>All of <a href="https://en.m.wikipedia.org/wiki/Timeline_of_human_prehistory">human history</a> for which there exists a known, accurate time can
 * 		be expressed in the Lukashian Calendar.</li>
 * 	<li>The last 2 digits of the Lukashian year are the same as the Gregorian year for most of the year (simply change the '20' into a '59').</li>
 * 	<li>The turn of day is during nighttime for the vast majority of the world's population (from westernmost Europe to easternmost Asia).</li>
 * </ul>
 *
 * This class uses the book "Astronomical Algorithms, Second Edition" by Jean Meeus to implement the calculations for the days and years.
 */
public class StandardEarthMillisecondStoreDataProvider implements MillisecondStoreDataProvider {

	private static final double[] A = new double[] {485, 203, 199, 182, 156, 136, 77, 74, 70, 58, 52, 50, 45, 44, 29, 18, 17, 16, 14, 12, 12, 12, 9, 8};
	private static final double[] B = new double[] {324.96, 337.23, 342.08, 27.85, 73.14, 171.52, 222.54, 296.72, 243.58, 119.81, 297.17, 21.02,
													247.54, 325.15, 60.93, 155.12, 288.79, 198.04, 199.76, 95.39, 287.11, 320.81, 227.73, 15.45};
	private static final double[] C = new double[] {1934.136, 32964.467, 20.186, 445267.112, 45036.886, 22518.443, 65928.934, 3034.906, 9037.513,
													33718.147, 150.678, 2281.226, 29929.562, 31555.956, 4443.417, 67555.328, 4562.452, 62894.029,
													31436.921, 14577.848, 31931.756, 34777.259, 1222.114, 16859.074};

	@Override
	public long loadUnixEpochOffsetMilliseconds() {
		//This was calculated as follows, with this method returning 0 and no leap seconds in the MillisecondStoreData (put this code in some main method):
		//ZonedDateTime gregorianWinterSolstice1970 = ZonedDateTime.of(1970, 12, 22, 6, 35, 43, 0, ZoneId.of("Z")); //A known value
		//Instant lukashianWinterSolstice5870 = Year.of(5870).lastInstant();
		//long gregorianWinterSolstice1970UnixEpochMillis = gregorianWinterSolstice1970.toInstant().toEpochMilli();
		//long lukashianWinterSolstice5870UnixEpochMillis = lukashianWinterSolstice5870.getUnixEpochMilliseconds();
		//long unixEpochOffsetMilliseconds = lukashianWinterSolstice5870UnixEpochMillis - gregorianWinterSolstice1970UnixEpochMillis;
		//System.out.println("UNIX Epoch Offset Milliseconds: " + unixEpochOffsetMilliseconds);
		//This way of "pulling" the Lukashian Calendar in sync with the System Clock also takes into account the difference between TAI and TT

		return 185208761225352L;
	}

	@Override
	public long[] loadYearEpochMilliseconds() {
		//See https://stellafane.org/misc/equinox.html
		//See http://www.astropixels.com/ephemeris/soleq2001.html
		//See Chapter 27 of "Astronomical Algorithms, Second Edition" by Jean Meeus

		long jdeMillisAtStartOfCalendar = this.getJdeMillisAtEndOfYear(0);
		long[] yearEpochMilliseconds = new long[7000];
		for (int year = 1; year <= 7000; year++) {
			yearEpochMilliseconds[year - 1] = this.getJdeMillisAtEndOfYear(year) - jdeMillisAtStartOfCalendar;
		}
		return yearEpochMilliseconds;
	}

	@Override
	public long[] loadDayEpochMilliseconds(long[] yearEpochMilliseconds) {
		//See https://en.wikipedia.org/wiki/Solar_time
		//See https://en.wikipedia.org/wiki/Equation_of_time
		//See https://en.wikipedia.org/wiki/Earth%27s_rotation
		//See Chapters 22, 25, 28 of "Astronomical Algorithms, Second Edition" by Jean Meeus

		long jdeMillisAtStartOfCalendar = this.getJdeMillisAtEndOfYear(0);

		//Initialize variables for calculating the duration of a Mean Solar Day, taking into account the slowing rotation of the Earth
		long centurialIncreaseInNanos = 1_700_000L; //Known value
		double dailyIncreaseInNanos = centurialIncreaseInNanos / (100 * 365.25);

		long lengthOfMeanSolarDayAtYear5900InNanos = 86_400_002_000_000L; //Known value
		long increaseBetweenEpochAndYear5900InNanos = centurialIncreaseInNanos * 59;
		long lengthOfMeanSolarDayAtEpochInNanos = lengthOfMeanSolarDayAtYear5900InNanos - increaseBetweenEpochAndYear5900InNanos;

		//Initialize ArrayList that will hold the days
		ArrayList<Long> dayEpochMilliseconds = new ArrayList<>(yearEpochMilliseconds.length * 370); //Make sure there's enough capacity

		//Initialize day loop
		int currentDay = 0;
		long eotOffsetMillis = 0;
		double jdeNanosOfCurrentMeanSolarDay = (double) jdeMillisAtStartOfCalendar * 1_000_000;
		long epochMillisOfEndOfFinalYear = yearEpochMilliseconds[yearEpochMilliseconds.length - 1];
		while (dayEpochMilliseconds.isEmpty() || dayEpochMilliseconds.getLast() < epochMillisOfEndOfFinalYear) {
			long jdeMillisOfCurrentMeanSolarDay = (long) (jdeNanosOfCurrentMeanSolarDay / 1_000_000);

			//Calculate Equation of Time and calculate true solar day
			double deltaT = ((double) jdeMillisOfCurrentMeanSolarDay / (24 * 3600 * 1000)) - 2451545.0; //Days since J2000 Epoch

			double deltaTC = deltaT / 36525; //Julian centuries since J2000 Epoch
			double deltaTC2 = deltaTC * deltaTC;
			double deltaTC3 = deltaTC2 * deltaTC;

			double deltaTM = deltaTC / 10; //Julian millennia since J2000 Epoch
			double deltaTM2 = deltaTM * deltaTM;
			double deltaTM3 = deltaTM2 * deltaTM;
			double deltaTM4 = deltaTM3 * deltaTM;
			double deltaTM5 = deltaTM4 * deltaTM;

			double deltaT10M = deltaTM / 10; //10 Julian millennia since J2000 Epoch
			double deltaT10M2 = deltaT10M * deltaT10M;
			double deltaT10M3 = deltaT10M2 * deltaT10M;
			double deltaT10M4 = deltaT10M3 * deltaT10M;
			double deltaT10M5 = deltaT10M4 * deltaT10M;
			double deltaT10M6 = deltaT10M5 * deltaT10M;
			double deltaT10M7 = deltaT10M6 * deltaT10M;
			double deltaT10M8 = deltaT10M7 * deltaT10M;
			double deltaT10M9 = deltaT10M8 * deltaT10M;
			double deltaT10M10 = deltaT10M9 * deltaT10M;

			double lSun = normalize( //Sun's mean longitude (28.2 / normalized degrees)
				280.4664567 +
				360007.6982779 * deltaTM +
				0.03032028 * deltaTM2 +
				deltaTM3 / 49931 -
				deltaTM4 / 15300 -
				deltaTM5 / 2000000
			);

			double omega = //Longitude of ascending node of Moon's mean orbit on ecliptic (22 / degrees)
				125.04452 -
				1934.136261 * deltaTC +
				0.0020708 * deltaTC2 +
				deltaTC3 / 450000;

			double lMoon = 218.3165 + 481267.8813 * deltaTC; //Moon's mean longitude (22 / degrees)

			double deltaPsi = arcsecToDegree( //Nutation in longitude (22 (errata) / degrees)
				-17.20 * sin(toRadians(omega)) -
				1.32 * sin(toRadians(2 * lSun)) -
				0.23 * sin(toRadians(2 * lMoon)) +
				0.21 * sin(toRadians(2 * omega))
			);

			double deltaEpsilon = arcsecToDegree( //Nutation in obliquity (22 / degrees)
				9.20 * cos(toRadians(omega)) +
				0.57 * cos(toRadians(2 * lSun)) +
				0.10 * cos(toRadians(2 * lMoon)) -
				0.09 * cos(toRadians(2 * omega))
			);

			double epsilonZero = arcsecToDegree( //Mean obliquity of ecliptic (22.2 / degrees)
				82800 + //23 degrees
				1560 + //26 arcminutes
				21.448 -
				4680.93 * deltaT10M -
				1.55 * deltaT10M2 +
				1999.25 * deltaT10M3 -
				51.38 * deltaT10M4 -
				249.67 * deltaT10M5 -
				39.05 * deltaT10M6 +
				7.12 * deltaT10M7 +
				27.87 * deltaT10M8 +
				5.79 * deltaT10M9 +
				2.45 * deltaT10M10
			);

			double epsilon = epsilonZero + deltaEpsilon; //True obliquity of ecliptic (22 / degrees)

			double m = //Sun's mean anomaly (25.3 / degrees)
				357.52911 +
				35999.05029 * deltaTC -
				0.0001537 * deltaTC2;

			double c = //Sun's equation of the centre (25 / degrees)
				(1.914602 - 0.004817 * deltaTC - 0.000014 * deltaTC2) * sin(toRadians(m)) +
				(0.019993 - 0.000101 * deltaTC) * sin(toRadians(2 * m)) +
				0.000289 * sin(toRadians(3 * m));

			double dot = lSun + c; //Sun's true longitude (25 / degrees)

			double gamma = dot - 0.00569 - 0.00478 * sin(toRadians(omega)); //Sun's apparent longitude (25 / degrees)

			double alpha = normalize(toDegrees(atan2( //Sun's apparent right ascension (25.6 / normalized degrees)
				cos(toRadians(epsilon)) * sin(toRadians(gamma)), //Correction from 25.8 already contained in epsilon (25.8 is simplified version of deltaEpsilon)
				cos(toRadians(gamma))
			)));

			double eotDegrees = lSun - 0.0057183 - alpha + deltaPsi * cos(toRadians(epsilon)); //Equation of Time (28.1 / degrees)

			double eotMinutes = (eotDegrees * 24 * 60) / 360;
			if (eotMinutes > 20) {
				eotMinutes -= 24 * 60;
			} else if (eotMinutes < -20) {
				eotMinutes += 24 * 60;
			}
			if (eotMinutes > 20 || eotMinutes < -20) {
				throw new IllegalStateException("eotMinutes is " + eotMinutes + " for currentDay " + currentDay);
			}

			long eotMillis = (long) (eotMinutes * 60 * 1000);

			//Subtract eot, rather than add, because if eot is positive, apparent solar time is *ahead* of mean, which means that the *duration* of apparent is *shorter*, not longer
			long jdeMillisOfCurrentTrueSolarDay = jdeMillisOfCurrentMeanSolarDay - eotMillis;

			long epochMillisOfCurrentTrueSolarDay = (jdeMillisOfCurrentTrueSolarDay - jdeMillisAtStartOfCalendar) - eotOffsetMillis;

			//Use this code if you want to display details of certain days
//			if (currentDay >= 2164066 && currentDay <= 2164430) {
//				System.out.println(
//					eotMinutes
//					"currentDay: " + currentDay + ", " +
//					"lSun: " + lSun + ", " +
//					"alpha: " + alpha + ", " +
//					"deltaPsi: " + deltaPsi + ", " +
//					"epsilon: " + epsilon + ", "
//				);
//			}

			//See StandardMarsMillisecondStoreDataProvider comments for an explanation of construct below
			if (currentDay == 0) { //Calculate eotOffsetMillis in the first iteration
				eotOffsetMillis = jdeMillisOfCurrentTrueSolarDay - jdeMillisAtStartOfCalendar; //Positive if too long, Negative if too short
			} else {
				//Add the first day and further to the List
				dayEpochMilliseconds.add(epochMillisOfCurrentTrueSolarDay);
			}

			//Calculate mean solar day length for this day
			double increaseSinceEpochInNanos = dailyIncreaseInNanos * currentDay;
			double lengthOfCurrentMeanSolarDayInNanos = lengthOfMeanSolarDayAtEpochInNanos + increaseSinceEpochInNanos;

			jdeNanosOfCurrentMeanSolarDay += lengthOfCurrentMeanSolarDayInNanos;
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

	private double normalize(double degrees) {
		double mod = degrees % 360;
		return mod < 0 ? mod + 360 : mod;
	}

	private double arcsecToDegree(double arcseconds) {
		return arcseconds / 3600;
	}
}
