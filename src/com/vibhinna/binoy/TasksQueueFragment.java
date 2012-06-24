package com.vibhinna.binoy;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SimpleCursorAdapter;

import com.actionbarsherlock.app.SherlockListFragment;

public class TasksQueueFragment extends SherlockListFragment implements
		LoaderManager.LoaderCallbacks<Cursor> {
	private static final int TASK_LIST_LOADER = 0x02;
	protected ContentResolver mResolver;
	protected TasksAdapter adapter;
	protected LoaderManager mLoaderManager;
	private LocalBroadcastManager mLocalBroadcastManager;
	private BroadcastReceiver mBroadcastReceiver;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setRetainInstance(true);

		mLocalBroadcastManager = LocalBroadcastManager
				.getInstance(getSherlockActivity());
		mBroadcastReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				if (intent.getAction().equals(
						VibhinnaService.ACTION_TASK_QUEUE_UPDATED)) {
					restartLoading();
				}
			}
		};

		mResolver = getActivity().getContentResolver();
		mLoaderManager = getLoaderManager();
		setHasOptionsMenu(true);
		startLoading();
	}

	private void startLoading() {
		adapter.notifyDataSetChanged();
		getListView().invalidateViews();
		mLoaderManager.initLoader(TASK_LIST_LOADER, null, this);
	}

	protected void restartLoading() {
		mLoaderManager.restartLoader(TASK_LIST_LOADER, null, this);
	}

	@Override
	public Loader<Cursor> onCreateLoader(int arg0, Bundle arg1) {
		CursorLoader cursorLoader = new CursorLoader(getActivity(),
				TasksProvider.CONTENT_URI, null, null, null, null);
		return cursorLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> arg0, Cursor arg1) {
		adapter.swapCursor(arg1);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		adapter.swapCursor(null);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		adapter = new TasksAdapter(getActivity(), R.layout.main_row, null,
				new String[] {}, new int[] {}, SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
		setListAdapter(adapter);
	}

	@Override
	public void onResume() {
		super.onResume();
		IntentFilter filter = new IntentFilter();
		filter.addAction(VibhinnaService.ACTION_TASK_QUEUE_UPDATED);
		mLocalBroadcastManager.registerReceiver(mBroadcastReceiver, filter);
	}
}
