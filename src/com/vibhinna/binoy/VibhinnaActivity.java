package com.vibhinna.binoy;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.Button;

import com.actionbarsherlock.app.SherlockFragmentActivity;

public class VibhinnaActivity extends SherlockFragmentActivity {

	private PropManager propManager;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_fragment);
		propManager = new PropManager(this);
		final Button mButton = (Button) findViewById(R.id.button);
		if (Integer.parseInt(propManager.multiBootProp()) != 1) {
			mButton.setText(getString(R.string.mbnactive));
		} else {
			mButton.setText(getString(R.string.mbactive));
		}
	}

	public void onButtonClicked(View v) {
		showPropDialog();
	}

	private void showPropDialog() {
		// Create and show the dialog.
		DialogFragment newFragment = PropDialogFragment.newInstance();
		newFragment.show(getSupportFragmentManager(), "dialog");
	}
}