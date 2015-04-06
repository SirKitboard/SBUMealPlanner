package com.adibalwani.sbumealplanner;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


public class LoginActivity extends ActionBarActivity {
	EditText usernameEdit;
	EditText passwordEdit;
	EditText passwordConfirmEdit;
	Button mActionButtom;
	RequestTask requestTask;
	String html;
	String url1;
	String skey;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		usernameEdit = (EditText) findViewById(R.id.login_username);
		passwordEdit = (EditText) findViewById(R.id.login_password);
		mActionButtom = (Button) findViewById(R.id.login_button);
		requestTask = new RequestTask();
		url1 = "https://services.jsatech.com/login.php?cid=129&";
		mActionButtom.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				requestTask.execute(url1);
			}
		});
	}

	public void getSkey(String result) {
		String username = usernameEdit.getText().toString().trim();
		String password = passwordEdit.getText().toString().trim();
		boolean fail = false;
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			Document document = docBuilder.parse(new ByteArrayInputStream(result.getBytes("UTF-8")));

			NodeList formNodes = document.getElementsByTagName("input");
			for(int i=0;i<formNodes.getLength();i++) {
				Node node = formNodes.item(i);
				Element element = (Element) formNodes.item(i);
				if(element.getAttribute("name").equalsIgnoreCase("skey")) {
					skey = element.getAttribute("value");
					Toast.makeText(getApplicationContext(), skey, Toast.LENGTH_SHORT).show();
				}
				else {
					Toast.makeText(getApplicationContext(), element.getAttribute("name"),Toast.LENGTH_SHORT).show();
				}
			}
					
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
//		if (username.length() == 0) {
//			usernameEdit.setError("Invalid username");
//			fail = true;
//		}
//		if (!fail) {
//			ParseUser.logInInBackground(username, password, new LogInCallback() {
//				@Override
//				public void done(ParseUser user, ParseException e) {
//					if (e != null) {
//						// Show the error message
//						Toast.makeText(LoginActivity.this, e.getMessage(),
//								Toast.LENGTH_LONG).show();
//					} else {
//						// Start an intent for the dispatch activity
//						user.put("remember",false);
//						Intent intent = new Intent(LoginActivity.this, DispatchActivity.class);
//						intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//						startActivity(intent);
//					}
//				}
//			});
//		}
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
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	public void gotoRegister(View view) {
//		Intent intent = new Intent(this, SignUpActivity.class);
//		startActivity(intent);
	}

	class RequestTask extends AsyncTask<String, String, String> {
		String response2;

		@Override
		protected String doInBackground(String... uri) {
			HttpClient httpclient = new DefaultHttpClient();
			HttpResponse response;
			String responseString = null;
			try {
				response = httpclient.execute(new HttpGet(uri[0]));
				StatusLine statusLine = response.getStatusLine();
				if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
					ByteArrayOutputStream out = new ByteArrayOutputStream();
					response.getEntity().writeTo(out);
					out.close();
					responseString = out.toString();
				} else {
					//Closes the connection.
					response.getEntity().getContent().close();
					throw new IOException(statusLine.getReasonPhrase());
				}
			} catch (ClientProtocolException e) {

			} catch (IOException e) {

			}
			response2 = responseString;
			return responseString;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			html = result;
			Toast.makeText(getApplicationContext(),"Task complete",Toast.LENGTH_SHORT).show();
			getSkey(result);
		}

		public String getResponse() {
			return response2;
		}
	}

}
