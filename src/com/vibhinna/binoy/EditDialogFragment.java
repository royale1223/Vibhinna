package com.vibhinna.binoy;

import java.io.File;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import com.actionbarsherlock.app.SherlockDialogFragment;

public class EditDialogFragment extends SherlockDialogFragment {
	private static VibhinnaFragment mVibFragment;
	private static Context mContext;
	private static ContentResolver mResolver;
	private static Cursor mCursor;

	private int iconid = 1;
	private static long _id;
	private static String mDesc;
	private static String mName;
	private static String mPath;

	static EditDialogFragment newInstance(VibhinnaFragment vibhinnaFragment,
			long id) {
		EditDialogFragment fragment = new EditDialogFragment();
		_id = id;
		mVibFragment = vibhinnaFragment;
		mContext = mVibFragment.getSherlockActivity();
		mResolver = mContext.getContentResolver();
		mCursor = mResolver.query(
				Uri.parse("content://" + VibhinnaProvider.AUTHORITY + "/"
						+ VibhinnaProvider.VFS_BASE_PATH + "/" + id), null,
				null, null, null);
		mCursor.moveToFirst();
		mName = mCursor.getString(mCursor
				.getColumnIndex(DatabaseHelper.VIRTUAL_SYSTEM_COLUMN_NAME));
		mPath = mCursor.getString(mCursor
				.getColumnIndex(DatabaseHelper.VIRTUAL_SYSTEM_COLUMN_PATH));
		mDesc = mCursor
				.getString(mCursor
						.getColumnIndex(DatabaseHelper.VIRTUAL_SYSTEM_COLUMN_DESCRIPTION));
		return fragment;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		super.onCreateDialog(savedInstanceState);
		LayoutInflater factory = LayoutInflater.from(mContext);
		iconid = mCursor.getInt(mCursor
				.getColumnIndex(DatabaseHelper.VIRTUAL_SYSTEM_COLUMN_TYPE));

		final View editVSView = factory.inflate(R.layout.edit_vs_layout, null);
		final EditText nameEditText = (EditText) editVSView
				.findViewById(R.id.vsname);
		final EditText descriptionEditText = (EditText) editVSView
				.findViewById(R.id.vsdesc);
		final Spinner iconSelectSpinner = (Spinner) editVSView
				.findViewById(R.id.spinner);
		final ImageView iconPreview = (ImageView) editVSView
				.findViewById(R.id.seticonimage);
		ArrayAdapter<CharSequence> iconSelectSpinnerAdapter = ArrayAdapter
				.createFromResource(mContext, R.array.icon_array,
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
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						iconid = arg2;
						iconPreview.setImageResource(MiscMethods
								.getIconRes(arg2));
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {

					}
				});
		descriptionEditText.setText(mDesc);
		nameEditText.setText(mName);
		AlertDialog.Builder builder;
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB)
			builder = new AlertDialog.Builder(mContext);
		else
			builder = new HoloAlertDialogBuilder(mContext);
		Dialog dialog = builder
				.setTitle(getString(R.string.edits) + mCursor.getString(1))
				.setView(editVSView)
				.setPositiveButton(getString(R.string.okay),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int whichButton) {

								String updatedName = mName;
								try {
									updatedName = nameEditText.getText()
											.toString();
								} catch (Exception e) {
									e.printStackTrace();
								}
								String updatedDescription = mDesc;
								try {
									updatedDescription = descriptionEditText
											.getText().toString();
								} catch (Exception e) {
									e.printStackTrace();
								}
								File finalFile = new File(
										"/mnt/sdcard/multiboot/" + updatedName);
								// get new name if already taken
								if (!(new File(mPath)).equals(finalFile)) {
									finalFile = MiscMethods
											.avoidDuplicateFile(finalFile);
									(new File(mPath)).renameTo(finalFile);
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
								mResolver.update(Uri.parse("content://"
										+ VibhinnaProvider.AUTHORITY + "/"
										+ VibhinnaProvider.VFS_BASE_PATH + "/"
										+ _id), values, null, null);
								iconid = 1;
								mVibFragment.restartLoading();
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
		return dialog;
	}
}