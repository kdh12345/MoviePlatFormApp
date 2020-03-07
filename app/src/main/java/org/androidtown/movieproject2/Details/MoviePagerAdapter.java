package org.androidtown.movieproject2.Details;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class MoviePagerAdapter extends FragmentStatePagerAdapter {
    ArrayList<Fragment> arrayList=new ArrayList<>();
    public MoviePagerAdapter(FragmentManager fragmentManager){
        super(fragmentManager);
    }

    public void addItem(Fragment fragment){
        arrayList.add(fragment);
    }
    @NonNull
    @Override
    public Fragment getItem(int position) {
        return arrayList.get(position);
    }
    public void setItem(ArrayList<Fragment> arrayList){
        this.arrayList=arrayList;
    }
    public void removeItem(Fragment fragment){
        arrayList.remove(fragment);
    }
    public void clearArrayList(){
        arrayList.clear();
    }
    @Override
    public int getCount() {
        return arrayList.size();
    }
}
