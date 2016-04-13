package com.ftovaro.topappstoreapps.utils;

import com.ftovaro.topappstoreapps.model.Application;
import com.orm.SugarRecord;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Manage the process of interpreting the response of a server.
 * Created by FelipeTovar on 10-Apr-16.
 */
public class JsonParser {

    /** The name of the json array with all the data **/
    private static final String FEED = "feed";
    /** A subgroup with all the information of the apps **/
    private static final String ENTRY = "entry";
    /** The name of the json object where most of the information is saved **/
    private static final String LABEL = "label";
    /** The name of the json object with the name of the app **/
    private static final String NAME = "im:name";
    /** The name of the json array with the images **/
    private static final String IMAGES = "im:image";
    /** The name of the json object with the summary of the app **/
    private static final String SUMMARY = "summary";
    /** The name of the json object with the right **/
    private static final String RIGHTS = "rights";
    /** The name of the json object with the author **/
    private static final String AUTHOR = "im:artist";
    /** The name of the json object with the category of the app **/
    private static final String CATEGORY = "category";
    /** An inner json with the name of the category **/
    private static final String ATTRIBUTES = "attributes";

    /**
     * Read the response of the server and creates a list of those objects.
     * @param jsonObject    response of the server.
     * @return              list of applications.
     */
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

    /**
     * Create the object Application and saves It in the DB.
     * @param name          name of the app.
     * @param image_url     url of the image.
     * @param summary       summary of the app.
     * @param rights        rights of the app.
     * @param author        author of the app.
     * @param category      category of the app.
     * @return              an object Application saved It on DB.
     */
    private static Application createApplication(String name, String image_url, String summary,
                                                 String rights, String author, String category){
        Application application = new Application(author, category, name, summary, image_url,
                rights);
        application.save();
        return application;
    }
}
