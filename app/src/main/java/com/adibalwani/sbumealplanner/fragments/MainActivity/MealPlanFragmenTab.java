package com.adibalwani.sbumealplanner.fragments.MainActivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adibalwani.sbumealplanner.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Aditya on 4/8/2015.
 */
public class MealPlanFragmenTab extends Fragment {
	String crazyHTML;
	View view;

	public static final String ARG_PAGE = "ARG_PAGE";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
		view = inflater.inflate(R.layout.fragment_mealplan, container, false);
		setRetainInstance(true);
		return view;
	}

	public static MealPlanFragmenTab newInstance(int page) {
		Bundle args = new Bundle();
		args.putInt(ARG_PAGE, page);
		MealPlanFragmenTab fragment = new MealPlanFragmenTab();
		fragment.setArguments(args);
		return fragment;
	}
}
