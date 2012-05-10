package com.vibhinna.binoy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.NoSuchElementException;
import java.util.Scanner;

import android.util.Log;

public class ProcessManager {
	private static final String TAG = "com.vibhinna.binoy.ProcessManager";
	
	public String errorStreamReader(String[] shellinput) {
		// TODO use pb instead of runtime
		StringBuilder command = new StringBuilder(Constants.EMPTY);
		for (int i = 0; i < shellinput.length; i++) {
			command.append(shellinput[i]);
		}
		Log.d(TAG, "execute : " + command.toString());
		InputStream inputstream = null;
		String esrval = "";
		try {
			inputstream = Runtime.getRuntime().exec(command.toString()).getErrorStream();
			InputStreamReader inputstreamreader = new InputStreamReader(inputstream);
			esrval = new BufferedReader(inputstreamreader).readLine();
		} catch (IOException e) {
			Log.w(TAG, "Error in errorStreamReader()");
		}
		return esrval + "";

	}

	public static String inputStreamReader(String[] shellinput, int length) {
		StringBuilder command = new StringBuilder(Constants.EMPTY);
		InputStream inputstream;
		for (int i = 0; i < shellinput.length; i++) {
			
			command.append(shellinput[i]);
		}
		Log.d(TAG, "execute : " + command.toString());
		try {
			inputstream = Runtime.getRuntime().exec(command.toString()).getInputStream();
		} catch (IOException e) {
			Log.e("IOException", "exception in executing");
			e.printStackTrace();
			return null;
		}
		String isrstr = convertStreamToString(inputstream);
		return isrstr;
	}

	public static String convertStreamToString(InputStream is) {
		try {
			return new Scanner(is).useDelimiter("\\A").next();
		} catch (NoSuchElementException e) {
			return "";
		}
	}
}
