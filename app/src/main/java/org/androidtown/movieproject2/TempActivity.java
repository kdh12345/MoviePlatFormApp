package org.androidtown.movieproject2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.androidtown.movieproject2.Details.MovieListFragment;
import org.androidtown.movieproject2.NetworkCheck.NetworkStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TempActivity extends AppCompatActivity {
    MovieListFragment listFragment;
    Intent intent;
    ArrayList<Movie> arrayList;
    TextView contents;
    Button button;

    Handler handler=new Handler();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.temp_activity);
        //listFragment=new MovieListFragment();
        contents = findViewById(R.id.temp_content);
        button = findViewById(R.id.temp_button);
        intent = new Intent(getApplicationContext(), MainActivity.class);
        // 데이터베이스 ( 변경을 원할 시 version 증가 시키기 )
        AppHelper.openDatabase(getApplicationContext(), "movies.db", 8);
        final int Status = NetworkStatus.getConnectivityStatus(getApplicationContext());
        if (Status == NetworkStatus.WIFI || Status == NetworkStatus.MOBILE) {
            if (AppHelper.requestQueue == null)
                AppHelper.requestQueue = Volley.newRequestQueue(getApplicationContext());
            Toast.makeText(this, "요청 성공!!!", Toast.LENGTH_SHORT).show();
            button.setVisibility(View.INVISIBLE);
            sendRequest();
        } else {
            Toast.makeText(this, "wifi 혹은 모바일 네트워크를 키고 시작하세요", Toast.LENGTH_SHORT).show();
            contents.setText("네트워크 연결 실패!!!!");
            button.setText("네트워크 연결 다시시도");
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finish();
                    startActivity(new Intent(getApplicationContext(), TempActivity.class));
                    if (Status == NetworkStatus.WIFI || Status == NetworkStatus.MOBILE) {
                        if (AppHelper.requestQueue == null)
                            AppHelper.requestQueue = Volley.newRequestQueue(getApplicationContext());
                        Toast.makeText(getApplicationContext(), "요청 성공!!!", Toast.LENGTH_SHORT).show();
                        sendRequest();
                    }
                }
            });
        }


    }

    public void sendRequest() {
        String url = "http://boostcourse-appapi.connect.or.kr:10000/movie/readMovieList";
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

    /*
    * id: 1,
title: "꾼",
title_eng: "The Swindlers",
date: "2017-11-22",
user_rating: 4,
audience_rating: 8.36,
reviewer_rating: 4.33,
reservation_rate: 61.69,
reservation_grade: 1,
grade: 15,
thumb: "http://movie2.phinf.naver.net/20171107_251/1510033896133nWqxG_JPEG/movie_image.jpg?type=m99_141_2",
image: "http://movie.phinf.naver.net/20171107_251/1510033896133nWqxG_JPEG/movie_image.jpg",
    *
    * */
    public void processResponse(String response) {
        Gson gson = new Gson();
        MovieListResult movieListResult = gson.fromJson(response, MovieListResult.class);
        if (movieListResult != null) {
            Bundle bundle = new Bundle();
            arrayList = movieListResult.result;
            intent.putParcelableArrayListExtra("movie_data", arrayList);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    finish();
                    startActivity(intent);
                }
            },1000);

            //   bundle.putParcelableArrayList("movie_data_list",arrayList);
            //intent.putExtra("to_main_bundle",bundle);
            //  listFragment.setArguments(bundle);
        }

    }
}

