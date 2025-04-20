package com.example.amoaiproject.database;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "history")
public class History {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String responseText;
    public String timestamp;

    public History(String responseText, String timestamp) {
        this.responseText = responseText;
        this.timestamp = timestamp;
    }
}
