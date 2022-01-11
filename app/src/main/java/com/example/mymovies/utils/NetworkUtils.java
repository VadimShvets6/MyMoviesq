package com.example.mymovies.utils;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.sql.Connection;
import java.util.concurrent.ExecutionException;

public class NetworkUtils {

    //ПЕРЕМЕНА ХРАНИТ БАЗОВЫЙ URL
    private static final String BASE_URL = "https://api.themoviedb.org/3/discover/movie";
    //ПЕРЕМЕНАЯ ХРАНИТ БАЗОЫЙ URL ДЛЯ ВИДЕО
    private static final String BASE_URL_VIDEO = "https://api.themoviedb.org/3/movie/%s/videos";
    //ПЕРМЕНАЯ ХРАНИТ БАЗОВЫЙ URL ДЛЯ ОТЗЫВОВ
    private static final String BASE_URL_REVIEWS = "https://api.themoviedb.org/3/movie/%s/reviews";
    //ПАРМАТЕРЫ ДЛЯ ЗАПРОСА
    private static final String PARAMS_API_KEY = "api_key";
    //ПАРАМЕТР ЯЗЫКА
    private static final String PARAMS_LANGUAGE = "language";
    //ПАРАМЕТР СОРТИРОВКИ
    private static final String PARAMS_SORT_BY = "sort_by";
    //ПАРАМЕТР СТРАНИЦЫ
    private static final String PARAMS_PAGE = "page";
    // ПЕРЕМЕНАЯ ХРАНИК КЛЮЧ API
    private static final String  API_KEY = "a43ee367646362d37a2d063cef6dffd9";
    //ЗНАЧЕНИЕ ЯЗЫКА РУССКИЙ
    private static final String LANGUAGE_VALUE = "ru-RU";
    //СОРТИРОВКА ПО ПОПУЛЯРНОСТИ
    private static final String SORT_BY_POPULARITY = "popularity.desc";
    //СОРТИРОВКА ПО СРЕДНЕЦ ОЦКНИ
    private static final String SORT_BY_TOP_RATED = "vote_average.desc";
    //ПАРАМЕТР ДЛЯ СОРТИРОВКИ КОЛИЧЕСТО ГОЛОСОВ ЗА ФИЛЬМ. ТО ЕСТЬ НАПРИМЕР ФИЛЬМЫ ЗА КОТОРЫЕ ПРОГОЛОСОВАЛО БОЛЕЕ 1000 ЛЮДЕЙ
    private static final String PARAMS_MIN_VOTE_COUNT = "vote_count.gte";
    //ПЕРЕМЕНАЯ ХРАНИТ МИНИМАЛЬНОЕ КОЛИЧЕСТВО ГОЛОСОВ
    private static final String MIN_VOTE_COUNT_VALUE = "1000";

    //ПЕРЕМЕНЫ ДЛЯ МЕТОДА В ЗАВИСиМОТИ ОТ ВЫБОРА БУДЕТ ВОЗВРАЩАТЬ ЛИБО 1 либо 0
    public static final int POPULARITY = 0;
    public static final int TOP_RATED = 1;

