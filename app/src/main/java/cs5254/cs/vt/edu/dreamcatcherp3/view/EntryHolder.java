package cs5254.cs.vt.edu.dreamcatcherp3.view;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.text.DateFormat;

import cs5254.cs.vt.edu.dreamcatcherp3.R;
import cs5254.cs.vt.edu.dreamcatcherp3.model.DreamEntry;

public class EntryHolder  extends RecyclerView.ViewHolder {

    private static final int REVEALED_COLOR = 0xff999f00;
    private static final int REALIZED_COLOR = 0xff008f00;
    private static final int DEFERRED_COLOR = 0xff010f99;
    private static final int COMMENT_COLOR = 0xffffd479;

    private DreamEntry mEntry;
    private Button mButton;

    public EntryHolder(LayoutInflater inflater, ViewGroup parent){
        super(inflater.inflate(R.layout.list_item_entry, parent, false));
        mButton = itemView.findViewById(R.id.entry_button);
    }

    public void bind(DreamEntry entry) {
        mEntry = entry;

        switch (entry.getKind()) {
            case REVEALED:
                setRevealedStyle(mButton);
                mButton.setText(mEntry.getText());
                break;
            case DEFERRED:
                setDeferredStyle(mButton);
                mButton.setText(mEntry.getText());
                break;
            case REALIZED:
                setRealizedStyle(mButton);
                mButton.setText(mEntry.getText());
                break;
            case COMMENT:
                setCommentStyle(mButton);
                String text = mEntry.getText();
                DateFormat dateFormat = android.text.format.DateFormat
                        .getMediumDateFormat(itemView.getContext());

                String fullText = text + " (" + dateFormat.format(mEntry.getDate()) + ")";
                Spannable spannable = new SpannableString(fullText);
                int start = text.length();
                int end = fullText.length();
                spannable.setSpan(
                        new ForegroundColorSpan(Color.GRAY),
                        start,
                        end,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                );

                mButton.setText(spannable);
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
