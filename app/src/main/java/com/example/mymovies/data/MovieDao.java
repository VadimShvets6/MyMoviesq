package com.example.mymovies.data;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

//ПОМЕЧАЕМ ЕГО АНОТАЦИЕЙ Dao
@Dao
public interface MovieDao {
    //ПОЛУЧАЕТ ВСЕ ФИЛЬМЫ ИЗ БД
    @Query("SELECT * FROM movies")
    LiveData<List<Movie>> getAllMovies();

    //ПОЛУЧАЕТ ВСЕ ФИЛЬМЫ ИЗ ИЗБРАНОГО
    @Query("SELECT * FROM favourite_movies")
    LiveData<List<FavoriteMovie>> getAllFavouriteMovies();

    //ПОЛУЧАЕТ ФИЛЬМЫ ПО ИД
    @Query("SELECT * FROM movies WHERE id == :moveId")
    Movie getMovieById(int moveId);

    //ПОЛУЧАЕТ ФИЛЬМЫ ПО ИД
    @Query("SELECT * FROM favourite_movies WHERE id == :moveId")
    FavoriteMovie getFavouriteMovieById(int moveId);

    //УДАЛЯЕТ ВСЕ ФИЛЬМЫ
    @Query("DELETE FROM movies")
    void deleteAllMovies();

    //ВСТАВЛЯАЕТ ФИЛЬМ
    @Insert
    void insertMovie(Movie movie);

    //УДАЛЯЕТ ВЫБРАНЫЙ ФИЛЬМ
    @Delete
    void deleteMovie(Movie movie);

    //ВСТАВЛЯАЕТ ФИЛЬМ
    @Insert
    void insertFavouriteMovie(FavoriteMovie movie);

    //УДАЛЯЕТ ВЫБРАНЫЙ ФИЛЬМ
    @Delete
    void deleteFavouriteMovie(FavoriteMovie movie);
}
