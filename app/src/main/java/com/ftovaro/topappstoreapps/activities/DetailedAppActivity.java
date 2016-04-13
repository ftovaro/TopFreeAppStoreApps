package com.ftovaro.topappstoreapps.activities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.transition.Explode;
import android.transition.Fade;
import android.view.MenuItem;
import android.widget.TextView;

import com.ftovaro.topappstoreapps.R;
import com.ftovaro.topappstoreapps.model.Application;

public class DetailedAppActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_app);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Application application = getIntent().getParcelableExtra("application");
        TextView author = (TextView) findViewById(R.id.author);
        TextView category = (TextView) findViewById(R.id.category);
        TextView summary = (TextView) findViewById(R.id.summary);
        TextView rights = (TextView) findViewById(R.id.rights);
        toolbar.setTitle(application.getName());
        setSupportActionBar(toolbar);
        author.setText(application.getAuthor());
        category.setText(application.getCategory());
        summary.setText(application.getSummary());
        rights.setText(application.getRights());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (android.os.Build.VERSION.SDK_INT >= 21) {
                    finishWithTransition();
                }else{
                    finish();
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void finishWithTransition(){
        getWindow().setExitTransition(new Fade());
        this.finishAfterTransition();
    }
}
