package cs5254.cs.vt.edu.dreamcatcherp3.controller;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import java.io.File;

import cs5254.cs.vt.edu.dreamcatcherp3.R;

public class DisplayPhotoFragment extends DialogFragment {

    private static final String ARG_PHOTO_FILE = "photoFile";

    public static DisplayPhotoFragment newInstance(String photoPath) {
        Log.d("PhotoDialog", "DisplayPhotoFragment.newInstance");
        Bundle args = new Bundle();
        args.putSerializable(ARG_PHOTO_FILE, photoPath);

        DisplayPhotoFragment fragment = new DisplayPhotoFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.d("Photo Dialog", "DisplayPhotoFragment.onCreateDialog");

        View view = LayoutInflater.from(getActivity()).inflate(
                R.layout.fragment_display_photo, null);

        ImageView image = view.findViewById(R.id.dialog_photo_view);

        Bitmap bitmap = PictureUtils.getScaledBitmap((String) getArguments().getSerializable(ARG_PHOTO_FILE), getActivity());
        image.setImageBitmap(bitmap);

        Dialog dialog = new AlertDialog.Builder(getActivity())
                .setView(view)
                .setTitle(R.string.display_photo_title)
                .create();

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.requestWindowFeature(Window.FEATURE_SWIPE_TO_DISMISS);

        return dialog;
    }
}
