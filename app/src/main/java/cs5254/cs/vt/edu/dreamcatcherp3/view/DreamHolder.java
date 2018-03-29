package cs5254.cs.vt.edu.dreamcatcherp3.view;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DateFormat;

import cs5254.cs.vt.edu.dreamcatcherp3.R;
import cs5254.cs.vt.edu.dreamcatcherp3.controller.DreamActivity;
import cs5254.cs.vt.edu.dreamcatcherp3.model.Dream;


public class DreamHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

    //model field
    private Dream mDream;

    //view field
    private TextView mTitleTextView;
    private TextView mDateTextView;
    private ImageView mDreamRealizedView;
    private ImageView mDreamDeferredView;

    //context
    private Context context;

    public DreamHolder(LayoutInflater inflater, ViewGroup parent) {
        super(inflater.inflate(R.layout.list_item_dream, parent, false));
        itemView.setOnClickListener(this);
        mTitleTextView = itemView.findViewById(R.id.dream_title);
        mDateTextView = itemView.findViewById(R.id.dream_date);
        mDreamRealizedView = itemView.findViewById(R.id.dream_realized_view);
        mDreamDeferredView = itemView.findViewById(R.id.dream_deferred_view);
        context = parent.getContext();
    }

    public void bind(Dream dream) {
        mDream = dream;
        mTitleTextView.setText(mDream.getTitle());
        DateFormat dateFormat = android.text.format.DateFormat
                .getMediumDateFormat(context);
        mDateTextView.setText(dateFormat.format(mDream.getRevealedDate()));
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
