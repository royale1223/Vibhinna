package com.vibhinna.binoy;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class AssetsManager {
	private static final String TAG = "com.vibhinna.binoy.AssetsManager";
	Context context;

	/**
	 * Constructer for AssetManager
	 * 
	 * @param mContext
	 *            Context
	 */
	public AssetsManager(Context mContext) {
		context = mContext;
	}

	/**
	 * Copies assets to /data/data/com.vibhinna.binoy/bin and sets permissions
	 */
	public void copyAssets() {
		Toast.makeText(context, "Copying binaries", 2000).show();
		String[] assetslist = null;
		try {
			assetslist = context.getAssets().list("bin");
		} catch (IOException e) {
			Toast.makeText(context, "Error copying binaries", 2000).show();
		}
		for (int i = 0; i < assetslist.length; i++) {
			try {
				InputStream in = context.getAssets().open("bin/" + assetslist[i]);
				FileOutputStream out = new FileOutputStream(Constants.BINARY_PATH + "/" + assetslist[i]);
				int read;
				byte[] buffer = new byte[4096];
				while ((read = in.read(buffer)) > 0) {
					out.write(buffer, 0, read);
				}
				out.close();
				in.close();
				File assetfile = new File(Constants.BINARY_PATH + "/" + assetslist[i]);
				assetfile.setReadable(true, false);
				assetfile.setExecutable(true, false);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				Log.w(TAG, "Error : File not found " + assetslist[i]);
			} catch (IOException e) {
				e.printStackTrace();
				Log.w(TAG, "IO Error " + assetslist[i]);
			}
		}
	}
}
