/**
 *
 */
package com.sample.framework.uiautomation.Utility;

import com.aventstack.extentreports.Status;
import com.sample.framework.uiautomation.base.TestBase;
import org.openqa.selenium.WebDriver;
import org.testng.Reporter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.time.temporal.IsoFields;
import java.util.*;

import static java.time.temporal.TemporalAdjusters.firstDayOfMonth;
import static java.time.temporal.TemporalAdjusters.lastDayOfMonth;


public class DateTimeHelper {

	UtilityHelper objUtil = new UtilityHelper();

	public static String getCurrentDateTime() {

		DateFormat dateFormat = new SimpleDateFormat("_yyyy-MM-dd_HH-mm-ss");
		Calendar cal = Calendar.getInstance();
		String time = "" + dateFormat.format(cal.getTime());
		return time;
	}

	public static String getCurrentDate() {
		return getCurrentDateTime().substring(0, 11);
	}

	/**
	 * Set custom Date
	 *
	 * @return
	 */
	public static String getCurrentLocalDateAndTime() {
		try {
			String dateTime;
			if (UtilityHelper.getProperty("config", "Local.Date.Change.Required").equalsIgnoreCase("Yes")) {
				dateTime = UtilityHelper.getProperty("config", "Change.Local.Date") + LocalTime.now().toString();
			} else {
				dateTime = LocalDateTime.now().toString();
			}
			return dateTime;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Exception while getting date and Time");
		}
	}

	/**
	 * Set custom Date
	 *
	 * @return
	 */
	public static String getCurrentLocalDate() {
		try {
			String dateTime;
			if (UtilityHelper.getProperty("config", "Local.Date.Change.Required").equalsIgnoreCase("Yes")) {
				dateTime = UtilityHelper.getProperty("config", "Change.Local.Date");
			} else {
				dateTime = LocalDate.now().toString();
			}
			return dateTime;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Exception while getting date and Time");
		}
	}

