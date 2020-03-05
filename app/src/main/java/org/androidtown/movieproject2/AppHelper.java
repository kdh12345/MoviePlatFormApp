package org.androidtown.movieproject2;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.androidtown.movieproject2.Details.EvaluationInfo;
import org.androidtown.movieproject2.Details.MovieList;
import org.androidtown.movieproject2.NetworkCheck.NetworkStatus;

import java.util.ArrayList;

public class AppHelper {
    public static RequestQueue requestQueue;

    public static String host = "boostcourse-appapi.connect.or.kr";
    public static int port = 10000;


    private static final String TAG = "AppHelper";
    private static SQLiteDatabase database;

    private static String createTableMovieInfoSql = "create table if not exists movieinfo" +
            "( " +
            "   _id integer PRIMARY KEY autoincrement, " +
            "   id integer, " +
            "   title text," +
            "   title_eng text, " +
            "   dateValue text, " +
            "   user_rating float, " +
            "   audience_rating float, " +
            "   reviewer_rating float, " +
            "   reservation_rate float, " +
            "   reservation_grade integer, " +
            "   grade integer, " +
            "   thumb text, " +
            "   image text," +
            "   photos text," +
            "   videos text," +
            "   outlinks text," +
            "   genre text," +
            "   duration integer," +
            "   audience integer," +
            "   synopsis text," +
            "   director text," +
            "   actor text," +
            "   num_like integer," +
            "   num_dislike integer" +
            " )";

    private static String createTableCommentSql = "create table if not exists comment" +
            "( " +
            "    _id integer PRIMARY KEY autoincrement, " +
            "    id integer, " +
            "    writer text, " +
            "    movieId integer, " +
            "    writer_image text, " +
            "    time text, " +
            "    timestamp integer, " +
            "    rating float, " +
            "    contents text, " +
            "    recommend integer" +
            " )";


    public static void openDatabase(Context context, String databaseName, int version) {
        println("openDatabase 호출됨");


        // 헬퍼이용
        DatabaseHelper helper = new DatabaseHelper(context, databaseName, null, version);
        database = helper.getWritableDatabase();
        createTable(database, "movieinfo");
        createTable(database, "comment");
        // requestMovieList 안에서 나머지도 요청하도록 하였음.
      //  requestMovieList(database);

    }

    public static void createTable(SQLiteDatabase db, String tableName) {
        println("createTable 호출됨 : " + tableName);

        if (db != null) {
            if (tableName.equals("movieinfo")) {
                db.execSQL(createTableMovieInfoSql);
                println("movieInfo 테이블 생성 요청됨.");
            } else if (tableName.equals("comment")) {
                db.execSQL(createTableCommentSql);
                println("comment 테이블 생성 요청됨.");
            }
        } else {
            println("데이터베이스가 없습니다. 먼저 만들어 주세요.");
        }

    }

    public static void insertData(SQLiteDatabase db, String tableName, Movie movieInfo) {
        if (db != null) {
            String sql = "insert into " + tableName + "(id, title, title_eng, dateValue, user_rating, " +
                    "audience_rating, reviewer_rating, reservation_rate, " +
                    "reservation_grade, grade, thumb, image, photos, videos, outlinks, " +
                    "genre, duration, audience, synopsis, director, actor, num_like, num_dislike) " +
                    "values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

            Object[] params = {movieInfo.id, movieInfo.title, movieInfo.title_eng, movieInfo.date, movieInfo.user_rating,
                    movieInfo.audience_rating, movieInfo.reviewer_rating, movieInfo.reservation_rate, movieInfo.reservation_grade, movieInfo.grade, movieInfo.thumb,
                    movieInfo.image, movieInfo.photos, movieInfo.videos, movieInfo.outlinks, movieInfo.genre, movieInfo.duration, movieInfo.audience,
                    movieInfo.synopsis, movieInfo.director, movieInfo.actor, movieInfo.like, movieInfo.dislike};
            db.execSQL(sql, params);
            println("영화 정보들 추가함.");
        } else {
            println("먼저 데이터베이스를 오픈하세요.");
        }
    }
    public static void insertData(SQLiteDatabase db, String tableName, EvaluationList commentList) {
        println("insertData() 호출됨");

        if (db != null) {
            for (int i = 0; i < commentList.result.size(); i++) {
                EvaluationInfo commentInfo = commentList.result.get(i);
                String sql = "insert into " + tableName + "(id, writer, movieId, writer_image, time, timestamp, " +
                        "rating, contents, recommend) " +
                        "values(?, ?, ?, ?, ?, ?, ?, ?, ?)";

                Object[] params = {commentInfo.getId(), commentInfo.getWriter(), commentInfo.getMovieId(),
                        commentInfo.getWriter_image(), commentInfo.getTime(), commentInfo.getTimestamp(),
                        commentInfo.getRating(), commentInfo.getContents(), commentInfo.getRecommend()};
                db.execSQL(sql, params);
            }
            println("한줄평 추가함.");
        } else {
            println("먼저 데이터베이스를 오픈하세요.");
        }

    }


