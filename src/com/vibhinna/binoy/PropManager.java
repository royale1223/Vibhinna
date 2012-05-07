package com.vibhinna.binoy;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Pattern;

import android.content.Context;
import android.database.MatrixCursor;
import android.provider.BaseColumns;
import android.util.Log;

public class PropManager {
	private Context mContext;

	public PropManager(Context context) {
		mContext = context;
	}

	public String deviceProp() {
		String mbvsprop = mContext.getString(R.string.none);
		try {
			Scanner scanner = new Scanner(propReader()).useDelimiter("\\n");
			scanner.findWithinHorizon(Pattern.compile("\\[ro.product.model\\].*\\[(.+?)\\]"), 0);
			mbvsprop = scanner.match().group(1);
		} catch (Exception e) {
			Log.w("Exception", "error in method deviceProp()");
		}
		return mbvsprop;
	}

	public String displayIdProp() {
		String mbvsprop = mContext.getString(R.string.nand);
		try {
			Scanner scanner = new Scanner(propReader()).useDelimiter("\\n");
			scanner.findWithinHorizon(Pattern.compile("\\[ro.build.display.id\\].*\\[(.+?)\\]"), 0);
			mbvsprop = scanner.match().group(1);
		} catch (Exception e) {
			Log.w("Exception", "error in displayIdProp()");
		}
		return mbvsprop;
	}

	public String kernelProp() {
		return System.getProperty("os.name") + " " + System.getProperty("os.version");
	}

	public String mbActivePath() {
		String mbvsprop = "";
		try {
			Scanner scanner = new Scanner(propReader()).useDelimiter("\\n");
			scanner.findWithinHorizon(Pattern.compile("\\[ro.multiboot.vs\\].*\\[(.+?)\\]"), 0);
			mbvsprop = scanner.match().group(1);
		} catch (Exception e) {
			Log.w("Exception", "error in mbActivePath()");
		}
		return mbvsprop;
	}

	public String multiBootDefaultFolderProp() {
		String mbpathprop = mContext.getString(R.string.none);
		try {
			Scanner scanner = new Scanner(propReader()).useDelimiter("\\n");
			scanner.findWithinHorizon(Pattern.compile("\\[ro.multiboot.path\\].*\\[(.+?)\\]"), 0);
			mbpathprop = scanner.match().group(1);
		} catch (Exception e) {
			Log.w("Exception", "error in multiBootDefaultFolderProp()");
		}
		return mbpathprop;
	}

	public String multibootPartitionProp(String propval) {
		String mbpartprop = mContext.getString(R.string.none);
		try {
			Scanner scanner = new Scanner(propReader()).useDelimiter("\\n");
			scanner.findWithinHorizon(Pattern.compile("\\[ro.multiboot.partition\\].*\\[(.+?)\\]"), 0);
			mbpartprop = scanner.match().group(1);
		} catch (Exception e) {
			Log.w("Exception", "error in multibootPartitionProp(String)");
		}
		return mbpartprop;
	}

	public String multiBootProp() {
		String mbprop = "0";
		try {
			Scanner scanner = new Scanner(propReader()).useDelimiter("\\n");
			scanner.findWithinHorizon(Pattern.compile("\\[ro.multiboot\\].*\\[(.+?)\\]"), 0);
			mbprop = scanner.match().group(1);
		} catch (Exception e) {
			Log.w("Exception", "error in multiBootProp()");
		}
		return mbprop;
	}

	public String multiBootVSPathProp() {
		String mbvsprop = null;
		try {
			Scanner scanner = new Scanner(propReader()).useDelimiter("\\n");
			scanner.findWithinHorizon(Pattern.compile("\\[ro.multiboot.vs\\].*\\[(.+?)\\]"), 0);
			mbvsprop = scanner.match().group(1);
		} catch (Exception e) {
			Log.w("Exception", "error in multiBootVSPathProp()");
		}
		if (mbvsprop == null) {
			return mContext.getString(R.string.none);
		} else
			return mbvsprop;
	}

	public String multiBootVSPathPropO() {
		String mbvsprop = mContext.getString(R.string.none);
		try {
			Scanner scanner = new Scanner(propReader()).useDelimiter("\\n");
			scanner.findWithinHorizon(Pattern.compile("\\[ro.multiboot.vs\\].*\\[(.+?)\\]"), 0);
			mbvsprop = scanner.match().group(1);
		} catch (Exception e) {
			Log.w("Exception", "error in multiBootVSPathPropO()");
		}
		return mbvsprop;
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
		InputStream inputstream = null;
		try {
			inputstream = Runtime.getRuntime().exec("/system/bin/getprop").getInputStream();
		} catch (IOException e) {
			Log.w("IOException", "error in propReader()");
		}
		String propval = "";
		try {

			propval = new Scanner(inputstream).useDelimiter("\\A").next();

		} catch (NoSuchElementException e) {
			Log.w("NoSuchElementException", "error in propReader()");
		}
		return propval;
	}

	public String vSNameProp() {
		String mbvsprop = mContext.getString(R.string.nand);
		try {
			Scanner scanner = new Scanner(propReader()).useDelimiter("\\n");
			scanner.findWithinHorizon(Pattern.compile("\\[ro.multiboot.vs\\].*\\[(.+?)\\]"), 0);
			mbvsprop = scanner.match().group(1);
			File vs = new File(mbvsprop);
			return "Multiboot (" + vs.getName() + ")";
		} catch (Exception e) {
			Log.w("Exception", "error in vSNameProp()");
		}
		return mbvsprop;
	}

}
