package com.vibhinna.binoy;

import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;

import com.actionbarsherlock.app.SherlockListFragment;

public class SystemInfoFragment extends SherlockListFragment {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setHasOptionsMenu(true);

		String[] from = { "name", "value" };
		int[] to = { R.id.system_info_name, R.id.system_info_value };

		SimpleCursorAdapter adapter = new SimpleCursorAdapter(getActivity(),
				R.layout.system_info_row,
				new PropManager(getActivity()).propCursor(), from, to,
				SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
		setListAdapter(adapter);
	}
}
