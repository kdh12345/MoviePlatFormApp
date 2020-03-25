package org.androidtown.movieproject2.Details;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
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
    Bundle new_bundle = new Bundle();
    MoviePagerAdpterOffLine moviePagerAdpterOffLine;
    //8장
    int order = 1;
    Animation translateUp;
    Animation translateDown;
    LinearLayout RootOrderLayout;
    boolean isPageOpen = false;

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
                        Movie movie=arrayList.get(i);
                        tmp.putParcelable("from_movie_data", movie);
                        firstFragment.setArguments(tmp);
                        break;
                    }
                    case 2: {
                        Bundle tmp = new Bundle();
                        Movie movie=arrayList.get(i);
                        tmp.putParcelable("from_movie_data", movie);
                        secondFragment.setArguments(tmp);
                        break;
                    }
                    case 3: {
                        Bundle tmp = new Bundle();
                        Movie movie=arrayList.get(i);
                        tmp.putParcelable("from_movie_data", movie);
                        thirdFragment.setArguments(tmp);
                        break;
                    }
                    case 4: {
                        Bundle tmp = new Bundle();
                        Movie movie=arrayList.get(i);
                        tmp.putParcelable("from_movie_data", movie);
                        fourthFragment.setArguments(tmp);
                        break;
                    }
                    case 5: {
                        Bundle tmp = new Bundle();
                        Movie movie=arrayList.get(i);
                        tmp.putParcelable("from_movie_data", movie);
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
        moviePagerAdapter.notifyDataSetChanged();
        viewPager.setAdapter(moviePagerAdapter);
        RootOrderLayout = view.findViewById(R.id.RootOrderLayout);
        translateUp = AnimationUtils.loadAnimation(getContext(), R.anim.translate_up);
        translateDown = AnimationUtils.loadAnimation(getContext(), R.anim.translate_down);

        SlidingPageAnimationListener animationLinstener = new SlidingPageAnimationListener();
        translateUp.setAnimationListener(animationLinstener);
        translateDown.setAnimationListener(animationLinstener);


        // 정렬이미지 클릭했을 때
        ((MainActivity) getActivity()).OrderImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPageOpen) {
                    RootOrderLayout.startAnimation(translateUp);
                } else {
                    RootOrderLayout.setVisibility(View.VISIBLE);
                    RootOrderLayout.startAnimation(translateDown);
                }
            }
        });
        //정렬 기준 정하기
        final int Status = NetworkStatus.getConnectivityStatus(getContext());
        LinearLayout layout1 = view.findViewById(R.id.layout1);
        layout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Status == NetworkStatus.WIFI || Status == NetworkStatus.MOBILE) {
                    moviePagerAdapter.clearArrayList();
                    moviePagerAdapter.notifyDataSetChanged();
                    order = 1;
                    Toast.makeText(getContext(), "예매율순으로 정렬", Toast.LENGTH_LONG).show();
                    ((MainActivity) getActivity()).OrderImage.setImageResource(R.drawable.order11);
                    RootOrderLayout.startAnimation(translateUp);
                    requestMovieList();
                } else {
                    Toast.makeText(getContext(), "인터넷 연결안됨", Toast.LENGTH_LONG).show();
                }
            }
        });
        LinearLayout layout2 = view.findViewById(R.id.layout2);
        layout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Status == NetworkStatus.WIFI || Status == NetworkStatus.MOBILE) {
                    order = 2;
                    moviePagerAdapter.clearArrayList();
                    moviePagerAdapter.notifyDataSetChanged();
                    Toast.makeText(getContext(), "큐레이션으로 정렬", Toast.LENGTH_LONG).show();
                    ((MainActivity) getActivity()).OrderImage.setImageResource(R.drawable.order22);
                    RootOrderLayout.startAnimation(translateUp);
                    requestMovieList();
                } else {
                    Toast.makeText(getContext(), "인터넷 연결안됨", Toast.LENGTH_LONG).show();
                }
            }
        });
        LinearLayout layout3 = view.findViewById(R.id.layout3);
        layout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Status == NetworkStatus.WIFI || Status == NetworkStatus.MOBILE) {
                    order = 3;
                    moviePagerAdapter.clearArrayList();
                    moviePagerAdapter.notifyDataSetChanged();
                    Toast.makeText(getContext(), "상영예정순으로 정렬", Toast.LENGTH_LONG).show();
                    ((MainActivity) getActivity()).OrderImage.setImageResource(R.drawable.order33);
                    RootOrderLayout.startAnimation(translateUp);
                    requestMovieList();
                } else {
                    Toast.makeText(getContext(), "인터넷 연결안됨", Toast.LENGTH_LONG).show();
                }
            }
        });

        return view;
    }

    public void requestMovieList() {
        String url = "http://boostcourse-appapi.connect.or.kr:10000/movie/readMovieList" + "?type=" + order;
        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        processResponse(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                return params;
            }
        };
        request.setShouldCache(false);
        AppHelper.requestQueue.add(request);
    }

    public void processResponse(String response) {
        Gson gson = new Gson();
        MovieListResult movieListResult = gson.fromJson(response, MovieListResult.class);
        if (movieListResult.code == 200) {
            GiveMovieArray = movieListResult.result;

            for(int i=0;i<GiveMovieArray.size();i++){
                Toast.makeText(getContext(), ""+GiveMovieArray.get(i).id, Toast.LENGTH_SHORT).show();
                if(GiveMovieArray.get(i).id==1){
                    Bundle bundle = new Bundle();
                    moviePagerAdapter.addItem(firstFragment);
                    Movie movie=GiveMovieArray.get(i);
                    bundle.putParcelable("from_movie_data",movie);
                    firstFragment.setArguments(bundle);
                    //bundle.putParcelableArrayList("from_movie_data", GiveMovieArray);

                }
                else if(GiveMovieArray.get(i).id==2){
                    Bundle bundle = new Bundle();
                    moviePagerAdapter.addItem(secondFragment);
                    Movie movie=GiveMovieArray.get(i);
                    bundle.putParcelable("from_movie_data",movie);
                    secondFragment.setArguments(bundle);
                    //bundle.putParcelableArrayList("from_movie_data", GiveMovieArray);

                }
                else if(GiveMovieArray.get(i).id==3){
                    Bundle bundle = new Bundle();
                    moviePagerAdapter.addItem(thirdFragment);
                    Movie movie=GiveMovieArray.get(i);
                    bundle.putParcelable("from_movie_data",movie);
                    thirdFragment.setArguments(bundle);
                    // bundle.putParcelableArrayList("from_movie_data", GiveMovieArray);

                }
                else if(GiveMovieArray.get(i).id==4){
                    Bundle bundle = new Bundle();
                    moviePagerAdapter.addItem(fourthFragment);
                    Movie movie=GiveMovieArray.get(i);
                    bundle.putParcelable("from_movie_data",movie);
                    fourthFragment.setArguments(bundle);
                    // bundle.putParcelableArrayList("from_movie_data", GiveMovieArray);

                }
                else if(GiveMovieArray.get(i).id==5){
                    Bundle bundle = new Bundle();
                    moviePagerAdapter.addItem(fifthFragment);
                    Movie movie=GiveMovieArray.get(i);
                    bundle.putParcelable("from_movie_data",movie);
                    fifthFragment.setArguments(bundle);
                    //bundle.putParcelableArrayList("from_movie_data", GiveMovieArray);

                }
            }
            moviePagerAdapter.notifyDataSetChanged();
            viewPager.setAdapter(moviePagerAdapter);
        }
    }

    private class SlidingPageAnimationListener implements Animation.AnimationListener {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }

        public void onAnimationEnd(Animation animation) {
            if (isPageOpen) {
                RootOrderLayout.setVisibility(View.INVISIBLE);
                isPageOpen = false;
            } else {
                isPageOpen = true;
            }
        }
    }
}