/*
 * Copyright (c) 2018-2026 (5918-5926 in Lukashian years)
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
 * The southern solstice that was chosen to be the Lukashian Epoch is the one that took place at 1947-04-27T05:15:21.600Z in the Gregorian Calendar.
 * This southern solstice was chosen for the following reasons:
 *
 * <ul>
 * 	<li>
 * 	    All of <a href="https://en.wikipedia.org/wiki/Human_mission_to_Mars">human history involving Mars</a> can be expressed in the Lukashian Calendar.
 * 	    Satisfying this constraint means choosing a southern solstice that took place before approximately 1960 Gregorian.
 * 		<br><br>
 * 	</li>
 * 	<li>
 * 	    Because the location of the Landing Site of the first Human Settlement isn't known yet, it cannot yet be ensured that the turn of day is during nighttime
 * 		for its inhabitants. In the meantime, we have chosen the location of <a href="https://en.wikipedia.org/wiki/Mars_Pathfinder#Entry,_descent_and_landing">Pathfinder</a>,
 * 		which happens to be close to the location of the <a href="https://www.nasa.gov/image-article/ares-3-landing-site-where-science-fact-meets-fiction">Ares III
 * 		Hab in Acidalia Planitia</a>, as a pretend Landing Site, in order to have an anchor point for ensuring that the turn of day is during nighttime at that location.
 * 		<br><br>
 * 		Pathfinder landed in the middle of the night on Mars. We want the turn of day (0000 beeps) to be as close as possible to the middle of the night at a certain location.
 * 		The chosen southern solstice Epoch leads to Pathfinder landing at 0452, which is the closest to 0000 of any of the Mars southern solstices that happened from 1940 to 1960
 * 		Gregorian.
 * 	</li>
 * </ul>
 *
 * This class uses the following resources to implement the calculations for the days and years:
 * <ul>
 * 	<li><a href="https://www.giss.nasa.gov/pubs/abs/al04000r.html">Allison 1997</a></li>
 * 	<li><a href="https://www.giss.nasa.gov/pubs/abs/al05000n.html">Allison/McEwen 2000</a></li>
 * 	<li><a href="https://www.giss.nasa.gov/tools/mars24">Mars Solar Time as Adopted by the Mars24 Sunclock</a></li>
 * 	<li><a href="https://www.planetary.org/articles/mars-calendar">Mars Calendar by Planetary Society</a></li>
 * 	<li><a href="https://marsclock.com">Mars Clock by James Tauber</a></li>
 * </ul>
 */
public class StandardMarsMillisecondStoreDataProvider implements MillisecondStoreDataProvider {

	private static final double[] ALPHA = new double[] { 0.0071, 0.0057, 0.0039, 0.0037, 0.0021, 0.0020, 0.0018 };
	private static final double[] TAU =   new double[] { 2.2353, 2.7543, 1.1177, 15.7866, 2.1354, 2.4694, 32.8493 };
	private static final double[] PHI =   new double[] { 49.409, 168.173, 191.837, 21.736, 15.704, 95.528, 49.095 };

	@Override
	public long loadUnixEpochOffsetMilliseconds() {
		//This was calculated as follows:
		//Generate known Gregorian Timestamp for the Mars Southern Solstice closest to UNIX Epoch:
		//System.out.println((double) new StandardMarsMillisecondStoreDataProvider().getJdeMillisAtEndOfYear(51 - EPOCH_SOLSTICE_INDEX) / (24 * 3600 * 1000));
		//This outputs 2441233.395, convert this with https://ssd.jpl.nasa.gov/tools/jdc/#/jd, which yields 1971-10-08 21:28:48

		//Then, write following code in some main method, with this method returning 0 and no leap seconds in the MillisecondStoreData
		//ZonedDateTime gregorianSolstice = ZonedDateTime.of(1971, 10, 8, 21, 28, 48, 0, ZoneId.of("Z"));
		//Instant lukashianSolstice = Year.of(51 - EPOCH_SOLSTICE_INDEX, CalendarKeys.MARS).lastInstant();
		//long gregorianSolsticeUnixEpochMillis = gregorianSolstice.toInstant().toEpochMilli();
		//long lukashianSolsticeUnixEpochMillis = lukashianSolstice.getUnixEpochMilliseconds();
		//long unixEpochOffsetMilliseconds = lukashianSolsticeUnixEpochMillis - gregorianSolsticeUnixEpochMillis;
		//System.out.println("UNIX Epoch Offset Milliseconds: " + unixEpochOffsetMilliseconds);
		//This way of "pulling" the Lukashian Calendar in sync with the System Clock also takes into account the difference between TAI and TT

		return 715805078401L;
	}

