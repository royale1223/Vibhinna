package com.vibhinna.binoy;

import java.io.IOException;
import java.io.InputStream;
import java.util.NoSuchElementException;
import java.util.Scanner;

import android.util.Log;

public class ProcessManager {
	private static final String TAG = "com.vibhinna.binoy.ProcessManager";

	/**
	 * Reads ErrorStream of a command and executes it
	 * 
	 * @param shellinput
	 *            String array of the command, spaces required
	 * @return ErrorStream of the command as a String.
	 */
	public static String errorStreamReader(String[] shellinput) {
		// TODO use pb instead of runtime
		StringBuilder command = new StringBuilder(Constants.EMPTY);
		for (int i = 0; i < shellinput.length; i++) {
			command.append(shellinput[i]);
		}
		Log.d(TAG, "execute : " + command.toString());
		try {
			return convertStreamToString(Runtime.getRuntime()
					.exec(command.toString()).getErrorStream());
		} catch (IOException e) {
			Log.d(TAG, "Error!");
			return Constants.EMPTY;
		}
	}

	/**
	 * Reads InputStream of a command and executes it
	 * 
	 * @param shellinput
	 *            String array of the command, spaces required
	 * @return InputStream of the command as a String.
	 */
	public static String inputStreamReader(String[] shellinput, int length) {
		StringBuilder command = new StringBuilder(Constants.EMPTY);
		for (int i = 0; i < shellinput.length; i++) {
			command.append(shellinput[i]);
		}
		Log.d(TAG, "execute : " + command.toString());
		try {
			return convertStreamToString(Runtime.getRuntime()
					.exec(command.toString()).getInputStream());
		} catch (IOException e) {
			Log.d(TAG, "Error!");
			return Constants.EMPTY;
		}
	}

	/**
	 * Converts an InputStream to String
	 * 
	 * @param is
	 *            InputStream
	 * @return InputStream converted to String
	 */
	public static String convertStreamToString(InputStream is) {
		try {
			return new Scanner(is).useDelimiter("\\A").next();
		} catch (NoSuchElementException e) {
			return "";
		}
	}
}
