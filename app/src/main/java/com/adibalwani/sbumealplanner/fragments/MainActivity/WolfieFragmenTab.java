package com.adibalwani.sbumealplanner.fragments.MainActivity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adibalwani.sbumealplanner.R;

/**
 * Created by Aditya on 4/8/2015.
 */
public class WolfieFragmenTab extends Fragment {

	public int mPage;

	public static final String ARG_PAGE = "ARG_PAGE";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mPage = getArguments().getInt(ARG_PAGE);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
		View rootview = inflater.inflate(R.layout.fragment_wolfie, container, false);
		setRetainInstance(true);
		return rootview;
	}

	public static WolfieFragmenTab newInstance(int page) {
		Bundle args = new Bundle();
		args.putInt(ARG_PAGE, page);
		WolfieFragmenTab fragment = new WolfieFragmenTab();
		fragment.setArguments(args);
		return fragment;
	}
}
