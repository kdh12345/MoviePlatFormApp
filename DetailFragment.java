package org.androidtown.movieproject2.Details.DetailViews;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import org.androidtown.movieproject2.Details.AllListActivity;
import org.androidtown.movieproject2.Details.EvaListAdapter;
import org.androidtown.movieproject2.Details.EvaluationInfo;
import org.androidtown.movieproject2.Details.WriteActivity;
import org.androidtown.movieproject2.Details.onBackPressedListener;
import org.androidtown.movieproject2.MainActivity;
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

public class DetailFragment extends Fragment implements onBackPressedListener {
    public static DetailFragment newInstance() {
        return new DetailFragment();
    }

    //// 5장!!!
    Bundle bundle;
    Bundle bundle2;
    ArrayList<Movie> arrayList;
    ArrayList<Movie> RecreateList = new ArrayList<>();
    ImageView Poster;
    TextView title;
    TextView dates;
    TextView GenreAndDuration;
    TextView ReserveGradeAndRate;
    TextView Audience;
    TextView Synopsis;
    TextView Director;
    TextView Actor;
    /////
    ListView listView;
    ArrayList<EvaluationInfo> evaluationInfos = new ArrayList<>();
    //String ID,int image,String Time,float ratingBar,String recommend,int count,String report,
    //                   String evaulates)
    ImageView ThumbsUpView;
    ImageView ThumbsDownView;
    TextView ThumbsUpText;
    TextView ThumbsDownText;
    boolean Like = false;
    boolean Unlike = false;
    RatingBar ratingBar;
    TextView evaluate;
    TextView write;
    TextView PrintAll;
    final static int OK = 5;
    // 요청 코드
    public static final int REQUEST_CODE_WRITE = 101;
    public static final int REQUEST_CODE_LOOK = 100;
    RecyclerView recyclerView;
    EvaListAdapter adapter = new EvaListAdapter();
    ReviewAdapter reviewAdapter = new ReviewAdapter();
    Context context;
    ArrayList<Movie> movieArrayList;
    Bundle new_bundle = new Bundle();//디테일로 보내는 번들
    String url;
    String MyWriter = "zas777";
    Intent intent;
    ///6장
    MovieDBhelper movieDBhelper;
    ViewGroup view;
    Movie movie;
    int movie_id;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ///super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_detail);
        view = (ViewGroup) inflater.inflate(R.layout.activity_detail, container, false);
        context = getContext();

        //db연결
        movieDBhelper = new MovieDBhelper(context, "movie.db", null, 1);
        ThumbsUpView = view.findViewById(R.id.thumbs_up);
        ThumbsUpText = view.findViewById(R.id.thumbs_up_text);
        ThumbsDownView = view.findViewById(R.id.thumbs_down);
        ThumbsDownText = view.findViewById(R.id.thumbs_down_text);
        ratingBar = view.findViewById(R.id.rating_evaluates);
        evaluate = view.findViewById(R.id.evaluate_rating);
        // ratingBar.setRating(4.5f);
        //evaluate.setText("8.2");
        recyclerView = view.findViewById(R.id.evaluation_list);
        // 리사이클러뷰안에 수직방향 리니어레이어를 set

