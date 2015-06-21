package com.example.care;

import java.text.DecimalFormat;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Globals {


	public static final String url = "https://elenarie.azurewebsites.net/";

	public static boolean isNetworkAvailable(Context context) {
		try {
			NetworkInfo activeNetworkInfo = ((ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE))
					.getActiveNetworkInfo();
			return activeNetworkInfo != null && activeNetworkInfo.isConnected();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
