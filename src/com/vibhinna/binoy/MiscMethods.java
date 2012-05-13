package com.vibhinna.binoy;

import android.graphics.*;
import android.os.*;
import java.io.*;

public class MiscMethods {

public class MiscMethods {
	/**
	 * To determine color of total mem in "new" dialog
	 * 
	 * @param c
	 *            Size of cache
	 * @param d
	 *            Size of data
	 * @param s
	 *            Size of system
	 * @return color of mem
	 */
	public static int getMemColor(int c, int d, int s) {
		int SD_FREE_SIZE = sdFreeSize();
		int SD_WARN_SIZE = (int) (SD_FREE_SIZE * 0.9);
		int TOTAL_SIZE = getTotalSize(c, d, s);
		if (TOTAL_SIZE > SD_FREE_SIZE)
			return Color.RED;
		else if (TOTAL_SIZE > SD_WARN_SIZE)
			return Color.parseColor("#FFA500");
		else
			return Color.parseColor("#4B8A08");
	}

	/**
	 * 
	 * @return Free size in SDcard
	 */
	private static int sdFreeSize() {
		StatFs stat = new StatFs(Environment.getExternalStorageDirectory().getPath());
		long bytesAvailable = (long) stat.getBlockSize() * (long) stat.getAvailableBlocks();
		int sdFreeMegs = (int) (bytesAvailable / (1024 * 1024));
		return sdFreeMegs;
	}

	public static int getTotalSize(int c, int d, int s) {
		return c + d + s;
	}

	public static int getIcon(int iconid) {
		switch (iconid) {
		case 0:
			return R.drawable.a2s_icon;
		case 1:
			return R.drawable.andro_icon;
		case 2:
			return R.drawable.cm7_icon;
		case 3:
			return R.drawable.cm9_icon;
		case 4:
			return R.drawable.froyo_icon;
		case 5:
			return R.drawable.gb_icon;
		case 6:
			return R.drawable.miui_icon;
		case 7:
			return R.drawable.moto_icon;
		case 8:
			return R.drawable.pikachu_icon;
		case 9:
			return R.drawable.whiter_icon;
		case 10:
			return R.drawable.wiui_icon;
		case 11:
			return R.drawable.akop_icon;
		case 12:
			return R.drawable.cna_icon;
		case 13:
			return R.drawable.miui4_icon;
		case 14:
			return R.drawable.cherry_icon;
		case 15:
			return R.drawable.eppy_icon;
		case 16:
			return R.drawable.quarx_icon;
		case 17:
			return R.drawable.cid_icon;
		case 18:
			return R.drawable.a3s_icon;
		case 19:
			return R.drawable.dfs_icon;
		case 20:
			return R.drawable.dx_icon;
		case 21:
			return R.drawable.speed_icon;
		case 22:
			return R.drawable.bone_icon;
		default:
			return R.drawable.andro_icon;
		}
	}

	/**
	 * Remove a directory and all of its contents.
	 * 
	 * The results of executing File.delete() on a File object that represents a
	 * directory seems to be platform dependent. This method removes the
	 * directory and all of its contents.
	 * 
	 * @return true if the complete directory was removed, false if it could not
	 *         be. If false is returned then some of the files in the directory
	 *         may have been removed.
	 */
	public static boolean removeDirectory(File directory) {
		if (directory == null)
			return false;
		if (!directory.exists())
			return true;
		if (!directory.isDirectory())
			return false;
		String[] list = directory.list();
		// Some JVMs return null for File.list() when the
		// directory is empty.
		if (list != null) {
			for (int i = 0; i < list.length; i++) {
				File entry = new File(directory, list[i]);
				if (entry.isDirectory()) {
					if (!removeDirectory(entry))
						return false;
				} else {
					if (!entry.delete())
						return false;
				}
			}
		}
		return directory.delete();
	}

	public static File avoidDuplicateFile(File finalFile) {
		String path = finalFile.getPath();
		if (new File(path).exists()) {
			for (int i = 1; i <= Constants.MBM_ROOT.list().length; i++) {
				if (!new File(path + "_" + i).exists()) {
					path = path + "_" + i;
					break;
				}
			}
		}
		return (new File(path));
	}
}
