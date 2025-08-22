package com.hnh.enterprise.core.rest.request;

public enum DirectionType {
    UNSORTED(0), ASCENDING(1), DESCENDING(2);

    private final int value;

    DirectionType(int value) {
        this.value = value;
    }

}
