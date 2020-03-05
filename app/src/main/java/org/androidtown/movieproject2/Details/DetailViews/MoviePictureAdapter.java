package org.androidtown.movieproject2.Details.DetailViews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.androidtown.movieproject2.R;

import java.util.ArrayList;

public class MoviePictureAdapter extends RecyclerView.Adapter<MoviePictureAdapter.ViewHolder>{
    ArrayList<MoviePictureInfo> pictureArrayList=new ArrayList<>();
    Context context;
    OnItemClickListener listener;


    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        protected ImageView Video;
        protected ImageView Picture;
        OnItemClickListener listener;
        public ViewHolder(View view){
            super(view);
            this.Video=view.findViewById(R.id.videoIcon);
            this.Picture=view.findViewById(R.id.mv_picture);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if(listener != null) {
                        listener.onItemClick(ViewHolder.this, v, position);
                    }
                }
            });
        }///
        public void setItem(MoviePictureInfo item){
            // glide 사용
            Glide.with(context)
                    .load(item.imageUrl)
                    .into(Picture);
            if (item.isMovie()) {
                Video.setVisibility(View.VISIBLE);
            } else {
                Video.setVisibility(View.GONE);
            }
        }
        public void setOnItemClickListener(OnItemClickListener listener) {
            this.listener = listener;
        }
    }
    public void addItem(MoviePictureInfo moviePictureInfo){
        pictureArrayList.add(moviePictureInfo);
    }
    public void setItems(ArrayList<MoviePictureInfo> pictureArrayList){
        this.pictureArrayList=pictureArrayList;
    }
    public MoviePictureInfo getItem(int position) {
        return pictureArrayList.get(position);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context=parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.movie_pictures, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MoviePictureInfo moviePictureInfo =pictureArrayList.get(position);
        holder.setItem(moviePictureInfo);

        holder.setOnItemClickListener(listener);

    }

    @Override
    public int getItemCount() {
        return pictureArrayList.size();
    }
}
