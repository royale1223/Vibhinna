package com.binoy.vibhinna;

import android.os.Bundle;

import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.binoy.vibhinna.R;

public class Preferences extends SherlockPreferenceActivity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
	}
}
