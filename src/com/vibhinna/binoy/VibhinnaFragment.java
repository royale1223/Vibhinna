package com.vibhinna.binoy;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Loader;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.database.Cursor;
import android.widget.CursorAdapter;
import android.widget.SimpleCursorAdapter;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.widget.AdapterView;


public class VibhinnaFragment extends ListFragment implements
		LoaderManager.LoaderCallbacks<Cursor> {
	private static final int TUTORIAL_LIST_LOADER = 0x01;
	private static final String TAG = null;
	private VibhinnaAdapter adapter;

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		// show toast for now
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// use crsorloader in fragment
		super.onCreate(savedInstanceState);
		String[] from = { "name", "desc", "status", "path", "folder",
				BaseColumns._ID };
		int[] to = { R.id.name, R.id.desc, R.id.status, R.id.path };

		getLoaderManager().initLoader(TUTORIAL_LIST_LOADER, null, this);

		adapter = new VibhinnaAdapter (
				getActivity().getApplicationContext(), R.layout.main_row, null,
				from, to, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
		setListAdapter(adapter);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState){
		super.onActivityCreated(savedInstanceState);
		registerForContextMenu(getListView());
	}
	
	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		CursorLoader cursorLoader = new CursorLoader(getActivity(),
				VibhinnaProvider.LIST_DISPLAY_URI, Constants.allColumns, null,
				null, null);
		return cursorLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
		String[] a = cursor.getColumnNames();
		cursor.moveToFirst();
		do {
			for (int i = 0; i < a.length; i++) {
				Log.d(TAG, "Column " + i + " : " + a[i]+ " = "+cursor.getString(i));
			}
		} while (cursor.moveToNext());
		adapter.swapCursor(cursor);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		adapter.swapCursor(null);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
									ContextMenu.ContextMenuInfo menuInfo)
	{
		super.onCreateContextMenu(menu, v, menuInfo);
		AdapterView.AdapterContextMenuInfo info;
		PropManager propmanager = new PropManager(this.getActivity().getApplicationContext());
		try
		{
			// Casts the incoming data object into the type for AdapterView
			// objects.
			info = (AdapterView.AdapterContextMenuInfo) menuInfo;
		}
		catch (ClassCastException e)
		{
			// If the menu object can't be cast, logs an error.
			Log.w("Exception", "exception in getting menuinfo");
			return;
		}
		Cursor cursor = (Cursor) getListAdapter().getItem(info.position);
		String s1 = Constants.SD_PATH + propmanager.mbActivePath();
		String s2 = cursor.getString(7);
		if (cursor.equals(null) || s1.equals(s2))
			return;
		menu.setHeaderTitle(cursor.getString(1));
		MenuInflater inflater = this.getActivity().getMenuInflater();
		inflater.inflate(R.menu.context_menu, menu);
	}
}
