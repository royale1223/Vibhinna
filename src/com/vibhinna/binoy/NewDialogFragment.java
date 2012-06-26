package com.vibhinna.binoy;

import java.io.File;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockDialogFragment;

public class NewDialogFragment extends SherlockDialogFragment {

	private static final String TAG = "com.vibhinna.binoy.NewVSDialogMakerICS";
	private static Context mContext;
	private static VibhinnaFragment mVibFragment;
	private static int iconId;
	private static int cacheSize;
	private static int dataSize;
	private static int systemSize;

	private boolean validName = true;
	private boolean validSize = false;

	private static String newvsdesc;
	private static String newName;
	private File defaultFolder;

	Handler handler;

	// ProcessManager processManager;

	/**
	 * creates a new instance of NewDialogFragment
	 * 
	 * @param vibFragment
	 */
	static NewDialogFragment newInstance(VibhinnaFragment vibFragment) {
		NewDialogFragment fragment = new NewDialogFragment();
		mVibFragment = vibFragment;
		mContext = mVibFragment.getSherlockActivity();
		mContext.getContentResolver();
		return fragment;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		iconId = 1;
		cacheSize = Constants.CACHE_SIZE;
		dataSize = Constants.DATA_SIZE;
		systemSize = Constants.SYSTEM_SIZE;

		validName = true;

		newName = mContext.getString(R.string.untitled);
		defaultFolder = new File(Constants.MULTI_BOOT_PATH + newName);
		newvsdesc = mContext.getString(R.string.newvfsi)
				+ defaultFolder.getPath() + ")";

		// processManager = new ProcessManager();

		LayoutInflater newVFSDialogInflater = LayoutInflater.from(mContext);
		final View view = newVFSDialogInflater.inflate(R.layout.new_vs_dialog,
				null);
		if (MiscMethods.getMemColor(cacheSize, dataSize, systemSize) != Color.RED) {
			validSize = true;
		} else
			validSize = false;
		final EditText evsname = (EditText) view.findViewById(R.id.vsname);
		final EditText evsdesc = (EditText) view.findViewById(R.id.vsdesc);
		final Spinner spinner = (Spinner) view.findViewById(R.id.spinner);
		final TextView memory = (TextView) view
				.findViewById(R.id.icon_and_memory);
		evsdesc.setText(newvsdesc);
		evsname.setText(newName);
		memory.setText(MiscMethods
				.getTotalSize(cacheSize, dataSize, systemSize) + " MB");
		memory.setTextColor(MiscMethods.getMemColor(cacheSize, dataSize,
				systemSize));
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				mContext, R.array.icon_array,
				android.R.layout.simple_spinner_item);
		if (mContext == null) {
			Log.d(TAG, "context is null");
		}

