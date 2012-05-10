package com.vibhinna.binoy;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;

import android.app.Dialog;
import android.os.Bundle;

public class VibhinnaActivity extends SherlockFragmentActivity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		ActionBar actionBar = getSupportActionBar();
//		actionBar.setDisplayShowTitleEnabled(false);
//		actionBar.setDisplayUseLogoEnabled(true);
		setContentView(R.layout.main_fragment);
	}

	protected Dialog mSplashDialog;

	protected void showSplashScreen() {
		mSplashDialog = new Dialog(this, R.style.SplashScreen);
		mSplashDialog.setContentView(R.layout.splashscreen);
		mSplashDialog.setCancelable(false);
		mSplashDialog.show();
	}

	protected void removeSplashScreen() {
		if (mSplashDialog != null) {
			mSplashDialog.dismiss();
			mSplashDialog = null;
		}
	}
}