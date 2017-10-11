package com.feedhenry.helper;

import android.support.annotation.NonNull;

import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.ReadableMapKeySetIterator;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.message.BasicHeader;

public abstract class HeaderUtils {
    @NonNull
    public final static Header[] mapToHeaders(ReadableMap headers) {
        List<Header> result = new ArrayList<>();
        ReadableMapKeySetIterator keys = headers.keySetIterator();
        while (keys.hasNextKey()) {
            String headerName = keys.nextKey();
            String headerValue = headers.getDynamic(headerName).asString();
            BasicHeader header = new BasicHeader(headerName, headerValue);
            result.add(header);
        }

        return result.toArray(new Header[result.size()]);
    }
}