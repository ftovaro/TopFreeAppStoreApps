package com.ftovaro.topappstoreapps.network;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ftovaro.topappstoreapps.interfaces.OnDownloadListener;
import com.ftovaro.topappstoreapps.utils.AppController;

import org.json.JSONObject;

/**
 * Manage the download of data from the server.
 * Created by FelipeTovar on 10-Apr-16.
 */
public class NetworkConnection {
    private static final String URL =
            "https://itunes.apple.com/us/rss/topfreeapplications/limit=20/json";

    /**
     * Download data from the server.
     * @param listener  an interface to control the download of the data.
     */
    public static void jsonObjectRequest(final OnDownloadListener listener){
        JsonObjectRequest request = new JsonObjectRequest(URL,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        listener.onSuccess(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.onError(error);
                    }
                });
        AppController.getInstance().addToRequestQueue(request);
    }
}
