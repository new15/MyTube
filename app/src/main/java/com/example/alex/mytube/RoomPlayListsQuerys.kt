package com.example.alex.mytube

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query

@Dao
interface RoomPlayListsQueries {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(videoTable: RoomVideoTable)

    @Query("SELECT * FROM video")
    fun getAllVideos(): LiveData<List<RoomVideoTable>>

    @Query("SELECT * FROM video WHERE play_list_name = :playListId")
    fun getVideosByPlayList(playListId: String): LiveData<List<RoomVideoTable>>


    @Query("SELECT video_id FROM video WHERE video_id = :s")
    fun checkVideoItem(s: String): String
}