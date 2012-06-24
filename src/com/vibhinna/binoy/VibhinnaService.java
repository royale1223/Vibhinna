package com.vibhinna.binoy;

import java.io.File;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;

public class VibhinnaService extends CustomIntentService {

	public static final String TASK_TYPE = "type";
	public static final int TASK_TYPE_NEW_VFS = 1;
	public static final String CACHE_SIZE = "cache_size";
	public static final String DATA_SIZE = "data_size";
	public static final String SYSTEM_SIZE = "system_size";
	public static final String FOLDER_PATH = "folder_path";
	public static final String VS_DESC = "vs_desc";
	public static final String ICON_ID = "icon_id";
	protected static final String TAG = "VibhinnaService";
	public static final String ACTION_TASK_QUEUE_UPDATED = "com.binoy.vibhinna.action.ACTION_TASK_QUEUE_UPDATED";

	private static ContentResolver mResolver;

	private static Context mContext;
	static NotificationManager notificationMgr;

	public VibhinnaService() {
		super("Service");
	}

	public int onStartCommand(Intent intent, int flags, int startId) {
		// mLocalBroadcastManager = LocalBroadcastManager.getInstance(this);
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		mResolver = this.getContentResolver();
		mContext = this;
		notificationMgr = (NotificationManager) mContext
				.getSystemService(NOTIFICATION_SERVICE);
		VibinnaTask vibTask = new VibinnaTask(intent);
		vibTask.executeTask();
	}

	private static class VibinnaTask {
		private static final String START_ID = "start_id";
		private int type;
		private int status;
		private int progress;

		AsyncTask<Object[], Void, Void> task;
		Object[] objects;

		public VibinnaTask(Intent intent) {
			if (intent.getIntExtra(TASK_TYPE, 0) == TASK_TYPE_NEW_VFS) {

				Object[] obj = new Object[9];

				obj[0] = intent.getIntExtra(CACHE_SIZE, 0);
				obj[1] = intent.getIntExtra(DATA_SIZE, 0);
				obj[2] = intent.getIntExtra(SYSTEM_SIZE, 0);

				obj[3] = intent.getStringExtra(FOLDER_PATH);
				obj[4] = intent.getStringExtra(VS_DESC);
				obj[5] = intent.getIntExtra(ICON_ID, 0);
				obj[6] = intent.getIntExtra(START_ID, 0);
				obj[7] = intent.getLongExtra(TasksProvider._ID, -1);
				task = new CreateVFSTask();
				objects = obj;
				// task.execute(obj);
			}
		}

		@SuppressWarnings("unused")
		public int getStatus() {
			return status;
		}

		@SuppressWarnings("unused")
		public int getProgress() {
			return progress;
		}

		@SuppressWarnings("unused")
		public int getType() {
			return type;
		}

		public void executeTask() {
			task.execute(objects);
		}

