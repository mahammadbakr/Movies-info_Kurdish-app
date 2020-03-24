package com.example.pupulermovies;

import android.content.Context;
import android.content.Intent;
import android.graphics.BlurMaskFilter;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.pupulermovies.samples.Movie;
import java.util.ArrayList;
import java.util.List;

public class MainAdapterGrid extends RecyclerView.Adapter<MainAdapterGrid.MyViewHolder> implements Filterable {

    private List<Movie> movies;
    private List<Movie> moviesFull;
    private Context mContext;

    public MainAdapterGrid(Context context, List<Movie> list) {
        movies = list;
        mContext= context;
        moviesFull=list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.main_grid, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {

        final Movie movie = movies.get(position);


        holder.title.setText(movie.getName());


        if (Build.VERSION.SDK_INT >= 11) {
            holder.title.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        float radius = holder.title.getTextSize() / 2;
        BlurMaskFilter filter = new BlurMaskFilter(radius, BlurMaskFilter.Blur.SOLID);
        holder.title.getPaint().setMaskFilter(filter);


        String a = movie.getImg();
        String url="https://image.tmdb.org/t/p/w154"+a;

         Glide
                .with(mContext)
                .load(url)
                .centerCrop()
                .placeholder(R.drawable.ic_cloud_download_black_24dp)
                .into(holder.imageView);


         holder.cardView.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent= new Intent(mContext, MoviesDetail.class);
                 intent.putExtra("data", new String[] {movie.getName(),movie.getOverview(),movie.getImg() , movie.getDate() , Double.toString(movie.getPopularity()) ,Double.toString(movie.getVote()),Integer.toString(movie.getFirst()),Integer.toString(movie.getId()) });
                 mContext.startActivity(intent);
             }
         });
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public ImageView imageView;
        public CardView cardView;

        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.titttt);
            imageView=view.findViewById(R.id.img);
            cardView=view.findViewById(R.id.card);

        }
    }

    @Override
    public Filter getFilter() {
        return exampleFilter;
    }
    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Movie> filteredList = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filteredList.addAll(moviesFull);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (Movie item : moviesFull) {
                    if (item.getName().toLowerCase().trim().contains(filterPattern)) {
                        filteredList.add(item);
                    }else if(!item.getName().toLowerCase().trim().contains(filterPattern)) {
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if(constraint == null || constraint.length() == 0 || moviesFull.size()==0|| results==null){
               return;
            }else {
                movies.clear();
                try {
                    movies.addAll((List) results.values);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                notifyDataSetChanged();
            }

        }
    };


}