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
	private InputStream inputstream;

	public String errorStreamReader(String[] shellinput) {
		// TODO use pb instead of runtime
		StringBuilder command = new StringBuilder(Constants.EMPTY);
		for (int i = 0; i < shellinput.length; i++) {
			command.append(shellinput[i]);
		}
		InputStream inputstream = null;
		String esrval = "";
		try {
			inputstream = Runtime
					.getRuntime()
					.exec(command.toString()).getErrorStream();
			InputStreamReader inputstreamreader = new InputStreamReader(
					inputstream);
			esrval = new BufferedReader(inputstreamreader).readLine();
		} catch (IOException e) {
			Log.w(TAG, "Error in errorStreamReader()");
		}
		return esrval + "";

	}

	public String inputStreamReader(String[] shellinput, int length) {
		StringBuilder command = new StringBuilder(Constants.EMPTY);
		for (int i = 0; i < shellinput.length; i++) {
			command.append(shellinput[i]);
		}
		try {
			inputstream = Runtime.getRuntime().exec(command.toString())
					.getInputStream();
		} catch (IOException e) {
			Log.e("IOException", "exception in executing");
			e.printStackTrace();
			return null;
		}
		String isrstr = convertStreamToString(inputstream);
		return isrstr;
	}

	public String convertStreamToString(InputStream is) {
		try {
			return new Scanner(is).useDelimiter("\\A").next();
		} catch (NoSuchElementException e) {
			return "";
		}
	}
}
