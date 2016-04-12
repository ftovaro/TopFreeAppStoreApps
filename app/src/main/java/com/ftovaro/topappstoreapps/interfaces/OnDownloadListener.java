package com.ftovaro.topappstoreapps.interfaces;

import com.android.volley.VolleyError;
import org.json.JSONObject;

/**
 * Created by FelipeTovar on 10-Apr-16.
 */
public interface OnDownloadListener {
    void onSuccess(JSONObject jsonObject);
    void onError(VolleyError error);
}
