package cs5254.cs.vt.edu.dreamcatcherp2.model;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import cs5254.cs.vt.edu.dreamcatcherp2.database.DreamBaseHelper;
import cs5254.cs.vt.edu.dreamcatcherp2.database.DreamDbSchema;

public class DreamEntryLab {

    private static DreamEntryLab sDreamEntryLab;

    //database additions
    private Context mContext;
    private SQLiteDatabase mDatabase;


    public static DreamEntryLab getInstance(Context context) {
        if (sDreamEntryLab == null) { sDreamEntryLab = new DreamEntryLab(context); }
        return sDreamEntryLab;
    }

    private DreamEntryLab(Context context) {

        mContext = context.getApplicationContext();
        mDatabase = new DreamBaseHelper(mContext).getWritableDatabase();

    }

    public void addDreamEntry(DreamEntry entry, Dream dream) {
        ContentValues values = getDreamEntryValues(entry, dream);
        mDatabase.insert(DreamDbSchema.DreamEntryTable.NAME, null, values);

    }

    private ContentValues getDreamEntryValues(DreamEntry entry, Dream dream) {
        ContentValues values = new ContentValues();
        values.put(DreamDbSchema.DreamEntryTable.Cols.TEXT, entry.getText());
        values.put(DreamDbSchema.DreamEntryTable.Cols.DATE, entry.getDate().getTime());
        values.put(DreamDbSchema.DreamEntryTable.Cols.KIND, entry.getKind().toString());
        values.put(DreamDbSchema.DreamEntryTable.Cols.UUID, dream.getId().toString());

        return values;
    }

    public List<DreamEntry> getDreamEntries(Dream dream) {
        List<DreamEntry> dreamEntries = new ArrayList<>();
        String whereClause = DreamDbSchema.DreamEntryTable.Cols.UUID + "=?";
        DreamEntryCursorWrapper cursor = queryDreamEntries(
                whereClause,
                new String[]{dream.getId().toString()});
        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                dreamEntries.add(cursor.getDreamEntry());
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return dreamEntries;
    }

    private DreamEntryCursorWrapper queryDreamEntries(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                DreamDbSchema.DreamEntryTable.NAME,
                null, // columns - null selects all columns
                whereClause,
                whereArgs,
                null,
                null,
                null
        );
        return new DreamEntryCursorWrapper(cursor);
    }

    public void updateDreamEntries(Dream dream) {
        String uuidString = dream.getId().toString();

        //Delete existing rows
        String whereClause = DreamDbSchema.DreamEntryTable.Cols.UUID + "=?";
        String[] whereArgs = new String[] { String.valueOf(dream.getId()) };
        mDatabase.delete(DreamDbSchema.DreamEntryTable.NAME, whereClause, whereArgs);

        //Add new Rows
        for (DreamEntry entry : dream.getDreamEntries()) {
            DreamEntryLab.getInstance(mContext).addDreamEntry(entry, dream);
        }
    }
}