		final AlertDialog dialog = new AlertDialog.Builder(mContext)
				.setTitle(mContext.getString(R.string.createvfs))
				.setView(view)
				.setPositiveButton(mContext.getString(R.string.okay),
						onClickListener)
				.setNegativeButton(mContext.getString(R.string.cancel),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int whichButton) {
								// Canceled.
							}
						}).show();
		TextWatcher vsNameWatcher = new TextWatcher() {
			@Override
			public void afterTextChanged(Editable s) {
				String filtered_str = s.toString();
				if (filtered_str.matches(".*[\\s&/&*].*")) {
					filtered_str = filtered_str.replaceAll("[\\s&/&*]", "");
					s.clear();
					s.append(filtered_str);
					Toast.makeText(mContext, "Illegal character!",
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
		evsname.addTextChangedListener(vsNameWatcher);
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
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setSelection(iconId);
		spinner.setAdapter(adapter);
		spinner.setSelection(iconId);
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

		String[] nums = new String[Constants.MAX_IMG_SIZE];
		for (int j = 0; j < nums.length; j++)
			nums[j] = Integer.toString((j + 1) * 10);
		final NumberPicker cacheSizePicker = (NumberPicker) view
				.findViewById(R.id.cache_size_picker);
		if (cacheSizePicker == null) {
			throw new RuntimeException("mNumberPicker is null!");
		}
		cacheSizePicker.setWrapSelectorWheel(false);
		cacheSizePicker.setMaxValue(Constants.MAX_IMG_SIZE);
		cacheSizePicker.setMinValue(Constants.MIN_IMG_SIZE);
		cacheSizePicker.setValue(cacheSize / 10);
		cacheSizePicker.setDisplayedValues(nums);
		OnValueChangeListener cacheOnValueChangeListener = new OnValueChangeListener() {
			@Override
			public void onValueChange(NumberPicker picker, int oldVal,
					int newVal) {
				cacheSize = newVal * 10;
				memory.setText(MiscMethods.getTotalSize(cacheSize, dataSize,
						systemSize) + " MB");
				memory.setTextColor(MiscMethods.getMemColor(cacheSize,
						dataSize, systemSize));
				if (MiscMethods.getMemColor(cacheSize, dataSize, systemSize) == Color.RED) {
				} else {
				}
				dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(
						newDialogButtonState());
			}
		};
		cacheSizePicker.setOnValueChangedListener(cacheOnValueChangeListener);

		final NumberPicker dataSizePicker = (NumberPicker) view
				.findViewById(R.id.data_size_picker);
		if (dataSizePicker == null) {
			throw new RuntimeException("mNumberPicker is null!");
		}
		dataSizePicker.setWrapSelectorWheel(false);
		dataSizePicker.setMaxValue(Constants.MAX_IMG_SIZE);
		dataSizePicker.setMinValue(Constants.MIN_IMG_SIZE);
		dataSizePicker.setValue(dataSize / 10);
		dataSizePicker.setDisplayedValues(nums);
		OnValueChangeListener dataOnValueChangeListener = new OnValueChangeListener() {
			@Override
			public void onValueChange(NumberPicker picker, int oldVal,
					int newVal) {
				dataSize = newVal * 10;
				// FIXME ugly code
				memory.setText(MiscMethods.getTotalSize(cacheSize, dataSize,
						systemSize) + " MB");
				memory.setTextColor(MiscMethods.getMemColor(cacheSize,
						dataSize, systemSize));
				if (MiscMethods.getMemColor(cacheSize, dataSize, systemSize) == Color.RED) {
				} else {
				}
				dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(
						newDialogButtonState());
			}
		};
		dataSizePicker.setOnValueChangedListener(dataOnValueChangeListener);

		final NumberPicker systemSizePicker = (NumberPicker) view
				.findViewById(R.id.system_size_picker);
		if (systemSizePicker == null) {
			throw new RuntimeException("mNumberPicker is null!");
		}
		systemSizePicker.setWrapSelectorWheel(false);
		systemSizePicker.setMaxValue(Constants.MAX_IMG_SIZE);
		systemSizePicker.setMinValue(Constants.MIN_IMG_SIZE);
		systemSizePicker.setValue(systemSize / 10);
		systemSizePicker.setDisplayedValues(nums);
		OnValueChangeListener systemOnValueChangeListener = new OnValueChangeListener() {
			@Override
			public void onValueChange(NumberPicker picker, int oldVal,
					int newVal) {
				systemSize = newVal * 10;
				memory.setText(MiscMethods.getTotalSize(cacheSize, dataSize,
						systemSize) + " MB");
				memory.setTextColor(MiscMethods.getMemColor(cacheSize,
						dataSize, systemSize));
				if (MiscMethods.getMemColor(cacheSize, dataSize, systemSize) == Color.RED) {
				} else {
				}
				dialog.getButton(DialogInterface.BUTTON_POSITIVE).setEnabled(
						newDialogButtonState());
			}
		};
		systemSizePicker.setOnValueChangedListener(systemOnValueChangeListener);

		return dialog;
	}

	protected boolean newDialogButtonState() {
		if (validSize & validName)
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
			// service.putExtra();
			mContext.startService(service);
		}
	};
}
