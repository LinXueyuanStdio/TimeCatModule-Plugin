package com.timecat.module.skin.database

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
        Skin::class,
    ],
    version = SkinDatabase.EXPORT_VERSION,
    exportSchema = true,
)
abstract class SkinDatabase : RoomDatabase() {
    abstract fun skinDao(): SkinDao

    companion object {
        const val NAME = "timecat_skin_room.db"
        const val EXPORT_VERSION = 2

        @JvmStatic
        var instance: SkinDatabase? = null

        @JvmStatic
        @JvmOverloads
        fun forFile(context: Context, fileName: String = NAME): SkinDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    SkinDatabase::class.java,
                    fileName
                ).addMigrations()
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return instance!!
        }
    }
}