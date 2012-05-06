package com.vibhinna.binoy;

import android.app.*;
import android.content.*;
import android.database.*;
import android.graphics.*;
import android.net.*;
import android.util.*;
import android.view.*;
import android.widget.*;

public class DetailsDialog {
	Context context;
	VibhinnaFragment mVibhinnaFragment;
	String TAG = "com.vibhinna.binoy.DetailsDialog";

	public DetailsDialog(VibhinnaFragment vibhinnaFragment) {
		context = vibhinnaFragment.getActivity();
		mVibhinnaFragment = vibhinnaFragment;
	}

	public AlertDialog getDialog(long id) {
		LayoutInflater onclicklistitem = LayoutInflater.from(context);
		final View onclicklistitemView = onclicklistitem.inflate(
				R.layout.vs_details, null);
		String[] vsinfo = new String[29];
		Cursor cursor = context.getContentResolver().query(
				Uri.parse("content://" + VibhinnaProvider.AUTHORITY + "/"
						+ VibhinnaProvider.TUTORIALS_BASE_PATH + "/details/"
						+ id), null, null, null, null);
		cursor.moveToFirst();
		for (int i = 0; i < cursor.getColumnCount(); i++) {
			vsinfo[i] = cursor.getString(i);
		}
		ImageView i = (ImageView) onclicklistitemView.findViewById(R.id.icon);
		i.setImageResource(MiscMethods.getIcon(Integer.parseInt(vsinfo[3])));
		TextView name = (TextView) onclicklistitemView.findViewById(R.id.name);
		name.setText(vsinfo[1]);
		TextView desc = (TextView) onclicklistitemView.findViewById(R.id.desc);
		desc.setText(vsinfo[4]);
		TextView cacheinfo = (TextView) onclicklistitemView
				.findViewById(R.id.cacheinfo);
		cacheinfo.setText(vsinfo[2] + Constants.CACHE_IMG);
		if (vsinfo[7] == Constants.CORR_S) {
			cacheinfo.setTextColor(Color.RED);
		}
		TextView datainfo = (TextView) onclicklistitemView
				.findViewById(R.id.datainfo);
		datainfo.setText(vsinfo[2] + Constants.DATA_IMG);
		if (vsinfo[15] == Constants.CORR_S) {
			datainfo.setTextColor(Color.RED);
		}
		TextView systeminfo = (TextView) onclicklistitemView
				.findViewById(R.id.systeminfo);
		systeminfo.setText(vsinfo[2] + Constants.SYSTEM_IMG);
		if (vsinfo[23] == Constants.CORR_S) {
			systeminfo.setTextColor(Color.RED);
		}
		TextView cuuid = (TextView) onclicklistitemView
				.findViewById(R.id.cuuid);
		if (vsinfo[5] == Constants.N_A)
			cuuid.setText(context.getString(R.string.uuidna));
		else
			cuuid.setText(vsinfo[5]);
		TextView cmagic = (TextView) onclicklistitemView
				.findViewById(R.id.cmagic);
		cmagic.setText(context.getString(R.string.magnum) + vsinfo[6]);
		TextView cstate = (TextView) onclicklistitemView
				.findViewById(R.id.cstate);
		cstate.setText(context.getString(R.string.vfsst) + vsinfo[7]);
		TextView cspace = (TextView) onclicklistitemView
				.findViewById(R.id.cspace);
		if (vsinfo[9] == Constants.N_A) {
			cspace.setText(context.getString(R.string.freespna));
		} else
			cspace.setText(context.getString(R.string.freesp) + vsinfo[9]
					+ context.getString(R.string.miB) + vsinfo[8]
					+ context.getString(R.string.miBf));
		TextView cbcount = (TextView) onclicklistitemView
				.findViewById(R.id.cbcount);
		cbcount.setText(context.getString(R.string.bcount) + vsinfo[10]);
		TextView cfblock = (TextView) onclicklistitemView
				.findViewById(R.id.cfblock);
		cfblock.setText(context.getString(R.string.fbcount) + vsinfo[11]);
		TextView cbsize = (TextView) onclicklistitemView
				.findViewById(R.id.cbsize);
		cbsize.setText(context.getString(R.string.blsiz) + vsinfo[12]);
		TextView duuid = (TextView) onclicklistitemView
				.findViewById(R.id.duuid);
		if (vsinfo[13] == Constants.N_A)
			duuid.setText(context.getString(R.string.uuidna));
		else
			duuid.setText(vsinfo[13]);
		TextView dmagic = (TextView) onclicklistitemView
				.findViewById(R.id.dmagic);
		dmagic.setText(context.getString(R.string.magnum) + vsinfo[14]);
		TextView dstate = (TextView) onclicklistitemView
				.findViewById(R.id.dstate);
		dstate.setText(context.getString(R.string.vfsst) + vsinfo[15]);
		TextView dspace = (TextView) onclicklistitemView
				.findViewById(R.id.dspace);
		if (vsinfo[16] == Constants.N_A) {
			dspace.setText(context.getString(R.string.freespna));
		} else
			dspace.setText(context.getString(R.string.freesp) + vsinfo[17]
					+ context.getString(R.string.miB) + vsinfo[16]
					+ context.getString(R.string.miBf));
		TextView dbcount = (TextView) onclicklistitemView
				.findViewById(R.id.dbcount);
		dbcount.setText(context.getString(R.string.bcount) + vsinfo[18]);
		TextView dfblock = (TextView) onclicklistitemView
				.findViewById(R.id.dfblock);
		dfblock.setText(context.getString(R.string.fbcount) + vsinfo[19]);
		TextView dbsize = (TextView) onclicklistitemView
				.findViewById(R.id.dbsize);
		dbsize.setText(context.getString(R.string.blsiz) + vsinfo[20]);
		TextView suuid = (TextView) onclicklistitemView
				.findViewById(R.id.suuid);
		if (vsinfo[21] == Constants.N_A)
			suuid.setText(context.getString(R.string.uuidna));
		else
			suuid.setText(vsinfo[21]);
		TextView smagic = (TextView) onclicklistitemView
				.findViewById(R.id.smagic);
		smagic.setText(context.getString(R.string.magnum) + vsinfo[22]);
		TextView sstate = (TextView) onclicklistitemView
				.findViewById(R.id.sstate);
		sstate.setText(context.getString(R.string.vfsst) + vsinfo[23]);
		TextView sspace = (TextView) onclicklistitemView
				.findViewById(R.id.sspace);
		if (vsinfo[25] == Constants.N_A) {
			sspace.setText(context.getString(R.string.freespna));
		} else
			sspace.setText(context.getString(R.string.freesp) + vsinfo[25]
					+ context.getString(R.string.miB) + vsinfo[24]
					+ context.getString(R.string.miBf));
		TextView sbcount = (TextView) onclicklistitemView
				.findViewById(R.id.sbcount);
		sbcount.setText(context.getString(R.string.bcount) + vsinfo[26]);
		TextView sfblock = (TextView) onclicklistitemView
				.findViewById(R.id.sfblock);
		sfblock.setText(context.getString(R.string.fbcount) + vsinfo[27]);
		TextView sbsize = (TextView) onclicklistitemView
				.findViewById(R.id.sbsize);
		sbsize.setText(context.getString(R.string.blsiz) + vsinfo[28]);
		return new AlertDialog.Builder(context).setView(onclicklistitemView)
				.show();

	}

}
