package com.volvocars.crprotobuf;

import android.content.ContentResolver;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

class PusherUtils {

    /** Bundle Key */
    private static final String HD_ROUTE_PROTO_BUNDLE_KEY = "hd_route";

    /** URI FROM VCC SERVICE */
    private static final Uri HD_ROUTE_PROVIDER_INTERFACE_URI =
            new Uri.Builder().scheme(ContentResolver.SCHEME_CONTENT).authority("hdroutedispatcher").build();

    /** Methods */
    private static final String METHOD_DATUM_CONVERSION = "ROUTE TO VCC";

    @NonNull
    static Bundle parseHdRouteToBundle(@NonNull HdRoute.NavigationRoute navigationRoute) {
        final Bundle bundle = new Bundle();
        bundle.putByteArray(PusherUtils.HD_ROUTE_PROTO_BUNDLE_KEY, navigationRoute.toByteArray());
        return bundle;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Nullable
    static Bundle callVccPusher(@NonNull ContentResolver resolver, @NonNull Bundle bundle) {

        Bundle call = null;
        try {
            call = resolver.call(HD_ROUTE_PROVIDER_INTERFACE_URI, METHOD_DATUM_CONVERSION, null, bundle);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return call;
    }
}
