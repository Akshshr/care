package com.example.care;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;


public class LoginActivity extends ActionBarActivity {
    EditText et_username;
    EditText et_password;
    Button btn_login;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        et_username = (EditText) findViewById(R.id.etUsername);
        et_password = (EditText) findViewById(R.id.etPassword);
        btn_login = (Button) findViewById(R.id.btnLogin);
        mContext = this;

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Globals.isNetworkAvailable(mContext)) {
                    if (et_username.getText().length() == 0) {
                        et_username.setError("This field is required");
                    } else if (et_password.getText().length() == 0) {
                        et_password.setError("This field is required");
                    } else {
                        try {
                            new startLogin().execute(et_username.getText()
                                    .toString(), encrypt(et_password.getText()
                                    .toString()));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    Toast.makeText(mContext, "No connection", Toast.LENGTH_LONG).show();
                }
            }
        });


        //TODO DELETE ME LATER FROM HERE
        Button btnGoHome = (Button) findViewById(R.id.btn_goHome);
        btnGoHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences prefs = getSharedPreferences("MyPref", 0);
                prefs.edit().putInt("user_id", 1).apply();
                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(intent);
                finishAffinity();
            }
        });

        //TODO TO HERE
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }


    private class startLogin extends AsyncTask<String, Integer, Void> {
        JSONObject response;
        boolean status = true;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // SHOW LOADING

        }

        @Override
        protected Void doInBackground(String... params) {

            ArrayList<NameValuePair> list = new ArrayList<NameValuePair>();
            JsonReader json = new JsonReader();

            list.add(new BasicNameValuePair("email", params[0]));
            list.add(new BasicNameValuePair("password", params[1]));
            list.add(new BasicNameValuePair("type", "login"));

            try {
                response = json.postJSONFromUrl(list, Globals.url
                        + "login");
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                status = response.getBoolean("status");
            } catch (Exception e1) {
                e1.printStackTrace();
            }
            if (status) {
                // LOGIN SUCCESS, GET DATA FROM SERVER

            } else {
                // LOGIN FAILED
                Log.e("LoginActivity", "Server returned login error");
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            if (status) {
                // GO TO NEXT ACTIVITY

            } else {
                // SHOW ERROR MESSAGE

            }

        }
    }

    public static String encrypt(String input) throws NoSuchAlgorithmException {
        String result = input;
        if (input != null) {
            MessageDigest md = MessageDigest.getInstance("MD5"); // or "SHA-1"
            md.update(input.getBytes());
            BigInteger hash = new BigInteger(1, md.digest());
            result = hash.toString(16);
            while (result.length() < 32) { // 40 for SHA-1
                result = "0" + result;
            }
        }
        return result;
    }
}