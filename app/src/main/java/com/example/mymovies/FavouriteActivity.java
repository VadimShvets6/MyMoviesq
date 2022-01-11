package com.example.mymovies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.example.mymovies.adapters.MovieAdapter;
import com.example.mymovies.data.FavoriteMovie;
import com.example.mymovies.data.MainViewModel;
import com.example.mymovies.data.Movie;

import java.util.ArrayList;
import java.util.List;

public class FavouriteActivity extends AppCompatActivity {

    //CCЫЛКА НА RecyclerView
    private RecyclerView recyclerViewFavouriteMovies;

    //ИСПОЛЬЗУЕМ ТОТ ЖЕ АДАПТЕР ЧТО И В ПЕРВОЙ АКТИВНОСТИ
    private MovieAdapter movieAdapter;

    //ЧТО БЫ ПОЛУЧИТЬ СПИСОК ФИЛЬМОВ
    private MainViewModel viewModel;

    //-------------------------------------------------------------------------------------------------------
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
    //----------------------------------------------------------------------------------------------------

    // АКТИВНОСТЬ СОХРАНЕНЫХ ФИЛЬМОВ КОТОРЫЕМ БОЛЬШЕ ВСЕГО НРАВЯТСЯ
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);

        //УСТАНОВИМ ЗНАЧЕНИЕ RecyclerView
        recyclerViewFavouriteMovies = findViewById(R.id.recyclerViewFavouriteMovies);

        //УСТАНОВИМ РАЗМЕТКУ ДЛЯ RecyclerView ввиде СЕТКИ
        recyclerViewFavouriteMovies.setLayoutManager(new GridLayoutManager(this, 2));

        //ПРИСВАЕВАЕМ ЗНАЧЕНИЕ АДАПТЕРУ
        movieAdapter = new MovieAdapter();

        //УСТАНОВИМ АДАПТЕР У RecyclerView
        recyclerViewFavouriteMovies.setAdapter(movieAdapter);

        //ПРИСВАЕМ ЗНАЧЕНИЕ MainViewModel
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);

        //ПОЛУЧАЕМ СПИСОК ЛЮБИМЫХ ФИЛЬМОВ
        LiveData<List<FavoriteMovie>> favouriteMovie = viewModel.getFavouriteMovies();

        //ДОБАВЛЯЕМ ОБСЕРВЕР
        favouriteMovie.observe(this, new Observer<List<FavoriteMovie>>() {
            @Override
            public void onChanged(List<FavoriteMovie> favoriteMovies) {

                //МЫ НЕ МОЖЕМ В ЛИСТ РОДИТЕЛЬСКОГО КЛАССА ДОБАВЛЯТЬ ЛИСТ С ЭЛЕМЕНТАМИ ДОЧЕРНЕГО КЛАССА ПОЭТОМУ СОЗАИМ ЕШЕ ОДИН ЛИСТ
                List<Movie> movies = new ArrayList<>();

                //ПРОВЕРИМ ЕСЛИ У НАС ЕСТЬ ЧТО ТО В FavouriteMovie
                if(favoriteMovies != null) {
                    movies.addAll(favoriteMovies);
                    movieAdapter.setMovies(movies);
                }
            }
        });

        movieAdapter.setOnPosterClickListener(new MovieAdapter.onPosterClickListener() {
            @Override
            public void onPosterClick(int position) {
                //ПОЛУЧЕМ ФИЛЬМ НА КОТОРЫЙ НАЖАЛИ
                Movie movie = movieAdapter.getMovies().get(position);
                //СОЗДАЕМ НОВЫЙ ИНТЕНТ
                Intent intent = new Intent(FavouriteActivity.this, DatailActivity.class);
                //ПЕРЕДАЕМ ид В ИНТЕНТ НОВЫЙ
                intent.putExtra("id", movie.getId());
                //ЗАПУСКАЕМ АКТТИВНОСТЬ
                startActivity(intent);
            }
        });

    }
}