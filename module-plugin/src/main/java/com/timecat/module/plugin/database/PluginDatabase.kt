package com.timecat.module.plugin.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/9/5
 * @description null
 * @usage null
 */
@Database(
    entities = [
        Plugin::class,
    ],
    version = PluginDatabase.EXPORT_VERSION,
    exportSchema = true
)
abstract class PluginDatabase : RoomDatabase() {
    abstract fun pluginDao(): PluginDao

    companion object {
        const val NAME = "timecat_plugin_room.db"
        const val EXPORT_VERSION = 2

        @JvmStatic
        var instance: PluginDatabase? = null

        @JvmStatic
        @JvmOverloads
        fun forFile(context: Context, fileName: String = NAME): PluginDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    PluginDatabase::class.java,
                    fileName
                )
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return instance!!
        }
    }
}