package com.roque.app.waylla_app.models;

import android.support.annotation.NonNull;
import com.google.firebase.firestore.Exclude;

public class EventoId {

    @Exclude
    public String EventoId;

    public <T extends EventoId> T withId(@NonNull final String id) {
        this.EventoId = id;
        return (T) this;
    }
}
