package org.androidtown.movieproject2.Details;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.androidtown.movieproject2.AppHelper;
import org.androidtown.movieproject2.Details.DetailViews.DetailFragment;
import org.androidtown.movieproject2.Details.DetailViews.ReviewItem;
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

public class FirstFragment extends Fragment {
    ImageView imageView;
    TextView title;
    TextView information;
    Button detail;
    Context context;

    int user_id;

    MoviePagerAdpterOffLine pagerAdpterOffLine;
    FirstFragment() {

    }

    public static FirstFragment newInstance() {
        Bundle args = new Bundle();
        FirstFragment firstFragment = new FirstFragment();

        return firstFragment;
    }

    ArrayList<Movie> movieArrayList;

    Bundle bundle;//받는 번들
    ArrayList<Movie> GiveMovieArray;
    Bundle new_bundle = new Bundle();//디테일로 보내는 번들
    int id;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment1, container, false);
        context = getContext();
        imageView = view.findViewById(R.id.img1);
        title = view.findViewById(R.id.tv_title);
        information = view.findViewById(R.id.info1);
        detail = view.findViewById(R.id.detail_btn1);
        bundle = this.getArguments();
        if (bundle != null) {
            bundle = getArguments();
            //movieArrayList = bundle.getParcelableArrayList("from_movie_data");
            Movie movie=bundle.getParcelable("from_movie_data");
            Glide.with(context).load(movie.getImage()).into(imageView);
            id = movie.getId();
            title.setText(movie.getId() + "." + " " + movie.getTitle());
            information.setText("예매율 " + movie.getReservation_rate() + " %" + " | " + movie.getGrade() + "세 관람가");
        }
        final int Status = NetworkStatus.getConnectivityStatus(getContext());
        if (Status == NetworkStatus.MOBILE || Status == NetworkStatus.WIFI) {
            if (AppHelper.requestQueue == null) {
                AppHelper.requestQueue = Volley.newRequestQueue(getContext());
            }
            sendRequest();
        } else {
            processJsonFromDB();
        }

        return view;
    }

    public void sendRequest() {
        String url = "http://boostcourse-appapi.connect.or.kr:10000/movie/readMovie?id="+id;
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




    public void processResponse(final String response) {
        Gson gson = new Gson();
        MovieListResult movieListResult = gson.fromJson(response, MovieListResult.class);
        final Movie movie = gson.fromJson(response, Movie.class);
        if (movieListResult != null) {
            GiveMovieArray = movieListResult.result;
            //Toast.makeText(context, GiveMovieArray.get(0).actor, Toast.LENGTH_SHORT).show();
            new_bundle.putParcelableArrayList("movie_data_detail", GiveMovieArray);
            new_bundle.putInt("MvID", 1);
            new_bundle.putInt("movie_grade", GiveMovieArray.get(0).grade);
            new_bundle.putString("movie_title", GiveMovieArray.get(0).title);

            Intent intent = new Intent(context, AllListActivity.class);
            intent.putParcelableArrayListExtra("all_data", GiveMovieArray);
            intent.putExtra("MovieId", 1);
            detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // fragmentTransaction.replace(R.id.main_frame, DetailFragment.newInstance()).commit();
                    ((MainActivity) getActivity()).onFragmentSelected(0, new_bundle);
                }
            });
        }

    }

    public void processJsonFromDB() {
// DB 이용
       new_bundle.putInt("Idx",id);
       detail.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               ((MainActivity) getActivity()).onFragmentSelected(0, new_bundle);
           }
       });
    }

}
