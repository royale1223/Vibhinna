package com.vibhinna.binoy;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class TasksAdapter extends SimpleCursorAdapter {

	public static final int TASK_TYPE_CREATE = 0;
	public static final int TASK_TYPE_FORMAT = 1;
	public static final int TASK_STATUS_RUNNING = 0;
	public static final int TASK_STATUS_WAITING = 1;
	public static final int TASK_STATUS_FINISHED = 2;
	protected static final String TAG = "TasksAdapter";
	private LayoutInflater mInflater;

	public TasksAdapter(Context context, int layout, Cursor c, String[] from,
			int[] to, int flags) {
		super(context, layout, c, from, to, flags);
		mContext = context;
		mInflater = LayoutInflater.from(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		// A ViewHolder keeps references to children views to avoid unneccessary
		// calls to findViewById() on each row.
		ViewHolder holder;

		// When convertView is not null, we can reuse it directly, there is no
		// need to reinflate it. We only inflate a new View when the convertView
		// supplied by ListView is null.
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.task_queue_row, null);
			// Creates a ViewHolder and store references to the two children
			// views we want to bind data to.
			holder = new ViewHolder();
			holder.title = (TextView) convertView
					.findViewById(R.id.task_queue_row_title);
			holder.message = (TextView) convertView
					.findViewById(R.id.task_queue_row_message);
			holder.icon = (ImageView) convertView
					.findViewById(R.id.task_queue_row_image);
			holder.progress = (ProgressBar) convertView
					.findViewById(R.id.task_queue_row_progess);
			holder.percent = (TextView) convertView
					.findViewById(R.id.task_queue_row_percent);
			convertView.setTag(holder);
		} else {
			// Get the ViewHolder back to get fast access to the TextView and
			// the ImageView.
			holder = (ViewHolder) convertView.getTag();
		}

		Cursor mCursor = getCursor();
		mCursor.moveToPosition(position);

		String title;
		switch (mCursor
				.getInt(mCursor.getColumnIndex(DatabaseHelper.TASK_TYPE))) {
		case TASK_TYPE_CREATE:
			title = "Create "
					+ mCursor.getString(mCursor
							.getColumnIndex(DatabaseHelper.TASK_VS));
			break;
		case TASK_TYPE_FORMAT:
			title = "Format "
					+ mCursor.getString(mCursor
							.getColumnIndex(DatabaseHelper.TASK_VS));
			break;
		default:
			title = "Unknown operation!!";
			break;
		}
		holder.title.setText(title);
		holder.message.setText(mCursor.getString(mCursor
				.getColumnIndex(DatabaseHelper.TASK_MESSAGE)));
		switch (mCursor.getInt(mCursor
				.getColumnIndex(DatabaseHelper.TASK_STATUS))) {
		case TASK_STATUS_RUNNING:
			holder.progress.setIndeterminate(false);
			holder.progress.setProgress(mCursor.getInt(mCursor
					.getColumnIndex(DatabaseHelper.TASK_PROGRESS)));
			holder.percent.setText(mCursor.getInt(mCursor
					.getColumnIndex(DatabaseHelper.TASK_PROGRESS)) + "%");
			holder.icon.setImageResource(R.drawable.ic_task_running);
			break;
		case TASK_STATUS_WAITING:
			holder.progress.setIndeterminate(true);
			holder.icon.setImageResource(R.drawable.ic_task_wait);
			break;
		case TASK_STATUS_FINISHED:
			holder.progress.setIndeterminate(false);
			holder.progress.setProgress(holder.progress.getMax());
			holder.percent.setText(holder.progress.getMax() + "%");
			holder.icon.setImageResource(R.drawable.ic_task_done);
			break;
		default:
			holder.progress.setIndeterminate(false);
			holder.icon.setImageResource(R.drawable.ic_task_wait);
			break;
		}
		return convertView;
	}

	static class ViewHolder {
		ImageView icon;
		TextView title;
		TextView message;
		ProgressBar progress;
		TextView percent;

	}
}
