package com.example.pupulermovies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.app.SearchManager;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;;
import android.widget.ImageButton;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.example.pupulermovies.database.dbHelper;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import java.util.ArrayList;
import java.util.List;


import static com.example.pupulermovies.database.dbHelper.COLUMN_BOOKMARK_ID;
import static com.example.pupulermovies.database.dbHelper.COLUMN_BOOKMARK_TITLE;
import static com.example.pupulermovies.database.dbHelper.TABLE_NAME2;

public class MoviesDetail extends AppCompatActivity {


    private ImageView img;

    private androidx.appcompat.widget.Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FloatingActionButton floatingActionButton;
    private ImageButton bookMark;

    private com.example.pupulermovies.database.dbHelper dbHelper;
    private SQLiteDatabase dbRead;
    private SQLiteDatabase dbWrite;

    private String[] data;

    private Boolean clicked ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_detail);

        dbHelper= new dbHelper(this);
        dbRead= dbHelper.getReadableDatabase();
        dbWrite= dbHelper.getWritableDatabase();


        data = getIntent().getStringArrayExtra("data");

        bookMark=findViewById(R.id.bookMarkDetails);
        floatingActionButton=findViewById(R.id.FAB);
        img=findViewById(R.id.imgDetail);

        String a = data[2];
        String url="https://image.tmdb.org/t/p/w500"+a;


        Glide
                .with(this)
                .load(url)
                .centerCrop()
                .placeholder(R.drawable.ic_image_black_24dp)
                .into(img);


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout =  findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH );
                intent.putExtra(SearchManager.QUERY, data[0]);
                startActivity(intent);

            }
        });

        clicked = isBookmarked(Integer.parseInt(data[7]),data[0]);
        if (clicked==true){
            bookMark.setImageResource(R.drawable.ic_bookmark_black_24dp);
        }else {
            bookMark.setImageResource(R.drawable.ic_bookmark_border_black_24dp);
        }

//        bookMark.setTag(clicked);

        bookMark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if( clicked==false || clicked==null ){
                    clicked=true;
                    bookMark.setImageResource(R.drawable.ic_bookmark_black_24dp);
                    addInBookmark(Integer.parseInt(data[7]),data[0],data[1],data[2],data[3],data[4],data[5],Integer.parseInt(data[6]));
                }else if( clicked==true ) {
                    clicked=false;
                    bookMark.setImageResource(R.drawable.ic_bookmark_border_black_24dp);
                    deleteInBookmark(Integer.parseInt(data[7]),data[0]);
                }

            }
        });



    }

    private boolean isBookmarked(int id ,String title ) {
        String selectString = "SELECT * FROM " + TABLE_NAME2 + " WHERE " + COLUMN_BOOKMARK_ID + " = '"+id + "' AND " + COLUMN_BOOKMARK_TITLE + " = '" + title + "'";
        String args[] = {"id" , "title"};
        Cursor cursor = dbWrite.rawQuery(selectString,null);
        boolean hasObject = false;
        if(cursor.moveToFirst()) {
            hasObject = true;
        }
        return hasObject;
    }


    private void addInBookmark(int id ,String title ,String overview ,String image ,String date ,String popularity ,String vote ,int first ) {

        ContentValues values = new ContentValues();
        values.put(com.example.pupulermovies.database.dbHelper.COLUMN_BOOKMARK_ID, id);
        values.put(com.example.pupulermovies.database.dbHelper.COLUMN_BOOKMARK_TITLE, title);
        values.put(com.example.pupulermovies.database.dbHelper.COLUMN_BOOKMARK_OVERVIEW, overview);
        values.put(com.example.pupulermovies.database.dbHelper.COLUMN_BOOKMARK_IMAGE, image);
        values.put(com.example.pupulermovies.database.dbHelper.COLUMN_BOOKMARK_DATE, date);
        values.put(com.example.pupulermovies.database.dbHelper.COLUMN_BOOKMARK_POPULARITY, popularity);
        values.put(com.example.pupulermovies.database.dbHelper.COLUMN_BOOKMARK_VOTEAVARAGE, vote);
        values.put(com.example.pupulermovies.database.dbHelper.COLUMN_BOOKMARK_FIRSTTYPE, first);

        dbWrite.insert(TABLE_NAME2, null, values);
    }




    private void deleteInBookmark( int id ,String title ){

        dbWrite.delete(TABLE_NAME2,
                COLUMN_BOOKMARK_ID + " = ? AND " + COLUMN_BOOKMARK_TITLE + " = ?",
                new String[] {id+"", title});
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new OneFragment(data[0],data[1]), getString(R.string.overview));
        adapter.addFragment(new TwoFragment(data[3],data[4],data[5],data[6],data[7]), getString(R.string.details));
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}
