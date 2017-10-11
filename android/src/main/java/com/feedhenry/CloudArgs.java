package com.feedhenry;

public enum CloudArgs {
    PATH("path"), METHOD("method"), HEADERS("headers"), PARAMS("params");

    String name;

    CloudArgs(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}