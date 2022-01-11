package com.example.mymovies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mymovies.adapters.ReviewAdapter;
import com.example.mymovies.adapters.TrailerAdapter;
import com.example.mymovies.data.FavoriteMovie;
import com.example.mymovies.data.MainViewModel;
import com.example.mymovies.data.Movie;
import com.example.mymovies.data.Review;
import com.example.mymovies.data.Trailer;
import com.example.mymovies.utils.JSONUtils;
import com.example.mymovies.utils.NetworkUtils;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;

public class DatailActivity extends AppCompatActivity {

    //ССЫЛКА НА IMAGEVIEW В НАШЕМ МАКЕТЕ
    private ImageView imageViewBigPoster;
    //ССЫЛКА НА textViewTitle в НАШЕМ МАКЕТЕ
    private TextView textViewTitle;
    //ССЫЛКА НА textViewOriginalTitle в НАШЕМ МАКЕТЕ
    private TextView textViewOriginalTitle;
    //ССЫЛКА НА textViewRating в НАШЕМ МАКЕТЕ
    private TextView textViewRating;
    //ССЫЛКА НА textViewReleseData в НАШЕМ МАКЕТЕ
    private TextView textViewReleseData;
    //ССЫЛКА НА textViewOverview в НАШЕМ МАКЕТЕ
    private TextView textViewOverview;
    private ScrollView scrollViewDrop;
    private ImageView imageViewAddToFavourite;

    //Ссылка на элементы для трейлеров и отзывов
    private RecyclerView recyclerViewTrailers;
    private RecyclerView recyclerViewReviews;

    //адаптеры для трейлеров и отзывов
    private ReviewAdapter reviewAdapter;
    private TrailerAdapter trailerAdapter;

    //ВЫНЕСЕМ id В ОТДЕЛЬНУЮ ПЕРЕМЕНУЮ
    private int id;

    private Movie movie;
    private FavoriteMovie favoriteMovie;
    //ОБЪЕКТ Нашей БД
    private MainViewModel viewModel;

//----------------------------------------------------------------------------------------------------
    //ПЕРЕОПРЕДЕЛИТЬ МЕТОД ДЛЯ РАБОТЫ МЕНЮ
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //ПЕРВЫМ ДЕЛОМ НАДО ПОЛУЧИТЬ MenuInfleter
        MenuInflater inflater = getMenuInflater();
        //ДЛЯ В ОБЕКТ inflater ПЕРЕДАЕЕМ МЕНЮ КОТОРОЕ МЫ СОЗДАЛИ main_menu
        inflater.inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //ДЛЯ ТОГО ЧТОБЫ КАК ТО РЕАГИРОВАТЬ НА НАЖАТЫЕ ПУНКТЫ МЕНЮ ПЕРЕОПРЕдеЛЯЕМ МЕТОД
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //ПОЛУЧИМ id НАЖАТОГО ПУНКТА
        int id = item.getItemId();

        //И БУДЕМ РЕАГИРОВАТЬ НА НАЖАТЫЕ ПУНКТЫ
        switch (id){
            //ЕСЛИ ВЫБРАН ПУНКТ "ГЛАВНАЯ"
            case R.id.itemMain:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;

            //ЕСЛИ ВЫБРАН ПУНКТ "ИЗБРАНОЕ"
            case R.id.itemFavourite:
                Intent intentFavourite = new Intent(this, FavouriteActivity.class);
                startActivity(intentFavourite);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
//------------------------------------------------------------------------------------------------
    // АКТИВНОСТЬ ПОДРОБНОСТЕЙ О ВЫБРАНОМ ФИЛЬМЕ
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datail);

        //ПРИСВАЕМ ЗНАЧЕНИЯ НАШИМ ССЛЫКА
        imageViewBigPoster = findViewById(R.id.imageViewBigPoster);
        textViewTitle = findViewById(R.id.textViewTitile);
        textViewOriginalTitle = findViewById(R.id.textViewOriginalTitle);
        textViewRating = findViewById(R.id.textViewRating);
        textViewReleseData = findViewById(R.id.textViewReleseData);
        textViewOverview = findViewById(R.id.textViewOverview);
        imageViewAddToFavourite = findViewById(R.id.imageViewAddToFavorite);
        scrollViewDrop = findViewById(R.id.scrollView);

        //ПОЛУЧАЕМ ИНТЕНТ КОТОРЫЙ ПЕРЕДАЛИ ИЗ MainActivity и ид КОТОРЫЙ ВЛОЖИЛИ В НЕГО
        Intent intent = getIntent();
        //ПРОВЕРЯЕМ ЕСЛИ Intent не пустой и содержит наш id
        if(intent != null && intent.hasExtra("id")){
            //ЕСЛИ СОДЕРЖИТ id ТО МЫ НАШЕЙ ПЕРЕМЕНОЙ id ПРИСВАЕМЕМ ПОЛУЧЕНЫЕ ДАННЫЕ
            id = intent.getIntExtra("id", -1);
        } else {
            //ИНАЧЕ ЗАКРЫВАЕМ ИНТЕНТ И ВОЗВРАШАЕМСЯ В АКТИВНОСТЬ КОТОРАЯ ЕГО ПЕРЕДАЛА
            finish();
        }

