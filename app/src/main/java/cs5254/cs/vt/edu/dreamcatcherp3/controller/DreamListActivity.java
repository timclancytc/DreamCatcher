package cs5254.cs.vt.edu.dreamcatcherp3.controller;

import android.support.v4.app.Fragment;


public class DreamListActivity extends SingleFragmentActivity {
    @Override
    protected Fragment createFragment() {
        return new DreamListFragment();
    }


}