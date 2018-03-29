package cs5254.cs.vt.edu.dreamcatcherp3.model;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.Date;

import cs5254.cs.vt.edu.dreamcatcherp3.database.DreamDbSchema;


class DreamEntryCursorWrapper extends CursorWrapper {
    public DreamEntryCursorWrapper(Cursor cursor) {
        super (cursor);
    }

    public DreamEntry getDreamEntry() {
        String text = getString(getColumnIndex(DreamDbSchema.DreamEntryTable.Cols.TEXT));
        long date = getLong(getColumnIndex(DreamDbSchema.DreamEntryTable.Cols.DATE));
        String kind = getString(getColumnIndex(DreamDbSchema.DreamEntryTable.Cols.KIND));

        return new DreamEntry(text, new Date(date), DreamEntryKind.valueOf(kind));

    }
}
