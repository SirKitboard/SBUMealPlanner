package com.adibalwani.sbumealplanner.util;

/**
 * Created by Aditya on 5/2/2015.
 */
public class HistoryEntry {
	String date;
	String time;
	String location;
	double value;
	double balance;

	HistoryEntry(String datetime, String location, String value, String balance) {
		String[] dateTime = datetime.split(" ");
		date = dateTime[0];
		time = dateTime[1] + " " + dateTime[2];
		this.location = location;
		this.value = -1* Double.parseDouble(value);
		this.balance = Double.parseDouble(balance);
	}

	public String getDate() {
		return date;
	}

	public String getTime() {
		return time;
	}

	public String getLocation() {
		return location;
	}

	public double getValue() {
		return value;
	}

	public double getBalance() {
		return balance;
	}
}
