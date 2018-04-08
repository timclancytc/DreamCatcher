package cs5254.cs.vt.edu.dreamcatcherp3.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import cs5254.cs.vt.edu.dreamcatcherp3.database.DreamBaseHelper;
import cs5254.cs.vt.edu.dreamcatcherp3.database.DreamDbSchema;

public class DreamLab {

    private static DreamLab sDreamLab;

    private Context mContext;
    private SQLiteDatabase mDatabase;


    public static DreamLab getInstance(Context context) {
        if (sDreamLab == null) { sDreamLab = new DreamLab(context); }
        return sDreamLab;
    }

    private DreamLab(Context context) {

        mContext = context.getApplicationContext();
        mDatabase = new DreamBaseHelper(mContext).getWritableDatabase();

        //Create one dream with 5 dream entries for testing convenience
        List<Dream> existingDreams = getDreams();
        if (existingDreams.size() == 0) {
            Dream dream0 = new Dream();
            dream0.setTitle("5 Entry Dream");
            dream0.addComment("Comment 1");
            dream0.addComment("Comment 2");
            dream0.addComment("Comment 3");
            dream0.addComment("Comment 4");
            dream0.selectDreamRealized();
            Log.d("install", "DreamLab.DreamLab before addDream");
            addDream(dream0);

            Log.d("install", "DreamLab.DreamLab end");
        }
    }


    public void addDream(Dream dream) {
        ContentValues values = getDreamValues(dream);
        mDatabase.insert(DreamDbSchema.DreamTable.NAME, null, values);
        for (DreamEntry entry : dream.getDreamEntries()) {
            DreamEntryLab.getInstance(mContext).addDreamEntry(entry, dream);
        }
    }

    public void updateDream(Dream dream) {
        String uuidString = dream.getId().toString();
        ContentValues values = getDreamValues(dream);
        mDatabase.update(DreamDbSchema.DreamTable.NAME, values,
                    DreamDbSchema.DreamTable.Cols.UUID + " = ?",
                    new String[] { uuidString });
    }

    private static ContentValues getDreamValues(Dream dream) {
        ContentValues values = new ContentValues();
        values.put(DreamDbSchema.DreamTable.Cols.UUID, dream.getId().toString());
        values.put(DreamDbSchema.DreamTable.Cols.TITLE, dream.getTitle());
        values.put(DreamDbSchema.DreamTable.Cols.DATE, dream.getRevealedDate().getTime());
        values.put(DreamDbSchema.DreamTable.Cols.DEFERRED, dream.isDeferred() ? 1 : 0);
        values.put(DreamDbSchema.DreamTable.Cols.REALIZED, dream.isRealized() ? 1: 0);

        return values;
    }

    private DreamCursorWrapper queryDreams(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                DreamDbSchema.DreamTable.NAME,
                null, // columns - null selects all columns
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new DreamCursorWrapper(cursor);
    }

    public List<Dream> getDreams() {
        List<Dream> dreams = new ArrayList<>();
        DreamCursorWrapper cursor = queryDreams(null, null);
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                dreams.add(cursor.getDream());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return dreams;
    }

    public Dream getDream(UUID id) {
        DreamCursorWrapper cursor = queryDreams(
                DreamDbSchema.DreamTable.Cols.UUID + " = ?",
                new String[]{ id.toString() }
        );
        try {
            if (cursor.getCount() == 0) { return null; }
            cursor.moveToFirst();
            Dream dream = cursor.getDream();
            List<DreamEntry> entries =
                    DreamEntryLab.getInstance(mContext).getDreamEntries(dream);
            dream.setDreamEntries(entries);
            return dream;
        } finally {
            cursor.close();
        }
    }

    public File getPhotoFile(Dream dream) {
        File filesDir = mContext.getFilesDir();
        return new File(filesDir, dream.getPhotoFilename());
    }


}