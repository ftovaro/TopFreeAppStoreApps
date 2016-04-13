package com.ftovaro.topappstoreapps.fragments;


import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;
import android.transition.Explode;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.VolleyError;
import com.ftovaro.topappstoreapps.R;
import com.ftovaro.topappstoreapps.activities.DetailedAppActivity;
import com.ftovaro.topappstoreapps.adapters.ApplicationAdapter;
import com.ftovaro.topappstoreapps.interfaces.CommunicatorListener;
import com.ftovaro.topappstoreapps.interfaces.OnDownloadListener;
import com.ftovaro.topappstoreapps.model.Application;
import com.ftovaro.topappstoreapps.network.NetworkConnection;

import com.ftovaro.topappstoreapps.utils.CategoriesOptions;
import com.ftovaro.topappstoreapps.utils.GridAutofitLayoutManager;
import com.ftovaro.topappstoreapps.utils.InfoShower;
import com.ftovaro.topappstoreapps.utils.JsonParser;
import com.orm.SugarRecord;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A fragment that manage the list of applications.
 */
public class AppsListFragment extends Fragment {

    /** The list of apps. **/
    private ArrayList<Application> applications = new ArrayList<>();
    /** Adapter for the list of apps. **/
    private ApplicationAdapter applicationAdapter;
    /** Determine the size of the screen. **/
    private boolean isScreenLarge;
    /** Listener with parent Activity **/
    private CommunicatorListener communicatorListener;
    /** Minimum version so the animations works. **/
    private static final int MINIMUM_SDK_VERSION = 21;

