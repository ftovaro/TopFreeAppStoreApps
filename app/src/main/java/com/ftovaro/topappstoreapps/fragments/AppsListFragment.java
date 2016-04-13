package com.ftovaro.topappstoreapps.fragments;


import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;
import android.transition.Explode;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.ftovaro.topappstoreapps.R;
import com.ftovaro.topappstoreapps.activities.DetailedAppActivity;
import com.ftovaro.topappstoreapps.adapters.ApplicationListAdapter;
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
 * A simple {@link Fragment} subclass.
 */
public class AppsListFragment extends Fragment {

    private ArrayList<Application> applications = new ArrayList<>();
    /** Adapter for the list of apps. **/
    private ApplicationListAdapter applicationListAdapter;

    private boolean isScreenLarge;

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

        applicationListAdapter = new ApplicationListAdapter(applications, isScreenLarge);

        recyclerView.setAdapter(applicationListAdapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(),
                recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                if (android.os.Build.VERSION.SDK_INT >= 21) {
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

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void callActivityTransition(Application application){
        Intent intent = new Intent(getActivity(), DetailedAppActivity.class);
        intent.putExtra("application", application);
        getActivity().getWindow().setExitTransition(new Explode());
        startActivity(intent,
                ActivityOptions.makeSceneTransitionAnimation(getActivity()).toBundle());
    }

    private void callActivity(Application application){
        Intent intent = new Intent(getActivity(), DetailedAppActivity.class);
        intent.putExtra("application", application);
        startActivity(intent);
    }

    public void setScreenLarge(boolean screenLarge) {
        isScreenLarge = screenLarge;
    }

    public void updateInfo(int option, boolean isRefreshing){
        InfoShower.showDialog(getActivity());
        applications.clear();
        if(isRefreshing){
            try {
                boolean isConnected = checkInternetStatus();
                if(isConnected){
                    SugarRecord.deleteAll(Application.class);
                    downloadData();
                } else {
                    getDataFromDB();
                    Toast.makeText(getActivity(), "Please connect to Internet", Toast.LENGTH_SHORT).show();
                }
            }catch (NullPointerException e){
                getDataFromDB();
                Toast.makeText(getActivity(), "Please connect to Internet", Toast.LENGTH_SHORT).show();
            }
        }else {
            switch (CategoriesOptions.getStatusFromInt(option)){
                case TOP20APPS:
                    selectResourceOfInfo();
                    break;
                case EDUCATION:
                    applications.addAll(SugarRecord.find(Application.class, "category = ?",
                            getString(R.string.education)));
                    applicationListAdapter.notifyDataSetChanged();
                    InfoShower.hideDialog();
                    break;
                case ENTERTAINMENT:
                    applications.addAll(SugarRecord.find(Application.class, "category = ?",
                            getString(R.string.entertainment)));
                    applicationListAdapter.notifyDataSetChanged();
                    InfoShower.hideDialog();
                    break;
                case GAMES:
                    applications.addAll(SugarRecord.find(Application.class, "category = ?",
                            getString(R.string.games)));
                    applicationListAdapter.notifyDataSetChanged();
                    InfoShower.hideDialog();
                    break;
                case MUSIC:
                    applications.addAll(SugarRecord.find(Application.class, "category = ?",
                            getString(R.string.music)));
                    applicationListAdapter.notifyDataSetChanged();
                    InfoShower.hideDialog();
                    break;
                case NAVIGATION:
                    applications.addAll(SugarRecord.find(Application.class, "category = ?",
                            getString(R.string.navigation)));
                    applicationListAdapter.notifyDataSetChanged();
                    InfoShower.hideDialog();
                    break;
                case PHOTO_AND_VIDEO:
                    applications.addAll(SugarRecord.find(Application.class, "category = ?",
                            getString(R.string.photo_and_video)));
                    applicationListAdapter.notifyDataSetChanged();
                    InfoShower.hideDialog();
                    break;
                case SOCIAL_NETWORKING:
                    applications.addAll(SugarRecord.find(Application.class, "category = ?",
                            getString(R.string.social_networking)));
                    applicationListAdapter.notifyDataSetChanged();
                    InfoShower.hideDialog();
                    break;
                case TRAVEL:
                    applications.addAll(SugarRecord.find(Application.class, "category = ?",
                            getString(R.string.travel)));
                    applicationListAdapter.notifyDataSetChanged();
                    InfoShower.hideDialog();
                    break;
                default:
                    break;
            }
        }
    }

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
                Toast.makeText(getActivity(), "Please connect to Internet", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getActivity(), "Please connect to Internet", Toast.LENGTH_SHORT).show();
            }
        }
        //InfoShower.hideDialog();
    }

    private boolean checkInternetStatus(){
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork.isConnectedOrConnecting();
    }

    private void getDataFromDB(){
        applications.clear();
        applications.addAll(SugarRecord.listAll(Application.class));
        applicationListAdapter.notifyDataSetChanged();
        InfoShower.hideDialog();
    }

    private void downloadData() {
        NetworkConnection.jsonObjectRequest(getActivity(), new OnDownloadListener() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                applications.addAll(JsonParser.parseJSON(jsonObject));
                applicationListAdapter.notifyDataSetChanged();
                InfoShower.hideDialog();
            }

            @Override
            public void onError(VolleyError error) {
                Log.e("Error", error.toString());
                InfoShower.hideDialog();
            }
        });
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
