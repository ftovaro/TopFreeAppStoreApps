package com.ftovaro.topappstoreapps.utils;

import com.ftovaro.topappstoreapps.model.Application;
import com.orm.SugarRecord;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by FelipeTovar on 10-Apr-16.
 */
public class JsonParser {

    private static final String FEED = "feed";
    private static final String ENTRY = "entry";
    private static final String LABEL = "label";
    private static final String NAME = "im:name";
    private static final String IMAGES = "im:image";
    private static final String SUMMARY = "summary";
    private static final String RIGHTS = "rights";
    private static final String AUTHOR = "im:artist";
    private static final String CATEGORY = "category";
    private static final String ATTRIBUTES = "attributes";

    public static ArrayList<Application> parseJSON(JSONObject jsonObject) {
        ArrayList<Application> applications = new ArrayList<>();
        SugarRecord.deleteAll(Application.class);
        try {
            JSONObject feed = jsonObject.getJSONObject(FEED);
            JSONArray entry = feed.getJSONArray(ENTRY);
            for(int i = 0; i < entry.length(); i++){
                JSONObject app = entry.getJSONObject(i);
                String name = app.getJSONObject(NAME).getString(LABEL);
                JSONArray imageArray = app.getJSONArray(IMAGES);
                String image_url  = imageArray.getJSONObject(1).getString(LABEL);
                String summary = app.getJSONObject(SUMMARY).getString(LABEL);
                String rights = app.getJSONObject(RIGHTS).getString(LABEL);
                String author = app.getJSONObject(AUTHOR).getString(LABEL);
                String category = app.getJSONObject(CATEGORY).getJSONObject(ATTRIBUTES)
                        .getString(LABEL);
                applications.add(createApplication(
                        name,
                        image_url,
                        summary,
                        rights,
                        author,
                        category));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return applications;
    }

    private static Application createApplication(String name, String image_url, String summary,
                                                 String rights, String author, String category){
        Application application = new Application(author, category, name, summary, image_url,
                rights);
        application.save();
        return application;
    }
}
