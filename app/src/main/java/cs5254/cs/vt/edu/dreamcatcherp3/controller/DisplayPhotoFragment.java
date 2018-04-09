package cs5254.cs.vt.edu.dreamcatcherp3.controller;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

import cs5254.cs.vt.edu.dreamcatcherp3.R;

public class DisplayPhotoFragment extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new android.support.v7.app.AlertDialog.Builder(getActivity())
                //.setView(view)
                .setTitle(R.string.display_photo_title)
                .setMessage(R.string.need_title_message)
                .setPositiveButton(
                        android.R.string.ok,
                        (dialog, which) -> {})
                .create()
                .set;
    }
}