    // 필요한 애만 빼옴
    public static ArrayList<Movie> selectMovieList(String tableName) {
        println("selectMovieList() 호출됨.");

        ArrayList<Movie> movieInfoArrayList = new ArrayList<>();
        if (database != null) {
            String sql = "select id, title, reservation_rate, grade, image from " + tableName +
                    " order by id asc";
            Cursor cursor = database.rawQuery(sql, null);
            println("조회된 데이터 개수 : " + cursor.getCount());
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();
                Movie movieInfo = new Movie();
                movieInfo.id = cursor.getInt(0);
                movieInfo.title = cursor.getString(1);
                movieInfo.reservation_rate = cursor.getFloat(2);
                movieInfo.grade = cursor.getInt(3);
                movieInfo.image = cursor.getString(4);
                movieInfoArrayList.add(movieInfo);
            }
            cursor.close();
        }
        return movieInfoArrayList;
    }

    public static Movie selectMovieInfo(String tableName, int movieIndex) {
        println("selectMovieInfo() 호출됨.");
        Movie movieInfo = new Movie();
        if (database != null) {
            String sql = "select id, title, title_eng, dateValue, user_rating, audience_rating, reviewer_rating, reservation_rate, " +
                    "reservation_grade, grade, thumb, image, photos, videos, outlinks, " +
                    "genre, duration, audience, synopsis, director, actor, num_like, num_dislike " +
                    "from " + tableName +
                    " where id = " + movieIndex;
            Log.d("Database",sql);
            Cursor cursor = database.rawQuery(sql, null);
            if(cursor!=null&& cursor.getCount()>0){
                cursor.moveToNext();
                movieInfo.id = cursor.getInt(0);
                movieInfo.title = cursor.getString(1);
                movieInfo.title_eng = cursor.getString(2);
                movieInfo.date = cursor.getString(3);
                movieInfo.user_rating = cursor.getFloat(4);
                movieInfo.audience_rating = cursor.getFloat(5);
                movieInfo.reviewer_rating = cursor.getFloat(6);
                movieInfo.reservation_rate = cursor.getFloat(7);
                movieInfo.reservation_grade = cursor.getInt(8);
                movieInfo.grade = cursor.getInt(9);
                movieInfo.thumb = cursor.getString(10);
                movieInfo.image = cursor.getString(11);
                movieInfo.photos = cursor.getString(12);
                movieInfo.videos = cursor.getString(13);
                movieInfo.outlinks = cursor.getString(14);
                movieInfo.genre = cursor.getString(15);
                movieInfo.duration = cursor.getInt(16);
                movieInfo.audience = cursor.getInt(17);
                movieInfo.synopsis = cursor.getString(18);
                movieInfo.director = cursor.getString(19);
                movieInfo.actor = cursor.getString(20);
                movieInfo.like = cursor.getInt(21);
                movieInfo.dislike = cursor.getInt(22);
            }
            cursor.close();

        }
        return movieInfo;
    }

    public static ArrayList<EvaluationInfo> selectCommentList(String tableName, int movieIndex) {
        println("selectCommentList() 호출됨.");
        ArrayList<EvaluationInfo> commentInfoArrayList = new ArrayList<>();
        if (database != null) {
            String sql = "select id, writer, movieId, writer_image, time, timestamp, " +
                    "rating, contents, recommend " +
                    "from " + tableName +
                    " where movieId = " + movieIndex +
                    " order by timestamp desc";
            Cursor cursor = database.rawQuery(sql, null);
            println("조회된 데이터 개수 : " + cursor.getCount());
            for (int i = 0; i < cursor.getCount(); i++) {
                cursor.moveToNext();
                int id = cursor.getInt(0);
                String writer = cursor.getString(1);
                int movieId = cursor.getInt(2);
                String writer_image = cursor.getString(3);
                String time = cursor.getString(4);
                int timestamp = cursor.getInt(5);
                float rating = cursor.getFloat(6);
                String contents = cursor.getString(7);
                int recommend = cursor.getInt(8);
                // public EvaluationInfo(String id,
                //                          int movieId,
                //                          String writer,
                //                          String writer_image,
                //                                  String time,
                //                                  long timestamp,
                //                                  String contents,
                //                                  int recommend,
                //                                  float rating,String report) {
                EvaluationInfo commentInfo = new EvaluationInfo(id, movieId, writer, writer_image, time, timestamp, contents,
                        recommend, rating, "신고하기");
                commentInfoArrayList.add(commentInfo);
            }
            cursor.close();
        }
        return commentInfoArrayList;
    }


    public static void println(String data) {
        Log.d(TAG, data);
    }

    // 데이터베이스 헬퍼
    static class DatabaseHelper extends SQLiteOpenHelper {

        Context context;

        public DatabaseHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
            this.context = context;

            if (requestQueue == null) {
                requestQueue = Volley.newRequestQueue(context);
            }
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            println("onCreate() 호출됨");

            // 연결 상태 확인
            int networkStatus = NetworkStatus.getConnectivityStatus(context);
            if (networkStatus == NetworkStatus.MOBILE) {
                Toast.makeText(context, "모바일로 연결됨", Toast.LENGTH_LONG).show();
                sameTasks(db);

            } else if (networkStatus == NetworkStatus.WIFI) {
                Toast.makeText(context, "WIFI로 연결됨", Toast.LENGTH_LONG).show();
                sameTasks(db);

            } else {
                Toast.makeText(context, "인터넷이 연결되지 않아 데이터를 DB에 저장할 수 없습니다.", Toast.LENGTH_LONG).show();
            }
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            println("onUpgrade() 호출됨: " + oldVersion + "," + newVersion);

            if (newVersion > oldVersion) {
                // 보통은 얼터테이블 해서 칼럼만 변경해줌
                db.execSQL("drop table if exists movieInfo");
                db.execSQL("drop table if exists comment");
                println("테이블 삭제함");

                // 연결 상태 확인
                int networkStatus = NetworkStatus.getConnectivityStatus(context);
                if (networkStatus == NetworkStatus.MOBILE) {
                    Toast.makeText(context, "모바일로 연결됨", Toast.LENGTH_LONG).show();
                    sameTasks(db);

                } else if (networkStatus == NetworkStatus.WIFI) {
                    Toast.makeText(context, "WIFI로 연결됨", Toast.LENGTH_LONG).show();
                    sameTasks(db);

                } else {
                    Toast.makeText(context, "인터넷이 연결되지 않아 데이터를 DB에 저장할 수 없습니다.", Toast.LENGTH_LONG).show();
                }

            }

        }

        public void sameTasks(SQLiteDatabase db) {
            //createTable("outline");
            createTable(db, "movieinfo");
            createTable(db, "comment");

            // requestMovieList 안에서 나머지도 요청하도록 하였음.
            requestMovieList(db);

        }


    }

    private static void requestMovieList(final SQLiteDatabase db) {
        String url = "http://" + host + ":" + port + "/movie/readMovieList";
        url += "?" + "type=1";

        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("성공", "응답 받음 -> " + response);

                        processMovieList(db, response);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("실패", "에러 발생 -> " + error.getMessage());
                    }
                }
        );

        request.setShouldCache(false);
        requestQueue.add(request);
    }

    private static void processMovieList(SQLiteDatabase db, String response) {
        Gson gson = new Gson();
        MovieListResult info = gson.fromJson(response, MovieListResult.class);
        if (info.code == 200) {
            MovieList movieList = gson.fromJson(response, MovieList.class);
            for (int i = 0; i < movieList.result.size(); i++) {
                Movie movieInfo = movieList.result.get(i);
                requestMovieInfo(db, movieInfo.id);
                requestCommentList(db, movieInfo.id);
            }

        }
    }

    private static void requestMovieInfo(final SQLiteDatabase db, int movieIndex) {
        String url = "http://" + host + ":" + port + "/movie/readMovie";
        url += "?id=" + movieIndex;

        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("성공", "응답 받음 -> " + response);

                        processMovieInfo(db, response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("실패", "에러 발생 -> " + error.getMessage());
                    }
                }
        );

        request.setShouldCache(false);
        requestQueue.add(request);
    }

    private static void processMovieInfo(SQLiteDatabase db, String response) {
        Gson gson = new Gson();

        MovieListResult info = gson.fromJson(response, MovieListResult.class);
        if (info.code == 200) {
            MovieList movieArray = gson.fromJson(response, MovieList.class);
            Movie movieInfo = movieArray.result.get(0);

            insertData(db, "movieinfo", movieInfo);
        }

    }

    private static void requestCommentList(final SQLiteDatabase db, int movieIndex) {
        String url = "http://" + host + ":" + port + "/movie/readCommentList";
        url += "?id=" + movieIndex + "&limit=-1";
        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("성공", "응답 받음 -> " + response);
                        processComment(db, response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("실패", "에러 발생 -> " + error.getMessage());
                    }
                }
        );

        request.setShouldCache(false);
        requestQueue.add(request);
    }

    private static void processComment(SQLiteDatabase db, String response) {
        Gson gson = new Gson();
        MovieListResult info = gson.fromJson(response, MovieListResult.class);
        if (info.code == 200) {
            EvaluationList commentList = gson.fromJson(response, EvaluationList.class);
            insertData(db, "comment", commentList);
        }
    }

}

