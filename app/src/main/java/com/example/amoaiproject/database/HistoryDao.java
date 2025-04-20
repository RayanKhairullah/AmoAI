package com.example.amoaiproject.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface HistoryDao {
    @Insert
    void insertHistory(History history);

    @Query("SELECT * FROM history ORDER BY id DESC")
    List<History> getAllHistory();

    @Delete
    void deleteHistory(History history);
}
