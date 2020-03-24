package com.example.pupulermovies.Other;


import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.example.pupulermovies.Adapters.SlidingImageAdapter;
import com.example.pupulermovies.MainActivity;
import com.example.pupulermovies.R;
import com.example.pupulermovies.samples.Image;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class FirstLunch extends AppCompatActivity {

    private static ViewPager mPager;
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    private ArrayList<Image> imageModelArrayList;

    private int[] myImageList = new int[]{R.drawable.ic_bookmark_black_24dp, R.drawable.movieicon,
            R.drawable.ic_date_range_black_24dp,R.drawable.ic_bookmark_black_24dp
            ,R.drawable.ic_bookmark_border_black_24dp,R.drawable.ic_trending_up_black_24dp};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_lunch);


        imageModelArrayList = new ArrayList<>();
        imageModelArrayList = populateList();

        init();

    }


    private ArrayList<Image> populateList(){

        ArrayList<Image> list = new ArrayList<>();

        for(int i = 0; i < 6; i++){
            Image imageModel = new Image();
            imageModel.setImage_drawable(myImageList[i]);
            list.add(imageModel);
        }

        return list;
    }

    private void init() {

        mPager = (ViewPager) findViewById(R.id.pager);
        mPager.setAdapter(new SlidingImageAdapter(FirstLunch.this,imageModelArrayList));

        CirclePageIndicator indicator = (CirclePageIndicator)
                findViewById(R.id.indicator);

        indicator.setViewPager(mPager);

        final float density = getResources().getDisplayMetrics().density;

//Set circle indicator radius
        indicator.setRadius(5 * density);

        NUM_PAGES =imageModelArrayList.size();

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == NUM_PAGES) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 3000, 3000);

        // Pager listener over indicator
        indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                currentPage = position;

            }

            @Override
            public void onPageScrolled(int pos, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int pos) {

            }
        });

    }




    public  void onFinishClick(View view){
        Intent intent =new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
