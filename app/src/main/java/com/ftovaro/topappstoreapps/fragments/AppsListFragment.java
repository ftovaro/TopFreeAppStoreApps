package com.ftovaro.topappstoreapps.fragments;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;
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
import com.ftovaro.topappstoreapps.adapters.ApplicationAdapter;
import com.ftovaro.topappstoreapps.interfaces.OnDownloadListener;
import com.ftovaro.topappstoreapps.model.Application;
import com.ftovaro.topappstoreapps.network.NetworkConnection;

import com.ftovaro.topappstoreapps.utils.CategoriesOptions;
import com.ftovaro.topappstoreapps.utils.InfoShower;
import com.ftovaro.topappstoreapps.utils.JsonParser;
import com.ftovaro.topappstoreapps.utils.SimpleDividerItemDecoration;
import com.orm.SugarRecord;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class AppsListFragment extends Fragment {

    private ArrayList<Application> applications = new ArrayList<>();
    /** Adapter for the list of articles. **/
    private ApplicationAdapter applicationAdapter;

    public AppsListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_apps_list, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        applicationAdapter = new ApplicationAdapter(applications);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        //recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        recyclerView.setAdapter(applicationAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(),
                recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Application application = applications.get(position);
                Intent intent = new Intent(getActivity(), DetailedAppActivity.class);
                intent.putExtra("application", application);
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        return view;
    }

    public void updateInfo(int option){
        InfoShower.showDialog(getActivity());
        applications.clear();
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

    private void selectResourceOfInfo(){
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        try{
            boolean isConnected = activeNetwork.isConnectedOrConnecting();
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
            } else {
                Toast.makeText(getActivity(),"Connection with internet necessary",Toast.LENGTH_SHORT).show();
            }
        }
        InfoShower.hideDialog();
    }

    private void getDataFromDB(){
        applications.clear();
        applications.addAll(SugarRecord.listAll(Application.class));
        applicationAdapter.notifyDataSetChanged();
    }

    private void downloadData() {
        NetworkConnection.jsonObjectRequest(getActivity(), new OnDownloadListener() {
            @Override
            public void onSuccess(JSONObject jsonObject) {
                applications.addAll(JsonParser.parseJSON(jsonObject));
                InfoShower.hideDialog();
            }

            @Override
            public void onError(VolleyError error) {
                Log.e("Error", error.toString());
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