        //  fragmentTransaction.add(R.id.contentMain,FirstFragment.newInstance());
        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        // 기본 스크롤 기능 안되도록 (모두 보기를 이용하도록 하기 위한 조치)
        recyclerView.setNestedScrollingEnabled(false);
        bundle = this.getArguments();
        write = view.findViewById(R.id.write_evaluates);
        PrintAll = view.findViewById(R.id.all_view_btn);
        ThumbsUpView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tmp = ThumbsUpText.getText().toString();
                int LikeCnt = Integer.parseInt(tmp);
                String tmp2 = ThumbsDownText.getText().toString();
                int UnlikeCnt = Integer.parseInt(tmp2);
                if (Like == false) {
                    Like = true;
                    LikeCnt++;
                    ThumbsUpText.setText(LikeCnt + "");
                    ThumbsUpView.setImageResource(R.drawable.ic_thumb_up_selected);
                    if (Unlike == true) {
                        Unlike = false;
                        UnlikeCnt--;
                        ThumbsDownText.setText(UnlikeCnt + "");
                        ThumbsDownView.setImageResource(R.drawable.ic_thumb_down);
                    }
                } else if (Like == true) {
                    Like = false;
                    LikeCnt--;
                    if (LikeCnt < 0)
                        LikeCnt = 0;
                    ThumbsUpText.setText(LikeCnt + "");
                    ThumbsUpView.setImageResource(R.drawable.ic_thumb_up);
                }
                sendThumb();
            }//onClick메소드
        });//좋아요
        ThumbsDownView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String likeString = ThumbsUpText.getText().toString();
                String unlikeString = ThumbsDownText.getText().toString();
                int likeCount = Integer.parseInt(likeString);
                int unlikeCount = Integer.parseInt(unlikeString);
                if (Unlike == false) {
                    Unlike = true;
                    unlikeCount++;
                    ThumbsDownText.setText(unlikeCount + "");
                    ThumbsDownView.setImageResource(R.drawable.ic_thumb_down_selected);
                    if (Like == true) {
                        Like = false;
                        likeCount--;
                        if (likeCount < 0)
                            likeCount = 0;
                        ThumbsUpText.setText(likeCount + "");
                        ThumbsUpView.setImageResource(R.drawable.ic_thumb_up);
                    }
                } else if (Unlike == true) {
                    unlikeCount--;
                    if (unlikeCount < 0)
                        unlikeCount = 0;
                    ThumbsDownText.setText(unlikeCount + "");
                    ThumbsDownView.setImageResource(R.drawable.ic_thumb_down);
                }
                sendThumb();
            }//onClick메소드
        });//싫어요
        write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), WriteActivity.class);
                int id = bundle.getInt("MvID");
                int grade = bundle.getInt("movie_grade");
                String title = bundle.getString("movie_title");
                intent.putExtra("MvID", movieArrayList.get(id).movieId);
                intent.putExtra("movie_grade", grade);
                intent.putExtra("movie_title", title);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                //finish();
                startActivityForResult(intent, REQUEST_CODE_WRITE);
            }
        });//작성하기 클릭

        /*Toolbar toolbar = (Toolbar)view.findViewById(R.id.detail_toolbar);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false); // 기존 title 지우기
        actionBar.setTitle("영화 상세");
        actionBar.setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼 만들기
        actionBar.setHomeAsUpIndicator(R.drawable.ic_hamburger_menu); //뒤로가기 버튼 이미지 지정
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        //////
        mDrawerLayout = (DrawerLayout) view.findViewById(R.id.drawer_layout_detail);
        NavigationView navigationView = (NavigationView) view.findViewById(R.id.nav_detail_view);*/

        if (bundle != null) {
            bundle = getArguments();
            arrayList = bundle.getParcelableArrayList("movie_data_detail");
            RecreateList = bundle.getParcelableArrayList("movie");
            Poster = view.findViewById(R.id.movie_poster);
            title = view.findViewById(R.id.mv_title);
            dates = view.findViewById(R.id.mv_date);
            GenreAndDuration = view.findViewById(R.id.genre_and_duration);
            ReserveGradeAndRate = view.findViewById(R.id.reserved_grade_and_rate);
            Audience = view.findViewById(R.id.tv_audience);
            Synopsis = view.findViewById(R.id.story);
            Director = view.findViewById(R.id.mv_director);
            Actor = view.findViewById(R.id.mv_actor);
            final int id = bundle.getInt("MvID");
            movie_id = bundle.getInt("MvID");
            switch (id) {
                case 1: {
                    Glide.with(context).load(arrayList.get(0).thumb).into(Poster);
                    title.setText(arrayList.get(0).title);
                    dates.setText(arrayList.get(0).date);
                    GenreAndDuration.setText(arrayList.get(0).genre + "/" + arrayList.get(0).duration + "분");
                    ThumbsUpText.setText(arrayList.get(0).like + "");
                    ThumbsDownText.setText(arrayList.get(0).dislike + "");
                    ReserveGradeAndRate.setText(arrayList.get(0).reservation_grade + "위" + arrayList.get(0).reservation_rate + " %");
                    float result = (arrayList.get(0).audience_rating + arrayList.get(0).reviewer_rating) / 2.0f;
                    ratingBar.setRating(result);
                    evaluate.setText(Math.round(result * 100) / 100.0f + "");
                    Audience.setText(arrayList.get(0).audience + "");
                    Synopsis.setText(arrayList.get(0).synopsis);
                    Director.setText(arrayList.get(0).director);
                    Actor.setText(arrayList.get(0).actor);
                    String ID = Integer.toString(id);
                    url = "http://boostcourse-appapi.connect.or.kr:10000/movie/readCommentList?id=" + ID;
                    //Toast.makeText(context, url, Toast.LENGTH_LONG).show();
                }
                case 2: {
                    Glide.with(context).load(arrayList.get(0).thumb).into(Poster);
                    title.setText(arrayList.get(0).title);
                    dates.setText(arrayList.get(0).date);
                    GenreAndDuration.setText(arrayList.get(0).genre + "/" + arrayList.get(0).duration + "분");
                    ThumbsUpText.setText(arrayList.get(0).like + "");
                    ThumbsDownText.setText(arrayList.get(0).dislike + "");
                    ReserveGradeAndRate.setText(arrayList.get(0).reservation_grade + "위" + arrayList.get(0).reservation_rate + " %");
                    float result = (arrayList.get(0).audience_rating + arrayList.get(0).reviewer_rating) / 2.0f;
                    ratingBar.setRating(result);
                    evaluate.setText(Math.round(result * 100) / 100.0f + "");
                    Audience.setText(arrayList.get(0).audience + "");
                    Synopsis.setText(arrayList.get(0).synopsis);
                    Director.setText(arrayList.get(0).director);
                    Actor.setText(arrayList.get(0).actor);
                    String ID = Integer.toString(id);
                    url = "http://boostcourse-appapi.connect.or.kr:10000/movie/readCommentList?id=" + ID;
                    // sendRequest();

                }
                case 3: {
                    Glide.with(context).load(arrayList.get(0).thumb).into(Poster);
                    Log.d("search", arrayList.get(0).thumb);
                    title.setText(arrayList.get(0).title);
                    dates.setText(arrayList.get(0).date);
                    GenreAndDuration.setText(arrayList.get(0).genre + "/" + arrayList.get(0).duration + "분");
                    ThumbsUpText.setText(arrayList.get(0).like + "");
                    ThumbsDownText.setText(arrayList.get(0).dislike + "");
                    ReserveGradeAndRate.setText(arrayList.get(0).reservation_grade + "위" + arrayList.get(0).reservation_rate + " %");
                    float result = (arrayList.get(0).audience_rating + arrayList.get(0).reviewer_rating) / 2.0f;
                    ratingBar.setRating(result);
                    evaluate.setText(Math.round(result * 100) / 100.0f + "");
                    Audience.setText(arrayList.get(0).audience + "");
                    Synopsis.setText(arrayList.get(0).synopsis);
                    Director.setText(arrayList.get(0).director);
                    Actor.setText(arrayList.get(0).actor);
                    String ID = Integer.toString(id);
                    url = "http://boostcourse-appapi.connect.or.kr:10000/movie/readCommentList?id=" + ID;
                    // sendRequest();

                }
                case 4: {
                    Glide.with(context).load(arrayList.get(0).thumb).into(Poster);
                    title.setText(arrayList.get(0).title);
                    dates.setText(arrayList.get(0).date);
                    GenreAndDuration.setText(arrayList.get(0).genre + "/" + arrayList.get(0).duration + "분");
                    ThumbsUpText.setText(arrayList.get(0).like + "");
                    ThumbsDownText.setText(arrayList.get(0).dislike + "");
                    ReserveGradeAndRate.setText(arrayList.get(0).reservation_grade + "위" + arrayList.get(0).reservation_rate + " %");
                    float result = (arrayList.get(0).audience_rating + arrayList.get(0).reviewer_rating) / 2.0f;
                    ratingBar.setRating(result);
                    evaluate.setText(Math.round(result * 100) / 100.0f + "");
                    Audience.setText(arrayList.get(0).audience + "");
                    Synopsis.setText(arrayList.get(0).synopsis);
                    Director.setText(arrayList.get(0).director);
                    Actor.setText(arrayList.get(0).actor);
                    String ID = Integer.toString(id);
                    url = "http://boostcourse-appapi.connect.or.kr:10000/movie/readCommentList?id=" + ID;
                    // sendRequest();

                }
                case 5: {
                    Glide.with(context).load(arrayList.get(0).thumb).into(Poster);
                    title.setText(arrayList.get(0).title);
                    dates.setText(arrayList.get(0).date);
                    GenreAndDuration.setText(arrayList.get(0).genre + "/" + arrayList.get(0).duration + "분");
                    ThumbsUpText.setText(arrayList.get(0).like + "");
                    ThumbsDownText.setText(arrayList.get(0).dislike + "");
                    ReserveGradeAndRate.setText(arrayList.get(0).reservation_grade + "위" + arrayList.get(0).reservation_rate + " %");
                    float result = (arrayList.get(0).audience_rating + arrayList.get(0).reviewer_rating) / 2.0f;
                    ratingBar.setRating(result);
                    evaluate.setText(Math.round(result * 100) / 100.0f + "");
                    Audience.setText(arrayList.get(0).audience + "");
                    Synopsis.setText(arrayList.get(0).synopsis);
                    Director.setText(arrayList.get(0).director);
                    Actor.setText(arrayList.get(0).actor);
                    String ID = Integer.toString(id);
                    url = "http://boostcourse-appapi.connect.or.kr:10000/movie/readCommentList?id=" + ID;
                    // sendRequest();

                }
            }
            bundle2=getArguments();
            int NetStatus = org.androidtown.movieproject2.NetworkCheck.NetworkStatus.getConnectivityStatus(getContext());
            if (NetStatus == NetworkStatus.TYPE_MOBILE || NetStatus == NetworkStatus.TYPE_WIFI) {
                if (AppHelper.requestQueue == null) {
                    AppHelper.requestQueue = Volley.newRequestQueue(context);
                }

                sendRequest();
                Toast.makeText(context, "요청 성공", Toast.LENGTH_SHORT).show();
            } else if (NetStatus == NetworkStatus.TYPE_NOT_CONNECTED) {
                processFromDB();
                //processFromDbComments();
                Toast.makeText(context, "이미 저장된 db만 보여줌", Toast.LENGTH_SHORT).show();
            }


        }

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_WRITE) {
            if (resultCode == OK) {
                Toast.makeText(getActivity(), "저장 버튼 클릭", Toast.LENGTH_SHORT).show();
                // Parcelable interface 사용
                //Bundle bundle = data.getBundleExtra("Bundle");
//                evaluationInfos = bundle.getParcelableArrayList("data");
                //adapter.addItem(evaluationInfos.get(evaluationInfos.size()-1));
                // Parcelable interface 사용
                //evaluationInfos.add(new EvaluationInfo(movieArrayList.get(0).writer, R.drawable.user1, TimeClass.formatTimeString(date) + "",
                // movieArrayList.get(0).rating,
                //                    "추천", movieArrayList.get(0).recommend, "신고하기", "", movieArrayList.get(0).contents));
                //  Bundle bundle = data.getExtras();
                StringRequest request = new StringRequest(
                        Request.Method.GET,
                        url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Gson gson = new Gson();
                                MovieListResult movieListResult = gson.fromJson(response, MovieListResult.class);
                                if (movieListResult != null) {
                                    movieArrayList = movieListResult.result;
                                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    Date date = null;
                                    try {
                                        date = simpleDateFormat.parse(movieArrayList.get(0).time);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    evaluationInfos.clear();
                                    int id = bundle.getInt("MvID");
                                    evaluationInfos.add(new EvaluationInfo(movieArrayList.get(0).id + "", movieArrayList.get(0).movieId,
                                            movieArrayList.get(0).writer,
                                            movieArrayList.get(0).writer_image, TimeClass.formatTimeString(date) + "",
                                            movieArrayList.get(0).timestamp,
                                            movieArrayList.get(0).contents,
                                            movieArrayList.get(0).recommend, movieArrayList.get(0).rating, "신고하기"));
                                    evaluationInfos.add(new EvaluationInfo(movieArrayList.get(1).id + "", movieArrayList.get(1).movieId,
                                            movieArrayList.get(1).writer,
                                            movieArrayList.get(1).writer_image, TimeClass.formatTimeString(date) + "",
                                            movieArrayList.get(1).timestamp,
                                            movieArrayList.get(1).contents,
                                            movieArrayList.get(1).recommend, movieArrayList.get(1).rating, "신고하기"));
                                    adapter.setItem(evaluationInfos);
                                    recyclerView.setAdapter(adapter);

                                }
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                                    int index = 0;
                                    while (index < jsonArray.length()) {
                                        Movie movie = gson.fromJson(jsonArray.get(index).toString(), Movie.class);
                                        movieDBhelper.updateDatabase(movie.getId(), SettingDB.COL_DETAIL, jsonArray.get(index).toString());
                                        index++;
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
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
            } else {
                Toast.makeText(getActivity(), "취소 버튼 클릭", Toast.LENGTH_SHORT).show();
            }

        } else if (requestCode == REQUEST_CODE_LOOK) {
            if (resultCode == OK) {
                // 모두보기에서 작성하기 버튼 눌럿을 때
                Toast.makeText(getActivity(), "작성하기 버튼 클릭", Toast.LENGTH_SHORT).show();
                intent = new Intent(getActivity(), WriteActivity.class);
                int id = bundle.getInt("MvID", 0);
                int grade = bundle.getInt("movie_grade", 0);
                String title = bundle.getString("movie_title");
                intent.putExtra("MvID", id);
                intent.putExtra("movie_grade", grade);
                intent.putExtra("movie_title", title);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(intent, REQUEST_CODE_WRITE);
            }
        }
    }

    public void onBackPressed() {
        //  super.onBackPressed();
        getActivity().finish();
        startActivity(new Intent(getActivity(), MainActivity.class));
    }

    /// 요청/응답
    public void sendRequest() {
        String url = "http://boostcourse-appapi.connect.or.kr:10000/movie/readCommentList?id=" + movie_id;
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
        ///
        String url1 = "http://boostcourse-appapi.connect.or.kr:10000/movie/readMovie?id=" + movie_id;
        StringRequest request2 = new StringRequest(
                Request.Method.GET,
                url1,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        processMovieResponse(response);
                       // recreate();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }

        );

        request2.setShouldCache(false);
        AppHelper.requestQueue.add(request);
    }

    ////
    public void processMovieResponse(String response) {
        Gson gson = new Gson();
        Movie movie = new Movie();
        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray jsonArray = jsonObject.getJSONArray("result");
            int index = 0;
            while (index < jsonArray.length()) {
                movie = gson.fromJson(jsonArray.get(index).toString(), Movie.class);
                movieDBhelper.updateDatabase(movie.getId(), SettingDB.COL_DETAIL, jsonArray.get(index).toString());
                index++;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        this.movie = movie;
    }

    public void processResponse(String response) {
        Gson gson = new Gson();
        MovieListResult movieListResult = gson.fromJson(response, MovieListResult.class);
        Movie movie = new Movie();
        if (movieListResult != null) {
            movieArrayList = movieListResult.result;
            //Toast.makeText(context, GiveMovieArray.get(0).actor, Toast.LENGTH_SHORT).show();
            //adapter.addItem(new EvaluationInfo("id1234", R.drawable.user1,"10분전", 4.0f, "추천", 0,
            //                "신고하기","11:00",
            //                "영화보고 난 뒤 남는생각이라
            //"2020-02-21 19:35:40"
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = null;
            try {
                date = simpleDateFormat.parse(movieArrayList.get(0).time);
            } catch (Exception e) {
                e.printStackTrace();
            }
            int id = bundle.getInt("MvID");
            evaluationInfos.clear();
            evaluationInfos.add(new EvaluationInfo(movieArrayList.get(0).id + "", movieArrayList.get(0).movieId,
                    movieArrayList.get(0).writer,
                    movieArrayList.get(0).writer_image, TimeClass.formatTimeString(date) + "",
                    movieArrayList.get(0).timestamp,
                    movieArrayList.get(0).contents,
                    movieArrayList.get(0).recommend, movieArrayList.get(0).rating, "신고하기"));
            evaluationInfos.add(new EvaluationInfo(movieArrayList.get(1).id + "", movieArrayList.get(1).movieId,
                    movieArrayList.get(1).writer,
                    movieArrayList.get(1).writer_image, TimeClass.formatTimeString(date) + "",
                    movieArrayList.get(1).timestamp,
                    movieArrayList.get(1).contents,
                    movieArrayList.get(1).recommend, movieArrayList.get(1).rating, "신고하기"));

            adapter.setItem(evaluationInfos);
            recyclerView.setAdapter(adapter);
            Movie movie1=new Movie();
            DetailFragment detailFragment=new DetailFragment();
            try {
                JSONObject jsonObject = new JSONObject(response);
                JSONArray jsonArray = jsonObject.getJSONArray("result");
                int index = 0;
                while(index<jsonArray.length()){
                    movie1 = gson.fromJson(jsonArray.get(index).toString(), Movie.class);
                    movieDBhelper.updateDatabase(movie1.getId(), SettingDB.COL_SIMPLE_COMMENTS, jsonArray.get(index).toString());
                    index++;
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            //////////
            PrintAll.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), AllListActivity.class);
                    // ParcelableArray 로 intent에 담아 전달
                    int grade = bundle.getInt("movie_grade", 0);
                    String title = bundle.getString("movie_title");
                    intent.putExtra("movie_grade", grade);
                    intent.putExtra("movie_title", title);
                    ArrayList<EvaluationInfo> arrayList = new ArrayList<>();
                    for (int i = 0; i < movieArrayList.size(); i++) {
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        Date date = null;
                        try {
                            date = simpleDateFormat.parse(movieArrayList.get(i).time);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        /*public EvaluationInfo(String id,
                          int movieId,
                          String writer,
                          String writer_image,
                                  String time,
                                  long timestamp,
                                  String contents,
                                  int recommend,
                                  float rating,String report)
                        * */
                        arrayList.add(new EvaluationInfo(movieArrayList.get(i).id + "", movieArrayList.get(i).movieId, movieArrayList.get(i).writer,
                                movieArrayList.get(i).writer_image, TimeClass.formatTimeString(date) + "", movieArrayList.get(i).timestamp,
                                movieArrayList.get(i).contents,
                                movieArrayList.get(i).recommend, movieArrayList.get(i).rating, "신고하기"));
                    }
                    //adapter.setItem(arrayList);
                    intent.putParcelableArrayListExtra("data", arrayList);
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("To_All_Id", movie_id);
                    // finish();
                    startActivityForResult(intent, REQUEST_CODE_LOOK);

                }
            });

        }


    }

    public void sendThumb() {
        String url2 = "http://boostcourse-appapi.connect.or.kr:10000/movie/increaseLikeDisLike";
        StringRequest request = new StringRequest(
                Request.Method.POST,
                url2,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(context, "성공!!!", Toast.LENGTH_SHORT).show();
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
                int id = bundle.getInt("MvID");
                params.put("id", id + "");
                params.put("likeyn", ThumbsUpText.getText().toString());
                params.put("dislikeyn", ThumbsDownText.getText().toString());
                return params;
            }
        };
        request.setShouldCache(false);
        AppHelper.requestQueue.add(request);
    }
    public void processFromDB() {
        Gson gson = new Gson();
        Movie movie = new Movie();
        try {
            String jsonString = movieDBhelper.getDataFromDB(SettingDB.COL_DETAIL, movie.getId());
            movie = gson.fromJson(jsonString, Movie.class);
         //   Log.d("Database", movie.getTitle() + "movie parsed");
        } catch (Exception e) {
            e.printStackTrace();
        }
        Movie movie1=new Movie();
       // Log.d("Database", movie.getId() + "movie parsed");
        try {
            String jsonString = movieDBhelper.getDataFromDB(SettingDB.COL_SIMPLE_COMMENTS, movie.getId());
            movie1 = gson.fromJson(jsonString, Movie.class);

        } catch (Exception e) {
            e.printStackTrace();
        }
        // public EvaluationInfo(String id,
        //                          int movieId,
        //                          String writer,
        //                          String writer_image,
        //                                  String time,
        //                                  long timestamp,
        //                                  String contents,
        //                                  int recommend,
        //                                  float rating,String report) {
        try {

        }catch (Exception e){
            e.printStackTrace();
        }
        evaluationInfos.add(new EvaluationInfo(""+movie1.getId(),movie1.getMovieId(),movie1.getWriter(),
                R.drawable.user1+"",movie1.getTime(),movie1.getTimestamp(),movie1.getContents(),movie1.getRecommend(),
                movie1.getRating(),"신고하기"));
        adapter.setItem(evaluationInfos);
        recyclerView.setAdapter(adapter);
        this.movie = movie;
        recreate();
        PrintAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), AllListActivity.class));
            }
        });
    }

    public void recreate() {
        ArrayList<ReviewItem> items = new ArrayList<>();
        //Movie 객체에 정보들을 입력
        Poster = view.findViewById(R.id.movie_poster);
        title = view.findViewById(R.id.mv_title);
        dates = view.findViewById(R.id.mv_date);
        GenreAndDuration = view.findViewById(R.id.genre_and_duration);
        ReserveGradeAndRate = view.findViewById(R.id.reserved_grade_and_rate);
        Audience = view.findViewById(R.id.tv_audience);
        Synopsis = view.findViewById(R.id.story);
        Director = view.findViewById(R.id.mv_director);
        Actor = view.findViewById(R.id.mv_actor);
        /////////////////
        Bundle bundle = new Bundle();
        bundle = getArguments();
        Movie movie = bundle.getParcelable("movie");
        Glide.with(context).load(movie.getThumb()).into(Poster);
        title.setText(movie.getTitle());
        dates.setText(movie.getDate());
        GenreAndDuration.setText(movie.getGenre() + "/" + movie.getDuration() + "분");
        ThumbsUpText.setText(movie.getLike() + "");
        ThumbsDownText.setText(movie.getDislike() + "");
        ReserveGradeAndRate.setText(movie.getReservation_grade() + "위" + movie.getReservation_rate() + " %");
        float result = (movie.getAudience_rating() + movie.getReviewer_rating()) / 2.0f;
        ratingBar.setRating(result);
        evaluate.setText(Math.round(result * 100) / 100.0f + "");
        Toast.makeText(getContext(), movie.getAudience() + "", Toast.LENGTH_SHORT).show();
        Audience.setText(movie.getAudience() + "");
        Synopsis.setText(movie.getSynopsis());
        Director.setText(movie.getDirector());
        Actor.setText(movie.getActor());


    }

}