    //МЕТОД ВОЗВРАШАЕТ URL К ОТЗЫВАМ
    public static URL buildURLToReviews(int id){
        Uri uri = Uri.parse(String.format(BASE_URL_REVIEWS, id)).buildUpon()
                .appendQueryParameter(PARAMS_API_KEY, API_KEY).build();
        try {
            return new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //МЕТОД СТРОИТ URL ДЛЯ ВИДЕО
    public static URL buildURLToVideos(int id){
        Uri uri = Uri.parse(String.format(BASE_URL_VIDEO, id)).buildUpon()
                .appendQueryParameter(PARAMS_API_KEY, API_KEY)
                .appendQueryParameter(PARAMS_LANGUAGE, LANGUAGE_VALUE).build();

        try {
            return new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //СТРОИМ URL
    public static URL bildURL(int sortBy, int page){
        URL resault = null;
        String methodOfSort;
        /// ЕСЛИ ХОТИМ ФИЛЬМЫ ПО ПОПУЯРНОСТИ ТО ЭТО
        if(sortBy == POPULARITY)
        {
            methodOfSort = SORT_BY_POPULARITY;
        }
        //ЕСЛИ ПО СРЕДНЕЙ ОЦЕНКИ ТО ЭТО
        else {
            methodOfSort = SORT_BY_TOP_RATED;
        }
        //ФОРМИРУЕМ URL ИЗ НАШИХ ПАРАМЕТРОВ И ЗНАЧЕННИЙ
        Uri uri = Uri.parse(BASE_URL).buildUpon()
                .appendQueryParameter(PARAMS_API_KEY, API_KEY)
                .appendQueryParameter(PARAMS_LANGUAGE, LANGUAGE_VALUE)
                .appendQueryParameter(PARAMS_SORT_BY, methodOfSort)
                .appendQueryParameter(PARAMS_MIN_VOTE_COUNT, MIN_VOTE_COUNT_VALUE)
                .appendQueryParameter(PARAMS_PAGE, Integer.toString(page))
                .build();
        //СОЗДАЕМ САМ URL ЗАПРОС
        try {
            resault = new URL (uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        //ВОЗВРАЩАЕМ ПРАВИЛЬНЫЙ URL
        return resault;
    }

    //МЕТОД ВОЗВРАЩАЕТ JSON ДЛЯ ОТЗЫВОВ
    public static JSONObject getJSONForReviews(int id){
        URL url = buildURLToReviews(id);
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONLoadTask().execute(url).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    //МЕТОД ПОЛУЧАЮЩТЙ JSON для ВИДЕО
    public static JSONObject getJSONForVideos(int id){
        URL url = buildURLToVideos(id);
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONLoadTask().execute(url).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    //МЕТОД ПОЛУЧАЮЩИЙ JSON ИЗ СЕТИ
    public static JSONObject getJSONFromNetwork(int srotBy, int page){
        URL url = bildURL(srotBy,page);
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONLoadTask().execute(url).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


    //МЕТОД ЗАГРУЩИК ДЛЯ ФИЛЬМОВ ДЛЯ ПОДГРУЗКИ С ИСПОЛЬЗОВАНИЕМ AsynTasckLoader
    public static class JSONLoader extends AsyncTaskLoader<JSONObject>{

        //Интерфейс для проверки если началась загрузка
        public interface OnStartLoadingListener{
            void onStartLoading();
        }
        //Обект слушателя
        private OnStartLoadingListener onStartLoadingListener;

        //Cеттер на обект слушателя
        public void setOnStartLoadingListener(OnStartLoadingListener onStartLoadingListener) {
            this.onStartLoadingListener = onStartLoadingListener;
        }

        //Когда запускается загрузчик он должен будет загрузить данные с какого то url, так вот источник данных
        //обычно передают в объекте Bundle, и предадим его в переопредленом конструкторе в ручную
        private Bundle bundle;

        //ПЕРЕОПРЕДЕЛЯЕМ КОНСТРУКТОР
        public JSONLoader(@NonNull Context context, Bundle bundle) {
            super(context);
            //Присваеваем значение bundle
            this.bundle = bundle;
        }

        //Что бы при инициальзации данного загрузчика происходила загрузка необходима переопделеить метод onStartLoading
        @Override
        protected void onStartLoading() {
            super.onStartLoading();
            //И в этом методе мы вызываем метод forceLoad();
            if(onStartLoadingListener != null){
                onStartLoadingListener.onStartLoading();
            }
            forceLoad(); //"Продолжить загрузку
        }

        //Переопредляем методы, тут мы должны получить URL откуда хотим загрузить даные
        @Nullable
        @Override
        public JSONObject loadInBackground() {
            //Получим URl ввиде строки, и так как bundle можеть быть null - добавим проверку
            if(bundle == null){
                return null;
            }
            //Получим URl ввиде строки
            String urlAsString = bundle.getString("url");

            //Теперь нам надо получит объект типа URL
            URL url = null;
            try {
                url = new URL(urlAsString);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            //CОЗДАЕМ JSONObject
            JSONObject jsonObject = null;
            if(url == null){
                return null;
            }
            //ОТКРЫВАЕМ СОЕДИНЕНИЕ

            StringBuilder builder = null;
            HttpURLConnection urlConnection = null;
            try {
                //ОТКРЫВАЕМ СОЕДЕНИНЕИЕ И ЧИТАЕМ ДАННЫЕ ПОСТРОЧНО И ЗАПИСЫВАЕм ИХ В БИЛДеР ПОСЛЕ ЧЕГО JSON обекту присваем получаную строчку
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = urlConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                builder = new StringBuilder();
                String line = bufferedReader.readLine();
                while(line != null){
                    builder.append(line);
                    line = bufferedReader.readLine();
                }
                jsonObject = new JSONObject(builder.toString());
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            } finally {
                if(urlConnection != null){
                    urlConnection.disconnect();
                }
            }
            //ВОЗВРАШАЕМ JSONOBject
            return jsonObject;
        }
    }

    //МЕТОД ПОЛУЧАЮЩЙ ДАННЫЕ ИЗ ИНТЕРНЕТА И ВОЗВРАЩАЕТ JSONObject (получает URL,
    // в процесе выполнения данные нам не нудны Void, и возвращать будет JSONObject)
    private static class JSONLoadTask extends AsyncTask<URL, Void, JSONObject>{
        @Override
        protected JSONObject doInBackground(URL... urls) {
            //CОЗДАЕМ JSONObject
            JSONObject jsonObject = null;
            if(urls == null || urls.length == 0){
                return jsonObject;
            }
            //ОТКРЫВАЕМ СОЕДИНЕНИЕ

            StringBuilder builder = null;
            HttpURLConnection urlConnection = null;
            try {
                //ОТКРЫВАЕМ СОЕДЕНИНЕИЕ И ЧИТАЕМ ДАННЫЕ ПОСТРОЧНО И ЗАПИСЫВАЕм ИХ В БИЛДеР ПОСЛЕ ЧЕГО JSON обекту присваем получаную строчку
                urlConnection = (HttpURLConnection) urls[0].openConnection();
                InputStream inputStream = urlConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                builder = new StringBuilder();
                String line = bufferedReader.readLine();
                while(line != null){
                    builder.append(line);
                    line = bufferedReader.readLine();
                }
                jsonObject = new JSONObject(builder.toString());
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            } finally {
                if(urlConnection != null){
                    urlConnection.disconnect();
                }
            }
            //ВОЗВРАШАЕМ JSONOBject
            return jsonObject;
        }
    }
}
