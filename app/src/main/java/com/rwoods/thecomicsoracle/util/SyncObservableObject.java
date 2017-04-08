package com.rwoods.thecomicsoracle.util;

import java.util.Observable;

/**
 * Created by rwoods on 2/18/2016.
 */
public class SyncObservableObject extends Observable {
    private static SyncObservableObject instance = new SyncObservableObject();

    public static SyncObservableObject getInstance() {
        return instance;
    }

    private SyncObservableObject() {
    }

    public void updateValue(Object data) {
        synchronized (this) {
            setChanged();
            notifyObservers(data);
        }
    }
}