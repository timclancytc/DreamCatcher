package cs5254.cs.vt.edu.dreamcatcherp3.controller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import java.io.File;
import java.util.List;
import java.util.UUID;

import cs5254.cs.vt.edu.dreamcatcherp3.R;
import cs5254.cs.vt.edu.dreamcatcherp3.model.Dream;
import cs5254.cs.vt.edu.dreamcatcherp3.model.DreamEntry;
import cs5254.cs.vt.edu.dreamcatcherp3.model.DreamLab;
import cs5254.cs.vt.edu.dreamcatcherp3.model.DreamEntryLab;
import cs5254.cs.vt.edu.dreamcatcherp3.view.EntryAdapter;

public class DreamFragment extends Fragment {

    private static final String DIALOG_ADD_DREAM_ENTRY = "Dialog_Add_Dream_Entry";
    private static final String DIALOG_ADD_TITLE = "Dialog_Add_Title";
    private static final String DIALOG_DISPLAY_PHOTO = "Dialog_Display_Photo";
    private static final int REQUEST_COMMENT = 0;
    private static final int REQUEST_PHOTO = 2;
    private static final int DISPLAY_PHOTO = 3;
    private static String ARG_DREAM_ID = "dream_id";

    // model fields
    private Dream mDream;
    private File mPhotoFile;

    // view fields
    private EditText mTitleField;
    private CheckBox mRealizedCheckBox;
    private CheckBox mDeferredCheckBox;

    private FloatingActionButton mAddCommentFAB;
    private ImageButton mPhotoButton;
    private ImageView mPhotoView;

    //Recycler view fields
    private RecyclerView mEntryRecyclerView;
    private EntryAdapter mEntryAdapter;

    //Callbacks
    private Callbacks mCallbacks;

    public interface Callbacks {
        void onDreamUpdated(Dream dream);
    }



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallbacks = (Callbacks) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }

    public static DreamFragment newInstance(UUID dreamId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_DREAM_ID, dreamId);
        DreamFragment fragment = new DreamFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        UUID dreamId = (UUID) getArguments().getSerializable(ARG_DREAM_ID);
        mDream = DreamLab.getInstance(getActivity()).getDream(dreamId);
        if (mDream == null) {
            mDream = new Dream();
        }

        mPhotoFile = DreamLab.getInstance(getActivity()).getPhotoFile(mDream);

        setHasOptionsMenu(true);
    }

        @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (mTitleField.getText().toString() == null ||
                        mTitleField.getText().toString().equals("")) {
                    FragmentManager manager = DreamFragment.this.getFragmentManager();
                    NeedTitleFragment dialog = new NeedTitleFragment();
//                    dialog.setTargetFragment(
//                            DreamFragment.this, REQUEST_COMMENT);
                    dialog.show(manager, DIALOG_ADD_TITLE);
                    return true;
                }
                return super.onOptionsItemSelected(item);
            case R.id.share_dream:
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain");
                i.putExtra(Intent.EXTRA_TEXT, getDreamShare());
                i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.dream_share_subject));
                i = Intent.createChooser(i, getString(R.string.dream_share_send));
                startActivity(i);
            case R.id.photograph_dream:
                final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                Uri uri = FileProvider.getUriForFile(getActivity(),
                        "cs5254.cs.vt.edu.dreamcatcherp3.fileprovider",
                        mPhotoFile);
                captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);

                List<ResolveInfo> cameraActivities = getActivity()
                        .getPackageManager().queryIntentActivities(captureImage,
                                PackageManager.MATCH_DEFAULT_ONLY);

                for (ResolveInfo activity : cameraActivities) {
                    getActivity().grantUriPermission(activity.activityInfo.packageName,
                            uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                }

                startActivityForResult(captureImage, REQUEST_PHOTO);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dream, container, false);

        mEntryRecyclerView = view.findViewById(R.id.entry_recycler_view);
        mEntryRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        // initialize view fields

        mTitleField = view.findViewById(R.id.dream_title);
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // do nothing
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                mDream.setTitle(charSequence.toString());

                if (DreamLab.getInstance(getActivity()).getDream(mDream.getId()) == null &&
                        mDream.getTitle() != null &&
                        !mDream.getTitle().equals("")) {
                    DreamLab.getInstance(getActivity()).addDream(mDream);
                }

                updateDream();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // do nothing
            }
        });

        mRealizedCheckBox = view.findViewById(R.id.dream_realized);
        mRealizedCheckBox.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                // add the DR message
                if (!mDream.isRealized()) { mDream.selectDreamRealized(); }
            } else {
                // remove the DR message
                if (mDream.isRealized()) { mDream.removeDreamRealized(); }
            }
            mDream.setRealized(b);
            updateDream();
            refreshView();
        });

        mDeferredCheckBox = view.findViewById(R.id.dream_deferred);
        mDeferredCheckBox.setOnCheckedChangeListener((compoundButton, c) -> {
            if (c) {
                //Add deferred message
                if (!mDream.isDeferred()) {mDream.selectDreamDeferred();}
            } else {
                //Remove deferred message
                if (mDream.isDeferred()) {mDream.removeDreamDeferred();}
            }
            mDream.setDeferred(c);
            updateDream();
            refreshView();
        });

        mAddCommentFAB = view.findViewById(R.id.add_comment_fab);
        mAddCommentFAB.setOnClickListener(
                v -> {
                    FragmentManager manager = DreamFragment.this.getFragmentManager();
                    AddDreamEntryFragment dialog = new AddDreamEntryFragment();
                    dialog.setTargetFragment(
                            DreamFragment.this, REQUEST_COMMENT);
                    dialog.show(manager, DIALOG_ADD_DREAM_ENTRY);
                    });

        refreshView();

        PackageManager packageManager = getActivity().getPackageManager();

