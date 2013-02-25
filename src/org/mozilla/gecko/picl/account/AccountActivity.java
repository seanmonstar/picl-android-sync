package org.mozilla.gecko.picl.account;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;

import org.mozilla.gecko.picl.R;

import com.github.kevinsawicki.http.HttpRequest;
import com.github.kevinsawicki.http.HttpRequest.HttpRequestException;

import android.accounts.AccountAuthenticatorActivity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AccountActivity extends AccountAuthenticatorActivity {
	
	private static final String TAG = "AccountAuthenticatorActivity";

	static final String KEY_SERVER = "http://192.168.1.100:8090";
	static final String KEY_SERVER_USER = "user";
	
	static final String KEY_SERVER_POST = KEY_SERVER + "/" + KEY_SERVER_USER;
	static final String KEY_SERVER_GET = KEY_SERVER_POST + "/";
	
	private EditText emailText;
	private Button submitButton;
	
	private boolean isGetting = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		emailText = (EditText) findViewById(R.id.email);
		submitButton = (Button) findViewById(R.id.submit);
		
		submitButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
				String email = emailText.getText().toString();
				if (TextUtils.isEmpty(email)) return;
				
				if (isGetting) return;
				isGetting = true;
				
				new GetUserKeyTask().execute(email);
			}
			
		});
	}
	
	private class GetUserKeyTask extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... params) {
			String email = params[0];
			String uri = Uri.parse(KEY_SERVER_GET).buildUpon()
					//.appendPath(KEY_SERVER_USER_ROUTE)
					.appendQueryParameter("email", email)
					.build().toString();
			
			// if email has been used before, we can get GET the key
			HttpRequest request = null;
			String response = null;
			
			request = HttpRequest.get(uri);
			if (request.ok()) {
				response = request.body();
			} else {
				Log.i(TAG, request.code() + " " + request.message() + "\n" + request.body());
			}
			
			// if that GET is 4xx, then we should POST to create a key
			if (response == null) {
				//String json = "{\"email\":\"" + email + "\"}";
				String send = "email=" + email;
				request = HttpRequest.post(KEY_SERVER_POST).send(send);
				if (request.ok() /*request.created()*/) {
					response = request.body();//request("POST", uri, json);
				} else {
					Log.i(TAG, request.code() + " " + request.message() + "\n" + request.body());
				}
				
			}
			
			if (response != null) {
				//lets find the key in the JSON
				
			}
			
			return response;
		}
		
		@Override
		protected void onPostExecute(String email) {
			isGetting = false;
			
		}

	}
}
