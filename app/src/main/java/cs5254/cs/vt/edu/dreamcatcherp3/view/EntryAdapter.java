package cs5254.cs.vt.edu.dreamcatcherp3.view;


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

import cs5254.cs.vt.edu.dreamcatcherp3.model.Dream;
import cs5254.cs.vt.edu.dreamcatcherp3.model.DreamEntry;

public class EntryAdapter extends RecyclerView.Adapter<EntryHolder> {

    //Model fields
    List<DreamEntry> mEntries;

    public EntryAdapter(List<DreamEntry> entries) {
        mEntries = entries;
    }

    @Override
    public EntryHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new EntryHolder(inflater, parent);
    }

    @Override
    public void onBindViewHolder(EntryHolder holder, int position) {
        holder.bind(mEntries.get(position));
    }

    @Override
    public int getItemCount() {
        return mEntries.size();
    }

    public void setEntries(List<DreamEntry> entries) {
        mEntries = entries;
    }
}
