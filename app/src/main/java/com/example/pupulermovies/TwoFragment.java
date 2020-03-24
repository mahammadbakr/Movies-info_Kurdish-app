package com.example.pupulermovies;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TwoFragment extends Fragment {

    private TextView date;
    private TextView popularity;
    private TextView vote;
    private TextView first;

    private String d;
    private String p;
    private String v;
    private String f;
    private String s;

    public TwoFragment(String d , String p , String v,String f,String s) {
        this.d=d;
        this.p=p;
        this.v=v;
        this.f=f;
        this.s=s;
    }
    final String JSON_KURDISH = "{\"genres\":[{\"id\":28,\"name\":\"شەڕ و بەرەنگاری\"},{\"id\":12,\"name\":\"سەرکێشی\"},\n"+
            "        {\"id\":16,\"name\":\"ئەنیمەشن\"},{\"id\":35,\"name\":\"گاڵتەئامێز\"},\n"+
            "        {\"id\":80,\"name\":\"تاوان\"},{\"id\":99,\"name\":\"بەڵگاداری - دۆکیومێنتاری\"},\n"+
            "        {\"id\":18,\"name\":\"دراما\"},{\"id\":10751,\"name\":\"خێزانی\"},\n"+
            "        {\"id\":14,\"name\":\"خەیاڵ و فانتازیا\"},{\"id\":36,\"name\":\"مێژوو\"},\n"+
            "        {\"id\":27,\"name\":\"ترسناک\"},{\"id\":10402,\"name\":\"میوزیک\"},\n"+
            "        {\"id\":9648,\"name\":\"نهێنی و ئاڵوز\"},{\"id\":10749,\"name\":\"ڕۆمانسی و هەستبزوێن\"},\n"+
            "        {\"id\":878,\"name\":\"چیرۆکی زانستی خەیاڵی\"},{\"id\":10770,\"name\":\"فیلمی تەلەفیژیونی\"},\n"+
            "        {\"id\":53,\"name\":\"چیرۆکی هەستبزوێن\"},{\"id\":10752,\"name\":\"شەڕ وجەنگ\"},{\"id\":37,\"name\":\"خۆرئاوایی\"}]}";

    final String JSON_ENGLISH = "{\"genres\":[{\"id\":28,\"name\":\"Action\"},{\"id\":12,\"name\":\"Adventure\"},\n"+
            "        {\"id\":16,\"name\":\"Animation\"},{\"id\":35,\"name\":\"Comedy\"},\n"+
            "        {\"id\":80,\"name\":\"Crime\"},{\"id\":99,\"name\":\"Documentary\"},\n"+
            "        {\"id\":18,\"name\":\"Drama\"},{\"id\":10751,\"name\":\"Family\"},\n"+
            "        {\"id\":14,\"name\":\"Fantasy\"},{\"id\":36,\"name\":\"History\"},\n"+
            "        {\"id\":27,\"name\":\"Horror\"},{\"id\":10402,\"name\":\"Music\"},\n"+
            "        {\"id\":9648,\"name\":\"Mystery\"},{\"id\":10749,\"name\":\"Romance\"},\n"+
            "        {\"id\":878,\"name\":\"Science Fiction\"},{\"id\":10770,\"name\":\"TV Movie\"},\n"+
            "        {\"id\":53,\"name\":\"Thriller\"},{\"id\":10752,\"name\":\"War\"},{\"id\":37,\"name\":\"Western\"}]}";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_two, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        date = view.findViewById(R.id.dateFrag);
        popularity = view.findViewById(R.id.popFrag);
        vote = view.findViewById(R.id.voteFrag);
        first = view.findViewById(R.id.first);


        date.setText(d);
        popularity.setText(p);
        vote.setText(v);

        String local = this.getResources().getConfiguration().locale.getLanguage();
        if(local=="it") {
            String firstType = getType(Integer.parseInt(f), JSON_KURDISH);
            first.setText(firstType);
        }else {
            String firstType = getType(Integer.parseInt(f), JSON_ENGLISH);
            first.setText(firstType);
        }
    }



    public String getType(int number,String json){
        String result =getString(R.string.cantFind);
        try {
            JSONObject jsonObject= new JSONObject(json);
            JSONArray array = jsonObject.getJSONArray("genres");

            for(int i = 0 ; i<array.length(); i++){
                JSONObject current = array.getJSONObject(i);
                int idJson = current.getInt("id");

                if(idJson==number){
                    result=current.getString("name");
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }
}
