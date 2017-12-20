package com.payease.p2p.base.util;

import org.apache.commons.lang.time.DateUtils;

import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	/**
	 * 得到一天的最后一秒钟
	 * 
	 * @param d
	 * @return
	 */
	public static Date endOfDay(Date d) {
		return DateUtils.addSeconds(
				DateUtils.addDays(DateUtils.truncate(d, Calendar.DATE), 1), -1);
	}

	/**
	 * 两个时间的间隔秒
	 * Math.abs() 数字绝对值
	 * @return
	 */
	public static long getSecondsBetween(Date d1, Date d2) {
		return Math.abs((d1.getTime() - d2.getTime()) / 1000);
	}
}