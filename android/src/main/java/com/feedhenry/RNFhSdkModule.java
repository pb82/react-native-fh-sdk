package com.feedhenry;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.feedhenry.helper.HeaderUtils;
import com.feedhenry.helper.SerializationUtils;
import com.feedhenry.sdk.FH;
import com.feedhenry.sdk.FHActCallback;
import com.feedhenry.sdk.FHResponse;
import com.feedhenry.sdk.exceptions.FHNotReadyException;

import org.json.fh.JSONObject;

import cz.msebera.android.httpclient.Header;

public class RNFhSdkModule extends ReactContextBaseJavaModule {
    private static final String MODULE_NAME = "RNFhSdk";
    private static final String DEFAULE_HTTP_VERB = "GET";
    private final ReactApplicationContext reactContext;

    public RNFhSdkModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;
    }

    /**
     * Initializes the application.
     * This must be called before the application can use the FH library.
     * The initialization process happens in a background thread so that the UI thread won't be
     * blocked. If you need to call other FH API methods, you need to make sure they are called
     * after the init finishes. The best way to do it is to provide a FHActCallback instance and
     * implement the success method. The callback functions are invoked on the main UI thread.
     *
     * @param promise Resolves to a Promise in JavaScript
     */
    @ReactMethod
    public void init(final Promise promise) {
        FH.init(getReactApplicationContext(), new FHActCallback() {
            @Override
            public void success(FHResponse pResponse) {
                // Init does not return meaningful data on success, so
                // we just ignore it
                promise.resolve(null);
            }

            @Override
            public void fail(FHResponse pResponse) {
                promise.reject(pResponse.getError());
            }
        });
    }

    /**
     * This should be called when the app is unmounted and the Feedhenry SDK is no
     * longer required. Shuts down all network listeners.
     */
    @ReactMethod
    public void stop() {
        FH.stop();
    }

    /**
     * Calls cloud APIs asynchronously.
     *
     * @param arguments see {@link CloudArgs}
     * @param promise   Resolves to a Promise in JavaScript
     */
    @ReactMethod
    public void cloud(ReadableMap arguments, final Promise promise) {
        if (!arguments.hasKey(CloudArgs.PATH.name)) {
            Exception error = new Exception("A path must be provided");
            promise.reject(error);
            return;
        }

        String path = arguments.getString(CloudArgs.PATH.name);

        // Default to a `GET` request if not verb is given
        String method = DEFAULE_HTTP_VERB;
        if (arguments.hasKey(CloudArgs.METHOD.name)) {
            method = arguments.getString(CloudArgs.METHOD.name);
        }

        // Headers are optional
        Header[] headers = null;
        if (arguments.hasKey(CloudArgs.HEADERS.name)) {
            headers = HeaderUtils.mapToHeaders(arguments.getMap(CloudArgs.HEADERS.name));
        }

        // Params are optional
        JSONObject params = null;
        if (arguments.hasKey(CloudArgs.PARAMS.name)) {
            params = new JSONObject(arguments.getMap(CloudArgs.PARAMS.name).toHashMap());
        }

        // Call the cloud app
        try {
            FH.cloud(path, method, headers, params, new FHActCallback() {
                @Override
                public void success(FHResponse pResponse) {
                    ReadableMap result = SerializationUtils.fhResponseToMap(pResponse);
                    promise.resolve(result);
                }

                @Override
                public void fail(FHResponse pResponse) {
                    promise.reject(pResponse.getError());
                }
            });
        } catch (FHNotReadyException e) {
            promise.reject(e);
        }

    }

    @Override
    public String getName() {
        return MODULE_NAME;
    }
}