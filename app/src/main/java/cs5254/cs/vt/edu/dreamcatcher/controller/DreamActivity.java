package cs5254.cs.vt.edu.dreamcatcher.controller;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.UUID;

import cs5254.cs.vt.edu.dreamcatcher.R;

public class DreamActivity extends SingleFragmentActivity {

    private static final String EXTRA_DREAM_ID = "dreamcatcher.dream_id";

    @Override
    protected Fragment createFragment() {
        UUID dreamId = (UUID) getIntent().getSerializableExtra(EXTRA_DREAM_ID);
        return DreamFragment.newInstance(dreamId);
    }

    public static Intent newIntent(Context context, UUID dreamId) {
        Intent intent = new Intent(context, DreamActivity.class);
        intent.putExtra(EXTRA_DREAM_ID, dreamId);
        return intent;
    }

}