        //ПОЛУЧИМ ФИЛЬМЫ ИЗ НАШЕЙ БАЗЫ ДАННЫХ
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        //ПОЛУЧАЕМ САМ ФИЛЬМ ПО ПОЛУЧЕНОМУ id
        movie = viewModel.getMovieById(id);
        //ПОСЛЕ ЭТОГО УСТАНАВЛИВАЕМ У ВСЕХ ЭЛЕМЕНТОВ НУЖНОЕ ЗНАЧЕНИЕ
        //ДЛЯ УСТАНОВКИ ИЗОБРАЖЕНИЕ ИСПОЛЬЗУЕМ Picaso
        Picasso.get().load(movie.getBigPosterPath()).into(imageViewBigPoster);
        //ЗАТЕМ УСТАНВАЛИВАЕМ ТЕКСТ ДЛЯ textViewTitle
        textViewTitle.setText(movie.getTitle());
        //УСТАНАВЛИВАЕМ ТЕКСТ У textViewOriginalTitle
        textViewOriginalTitle.setText(movie.getOriginTitle());
        //УСТАНАВЛИВАЕМ ТЕКСТ У textViewRating он у нас double ПОЭТОМУ ПРИВОДИМ ЕГО К ТЕКСТУ
        textViewRating.setText(Double.toString(movie.getVoteAverage()));
        //УСТАНАВЛИВАЕМ ТЕКСТ У textViewReleseData
        textViewReleseData.setText(movie.getReleaseData());
        //УСТАНАЛИВАЕМ ТЕКСТ У textViewOverview
        textViewOverview.setText(movie.getOverview());
        setFavourite();

        //Присваеваем значения для recyclerView у видео и отзывово
        recyclerViewReviews = findViewById(R.id.recyclerViewReviews);
        recyclerViewTrailers = findViewById(R.id.recyclerViewTrailers);

        //Присваеваем значения адапетров для видео и отзывов
        reviewAdapter = new ReviewAdapter();
        trailerAdapter = new TrailerAdapter();

        //Проверка слушателя событий
        trailerAdapter.setOnTrailerClickListener(new TrailerAdapter.onTrailerClickListener() {
            @Override
            public void onTrailerClick(String url) {
                Intent intentToTrailer = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intentToTrailer);
            }
        });

        //Устанавливаем внещний вид recyclerView в линию
        recyclerViewReviews.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewTrailers.setLayoutManager(new LinearLayoutManager(this));

        //Устанавливаем у reyclerView необходимые адпетеры
        recyclerViewReviews.setAdapter(reviewAdapter);
        recyclerViewTrailers.setAdapter(trailerAdapter);

        //ПОЛУЧАЕМ СПИСОК НАШИХ ВИДЕО И ОТЗЫВОВ ИЗ JSON
        JSONObject jsonObjectTrailers = NetworkUtils.getJSONForVideos(movie.getId());
        JSONObject jsonObjectReviews = NetworkUtils.getJSONForReviews(movie.getId());

        //ТЕПЕРЬ из полученых JSONObject мы долны получить трецлеры и отызвыв
        ArrayList<Trailer> trailers = JSONUtils.getTrailerFromJSON(jsonObjectTrailers);
        ArrayList<Review> reviews = JSONUtils.getReviewsFromJSON(jsonObjectReviews);

        //Теперь олученые массивы надо установить у адаптеров
        reviewAdapter.setReviews(reviews);
        trailerAdapter.setTrailers(trailers);
    }

    public void onClickChangeFavorite(View view) {
        if(favoriteMovie == null){
            viewModel.insertFavouriteMovie(new FavoriteMovie(movie));
            Toast.makeText(this, getString(R.string.add_to_favourite), Toast.LENGTH_SHORT).show();
        } else {
            viewModel.deleteFavouriteMovie(favoriteMovie);
            Toast.makeText(this, getString(R.string.remove_from_favourite), Toast.LENGTH_SHORT).show();
        }
        setFavourite();
    }

    private void setFavourite(){
        favoriteMovie = viewModel.getFavouriteMovieById(id);
        if(favoriteMovie == null){
            imageViewAddToFavourite.setImageResource(R.drawable.addfavorite);
        } else {
            imageViewAddToFavourite.setImageResource(R.drawable.zvezdafavorite);
        }
    }
}