		class CreateVFSTask extends AsyncTask<Object[], Void, Void> {
			protected Void doInBackground(Object[]... objs) {

				int cacheSize = (Integer) objs[0][0];
				int dataSize = (Integer) objs[0][1];
				int systemSize = (Integer) objs[0][2];

				String folderPath = MiscMethods.avoidDuplicateFile(
						new File((String) objs[0][3])).getPath();
				String vsDesc = (String) objs[0][4];
				int iconId = (Integer) objs[0][5];
				String _id = (Long) objs[0][7] + "";

				File newFolder = new File(folderPath);
				String vsName = newFolder.getName();
				newFolder.mkdir();

				String cachesize = cacheSize + "";
				String datasize = dataSize + "";
				String systemsize = systemSize + "";
				String[] shellinput = { "", "", "", "", "" };
				shellinput[1] = folderPath;
				shellinput[0] = Constants.CMD_DD;
				shellinput[2] = "/cache.img bs=1000000 count=";
				shellinput[3] = cachesize;
				final Message m1 = new Message();

				ContentValues values = new ContentValues();
				values.put(DatabaseHelper.TASK_VS, newFolder.getName());
				values.put(DatabaseHelper.TASK_TYPE, TASK_TYPE_NEW_VFS);
				values.put(DatabaseHelper.TASK_STATUS,
						TasksAdapter.TASK_STATUS_RUNNING);
				values.put(DatabaseHelper.TASK_MESSAGE,
						mContext.getString(R.string.creating) + vsName
								+ mContext.getString(R.string.cacheimg));
				values.put(DatabaseHelper.TASK_PROGRESS, 1 * 100 / 7);
				mResolver.update(
						Uri.withAppendedPath(TasksProvider.CONTENT_URI, _id),
						values, null, null);

				m1.arg1 = 1;
				m1.obj = new File(folderPath).getName();
				handler.sendMessage(m1);
				ProcessManager.errorStreamReader(shellinput);
				shellinput[0] = Constants.CMD_MKE2FS_EXT3;
				shellinput[2] = Constants.CACHE_IMG;
				shellinput[3] = "";

				values.put(DatabaseHelper.TASK_MESSAGE,
						mContext.getString(R.string.formating) + vsName
								+ mContext.getString(R.string.cachext3));
				values.put(DatabaseHelper.TASK_PROGRESS, 2 * 100 / 7);
				mResolver.update(
						Uri.withAppendedPath(TasksProvider.CONTENT_URI, _id),
						values, null, null);

				final Message m2 = new Message();
				m2.arg1 = 2;
				m2.obj = new File(folderPath).getName();
				handler.sendMessage(m2);
				ProcessManager.inputStreamReader(shellinput, 20);

				values.put(DatabaseHelper.TASK_MESSAGE,
						mContext.getString(R.string.creating) + vsName
								+ mContext.getString(R.string.dataimg));
				values.put(DatabaseHelper.TASK_PROGRESS, 3 * 100 / 7);
				mResolver.update(
						Uri.withAppendedPath(TasksProvider.CONTENT_URI, _id),
						values, null, null);

				final Message m3 = new Message();
				m3.arg1 = 3;
				m3.obj = new File(folderPath).getName();
				handler.sendMessage(m3);
				shellinput[0] = Constants.CMD_DD;
				shellinput[2] = "/data.img bs=1000000 count=";
				shellinput[3] = datasize;
				ProcessManager.errorStreamReader(shellinput);
				shellinput[0] = Constants.CMD_MKE2FS_EXT3;
				shellinput[2] = Constants.DATA_IMG;
				shellinput[3] = "";

				values.put(DatabaseHelper.TASK_MESSAGE,
						mContext.getString(R.string.formating) + vsName
								+ mContext.getString(R.string.dataext3));
				values.put(DatabaseHelper.TASK_PROGRESS, 4 * 100 / 7);
				mResolver.update(
						Uri.withAppendedPath(TasksProvider.CONTENT_URI, _id),
						values, null, null);

				final Message m4 = new Message();
				m4.arg1 = 4;
				m4.obj = new File(folderPath).getName();
				handler.sendMessage(m4);
				ProcessManager.inputStreamReader(shellinput, 20);
				shellinput[0] = Constants.CMD_DD;
				shellinput[2] = "/system.img bs=1000000 count=";
				shellinput[3] = systemsize;

				values.put(DatabaseHelper.TASK_MESSAGE,
						mContext.getString(R.string.creating) + vsName
								+ mContext.getString(R.string.systemimg));
				values.put(DatabaseHelper.TASK_PROGRESS, 5 * 100 / 7);
				mResolver.update(
						Uri.withAppendedPath(TasksProvider.CONTENT_URI, _id),
						values, null, null);

				final Message m5 = new Message();
				m5.arg1 = 5;
				m5.obj = new File(folderPath).getName();
				handler.sendMessage(m5);
				ProcessManager.errorStreamReader(shellinput);
				shellinput[0] = Constants.CMD_MKE2FS_EXT3;
				shellinput[2] = Constants.SYSTEM_IMG;
				shellinput[3] = "";

				values.put(DatabaseHelper.TASK_MESSAGE,
						mContext.getString(R.string.formating) + vsName
								+ mContext.getString(R.string.systemext3));
				values.put(DatabaseHelper.TASK_PROGRESS, 6 * 100 / 7);
				mResolver.update(
						Uri.withAppendedPath(TasksProvider.CONTENT_URI, _id),
						values, null, null);

				final Message m6 = new Message();
				m6.arg1 = 6;
				m6.obj = new File(folderPath).getName();
				handler.sendMessage(m6);
				ProcessManager.inputStreamReader(shellinput, 20);

				ContentValues vValues = new ContentValues();
				vValues.put(DatabaseHelper.VIRTUAL_SYSTEM_COLUMN_NAME,
						newFolder.getName());
				vValues.put(DatabaseHelper.VIRTUAL_SYSTEM_COLUMN_PATH,
						folderPath);
				vValues.put(DatabaseHelper.VIRTUAL_SYSTEM_COLUMN_DESCRIPTION,
						vsDesc);
				vValues.put(DatabaseHelper.VIRTUAL_SYSTEM_COLUMN_TYPE, iconId
						+ "");
				mResolver.insert(VibhinnaProvider.CONTENT_URI, vValues);

				values.put(DatabaseHelper.TASK_STATUS,
						TasksAdapter.TASK_STATUS_FINISHED);
				values.put(DatabaseHelper.TASK_MESSAGE, "Creation of " + vsName
						+ " completed.");
				values.put(DatabaseHelper.TASK_PROGRESS, 7 * 100 / 7);
				mResolver.update(
						Uri.withAppendedPath(TasksProvider.CONTENT_URI, _id),
						values, null, null);

				final Message endmessage = new Message();
				endmessage.obj = new File(folderPath).getName();
				handler.sendMessage(endmessage);
				return null;
			}
		}
	}

