package com.example.pupulermovies;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.ContentValues;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.pupulermovies.Other.FirstLunch;
import com.example.pupulermovies.database.dbHelper ;
import com.example.pupulermovies.samples.Movie;
import com.facebook.stetho.Stetho;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {
    public static String PACKAGE_NAME;

    private static int pageNum =1;
    private static int modeType=0;
    private static String URL_SITE="https://api.themoviedb.org/3/movie/popular?api_key=dc82c675a29d80ed51c3836dbe86f7cd&language=en-US&page="+pageNum;

     private List<Movie> list = new ArrayList<>();
     private  List<Movie> bookmarkList=new ArrayList<>();
     private MainAdapterGrid adapter;
     private RecyclerView recyclerView;

    private dbHelper dbHelper;
    private SQLiteDatabase dbRead;
    private SQLiteDatabase dbWrite;

    private FloatingActionButton seeMore;
    private ProgressBar progressBar;


    private SharedPreferences share;
    private SharedPreferences.Editor editor;

    private  MenuItem searchItem;
    private  SearchView searchView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Stetho.initializeWithDefaults(this);
        PACKAGE_NAME = getApplicationContext().getPackageName();

        installingLunch();
        progressBar=findViewById(R.id.progress_circular);


        share=getSharedPreferences(PACKAGE_NAME,Context.MODE_PRIVATE);

        pageNum=share.getInt("page",1);
        modeType=share.getInt("mode",0);

        recyclerView =  findViewById(R.id.recycler);
        adapter = new MainAdapterGrid(this,list);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        recyclerView.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);



        dbHelper= new dbHelper(this);
        dbRead= dbHelper.getReadableDatabase();
        dbWrite= dbHelper.getWritableDatabase();


        OfflineTask taskOffline = new OfflineTask();
        taskOffline.execute();
        OfflineTask2 taskOffline2 = new OfflineTask2();
        taskOffline2.execute();

        seeMore=findViewById(R.id.seeMore);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {

                if(recyclerView.computeVerticalScrollOffset()==0){
                    getSupportActionBar().show();
                }else {
                    getSupportActionBar().hide();
                }
                Log.v("ddddddddddddddddddddd",Integer.toString(recyclerView.computeVerticalScrollOffset()+recyclerView.computeVerticalScrollExtent()));
                Log.v("ddddddddddddddddddddd",Integer.toString(recyclerView.computeVerticalScrollRange()));
                if(recyclerView.computeVerticalScrollOffset()+recyclerView.computeVerticalScrollExtent()==recyclerView.computeVerticalScrollRange()){
                    seeMore.show();
                }else {
                    seeMore.hide();
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });



    }

    private void installingLunch() {
            Boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                    .getBoolean("isFirstRun", true);
            if (isFirstRun) {
                //fetch data
                AsyncTask taskOnline=new AsyncTask();
                taskOnline.execute();
                //show start activitی
                startActivity(new Intent(MainActivity.this, FirstLunch.class));
                Toast.makeText(MainActivity.this, getString(R.string.firstLunch), Toast.LENGTH_LONG)
                        .show();
            }
            getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                    .putBoolean("isFirstRun", false).commit();
        }


    @Override
    protected void onStart() {

        super.onStart();
    }


    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(R.string.app_name);
        builder.setIcon(R.mipmap.ic_launcher_foreground);
        builder.setMessage(getString(R.string.askExit))
                .setCancelable(true)
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                })
                .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        dbHelper.close();
        searchItem.collapseActionView();
        super.onDestroy();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    public void onSeeMore(View view){

        pageNum=pageNum+1;
        URL_SITE="https://api.themoviedb.org/3/movie/popular?api_key=dc82c675a29d80ed51c3836dbe86f7cd&language=en-US&page="+pageNum;
        AsyncTask task=new AsyncTask();
        task.execute();

    }


    public boolean isEmpty(String TableName) {
        int NoOfRows = (int) DatabaseUtils.queryNumEntries(dbRead, TableName);
        if (NoOfRows == 0) {
            return true;
        } else {
            return false;
        }
    }



    public class OfflineTask extends android.os.AsyncTask<Void,Void,Void> {

        @Override
        protected void onPostExecute(Void aVoid) {
            if(!isEmpty("movies")){
                recyclerView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }

            super.onPostExecute(aVoid);
        }


        public void curser(String String){
            String[] from = {"id", "title", "overview", "img","date","popularity","vote","first"};
            Cursor cursor = dbRead.query(com.example.pupulermovies.
                    database.dbHelper.TABLE_NAME, from, null, null, null, null, String, null);


            try {
                int idCoulmn = cursor.getColumnIndex(com.example.pupulermovies.database.dbHelper.COLUMN_MOVIES_ID);
                int titleCoulmn = cursor.getColumnIndex(com.example.pupulermovies.database.dbHelper.COLUMN_MOVIES_TITLE);
                int overviewCoulmn = cursor.getColumnIndex(com.example.pupulermovies.database.dbHelper.COLUMN_MOVIES_OVERVIEW);
                int imageCoulmn = cursor.getColumnIndex(com.example.pupulermovies.database.dbHelper.COLUMN_MOVIES_IMAGE);
                int dateCoulmn = cursor.getColumnIndex(com.example.pupulermovies.database.dbHelper.COLUMN_MOVIES_DATE);
                int popularityCoulmn = cursor.getColumnIndex(com.example.pupulermovies.database.dbHelper.COLUMN_MOVIES_POPULARITY);
                int voteCoulmn = cursor.getColumnIndex(com.example.pupulermovies.database.dbHelper.COLUMN_MOVIES_VOTEAVARAGE);
                int firstCoulmn = cursor.getColumnIndex(com.example.pupulermovies.database.dbHelper.COLUMN_MOVIES_FIRSTTYPE);

                cursor.moveToFirst();
                while (cursor.moveToNext()) {
                    int id = cursor.getInt(idCoulmn);
                    String title = cursor.getString(titleCoulmn);
                    String overview = cursor.getString(overviewCoulmn);
                    String img = cursor.getString(imageCoulmn);
                    String date = cursor.getString(dateCoulmn);
                    double popularity = cursor.getDouble(popularityCoulmn);
                    double vote = cursor.getDouble(voteCoulmn);
                    int first=cursor.getInt(firstCoulmn);

                    list.add(new Movie(id, title, overview, img, date, popularity, vote,first));

                }
            } finally {
                cursor.close();
            }
        }


        @Override
        protected Void doInBackground(Void... voids) {
            list.clear();
            if(modeType==0) {
                curser("date" +" DESC");
            }else if(modeType==1) {
                curser("popularity" +" DESC");
            } else if(modeType==2) {
                curser("vote" +" DESC");
            }else  if(modeType==3) {
                curser("title" +" ASC");
            }else { return null;}

            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    adapter.notifyDataSetChanged();
                }
            });

            return null;
        }
    }


    public class OfflineTask2 extends android.os.AsyncTask<Void,Void,Void> {

        @Override
        protected void onPostExecute(Void aVoid) {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    adapter.notifyDataSetChanged();
                }
            });
            super.onPostExecute(aVoid);
        }


        public void curser(){
            String[] from = {"id", "title", "overview", "img","date","popularity","vote","first"};
            Cursor cursor = dbRead.query(com.example.pupulermovies.
                    database.dbHelper.TABLE_NAME2, from, null, null, null, null, null, null);

            try {
                int idCoulmn = cursor.getColumnIndex(com.example.pupulermovies.database.dbHelper.COLUMN_BOOKMARK_ID);
                int titleCoulmn = cursor.getColumnIndex(com.example.pupulermovies.database.dbHelper.COLUMN_BOOKMARK_TITLE);
                int overviewCoulmn = cursor.getColumnIndex(com.example.pupulermovies.database.dbHelper.COLUMN_BOOKMARK_OVERVIEW);
                int imageCoulmn = cursor.getColumnIndex(com.example.pupulermovies.database.dbHelper.COLUMN_BOOKMARK_IMAGE);
                int dateCoulmn = cursor.getColumnIndex(com.example.pupulermovies.database.dbHelper.COLUMN_BOOKMARK_DATE);
                int popularityCoulmn = cursor.getColumnIndex(com.example.pupulermovies.database.dbHelper.COLUMN_BOOKMARK_POPULARITY);
                int voteCoulmn = cursor.getColumnIndex(com.example.pupulermovies.database.dbHelper.COLUMN_BOOKMARK_VOTEAVARAGE);
                int firstCoulmn = cursor.getColumnIndex(com.example.pupulermovies.database.dbHelper.COLUMN_BOOKMARK_FIRSTTYPE);

                cursor.moveToFirst();
                while (cursor.moveToNext()) {
                    int id = cursor.getInt(idCoulmn);
                    String title = cursor.getString(titleCoulmn);
                    String overview = cursor.getString(overviewCoulmn);
                    String img = cursor.getString(imageCoulmn);
                    String date = cursor.getString(dateCoulmn);
                    double popularity = cursor.getDouble(popularityCoulmn);
                    double vote = cursor.getDouble(voteCoulmn);
                    int first=cursor.getInt(firstCoulmn);

                    bookmarkList.add(new Movie(id, title, overview, img, date, popularity, vote,first));

                }
            } finally {
                cursor.close();
            }
        }



        @Override
        protected Void doInBackground(Void... voids) {
            curser();
            return null;
        }
    }




    public class AsyncTask extends android.os.AsyncTask<URL,Void,List<Movie>> {

        @Override
        protected void onPostExecute(List<Movie> movie) {
            share= getSharedPreferences(PACKAGE_NAME,Context.MODE_PRIVATE);
            editor= share.edit();
            editor.putInt("page", pageNum);
            editor.commit();

            if(movie==null){
                return;
            }
            recyclerView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            showToast();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    adapter.notifyDataSetChanged();
                }
            });

        }

        @Override
        protected List<Movie> doInBackground(URL... urls) {

            URL url = createUrl(URL_SITE);
            String jsonResponse = "";

            try {
                jsonResponse = makeHttpConnection(url);
            }catch (IOException e){

            }

            List<Movie> movie = extractFeatureFromJson(jsonResponse);

            return movie;
        }


        private List<Movie> extractFeatureFromJson(String jsonResponse) {
            try {
                JSONObject jsonObject= new JSONObject(jsonResponse);
                JSONArray properties = jsonObject.getJSONArray("results");

                for (int i = 0; i < properties.length(); i++) {
                    JSONObject object = properties.getJSONObject(i);

                    int id = object.getInt("id");
                    String title = object.getString("original_title");
                    String overview = object.getString("overview");
                    String img = object.getString("poster_path");
                    String date = object.getString("release_date");
                    double popularity = object.getDouble("popularity");
                    double voteAvarage = object.getDouble("vote_average");

                    JSONArray genre_ids = object.getJSONArray("genre_ids");
                         int first = genre_ids.getInt(0);



                    list.add(new Movie(id, title, overview, img, date, popularity, voteAvarage,first));


                    ContentValues values = new ContentValues();
                    values.put(com.example.pupulermovies.database.dbHelper.COLUMN_MOVIES_ID, id);
                    values.put(com.example.pupulermovies.database.dbHelper.COLUMN_MOVIES_TITLE, title);
                    values.put(com.example.pupulermovies.database.dbHelper.COLUMN_MOVIES_OVERVIEW, overview);
                    values.put(com.example.pupulermovies.database.dbHelper.COLUMN_MOVIES_IMAGE, img);
                    values.put(com.example.pupulermovies.database.dbHelper.COLUMN_MOVIES_DATE, date);
                    values.put(com.example.pupulermovies.database.dbHelper.COLUMN_MOVIES_POPULARITY, popularity);
                    values.put(com.example.pupulermovies.database.dbHelper.COLUMN_MOVIES_VOTEAVARAGE, voteAvarage);
                    values.put(com.example.pupulermovies.database.dbHelper.COLUMN_MOVIES_FIRSTTYPE, first);


                    dbWrite.insert(com.example.pupulermovies.database.dbHelper.TABLE_NAME, null, values);
                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();

                        }
                    });
                }

                return list;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        private String makeHttpConnection(URL url)  throws IOException{
            String jsonParse="";

            HttpURLConnection httpURLConnection =null;
            InputStream inputStream = null;

            try{
                httpURLConnection =(HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setConnectTimeout(10000);
                httpURLConnection.setReadTimeout(15000);
                httpURLConnection.connect();
                inputStream = httpURLConnection.getInputStream();
                jsonParse = readFromStream(inputStream);

            }catch (IOException e){
                //ssss
            } finally {
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
                if (inputStream != null) {
                    // function must handle java.io.IOException here
                    inputStream.close();
                }
            }

            return jsonParse;
        }

        private String readFromStream(InputStream inputStream) {
            StringBuilder output = new StringBuilder();
            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line = null;
                try {
                    line = reader.readLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                while (line != null) {
                    output.append(line);
                    try {
                        line = reader.readLine();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return output.toString();
        }

        private URL createUrl(String sUrl)  {
            URL y =null;

            try {
                y =new URL(sUrl);
            }catch (MalformedURLException e){
                //something
            }

            return y;
        }
    }

    private void showToast() {
        Toast.makeText(this, getString(R.string.dataAdded), Toast.LENGTH_LONG).show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        searchItem = menu.findItem(R.id.searchAction);
        searchView = (SearchView) searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if(adapter.getItemCount()==0){
                    OfflineTask offlineTask = new OfflineTask();
                    offlineTask.execute();
                }
                searchView.clearFocus();
                searchItem.collapseActionView();
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                if(newText==null||newText.isEmpty()){
                   OfflineTask offlineTask = new OfflineTask();
                   offlineTask.execute();
                }
                return false;
            }
        });
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.dateMenu:
                onDate();
                return true;
            case R.id.topRateMenu:
                onPopuler();
                return true;
            case R.id.populerMenu:
                    onTopRated();
                return true;
            case R.id.bookmarkMenu:
                    onBookMark();
                return true;
            case R.id.languageMenu:
                onlans();
                return true;
            case R.id.titleMenu:
                onTitle();
                return true;
            case R.id.exitMenu:
                onExit();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void onTitle() {
        share= getSharedPreferences(PACKAGE_NAME,Context.MODE_PRIVATE);
        editor= share.edit();
        editor.putInt("mode", 3);
        editor.commit();

        finish();
        startActivity(getIntent());

    }

    private void onDate() {
        share= getSharedPreferences(PACKAGE_NAME,Context.MODE_PRIVATE);
        editor= share.edit();
        editor.putInt("mode", 0);
        editor.commit();

        finish();
        startActivity(getIntent());

    }

    private void onTopRated() {
        share= getSharedPreferences(PACKAGE_NAME,Context.MODE_PRIVATE);
        editor= share.edit();
        editor.putInt("mode", 1);
        editor.commit();

        finish();
        startActivity(getIntent());

    }

    private void onPopuler() {
        share= getSharedPreferences(PACKAGE_NAME,Context.MODE_PRIVATE);
        editor= share.edit();
        editor.putInt("mode", 2);
        editor.commit();

        finish();
        startActivity(getIntent());

    }

    private void onBookMark() {
        adapter = new MainAdapterGrid(this,bookmarkList);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void  onlans(){
        String local = this.getResources().getConfiguration().locale.getLanguage();
        if(local=="it"){
            Locale lan = new Locale("en");
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = lan;
            res.updateConfiguration(conf, dm);
            finish();
            Intent refresh = new Intent(this, MainActivity.class);
            startActivity(refresh);
            Toast.makeText(this, getString(R.string.english), Toast.LENGTH_LONG).show();
        }else if(local=="en"){
            Locale lan2 = new Locale("it");
            Resources res = getResources();
            DisplayMetrics dm = res.getDisplayMetrics();
            Configuration conf = res.getConfiguration();
            conf.locale = lan2;
            res.updateConfiguration(conf, dm);
            finish();
            Intent refresh = new Intent(this, MainActivity.class);
            startActivity(refresh);
            Toast.makeText(this, getString(R.string.kurdish), Toast.LENGTH_LONG).show();

        }


    }
    private void  onExit(){
        onBackPressed();
    }


}


