package com.vibhinna.binoy;

import com.actionbarsherlock.app.SherlockFragmentActivity;

import android.app.Dialog;
import android.os.Bundle;

public class VibhinnaActivity extends SherlockFragmentActivity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_fragment);
	}

	protected Dialog mSplashDialog;

}