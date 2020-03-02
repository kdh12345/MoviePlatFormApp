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
import org.androidtown.movieproject2.DBClasses.MovieDBhelper;
import org.androidtown.movieproject2.DBClasses.SettingDB;
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
    //6장
    private MovieDBhelper dbHelper;
    private SQLiteDatabase database;
    int user_id;
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
            movieArrayList = bundle.getParcelableArrayList("from_movie_data");
            Glide.with(context).load(movieArrayList.get(0).image).into(imageView);
            id = movieArrayList.get(0).id;
            title.setText(movieArrayList.get(0).id + "." + " " + movieArrayList.get(0).title);
            information.setText("예매율 " + movieArrayList.get(0).reservation_rate + " %" + " | " + movieArrayList.get(0).grade + "세 관람가");
        }
        dbHelper = new MovieDBhelper(getContext(), "movie.db", null, 1);
        database = dbHelper.getWritableDatabase();
        int Status = NetworkStatus.getConnectivityStatus(getContext());
        if (Status == NetworkStatus.TYPE_MOBILE || Status == NetworkStatus.TYPE_WIFI) {
            if (AppHelper.requestQueue == null) {
                AppHelper.requestQueue = Volley.newRequestQueue(getContext());
            }
            sendRequest();
        } else {
            processJsonFromDB();
        }
        //  final FragmentTransaction fragmentTransaction = (getActivity()).getSupportFragmentManager().beginTransaction();

        return view;
    }

    public void sendRequest() {
        String url = "http://boostcourse-appapi.connect.or.kr:10000/movie/readMovie?id=1";
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
        /////
        String url2 = "http://boostcourse-appapi.connect.or.kr:10000/movie/readCommentList?id=1";
        StringRequest request2 = new StringRequest(
                Request.Method.GET,
                url2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        CommentResponse(response);
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
        request2.setShouldCache(false);
        AppHelper.requestQueue.add(request2);
    }

    public void CommentResponse(String response) {
        Gson gson = new Gson();
        Movie movie1 = new Movie();
        DetailFragment detailFragment = new DetailFragment();
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("result");
            int index = 0;
            while (index < jsonArray.length()) {
                movie1 = gson.fromJson(jsonArray.get(index).toString(), Movie.class);
                dbHelper.updateDatabase(movie1.getId(), SettingDB.COL_SIMPLE_COMMENTS, jsonArray.get(index).toString());
                user_id=movie1.getId();
               // Log.d("Database",movie1.getWriter());
                index++;
            }
            new_bundle.putParcelable("review", movie1);
            detailFragment.setArguments(new_bundle);
        } catch (Exception e) {
            e.printStackTrace();
        }
        detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // fragmentTransaction.replace(R.id.main_frame, DetailFragment.newInstance()).commit();
                ((MainActivity) getActivity()).onFragmentSelected(0, new_bundle, null);
            }
        });
    }

    public void processResponse(String response) {
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
            Movie movie1 = new Movie();
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.getJSONArray("result");
                int index = 0;
                while(index<jsonArray.length()){
                    movie1 = gson.fromJson(jsonArray.get(index).toString(), Movie.class);
                    dbHelper.updateDatabase(movie1.getId(), SettingDB.COL_DETAIL, jsonArray.get(index).toString());
                    index++;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            Intent intent = new Intent(context, AllListActivity.class);
            intent.putParcelableArrayListExtra("all_data", GiveMovieArray);
            intent.putExtra("MovieId", 1);
            detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // fragmentTransaction.replace(R.id.main_frame, DetailFragment.newInstance()).commit();
                    ((MainActivity) getActivity()).onFragmentSelected(0, new_bundle, movie);
                }
            });
        }

    }

    public void processJsonFromDB() {
        final DetailFragment detailFragment = new DetailFragment();
        final Bundle bundle = new Bundle();
        final Bundle bundle1 = new Bundle();

        detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new Gson();
                ArrayList<Movie> movies = new ArrayList<>();
                Movie movie = new Movie();
                Movie movie1 = new Movie();
                //  Movie movie_detail=new Movie();
                try {
                    //ArrayList<String> jsonStringArray = dbHelper.getDataFromDB(SettingDB.COL_MOVIE);
                    String m = dbHelper.getDataFromDB(SettingDB.COL_DETAIL, id);
                    movie = gson.fromJson(m, Movie.class);
                    movies.add(movie);
                    Log.d("Database", movie.getTitle() + "movie parsed");
                    //ArrayList<String> jsonStringArray = dbHelper.getDataFromDB(SettingDB.COL_MOVIE);
                    m = dbHelper.getDataFromDB(SettingDB.COL_SIMPLE_COMMENTS,user_id);
                    movie1 = gson.fromJson(m, Movie.class);
                    movies.add(movie1);
                    Log.d("Database", movie1.getWriter() + "movie parsed");
                } catch (Exception e) {
                    e.printStackTrace();
                }
                bundle.putParcelable("movie", movie);
                bundle.putParcelable("review", movie1);
                //detailFragment.setArguments(bundle);
                // fragmentTransaction.replace(R.id.main_frame, DetailFragment.newInstance()).commit();
                ((MainActivity) getActivity()).onFragmentSelected(0, bundle, movie);
            }
        });
        Log.d("Database", "pager creating...");
    }

}
