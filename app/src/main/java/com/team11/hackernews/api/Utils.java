package com.team11.hackernews.api;

import com.firebase.client.Firebase;

public class Utils {

    private static Firebase mFirebase;

    public static Firebase getFirebaseInstance() {
        if (mFirebase == null) {
            mFirebase = new Firebase(HackerNewsAPI.ROOT_PATH);
        }
        return mFirebase;
    }

    public static ItemType getItemTypeFromString(String type) {
        if (type.equalsIgnoreCase("story")) {
            return ItemType.Story;
        } else if (type.equalsIgnoreCase("job")) {
            return ItemType.Job;
        } else if (type.equalsIgnoreCase("poll")) {
            return ItemType.Poll;
        } else if (type.equalsIgnoreCase("pollopt")) {
            return ItemType.Pollopt;
        } else if (type.equalsIgnoreCase("comment")) {
            return ItemType.Comment;
        } else {
            throw new IllegalStateException("Invalid Item type: " + type);
        }
    }

    public enum ItemType {
        Job,
        Story,
        Comment,
        Poll,
        Pollopt
    }
}