	private static Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			String vsName = (String) msg.obj;
			mLocalBroadcastManager.sendBroadcast(broadcastIntent);
			switch (msg.arg1) {
			case 1: {

				displayNotificationMessage(
						mContext.getString(R.string.creating) + vsName
								+ mContext.getString(R.string.cacheimg), false);
				return;
			}
			case 2: {
				displayNotificationMessage(
						mContext.getString(R.string.formating) + vsName
								+ mContext.getString(R.string.cachext3), false);
				return;
			}
			case 3: {
				displayNotificationMessage(
						mContext.getString(R.string.creating) + vsName
								+ mContext.getString(R.string.dataimg), false);
				return;
			}
			case 4: {
				displayNotificationMessage(
						mContext.getString(R.string.formating) + vsName
								+ mContext.getString(R.string.dataext3), false);
				return;
			}
			case 5: {
				displayNotificationMessage(
						mContext.getString(R.string.creating) + vsName
								+ mContext.getString(R.string.systemimg), false);
				return;
			}
			case 6: {
				displayNotificationMessage(
						mContext.getString(R.string.formating) + vsName
								+ mContext.getString(R.string.systemext3),
						false);
				return;
			}
			default: {
				// mVibFragment.restartLoading();
				displayNotificationMessage("Creation of " + vsName
						+ " completed.", true);
				return;
			}
			}
		}
	};

	private static void displayNotificationMessage(String message,
			boolean cancellable) {
		PendingIntent intent = PendingIntent.getActivity(mContext, 0,
				new Intent(mContext, VibhinnaActivity.class), 0);
		NotificationCompat.Builder builder = new NotificationCompat.Builder(
				mContext)
				.setContentTitle("Vibhinna")
				.setTicker(message)
				.setContentText(message)
				.setContentIntent(intent)
				.setSmallIcon(R.drawable.ic_notification)
				.setLargeIcon(
						BitmapFactory.decodeResource(mContext.getResources(),
								R.drawable.ic_notification))
				.setWhen(System.currentTimeMillis());

		if (cancellable) {
			builder.setAutoCancel(true);
		} else {
			builder.setOngoing(true);
		}
		Notification notification = builder.getNotification();
		notificationMgr.notify(0, notification);
	}

}