//        mPhotoButton = (ImageButton) view.findViewById(R.id.dream_camera);
//        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//
//        boolean canTakePhoto = mPhotoFile != null &&
//                captureImage.resolveActivity(packageManager) != null;
//        mPhotoButton.setEnabled(canTakePhoto);
//
//        mPhotoButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Uri uri = FileProvider.getUriForFile(getActivity(),
//                        "cs5254.cs.vt.edu.dreamcatcherp3.fileprovider",
//                        mPhotoFile);
//                captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);
//
//                List<ResolveInfo> cameraActivities = getActivity()
//                        .getPackageManager().queryIntentActivities(captureImage,
//                                PackageManager.MATCH_DEFAULT_ONLY);
//
//                for (ResolveInfo activity : cameraActivities) {
//                    getActivity().grantUriPermission(activity.activityInfo.packageName,
//                            uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//                }
//
//                startActivityForResult(captureImage, REQUEST_PHOTO);
//            }
//        });


        mPhotoView = view.findViewById(R.id.dream_photo);
        mPhotoView.setOnClickListener(
                v -> {
                    if (mPhotoFile == null || !mPhotoFile.exists()) {
                        return;
                    }
                    FragmentManager manager = DreamFragment.this.getFragmentManager();
                    DisplayPhotoFragment dialog = DisplayPhotoFragment.newInstance(mPhotoFile.getPath());
                    dialog.show(manager, DIALOG_DISPLAY_PHOTO);
                });

        updatePhotoView();
        updateUI();

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode != Activity.RESULT_OK) {return;}

        if (requestCode == REQUEST_COMMENT) {
            String comment = (String) intent.getSerializableExtra(
                    AddDreamEntryFragment.EXTRA_COMMENT);
            mDream.addComment(comment);
            updateDream();
            updateUI();
            //refreshEntryButtons();
        } else if (requestCode == REQUEST_PHOTO) {
             Uri uri = FileProvider.getUriForFile(getActivity(),
                    "cs5254.cs.vt.edu.dreamcatcherp3.fileprovider",
                    mPhotoFile);

            getActivity().revokeUriPermission(uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

            updateDream();
            updatePhotoView();
            updateUI();
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        // If the dream exists, update the entries
        // If the dream exists and the title is valid update the dream
        // If the dream does not exist and the title is valid, create the dream
        // (which will create entries)
        // else do nothing after call to super

        if (DreamLab.getInstance(getActivity()).getDream(mDream.getId()) != null) {
            DreamEntryLab.getInstance(getActivity()).updateDreamEntries(mDream);

            if (mDream.getTitle() != null && !mDream.getTitle().equals("")) {
                DreamLab.getInstance(getActivity()).updateDream(mDream);
            }
        }
        if (DreamLab.getInstance(getActivity()).getDream(mDream.getId()) == null &&
                mDream.getTitle() != null &&
                !mDream.getTitle().equals("")) {
            DreamLab.getInstance(getActivity()).addDream(mDream);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_dream, menu);

        PackageManager packageManager = getActivity().getPackageManager();
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        boolean canTakePhoto = mPhotoFile != null &&
                captureImage.resolveActivity(packageManager) != null;

        menu.findItem(R.id.photograph_dream).setEnabled(canTakePhoto);
    }



    private void refreshView() {
        if (mDream.getTitle() != null) {
            mTitleField.setText(mDream.getTitle());
        }
        mRealizedCheckBox.setChecked(mDream.isRealized());
        mDeferredCheckBox.setChecked(mDream.isDeferred());

        refreshCheckboxEnabled();

        List<DreamEntry> entries = mDream.getDreamEntries();
        //refreshEntryButtons();
        updateUI();

//        for (DreamEntry e : entries) {
//            Log.d("refreshView", e.getText());
//        }
    }

    private void refreshCheckboxEnabled() {
        mRealizedCheckBox.setEnabled(true);
        mDeferredCheckBox.setEnabled(true);
        if (mDream.isDeferred()) {
            mRealizedCheckBox.setEnabled(false);
        }
        if (mDream.isRealized()) {
            mDeferredCheckBox.setEnabled(false);
        }
    }

//    private void refreshEntryButtons() {
////        refreshEntryButton(mEntryButton0, 0);
////        refreshEntryButton(mEntryButton1, 1);
////        refreshEntryButton(mEntryButton2, 2);
////        refreshEntryButton(mEntryButton3, 3);
////        refreshEntryButton(mEntryButton4, 4);
//
//        //TODO implement with Recycler View
//    }

//    private void refreshEntryButton(Button button, int position) {
//        // position should not be greater than the last position
//        int lastPosition = mDream.getDreamEntries().size() - 1;
//        if (position > lastPosition) {
//            button.setVisibility(View.GONE);
//            return;
//        }
//
//        button.setVisibility(View.VISIBLE);
//        DreamEntry entry = mDream.getDreamEntries().get(position);
//
//        //Only show the 5 most recent dream entries
//        if (mDream.getDreamEntries().size() > 5)
//        {
//            int newPosition = mDream.getDreamEntries().size() - 5 + position;
//            entry = mDream.getDreamEntries().get(newPosition);
//        }
//    }

    private String getDreamShare() {
        String dateFormat = "EEE, MMM dd";
        String dateRevealedString = android.text.format.DateFormat.format(
                dateFormat, mDream.getRevealedDate()).toString();

        String dreamFateString = getString(R.string.dream_share_active);

        if (mDream.isRealized()) {
            dreamFateString = getString(
                    R.string.dream_share_realized, android.text.format.DateFormat.format(
                            dateFormat, mDream.getRealizedDate()).toString());
        } else if (mDream.isDeferred()){
            dreamFateString = getString(
                    R.string.dream_share_deferred, android.text.format.DateFormat.format(
                            dateFormat, mDream.getDeferredDate()).toString());
        }



        String shareDream = getString(R.string.dream_share,
                mDream.getTitle(), dateRevealedString, dreamFateString);

        return shareDream;
    }

    private void updatePhotoView() {
        if (mPhotoFile == null || !mPhotoFile.exists()) {
            mPhotoView.setImageDrawable(null);
        } else {
            Bitmap bitmap = PictureUtils.getScaledBitmap(mPhotoFile.getPath(), getActivity());
            mPhotoView.setImageBitmap(bitmap);
        }
    }

    private void updateDream() {
        DreamLab.getInstance(getActivity()).updateDream(mDream);
        mCallbacks.onDreamUpdated(mDream);
    }

    public void updateUI() {

        List<DreamEntry> entries = mDream.getDreamEntries();

        if (mEntryAdapter == null) {
            mEntryAdapter = new EntryAdapter(entries);
            mEntryRecyclerView.setAdapter(mEntryAdapter);
        }
        else {
            mEntryAdapter.setEntries(entries);
            mEntryAdapter.notifyDataSetChanged();
        }

    }

}

