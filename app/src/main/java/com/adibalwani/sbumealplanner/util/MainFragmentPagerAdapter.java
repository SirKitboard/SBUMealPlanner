package com.adibalwani.sbumealplanner.util;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;
import android.view.View;

import com.adibalwani.sbumealplanner.fragments.MainActivity.BookstoreFragmenTab;
import com.adibalwani.sbumealplanner.fragments.MainActivity.MealPlanFragmenTab;
import com.adibalwani.sbumealplanner.fragments.MainActivity.WolfieFragmenTab;

/**
 * Created by Aditya on 8/25/2015.
 */
public class MainFragmentPagerAdapter extends FragmentPagerAdapter{
    final int PAGE_COUNT = 3;
    private String tabTitles[] = new String[] { "Meal Plan", "Wolfie Wallet","Bookstore Account" };

    private Context context;

    public MainFragmentPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public Fragment getItem(int position) {
        if(position == 0) {
            return MealPlanFragmenTab.newInstance(position + 1);
        }
        else if(position == 1) {
            return WolfieFragmenTab.newInstance(position + 1);
        }
        else if(position == 2) {
            return BookstoreFragmenTab.newInstance(position + 1);
        }
        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        return tabTitles[position];
    }

    public View getTabView(int position) {
        // Given you have a custom layout in `res/layout/custom_tab.xml` with a TextView and ImageView
        View v = null;
        Log.e("TabNum", position + "");
//		if(position == 0) {
//			v =  LayoutInflater.from(context).inflate(R.layout.fragment_browse, null);
//		}
//		else if(position == 1) {
//			v =  LayoutInflater.from(context).inflate(R.layout.fragment_saved, null);
//		}
        return v;
    }
}
