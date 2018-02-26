package cs5254.cs.vt.edu.dreamcatcher.controller;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import cs5254.cs.vt.edu.dreamcatcher.R;
import cs5254.cs.vt.edu.dreamcatcher.model.DreamLab;
import cs5254.cs.vt.edu.dreamcatcher.view.DreamAdapter;

public class DreamListFragment extends Fragment {

    //view field
    private RecyclerView mDreamRecyclerView;
    private DreamAdapter mDreamAdapter;

    //model field
    private DreamLab mDreamLab = DreamLab.getInstance(getActivity());;


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

    private void updateUI() {
        if (mDreamAdapter == null) {
            mDreamAdapter = new DreamAdapter(mDreamLab.getDreams());
            mDreamRecyclerView.setAdapter(mDreamAdapter);
        }
        else {
            mDreamAdapter.notifyDataSetChanged();
        }

    }
}
