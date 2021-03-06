package cs5254.cs.vt.edu.dreamcatcherp3.model;

import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class Dream {

    private UUID mId;
    private String mTitle;
    private Date mRevealedDate;
    private boolean mRealized;
    private boolean mDeferred;
    private List<DreamEntry> mDreamEntries;

    public Dream() {
        mId = UUID.randomUUID();
        mTitle = null;
        mRevealedDate = new Date();
        mRealized = false;
        mDeferred = false;
        mDreamEntries = new ArrayList<>();
        addDreamRevealed();
    }

    public Dream(UUID uuid) {
        mId = uuid;
        mTitle = null;
        mRevealedDate = new Date();
        mRealized = false;
        mDeferred = false;
        mDreamEntries = new ArrayList<>();
        addDreamRevealed();
    }

    // getters

    public UUID getId() {
        return mId;
    }

    public String getTitle() {

        Log.d("cancelCreate", "Dream.getTitle start");
        return mTitle;
    }

    public Date getRevealedDate() {
        return mRevealedDate;
    }

    public boolean isRealized() {
        return mRealized;
    }

    public boolean isDeferred() { return mDeferred; }

    public List<DreamEntry> getDreamEntries() {
        return mDreamEntries;
    }

    // setters

    public void setTitle(String title) {
        mTitle = title;
    }

    public void setRevealedDate(Date revealedDate) {
        mRevealedDate = revealedDate;
    }

    public void setRealized(boolean realized) {
        mRealized = realized;
    }

    public void setDeferred(boolean deferred) { mDeferred = deferred; }

    // convenience methods

    public void addComment(String text) {
        DreamEntry entry = new DreamEntry(text, new Date(),
                DreamEntryKind.COMMENT);
        mDreamEntries.add(entry);
    }

    public void addDreamRealized() {
        DreamEntry entry = new DreamEntry("Dream Realized", new Date(),
                DreamEntryKind.REALIZED);
        mDreamEntries.add(entry);
    }

    public void addDreamDeferred() {
        DreamEntry entry = new DreamEntry("Dream Deferred", new Date(),
                DreamEntryKind.DEFERRED);
        mDreamEntries.add(entry);
    }

    public void addDreamRevealed() {
        DreamEntry entry = new DreamEntry("Dream Revealed", new Date(),
                DreamEntryKind.REVEALED);
        mDreamEntries.add(entry);
    }

    // Consider consolidating further into a method
    // (for example, selectDreamRealized) that includes:
    // (1) logic related to deferred
    // (2) setting realized
    // (3) creating + adding realized dream entry

    public void removeDreamRealized() {
        Iterator<DreamEntry> iterator = mDreamEntries.iterator();
        while (iterator.hasNext()) {
            DreamEntry e = iterator.next();
            if (e.getKind() == DreamEntryKind.REALIZED) {
                iterator.remove();
                setRealized(false);
            }
        }
    }

    public void removeDreamDeferred() {
        Iterator<DreamEntry> iterator = mDreamEntries.iterator();
        while (iterator.hasNext()) {
            DreamEntry e = iterator.next();
            if (e.getKind() == DreamEntryKind.DEFERRED) {
                iterator.remove();
                setDeferred(false);
            }
        }
    }


    public void selectDreamRealized() {
        removeDreamDeferred();
        setDeferred(false);
        addDreamRealized();
        setRealized(true);
    }

    public void selectDreamDeferred() {
        removeDreamRealized();
        setRealized(false);
        addDreamDeferred();
        setDeferred(true);
    }

    public void setDate(Date date) {
        mRevealedDate = date;
    }

    public void setDreamEntries(List<DreamEntry> entries) {
        mDreamEntries = entries;
    }

    /*
    This method assumes that it will only be called if the dream is realized
    It will return Null if the dream is not realized
     */
    public Date getRealizedDate() {

        for (DreamEntry entry : mDreamEntries) {
            if (entry.getKind() == DreamEntryKind.REALIZED)
                return entry.getDate();
        }

        return null;
    }

    /*
    This method assumes that it will only be called if the dream is deferred
    It will return Null if the dream is not deferred
     */
    public Date getDeferredDate() {

        for (DreamEntry entry : mDreamEntries) {
            if (entry.getKind() == DreamEntryKind.REALIZED)
                return entry.getDate();
        }

        return null;
    }

    public String getPhotoFilename() {
        return "IMG_" + this.getId().toString() + ".jpg";
    }
}
