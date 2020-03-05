package org.androidtown.movieproject2.Details;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.androidtown.movieproject2.AppHelper;
import org.androidtown.movieproject2.MainActivity;
import org.androidtown.movieproject2.Movie;
import org.androidtown.movieproject2.MovieListResult;
import org.androidtown.movieproject2.NetworkCheck.NetworkStatus;
import org.androidtown.movieproject2.R;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MovieListFragment extends Fragment {
    ViewPager viewPager;
    MoviePagerAdapter moviePagerAdapter;
    FirstFragment firstFragment;
    SecondFragment secondFragment;
    ThirdFragment thirdFragment;
    FourthFragment fourthFragment;
    FifthFragment fifthFragment;
    ArrayList<Movie> arrayList;
    ArrayList<Movie> GiveMovieArray;
    Bundle new_bundle=new Bundle();
    MoviePagerAdpterOffLine moviePagerAdpterOffLine;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment_viewpager, container, false);
        viewPager = view.findViewById(R.id.draw_viewpager);
        viewPager.setOffscreenPageLimit(5);
        moviePagerAdapter = new MoviePagerAdapter(getActivity().getSupportFragmentManager());
        //프래그먼트 1추가
        firstFragment = new FirstFragment();
        //프래그먼트 2추가
        secondFragment = new SecondFragment();
        //프래그먼트 3추가
        thirdFragment = new ThirdFragment();
        //프래그먼트 4추가
        fourthFragment = new FourthFragment();
        //프래그먼트 5추가
        fifthFragment = new FifthFragment();
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            bundle = getArguments();
            arrayList = bundle.getParcelableArrayList("movie_data_list");
            for (int i = 0; i < arrayList.size(); i++) {
                switch (arrayList.get(i).id) {
                    case 1: {
                        Bundle tmp = new Bundle();
                        tmp.putParcelableArrayList("from_movie_data", arrayList);
                        firstFragment.setArguments(tmp);
                        break;
                    }
                    case 2: {
                        Bundle tmp = new Bundle();
                        tmp.putParcelableArrayList("from_movie_data", arrayList);
                        secondFragment.setArguments(tmp);
                        break;
                    }
                    case 3: {
                        Bundle tmp = new Bundle();
                        tmp.putParcelableArrayList("from_movie_data", arrayList);
                        thirdFragment.setArguments(tmp);
                        break;
                    }
                    case 4: {
                        Bundle tmp = new Bundle();
                        tmp.putParcelableArrayList("from_movie_data", arrayList);
                        fourthFragment.setArguments(tmp);
                        break;
                    }
                    case 5: {
                        Bundle tmp = new Bundle();
                        tmp.putParcelableArrayList("from_movie_data", arrayList);
                        fifthFragment.setArguments(tmp);
                        break;
                    }
                }
            }
        }
        moviePagerAdapter.addItem(firstFragment);
        moviePagerAdapter.addItem(secondFragment);
        moviePagerAdapter.addItem(thirdFragment);
        moviePagerAdapter.addItem(fourthFragment);
        moviePagerAdapter.addItem(fifthFragment);
        viewPager.setAdapter(moviePagerAdapter);
        int Status=NetworkStatus.getConnectivityStatus(getContext());
        if(Status==NetworkStatus.NOT_CONNECTED){
           /// NotConnectProcess();
        }
        return view;
    }
    public void NotConnectProcess(){
        Toast.makeText(getContext(), "인터넷 연결안됨", Toast.LENGTH_LONG).show();
        // DB 이용
        ArrayList<Movie> movieInfos = AppHelper.selectMovieList("movieinfo");
        moviePagerAdpterOffLine = new MoviePagerAdpterOffLine(getChildFragmentManager(), 0);

        for (int i = 0; i < movieInfos.size(); i++) {
            Movie movieInfo = movieInfos.get(i);
            MovieItemFragment movieItemFragment = new MovieItemFragment(movieInfo.id, i+1 + ". " + movieInfo.title,
                    "예매율 " + movieInfo.reservation_rate + "% | " + movieInfo.grade + "세 관람가", movieInfo.image);
            moviePagerAdpterOffLine.addItem(movieItemFragment);
        }
        moviePagerAdpterOffLine.notifyDataSetChanged();
    }
}
