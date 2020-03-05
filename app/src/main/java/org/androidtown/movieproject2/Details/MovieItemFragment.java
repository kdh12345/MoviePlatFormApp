package org.androidtown.movieproject2.Details;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

import org.androidtown.movieproject2.FragmentCallback;
import org.androidtown.movieproject2.R;

public class MovieItemFragment extends Fragment {
    FragmentCallback callback;

    int index;

    String movieTitle;
    String movieInfo;
    String imageUrl;

    ImageView imageView;
    //ProgressBar progressBar;

    public MovieItemFragment(int index, String movieTitle, String movieInfo, String imageUrl) {
        this.index = index;
        this.movieTitle = movieTitle;
        this.movieInfo = movieInfo;
        this.imageUrl = imageUrl;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof FragmentCallback){
            callback = (FragmentCallback) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment1, container, false);

        Button button = rootView.findViewById(R.id.detail_btn1);
        imageView = rootView.findViewById(R.id.img1);
        //progressBar = rootView.findViewById(R.id.progressBar);
        //progressBar.setVisibility(View.VISIBLE);
        sendImageRequest();
        TextView textView1 = rootView.findViewById(R.id.tv_title);
        textView1.setText(movieTitle);
        TextView textView2 = rootView.findViewById(R.id.info1);
        textView2.setText(movieInfo);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (callback != null) {
                    Bundle bundle = new Bundle(1);  // 넘겨줄 데이터 개수
                    bundle.putInt("Index", index);
                    callback.onFragmentSelected(1,bundle);
                }
            }
        });

        return rootView;
    }

    public void sendImageRequest(){
        // 배운대로
        //ImageLoadTask task = new ImageLoadTask(imageUrl, imageView);
        //task.execute();

        // glide 사용했을 때
        Glide.with(getContext())
                .load(imageUrl)
                .into(imageView);
        //progressBar.setVisibility(View.INVISIBLE);
    }


}
