package org.chendu.demo.oauth2.config;

import org.springframework.beans.propertyeditors.CustomCollectionEditor;

import java.util.Collection;

/**
 * Created by Chen Du @10/31/2018.
 * Version 1.0
 */
public class EditorSplitCollection extends CustomCollectionEditor {

    private final Class<? extends Collection> collectionType;
    private final String splitRegex;

    public EditorSplitCollection(Class<? extends Collection> collectionType, String splitRegex) {
        super(collectionType, true);
        this.collectionType = collectionType;
        this.splitRegex = splitRegex;
    }

    @Override
    public void setAsText(String text) throws IllegalArgumentException {
        if (text == null || text.isEmpty()) {
            super.setValue(super.createCollection(this.collectionType, 0));
        } else {
            super.setValue(text.split(splitRegex));
        }
    }
}