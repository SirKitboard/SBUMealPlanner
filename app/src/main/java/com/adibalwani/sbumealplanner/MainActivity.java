package com.adibalwani.sbumealplanner;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends ActionBarActivity {
	String skey;
	String urlBalance;
	String urlLogin;
	String urlLogout;
	ProgressDialog dialog;
	BalanceRequestTask brt;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		skey = this.getIntent().getExtras().getString("skey");
		setContentView(R.layout.activity_main);
		Toolbar mToolBar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(mToolBar);
		urlLogin = "https://services.jsatech.com/login.php?skey=" + skey + "&cid=129&fullscreen=1&wason=";
		urlBalance = "https://services.jsatech.com/index.php?skey=" + skey + "&cid=129&fullscreen=1&wason=";
		urlLogout = "https://services.jsatech.com/logout.php?skey=" + skey + "&cid=129&goto=index.php?cid=129";
		dialog = ProgressDialog.show(MainActivity.this, "", "Loading. Please wait...", true);
		BackgroundLoginTask blt = new BackgroundLoginTask();
		blt.execute(urlLogin);
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

	public void requestSuccess(Document document) {
		//Elements tables = document.getElementsByClass("boxOutside");
		String crazyHTML = document.body().toString();
		TextView balance = (TextView) findViewById(R.id.balance);
		Double balanceValue = Double.parseDouble(crazyHTML.substring(crazyHTML.indexOf("Current Balance:") + "Current Balance:".length(), +crazyHTML.indexOf("</b>", crazyHTML.indexOf("Current Balance:"))));
		balance.setText("$" + crazyHTML.substring(crazyHTML.indexOf("Current Balance:") + "Current Balance:".length(), +crazyHTML.indexOf("</b>", crazyHTML.indexOf("Current Balance:"))));
		TextView dailyBudget = (TextView) findViewById(R.id.budget);
		double dailyBudgetValue = balanceValue/getNumDaysLeft();
		dailyBudget.setText("$" + String.format("%.2f",dailyBudgetValue));
		dialog.hide();
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
					requestSuccess(document);
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
}
