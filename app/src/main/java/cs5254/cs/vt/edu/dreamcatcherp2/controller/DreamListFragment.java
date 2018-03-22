package cs5254.cs.vt.edu.dreamcatcherp2.controller;

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

import cs5254.cs.vt.edu.dreamcatcherp2.R;
import cs5254.cs.vt.edu.dreamcatcherp2.model.Dream;
import cs5254.cs.vt.edu.dreamcatcherp2.model.DreamLab;
import cs5254.cs.vt.edu.dreamcatcherp2.view.DreamAdapter;

public class DreamListFragment extends Fragment {

    //view field
    private RecyclerView mDreamRecyclerView;
    private DreamAdapter mDreamAdapter;

    //model field
    private DreamLab mDreamLab;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
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
                // Don't add Dream to DB here, otherwise dream will still get added to the db even
                // if you hit back
                //DreamLab.getInstance(getActivity()).addDream(dream);
                Intent intent = DreamActivity
                        .newIntent(getActivity(), dream.getId());
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    private void updateUI() {

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
}
