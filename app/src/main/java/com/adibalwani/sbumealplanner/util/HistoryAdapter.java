package com.adibalwani.sbumealplanner.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.adibalwani.sbumealplanner.R;

import java.util.List;

/**
 * Created by Aditya on 5/22/2015.
 */
public class HistoryAdapter extends ArrayAdapter<HistoryEntry>{
	public HistoryAdapter(Context context, int textViewResourceId) {
		super(context,textViewResourceId);
	}
	public HistoryAdapter(Context context, int textViewResourceId, List<HistoryEntry> items) {
		super(context,textViewResourceId, items);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;

		if(v == null) {
			LayoutInflater vi;
			vi = LayoutInflater.from(getContext());
			v = vi.inflate(R.layout.history_row,null);
		}
		HistoryEntry h = getItem(position);
		if(h!=null) {
			TextView date = (TextView) v.findViewById(R.id.date);
			TextView time = (TextView) v.findViewById(R.id.time);
			TextView location = (TextView) v.findViewById(R.id.location);
			TextView cost = (TextView) v.findViewById(R.id.cost);
			if(date!=null) {
				date.setText(h.getDate());
			}
			if(time!=null) {
				time.setText(h.getTime());
			}
			if(location!=null) {
				location.setText(h.getLocation());
			}
			if(cost!=null) {
				cost.setText(h.getValue() + "$");
			}
		}
		return v;
	}
}
