package com.binoy.vibhinna;

import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragmentActivity;

public class VibhinnaActivity extends SherlockFragmentActivity {

    protected static final String ACTION_PROGRESS_UPDATE = "com.binoy.vibhinna.intent.action.ACTION_PROGRESS_UPDATE";
    protected static final String ACTION_NEW_TASK = "com.binoy.vibhinna.intent.action.ACTION_NEW_TASK";

    ViewPager mViewPager;
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        verifyExternalStorageMountState();

        setContentView(R.layout.main);
        // Create the adapter that will return a fragment for each of the three
        // primary sections
        // of the app.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        verifyExternalStorageMountState();
    }

    private void verifyExternalStorageMountState() {
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(getApplicationContext(), R.string.verify_external_storage,
                    Toast.LENGTH_LONG).show();
            finish();

        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the primary sections of the app.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            switch (i) {
                case VibhinnaFragment.SECTION_NUMBER:
                    return new VibhinnaFragment();
                case TasksQueueFragment.SECTION_NUMBER:
                    return new TasksQueueFragment();
                case SystemInfoFragment.SECTION_NUMBER:
                    return new SystemInfoFragment();
                default:
                    throw new IllegalArgumentException("Wrong section number for titlestrip!!");
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case VibhinnaFragment.SECTION_NUMBER:
                    return getString(R.string.list_tab_title).toUpperCase();
                case TasksQueueFragment.SECTION_NUMBER:
                    return getString(R.string.list_tab_tasks).toUpperCase();
                case SystemInfoFragment.SECTION_NUMBER:
                    return getString(R.string.list_tab_info).toUpperCase();
                default:
                    throw new IllegalArgumentException("Wrong section number for titlestrip!!");
            }
        }
    }
}
