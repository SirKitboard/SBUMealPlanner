package com.adibalwani.sbumealplanner;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Aditya on 4/8/2015.
 */
public class MealPlanFragmenTab extends Fragment {
	String crazyHTML;
	View view;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
		view = inflater.inflate(R.layout.fragment_mealplan, container, false);
		setRetainInstance(true);
		return view;
	}

//	public MealPlanFragmenTab createData(String crazyHTML) {
//
//		TextView balance = (TextView) view.findViewById(R.id.mealBalance);
//		Double balanceValue = Double.parseDouble(crazyHTML.substring(crazyHTML.indexOf("Current Balance:") + "Current Balance:".length(), +crazyHTML.indexOf("</b>", crazyHTML.indexOf("Current Balance:"))));
//		balance.setText("$" + crazyHTML.substring(crazyHTML.indexOf("Current Balance:") + "Current Balance:".length(), +crazyHTML.indexOf("</b>", crazyHTML.indexOf("Current Balance:"))));
//		TextView dailyBudget = (TextView) view.findViewById(R.id.budget);
//		double dailyBudgetValue = balanceValue/getNumDaysLeft();
//		dailyBudget.setText("$" + String.format("%.2f",dailyBudgetValue));
//
//		return this;
//	}
//
//	public long getNumDaysLeft() {
//		long ret = 1;
//		try {
//			SimpleDateFormat formater=new SimpleDateFormat("yyyy-MM-dd");
//			long d1 = new Date().getTime();
//			long d2=formater.parse("2015-05-20").getTime();
//
//			ret = Math.abs((d2-d1)/(1000*60*60*24));
//
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
//		return ret;
//	}



}
