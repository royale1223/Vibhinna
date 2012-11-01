package com.binoy.vibhinna;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filterable;
import android.widget.ImageView;
import android.support.v4.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class VibhinnaAdapter extends SimpleCursorAdapter implements Filterable {

    private static final String sdpath = "/mnt/sdcard";
    private Context mContext;
    PropManager propmanager;
    private final String mbpath;
    private LayoutInflater mInflater;
    int defaultColor = 0;

    public VibhinnaAdapter(Context context, int layout, Cursor c, String[] from, int[] to, int flags) {
        super(context, layout, c, from, to, flags);
        propmanager = new PropManager(mContext);
        mbpath = sdpath + propmanager.mbActivePath();
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mCursor = getCursor();
    }

    /**
     * Make a view to hold each row.
     * 
     * @see android.widget.ListAdapter#getView(int, android.view.View,
     *      android.view.ViewGroup)
     */
    public View getView(int position, View convertView, ViewGroup parent) {
        // A ViewHolder keeps references to children views to avoid unneccessary
        // calls to findViewById() on each row.
        ViewHolder holder;
        mCursor.moveToPosition(position);
        // When convertView is not null, we can reuse it directly, there is no
        // need to reinflate it. We only inflate a new View when the convertView
        // supplied by ListView is null.
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.main_row, null);
            // Creates a ViewHolder and store references to the two children
            // views we want to bind data to.
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.desc = (TextView) convertView.findViewById(R.id.desc);
            holder.status = (TextView) convertView.findViewById(R.id.status);
            holder.path = (TextView) convertView.findViewById(R.id.path);
            holder.icon = (ImageView) convertView.findViewById(R.id.icon);
            convertView.setTag(holder);
        } else {
            // Get the ViewHolder back to get fast access to the TextView and
            // the ImageView.
            holder = (ViewHolder) convertView.getTag();
        }
        // Bind the data efficiently with the holder.
        if (0 == defaultColor) {
            defaultColor = holder.name.getTextColors().getDefaultColor();
        }
        holder.name.setText(mCursor.getString(1));
        holder.desc.setText(mCursor.getString(2));
        holder.status.setText(mCursor.getString(5));
        holder.path.setText(mCursor.getString(7));
        holder.icon
                .setImageResource(MiscMethods.getIconRes(Integer.parseInt(mCursor.getString(4))));
        if ("0".equals(mCursor.getString(6))) {
            holder.status.setTextColor(Color.RED);
        } else {
            holder.status.setTextColor(Color.parseColor("#4B8A08"));
        }
        if (mbpath.equals(mCursor.getString(7))) {
            holder.name.setTextColor(Color.RED);
        } else {
            holder.name.setTextColor(defaultColor);
        }
        return convertView;
    }

    static class ViewHolder {
        ImageView icon;
        TextView name;
        TextView desc;
        TextView status;
        TextView path;
    }
}
