package com.vibhinna.binoy;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;

import com.actionbarsherlock.app.SherlockDialogFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;

public class FormatDialogFragment extends SherlockDialogFragment {
	private static VibhinnaFragment mVibFragment;
	private static SherlockFragmentActivity mContext;
	private static ContentResolver mResolver;

	private static Cursor mCursor;
	private static String mPath;
	private static String mName;

	protected boolean cacheCheckBool = false;
	protected boolean dataCheckBool = false;
	protected boolean systemCheckBool = false;

	static FormatDialogFragment newInstance(VibhinnaFragment vibhinnaFragment,
			long id) {
		FormatDialogFragment fragment = new FormatDialogFragment();
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
		return fragment;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		LayoutInflater factory = LayoutInflater.from(mContext);
		final View formatView = factory.inflate(R.layout.format_dialog, null);

		final AlertDialog dialog = new AlertDialog.Builder(mContext)
				.setTitle(mContext.getString(R.string.format) + mName + "?")
				.setView(formatView)
				.setPositiveButton(mContext.getString(R.string.okay),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								final ProgressDialog processdialog = ProgressDialog
										.show(mContext, Constants.EMPTY,
												Constants.EMPTY, true);
								final Handler handler = new Handler() {

									@Override
									public void handleMessage(Message msg) {
										switch (msg.arg1) {
										case 0:
											processdialog.setMessage(mContext
													.getString(R.string.formating)
													+ mPath
													+ mContext
															.getString(R.string.cachext3));
											break;
										case 1:
											processdialog.setMessage(mContext
													.getString(R.string.formating)
													+ mPath
													+ mContext
															.getString(R.string.dataext3));
											break;
										case 2:
											processdialog.setMessage(mContext
													.getString(R.string.formating)
													+ mPath
													+ mContext
															.getString(R.string.systemext3));
											break;
										case 3:
											processdialog.dismiss();
											break;
										}
									}
								};

								class FormatVFSTask extends
										AsyncTask<Void, Void, Void> {

									@Override
									protected Void doInBackground(
											Void... params) {
										String[] shellinput = {
												Constants.EMPTY,
												Constants.EMPTY,
												Constants.EMPTY,
												Constants.EMPTY,
												Constants.EMPTY };
										shellinput[0] = Constants.CMD_MKE2FS_EXT3;
										shellinput[1] = mPath;
										final Message m0 = new Message();
										final Message m1 = new Message();
										final Message m2 = new Message();
										final Message endmessage = new Message();
										m0.arg1 = 0;
										m1.arg1 = 1;
										m2.arg1 = 2;
										endmessage.arg1 = 3;
										if (cacheCheckBool) {
											handler.sendMessage(m0);
											shellinput[2] = Constants.CACHE_IMG;
											ProcessManager.inputStreamReader(
													shellinput, 20);
											cacheCheckBool = false;
										}
										if (dataCheckBool) {
											handler.sendMessage(m1);
											shellinput[2] = Constants.DATA_IMG;
											ProcessManager.inputStreamReader(
													shellinput, 20);
											dataCheckBool = false;
										}
										if (systemCheckBool) {
											handler.sendMessage(m2);
											shellinput[2] = Constants.SYSTEM_IMG;
											ProcessManager.inputStreamReader(
													shellinput, 20);
											systemCheckBool = false;
										}
										handler.sendMessage(endmessage);
										return null;
									}
								}
								new FormatVFSTask().execute();
							}
						})
				.setNeutralButton(mContext.getString(R.string.cancel),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(
									DialogInterface dialogInterface, int i) {
							}
						}).show();
		CheckBox chkCache = (CheckBox) formatView.findViewById(R.id.cache);
		CheckBox chkData = (CheckBox) formatView.findViewById(R.id.data);
		CheckBox chkSystem = (CheckBox) formatView.findViewById(R.id.system);
		chkCache.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (((CheckBox) v).isChecked()) {
					cacheCheckBool = true;
				} else {
					cacheCheckBool = false;
				}
			}
		});
		chkData.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (((CheckBox) v).isChecked()) {
					dataCheckBool = true;
				} else {
					dataCheckBool = false;
				}
			}
		});
		chkSystem.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (((CheckBox) v).isChecked()) {
					systemCheckBool = true;
				} else {
					systemCheckBool = false;
				}
			}
		});
		return dialog;
	}
}
