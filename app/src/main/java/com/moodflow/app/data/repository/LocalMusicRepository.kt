package com.moodflow.app.data.repository

import android.content.ContentResolver
import android.content.ContentUris
import android.database.Cursor
import android.os.Build
import android.provider.MediaStore
import android.provider.MediaStore.Audio.AudioColumns
import com.moodflow.app.data.db.TrackDao
import com.moodflow.app.data.db.TrackEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocalMusicRepository(
    private val contentResolver: ContentResolver,
    private val trackDao: TrackDao
) {

    suspend fun scanLocalMusic() = withContext(Dispatchers.IO) {
        val tracks = mutableListOf<TrackEntity>()
        val collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
        } else {
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
        }

        val projection = arrayOf(
            AudioColumns._ID,
            AudioColumns.TITLE,
            AudioColumns.ARTIST,
            AudioColumns.ALBUM,
            AudioColumns.DURATION,
            AudioColumns.DATA
        )

        val selection = "${AudioColumns.IS_MUSIC} = 1 AND ${AudioColumns.DURATION} > 180000"

        try {
            contentResolver.query(
                collection,
                projection,
                selection,
                null,
                "${AudioColumns.TITLE} ASC"
            )?.use { cursor ->
                while (cursor.moveToNext()) {
                    val track = cursorToTrackEntity(cursor, collection)
                    if (track != null) {
                        tracks.add(track)
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        if (tracks.isNotEmpty()) {
            trackDao.insertTracks(tracks)
        }
        tracks
    }

    private fun cursorToTrackEntity(cursor: Cursor, collection: android.net.Uri): TrackEntity? {
        return try {
            val id = cursor.getLong(cursor.getColumnIndexOrThrow(AudioColumns._ID))
            val title = cursor.getString(cursor.getColumnIndexOrThrow(AudioColumns.TITLE)) ?: "Unknown"
            val artist = cursor.getString(cursor.getColumnIndexOrThrow(AudioColumns.ARTIST)) ?: "Unknown Artist"
            val album = cursor.getString(cursor.getColumnIndexOrThrow(AudioColumns.ALBUM)) ?: "Unknown Album"
            val duration = cursor.getLong(cursor.getColumnIndexOrThrow(AudioColumns.DURATION))
            val data = cursor.getString(cursor.getColumnIndexOrThrow(AudioColumns.DATA))

            val contentUri = ContentUris.withAppendedId(collection, id)

            TrackEntity(
                id = id.toString(),
                title = title,
                artist = artist,
                album = album,
                duration = duration,
                source = "local",
                audioPath = data,
                spotifyUri = null
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun getLocalTrackCount(): Long {
        return withContext(Dispatchers.IO) {
            val collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
            } else {
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
            }

            val selection = "${AudioColumns.IS_MUSIC} = 1 AND ${AudioColumns.DURATION} > 180000"

            try {
                contentResolver.query(collection, arrayOf(AudioColumns._ID), selection, null, null)?.use { cursor ->
                    cursor.count.toLong()
                } ?: 0L
            } catch (e: Exception) {
                e.printStackTrace()
                0L
            }
        }
    }

    suspend fun clearLocalTracks() = withContext(Dispatchers.IO) {
        trackDao.clearAllTracks()
    }
}
