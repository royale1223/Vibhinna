package com.vibhinna.binoy;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

public class VibhinnaFragment extends SherlockListFragment implements
		LoaderManager.LoaderCallbacks<Cursor> {
	private static final int VFS_LIST_LOADER = 0x01;
	protected static final String TAG = "com.vibhinna.binoy.VibhinnaFragment";
	private VibhinnaAdapter adapter;
	protected boolean cacheCheckBool = false;
	protected boolean dataCheckBool = false;
	protected boolean systemCheckBool = false;
	int iconid = 1;
	private ContentResolver resolver;

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setRetainInstance(true);
		registerForContextMenu(getListView());
		if (!Constants.BINARY_FOLDER.exists()
				|| Constants.BINARY_FOLDER.list().length < 3) {
			Constants.BINARY_FOLDER.mkdirs();
			AssetsManager assetsManager = new AssetsManager(getActivity());
			assetsManager.copyAssets();
		}
		resolver = getActivity().getContentResolver();
		setHasOptionsMenu(true);
		startLoading();
	}

	protected void startLoading() {
		setListShown(false);
		adapter.notifyDataSetChanged();
		getListView().invalidateViews();
		LoaderManager lm = getLoaderManager();
		lm.initLoader(VFS_LIST_LOADER, null, this);
	}

	protected void restartLoading() {
		setListShown(false);
		getLoaderManager().restartLoader(VFS_LIST_LOADER, null, this);
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
		adapter.swapCursor(cursor);
		setListShown(true);
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		adapter.swapCursor(null);
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		showDetailsDialog(id);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setHasOptionsMenu(true);

		String[] from = { "name", "desc", "status", "path", "folder",
				BaseColumns._ID };
		int[] to = { R.id.name, R.id.desc, R.id.status, R.id.path };

		adapter = new VibhinnaAdapter(getActivity(), R.layout.main_row, null,
				from, to, SimpleCursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
		setListAdapter(adapter);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenu.ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		AdapterView.AdapterContextMenuInfo info;
		PropManager propmanager = new PropManager(this.getActivity()
				.getApplicationContext());
		try {
			// Casts the incoming data object into the type for AdapterView
			// objects.
			info = (AdapterView.AdapterContextMenuInfo) menuInfo;
		} catch (ClassCastException e) {
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
		android.view.MenuInflater inflater = this.getActivity()
				.getMenuInflater();
		inflater.inflate(R.menu.context_menu, menu);
	}

	@Override
	public boolean onContextItemSelected(android.view.MenuItem item) {
		final Context context = getActivity();
		final ContentResolver mContentResolver = getActivity()
				.getContentResolver();
		AdapterContextMenuInfo menuInfo = (AdapterContextMenuInfo) item
				.getMenuInfo();
		// final DataSource datasource = new DataSource(this);
		final Cursor item_cursor = (Cursor) getListAdapter().getItem(
				menuInfo.position);
		if (item_cursor == null) {
			// For some reason the requested item isn't available, do nothing
			return false;
		}
		final String initialFilePath = item_cursor.getString(7);
		final String foldername = item_cursor.getString(1);
		final String folderdesc = item_cursor.getString(2);
		iconid = Integer.parseInt(item_cursor.getString(4));
		final int itemid = Integer.parseInt(item_cursor.getString(0));
		switch (item.getItemId()) {
		case R.id.edit:
			LayoutInflater factory = LayoutInflater.from(context);
			final View editVSView = factory.inflate(R.layout.edit_vs_layout,
					null);
			final EditText nameEditText = (EditText) editVSView
					.findViewById(R.id.vsname);
			final EditText descriptionEditText = (EditText) editVSView
					.findViewById(R.id.vsdesc);
			final Spinner iconSelectSpinner = (Spinner) editVSView
					.findViewById(R.id.spinner);
			final ImageView iconPreview = (ImageView) editVSView
					.findViewById(R.id.seticonimage);
			ArrayAdapter<CharSequence> iconSelectSpinnerAdapter = ArrayAdapter
					.createFromResource(context, R.array.icon_array,
							android.R.layout.simple_spinner_item);
			iconSelectSpinnerAdapter
					.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			iconSelectSpinner.setAdapter(iconSelectSpinnerAdapter);
			iconSelectSpinner.setSelection(iconid);
			iconSelectSpinner.setAdapter(iconSelectSpinnerAdapter);
			iconSelectSpinner.setSelection(iconid);
			iconSelectSpinner
					.setOnItemSelectedListener(new OnItemSelectedListener() {
						@Override
						public void onItemSelected(AdapterView<?> arg0,
								View arg1, int arg2, long arg3) {
							iconid = arg2;
							iconPreview.setImageResource(MiscMethods
									.getIconRes(arg2));
						}

						@Override
						public void onNothingSelected(AdapterView<?> arg0) {

						}
					});
			descriptionEditText.setText(folderdesc);
			nameEditText.setText(foldername);
			new AlertDialog.Builder(context)
					.setTitle(
							getString(R.string.edits)
									+ item_cursor.getString(1))
					.setView(editVSView)
					.setPositiveButton(getString(R.string.okay),
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int whichButton) {
									String updatedName = foldername;
									try {
										updatedName = nameEditText.getText()
												.toString();
									} catch (Exception e) {
										e.printStackTrace();
									}
									String updatedDescription = folderdesc;
									try {
										updatedDescription = descriptionEditText
												.getText().toString();
									} catch (Exception e) {
										e.printStackTrace();
									}
									File finalFile = new File(
											"/mnt/sdcard/multiboot/"
													+ updatedName);
									// get new name if already taken
									if (!(new File(initialFilePath))
											.equals(finalFile)) {
										finalFile = MiscMethods
												.avoidDuplicateFile(finalFile);
										(new File(initialFilePath))
												.renameTo(finalFile);
									}
									ContentValues values = new ContentValues();
									values.put(
											DatabaseHelper.VIRTUAL_SYSTEM_COLUMN_NAME,
											finalFile.getName());
									values.put(
											DatabaseHelper.VIRTUAL_SYSTEM_COLUMN_PATH,
											finalFile.getPath());
									values.put(
											DatabaseHelper.VIRTUAL_SYSTEM_COLUMN_DESCRIPTION,
											updatedDescription);
									values.put(
											DatabaseHelper.VIRTUAL_SYSTEM_COLUMN_TYPE,
											iconid);
									mContentResolver.update(
											Uri.parse("content://"
													+ VibhinnaProvider.AUTHORITY
													+ "/"
													+ VibhinnaProvider.VFS_BASE_PATH
													+ "/" + itemid), values,
											null, null);
									iconid = 1;
									restartLoading();
								}
							})
					.setNegativeButton(getString(R.string.cancel),
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int whichButton) {
									// Canceled.
								}
							}).show();
			return true;
		case R.id.delete:
			new AlertDialog.Builder(context)
					.setTitle(
							getString(R.string.delete)
									+ item_cursor.getString(1))
					.setMessage(getString(R.string.rusure))
					.setPositiveButton(getString(R.string.okay),
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(
										DialogInterface dialogInterface, int i) {
									try {
										MiscMethods.removeDirectory(new File(
												initialFilePath));
										restartLoading();
									} catch (Exception e) {
										e.printStackTrace();
										return;
									}
								}
							})
					.setNeutralButton(getString(R.string.cancel),
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(
										DialogInterface dialogInterface, int i) {
								}
							}).create().show();
			return true;
		case R.id.format:
			showFormatDialog(this, itemid);
			return true;
		default:
			return super.onContextItemSelected(item);
		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.options_menu, menu);
		super.onCreateOptionsMenu(menu, inflater);
		Intent prefsIntent = new Intent(getActivity().getApplicationContext(),
				Preferences.class);

		MenuItem preferences = menu.findItem(R.id.menu_settings);
		preferences.setIntent(prefsIntent);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_refresh:
			restartLoading();
			return true;
		case R.id.menu_new:
			showNewVFSDialog(this);
			return true;
		case R.id.menu_settings:
			// getActivity().startActivity(item.getIntent());
			Toast.makeText(getActivity(), "This is a stub!! Move on.",
					Toast.LENGTH_SHORT);
			return true;
		case R.id.menu_scan:
			resolver.query(
					Uri.parse("content://" + VibhinnaProvider.AUTHORITY + "/"
							+ VibhinnaProvider.VFS_BASE_PATH + "/scan"), null,
					null, null, null);
			restartLoading();
			return true;
		case R.id.menu_backup:
			resolver.query(
					Uri.parse("content://" + VibhinnaProvider.AUTHORITY + "/"
							+ VibhinnaProvider.VFS_BASE_PATH + "/write_xml"),
					null, null, null, null);
			restartLoading();
			return true;
		case R.id.menu_restore:
			resolver.query(
					Uri.parse("content://" + VibhinnaProvider.AUTHORITY + "/"
							+ VibhinnaProvider.VFS_BASE_PATH + "/read_xml"),
					null, null, null, null);
			restartLoading();
			return true;
		case R.id.menu_help:
			Toast.makeText(getActivity(), "This is a stub!! Move on.",
					Toast.LENGTH_SHORT);
			return true;
		case R.id.menu_about:
			Toast.makeText(getActivity(), "This is a stub!! Move on.",
					Toast.LENGTH_SHORT);
			return true;
		case R.id.menu_license:
			Toast.makeText(getActivity(), "This is a stub!! Move on.",
					Toast.LENGTH_SHORT);
			return true;
		default:
			return false;
		}
	}

	/**
	 * Shows the Details Dialog, which gives all info about a VFS.
	 * 
	 * @param id
	 *            _id of the VFS
	 */
	void showDetailsDialog(long id) {
		DialogFragment newFragment = DetailsDialogFragment.newInstance(id);
		newFragment.show(getFragmentManager(), "details_dialog");
	}

	/**
	 * Shows New VFS dialog according to API, which will create a new VFS
	 * 
	 * @param vibhinnaFragment
	 */
	private void showNewVFSDialog(VibhinnaFragment vibhinnaFragment) {
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB) {
			NewDialogFragment.newInstance(vibhinnaFragment).show(
					getFragmentManager(), "new_dialog");
		} else {
			NewDialogFragmentOld.newInstance(vibhinnaFragment).show(
					getFragmentManager(), "new_dialog");
		}
	}

	private void showFormatDialog(VibhinnaFragment vibhinnaFragment, long id) {
		FormatDialogFragment.newInstance(vibhinnaFragment, id).show(
				getFragmentManager(), "format_dialog");
	}
}