	@Override
	public long[] loadYearEpochMilliseconds() {
		int amountOfYears = MARTIAN_SOUTHERN_SOLSTICE_MJDS.length - (EPOCH_SOLSTICE_INDEX+1);
		long jdeMillisAtStartOfCalendar = this.getJdeMillisAtEndOfYear(0);
		long[] yearEpochMilliseconds = new long[amountOfYears];
		for (int year = 1; year <= amountOfYears; year++) {
			yearEpochMilliseconds[year - 1] = this.getJdeMillisAtEndOfYear(year) - jdeMillisAtStartOfCalendar;
		}
		return yearEpochMilliseconds;
	}

	@Override
	public long[] loadDayEpochMilliseconds(long[] yearEpochMilliseconds) {
		//See https://www.giss.nasa.gov/tools/mars24/help/algorithm.html

		long jdeMillisAtStartOfCalendar = this.getJdeMillisAtEndOfYear(0);
		long lengthOfMeanSolarDayInMillis = (long) (24 * 3600 * 1000 * 1.02749125);

		//Initialize ArrayList that will hold the days
		ArrayList<Long> dayEpochMilliseconds = new ArrayList<>(yearEpochMilliseconds.length * 670); //Make sure there's enough capacity

		//Initialize day loop
		int currentDay = 0;
		long eotOffsetMillis = 0;
		long jdeMillisOfCurrentMeanSolarDay = jdeMillisAtStartOfCalendar;
		long epochMillisOfEndOfFinalYear = yearEpochMilliseconds[yearEpochMilliseconds.length - 1];
		while (dayEpochMilliseconds.isEmpty() || dayEpochMilliseconds.getLast() < epochMillisOfEndOfFinalYear) {
			//Calculate Equation of Time and calculate true solar day
			double deltaT = ((double) jdeMillisOfCurrentMeanSolarDay / (24 * 3600 * 1000)) - 2451545.0; //Days since J2000 Epoch

			double m = 19.3871 + (0.52402073 * deltaT); //Mars Mean Anomaly
			double alphaFMS = 270.3871 + (0.524038496 * deltaT); //Angle of Fictitious Mean Sun

			double perturbers = 0;
			for (int i = 0; i < 7; i++) {
				perturbers += ALPHA[i] * cos(toRadians(((0.985626 * deltaT) / TAU[i]) + PHI[i]));
			}

			double vMinusM = //True minus Mean Anomaly
				(10.691 + 0.0000003 * deltaT) * sin(toRadians(m)) +
				0.623 * sin(toRadians(2 * m)) +
				0.050 * sin(toRadians(3 * m)) +
				0.005 * sin(toRadians(4 * m)) +
          		0.0005 * sin(toRadians(5 * m)) +
				perturbers;

			double ls = alphaFMS + vMinusM; //Aereocentric Solar Longitude

			double eotDegrees =
				2.861 * sin(toRadians(2 * ls)) -
				0.071 * sin(toRadians(4 * ls)) +
          		0.002 * sin(toRadians(6 * ls)) -
				vMinusM;

			double eotHours = (eotDegrees * 24) / 360;
			long eotMillis = (long) (eotHours * 3600 * 1000);

			//Subtract eot, rather than add, because if eot is positive, apparent solar time is *ahead* of mean, which means that the *duration* of apparent is *shorter*, not longer
			long jdeMillisOfCurrentTrueSolarDay = jdeMillisOfCurrentMeanSolarDay - eotMillis;

			long epochMillisOfCurrentTrueSolarDay = (jdeMillisOfCurrentTrueSolarDay - jdeMillisAtStartOfCalendar) - eotOffsetMillis;

			//Use this code if you want to display details of certain days
//			if (currentDay >= 27414 && currentDay <= 28081) {
//				System.out.println(
//					eotHours * 60
//					"Epoch day: " + currentDay + ", " +
//					"deltaT: " + deltaT + ", " +
//					"perturbers: " + perturbers + ", " +
//					"vMinusM: " + vMinusM + ", " +
//					"ls: " + ls + ", " +
//					"eotDegrees: " + eotDegrees + ", " +
//					"eotMillis: " + eotMillis + ", " +
//					"jdeMillisOfCurrentMeanSolarDay: " + jdeMillisOfCurrentMeanSolarDay + ", " +
//					"jdeMillisOfCurrentTrueSolarDay: " + jdeMillisOfCurrentTrueSolarDay + ", " +
//					"epochMillisOfCurrentTrueSolarDay: " + epochMillisOfCurrentTrueSolarDay + ", "
//				);
//			}

			// Situation with Too Short first True Solar Day, we want to add the offset
			//                   Epoch \
			//                         |----------Mean----------|----------Mean----------|----------Mean----------|
			//                  |Offset|   TooShortTrue   |----------True----------|----------True----------|
			//           EoT-0 /                     EoT /                    EoT /                    EoT /

			// Situation with Too Long first True Solar Day, we want to subtract the offset
			//                   Epoch \
			//                         |----------Mean----------|----------Mean----------|----------Mean----------|
			//                         |Offset|      TooLongTrue      |----------True----------|----------True----------|
			//                         EoT-0 /                   EoT /                    EoT /                    EoT /
			if (currentDay == 0) { //Calculate eotOffsetMillis in the first iteration
				eotOffsetMillis = jdeMillisOfCurrentTrueSolarDay - jdeMillisAtStartOfCalendar; //Positive if too long, Negative if too short
			} else {
				//Add the first day and further to the List
				dayEpochMilliseconds.add(epochMillisOfCurrentTrueSolarDay);
			}

			jdeMillisOfCurrentMeanSolarDay += lengthOfMeanSolarDayInMillis;
			currentDay++;
		}
		return dayEpochMilliseconds.stream().mapToLong(Long::longValue).toArray();
	}

