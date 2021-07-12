package com.volvocars.crprotobuf;

import android.content.ContentResolver;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class VccServicePusher {

    private static final String TAG = "VccServicePusher";

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Nullable
    public static HdRoute push(@NonNull Context context,
                               @NonNull HdRoute.NavigationRoute navigationRoute) {

        // 1. get resolver from pusher app
        final ContentResolver resolver = context.getContentResolver();
        if (resolver == null) {
            Log.e(TAG, "Failed to get content resolver");
            return null;
        }

        // 2. push to service app
        Bundle bundle = PusherUtils.parseHdRouteToBundle(navigationRoute);
        bundle = PusherUtils.callVccPusher(resolver, bundle);
        if (bundle == null) {
            Log.w(TAG, "Received HD route bundle is null");
            return null;
        }
        return null;
    }
}
