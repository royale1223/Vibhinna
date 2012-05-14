package com.vibhinna.binoy;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.actionbarsherlock.app.SherlockDialogFragment;

public class PropDialogFragment extends SherlockDialogFragment {
	PropManager propManager;

	/** creates a new instance of PropDialogFragment */
	static PropDialogFragment newInstance() {
		PropDialogFragment f = new PropDialogFragment();
		return f;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		getActivity();
		LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.button_dialog, null);
		propManager = new PropManager(getSherlockActivity());
		AlertDialog.Builder builder = new AlertDialog.Builder(getSherlockActivity());
		builder.setView(view);
		ListView mbListView = (ListView) view.findViewById(R.id.mblist);
		builder.setTitle(getActivity().getString(R.string.sysinfo)).setNeutralButton(
				getActivity().getString(R.string.okay), null);
		MatrixCursor dcursor = propManager.propCursor();
		if (dcursor.moveToFirst()) {
			String[] from = { Constants.NAME_DET, Constants.VALUE_DET };
			int[] to = { R.id.mbname, R.id.mbvalue };
			SimpleCursorAdapter dadapter = new SimpleCursorAdapter(getSherlockActivity(), R.layout.info_list_row,
					dcursor, from, to);
			mbListView.setAdapter(dadapter);
		}
		return builder.create();
	}
}
