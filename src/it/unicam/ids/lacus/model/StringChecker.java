package it.unicam.ids.lacus.model;

import it.unicam.ids.lacus.database.DatabaseOperation;
import it.unicam.ids.lacus.view.Alerts;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringChecker {
	public int characterAndNumberChecker(String s) {
		if (s == null || s.trim().isEmpty())
			return 0; //La stringa è vuota
		Pattern p = Pattern.compile("[^A-Za-z0-9]");
		Matcher m = p.matcher(s);
		boolean b = m.find();
		if (b)
			return -1; //La stringa non è valida
		else
			return 1; //La stringa è valida
	}

	int characterNumberAndSpacesChecker(String s) {
		if (s == null || s.trim().isEmpty())
			return 0; //La stringa è vuota
		Pattern p = Pattern.compile("[^A-Za-z0-9]+$");
		Matcher m = p.matcher(s);
		boolean b = m.find();
		if (b)
			return -1; //La stringa non è valida
		else
			return 1; //La stringa è valida
	}

	int characterAndSpacesChecker(String s) {
		if (s == null || s.trim().isEmpty())
			return 0; //La stringa è vuota
		Pattern p = Pattern.compile("[^A-Za-z]+$");
		Matcher m = p.matcher(s);
		boolean b = m.find();
		if (b)
			return -1; //La stringa non è valida

		else
			return 1; //La stringa è valida
	}

	int numberOnlyChecker(String cr) {
		if (cr == null || cr.trim().isEmpty()) {
			return 0; //La stringa è vuota
		}
		Pattern p = Pattern.compile("[1-9]");
		Matcher m = p.matcher(cr);
		boolean b = m.find();
		if (!b)
			return -1; //La stringa non è valida
		else
			return 1; //La stringa è valida

	}

	int emailChecker(String email) {
		if (email == null || email.trim().isEmpty()) {
			return 0; //La stringa è vuota
		}
		Pattern p = Pattern.compile("[a-zA-Z0-9._%-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}");
		Matcher m = p.matcher(email);
		boolean b = m.find();
		if (b) {
			String emailDomain = email.substring((email.indexOf("@") + 1));
			// ESEGUO LA QUERY SUL DATABASE
			String sql = "SELECT * FROM email WHERE email='" + emailDomain + "';";
			DatabaseOperation emailOperation = new DatabaseOperation();
			if (emailOperation.searchElement(sql))
				return 1; //La stringa è valida
			else
				return -2; //Il dominio della mail non esiste
		} else
			return -1; //La stringa non è valida
	}

	int cfChecker(String cf) {
		if(cf == null || cf.trim().isEmpty()) {
			return 0; //La stringa è vuota
		}
		if(cf.length() == 16) {
			boolean flag1 = cf.substring(0,6).matches("[a-zA-Z]+");
			boolean flag2 = cf.substring(6,8).matches("\\d+");
			boolean flag3 = Character.isLetter(cf.charAt(8));
			boolean flag4 = cf.substring(9,11).matches("\\d+");
			boolean flag5 = Character.isLetter(cf.charAt(11));
			boolean flag6 = cf.substring(12,15).matches("\\d+");
			boolean flag7 = Character.isLetter(cf.charAt(15));
			if(flag1 && flag2 && flag3 && flag4 && flag5 && flag6 && flag7) {
				return 1; //La stringa è valida
			}
		}
		return -1; //La stringa non è valida
	}

	int cityChecker(String city) {
		if (city == null || city.trim().isEmpty()) {
			return 0; //La stringa è vuota
		}
		// ESEGUO LA QUERY SUL DATABASE
		String sql = "SELECT * FROM cities WHERE city='" + city + "';";
		DatabaseOperation cityOperation = new DatabaseOperation();
		if(cityOperation.searchElement(sql)) {
			return 1; //La stringa è valida
		}
		else {
			return -1; //La stringa non è valida
		}
	}

	boolean idChecker(String id) {
		// ESEGUO LA QUERY SUL DATABASE
		String sql = "SELECT * FROM users WHERE userid='" + id + "';";
		DatabaseOperation userOperation = new DatabaseOperation();
		return userOperation.searchElement(sql);
	}

	public boolean revenueChecker(String stringa) {
		try {
			double revenue = Double.parseDouble(stringa);
			return revenue >= 0.00;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public boolean dateChecker(LocalDate data) {
		if(data != null) {
			Calendar calendar = Calendar.getInstance();
			calendar.set(Calendar.HOUR_OF_DAY, 0);
			calendar.set(Calendar.MINUTE, 0);
			calendar.set(Calendar.SECOND, 0);
			calendar.set(Calendar.MILLISECOND, 0);
			return !Date.from(Instant.from(data.atStartOfDay(ZoneId.systemDefault()))).before(calendar.getTime());
		}
		return false;
	}

	String dateConverter(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date d = sdf.parse(date);
			sdf.applyPattern("dd/MM/yyyy");
			return sdf.format(d);
		}
		catch(ParseException e) {
			Alerts alert = new Alerts();
			alert.printDateErrorMessage();
		}
		return "";
	}
}