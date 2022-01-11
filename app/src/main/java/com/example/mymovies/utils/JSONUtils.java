package com.example.mymovies.utils;

import com.example.mymovies.data.Movie;
import com.example.mymovies.data.Review;
import com.example.mymovies.data.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JSONUtils {
    // ДЕЛАЕМ ПЕРЕМЕНЫЕ КЛЮЧИ ДЛЯ ЗАПРОСОВ К JSON
    private static final String KEY_RESULTS = "results";

    //                   Для отзыввов
    //КЛЮЧ ДЛЯ ИМЕНИ АВТОРА
    private static final String KEY_AUTHOR = "author";
    //КЛЮЧ ДЛЯ ОТЗЫВА
    private static final String KEY_CONTENT = "content";

    //                   Для видео
    //ПЕРЕМЕНАЯ ХРАНИТ КЛЮЧ ДЛЯ ВИДЕО НА ЮТУБ
    private static final String KEY_KEY_OF_VIDEO = "key";
    //ПЕРЕМЕНАЯ ХРАНИТ НАЗВАНИЕ ФИЛЬМА У ТРЕЙЛЕРА
    private static final String KEY_NAME = "name";
    //ПЕРЕМЕНАЯ ХРАНИТ БАЗОВЙ URL ЮТУБА
    private static final String BASE_YOUTUBE_URL = "https://www.youtube.com/watch?v=";

    //                  Для фильма
    //КЛЮЧ ДЛЯ КОЛИЧЕСТВА ГОЛОСОВ
    private static final String KEY_VOTE_COUNT = "vote_count";
    //КЛЮЧ ДЛЯ ID
    private static final String KEY_ID = "id";
    //КЛЮЧ ДЛЯ ЗАГОЛОВКА/НАЗВАНИЯ
    private static final String KEY_TITLE = "title";
    //КЛЮЧ ДЛЯ ОРИГИНАЛЬНОГО НАЗВАНИЯ
    private static final String KEY_ORIGIN_TITLE = "original_title";
    //КЛЮЧ ДЛЯ ОПИСАНИЯ К ФИЛЬМУ
    private static final String KEY_OVERVIEW = "overview";
    //КЛЮЧ ДЛЯ КАРТИНКИ ФИЛЬМА
    private static final String KEY_POSTER_PATH = "poster_path";
    //КЛЮЧ ДЛЯ BACKGROUNDA
    private static final String KEY_BACKDROP_PATH = "backdrop_path";
    //КЛЮЧ ДЛЯ СРЕДНЕГО КОЛИЧЕСТВА ГООЛОСОВ
    private static final String KEY_VOTE_AVERAGE = "vote_average";
    //КЛЮЧ ДЛЯ ДАТЫ РЕЛИЗА
    private static final String KEY_RELEASE_DATA = "release_date";

    //ПЕРЕМЕНАЯ С БАЗОВЫМ URL ДЛЯ КАРТИНКИ
    public static final String BASE_POSTER_URL = "https://image.tmdb.org/t/p/";
    //ПЕРМЕНАЯ ХРАНИТ РАЗМЕР ДЛЯ МАЛЕНКЬОЙ КАРТИНКИ
    public static final String SMALL_POSTER_SIZE = "w185";
    //ПЕРЕМЕНАЯ ХРАНИТ РАЗМЕР ДЛЯ БОЛЬШОЙ КАРНИТКИ
    public static final String BIG_POSTER_SIZE = "w780";

    //МЕТТОД ВОЗВРАШАЕТ МАССИВ С ОТЫЗВАМИ
    public static ArrayList<Review> getReviewsFromJSON(JSONObject jsonObject){
        ArrayList<Review> result = new ArrayList<>();
        if(jsonObject == null){
            return result;
        }
        try {
            JSONArray jsonArray = jsonObject.getJSONArray(KEY_RESULTS);
            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonObjectReviews = jsonArray.getJSONObject(i);
                //Получаем имя автора
                String authorName = jsonObjectReviews.getString(KEY_AUTHOR);
                //Получаем отзыв
                String content = jsonObjectReviews.getString(KEY_CONTENT);
                Review review = new Review(authorName, content);
                result.add(review);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    //МЕТОД ВОЗВРАШАЕТ МАССИВ С ВИДЕО ТРЕЙЛЕРАМИ
    public static ArrayList<Trailer> getTrailerFromJSON(JSONObject jsonObject){
        ArrayList<Trailer> result = new ArrayList<>();
        if(jsonObject == null){
            return result;
        }
        try {
            JSONArray jsonArray = jsonObject.getJSONArray(KEY_RESULTS);
            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonObjectTrailers = jsonArray.getJSONObject(i);
                //ПОлучаем ссылку
                String key = BASE_YOUTUBE_URL + jsonObjectTrailers.getString(KEY_KEY_OF_VIDEO);
                //Получаем name
                String name = jsonObjectTrailers.getString(KEY_NAME);
                Trailer trailer = new Trailer(key, name);
                result.add(trailer);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }

    //МЕТОД ВОЗВРАШАЕТ МАССИВ С ФИЛЬМАМИ
    public static ArrayList<Movie> getMoviesFromJSON(JSONObject jsonObject){
        //СОЗДАЕМ НАШ МАССИВ
        ArrayList<Movie> result = new ArrayList<>();
        //ДЕЛАЕМ ПРОВЕРКУ ЕСЛИ НАШ jsonObject null
        if(jsonObject == null){
            return result;
        }
        try {
            // ПУОЛУЧАЕМ JSONArray
            JSONArray jsonArray = jsonObject.getJSONArray(KEY_RESULTS);
            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject objectMovie = jsonArray.getJSONObject(i);
                //ПОЛУЧАЕМ ID
                int id = objectMovie.getInt(KEY_ID);
                //ПОЛУЧАЕМ КОЛИЧЕСТО ГОЛОСОВ
                int vote_count = objectMovie.getInt(KEY_VOTE_COUNT);
                //ПОЛУЧАЕМ НАЗВАНИЕ ФИЛЬМА
                String title = objectMovie.getString(KEY_TITLE);
                //ПОУЛЧАЕМ оРИГИНАЛЬноЕ НАЗВАНИЕ
                String originTitle = objectMovie.getString(KEY_ORIGIN_TITLE);
                //ПОЛУЧАЕМ ОПИСАНИЕ ФИЛЬМА
                String overview = objectMovie.getString(KEY_OVERVIEW);
                //ПОЛУЧАЕМ ПУТЬ К КАРТИНКЕ
                String posterPath = BASE_POSTER_URL + SMALL_POSTER_SIZE + objectMovie.getString(KEY_POSTER_PATH);
                //ПОЛУЧАЕМ ПУТЬ К ФОНОВОМУ ИЗОБРАЖЕНИЮ
                String bigPosterPath = BASE_POSTER_URL + BIG_POSTER_SIZE + objectMovie.getString(KEY_POSTER_PATH);
                String backdropPath = objectMovie.getString(KEY_BACKDROP_PATH);
                //ПОЛУЧАЕМ СРЕДНЕЕ КОЛИЧЕСТВО ГОЛОСОВ
                double voteAverage = objectMovie.getInt(KEY_VOTE_AVERAGE);
                //ПОЛУЧАЕМ ДАТУ РЕЛИЗА
                String dataRelease = objectMovie.getString(KEY_RELEASE_DATA);
                //СОЗДАЕМ ОБЬЕКТ ФИЛЬМА И ИСПОЛЬЗУЕМ ВСЕ ПОЛУЧЕНЫЕ ЗНАЧЕНИЯ
                Movie movie = new Movie(id, vote_count, title, originTitle, overview, posterPath, bigPosterPath, backdropPath, voteAverage,dataRelease);
                //И ЗАПИСЫВАЕМ НАШ ФИЛЬМ В МАССИВ
                result.add(movie);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return result;
    }
}
