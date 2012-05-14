package com.vibhinna.binoy;

import java.io.File;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Pattern;

import android.content.Context;
import android.database.MatrixCursor;
import android.provider.BaseColumns;
import android.util.Log;

public class PropManager {
	public static final String TAG = "com.vibhinna.binoy.PropManager";
	private Context mContext;

	public PropManager(Context context) {
		// Log.e(TAG, "PropManager : context : "+context.toString());
		mContext = context;
	}

	public String deviceProp() {
		try {
			Scanner scanner = new Scanner(propReader()).useDelimiter("\\n");
			scanner.findWithinHorizon(Pattern.compile("\\[ro.product.model\\].*\\[(.+?)\\]"), 0);
			return scanner.match().group(1);
		} catch (Exception e) {
			Log.w("Exception", "error in method deviceProp()");
			return mContext.getString(R.string.none);
		}
	}

	public String displayIdProp() {
		try {
			Scanner scanner = new Scanner(propReader()).useDelimiter("\\n");
			scanner.findWithinHorizon(Pattern.compile("\\[ro.build.display.id\\].*\\[(.+?)\\]"), 0);
			return scanner.match().group(1);
		} catch (Exception e) {
			Log.w("Exception", "error in displayIdProp()");
			return mContext.getString(R.string.nand);
		}
	}

	public String kernelProp() {
		return System.getProperty("os.name") + " " + System.getProperty("os.version");
	}

	public String mbActivePath() {
		try {
			Scanner scanner = new Scanner(propReader()).useDelimiter("\\n");
			scanner.findWithinHorizon(Pattern.compile("\\[ro.multiboot.vs\\].*\\[(.+?)\\]"), 0);
			return scanner.match().group(1);
		} catch (Exception e) {
			Log.w("Exception", "error in mbActivePath()");
			return "";
		}
	}

	public String multiBootDefaultFolderProp() {
		try {
			Scanner scanner = new Scanner(propReader()).useDelimiter("\\n");
			scanner.findWithinHorizon(Pattern.compile("\\[ro.multiboot.path\\].*\\[(.+?)\\]"), 0);
			return scanner.match().group(1);
		} catch (Exception e) {
			Log.w("Exception", "error in multiBootDefaultFolderProp()");
			return mContext.getString(R.string.none);
		}
	}

	public String multibootPartitionProp(String propval) {
		try {
			Scanner scanner = new Scanner(propReader()).useDelimiter("\\n");
			scanner.findWithinHorizon(Pattern.compile("\\[ro.multiboot.partition\\].*\\[(.+?)\\]"), 0);
			return scanner.match().group(1);
		} catch (Exception e) {
			Log.w("Exception", "error in multibootPartitionProp(String)");
			return mContext.getString(R.string.none);
		}
	}

	public String multiBootProp() {
		try {
			Scanner scanner = new Scanner(propReader()).useDelimiter("\\n");
			scanner.findWithinHorizon(Pattern.compile("\\[ro.multiboot\\].*\\[(.+?)\\]"), 0);
			return scanner.match().group(1);
		} catch (Exception e) {
			Log.w("Exception", "error in multiBootProp()");
			return "0";
		}
	}

	public String multiBootVSPathProp() {
		try {
			Scanner scanner = new Scanner(propReader()).useDelimiter("\\n");
			scanner.useDelimiter("\\n").findWithinHorizon(Pattern.compile("\\[ro.multiboot.vs\\].*\\[(.+?)\\]"), 0);
			return scanner.match().group(1);
		} catch (Exception e) {
			Log.w("Exception", "error in multiBootVSPathProp()");
			return mContext.getString(R.string.none);
		}
	}

	public String multiBootVSPathPropO() {
		try {
			Scanner scanner = new Scanner(propReader()).useDelimiter("\\n");
			scanner.findWithinHorizon(Pattern.compile("\\[ro.multiboot.vs\\].*\\[(.+?)\\]"), 0);
			return scanner.match().group(1);
		} catch (Exception e) {
			Log.w("Exception", "error in multiBootVSPathPropO()");
			return mContext.getString(R.string.none);
		}
	}

	public MatrixCursor propCursor() {
		MatrixCursor propcursor = new MatrixCursor(new String[] { BaseColumns._ID, "name", "value" });
		propcursor.addRow(new Object[] { 1, mContext.getString(R.string.currsys), vSNameProp() });
		propcursor.addRow(new Object[] { 2, mContext.getString(R.string.currrom), displayIdProp() });
		propcursor.addRow(new Object[] { 3, mContext.getString(R.string.currkernel), kernelProp() });
		propcursor.addRow(new Object[] { 4, mContext.getString(R.string.currpath), multiBootVSPathProp() });
		propcursor.addRow(new Object[] { 5, mContext.getString(R.string.device), deviceProp() });
		return propcursor;
	}

	public String propReader() {
		try {
			return new Scanner(Runtime.getRuntime().exec("/system/bin/getprop").getInputStream()).useDelimiter("\\A")
					.next();
		} catch (NoSuchElementException e) {
			Log.w("NoSuchElementException", "error in propReader()");
			return Constants.EMPTY;
		} catch (IOException e) {
			Log.w("IOException", "error in propReader()");
			return Constants.EMPTY;
		}
	}

	public String vSNameProp() {
		try {
			Scanner scanner = new Scanner(propReader()).useDelimiter("\\n");
			scanner.findWithinHorizon(Pattern.compile("\\[ro.multiboot.vs\\].*\\[(.+?)\\]"), 0);
			return "Multiboot (" + (new File(scanner.match().group(1))).getName() + ")";
		} catch (Exception e) {
			Log.w("Exception", "error in vSNameProp()");
			return mContext.getString(R.string.nand);
		}
	}

}
