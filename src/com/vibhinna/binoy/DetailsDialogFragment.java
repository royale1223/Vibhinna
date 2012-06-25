package com.vibhinna.binoy;

import android.app.Dialog;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewParent;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockDialogFragment;

public class DetailsDialogFragment extends SherlockDialogFragment {

	// private long id;

	public static DetailsDialogFragment newInstance(long id) {
		DetailsDialogFragment detailsDialogFragment = new DetailsDialogFragment();
		Bundle args = new Bundle();
		args.putLong("_ID", id);
		detailsDialogFragment.setArguments(args);
		return detailsDialogFragment;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		long id = getArguments().getLong("_ID");
		this.setStyle(STYLE_NO_TITLE, 0);
		LayoutInflater inflater = LayoutInflater.from(getActivity());
		final View view = inflater.inflate(R.layout.vs_details, null);
		String[] vsinfo = new String[29];
		Cursor cursor = getActivity().getContentResolver().query(
				Uri.parse("content://" + VibhinnaProvider.AUTHORITY + "/"
						+ VibhinnaProvider.VFS_BASE_PATH + "/details/" + id),
				null, null, null, null);
		cursor.moveToFirst();
		for (int i = 0; i < cursor.getColumnCount(); i++) {
			vsinfo[i] = cursor.getString(i);
		}
		ImageView i = (ImageView) view.findViewById(R.id.icon);
		i.setImageResource(MiscMethods.getIconRes(Integer.parseInt(vsinfo[3])));
		TextView name = (TextView) view.findViewById(R.id.name);
		name.setText(vsinfo[1]);
		TextView desc = (TextView) view.findViewById(R.id.desc);
		desc.setText(vsinfo[4]);
		TextView cacheinfo = (TextView) view.findViewById(R.id.cacheinfo);
		cacheinfo.setText(vsinfo[2] + Constants.CACHE_IMG);
		if (vsinfo[7] == Constants.CORR_S) {
			cacheinfo.setTextColor(Color.RED);
		}
		TextView datainfo = (TextView) view.findViewById(R.id.datainfo);
		datainfo.setText(vsinfo[2] + Constants.DATA_IMG);
		if (vsinfo[15] == Constants.CORR_S) {
			datainfo.setTextColor(Color.RED);
		}
		TextView systeminfo = (TextView) view.findViewById(R.id.systeminfo);
		systeminfo.setText(vsinfo[2] + Constants.SYSTEM_IMG);
		if (vsinfo[23] == Constants.CORR_S) {
			systeminfo.setTextColor(Color.RED);
		}
		TextView cuuid = (TextView) view.findViewById(R.id.cuuid);
		if (vsinfo[5] == Constants.N_A)
			cuuid.setText(getActivity().getString(R.string.uuidna));
		else
			cuuid.setText(vsinfo[5]);
		TextView cmagic = (TextView) view.findViewById(R.id.cmagic);
		cmagic.setText(getActivity().getString(R.string.magnum) + vsinfo[6]);
		TextView cstate = (TextView) view.findViewById(R.id.cstate);
		cstate.setText(getActivity().getString(R.string.vfsst) + vsinfo[7]);
		TextView cspace = (TextView) view.findViewById(R.id.cspace);
		if (vsinfo[9] == Constants.N_A) {
			cspace.setText(getActivity().getString(R.string.freespna));
		} else
			cspace.setText(getActivity().getString(R.string.freesp) + vsinfo[9]
					+ getActivity().getString(R.string.miB) + vsinfo[8]
					+ getActivity().getString(R.string.miBf));
		TextView cbcount = (TextView) view.findViewById(R.id.cbcount);
		cbcount.setText(getActivity().getString(R.string.bcount) + vsinfo[10]);
		TextView cfblock = (TextView) view.findViewById(R.id.cfblock);
		cfblock.setText(getActivity().getString(R.string.fbcount) + vsinfo[11]);
		TextView cbsize = (TextView) view.findViewById(R.id.cbsize);
		cbsize.setText(getActivity().getString(R.string.blsiz) + vsinfo[12]);
		TextView duuid = (TextView) view.findViewById(R.id.duuid);
		if (vsinfo[13] == Constants.N_A)
			duuid.setText(getActivity().getString(R.string.uuidna));
		else
			duuid.setText(vsinfo[13]);
		TextView dmagic = (TextView) view.findViewById(R.id.dmagic);
		dmagic.setText(getActivity().getString(R.string.magnum) + vsinfo[14]);
		TextView dstate = (TextView) view.findViewById(R.id.dstate);
		dstate.setText(getActivity().getString(R.string.vfsst) + vsinfo[15]);
		TextView dspace = (TextView) view.findViewById(R.id.dspace);
		if (vsinfo[16] == Constants.N_A) {
			dspace.setText(getActivity().getString(R.string.freespna));
		} else
			dspace.setText(getActivity().getString(R.string.freesp)
					+ vsinfo[17] + getActivity().getString(R.string.miB)
					+ vsinfo[16] + getActivity().getString(R.string.miBf));
		TextView dbcount = (TextView) view.findViewById(R.id.dbcount);
		dbcount.setText(getActivity().getString(R.string.bcount) + vsinfo[18]);
		TextView dfblock = (TextView) view.findViewById(R.id.dfblock);
		dfblock.setText(getActivity().getString(R.string.fbcount) + vsinfo[19]);
		TextView dbsize = (TextView) view.findViewById(R.id.dbsize);
		dbsize.setText(getActivity().getString(R.string.blsiz) + vsinfo[20]);
		TextView suuid = (TextView) view.findViewById(R.id.suuid);
		if (vsinfo[21] == Constants.N_A)
			suuid.setText(getActivity().getString(R.string.uuidna));
		else
			suuid.setText(vsinfo[21]);
		TextView smagic = (TextView) view.findViewById(R.id.smagic);
		smagic.setText(getActivity().getString(R.string.magnum) + vsinfo[22]);
		TextView sstate = (TextView) view.findViewById(R.id.sstate);
		sstate.setText(getActivity().getString(R.string.vfsst) + vsinfo[23]);
		TextView sspace = (TextView) view.findViewById(R.id.sspace);
		if (vsinfo[25] == Constants.N_A) {
			sspace.setText(getActivity().getString(R.string.freespna));
		} else
			sspace.setText(getActivity().getString(R.string.freesp)
					+ vsinfo[25] + getActivity().getString(R.string.miB)
					+ vsinfo[24] + getActivity().getString(R.string.miBf));
		TextView sbcount = (TextView) view.findViewById(R.id.sbcount);
		sbcount.setText(getActivity().getString(R.string.bcount) + vsinfo[26]);
		TextView sfblock = (TextView) view.findViewById(R.id.sfblock);
		sfblock.setText(getActivity().getString(R.string.fbcount) + vsinfo[27]);
		TextView sbsize = (TextView) view.findViewById(R.id.sbsize);
		sbsize.setText(getActivity().getString(R.string.blsiz) + vsinfo[28]);
		return showDialogWithNoTopSpace(view, new HoloAlertDialogBuilder(
				getActivity()).setView(view).create());
	}

