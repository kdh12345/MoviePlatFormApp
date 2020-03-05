package org.androidtown.movieproject2.Details.DetailViews;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.androidtown.movieproject2.Details.EvaListAdapter;
import org.androidtown.movieproject2.Details.EvaluationInfo;
import org.androidtown.movieproject2.R;

import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ViewHolder>{
    ArrayList<ReviewItem> EvaLists=new ArrayList<>();
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
    public void addItem(ReviewItem info){
        EvaLists.add(info);
    }
    public void setItem(ArrayList<ReviewItem> info){
        EvaLists=info;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.evalutionlist, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ReviewItem reviewItem=EvaLists.get(position);
        holder.textView.setText(reviewItem.getWriter());
        holder.imageView.setImageResource(R.drawable.user1);
        holder.user_times.setText(reviewItem.getTime());
        // holder.ReportTimes.setText(evaluationInfo.getTime());
        holder.ratingBar.setRating(reviewItem.getRating());
        holder.recommends.setText("추천");
        String tmp=reviewItem.getRecommend()+"";
        holder.recommendsCounts.setText(tmp);
        //holder.Reports.setText(reviewItem.getReport());
        holder.Evaluates.setText(reviewItem.getContents());
    }

    @Override
    public int getItemCount() {
        return EvaLists.size();
    }
}
