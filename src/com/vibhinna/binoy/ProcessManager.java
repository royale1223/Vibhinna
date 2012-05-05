package com.vibhinna.binoy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.NoSuchElementException;
import java.util.Scanner;

import android.util.Log;

public class ProcessManager {
	private static final String TAG = null;
	private InputStream inputstream;

	public String errorStreamReader(String[] shellinput) {
		// TODO use pb instead of runtime
		InputStream inputstream = null;
		String esrval = "";
		// hack to prevent executing null
		for (int i = 0; i <= 3; i++) {
			if (shellinput[i] == null) {
				shellinput[i] = "";
			}
		}
		try {
			inputstream = Runtime
					.getRuntime()
					.exec(shellinput[0] + shellinput[1] + shellinput[2]
							+ shellinput[3]).getErrorStream();
			InputStreamReader inputstreamreader = new InputStreamReader(
					inputstream);
			esrval = new BufferedReader(inputstreamreader).readLine();
		} catch (IOException e) {
			Log.w(TAG, "Error in errorStreamReader()");
		}
		Log.i("execout", esrval + "");
		return esrval + "";

	}


	public String inputStreamReader(String[] shellinput, int length) {
		// TODO use pb instead of runtime
		StringBuilder command = new StringBuilder("");
		for (int i = 0; i < shellinput.length; i++) {
			command.append(shellinput[i]);
		}
		try {
			inputstream = Runtime.getRuntime().exec(command.toString())
					.getInputStream();
		} catch (IOException e) {
			Log.w("IOException", "exception in executing");
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
