package cs5254.cs.vt.edu.dreamcatcherp2.controller;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import java.text.DateFormat;
import java.util.List;
import java.util.UUID;

import cs5254.cs.vt.edu.dreamcatcherp2.R;
import cs5254.cs.vt.edu.dreamcatcherp2.model.Dream;
import cs5254.cs.vt.edu.dreamcatcherp2.model.DreamEntry;
import cs5254.cs.vt.edu.dreamcatcherp2.model.DreamLab;
import cs5254.cs.vt.edu.dreamcatcherp2.model.DreamEntryLab;

public class DreamFragment extends Fragment {

    private static final int REVEALED_COLOR = 0xff999f00;
    private static final int REALIZED_COLOR = 0xff008f00;
    private static final int DEFERRED_COLOR = 0xff010f99;
    private static final int COMMENT_COLOR = 0xffffd479;
    private static final String DIALOG_ADD_DREAM_ENTRY = "Dialog_Add_Dream_Entry";
    private static final String DIALOG_ADD_TITLE = "Dialog_Add_Title";
    private static final int REQUEST_COMMENT = 0; //TODO update number
    private static String ARG_DREAM_ID = "dream_id";

    // model fields
    private Dream mDream;

    // view fields
    private EditText mTitleField;
    private CheckBox mRealizedCheckBox;
    private CheckBox mDeferredCheckBox;
    private Button mEntryButton0;
    private Button mEntryButton1;
    private Button mEntryButton2;
    private Button mEntryButton3;
    private Button mEntryButton4;
    private FloatingActionButton mAddCommentFAB;

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
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_dream, container, false);

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
            refreshView();
        });

        mEntryButton0 = view.findViewById(R.id.dream_entry_0);
        mEntryButton0.setEnabled(false);
        mEntryButton1 = view.findViewById(R.id.dream_entry_1);
        mEntryButton1.setEnabled(false);
        mEntryButton2 = view.findViewById(R.id.dream_entry_2);
        mEntryButton2.setEnabled(false);
        mEntryButton3 = view.findViewById(R.id.dream_entry_3);
        mEntryButton3.setEnabled(false);
        mEntryButton4 = view.findViewById(R.id.dream_entry_4);
        mEntryButton4.setEnabled(false);

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

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode != Activity.RESULT_OK) {return;}

        if (requestCode == REQUEST_COMMENT) {
            String comment = (String) intent.getSerializableExtra(
                    AddDreamEntryFragment.EXTRA_COMMENT);
            mDream.addComment(comment);
            refreshEntryButtons();
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


    private void refreshView() {
        if (mDream.getTitle() != null) {
            mTitleField.setText(mDream.getTitle());
        }
        mRealizedCheckBox.setChecked(mDream.isRealized());
        mDeferredCheckBox.setChecked(mDream.isDeferred());

        refreshCheckboxEnabled();

        List<DreamEntry> entries = mDream.getDreamEntries();
        refreshEntryButtons();

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

    private void refreshEntryButtons() {
        refreshEntryButton(mEntryButton0, 0);
        refreshEntryButton(mEntryButton1, 1);
        refreshEntryButton(mEntryButton2, 2);
        refreshEntryButton(mEntryButton3, 3);
        refreshEntryButton(mEntryButton4, 4);
    }

    private void refreshEntryButton(Button button, int position) {
        // position should not be greater than the last position
        int lastPosition = mDream.getDreamEntries().size() - 1;
        if (position > lastPosition) {
            button.setVisibility(View.GONE);
            return;
        }

        button.setVisibility(View.VISIBLE);
        DreamEntry entry = mDream.getDreamEntries().get(position);

        //Only show the 5 most recent dream entries
        if (mDream.getDreamEntries().size() > 5)
        {
            int newPosition = mDream.getDreamEntries().size() - 5 + position;
            entry = mDream.getDreamEntries().get(newPosition);
        }


        // set style
        // set text
        switch (entry.getKind()) {
            case REVEALED:
                setRevealedStyle(button);
                button.setText(entry.getText());
                break;
            case DEFERRED:
                setDeferredStyle(button);
                button.setText(entry.getText());
                break;
            case REALIZED:
                setRealizedStyle(button);
                button.setText(entry.getText());
                break;
            case COMMENT:
                setCommentStyle(button);
                String text = entry.getText();
                DateFormat dateFormat = android.text.format.DateFormat
                        .getMediumDateFormat(getContext());
                button.setText(text + " (" + dateFormat.format(entry.getDate()) + ")");
        }
    }

    private void setRevealedStyle(Button button) {
        button.getBackground().setColorFilter(REVEALED_COLOR, PorterDuff.Mode.MULTIPLY);
        button.setTextColor(Color.WHITE);
    }

    private void setRealizedStyle(Button button) {
        button.getBackground().setColorFilter(REALIZED_COLOR, PorterDuff.Mode.MULTIPLY);
        button.setTextColor(Color.WHITE);
    }

    private void setDeferredStyle(Button button) {
        button.getBackground().setColorFilter(DEFERRED_COLOR, PorterDuff.Mode.MULTIPLY);
        button.setTextColor(Color.WHITE);
    }

    private void setCommentStyle(Button button) {
        button.getBackground().setColorFilter(COMMENT_COLOR, PorterDuff.Mode.MULTIPLY);
        button.setTextColor(Color.BLACK);
    }

}

