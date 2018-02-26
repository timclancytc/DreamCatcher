package cs5254.cs.vt.edu.dreamcatcher.view;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import cs5254.cs.vt.edu.dreamcatcher.R;
import cs5254.cs.vt.edu.dreamcatcher.controller.DreamActivity;
import cs5254.cs.vt.edu.dreamcatcher.model.Dream;


public class DreamHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    //model field
    private Dream mDream;

    //view field
    private TextView mTitleTextView;
    private TextView mDateTextView;
    private ImageView mDreamRealizedView;
    private ImageView mDreamDeferredView;

    public DreamHolder(LayoutInflater inflater, ViewGroup parent) {
        super(inflater.inflate(R.layout.list_item_dream, parent, false));
        itemView.setOnClickListener(this);
        mTitleTextView = itemView.findViewById(R.id.dream_title);
        mDateTextView = itemView.findViewById(R.id.dream_date);
        mDreamRealizedView = itemView.findViewById(R.id.dream_realized_view);
        mDreamDeferredView = itemView.findViewById(R.id.dream_deferred_view);
    }

    public void bind(Dream Dream) {
        mDream = Dream;
        mTitleTextView.setText(mDream.getTitle());
        mDateTextView.setText(mDream.getRevealedDate().toString());
        mDreamRealizedView.setVisibility(mDream.isRealized() ? View.VISIBLE : View.GONE);
        mDreamDeferredView.setVisibility(mDream.isDeferred() ? View.VISIBLE : View.GONE);

    }

    @Override
    public void onClick(View v) {
//        Toast.makeText(v.getContext(), mDream.getTitle() + "clicked!",
//                Toast.LENGTH_SHORT).show();
        Context context = v.getContext();
        Intent intent = DreamActivity.newIntent(context, mDream.getId());
        context.startActivity(intent);


    }
}
