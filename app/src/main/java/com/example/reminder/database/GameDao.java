package com.example.reminder.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.reminder.model.Game;

import java.util.List;

@Dao
public interface GameDao {

	@Query("SELECT * FROM game")
	List<Game> getAllGames();

	@Insert
	void insertGame(Game game);

	@Delete
	void deleteGame(Game game);

	@Delete
	void deleteAllGames(List<Game> games);

	@Update
	void updateGame(Game game);
}

