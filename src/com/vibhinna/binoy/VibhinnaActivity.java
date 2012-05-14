package com.vibhinna.binoy;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
	    // DialogFragment.show() will take care of adding the fragment
	    // in a transaction.  We also want to remove any currently showing
	    // dialog, so make our own transaction and take care of that here.
	    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
	    Fragment prev = getSupportFragmentManager().findFragmentByTag("dialog");
	    if (prev != null) {
	        ft.remove(prev);
	    }
	    ft.addToBackStack(null);
	    
	    // Create and show the dialog.
	    DialogFragment newFragment = PropDialogFragment.newInstance();
	    newFragment.show(ft, "dialog");
	}
}