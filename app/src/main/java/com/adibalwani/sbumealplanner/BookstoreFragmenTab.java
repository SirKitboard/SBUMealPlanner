package com.adibalwani.sbumealplanner;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Aditya on 4/8/2015.
 */
public class BookstoreFragmenTab extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
		View rootview = inflater.inflate(R.layout.fragment_bookstore, container, false);
		setRetainInstance(true);
		return rootview;
	}
}
