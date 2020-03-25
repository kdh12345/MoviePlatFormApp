package org.androidtown.movieproject2.Details;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.androidtown.movieproject2.AppHelper;
import org.androidtown.movieproject2.Details.DetailViews.TimeClass;
import org.androidtown.movieproject2.Movie;
import org.androidtown.movieproject2.MovieListResult;
import org.androidtown.movieproject2.NetworkCheck.NetworkStatus;
import org.androidtown.movieproject2.R;
import org.json.JSONArray;
import org.json.JSONObject;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AllListActivity extends AppCompatActivity {

    // EvaluationInfo(String ID,int image,String Time,float ratingBar,String recommend,int count,String report,
    //                   String evaulates)
    ArrayList<EvaluationInfo> mArrayList = new ArrayList<>();
    EvaListAdapter evaListAdapter;
    TextView write;
    RecyclerView mRecyclerView;
    ArrayList<Movie> movieArrayList;
    final static int OK = 5;
    ArrayList<EvaluationInfo> evaluationInfos = new ArrayList<>();
    EvaListAdapter adapter = new EvaListAdapter();
    Bundle bundle = new Bundle();
    TextView mv_title;
    ImageView grades;
    int recommend = 0;
    Movie movie;
    TextView participation;
    Movie movie_off;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //mArrayList=getIntent().getParcelableArrayListExtra("data");
        mRecyclerView = findViewById(R.id.eva_list);
        mRecyclerView.setNestedScrollingEnabled(false);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        evaListAdapter = new EvaListAdapter();
        //evaListAdapter.setItem(mArrayList);
        movie_off=getIntent().getParcelableExtra("movies");
        //mRecyclerView.setAdapter(evaListAdapter);
        write = findViewById(R.id.list_add);
        mv_title = findViewById(R.id.all_list_movie_title);
        grades = findViewById(R.id.all_list_movie_grade);
        int grade = getIntent().getIntExtra("movie_grade", 0);
        String title = getIntent().getStringExtra("movie_title");
        mv_title.setText(title);
        RatingBar ratingBar = findViewById(R.id.rating_bar);
        participation =findViewById(R.id.all_list_audience);
        if (grade == 12) {
            grades.setImageResource(R.drawable.ic_12);
        } else if (grade == 15) {
            grades.setImageResource(R.drawable.ic_15);
        } else if (grade == 19) {
            grades.setImageResource(R.drawable.ic_19);
        }
        Intent processedIntent = getIntent();
        if (processedIntent != null) {
            if (processedIntent.getParcelableExtra("movie") != null) {
                movie = processedIntent.getParcelableExtra("movie");
                ratingBar.setRating(movie.getAudience_rating()/2);
                participation.setText(String.format("%.1f점 (%,d명 참여)",movie.getAudience_rating(),movie.getAudience()));
            }
            if(processedIntent.getParcelableExtra("movies")!=null){
                Movie movie1=processedIntent.getParcelableExtra("movies");
                String Title=movie1.getTitle();
                mv_title.setText(Title);
                int Grades=movie1.getGrade();
                if (Grades == 12) {
                    grades.setImageResource(R.drawable.ic_12);
                } else if (Grades == 15) {
                    grades.setImageResource(R.drawable.ic_15);
                } else if (Grades == 19) {
                    grades.setImageResource(R.drawable.ic_19);
                }
            }
        }
        int NetStatus = org.androidtown.movieproject2.NetworkCheck.NetworkStatus.getConnectivityStatus(this);
        if (NetStatus == NetworkStatus.MOBILE || NetStatus == NetworkStatus.WIFI) {
            if (AppHelper.requestQueue == null) {
                AppHelper.requestQueue = Volley.newRequestQueue(this);
            }
            sendRequest();
        } else {
            processJsonFromDB();
            //Toast.makeText(this, "네트워크 연결x", Toast.LENGTH_SHORT).show();
        }
        write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(OK);
                finish();
                //startActivity(new Intent(getApplicationContext(),WriteActivity.class));
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /// 요청/응답
    public void sendRequest() {
        int id = getIntent().getIntExtra("To_All_Id", 0);
        Toast.makeText(this, id + "", Toast.LENGTH_SHORT).show();
        String ID = Integer.toString(id);
        ID += "&limit=-1";
        String url = "http://boostcourse-appapi.connect.or.kr:10000/movie/readCommentList?id=" + ID;
        Toast.makeText(this, url, Toast.LENGTH_SHORT).show();
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
            movieArrayList = movieListResult.result;
            //Toast.makeText(context, GiveMovieArray.get(0).actor, Toast.LENGTH_SHORT).show();
            //adapter.addItem(new EvaluationInfo("id1234", R.drawable.user1,"10분전", 4.0f, "추천", 0,
            //                "신고하기","11:00",
            //                "영화보고 난 뒤 남는생각이라
            //"2020-02-21 19:35:40"
            for (int i = 0; i < movieArrayList.size(); i++) {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = null;
                try {
                    date = simpleDateFormat.parse(movieArrayList.get(i).time);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                evaluationInfos.add(new EvaluationInfo(movieArrayList.get(i).id, movieArrayList.get(i).movieId, movieArrayList.get(i).writer,
                        movieArrayList.get(i).writer_image, TimeClass.formatTimeString(date) + "", movieArrayList.get(i).timestamp,
                        movieArrayList.get(i).contents,
                        movieArrayList.get(i).recommend, movieArrayList.get(i).rating, "신고하기"));
            }
            adapter.setItem(evaluationInfos);
            mRecyclerView.setAdapter(adapter);

            //db 업데이트!!!
            EvaListAdapter reviewAdapter = new EvaListAdapter();
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.getJSONArray("result");
                int index = 0;
             //   movieDBhelper.updateDatabase(movie.getId(), SettingDB.COL_COMMENTS, jsonArray.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }//processResponse

    public void processJsonFromDB() {
        Toast.makeText(getApplicationContext(), "인터넷 연결안됨", Toast.LENGTH_LONG).show();
        // DB 이용
        // processResponse 에 있던 코드

        Toast.makeText(getApplicationContext(),movie_off.getId()+"",Toast.LENGTH_LONG).show();
        ArrayList<EvaluationInfo> commentInfoArrayList = AppHelper.selectCommentList("comment", movie_off.getId());
        // 천단위 콤마
        participation.setText("("+String.format("%,d", commentInfoArrayList.size())+"명 참여)");
        // CommentAdapter 를 생성
        adapter = new EvaListAdapter();
//  public EvaluationInfo(int id,
//                          int movieId,
//                          String writer,
//                          String writer_image,
//                                  String time,
//                                  long timestamp,
//                                  String contents,
//                                  int recommend,
//                                  float rating,String report) {
//
//    }
        for (int i = 0; i < commentInfoArrayList.size(); i++) {
            EvaluationInfo commentInfo = commentInfoArrayList.get(i);
            adapter.addItem(new EvaluationInfo(commentInfo.getId(), commentInfo.getMovieId(),
                    commentInfo.getWriter(), commentInfo.getWriter_image(), commentInfo.getTime(),
                    commentInfo.getTimestamp(),commentInfo.getContents(), commentInfo.getRecommend(),
                    commentInfo.getRating(),"신고하기"));
        }

        adapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(adapter);
    }
}




