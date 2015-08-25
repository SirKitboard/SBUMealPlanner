package com.adibalwani.sbumealplanner;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


public class LoginActivity extends AppCompatActivity {
	EditText mUsernameEdit;
	EditText mPasswordEdit;
	CheckBox mRememberMe;
	Button mActionButtom;
	RequestTask requestTask;
	String html;
	String url1;
	String url2;
	String skey;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		SharedPreferences settings = getApplicationContext().getSharedPreferences("com.adibalwani.sbumealplanner", 0);
		String username = settings.getString("username","");
		String password = settings.getString("password","");
		//Log.e("Pass", password);
		boolean remember = settings.getBoolean("remember",false);
		mUsernameEdit = (EditText) findViewById(R.id.login_username);
		mPasswordEdit = (EditText) findViewById(R.id.login_password);
		mActionButtom = (Button) findViewById(R.id.login_button);
		mRememberMe = (CheckBox) findViewById(R.id.remember);
		if(!username.equalsIgnoreCase("")) {
			mUsernameEdit.setText(username);
		}
		if(!password.equalsIgnoreCase("")) {
			mPasswordEdit.setText(password);
		}
		if(remember) {
			mRememberMe.setChecked(true);
		}
		requestTask = new RequestTask();
		url1 = "https://services.jsatech.com/login.php?cid=129&";
		mActionButtom.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				requestTask.execute(url1);
				requestTask = new RequestTask();
			}
		});
	}

	private void setTrustAllCerts() throws Exception
	{
		TrustManager[] trustAllCerts = new TrustManager[]{
				new X509TrustManager() {
					public java.security.cert.X509Certificate[] getAcceptedIssuers() {
						return null;
					}
					public void checkClientTrusted( java.security.cert.X509Certificate[] certs, String authType ) {	}
					public void checkServerTrusted( java.security.cert.X509Certificate[] certs, String authType ) {	}
				}
		};

		// Install the all-trusting trust manager
		try {
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init( null, trustAllCerts, new java.security.SecureRandom() );
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
			HttpsURLConnection.setDefaultHostnameVerifier(
					new HostnameVerifier() {
						public boolean verify(String urlHostName, SSLSession session) {
							return true;
						}
					});
		}
		catch ( Exception e ) {
			//We can not recover from this exception.
			e.printStackTrace();
		}
	}

	public void getSkey(Document document) {
		String username = mUsernameEdit.getText().toString().trim();
		String password = mPasswordEdit.getText().toString().trim();
		Elements forms = document.getElementsByTag("form");
		url2 = forms.attr("action");
		String formUrl = url2;
		String cid = "129";
		String save = "1";
		String wason = "";
		skey = "";
		Elements formNodes = document.getElementsByTag("input");
		//Toast.makeText(getApplicationContext(),formNodes.size() + "",Toast.LENGTH_SHORT).show();
		for(int i=0;i<formNodes.size();i++) {
			Element element = formNodes.get(i);
			if(element.attr("name").equalsIgnoreCase("skey")) {
				skey = element.attr("value");
				//Toast.makeText(getApplicationContext(), skey, Toast.LENGTH_SHORT).show();
			}
		}
		LoginTask loginTask= new LoginTask();
		loginTask.execute(formUrl,cid,skey,save,wason,username,password);
		requestTask = new RequestTask();
	}

	public void getBalance(Connection.Response res) {
		try {
			Document doc = res.parse();
			Elements scripts = doc.getElementsByTag("script");
			//if()
			Log.e("Response", scripts.get(0).toString());
			if(scripts.get(0).toString().contains("window.location.href='/login.php")) {
				SharedPreferences settings = getApplicationContext().getSharedPreferences("com.adibalwani.sbumealplanner", 0);
				SharedPreferences.Editor editor = settings.edit();
				if(mRememberMe.isChecked()) {
					editor.putString("username",mUsernameEdit.getText().toString());
					editor.putString("password",mPasswordEdit.getText().toString());
					editor.putBoolean("remember",true);
				}
				else {
					editor.remove("username");
					editor.remove("password");
					editor.putBoolean("remember",false);
				}
				editor.apply();
				Intent intent = new Intent(getApplicationContext(), MainActivity.class);
				intent.putExtra("skey", skey);
				startActivity(intent);
				finish();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		//int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		return super.onOptionsItemSelected(item);
	}

	public void gotoRegister(View view) {
//		Intent intent = new Intent(this, SignUpActivity.class);
//		startActivity(intent);
	}

	class RequestTask extends AsyncTask<String, String, String> {
		Document document;

		@Override
		protected String doInBackground(String... uri) {
			try {
				document = Jsoup.connect(url1).ignoreHttpErrors(true).get();//docBuilder.parse(new ByteArrayInputStream(result.getBytes(SA)));
			}  catch (IOException e) {
				try {
					setTrustAllCerts();
					requestTask = new RequestTask();
					requestTask.execute(url1);
				} catch (Exception e1) {
					Toast.makeText(getApplicationContext(), e1.getMessage(), Toast.LENGTH_SHORT).show();
				}
				return "fail";
			}

			return "success";

		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if(result.equalsIgnoreCase("success")) {
				getSkey(document);
			}

		}
	}

	class LoginTask extends AsyncTask<String, String, String> {
		Connection.Response res;
		@Override
		protected String doInBackground(String... uri) {
			try {
				res = Jsoup
						.connect(uri[0])
						.data("cid", uri[1], "skey", uri[2],"save",uri[3],"wason",uri[4],"loginphrase",uri[5],"password",uri[6])
						.method(Connection.Method.POST)
						.execute();
			}  catch (IOException e) {
				Log.e("FAIL", "Request 2");
				return "fail";
			}

			return "success";

		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if(result.equalsIgnoreCase("success")) {
				getBalance(res);
			}

		}
	}


}
