package com.binoy.vibhinna;

import java.io.File;
import java.util.Scanner;
import java.util.regex.Pattern;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockDialogFragment;

public class ResizeDialogFragment extends SherlockDialogFragment {

	protected static final String TAG = "ResizeDialogFragment";

	private static final String CACHE_IMG = "cache.img";
	private static final String DATA_IMG = "data.img";
	private static final String SYSTEM_IMG = "system.img";

	private static Context mContext;
	private static String mPath;

	private int cacheSize;
	private int dataSize;
	private int systemSize;

	private TextView cacheSizeText;
	private SeekBar cacheSeekBar;
	private TextView dataSizeText;
	private SeekBar dataSeekBar;
	private TextView systemSizeText;
	private SeekBar systemSeekBar;

	private int cacheMin;
	private int dataMin;
	private int systemMin;

	private int cacheOrig;
	private int dataOrig;
	private int systemOrig;

	private int maxTotalSize;

	static ResizeDialogFragment newInstance(Context context, String path) {
		ResizeDialogFragment fragment = new ResizeDialogFragment();
		mPath = path;
		mContext = context;
		return fragment;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		super.onCreateDialog(savedInstanceState);
		LayoutInflater factory = LayoutInflater.from(mContext);
		final View resizeView = factory.inflate(R.layout.resize_dialog, null);

		setUpMaxMin();
		cacheSize = cacheOrig = getSize(CACHE_IMG);
		dataSize = dataOrig = getSize(DATA_IMG);
		systemSize = systemOrig = getSize(SYSTEM_IMG);
		maxTotalSize = sdFreeSpace() + cacheOrig + dataOrig + systemOrig;

		AlertDialog.Builder builder;
		if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.HONEYCOMB)
			builder = new AlertDialog.Builder(mContext);
		else
			builder = new HoloAlertDialogBuilder(mContext);
		final AlertDialog dialog = builder.setTitle(getString(R.string.resize))
				.setView(resizeView)
				.setPositiveButton(R.string.okay, onClickListener)
				.setNeutralButton(R.string.cancel, null).show();

		cacheSizeText = (TextView) resizeView
				.findViewById(R.id.resize_dialog_cache_size);
		cacheSeekBar = (SeekBar) resizeView
				.findViewById(R.id.resize_dialog_cache_size_seekbar);

		dataSizeText = (TextView) resizeView
				.findViewById(R.id.resize_dialog_data_size);
		dataSeekBar = (SeekBar) resizeView
				.findViewById(R.id.resize_dialog_data_size_seekbar);

		systemSizeText = (TextView) resizeView
				.findViewById(R.id.resize_dialog_system_size);
		systemSeekBar = (SeekBar) resizeView
				.findViewById(R.id.resize_dialog_system_size_seekbar);

		cacheSeekBar.setMax(getMaxSize(CACHE_IMG));
		dataSeekBar.setMax(getMaxSize(DATA_IMG));
		systemSeekBar.setMax(getMaxSize(SYSTEM_IMG));

		cacheSeekBar.setProgress(cacheOrig);
		dataSeekBar.setProgress(dataOrig);
		systemSeekBar.setProgress(systemOrig);

		cacheSizeText.setText(getString(R.string.space_in_mb, cacheOrig));
		dataSizeText.setText(getString(R.string.space_in_mb, dataOrig));
		systemSizeText.setText(getString(R.string.space_in_mb, systemOrig));

		cacheSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				int previous = cacheSize;
				if (progress < cacheMin) {
					seekBar.setProgress(cacheMin);
					cacheSize = cacheMin;
					cacheSizeText.setText(getString(R.string.space_in_mb,
							cacheMin));
				} else {
					cacheSize = progress;
					cacheSizeText.setText(getString(R.string.space_in_mb,
							progress));
					if (progress > previous) {
						fixOtherSizes(CACHE_IMG, progress);
					}
				}
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}
		});

		dataSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				int previous = dataSize;
				if (progress < dataMin) {
					seekBar.setProgress(dataMin);
					dataSize = dataMin;
					dataSizeText.setText(getString(R.string.space_in_mb,
							dataMin));
				} else {
					dataSize = progress;
					dataSizeText.setText(getString(R.string.space_in_mb,
							progress));
					if (progress > previous) {
						fixOtherSizes(DATA_IMG, progress);
					}
				}
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}
		});

		systemSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				int previous = systemSize;
				if (progress < systemMin) {
					seekBar.setProgress(systemMin);
					systemSize = systemMin;
					systemSizeText.setText(getString(R.string.space_in_mb,
							systemMin));
				} else {
					systemSize = progress;
					systemSizeText.setText(getString(R.string.space_in_mb,
							progress));
					if (progress > previous) {
						fixOtherSizes(SYSTEM_IMG, progress);
					}
				}
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
			}
		});

		return dialog;
	}

	private OnClickListener onClickListener = new OnClickListener() {

		@Override
		public void onClick(DialogInterface dialog, int which) {
			Intent service = new Intent(mContext, VibhinnaService.class);
			service.putExtra(VibhinnaService.TASK_TYPE,
					VibhinnaService.TASK_TYPE_RESIZE_VFS);
			service.putExtra(VibhinnaService.CACHE_SIZE, cacheSize);
			service.putExtra(VibhinnaService.DATA_SIZE, dataSize);
			service.putExtra(VibhinnaService.SYSTEM_SIZE, systemSize);
			service.putExtra(VibhinnaService.FOLDER_PATH, mPath);
			mContext.startService(service);
		}
	};

	private int getMaxSize(String string) {
		File imageFile = new File(mPath, string);
		int imageSpace = (int) (imageFile.length() / 1048576);
		String[] images = new String[] { CACHE_IMG, DATA_IMG, SYSTEM_IMG };
		int maxSpace = sdFreeSpace() + imageSpace;
		for (String image : images) {
			if (!image.equals(string)) {
				if (image.equals(CACHE_IMG)) {
					maxSpace = maxSpace + cacheOrig - cacheMin;
				}
				if (image.equals(DATA_IMG)) {
					maxSpace = maxSpace + dataOrig - dataMin;
				}
				if (image.equals(SYSTEM_IMG)) {
					maxSpace = maxSpace + systemOrig - systemMin;
				}
			}
		}
		return maxSpace;
	}

	private int getSize(String image) {
		return (int) (new File(mPath, image).length() / 1048576);
	}

	private void setUpMaxMin() {
		String[] images = new String[] { CACHE_IMG, DATA_IMG, SYSTEM_IMG };
		long imagesFreeSpace = 0;
		for (String image : images) {
			File file = new File(mPath, image);
			String[] shellinput = { Constants.CMD_TUNE2FS, mPath, "/", image };
			String istr = ProcessManager.inputStreamReader(shellinput, 40);
			Scanner scanner = new Scanner(istr).useDelimiter("\\n");
			scanner.findWithinHorizon(
					Pattern.compile("Free\\sblocks:\\s*(\\d+)"), 0);
			String freeBlocks = scanner.match().group(1);
			scanner.findWithinHorizon(
					Pattern.compile("Block\\ssize:\\s*(\\d+)"), 0);
			String blockSize = scanner.match().group(1);
			int freeSpaceInMb = Integer.parseInt(freeBlocks)
					* Integer.parseInt(blockSize) / 1048576;
			imagesFreeSpace = imagesFreeSpace + freeSpaceInMb;
			int totalSize = (int) (file.length() / 1048576);
			int usedSpace = totalSize - freeSpaceInMb;
			if (image.equals(CACHE_IMG))
				cacheMin = usedSpace + 1;
			if (image.equals(DATA_IMG))
				dataMin = usedSpace + 1;
			if (image.equals(SYSTEM_IMG))
				systemMin = usedSpace + 1;
		}
	}

	private int sdFreeSpace() {
		return (int) (Environment.getExternalStorageDirectory()
				.getUsableSpace() / 1048576);
	}

	private void fixOtherSizes(String image, int progress) {
		if (image.equals(CACHE_IMG) && getTotalSize() > maxTotalSize) {
			int excess = getTotalSize() - maxTotalSize;
			if (excess <= systemSize - systemMin) {
				systemSize = systemSize - excess;
				systemSeekBar.setProgress(systemSize);
			} else {
				systemSize = systemMin;
				dataSize = dataSize - (excess - (systemSize - systemMin));
				systemSeekBar.setProgress(systemSize);
				dataSeekBar.setProgress(dataSize);
			}
		}
		if (image.equals(DATA_IMG) && getTotalSize() > maxTotalSize) {
			int excess = getTotalSize() - maxTotalSize;
			if (excess < cacheSize - cacheMin) {
				cacheSize = cacheSize - excess;
				cacheSeekBar.setProgress(cacheSize);
			} else {
				cacheSize = cacheMin;
				systemSize = systemSize - (excess - (cacheSize - cacheMin));
				cacheSeekBar.setProgress(cacheSize);
				systemSeekBar.setProgress(systemSize);
			}
		}
		if (image.equals(SYSTEM_IMG) && getTotalSize() > maxTotalSize) {
			int excess = getTotalSize() - maxTotalSize;
			if (excess < cacheSize - cacheMin) {
				cacheSize = cacheSize - excess;
				cacheSeekBar.setProgress(cacheSize);
			} else {
				cacheSize = cacheMin;
				dataSize = dataSize - (excess - (cacheSize - cacheMin));
				cacheSeekBar.setProgress(cacheSize);
				dataSeekBar.setProgress(dataSize);
			}
		}
	}

	private int getTotalSize() {
		return cacheSize + dataSize + systemSize;
	}
}