	public static void changeLocalDateUsingCommandLine() {
		try {

			String localDate = UtilityHelper.getProperty("config", "Change.Server.Date");
			if (System.getProperty("os.name").contains("Window")) {
				String cmd1 = "cmd /c date " + UtilityHelper.getProperty("config", "Change.Local.Date");
				Runtime.getRuntime().exec(cmd1);

				String cmd2 = "cmd /c time 10:05";
				Runtime.getRuntime().exec(cmd2);
			} else if (System.getProperty("os.name").contains("Linux")) {
				String dateToChange = UtilityHelper.getProperty("config", "Change.Local.Date");
				String text = "echo Shikhar123 | sudo -S date --set='" + dateToChange + " 10:05:59'";
				String[] cmd = {"/bin/bash", "-c", text};
				Runtime.getRuntime().exec(cmd);
			}

			if (LocalDate.now().toString().equals(localDate)) {
				Reporter.log("System date changed to " + LocalDate.now().toString(), true);
			} else {
				throw new RuntimeException("Exception while changing server date its " + LocalDate.now().toString()
						+ " and not changed to " + localDate);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static LocalDate getCurrentDateLocalDateFormat() {
		return LocalDate.parse(LocalDate.now().toString());
	}

	public boolean changeServerDate(WebDriver driver, String date) {
		try {
			String text = "emailtemplate/Setserverdate?set_date=" + date;
			Reporter.log("Server date is changed to '" + objUtil.getHTMLTextFromAPI(driver, text) + "'", true);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Exception while changing server date and Local Date");
		}
	}

	/**
	 * This method converts Local Date to Epoch day
	 *
	 * @param date
	 * @return long
	 */
	public long convertLocalDateToEpochDay(String date) {
		try {
			String dateTime = date + "T14:47:55";
			return ((LocalDateTime.parse(dateTime).atZone(ZoneId.of("Asia/Kolkata")).toInstant().toEpochMilli()) / 1000);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Exception while changing Local date to epoch day");
		}
	}
	/**
	 * This month will calculate difference between months
	 *
	 * @param DATEIN_YYYY_MM_DD_format
	 * @return
	 */
	public double getMonthDifferenceFromCurrentDate(String DATEIN_YYYY_MM_DD_format,String toDate) {
		try {
			String arr[] = DATEIN_YYYY_MM_DD_format.split("-");
			int year = Integer.parseInt(arr[0]);
			int month = Integer.parseInt(arr[1]);
			int day = Integer.parseInt(arr[2]);
			LocalDate currentDate = LocalDate.parse(toDate);

			LocalDate startDate = LocalDate.of(year, month, day).with(firstDayOfMonth());
			LocalDate endDate = currentDate.with(firstDayOfMonth());
			double monthsDiff = ChronoUnit.MONTHS.between(startDate, endDate);
			return monthsDiff;

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	/**
	 * This month will calculate difference between months
	 *
	 * @param DATEIN_YYYY_MM_DD_format
	 * @return
	 */
	public double getMonthDifferenceFromCurrentDate(String DATEIN_YYYY_MM_DD_format) {
		try {
			String arr[] = DATEIN_YYYY_MM_DD_format.split("-");
			int year = Integer.parseInt(arr[0]);
			int month = Integer.parseInt(arr[1]);
			int day = Integer.parseInt(arr[2]);
			LocalDate currentDate = LocalDate.parse(getCurrentLocalDate());

			LocalDate startDate = LocalDate.of(year, month, day).with(firstDayOfMonth());
			LocalDate endDate = currentDate.with(firstDayOfMonth());
			double monthsDiff = ChronoUnit.MONTHS.between(startDate, endDate);
			return monthsDiff;

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	/**
	 * This month will calculate months difference between first day of month of two
	 * dates
	 *
	 * @param Date1
	 * @param Date2
	 * @return
	 */
	public double getMonthDifferenceBetweenTwoDates(String Date1, String Date2) {
		try {

			LocalDate startDate = LocalDate.parse(Date1).with(firstDayOfMonth());
			LocalDate endDate = LocalDate.parse(Date2).with(firstDayOfMonth());
			double monthsDiff = ChronoUnit.MONTHS.between(startDate, endDate);
			return monthsDiff;

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	/**
	 * This month will calculate exact month difference between two dates
	 *
	 * @param Date1
	 * @param Date2
	 * @return
	 */
	public double getExactMonthDifferenceBetweenTwoDates(String Date1, String Date2) {
		try {
			LocalDate startDate = LocalDate.parse(Date1);
			LocalDate endDate = LocalDate.parse(Date2);
			int febdiff = 0;
			int startday = 30;

			if (endDate.getMonth() == Month.FEBRUARY && startDate.isAfter(startDate.withDayOfMonth(28))
					&& (endDate.getDayOfMonth() > (startDate.getDayOfMonth()))) {
				febdiff = 1;
			}
			if ((startDate == startDate.withDayOfMonth(startDate.lengthOfMonth()))
					&& (endDate == endDate.withDayOfMonth(endDate.lengthOfMonth()))) {
				if (startDate.getMonth() == Month.FEBRUARY) {
					startday = 28;
				} else {
					startday = 30;
				}
				startDate = startDate.withDayOfMonth(startday);
			}
			double monthsDiff = ChronoUnit.MONTHS.between(startDate, endDate);
			return monthsDiff + febdiff;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	/**
	 * This month will calculate days difference between two dates
	 *
	 * @param Date1
	 * @param Date2
	 * @return
	 */
	public double getDaysDifferenceBetweenTwoDates(String Date1, String Date2) {
		try {
			LocalDate startDate = LocalDate.parse(Date1).with(firstDayOfMonth());
			LocalDate endDate = LocalDate.parse(Date2).with(firstDayOfMonth());
			double daysDiff = ChronoUnit.DAYS.between(startDate, endDate);
			return daysDiff;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	/**
	 * This month will calculate difference between two dates
	 *
	 * @param DOJ
	 * @return
	 */
	public double getDaysDifferenceBetweenDOJAndCurrentDate(String DOJ) {
		try {
			LocalDate startDate = LocalDate.parse(DOJ);
			LocalDate endDate = LocalDate.parse(getCurrentLocalDate());
			double daysDiff = ChronoUnit.DAYS.between(startDate, endDate);
			return daysDiff;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	/**
	 * This month will calculate difference between two dates
	 *
	 * @param customCurrentDate
	 * @param DOJ
	 * @return
	 */
	public double getDaysDifferenceBetweenDOJAndCurrentDate(String DOJ, String customCurrentDate) {
		try {
			LocalDate startDate = LocalDate.parse(DOJ);
			LocalDate endDate = LocalDate.parse(customCurrentDate);
			double daysDiff = ChronoUnit.DAYS.between(startDate, endDate);
			return daysDiff;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	/**
	 * This method verifies whether date is after of before mid of month
	 *
	 * @param DATEIN_YYYY_MM_DD_format
	 * @return String
	 */
	public String verifyDOJMidJoining(String DATEIN_YYYY_MM_DD_format) {

		String arr[] = DATEIN_YYYY_MM_DD_format.split("-");
		int day = Integer.parseInt(arr[2]);
		String midJoining = "";
		if (day > 15) {
			midJoining = "Yes";
		} else {
			midJoining = "No";
		}
		return midJoining;
	}

	/**
	 * This method returns Quarter difference from Current Date
	 *
	 * @param DATEIN_YYYY_MM_DD_format
	 * @return
	 */
	public double getQuarterDiffFromCurrentDate(String DATEIN_YYYY_MM_DD_format) {
		try {
			LocalDate DOJQuarterStartDate = getFirstDayOfQuarter(DATEIN_YYYY_MM_DD_format);
			LocalDate currentQuarterStartDate = getFirstDayOfQuarter(getCurrentLocalDate());
			double quarterDiff = (ChronoUnit.MONTHS.between(DOJQuarterStartDate, currentQuarterStartDate) / 3);
			return quarterDiff;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	/**
	 * This method returns Quarter difference from Current Date
	 *
	 * @param DATEIN_YYYY_MM_DD_format
	 * @return
	 */
	public double getQuarterDiffFromCurrentDate(String DATEIN_YYYY_MM_DD_format,String toDate) {
		try {
		/*	LocalDate DOJQuarterStartDate = getFirstDayOfQuarter(DATEIN_YYYY_MM_DD_format);
			LocalDate currentQuarterStartDate = getFirstDayOfQuarter(toDate);
			double quarterDiff = (ChronoUnit.MONTHS.between(DOJQuarterStartDate, currentQuarterStartDate) / 3);
			return quarterDiff;*/
			Period age=Period.between(LocalDate.parse(DATEIN_YYYY_MM_DD_format),LocalDate.parse(toDate));
			return -age.getMonths()/3;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}


	/**
	 * This method returns Quarter difference from Current Date
	 *
	 * @param DATEIN_YYYY_MM_DD_format
	 * @return
	 */
	public double getQuarterDiffBetweenTwoDates(String DATEIN_YYYY_MM_DD_format, String endDate) {
		try {
			LocalDate DOJQuarterStartDate = getFirstDayOfQuarter(DATEIN_YYYY_MM_DD_format);
			LocalDate currentQuarterStartDate = getFirstDayOfQuarter(endDate);
			double monthsDiff = ChronoUnit.MONTHS.between(DOJQuarterStartDate, currentQuarterStartDate);
			double quarterDiff = (monthsDiff / 3);
			return quarterDiff;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	/**
	 * This method returns Month difference from first day of Quarter
	 *
	 * @param DATEIN_YYYY_MM_DD_format
	 * @return
	 */
	public double getMonthDiffFromFirstDayOfQuarter(String DATEIN_YYYY_MM_DD_format) {
		try {
			String arr[] = DATEIN_YYYY_MM_DD_format.split("-");
			int year = Integer.parseInt(arr[0]);
			int month = Integer.parseInt(arr[1]);
			int day = Integer.parseInt(arr[2]);

			LocalDate startDate = getFirstDayOfQuarter(DATEIN_YYYY_MM_DD_format);
			LocalDate endDate = LocalDate.of(year, month, day).with(firstDayOfMonth());
			double monthsDiffFromQuarterMonth = ChronoUnit.MONTHS.between(startDate, endDate);
			return monthsDiffFromQuarterMonth;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	/**
	 * This method returns Month difference from first day of Quarter
	 *
	 * @param DATEIN_YYYY_MM_DD_format
	 * @return
	 */
	public double getMonthDiffFromLastMonthOfQuarter(String DATEIN_YYYY_MM_DD_format) {
		try {
			String arr[] = DATEIN_YYYY_MM_DD_format.split("-");
			int year = Integer.parseInt(arr[0]);
			int month = Integer.parseInt(arr[1]);
			int day = Integer.parseInt(arr[2]);

			LocalDate endDate = getFirstDayOfLastMonthOfCurrentQuarter(DATEIN_YYYY_MM_DD_format);
			LocalDate startDate = LocalDate.of(year, month, day).with(firstDayOfMonth());
			double monthsDiffFromQuarterMonth = ChronoUnit.MONTHS.between(startDate, endDate);
			return monthsDiffFromQuarterMonth;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	/**
	 * This method returns Month difference from first day of Quarter
	 *
	 * @param DATEIN_YYYY_MM_DD_format
	 * @return
	 */
	public double getMonthDiffFromFirstDayOfQuarterBetweenTwoDates(String DATEIN_YYYY_MM_DD_format,
																   String endDateString) {
		try {

			LocalDate startDate = getFirstDayOfQuarter(DATEIN_YYYY_MM_DD_format);
			LocalDate endDate = LocalDate.parse(endDateString).with(firstDayOfMonth());
			double monthsDiffFromQuarterMonth = ChronoUnit.MONTHS.between(startDate, endDate);
			return monthsDiffFromQuarterMonth;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	/**
	 * This method will return First Day of Quarter in Local Date format
	 *
	 * @param DATEIN_YYYY_MM_DD_format
	 * @return Local Date
	 */
	public LocalDate getFirstDayOfQuarter(String DATEIN_YYYY_MM_DD_format) {

		String arr[] = DATEIN_YYYY_MM_DD_format.split("-");
		int year = Integer.parseInt(arr[0]);

		int DOJQuarter = LocalDate.parse(DATEIN_YYYY_MM_DD_format).get(IsoFields.QUARTER_OF_YEAR);
		int quarterMonth = 0;

		if (DOJQuarter == 1) {
			quarterMonth = 1;
		} else if (DOJQuarter == 2) {
			quarterMonth = 4;
		} else if (DOJQuarter == 3) {
			quarterMonth = 7;
		} else if (DOJQuarter == 4) {
			quarterMonth = 10;
		} else {
			throw new RuntimeException("DOJ is not correct");
		}

		LocalDate startDate = LocalDate.of(year, quarterMonth, 01).with(firstDayOfMonth());
		return startDate;
	}

	/**
	 * This method will return last Day of Quarter in Local Date format
	 *
	 * @param DATEIN_YYYY_MM_DD_format
	 * @return Local Date
	 */
	public LocalDate getLastDayOfQuarter(String DATEIN_YYYY_MM_DD_format) {



		LocalDate.parse("2019-10-30").minusMonths(LocalDate.parse("2018-11-01").getMonthValue()).getMonthValue();
		String arr[] = DATEIN_YYYY_MM_DD_format.split("-");
		int year = Integer.parseInt(arr[0]);

		int DOJQuarter = LocalDate.parse(DATEIN_YYYY_MM_DD_format).get(IsoFields.QUARTER_OF_YEAR);
		int quarterMonth = 0;

		if (DOJQuarter == 1) {
			quarterMonth = 3;
		} else if (DOJQuarter == 2) {
			quarterMonth = 6;
		} else if (DOJQuarter == 3) {
			quarterMonth = 9;
		} else if (DOJQuarter == 4) {
			quarterMonth = 12;
		} else {
			throw new RuntimeException("DOJ is not correct");
		}

		LocalDate lastDate = LocalDate.of(year, quarterMonth, 01).with(lastDayOfMonth());
		return lastDate;
	}

	/**
	 * This method will return First Day of Quarter in Local Date format
	 *
	 * @param DATEIN_YYYY_MM_DD_format
	 * @return Local Date
	 */
	public LocalDate getFirstDayOfLastMonthOfCurrentQuarter(String DATEIN_YYYY_MM_DD_format) {

		String arr[] = DATEIN_YYYY_MM_DD_format.split("-");
		int year = Integer.parseInt(arr[0]);

		int DOJQuarter = LocalDate.parse(DATEIN_YYYY_MM_DD_format).get(IsoFields.QUARTER_OF_YEAR);
		int quarterMonth = 0;

		if (DOJQuarter == 1) {
			quarterMonth = 3;
		} else if (DOJQuarter == 2) {
			quarterMonth = 6;
		} else if (DOJQuarter == 3) {
			quarterMonth = 9;
		} else if (DOJQuarter == 4) {
			quarterMonth = 12;
		} else {
			throw new RuntimeException("DOJ is not correct");
		}

		LocalDate startDate = LocalDate.of(year, quarterMonth, 01).with(firstDayOfMonth());
		return startDate;
	}

	public LocalDate getLastDayOfBiannual(String DATEIN_YYYY_MM_DD_format, String leaveCycle) {

		String arr[] = DATEIN_YYYY_MM_DD_format.split("-");
		int year = Integer.parseInt(arr[0]);

		int biannualEndMonth = 0;
		if (leaveCycle.equalsIgnoreCase("Calendar")) {
			biannualEndMonth = 6;
		} else if (leaveCycle.equalsIgnoreCase("Financial")) {
			biannualEndMonth = 9;
		}

		LocalDate biannualEndDate = LocalDate.of(year, biannualEndMonth, 01).with(lastDayOfMonth());
		return biannualEndDate;
	}

	public String getNextDate(String date) {

		Calendar cal = Calendar.getInstance();
		try {
			Date currentDate = formatStringToDate("dd-MM-yyyy", date);
			cal.setTime(currentDate);
			cal.add(Calendar.DAY_OF_YEAR, 1);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("ERROR: Unable to parse date: " + date);
		}
		return formatDateTo(cal.getTime(), "dd-MM-yyyy");
	}

	public Date getNextDate(Date date) {
		return addDays(date, 1);
	}

	public Date getPreviousDate(Date date) {
		return addDays(date, -1);
	}

	public String getRandomDateBetween(int startYr, int endYr) {
		Calendar gc = Calendar.getInstance();
		int year = startYr + (int) Math.round(Math.random() * (endYr - startYr));
		gc.set(Calendar.YEAR, year);
		int dayOfYear = 1 + (int) Math.round(Math.random() * (gc.getActualMaximum(Calendar.DAY_OF_YEAR) - 1));
		gc.set(Calendar.DAY_OF_YEAR, dayOfYear);
		return String.format("%d-%02d-%02d", gc.get(Calendar.YEAR), (gc.get(Calendar.MONTH) + 1), gc.get(Calendar.DAY_OF_MONTH));
	}

	public String formatDateTo(Date date, String formatPattern) {
		String format = "";
		try {
			format = new SimpleDateFormat(formatPattern).format(date);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return format;
		}
	}

	public Date formatStringToDate(String format, String date) {

		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date req = null;
		try {
			req = sdf.parse(date);
		} catch (Exception e) {

		}

		return req;
	}

	public String parseTime(int n) {

		String hrs = String.format("%02d", (n / 60));
		String mins = String.format("%02d", n % 60);

		String secs = String.format("%02d", new Random().nextInt(60));

		return hrs + ":" + mins + ":" + secs;
	}

	public Date setDayofMonth(Date date, int n) {

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.DAY_OF_MONTH, n);

		return cal.getTime();
	}

	public Date addDays(Date date, int n) {

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, n);

		return cal.getTime();
	}

	public Date getNextMonthFirst(Date date) {

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, 1);
		cal.add(Calendar.MONTH, 1);

		return cal.getTime();

	}

	public int getMins() {

		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.HOUR_OF_DAY) * 60 + cal.get(Calendar.MINUTE);
	}

	public long getMins(Date date) {
		Calendar cal = Calendar.getInstance();
		return cal.get(Calendar.HOUR_OF_DAY) * 60 + cal.get(Calendar.MINUTE);
	}

	public Date setTime(Date date, int mins) {

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, mins / 60);
		cal.set(Calendar.MINUTE, mins % 60);
		cal.set(Calendar.SECOND, 0);
		return cal.getTime();
	}


	public Date setEndingTime(Date date, int mins) {
		mins = mins - 1;
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, mins / 60);
		cal.set(Calendar.MINUTE, mins % 60 );
		cal.set(Calendar.SECOND, 59);
		return cal.getTime();
	}


	public LocalDateTime getLocalDateFromDate(Date date){
		return LocalDateTime.from(Instant.ofEpochMilli(date.getTime()).atZone(ZoneId.systemDefault()));
	}

	public long getDateDiff(Date date1, Date date2){

		LocalDateTime start = getLocalDateFromDate(date1);
		LocalDateTime end = getLocalDateFromDate(date2);
		return Duration.between(start, end).toMillis()/1000;

	}

	public Date addTime(Date date, int mins) {

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.HOUR_OF_DAY, mins / 60);
		cal.add(Calendar.MINUTE, mins % 60 );
		return cal.getTime();
	}

	public Date addEndTime(Date date, int mins) {
		mins = mins - 1;
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.HOUR_OF_DAY, mins / 60);
		cal.add(Calendar.MINUTE, mins % 60 );
		cal.add(Calendar.SECOND, 59 );
		return cal.getTime();
	}

	public long getDifference(Date second,Date first) {

		long firstInstant = first.toInstant().getEpochSecond();
		long secondInstant = second.toInstant().getEpochSecond();

		return  secondInstant - firstInstant;
	}

	//changes the Local Date format ex : 10th January 2019
	public String changeDateFormatForHolidays(LocalDate date) {
		Month month = date.getMonth();
		String day = null;
		int year = date.getYear();
		if (date.getDayOfMonth() < 10) {
			if (date.getDayOfMonth() == 1)
				day = date.getDayOfMonth() + "st".replace("0", "").trim();
			if (date.getDayOfMonth() == 2)
				day = date.getDayOfMonth() + "nd".replace("0", "").trim();
			if (date.getDayOfMonth() == 3)
				day = date.getDayOfMonth() + "rd".replace("0", "").trim();
			if (date.getDayOfMonth() >= 4)
				day = date.getDayOfMonth() + "th".replace("0", "").trim();
		} else {
			day = date.getDayOfMonth() + "th";
		}
		return day + " " + month + " " + year;
	}

	public long getDifferenceBetweenDOJAndCurrentLocalLate(String DOJ){
		LocalDate startDate = LocalDate.parse(DOJ);
		LocalDate endDate = LocalDate.now();
		long daysDiff = ChronoUnit.DAYS.between(startDate, endDate);
		return daysDiff;
	}
	//changes the Local Date to table format ex: 29-10-1992
	public String changeDateFormatForTable(LocalDate date) {
		int day = date.getDayOfMonth();
		String tempMonth = date.getMonth() + "";
		String tempMonth1 = tempMonth.toLowerCase();
		String month = tempMonth1.replace(tempMonth1.charAt(0), tempMonth1.toUpperCase().charAt(0));
		int year = date.getYear();
		return day + "-" + month.substring(0, 3) + "-" + year;
	}


	public LocalDate changeServerDateToStartOfFirstmonth(WebDriver driver){
		try {
			LocalDate today = LocalDate.now();
			if(today.getDayOfMonth()!=01) {
				LocalDate requestedChangeDate = today.plusDays(today.lengthOfMonth() - today.getDayOfMonth() + 1);
				changeServerDate(driver, requestedChangeDate.toString());
				TestBase.xtReportLog.log(Status.INFO, "Server Date is Changed Successfully to" + requestedChangeDate);
				return requestedChangeDate;
			}
			changeServerDate(driver, today.toString());
			return today;
		}
		catch (Exception e){
			TestBase.xtReportLog.log(Status.ERROR,"Exception while changing server date");
			throw new RuntimeException("Exception while changing server date");
		}
	}

	public Long parseTime(String time){

		String timeSplit[]=time.split(":");

		long hrs = Long.parseLong(timeSplit[0]);
		long mins = Long.parseLong(timeSplit[1]);
		long secs = Long.parseLong(timeSplit[2]);

		return  (hrs + mins*60)*60 +secs;

	}
	public Long parseTimeInMinutes(String time){

		String timeSplit[]=time.split(":");

		long hrs = Long.parseLong(timeSplit[0]);
		long mins = Long.parseLong(timeSplit[1]);
		long secs = Long.parseLong(timeSplit[2]);

		return  (hrs*60) + mins +(secs);

	}
	public Long parseTimeFromHHMMFormat(String time){

		String timeSplit[]=time.split(":");

		long hrs = Long.parseLong(timeSplit[0]);
		long mins = Long.parseLong(timeSplit[1]);

		return  (hrs + mins*60)*60;

	}
	public Long parseTimeFromMMSSFormat(String time){

		String timeSplit[]=time.split(":");

		long mins = Long.parseLong(timeSplit[0]);
		long secs = Long.parseLong(timeSplit[1]);

		return  (mins*60)*60 +secs;

	}
	public Long parseTimeIntoSeconds(String time){

		String timeSplit[]=time.split(":");

		long hrs = Long.parseLong(timeSplit[0]);
		long mins = Long.parseLong(timeSplit[1]);
		long secs = Long.parseLong(timeSplit[2]);

		return  (secs) + (60*mins) + (3600*hrs);

	}

	public List<Date> getDatesForNextNDays(Date date, int n) {

		List<Date> dates = new ArrayList<>();

		for ( int i = 0 ; i<=n ; i++ ) {
			dates.add(date);
			date = getNextDate(date);
		}

		return dates;
	}

	public Date getByPayCycle(boolean isPayCycle, Date date) {

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH,1);
		cal.set(Calendar.DAY_OF_MONTH, isPayCycle ? 25 : 1);

		return cal.getTime();

	}

	public Date getFirstDateOfNextMonth(Date date) {

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		cal.set(Calendar.DAY_OF_MONTH,1);
		cal.add(Calendar.MONTH, 1);

		return cal.getTime();
	}

	public Date getFirstDateOfPreviousMonth(Date date){
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		cal.set(Calendar.DAY_OF_MONTH,1);
		cal.add(Calendar.MONTH, -1);

		return cal.getTime();
	}
	public Date getLastDateOfPreviousMonth() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -1);

		int max = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
		cal.set(Calendar.DAY_OF_MONTH, max);

		return cal.getTime();
	}

}