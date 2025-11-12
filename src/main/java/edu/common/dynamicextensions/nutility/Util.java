package edu.common.dynamicextensions.nutility;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.beanutils.MethodUtils;

import edu.common.dynamicextensions.napi.FormException;

public class Util {
	public static boolean isOraTimestamp(Object obj) {
		if (obj == null) {
			return false;
		}
		
		return obj.getClass().getName().equals("oracle.sql.TIMESTAMP");
	}

	public static LocalDate getLocalDateFromOraTimestamp(Object obj) {
		if (obj == null) {
			return null;
		}

		try {
			Timestamp time = (Timestamp)MethodUtils.invokeExactMethod(obj, "timestampValue", null);
			return time.toLocalDateTime().toLocalDate();
		} catch (Exception e) {
			throw new FormException("Error converting to timestamp: " + obj.getClass().getName());
		}
	}
	
	public static Date getDateFromOraTimestamp(Object obj) {
		if (obj == null) {
			return null;
		}
	
		try {
			Timestamp time = (Timestamp)MethodUtils.invokeExactMethod(obj, "timestampValue", null);
			Calendar cal = Calendar.getInstance();
			cal.setTimeInMillis(time.getTime());
			return cal.getTime();							
		} catch (Exception e) {
			throw new FormException("Error converting to timestamp: " + obj.getClass().getName());
		}
	}
}
