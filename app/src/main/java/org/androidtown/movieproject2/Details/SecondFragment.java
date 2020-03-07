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

public class SecondFragment extends Fragment {
    Button detail;
    ImageView imageView;
    TextView title;
    TextView information;
    Context context;
    ArrayList<Movie> movieArrayList;
    //6장

    private SQLiteDatabase database;
    int id;
    int user_id;
    //8장
    int order=1;
    Animation translateUp;
    Animation translateDown;
    LinearLayout RootOrderLayout;
    boolean isPageOpen=false;
    SecondFragment() {

    }

    public static SecondFragment newInstance() {
        Bundle args = new Bundle();
        SecondFragment secondFragment = new SecondFragment();

        return secondFragment;
    }

    Bundle bundle;
    ArrayList<Movie> GiveMovieArray;
    Bundle new_bundle = new Bundle();//디테일로 보내는 번들

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment2, container, false);
        context = getContext();
        imageView = view.findViewById(R.id.img2);
        title = view.findViewById(R.id.tv_title2);
        information = view.findViewById(R.id.info2);
        detail = view.findViewById(R.id.detail_btn2);
        bundle = this.getArguments();
        if (bundle != null) {
            bundle = getArguments();
            //movieArrayList = bundle.getParcelableArrayList("from_movie_data");
            Movie movie=bundle.getParcelable("from_movie_data");
            // Toast.makeText(context, movieArrayList.get(1).id+"", Toast.LENGTH_LONG).show();
            Glide.with(context).load(movie.getImage()).into(imageView);
            id=movie.getId();
            //Toast.makeText(context, movieArrayList.get(1).id+"", Toast.LENGTH_SHORT).show();
            title.setText(movie.getId() + "." + " " + movie.getTitle());
            information.setText("예매율 " + movie.getReservation_rate() + " %" + " | " + movie.getGrade() + "세 관람가");
        }
        int Status = NetworkStatus.getConnectivityStatus(getContext());
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
        String url = "http://boostcourse-appapi.connect.or.kr:10000/movie/readMovie?id=2";
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
    public void processResponse(String response) {
        Gson gson = new Gson();
        MovieListResult movieListResult = gson.fromJson(response, MovieListResult.class);
        if (movieListResult != null) {
            GiveMovieArray = movieListResult.result;
            //Toast.makeText(context, GiveMovieArray.get(0).actor, Toast.LENGTH_SHORT).show();
            new_bundle.putParcelableArrayList("movie_data_detail", GiveMovieArray);
            new_bundle.putInt("MvID", 2);
            new_bundle.putInt("movie_grade", GiveMovieArray.get(0).grade);
            new_bundle.putString("movie_title", GiveMovieArray.get(0).title);
            Intent intent = new Intent(context, AllListActivity.class);
            intent.putParcelableArrayListExtra("all_data", GiveMovieArray);
            intent.putExtra("MovieId", 2);
            detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // fragmentTransaction.replace(R.id.main_frame, DetailFragment.newInstance()).commit();
                    ((MainActivity) getActivity()).onFragmentSelected(0, new_bundle);
                }
            });

        }
    }//processResponse

    public void processJsonFromDB() {
        new_bundle.putInt("Idx",movieArrayList.get(1).id);
        detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).onFragmentSelected(0, new_bundle);
            }
        });
    }

}
