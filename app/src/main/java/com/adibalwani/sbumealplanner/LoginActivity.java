package com.adibalwani.sbumealplanner;

import android.content.Intent;
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


public class LoginActivity extends ActionBarActivity {
	EditText usernameEdit;
	EditText passwordEdit;
	EditText passwordConfirmEdit;
	Button mActionButtom;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		usernameEdit = (EditText) findViewById(R.id.login_username);
		passwordEdit = (EditText) findViewById(R.id.login_password);
		mActionButtom = (Button) findViewById(R.id.login_button);
		mActionButtom.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				signin();
			}
		});
	}

	public void signin() {
		String username = usernameEdit.getText().toString().trim();
		String password = passwordEdit.getText().toString().trim();
		boolean fail = false;
		if (username.length() == 0) {
			usernameEdit.setError("Invalid username");
			fail = true;
		}
		if (password.length() <= 8) {
			passwordEdit.setError("Invalid password");
			fail = true;
		}
		if (!fail) {
			ParseUser.logInInBackground(username, password, new LogInCallback() {
				@Override
				public void done(ParseUser user, ParseException e) {
					if (e != null) {
						// Show the error message
						Toast.makeText(LoginActivity.this, e.getMessage(),
								Toast.LENGTH_LONG).show();
					} else {
						// Start an intent for the dispatch activity
						Intent intent = new Intent(LoginActivity.this, DispatchActivity.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
						startActivity(intent);
					}
				}
			});
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
}
