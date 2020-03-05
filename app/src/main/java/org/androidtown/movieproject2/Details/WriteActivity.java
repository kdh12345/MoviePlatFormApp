package org.androidtown.movieproject2.Details;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.androidtown.movieproject2.AppHelper;
import org.androidtown.movieproject2.Details.DetailViews.DetailFragment;
import org.androidtown.movieproject2.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class WriteActivity extends AppCompatActivity {
    // EvaluationInfo(String ID,int image,String Time,float ratingBar,String recommend,int count,String report,
    //                   String evaulates){
    final static int OK = 5;
    RatingBar ratingBar;
    EditText Comment;
    TextView store;
    TextView undo;
    //// 5장
    EvaluationInfo info;

    final String MyWriter = "zas777";
    int Id;
    ImageView Grade;
    TextView MovieTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);
        //ratingBar=findViewById(R.id.rating_set);
        //edit=findViewById(R.id.edit_rating);
        store = findViewById(R.id.store);
        undo = findViewById(R.id.undo);
        ratingBar = findViewById(R.id.rating_set);
        Comment = findViewById(R.id.edit_rating);
        Grade=findViewById(R.id.mv_grade);
        MovieTitle=findViewById(R.id.movie_title1);
        final Bundle bundle=new Bundle();
        bundle.putFloat("user_ratings",ratingBar.getRating());
        bundle.putString("comments",Comment.getText().toString());
        Id = getIntent().getIntExtra("MvID", 0);
        int grade=getIntent().getIntExtra("movie_grade",0);
        String title=getIntent().getStringExtra("movie_title");
        Toast.makeText(this, grade+"", Toast.LENGTH_SHORT).show();
        if(grade==12){
            Grade.setImageResource(R.drawable.ic_12);
        }else if(grade==15){
            Grade.setImageResource(R.drawable.ic_15);
        }else if(grade==19){
            Grade.setImageResource(R.drawable.ic_19);
        }
        MovieTitle.setText(title);
        store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                float rating = ratingBar.getRating();
                String evaluation = Comment.getText().toString();
                Intent intent = new Intent();
                DetailFragment detailFragment=new DetailFragment();
                // 이름이랑 시간은 로그인 정보에서 받아야할듯..
                /*info = new EvaluationInfo("kim1234", R.drawable.user1, "5분전", rating, "추천하기", 0, "신고하기", "12:00",
                        evaluation);
                intent.putExtra("UserInfo", info);*/
                setResult(OK, intent);
                sendRequest();
                Bundle bundle1=new Bundle();
                bundle1.putFloat("your_rating",rating);
                bundle1.putString("your_evaluation",evaluation);
                bundle1.putInt("your_id",Id);
                detailFragment.setArguments(bundle1);
                finish();
            }
        });
        undo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                //mainActivity.onFragmentSelected(0, bundle);
            }
        });

    }


    public void sendRequest() {
        String url = "http://boostcourse-appapi.connect.or.kr:10000/movie/createComment";
        final StringRequest request = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("From Web", response);
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
                float rating = ratingBar.getRating();
                String evaluation = Comment.getText().toString();
                params.put("id", Id + "");
                params.put("writer", MyWriter);
                SimpleDateFormat format1 = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss");
                Date time = new Date();
                String time1 = format1.format(time);
                params.put("time",time1);
                params.put("rating", rating + "");
                params.put("contents", evaluation);
                // 이름이랑 시간은 로그인 정보에서 받아야할듯..
                return params;
            }
        };
        request.setShouldCache(false);
        AppHelper.requestQueue.add(request);
    }

}
