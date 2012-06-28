package com.binoy.vibhinna;

import java.io.File;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockDialogFragment;
import com.binoy.vibhinna.R;

public class NewDialogFragmentOld extends SherlockDialogFragment {

	private static final String TAG = "NewDialogFragmentOld";
	private static Context mContext;
	private int iconId;
	private int cacheSize;
	private int dataSize;
	private int systemSize;

	private boolean validName = true;
	private boolean validSize = false;

	private boolean nullCache;
	private boolean nullData;
	private boolean nullSystem;

	private String newvsdesc;
	private String newName;

	/**
	 * creates a new instance of NewDialogFragmentOld
	 * 
	 * @param lsitFragment
	 */
	static NewDialogFragmentOld newInstance(Context context) {
		NewDialogFragmentOld fragment = new NewDialogFragmentOld();
		mContext = context;
		return fragment;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		iconId = 1;
		cacheSize = Constants.CACHE_SIZE;
		dataSize = Constants.DATA_SIZE;
		systemSize = Constants.SYSTEM_SIZE;

		validName = true;

		newName = getString(R.string.default_vfs_name);
		new File(Constants.MULTI_BOOT_PATH + newName);
		newvsdesc = getString(R.string.default_vfs_description);

		LayoutInflater newVFSDialogInflater = LayoutInflater.from(mContext);
		final View view = newVFSDialogInflater.inflate(R.layout.new_vs_dialog,
				null);
		if (MiscMethods.getMemColor(cacheSize, dataSize, systemSize) != Color.RED) {
			validSize = true;
		} else
			validSize = false;

		if (mContext == null) {
			Log.d(TAG, "context is null");
		}
		AlertDialog.Builder builder;
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB)
			builder = new AlertDialog.Builder(mContext);
		else
			builder = new HoloAlertDialogBuilder(mContext);
		final AlertDialog dialog = builder
				.setTitle(getString(R.string.create_new_vfs))
				.setView(view)
				.setPositiveButton(getString(R.string.okay), onClickListener)
				.setNegativeButton(getString(R.string.cancel),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int whichButton) {
								// Canceled.
							}
						}).show();

		// declare widgets
		final EditText evsname = (EditText) view.findViewById(R.id.vsname);
		final EditText evsdesc = (EditText) view.findViewById(R.id.vsdesc);
		final Spinner spinner = (Spinner) view.findViewById(R.id.spinner);
		final TextView memory = (TextView) view
				.findViewById(R.id.icon_and_memory);
		final EditText cacheSizePicker = (EditText) view
				.findViewById(R.id.cache_size_editer);
		final EditText dataSizePicker = (EditText) view
				.findViewById(R.id.data_size_editer);
		final EditText systemSizePicker = (EditText) view
				.findViewById(R.id.system_size_editer);

		// set up widgets - evsdesc
		evsdesc.setText(newvsdesc);
		evsname.setText(newName);
		memory.setText(getString(R.string.total_memory_warning,
				MiscMethods.getTotalSize(cacheSize, dataSize, systemSize)));
		memory.setTextColor(MiscMethods.getMemColor(cacheSize, dataSize,
				systemSize));
		cacheSizePicker.setText(cacheSize + "");
		dataSizePicker.setText(dataSize + "");
		systemSizePicker.setText(systemSize + "");

		// set up spinner
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				mContext, R.array.icon_array,
				android.R.layout.simple_spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setSelection(iconId);
		spinner.setAdapter(adapter);
		spinner.setSelection(iconId);

		// set up listeners - evsname
		TextWatcher nameTextWatcher = new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {
				String filtered_str = s.toString();
				if (filtered_str.matches(".*[\\s&/&*].*")) {
					filtered_str = filtered_str.replaceAll("[\\s&/&*]", "");
					s.clear();
					s.append(filtered_str);
					Toast.makeText(mContext, R.string.illegal_char,
							Toast.LENGTH_SHORT).show();
				}
				if (s.length() > 0) {
					validName = true;
				} else {
					validName = false;
				}
				dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(
						newDialogButtonState());
				newName = filtered_str;
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}

		};
		evsname.addTextChangedListener(nameTextWatcher);

		// add textwatcher to evsdesc
		TextWatcher vsDescWatcher = new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence paramCharSequence,
					int paramInt1, int paramInt2, int paramInt3) {

			}

			@Override
			public void onTextChanged(CharSequence paramCharSequence,
					int paramInt1, int paramInt2, int paramInt3) {

			}

			@Override
			public void afterTextChanged(Editable paramEditable) {
				newvsdesc = paramEditable.toString();
			}
		};
		evsdesc.addTextChangedListener(vsDescWatcher);
		// Listener for spinner
		spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				iconId = arg2;
				memory.setCompoundDrawablesWithIntrinsicBounds(0,
						MiscMethods.getIconRes(arg2), 0, 0);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {

			}
		});

		// add textwatcher to cacheSizePicker
		TextWatcher cacheSizeWatcher = new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {
				if (s.length() < 1) {
					cacheSize = 0;
					nullData = true;
				} else {
					int newVal = Integer.parseInt(s.toString());
					cacheSize = newVal;
					nullData = false;
				}
				dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(
						newDialogButtonState());
				memory.setText(getString(R.string.total_memory_warning,
						MiscMethods.getTotalSize(cacheSize, dataSize,
								systemSize)));
				memory.setTextColor(MiscMethods.getMemColor(cacheSize,
						dataSize, systemSize));
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {

			}
		};
		cacheSizePicker.addTextChangedListener(cacheSizeWatcher);

		// add textwatcher to dataSizeWatcher
		TextWatcher dataSizeWatcher = new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {
				if (s.length() < 1) {
					dataSize = 0;
					nullData = true;
				} else {
					int newVal = Integer.parseInt(s.toString());
					dataSize = newVal;
					nullData = false;
				}
				dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(
						newDialogButtonState());
				memory.setText(getString(R.string.total_memory_warning,
						MiscMethods.getTotalSize(cacheSize, dataSize,
								systemSize)));
				memory.setTextColor(MiscMethods.getMemColor(cacheSize,
						dataSize, systemSize));
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}
		};
		dataSizePicker.addTextChangedListener(dataSizeWatcher);

		// add textwatcher to systemSizeWatcher
		TextWatcher systemSizeWatcher = new TextWatcher() {

			@Override
			public void afterTextChanged(Editable s) {
				if (s.length() < 1) {
					systemSize = 0;
					nullSystem = true;
				} else {
					int newVal = Integer.parseInt(s.toString());
					systemSize = newVal;
					nullSystem = false;
				}
				dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(
						newDialogButtonState());
				memory.setText(getString(R.string.total_memory_warning,
						MiscMethods.getTotalSize(cacheSize, dataSize,
								systemSize)));
				memory.setTextColor(MiscMethods.getMemColor(cacheSize,
						dataSize, systemSize));
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}
		};
		systemSizePicker.addTextChangedListener(systemSizeWatcher);
		return dialog;
	}

	protected boolean newDialogButtonState() {
		if (validSize & validName & !nullData & !nullCache & !nullSystem)
			return true;
		else
			return false;
	}

	private DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int whichButton) {
			Intent service = new Intent(mContext, VibhinnaService.class);
			service.putExtra(VibhinnaService.TASK_TYPE,
					VibhinnaService.TASK_TYPE_NEW_VFS);
			service.putExtra(VibhinnaService.CACHE_SIZE, cacheSize);
			service.putExtra(VibhinnaService.DATA_SIZE, dataSize);
			service.putExtra(VibhinnaService.SYSTEM_SIZE, systemSize);
			service.putExtra(VibhinnaService.ICON_ID, iconId);
			service.putExtra(VibhinnaService.FOLDER_PATH,
					"/mnt/sdcard/multiboot/" + newName);
			service.putExtra(VibhinnaService.VS_DESC, newvsdesc);
			mContext.startService(service);
		}
	};
}
