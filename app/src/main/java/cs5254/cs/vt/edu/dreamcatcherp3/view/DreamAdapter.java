package cs5254.cs.vt.edu.dreamcatcherp3.view;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import cs5254.cs.vt.edu.dreamcatcherp3.model.Dream;




public class DreamAdapter extends RecyclerView.Adapter<DreamHolder> {

    //Model fields
    List<Dream> mDreams;

    public DreamAdapter(List<Dream> dreams) {
        mDreams = dreams;
    }

    @Override
    public DreamHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new DreamHolder(inflater, parent);
    }

    @Override
    public void onBindViewHolder(DreamHolder holder, int position) {
        holder.bind(mDreams.get(position));
    }

    @Override
    public int getItemCount() {
        return mDreams.size();
    }

    public void setDreams(List<Dream> dreams) {
        mDreams = dreams;
    }
}

