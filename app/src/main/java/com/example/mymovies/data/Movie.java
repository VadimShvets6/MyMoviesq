package com.example.mymovies.data;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Insert;
import androidx.room.PrimaryKey;

//БУДЕТ ТАБЛИЦЕЙ В БАЗЕ ДАННЫХ
@Entity(tableName = "movies")
public class Movie {
    //uniqueId БУДЕТ ПЕРВИЧНЫМ КЛЮЧЕМ И ДОБАВЛЯЕМ СТРОКУ @PrimaryKEy
    @PrimaryKey(autoGenerate = true)
    private int uniqueId;
    private int id;
    //ПЕРЕМЕНАЯ ХРАНИТ КОЛИЧЕСТВО ГОЛОСОВ
    private int voteCunt;
    //ПЕРЕМЕНАЯ ХРАНИТ НАЗВАНИЕ/ЗОГОЛОВОК
    private String title;
    //ПЕРЕМЕАЯ ХРАНИТ ОРИГИНАЛЬНОЕ НАЗВАНИЕ
    private String originTitle;
    //ПЕРЕМЕНАЯ ХРАНИТ ОПИСАНИЕ ФИЛЬМА
    private String overview;
    //ПЕРЕМЕНАЯ ХРАНИТ ПУТЬ К ПОСТЕРУ
    private String posterPath;
    //ПЕРЕМЕНАЯ ХРАНИТ РАЗМЕР БОЛЬШОГО ИЗОБРАЩЕНИЯ
    private String bigPosterPath;
    //ПЕРЕМЕНАЯ ХРАНИТ ФОНОВОЕ ИЗОБРАЖЕНИЕ
    private String backdropPath;
    //ПЕРЕМЕНАЯ ХРАНИТ РЕЙТИНГ
    private double voteAverage;
    //ПЕРЕМЕНАЯ ХРАНИТ ДАТУ РЕЛИЗА
    private String releaseData;

    //КОНСТРУКТОР в Котором присутсвтует уникальный ид к фильму для баз данныз
    public Movie(int uniqueId, int id, int voteCunt, String title, String originTitle, String overview, String posterPath, String bigPosterPath, String backdropPath, double voteAverage, String releaseData) {
        this.uniqueId = uniqueId;
        this.id = id;
        this.voteCunt = voteCunt;
        this.title = title;
        this.originTitle = originTitle;
        this.overview = overview;
        this.posterPath = posterPath;
        this.bigPosterPath = bigPosterPath;
        this.backdropPath = backdropPath;
        this.voteAverage = voteAverage;
        this.releaseData = releaseData;
    }

    @Ignore
    //КОНСТРУКТОР Для того что бы мы сами могли создавать обекты фильмов
    public Movie( int id, int voteCunt, String title, String originTitle, String overview, String posterPath, String bigPosterPath, String backdropPath, double voteAverage, String releaseData) {
        this.id = id;
        this.voteCunt = voteCunt;
        this.title = title;
        this.originTitle = originTitle;
        this.overview = overview;
        this.posterPath = posterPath;
        this.bigPosterPath = bigPosterPath;
        this.backdropPath = backdropPath;
        this.voteAverage = voteAverage;
        this.releaseData = releaseData;
    }

    //ГЕТТЕРЫ


    public int getUniqueId() {
        return uniqueId;
    }

    public String getBigPosterPath() {
        return bigPosterPath;
    }

    public int getId() {
        return id;
    }

    public int getVoteCunt() {
        return voteCunt;
    }

    public String getTitle() {
        return title;
    }

    public String getOriginTitle() {
        return originTitle;
    }

    public String getOverview() {
        return overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public String getReleaseData() {
        return releaseData;
    }

    //СЕТТЕРЫ


    public void setUniqueId(int uniqueId) {
        this.uniqueId = uniqueId;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setBigPosterPath(String bigPosterPath) {
        this.bigPosterPath = bigPosterPath;
    }

    public void setVoteCunt(int voteCunt) {
        this.voteCunt = voteCunt;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setOriginTitle(String originTitle) {
        this.originTitle = originTitle;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public void setReleaseData(String releaseData) {
        this.releaseData = releaseData;
    }
}
