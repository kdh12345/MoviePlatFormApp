package org.androidtown.movieproject2.Details;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.androidtown.movieproject2.AppHelper;
import org.androidtown.movieproject2.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EvaListAdapter extends RecyclerView.Adapter<EvaListAdapter.ViewHolder> {
    ArrayList<EvaluationInfo> EvaLists=new ArrayList<>();
    String recommend_count;
    public class ViewHolder extends RecyclerView.ViewHolder{
        protected TextView textView;
        protected ImageView imageView;
        protected TextView user_times;
        protected TextView ReportTimes;

        protected RatingBar ratingBar;

        protected TextView recommends;

        protected TextView recommendsCounts;

        protected TextView Reports;
        protected TextView Evaluates;
        ViewHolder(View view){
            super(view);
            this.textView=view.findViewById(R.id.user_id);
            this.imageView=view.findViewById(R.id.user_images);
            this.user_times=view.findViewById(R.id.user_times);
            this.ReportTimes=view.findViewById(R.id.report_time);
            this.ratingBar=view.findViewById(R.id.user_rating);
            this.recommends=view.findViewById(R.id.recommends);
            this.recommendsCounts=view.findViewById(R.id.recommends_count);
            this.Reports=view.findViewById(R.id.user_report);
            this.Evaluates=view.findViewById(R.id.user_evaluate);
        }
    }
    public void addItem(EvaluationInfo info){
        EvaLists.add(info);
    }
    public void setItem(ArrayList<EvaluationInfo> info){
        EvaLists=info;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.evalutionlist, parent, false);
        TextView recommend=itemView.findViewById(R.id.recommends_count);
        recommend_count=recommend.getText().toString();
        recommend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRecommend();
            }
        });
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
          EvaluationInfo evaluationInfo=EvaLists.get(position);
           holder.textView.setText(evaluationInfo.getWriter());
           holder.imageView.setImageResource(R.drawable.user1);
           holder.user_times.setText(evaluationInfo.getTime());
          // holder.ReportTimes.setText(evaluationInfo.getTime());
           holder.ratingBar.setRating(evaluationInfo.getRating());
           holder.recommends.setText("추천");
           String tmp=evaluationInfo.getRecommend()+"";
           holder.recommendsCounts.setText(tmp);
           holder.Reports.setText(evaluationInfo.getReport());
           holder.Evaluates.setText(evaluationInfo.getContents());
    }

    @Override
    public int getItemCount() {
        return EvaLists.size();
    }

    public ArrayList<EvaluationInfo> getLists(){
        return EvaLists;
    }
    public EvaluationInfo getItem(int position) {
        return EvaLists.get(position);
    }
    /// 요청/응답
    public void sendRecommend(){
        String url = "http://boostcourse-appapi.connect.or.kr:10000/movie/increaseRecommend";
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
                int cnt=Integer.parseInt(recommend_count);
                int IncreasedRecommend=cnt++;
                params.put("recommend",Integer.toString(IncreasedRecommend));
                // 이름이랑 시간은 로그인 정보에서 받아야할듯..
                return params;
            }
        };
        request.setShouldCache(false);
        AppHelper.requestQueue.add(request);
    }
}
