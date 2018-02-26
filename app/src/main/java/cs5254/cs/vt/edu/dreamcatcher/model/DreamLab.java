package cs5254.cs.vt.edu.dreamcatcher.model;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DreamLab {

    private static DreamLab sDreamLab;
    private List<Dream> mDreams;

    public static DreamLab getInstance(Context context) {
        if (sDreamLab == null) { sDreamLab = new DreamLab(context); }
        return sDreamLab;
    }

    private DreamLab(Context context) {

        mDreams = new ArrayList<>();

        Dream dream0 = new Dream();
        dream0.setTitle("My First Dream");
        dream0.addComment("Comment 1");
        dream0.addComment("Comment 2");
        dream0.addComment("Comment 3");
        dream0.selectDreamRealized();
        //dream0.setRealized(true);
        mDreams.add(dream0);

        Dream dream1 = new Dream();
        dream1.setTitle("My Second Dream");
        dream1.addComment("Comment 1");
        dream1.addComment("Comment 2");
        dream1.selectDreamDeferred();
        dream1.addComment("Comment 3");
        //dream0.setRealized(true);
        mDreams.add(dream1);

        for (int i = 2; i < 20; i++) {
            Dream dream = new Dream();
            dream.setTitle("Dream #" + i);
            if (i % 3 == 0) {
                dream.selectDreamRealized();
            }
            if (i % 2 == 0) {
                dream.selectDreamDeferred();
            }
            //dream.setRealized(i % 2 == 0); // Every other one
            mDreams.add(dream);
        }
    }

    public List<Dream> getDreams() { return mDreams; }

    public Dream getDream(UUID id) {
        for (Dream dream : mDreams) {
            if (dream.getId().equals(id)) { return dream; }
        }
        return null;
    }
}