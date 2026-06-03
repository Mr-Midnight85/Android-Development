package com.moodflow.app.data.model

data class Mood(
    val id: Int,
    val name: String,
    val emoji: String,
    val description: String,
    val color: String,
    val isCustom: Boolean = false
)

object MoodDefaults {
    val moods = listOf(
        Mood(1, "Happy", "😊", "Feeling joyful and upbeat", "#FFD700"),
        Mood(2, "Sad", "😢", "Feeling melancholic or down", "#4169E1"),
        Mood(3, "Energetic", "⚡", "Feeling pumped and active", "#FF6347"),
        Mood(4, "Calm", "🧘", "Feeling peaceful and relaxed", "#98D8C8"),
        Mood(5, "Focused", "🎯", "In the zone, concentrating", "#9370DB"),
        Mood(6, "Romantic", "💕", "Feeling love and affection", "#FF1493"),
        Mood(7, "Nostalgic", "🌅", "Feeling reflective and sentimental", "#DEB887"),
        Mood(8, "Anxious", "😰", "Feeling worried or stressed", "#FF8C00"),
        Mood(9, "Confident", "💪", "Feeling strong and assured", "#FFD700"),
        Mood(10, "Chill", "😎", "Feeling laid-back and cool", "#87CEEB"),
        Mood(11, "Creative", "🎨", "Feeling inspired and imaginative", "#FF69B4"),
        Mood(12, "Introspective", "🤔", "Feeling thoughtful and contemplative", "#A9A9A9"),
        Mood(13, "Adventurous", "🚀", "Feeling bold and daring", "#FF4500"),
        Mood(14, "Melancholic", "🌧️", "Feeling deep sadness or loss", "#708090"),
        Mood(15, "Motivated", "🔥", "Feeling driven and determined", "#FF6347"),
        Mood(16, "Playful", "🎭", "Feeling fun and light-hearted", "#FFB6C1"),
        Mood(17, "Peaceful", "☮️", "Feeling serene and balanced", "#7FFFD4"),
        Mood(18, "Rebellious", "✊", "Feeling defiant and free-spirited", "#000000"),
        Mood(19, "Hopeful", "🌟", "Feeling optimistic and positive", "#FFD700"),
        Mood(20, "Sensual", "🌹", "Feeling passionate and sensory", "#C71585")
    )
}
