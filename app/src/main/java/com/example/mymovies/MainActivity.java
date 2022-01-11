package com.example.mymovies;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ProgressBar;
import android.widget.Switch;
import android.widget.TextView;

import com.example.mymovies.adapters.MovieAdapter;
import com.example.mymovies.data.MainViewModel;
import com.example.mymovies.data.Movie;
import com.example.mymovies.utils.JSONUtils;
import com.example.mymovies.utils.NetworkUtils;

import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity /*Показываем что Мэин является слушателем*-->*/ implements LoaderManager.LoaderCallbacks<JSONObject> {

    private RecyclerView recyclerView;
    private MovieAdapter movieAdapter;
    private Switch switchSort;
    private TextView textViewTopRated;
    private TextView textViewPopularity;
    //Для вида загрузки, крятщийся круг
    private ProgressBar progressBarLoading;

    private MainViewModel viewModel;

    //Укникальный id загрузчика
    private static final int LOADER_ID = 133;
    //Так же ддля загрузчика понадобится манаджер
    private LoaderManager loaderManager;

    //Страницы для подгрузки
    private static int page = 1;
    //Проверяет если загруженно  фильмы
    private static boolean isLoading = false;
    //Выбраный метод сортировки
    private static int methodOfSort;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
       // viewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(MainViewModel.class);

        //Устанавливаем всем элементам значения
        recyclerView = findViewById(R.id.recyclerViewPosters);
        textViewTopRated = findViewById(R.id.textViewTopRated);
        textViewPopularity = findViewById(R.id.textViewPopularity);
        switchSort = findViewById(R.id.switchSort);
        loaderManager = LoaderManager.getInstance(this);
        progressBarLoading = findViewById(R.id.progresBarLoading);

        //Присваевыем внешний вид recyclerView в данном случае ввиде сетки
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        //Задаем значение адаптеру
        movieAdapter = new MovieAdapter();

        //Получаем значения из JSON из интернета
        JSONObject jsonObject = NetworkUtils.getJSONFromNetwork(NetworkUtils.POPULARITY,1);

        //Получаем информацию о фильме из полученого JSON обеъкта и заполняем в массив
        ArrayList<Movie> movies = JSONUtils.getMoviesFromJSON(jsonObject);

        //Устанавливаем значения адаптеру
        movieAdapter.setMovies(movies);

        //И устанавливаем recyclerView наш адаптер уже с фильмами
        recyclerView.setAdapter(movieAdapter);
        switchSort.setChecked(true);
        switchSort.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            setMethodoOfSort(isChecked);
            page = 1;
            }
        });
        switchSort.setChecked(false);
        movieAdapter.setOnPosterClickListener(new MovieAdapter.onPosterClickListener() {
            @Override
            public void onPosterClick(int position) {
                //ПОЛУЧЕМ ФИЛЬМ НА КОТОРЫЙ НАЖАЛИ
                Movie movie = movieAdapter.getMovies().get(position);
                //СОЗДАЕМ НОВЫЙ ИНТЕНТ
                Intent intent = new Intent(MainActivity.this, DatailActivity.class);
                //ПЕРЕДАЕМ ид В ИНТЕНТ НОВЫЙ
                intent.putExtra("id", movie.getId());
                //ЗАПУСКАЕМ АКТТИВНОСТЬ
                startActivity(intent);
            }
        });
        movieAdapter.setOnReachEndListener(new MovieAdapter.OnReachEndListener() {
            @Override
            public void onReachEnd() {
                if(!isLoading){
                    downloadData(methodOfSort, page);
                }
            }
        });
        LiveData<List<Movie>> moviesFromLiveData = viewModel.getMovies();
        moviesFromLiveData.observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> movies) {
                if(page == 1){
                    movieAdapter.setMovies(movies);
                }
            }
        });
    }

    public void onClickSetPopylarity(View view) {
        setMethodoOfSort(false);
        switchSort.setChecked(false);
    }

    public void onClickSetTopRated(View view) {
        setMethodoOfSort(true);
        switchSort.setChecked(true);
    }

    private void setMethodoOfSort (boolean isTopRated) {
        if (isTopRated) {
            textViewTopRated.setTextColor(getResources().getColor(R.color.ping));
            textViewPopularity.setTextColor(getResources().getColor(R.color.white));
            methodOfSort = NetworkUtils.TOP_RATED;
        } else {
            methodOfSort = NetworkUtils.POPULARITY;
            textViewTopRated.setTextColor(getResources().getColor(R.color.white));
            textViewPopularity.setTextColor(getResources().getColor(R.color.ping));
        }
        downloadData(methodOfSort, page);
    }

    //ЗАГРУЗКА ДАННЫХ
    private void downloadData(int methodOfSort, int page){
        //ПОЛУЧАЕМ URL
        URL url = NetworkUtils.bildURL(methodOfSort, page);
        //Строим обхект bundle
        Bundle bundle = new Bundle();
        bundle.putString("url", url.toString());
        //Теперь необходимо запустить загрузчик
        loaderManager.restartLoader(LOADER_ID, bundle, this);
    }

    //ПЕРЕОПРЕДЕЛЯМ МЕТОДЫ ДЛЯ интефейса LoaderCollback

    @NonNull
    @Override
    public Loader<JSONObject> onCreateLoader(int id, @Nullable Bundle args) {
        //Создаем наш загрузчик
        NetworkUtils.JSONLoader jsonLoader = new NetworkUtils.JSONLoader(this, args);

        //Добовляем слшуателя для загрузчика для проверки если началась загрузка
        jsonLoader.setOnStartLoadingListener(new NetworkUtils.JSONLoader.OnStartLoadingListener() {
            @Override
            public void onStartLoading() {
                progressBarLoading.setVisibility(View.VISIBLE);
                isLoading = true;
            }
        });
        //Возврашаем полученый загручик
        return jsonLoader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<JSONObject> loader, JSONObject data) {
        ArrayList<Movie> movies = JSONUtils.getMoviesFromJSON(data);
        if(movies != null && !movies.isEmpty()){
            if(page == 1) {
                viewModel.deleteAllMovies();
                movieAdapter.clear();
            }
            for(Movie movie : movies){
                viewModel.insertMovie(movie);
            }
            movieAdapter.addMovies(movies);
            page++;
        }
        isLoading = false;
        progressBarLoading.setVisibility(View.INVISIBLE);
        loaderManager.destroyLoader(LOADER_ID);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<JSONObject> loader) {

    }
}