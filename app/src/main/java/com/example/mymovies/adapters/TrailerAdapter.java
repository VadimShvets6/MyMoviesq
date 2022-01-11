package com.example.mymovies.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymovies.R;
import com.example.mymovies.data.Trailer;

import java.util.ArrayList;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.TrailerViewHolder> {

    //Добавляем список трейлеров
    private ArrayList<Trailer> trailers;

    //Добавляем слушателя
    private onTrailerClickListener onTrailerClickListener;

    @NonNull
    @Override
    public TrailerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer_itm, parent, false);
        return new TrailerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailerViewHolder holder, int position) {
        Trailer trailer = trailers.get(position);
        holder.textViewNameOfVideo.setText(trailer.getName());
    }

    @Override
    public int getItemCount() {
        return trailers.size();
    }

    //Интерфйс слушателя
    public interface onTrailerClickListener{
        void onTrailerClick(String url);
    }

    public class TrailerViewHolder extends RecyclerView.ViewHolder{

        //Ccылки на наши элементы макета
        private TextView textViewNameOfVideo;

        public TrailerViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewNameOfVideo = itemView.findViewById(R.id.textViewNameOfVideo);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onTrailerClickListener != null){
                        onTrailerClickListener.onTrailerClick(trailers.get(getAdapterPosition()).getKey());
                    }
                }
            });
        }
    }

    public void setTrailers(ArrayList<Trailer> trailers) {
        this.trailers = trailers;
        notifyDataSetChanged();
    }

    //Cеттер на слушателя
    public void setOnTrailerClickListener(TrailerAdapter.onTrailerClickListener onTrailerClickListener) {
        this.onTrailerClickListener = onTrailerClickListener;
    }
}
