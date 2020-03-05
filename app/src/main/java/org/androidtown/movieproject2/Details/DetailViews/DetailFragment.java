package org.androidtown.movieproject2.Details.DetailViews;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import org.androidtown.movieproject2.Details.AllListActivity;
import org.androidtown.movieproject2.Details.EvaListAdapter;
import org.androidtown.movieproject2.Details.EvaluationInfo;
import org.androidtown.movieproject2.Details.MovieList;
import org.androidtown.movieproject2.Details.WriteActivity;
import org.androidtown.movieproject2.Details.onBackPressedListener;
import org.androidtown.movieproject2.MainActivity;
import org.androidtown.movieproject2.Movie;
import org.androidtown.movieproject2.MovieListResult;
import org.androidtown.movieproject2.NetworkCheck.NetworkStatus;
import org.androidtown.movieproject2.R;

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
    ImageView Grade;
    TextView title;
    TextView dates;
    TextView GenreAndDuration;
    TextView ReserveGradeAndRate;
    TextView Audience;
    TextView Synopsis;
    TextView Director;
    TextView Actor;
    int likeCount;
    int dislikeCount;
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
    ViewGroup view;
    Movie movie;
    int movie_idx;
    int idx;
    ///7장
    RecyclerView recyclerView2;
    MoviePictureAdapter adapter2;
    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ///super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_detail);
        view = (ViewGroup) inflater.inflate(R.layout.activity_detail, container, false);
        context = getContext();
        movie_idx = getArguments().getInt("Idx");
        Grade = view.findViewById(R.id.grade_img);
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
        /////갤러리 기능추가 7장
        recyclerView2 = view.findViewById(R.id.gallery_list);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView2.setLayoutManager(layoutManager2);
        recyclerView2.setNestedScrollingEnabled(true);
        adapter2 = new MoviePictureAdapter();
        // gallery 클릭 했을 때 이벤트
        adapter2.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(MoviePictureAdapter.ViewHolder holder, View view, int position) {
                MoviePictureInfo item = adapter2.getItem(position);
                if(item.isMovie() == true) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(item.getYoutubeUrl()));
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(context, GalleryActivity.class);
                    intent.putExtra("url", item.getImageUrl());
                    startActivity(intent);
                }
            }
        });
        ////////////
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
            idx = id;
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
            bundle2 = getArguments();
            int NetStatus = org.androidtown.movieproject2.NetworkCheck.NetworkStatus.getConnectivityStatus(getContext());
            if (NetStatus == NetworkStatus.MOBILE || NetStatus == NetworkStatus.WIFI) {
                if (AppHelper.requestQueue == null) {
                    AppHelper.requestQueue = Volley.newRequestQueue(context);
                }
                sendRequest();
                Toast.makeText(context, "요청 성공", Toast.LENGTH_SHORT).show();
            } else if (NetStatus == NetworkStatus.NOT_CONNECTED) {
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
                                    evaluationInfos.add(new EvaluationInfo(movieArrayList.get(0).id, movieArrayList.get(0).movieId,
                                            movieArrayList.get(0).writer,
                                            movieArrayList.get(0).writer_image, TimeClass.formatTimeString(date) + "",
                                            movieArrayList.get(0).timestamp,
                                            movieArrayList.get(0).contents,
                                            movieArrayList.get(0).recommend, movieArrayList.get(0).rating, "신고하기"));
                                    evaluationInfos.add(new EvaluationInfo(movieArrayList.get(1).id, movieArrayList.get(1).movieId,
                                            movieArrayList.get(1).writer,
                                            movieArrayList.get(1).writer_image, TimeClass.formatTimeString(date) + "",
                                            movieArrayList.get(1).timestamp,
                                            movieArrayList.get(1).contents,
                                            movieArrayList.get(1).recommend, movieArrayList.get(1).rating, "신고하기"));
                                    adapter.setItem(evaluationInfos);
                                    recyclerView.setAdapter(adapter);

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
    public void sendMovieRequest(){

    }
    /// 요청/응답
    public void sendRequest() {
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
        String url1 = "http://boostcourse-appapi.connect.or.kr:10000/movie/readMovie?id=" + idx;
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
        AppHelper.requestQueue.add(request2);
    }

    ////
    public void processMovieResponse(String response) {
        Gson gson = new Gson();
        MovieListResult info = gson.fromJson(response, MovieListResult.class);
        if (info.code == 200) {
            MovieList movieArray = gson.fromJson(response, MovieList.class);
            movie = movieArray.result.get(0);
            // Gallery 추가
            if (movie.photos != null) {
                String[] photos = movie.photos.split(",");
                for (int i = 0; i < photos.length; i++) {
                    adapter2.addItem(new MoviePictureInfo(photos[i], false, null));
                }
            }
            if (movie.videos != null) {
                String[] videos = movie.videos.split(",");
                for (int i = 0; i < videos.length; i++) {
                    String ids = videos[i].substring(17);
                    String image = "https://img.youtube.com/vi/" + ids + "/0.jpg";
                    adapter2.addItem(new MoviePictureInfo(image, true, videos[i]));
                }
            }
            adapter2.notifyDataSetChanged();
            recyclerView2.setAdapter(adapter2);
            ////
        }

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
            evaluationInfos.add(new EvaluationInfo(movieArrayList.get(0).id, movieArrayList.get(0).movieId,
                    movieArrayList.get(0).writer,
                    movieArrayList.get(0).writer_image, TimeClass.formatTimeString(date) + "",
                    movieArrayList.get(0).timestamp,
                    movieArrayList.get(0).contents,
                    movieArrayList.get(0).recommend, movieArrayList.get(0).rating, "신고하기"));
            evaluationInfos.add(new EvaluationInfo(movieArrayList.get(1).id, movieArrayList.get(1).movieId,
                    movieArrayList.get(1).writer,
                    movieArrayList.get(1).writer_image, TimeClass.formatTimeString(date) + "",
                    movieArrayList.get(1).timestamp,
                    movieArrayList.get(1).contents,
                    movieArrayList.get(1).recommend, movieArrayList.get(1).rating, "신고하기"));
            adapter.setItem(evaluationInfos);
            recyclerView.setAdapter(adapter);
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
                        arrayList.add(new EvaluationInfo(movieArrayList.get(i).id, movieArrayList.get(i).movieId, movieArrayList.get(i).writer,
                                movieArrayList.get(i).writer_image, TimeClass.formatTimeString(date) + "", movieArrayList.get(i).timestamp,
                                movieArrayList.get(i).contents,
                                movieArrayList.get(i).recommend, movieArrayList.get(i).rating, "신고하기"));
                    }
                    //adapter.setItem(arrayList);
                    intent.putParcelableArrayListExtra("data", arrayList);
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("To_All_Id", idx);
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
        Toast.makeText(context, "인터넷 연결안됨", Toast.LENGTH_LONG).show();
        // DB 이용
        movie = AppHelper.selectMovieInfo("movieinfo", movie_idx);
        final Intent intent=new Intent(getContext(),AllListActivity.class);
        intent.putExtra("movies",movie);
        Log.d("Data",movie.id+"");
        // processResponse 에 있던 코드
        // glide 사용
        Glide.with(getContext())
                .load(movie.thumb)
                .into(Poster);
        title.setText(movie.title);
        if (movie.grade == 12) {
            Grade.setImageResource(R.drawable.ic_12);
        } else if (movie.grade == 15) {
            Grade.setImageResource(R.drawable.ic_15);
        } else if (movie.grade == 19) {
            Grade.setImageResource(R.drawable.ic_19);
        }

        dates.setText(movie.date + " 개봉");
        GenreAndDuration.setText(movie.genre + " / " + movie.duration + "분");
        ThumbsUpText.setText(movie.like + "");
        ThumbsDownText.setText(movie.dislike + "");
        ReserveGradeAndRate.setText(movie.reservation_grade + "위  " + movie.reservation_rate + "%");
        ratingBar.setRating(movie.audience_rating / 2);
        Audience.setText(movie.audience + "명");
        Synopsis.setText(movie.synopsis);
        Director.setText(movie.director);
        Actor.setText(movie.actor);

        likeCount = movie.like;
        dislikeCount = movie.dislike;
        if (movie.photos != null) {
            String[] photos = movie.photos.split(",");
            for (int i = 0; i < photos.length; i++) {
                adapter2.addItem(new MoviePictureInfo(photos[i], false, null));
            }
        }
        if (movie.videos != null) {
            String[] videos = movie.videos.split(",");
            for (int i = 0; i < videos.length; i++) {
                String id = videos[i].substring(17);
                String image = "https://img.youtube.com/vi/" + id + "/0.jpg";
                adapter2.addItem(new MoviePictureInfo(image, true, videos[i]));
            }
        }
        adapter2.notifyDataSetChanged();
        recyclerView2.setAdapter(adapter2);
        // processComment 에 있던 코드
        ArrayList<EvaluationInfo> commentInfoArrayList = AppHelper.selectCommentList("comment", movie_idx);
        // CommentAdapter 를 생성
        adapter = new EvaListAdapter();
// public EvaluationInfo(String id,
//                          int movieId,
//                          String writer,
//                          String writer_image,
//                                  String time,
//                                  long timestamp,
//                                  String contents,
//                                  int recommend,
//                                  float rating,String report) {
        for (int i = 0; i < 3; i++) {
            EvaluationInfo commentInfo = commentInfoArrayList.get(i);
            ///
            adapter.addItem(new EvaluationInfo(commentInfo.getId(), commentInfo.getMovieId(), commentInfo.getWriter()
                    , commentInfo.getWriter_image(), commentInfo.getTime(),
                    commentInfo.getTimestamp(), commentInfo.getContents(), commentInfo.getRecommend(), commentInfo.getRating(),
                    "신고하기"));
        }
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
        PrintAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
            }
        });
    }
}