	/**
	 * Show a Dialog with the extra title/top padding collapsed.
	 * 
	 * @param customView
	 *            The custom view that you added to the dialog
	 * @param dialog
	 *            The dialog to display without top spacing
	 */
	public static Dialog showDialogWithNoTopSpace(final View customView,
			final Dialog dialog) {
		// Now we setup a listener to detect as soon as the dialog has shown.
		customView.getViewTreeObserver().addOnGlobalLayoutListener(
				new OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						// Check if your view has been laid out yet
						if (customView.getHeight() > 0) {
							// If it has been, we will search the view hierarchy
							// for the
							// view that is responsible for the extra space.
							LinearLayout dialogLayout = findDialogLinearLayout(customView);
							if (dialogLayout == null) {
								// Could find it. Unexpected.

							} else {
								// Found it, now remove the height of the title
								// area
								View child = dialogLayout.getChildAt(0);
								if (child != customView) {
									// remove height
									LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) child
											.getLayoutParams();
									lp.height = 0;
									child.setLayoutParams(lp);

								} else {
									// Could find it. Unexpected.
								}
							}

							// Done with the listener
							customView.getViewTreeObserver()
									.removeGlobalOnLayoutListener(this);
						}
					}

				});
		// Show the dialog
		return dialog;
	}

	/**
	 * Searches parents for a LinearLayout
	 * 
	 * @param view
	 *            to search the search from
	 * @return the first parent view that is a LinearLayout or null if none was
	 *         found
	 */
	public static LinearLayout findDialogLinearLayout(View view) {
		ViewParent parent = (ViewParent) view.getParent();
		if (parent != null) {
			if (parent instanceof LinearLayout) {
				// Found it
				return (LinearLayout) parent;

			} else if (parent instanceof View) {
				// Keep looking
				return findDialogLinearLayout((View) parent);

			}
		}
		// Couldn't find it
		return null;
	}
}
