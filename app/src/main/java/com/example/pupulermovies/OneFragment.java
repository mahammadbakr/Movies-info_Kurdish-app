package com.example.pupulermovies;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


public class OneFragment extends Fragment {
    private TextView title;
    private TextView overview;
    private String t;
    private String o;


    public OneFragment(String t , String o) {
       this.t=t;
       this.o=o;
    }

       @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_one, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        title = view.findViewById(R.id.titleFragment);
        overview = view.findViewById(R.id.overviewFragment);

        title.setText(t);
        if(o.isEmpty()){
            overview.setText(getString(R.string.nothingFound));
        }else {
            overview.setText(o);
        }


    }


}