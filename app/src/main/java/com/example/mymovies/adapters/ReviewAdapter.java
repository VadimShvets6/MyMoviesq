package com.example.mymovies.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymovies.R;
import com.example.mymovies.data.Review;

import java.util.ArrayList;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    //Переопределяем методы нашего адапетра

    //Добавляем Список отзывов
    private ArrayList<Review> reviews;

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review review = reviews.get(position);
        //Устанавиваем текст у контента и у автора
        holder.textViewContent.setText(review.getContent());
        holder.textViewAuthor.setText(review.getAuthor());
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    //Cеттер на созданый список ArrayList<Review>
    public void setReviews(ArrayList<Review> reviews) {
        this.reviews = reviews;
        notifyDataSetChanged();
    }

    public class ReviewViewHolder extends RecyclerView.ViewHolder{
        //Нам нужны два элемента который есть в нашем макете это два текста
        private TextView textViewAuthor;
        private TextView textViewContent;

        //Переоределяем конструктор
        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            //Присваеваем значения нашим элементам
            textViewAuthor = itemView.findViewById(R.id.textViewAuthor);
            textViewContent = itemView.findViewById(R.id.textViewContent);
        }
    }

}
