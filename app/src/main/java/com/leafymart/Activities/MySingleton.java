package com.leafymart.Activities;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/** Reusable Volley request queue for all API calls */
public class MySingleton {

    private static MySingleton instance;
    private final RequestQueue requestQueue;

    private MySingleton(Context context) {
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
    }

    public static synchronized MySingleton get(Context context) {
        if (instance == null) {
            instance = new MySingleton(context);
        }
        return instance;
    }

    public <T> void add(Request<T> request) {
        requestQueue.add(request);
    }
}
