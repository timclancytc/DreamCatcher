package cs5254.cs.vt.edu.dreamcatcherp3.controller;

import android.content.Intent;
import android.support.v4.app.Fragment;

import cs5254.cs.vt.edu.dreamcatcherp3.R;
import cs5254.cs.vt.edu.dreamcatcherp3.model.Dream;


public class DreamListActivity extends SingleFragmentActivity
    implements DreamListFragment.Callbacks,
                DreamFragment.Callbacks {
    @Override
    protected Fragment createFragment() {
        return new DreamListFragment();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_masterdetail;
    }

    @Override
    public void onDreamSelected(Dream dream) {
        if (findViewById(R.id.detail_fragment_container) == null) {
            Intent intent = DreamActivity.newIntent(this, dream.getId());
            startActivity(intent);
        } else {
            Fragment dreamFragment = DreamFragment.newInstance(dream.getId());
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.detail_fragment_container, dreamFragment)
                    .commit();
        }
    }

    @Override
    public void onDreamUpdated(Dream dream) {
        DreamListFragment listFragment =
                (DreamListFragment) getSupportFragmentManager().findFragmentById(
                        R.id.fragment_container);
        listFragment.updateUI();
    }




}