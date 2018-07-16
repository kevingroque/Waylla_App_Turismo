package com.roque.app.waylla_app.models;

import android.support.annotation.NonNull;
import com.google.firebase.firestore.Exclude;

public class LomaId {

    @Exclude
    public String LomaId;

    public <T extends LomaId> T withId(@NonNull final String id) {
        this.LomaId = id;
        return (T) this;
    }
}
