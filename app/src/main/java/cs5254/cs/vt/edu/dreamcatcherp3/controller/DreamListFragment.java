package cs5254.cs.vt.edu.dreamcatcherp3.controller;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import cs5254.cs.vt.edu.dreamcatcherp3.R;
import cs5254.cs.vt.edu.dreamcatcherp3.model.Dream;
import cs5254.cs.vt.edu.dreamcatcherp3.model.DreamLab;
import cs5254.cs.vt.edu.dreamcatcherp3.view.DreamAdapter;

public class DreamListFragment extends Fragment {

    //view field
    private RecyclerView mDreamRecyclerView;
    private DreamAdapter mDreamAdapter;

    //model field
    private DreamLab mDreamLab;

    public static Callbacks mCallbacks;

    public interface Callbacks {
        void onDreamSelected(Dream dream);
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

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        Log.d("install", "DreamListFragment.onCreate start");
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dream_list, container, false);

        mDreamRecyclerView = view.findViewById(R.id.dream_recycler_view);
        mDreamRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_dream_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.new_dream:
                Dream dream = new Dream();
                //Original
                //DreamLab.getInstance(getActivity()).addDream(dream);

                // Don't add Dream to DB here, otherwise dream will still get added to the db even
                // if you hit back
                //Modified
//                Intent intent = DreamActivity
//                        .newIntent(getActivity(), dream.getId());
//                startActivity(intent);

                //Modified again, this time for using Callbacks
                //This may break the previous modification
                updateUI();
                mCallbacks.onDreamSelected(dream);

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void updateUI() {

        mDreamLab = DreamLab.getInstance(getActivity());
        List<Dream> dreams = mDreamLab.getDreams();

        if (mDreamAdapter == null) {
            mDreamAdapter = new DreamAdapter(mDreamLab.getDreams());
            mDreamRecyclerView.setAdapter(mDreamAdapter);
        }
        else {
            mDreamAdapter.setDreams(dreams);
            mDreamAdapter.notifyDataSetChanged();
        }

    }

    public Callbacks getCallbacks() {
        return mCallbacks;
    }
}
