package cs5254.cs.vt.edu.dreamcatcherp3.controller;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import cs5254.cs.vt.edu.dreamcatcherp3.R;

public class NeedTitleFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new android.support.v7.app.AlertDialog.Builder(getActivity())
                //.setView(view)
                .setTitle(R.string.need_title_title)
                .setMessage(R.string.need_title_message)
                .setPositiveButton(
                        android.R.string.ok,
                        (dialog, which) -> {})
//                .setNegativeButton(
//                        android.R.string.cancel,
//                        (dialog, which) -> {})
                .create();
    }
}
