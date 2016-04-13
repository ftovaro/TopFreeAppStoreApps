package com.ftovaro.topappstoreapps.interfaces;

import com.android.volley.VolleyError;
import org.json.JSONObject;

/**
 * Interface that controls the success or failure downloading data.
 * Created by FelipeTovar on 10-Apr-16.
 */
public interface OnDownloadListener {
    /**
     * Success downloading data.
     * @param jsonObject    the response of the server.
     */
    void onSuccess(JSONObject jsonObject);

    /**
     * Failure downloading data.
     * @param error the response of the server.
     */
    void onError(VolleyError error);
}
