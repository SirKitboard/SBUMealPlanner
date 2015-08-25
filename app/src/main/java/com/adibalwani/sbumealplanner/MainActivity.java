package com.adibalwani.sbumealplanner;

import android.app.ActionBar;
import android.support.v4.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
	String skey;
	String urlBalance;
	String urlLogin;
	String urlLogout;
	String crazyHTML;
	ProgressDialog dialog;
	BalanceRequestTask brt;
	SectionsPagerAdapter mSectionsPagerAdapter;
	ViewPager mViewPager;
	boolean loaded = false;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		skey = this.getIntent().getExtras().getString("skey");
		setContentView(R.layout.activity_main);
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
		mViewPager.setAdapter(mSectionsPagerAdapter);
		mViewPager.setOffscreenPageLimit(2);
		urlLogin = "https://services.jsatech.com/login.php?skey=" + skey + "&cid=129&fullscreen=1&wason=";
		urlBalance = "https://services.jsatech.com/index.php?skey=" + skey + "&cid=129&fullscreen=1&wason=";
		urlLogout = "https://services.jsatech.com/logout.php?skey=" + skey + "&cid=129&goto=index.php?cid=129";
		if(savedInstanceState!=null) {
			loaded = savedInstanceState.getBoolean("loaded");
			if(loaded) {
				crazyHTML = savedInstanceState.getString("htmlData");
				StallTask stallTask = new StallTask();
				stallTask.execute("");
			}
		}
		else if (!loaded){
			dialog = ProgressDialog.show(MainActivity.this, "", "Loading. Please wait...", true);
			BackgroundLoginTask blt = new BackgroundLoginTask();
			blt.execute(urlLogin);
		}
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_logout) {
			LogoutTask logout = new LogoutTask();
			logout.execute(urlLogout);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void logoutComplete() {
		Intent intent = new Intent(this, LoginActivity.class);
		startActivity(intent);
		finish();
	}

	public void storeDocument(Document document) {
		crazyHTML = document.body().html();
		loaded = true;
		requestSuccess();
		loadWolfie();
		loadBookstore();
	}

	public void requestSuccess() {
		//Elements tables = document.getElementsByClass("boxOutside");

		TextView balance = (TextView) findViewById(R.id.mealBalance);
		Double balanceValue = Double.parseDouble(crazyHTML.substring(crazyHTML.indexOf("Current Balance:") + "Current Balance:".length(), +crazyHTML.indexOf("</b>", crazyHTML.indexOf("Current Balance:"))));
		balance.setText("$" + crazyHTML.substring(crazyHTML.indexOf("Current Balance:") + "Current Balance:".length(), crazyHTML.indexOf("</b>", crazyHTML.indexOf("Current Balance:"))));
		TextView dailyBudget = (TextView) findViewById(R.id.budget);
		double dailyBudgetValue = balanceValue/getNumDaysLeft();
		dailyBudget.setText("$" + String.format("%.2f",dailyBudgetValue));
		if(dialog!=null)
			dialog.hide();
	}

	public void loadWolfie() {
		TextView balance = (TextView) findViewById(R.id.wolfieBalance);
		balance.setText("$" + crazyHTML.substring(indexOfString(crazyHTML,"Current Balance:",2) + "Current Balance:".length(), crazyHTML.indexOf("</b>", indexOfString(crazyHTML,"Current Balance:",2))));
	}

	public void loadBookstore() {
		TextView balance = (TextView) findViewById(R.id.bookBalance);
		balance.setText("$" + crazyHTML.substring(indexOfString(crazyHTML,"Current :",1) + "Current :".length(), crazyHTML.indexOf("</b>", indexOfString(crazyHTML,"Current :",1))));
	}

	public int indexOfString(String source, String search, int index) {
		int retValue = -1;
		while(index>0) {
			retValue++;
			retValue = source.indexOf(search, retValue);
			index--;
		}
		return retValue;
	}

	public long getNumDaysLeft() {
		long ret = 1;
		try {
			SimpleDateFormat formater=new SimpleDateFormat("yyyy-MM-dd");
			long d1 = new Date().getTime();
			long d2=formater.parse("2015-05-20").getTime();

			ret = Math.abs((d2-d1)/(1000*60*60*24));

		} catch (ParseException e) {
			e.printStackTrace();
		}
		return ret;
	}

	public void requestFail() {
		brt = new BalanceRequestTask();
		brt.execute(urlBalance);
	}

	@Override
	protected void onSaveInstanceState(Bundle newState) {
		super.onSaveInstanceState(newState);
		newState.putBoolean("loaded",true);
		newState.putString("htmlData", crazyHTML);
	}


	//ALL TASKS
	class BalanceRequestTask extends AsyncTask<String, String, String> {
		Document document;

		@Override
		protected String doInBackground(String... uri) {
			try {
				document = Jsoup.connect(uri[0]).get();//docBuilder.parse(new ByteArrayInputStream(result.getBytes(SA)));
			}  catch (IOException e) {
				return "fail";
			}

			return "success";

		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if(result.equalsIgnoreCase("success")) {
				String crazyHTML = document.body().toString();
				if (crazyHTML.contains("Current Balance")) {
					storeDocument(document);
				}
				else {
					requestFail();
					Log.e("Fail Level", "2");
				}
			}
			else {
				requestFail();
				Log.e("Fail Level", "1");
			}
		}
	}

	class BackgroundLoginTask extends AsyncTask<String, String, String> {
		Document document;

		@Override
		protected String doInBackground(String... uri) {
			try {
				document = Jsoup.connect(uri[0]).get();//docBuilder.parse(new ByteArrayInputStream(result.getBytes(SA)));
			}  catch (IOException e) {
				return "fail";
			}

			return "success";

		}

		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if(result.equalsIgnoreCase("success")) {
				requestFail();
			}
			else {
				finish();
			}
		}
	}

	class LogoutTask extends AsyncTask<String, String, String> {
		@Override
		protected String doInBackground(String...uri) {
			try {
				Document document = Jsoup.connect(uri[0]).get();//docBuilder.parse(new ByteArrayInputStream(result.getBytes(SA)));
			}  catch (IOException e) {
				return "fail";
			}

			return "success";
		}


		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			logoutComplete();
		}
	}

	public class SectionsPagerAdapter extends FragmentPagerAdapter {
		MealPlanFragmenTab mealPlanFragmenTab = new MealPlanFragmenTab();
		WolfieFragmenTab wolfieFragmenTab = new WolfieFragmenTab();
		BookstoreFragmenTab bookstoreFragmenTab = new BookstoreFragmenTab();
		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public android.support.v4.app.Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a PlaceholderFragment (defined as a static inner class below).
			switch (position) {
				case 0:
					return mealPlanFragmenTab;
				case 1:
					return wolfieFragmenTab;
				case 2:
					return bookstoreFragmenTab;
			}
			return null;

//			if(position == 0) {
//				return mealPlanFragmenTab;
//			}
//			else if(position == 1) {
//				return wolfieFragmenTab;
//			}
//			else {
//				return bookstoreFragmenTab;
//			}

		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
				case 0:
					return "Meal Plan".toUpperCase(l);
				case 1:
					return "Wolfie Wallet".toUpperCase(l);
				case 2:
					return "Bookstore Account".toUpperCase(l);
			}
			return null;
		}
	}

	class StallTask extends AsyncTask<String, String, String> {
		@Override
		protected String doInBackground(String...uri) bash


		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			requestSuccess();
		}
	}

	public void openHistory(View v) {
		Intent intent = new Intent(this, HistoryActivity.class);
		intent.putExtra("skey", skey);
		startActivity(intent);
	}

}
