package com.github.didahdx.githubapp.data.local

import androidx.room.TypeConverter

class TopicsConvertor {
    companion object {
        private const val SEPARATOR = "|"
    }

    @TypeConverter
    fun fromTopicsList(topics: List<String>?): String? {
        return topics?.joinToString(SEPARATOR)
    }

    @TypeConverter
    fun toTopicsList(topic: String?): List<String>? {
        return topic?.split(SEPARATOR)
    }

}