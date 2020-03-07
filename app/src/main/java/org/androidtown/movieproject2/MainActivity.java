package org.androidtown.movieproject2;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.navigation.NavigationView;

import org.androidtown.movieproject2.Details.DetailViews.DetailFragment;
import org.androidtown.movieproject2.Details.MovieListFragment;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, FragmentCallback {

    ViewPager viewPager;
    private DrawerLayout mDrawerLayout;
    private Context context = this;
    private Fragment detailFragment;
    ///////////
    RecyclerView recyclerView;

    NavigationView navigationView;
    MovieListFragment movieListFragment;
    Button button;
    Toolbar toolbar;
    int flag = 0;
    ArrayList<Movie> arrayList;
    //8장
    public ImageView OrderImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_main);
        // FirstFragment firstFragment = new FirstFragment();
        //firstFragment=findViewById(R.id.fr1);
        movieListFragment = new MovieListFragment();
        OrderImage=findViewById(R.id.order_image);
        init();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.main_frame, movieListFragment).commit();

        //  fragmentTransaction.add(R.id.contentMain,FirstFragment.newInstance());
        detailFragment = new DetailFragment();
        // fragmentTransaction.add(R.id.main_frame,detailFragment).commit();
        //fragmentTransaction.commit();
        //getSupportFragmentManager().beginTransaction().show(firstFragment).commit();

        ///////////////////////////////////////////
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("영화 목록");
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_hamburger_menu);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        /////////////////////////////
    }


    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        //FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        switch (menuItem.getItemId()) {
            case R.id.nav_movie_list: {
                Toast.makeText(context, "클릭!!!", Toast.LENGTH_SHORT).show();
                onFragmentSelected(1, null);
            }
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);



        return true;
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
    public void onFragmentSelected(int pos, Bundle bundle) {
        Fragment cur = null;
        if (pos == 0) {
            cur = new DetailFragment();
            detailFragment = cur;
            detailFragment.setArguments(bundle);
            toolbar.setTitle("영화 상세");
        } else if (pos == 1) {
            cur = new MovieListFragment();
            movieListFragment = (MovieListFragment) cur;
            init();
            toolbar.setTitle("영화 목록");
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.main_frame, cur).commit();
    }
    public void init(){
        //tempActivity에서 받아서 메인 액티비티로다시 ListFragment로
        //tempActivity->Main activity->ListFragment;
        arrayList=getIntent().getParcelableArrayListExtra("movie_data");
        Bundle bundle=new Bundle();
        bundle.putParcelableArrayList("movie_data_list",arrayList);
        movieListFragment.setArguments(bundle);
    }


}
