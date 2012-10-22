package com.binoy.vibhinna;

import java.io.File;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;

import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.provider.BaseColumns;
import android.util.Log;
import com.binoy.vibhinna.R;

public class PropManager {
    public static final String TAG = "PropManager";
    private Context mContext;

    public PropManager(Context context) {
        mContext = context;
    }

    private String getDevice() {
        return propReader("ro.product.model", mContext.getString(R.string.none));
    }

    private String getROM() {
        return propReader("ro.build.display.id", mContext.getString(R.string.nand));
    }

    private String getKernel() {
        return System.getProperty("os.name") + " " + System.getProperty("os.version");
    }

    public String mbActivePath() {
        return propReader("ro.multiboot.vs", Constants.EMPTY);
    }

    public String vSNameProp() {
        String str = mbActivePath();
        if (!str.equals(Constants.EMPTY))
            return mContext.getString(R.string.multiboot_info, (new File(str)).getName());
        else
            return mContext.getString(R.string.nand);
    }

    public String multiBootProp() {
        return propReader("ro.multiboot", "0");
    }

    private String getMultiBootPath() {
        return propReader("ro.multiboot.vs", mContext.getString(R.string.none));
    }

    public Cursor propCursor() {
        MatrixCursor propcursor = new MatrixCursor(
                new String[] { BaseColumns._ID, "name", "value" });
        propcursor.addRow(new Object[] { 1, mContext.getString(R.string.current_system),
                vSNameProp() });
        propcursor.addRow(new Object[] { 2, mContext.getString(R.string.current_rom), getROM() });
        propcursor.addRow(new Object[] { 3, mContext.getString(R.string.current_kernel),
                getKernel() });
        propcursor.addRow(new Object[] { 4, mContext.getString(R.string.current_path),
                getMultiBootPath() });
        propcursor.addRow(new Object[] { 5, mContext.getString(R.string.device), getDevice() });
        return propcursor;
    }

    public String propReader(String prop, String defaultValue) {
        try {
            String str = new Scanner(Runtime.getRuntime().exec("/system/bin/getprop " + prop)
                    .getInputStream()).useDelimiter("\\A").next().replaceAll("\\s", "");
            if (str != Constants.EMPTY && str != null)
                return str;
            else
                return defaultValue;
        } catch (NoSuchElementException e) {
            Log.w("NoSuchElementException", "error in propReader()");
            return defaultValue;
        } catch (IOException e) {
            Log.w("IOException", "error in propReader()");
            return defaultValue;
        }
    }
}
