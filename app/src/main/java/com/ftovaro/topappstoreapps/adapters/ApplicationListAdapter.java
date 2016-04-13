package com.ftovaro.topappstoreapps.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.ftovaro.topappstoreapps.R;
import com.ftovaro.topappstoreapps.model.Application;
import com.ftovaro.topappstoreapps.utils.AppController;

import java.util.List;

/**
 * Created by FelipeTovar on 10-Apr-16.
 */
public class ApplicationListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    /** List of apps **/
    private List<Application> applicationList;
    /** Loader for images that uses LRU Caché **/
    private ImageLoader imageLoader;

    private boolean isScreenLarge;

    public ApplicationListAdapter(List<Application> applicationList, boolean isScreenLarge) {
        this.applicationList = applicationList;
        this.isScreenLarge = isScreenLarge;
        imageLoader = AppController.getInstance().getImageLoader();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        try{
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.recycler_row, parent, false);
            return new ViewHolder(itemView);
        }catch (Exception e){
            throw new RuntimeException("there is no type that matches the type " + viewType +
                    " + make sure your using types correctly");
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Application application = applicationList.get(position);
        ((ViewHolder) holder).name.setText(application.getName());
        ((ViewHolder) holder).image.setImageUrl(application.getImage_url(), imageLoader);
        ((ViewHolder) holder).image.setDefaultImageResId(R.drawable.ic_image_white_24dp);
        if(!isScreenLarge){
            ((ViewHolder) holder).author.setText(application.getAuthor());
        }
    }

    @Override
    public int getItemCount() {
        return applicationList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name, author;
        public NetworkImageView image;
        public ViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            image = (NetworkImageView) view.findViewById(R.id.image);
            if(!isScreenLarge){
                author = (TextView) view.findViewById(R.id.author);
            }
        }
    }
}
