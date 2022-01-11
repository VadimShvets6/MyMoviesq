package com.example.mymovies.data;

import androidx.room.Entity;
import androidx.room.Ignore;

//ЭТО ТАБЛИЦА В БД ФИЛЬМВО ДОБАВЛЕНЫХ В ИЗБРАНОЕ
@Entity(tableName = "favourite_movies")
public class FavoriteMovie extends Movie{
    public FavoriteMovie(int uniqueId, int id, int voteCunt, String title, String originTitle, String overview, String posterPath, String bigPosterPath, String backdropPath, double voteAverage, String releaseData) {
        super(uniqueId, id, voteCunt, title, originTitle, overview, posterPath, bigPosterPath, backdropPath, voteAverage, releaseData);
    }

    @Ignore
    public FavoriteMovie(Movie movie) {
        super(movie.getUniqueId(), movie.getId(), movie.getVoteCunt(), movie.getTitle(), movie.getOriginTitle(), movie.getOverview(), movie.getPosterPath(), movie.getBigPosterPath(), movie.getBackdropPath(), movie.getVoteAverage(), movie.getReleaseData());
    }
}