	private long getJdeMillisAtEndOfYear(int year) {
		double mjd = MARTIAN_SOUTHERN_SOLSTICE_MJDS[EPOCH_SOLSTICE_INDEX + year];
		double jde = mjd + 2400000.5;
		return (long) (jde * 24 * 3600 * 1000);
	}

	/**
	 * This is the index of the solstice that we choose to be the Lukashian Epoch on Mars.
	 * <p>
	 * Whenever this value changes, the {@link #loadUnixEpochOffsetMilliseconds()} method needs to be reevaluated.
	 */
	private static final int EPOCH_SOLSTICE_INDEX = 38;

	/**
	 * These are taken from <a href="https://www.giss.nasa.gov/pubs/abs/al05000n.html">Allison/McEwen 2000</a> and provide all Martian Southern Solstices from
	 * 1874 Gregorian to 2127 Gregorian. They are in Modified Julian Date (MJD = JDE - 2400000.5) (JDE = MJD + 2400000.5)
	 */
	private static final double[] MARTIAN_SOUTHERN_SOLSTICE_MJDS = new double[] {
		6197.109, //Gregorian: 1874-1875
		6884.078,
		7571.060,
		8258.049,
		8944.979,
		9631.969,
		10318.974,
		11005.929,
		11692.879,
		12379.887,
		13066.853,
		13753.824,
		14440.832,
		15127.803,
		15814.749,
		16501.737,
		17188.701,
		17875.653,
		18562.688,
		19249.687,
		19936.629,
		20623.597,
		21310.583,
		21997.519,
		22684.513,
		23371.515,
		24058.485,
		24745.458,
		25432.453,
		26119.388,
		26806.362,
		27493.375,
		28180.343,
		28867.287,
		29554.288,
		30241.272,
		30928.229,
		31615.237,
		32302.219,
		32989.166,
		33676.143,
		34363.122,
		35050.060,
		35737.080,
		36424.091,
		37111.039,
		37797.994,
		38484.989,
		39171.928,
		39858.909,
		40545.917,
		41232.895,
		41919.863,
		42606.860,
		43293.810,
		43980.768,
		44667.783,
		45354.763,
		46041.703,
		46728.688,
		47415.685,
		48102.632,
		48789.632,
		49476.625,
		50163.575,
		50850.540,
		51537.531,
		52224.466,
		52911.471,
		53598.497,
		54285.458,
		54972.406,
		55659.403,
		56346.353,
		57033.314,
		57720.323,
		58407.306,
		59094.272,
		59781.261,
		60468.226,
		61155.167,
		61842.176,
		62529.169,
		63216.112,
		63903.083,
		64590.096,
		65277.045,
		65964.037,
		66651.040,
		67337.998,
		68024.952,
		68711.945,
		69398.886,
		70085.868,
		70772.900,
		71459.872,
		72146.812,
		72833.799,
		73520.764,
		74207.710,
		74894.717,
		75581.712,
		76268.684,
		76955.667,
		77642.650,
		78329.583,
		79016.582,
		79703.585,
		80390.535,
		81077.491,
		81764.505,
		82451.461,
		83138.435,
		83825.440,
		84512.406,
		85199.352,
		85886.342,
		86573.299,
		87260.264,
		87947.300,
		88634.290,
		89321.231,
		90008.206,
		90695.188,
		91382.126,
		92069.125,
		92756.128,
		93443.101,
		94130.072,
		94817.062,
		95503.994,
		96190.974,
		96877.984,
		97564.947,
		98251.894 //Gregorian: 2126-2127
	};
}