    public AppsListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_apps_list, container, false);

        if(getArguments() != null)
        {
            setScreenLarge(getArguments().getBoolean("IsScreenLarge"));
        }

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        RecyclerView.LayoutManager layoutManagerList = new LinearLayoutManager(getActivity());
        GridAutofitLayoutManager layoutManagerGrid = new GridAutofitLayoutManager(getActivity(), 160);

        if(isScreenLarge) recyclerView.setLayoutManager(layoutManagerGrid);
        else recyclerView.setLayoutManager(layoutManagerList);

        applicationAdapter = new ApplicationAdapter(applications, isScreenLarge);

        recyclerView.setAdapter(applicationAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(),
                recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                if (android.os.Build.VERSION.SDK_INT >= MINIMUM_SDK_VERSION) {
                    callActivityTransition(applications.get(position));
                }else{
                    callActivity(applications.get(position));
                }
            }
            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        return view;
    }

    /**
     * Call the next activity with an animation.
     * @param application   the object to be shown in detail.
     */
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void callActivityTransition(Application application){
        Intent intent = new Intent(getActivity(), DetailedAppActivity.class);
        intent.putExtra("application", application);
        getActivity().getWindow().setExitTransition(new Explode());
        startActivity(intent,
                ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
    }

    /**
     * Call the next activity without any animation.
     * @param application   the object to be shown in detail.
     */
    private void callActivity(Application application){
        Intent intent = new Intent(getActivity(), DetailedAppActivity.class);
        intent.putExtra("application", application);
        startActivity(intent);
    }

    /**
     * Set the size of the screen.
     * @param screenLarge   true if the screen is XLarge or Large.
     */
    public void setScreenLarge(boolean screenLarge) {
        isScreenLarge = screenLarge;
    }

    /**
     * Update the data.
     * @param option        the category that the user wants to see.
     * @param isRefreshing  true if the user clicked on the refresh button.
     */
    public void updateInfo(int option, boolean isRefreshing){
        InfoShower.showDialog(getActivity());
        applications.clear();
        /* This is only called if the user clicked on the refresh button **/
        if(isRefreshing){
            try {
                boolean isConnected = checkInternetStatus();
                if(isConnected){
                    SugarRecord.deleteAll(Application.class);
                    downloadData();
                } else {
                    getDataFromDB();
                    communicatorListener.showInfo();
                }
            }catch (NullPointerException e){
                getDataFromDB();
                communicatorListener.showInfo();
            }
        }else {
            switch (CategoriesOptions.getStatusFromInt(option)){
                case TOP20APPS:
                    selectResourceOfInfo();
                    break;
                case EDUCATION:
                    applications.addAll(SugarRecord.find(Application.class, "category = ?",
                            getString(R.string.education)));
                    applicationAdapter.notifyDataSetChanged();
                    InfoShower.hideDialog();
                    break;
                case ENTERTAINMENT:
                    applications.addAll(SugarRecord.find(Application.class, "category = ?",
                            getString(R.string.entertainment)));
                    applicationAdapter.notifyDataSetChanged();
                    InfoShower.hideDialog();
                    break;
                case GAMES:
                    applications.addAll(SugarRecord.find(Application.class, "category = ?",
                            getString(R.string.games)));
                    applicationAdapter.notifyDataSetChanged();
                    InfoShower.hideDialog();
                    break;
                case MUSIC:
                    applications.addAll(SugarRecord.find(Application.class, "category = ?",
                            getString(R.string.music)));
                    applicationAdapter.notifyDataSetChanged();
                    InfoShower.hideDialog();
                    break;
                case NAVIGATION:
                    applications.addAll(SugarRecord.find(Application.class, "category = ?",
                            getString(R.string.navigation)));
                    applicationAdapter.notifyDataSetChanged();
                    InfoShower.hideDialog();
                    break;
                case PHOTO_AND_VIDEO:
                    applications.addAll(SugarRecord.find(Application.class, "category = ?",
                            getString(R.string.photo_and_video)));
                    applicationAdapter.notifyDataSetChanged();
                    InfoShower.hideDialog();
                    break;
                case SOCIAL_NETWORKING:
                    applications.addAll(SugarRecord.find(Application.class, "category = ?",
                            getString(R.string.social_networking)));
                    applicationAdapter.notifyDataSetChanged();
                    InfoShower.hideDialog();
                    break;
                case TRAVEL:
                    applications.addAll(SugarRecord.find(Application.class, "category = ?",
                            getString(R.string.travel)));
                    applicationAdapter.notifyDataSetChanged();
                    InfoShower.hideDialog();
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Select from getting the data from DB or download It from the server.
     */
    private void selectResourceOfInfo(){
        try{
            boolean isConnected = checkInternetStatus();
            if(isConnected){
                if(SugarRecord.count(Application.class) > 0){
                    getDataFromDB();
                } else {
                    downloadData();
                }
            } else {
                getDataFromDB();
            }
        }catch (NullPointerException e){
            e.printStackTrace();
            if(SugarRecord.count(Application.class) > 0){
                getDataFromDB();
                communicatorListener.showInfo();
            } else {
                communicatorListener.showInfo();
            }
        }
    }

    /**
     * Determine the Internet status.
     * @return  true if connected.
     */
    private boolean checkInternetStatus(){
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork.isConnectedOrConnecting();
    }

    /**
     * Get data from DB.
     */
    private void getDataFromDB(){
        applications.clear();
        applications.addAll(SugarRecord.listAll(Application.class));
        applicationAdapter.notifyDataSetChanged();
        InfoShower.hideDialog();
    }

    /**
     * Download data from the server.
     */
    private void downloadData() {
        NetworkConnection.jsonObjectRequest(new OnDownloadListener() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                applications.addAll(JsonParser.parseJSON(jsonObject));
                applicationAdapter.notifyDataSetChanged();
                InfoShower.hideDialog();
            }

            @Override
            public void onError(VolleyError error) {
                InfoShower.hideDialog();
            }
        });
    }

    /* There is a current problem with the fragment's onAttach method that is not being called,
     * so here we use the deprecated version to avoid those problems. */
    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            communicatorListener = (CommunicatorListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(context.toString()
                    + " must implement CommunicatorListener");
        }
    }

    public interface ClickListener {
        void onClick(View view, int position);
        void onLongClick(View view, int position);
    }

    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private AppsListFragment.ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView,
                                     final AppsListFragment.ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context,
                    new GestureDetector.SimpleOnGestureListener() {
                        @Override
                        public boolean onSingleTapUp(MotionEvent e) {
                            return true;
                        }

                        @Override
                        public void onLongPress(MotionEvent e) {
                            View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                            if (child != null && clickListener != null) {
                                clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                            }
                        }
                    });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

}
