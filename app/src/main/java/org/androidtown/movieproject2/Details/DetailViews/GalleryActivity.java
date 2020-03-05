package org.androidtown.movieproject2.Details.DetailViews;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;

import org.androidtown.movieproject2.R;

public class GalleryActivity extends AppCompatActivity {
    ActionBar actionBar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        String photoUrl = getIntent().getStringExtra("url");
        PhotoView photoView = findViewById(R.id.photoview);
        Glide.with(getApplicationContext()).load(photoUrl).into(photoView);
    }
}
