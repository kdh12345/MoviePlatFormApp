package org.androidtown.movieproject2.Details;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
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

public class ThirdFragment extends Fragment {
    Button detail;
    ImageView imageView;
    TextView title;
    TextView information;
    Context context;
    ArrayList<Movie> movieArrayList;
    int id;
    int user_id;
    ThirdFragment() {

    }

    public static ThirdFragment newInstance() {
        Bundle args = new Bundle();
        ThirdFragment thirdFragment = new ThirdFragment();

        return thirdFragment;
    }

    Bundle bundle;
    ArrayList<Movie> GiveMovieArray;
    Bundle new_bundle = new Bundle();//디테일로 보내는 번들

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.fragment3, container, false);
        context = getContext();
        imageView = view.findViewById(R.id.img3);
        title = view.findViewById(R.id.tv_title3);
        information = view.findViewById(R.id.info3);
        detail = view.findViewById(R.id.detail_btn3);
        bundle = this.getArguments();
        if (bundle != null) {
            bundle = getArguments();
            movieArrayList = bundle.getParcelableArrayList("from_movie_data");
            // Toast.makeText(context, movieArrayList.get(2).id+"", Toast.LENGTH_LONG).show();
            Glide.with(context).load(movieArrayList.get(2).image).into(imageView);
            //   Toast.makeText(context, movieArrayList.get(2).id+"", Toast.LENGTH_SHORT).show();
            id=movieArrayList.get(2).id;
            title.setText(movieArrayList.get(2).id + "." + " " + movieArrayList.get(2).title);
            information.setText("예매율 " + movieArrayList.get(2).reservation_rate + " %" + " | " + movieArrayList.get(2).grade + "세 관람가");
        }
        ///final FragmentTransaction fragmentTransaction=(getActivity()).getSupportFragmentManager().beginTransaction();
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
        String url = "http://boostcourse-appapi.connect.or.kr:10000/movie/readMovie?id=3";
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
            new_bundle.putInt("MvID", 3);
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
        new_bundle.putInt("Idx",movieArrayList.get(2).id);
        detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).onFragmentSelected(0, new_bundle);
            }
        });
    }
}
