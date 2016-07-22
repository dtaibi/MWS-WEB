package de.unihannover.l3s.mws.bean;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TestParseDate {

	public static void main(String[] args) {
		String t3="Timestamp: Wed Mar 02 20:31:44 CEST 2011";
		 String s3=	"http://wayback.archive-it.org/1068/20110302222211/http://www.i-indiaonline.com//";
		 DateFormat df1=new SimpleDateFormat("EEE MMM dd hh:mm:ss z yyyy",Locale.ENGLISH);
		 DateFormat df2=new SimpleDateFormat("dd MMM yyyy",Locale.ENGLISH);
		 try {
			Date date = df1.parse(t3.replace("Timestamp: ", ""));
			System.out.println(df2.format(date));
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}

}
