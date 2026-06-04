package com.moodflow.app.data.recommendation

import com.moodflow.app.data.db.TrackEntity
import com.moodflow.app.data.db.TrackMoodTagDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RecommendationEngine @Inject constructor(
    private val trackMoodTagDao: TrackMoodTagDao
) {
    
    /**
     * Get recommended tracks for a specific mood
     */
    suspend fun getRecommendationsForMood(
        moodId: Int,
        limit: Int = 10
    ): List<TrackEntity> {
        // Get weighted tracks for this mood from the database
        val weightedTracks = trackMoodTagDao.getTracksForMoodByWeight(moodId, limit)
        
        // If no learned preferences, return default recommendations
        return if (weightedTracks.isEmpty()) {
            getDefaultRecommendationsForMood(moodId)
        } else {
            // Sort by weight (which is already done in the query) and limit results
            weightedTracks.take(limit).map { tag ->
                // In production, you'd fetch the actual TrackEntity here
                // For now, this returns placeholder track data
                createPlaceholderTrack(tag.trackId)
            }
        }
    }
    
    /**
     * Get default recommendations based on mood type
     * This is used when user hasn't built up listening history
     */
    private suspend fun getDefaultRecommendationsForMood(moodId: Int): List<TrackEntity> {
        return when (moodId) {
            1 -> getHappyMoodDefaults()
            2 -> getSadMoodDefaults()
            3 -> getEnergizedMoodDefaults()
            4 -> getRelaxedMoodDefaults()
            5 -> getFocusedMoodDefaults()
            else -> emptyList()
        }
    }
    
    private fun getHappyMoodDefaults(): List<TrackEntity> {
        return listOf(
            TrackEntity(
                id = "happy_1",
                title = "Good as Hell",
                artist = "Lizzo",
                album = "Cuz I Love You",
                duration = 189000,
                source = "spotify",
                spotifyUri = "spotify:track:happy1"
            ),
            TrackEntity(
                id = "happy_2",
                title = "Walking on Sunshine",
                artist = "Katrina & The Waves",
                album = "Walking on Sunshine",
                duration = 180000,
                source = "spotify",
                spotifyUri = "spotify:track:happy2"
            ),
            TrackEntity(
                id = "happy_3",
                title = "Don't Stop Me Now",
                artist = "Queen",
                album = "News of the World",
                duration = 215000,
                source = "spotify",
                spotifyUri = "spotify:track:happy3"
            )
        )
    }
    
    private fun getSadMoodDefaults(): List<TrackEntity> {
        return listOf(
            TrackEntity(
                id = "sad_1",
                title = "Someone Like You",
                artist = "Adele",
                album = "21",
                duration = 285000,
                source = "spotify",
                spotifyUri = "spotify:track:sad1"
            ),
            TrackEntity(
                id = "sad_2",
                title = "The Night We Met",
                artist = "Lord Huron",
                album = "Lonesome Dreams",
                duration = 243000,
                source = "spotify",
                spotifyUri = "spotify:track:sad2"
            ),
            TrackEntity(
                id = "sad_3",
                title = "Hurt",
                artist = "Johnny Cash",
                album = "American Recordings",
                duration = 233000,
                source = "spotify",
                spotifyUri = "spotify:track:sad3"
            )
        )
    }
    
    private fun getEnergizedMoodDefaults(): List<TrackEntity> {
        return listOf(
            TrackEntity(
                id = "energy_1",
                title = "Blinding Lights",
                artist = "The Weeknd",
                album = "After Hours",
                duration = 200000,
                source = "spotify",
                spotifyUri = "spotify:track:energy1"
            ),
            TrackEntity(
                id = "energy_2",
                title = "Uptown Funk",
                artist = "Mark Ronson ft. Bruno Mars",
                album = "Uptown Special",
                duration = 269000,
                source = "spotify",
                spotifyUri = "spotify:track:energy2"
            ),
            TrackEntity(
                id = "energy_3",
                title = "Levitating",
                artist = "Dua Lipa",
                album = "Future Nostalgia",
                duration = 203000,
                source = "spotify",
                spotifyUri = "spotify:track:energy3"
            )
        )
    }
    
    private fun getRelaxedMoodDefaults(): List<TrackEntity> {
        return listOf(
            TrackEntity(
                id = "relax_1",
                title = "Weightless",
                artist = "Marconi Union",
                album = "Weightless",
                duration = 480000,
                source = "spotify",
                spotifyUri = "spotify:track:relax1"
            ),
            TrackEntity(
                id = "relax_2",
                title = "Peaceful Piano",
                artist = "Spotify Playlist",
                album = "Peaceful Piano",
                duration = 300000,
                source = "spotify",
                spotifyUri = "spotify:track:relax2"
            ),
            TrackEntity(
                id = "relax_3",
                title = "Teardrop",
                artist = "Massive Attack",
                album = "Mezzanine",
                duration = 298000,
                source = "spotify",
                spotifyUri = "spotify:track:relax3"
            )
        )
    }
    
    private fun getFocusedMoodDefaults(): List<TrackEntity> {
        return listOf(
            TrackEntity(
                id = "focus_1",
                title = "Lo-Fi Hip Hop",
                artist = "Spotify Playlist",
                album = "Lo-Fi Beats",
                duration = 240000,
                source = "spotify",
                spotifyUri = "spotify:track:focus1"
            ),
            TrackEntity(
                id = "focus_2",
                title = "Ambient Sounds",
                artist = "Brian Eno",
                album = "Music for Airports",
                duration = 1200000,
                source = "spotify",
                spotifyUri = "spotify:track:focus2"
            ),
            TrackEntity(
                id = "focus_3",
                title = "Study Music",
                artist = "Various Artists",
                album = "Study Beats",
                duration = 300000,
                source = "spotify",
                spotifyUri = "spotify:track:focus3"
            )
        )
    }
    
    /**
     * Create placeholder track from ID
     * In production, this would fetch from database
     */
    private fun createPlaceholderTrack(trackId: String): TrackEntity {
        return TrackEntity(
            id = trackId,
            title = "Track from recommendations",
            artist = "Unknown Artist",
            album = "Unknown Album",
            duration = 180000,
            source = "spotify",
            spotifyUri = "spotify:track:$trackId"
        )
    }
}
