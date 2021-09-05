package com.timecat.module.plugin.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.timecat.data.room.BaseDao

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/9/5
 * @description null
 * @usage null
 */
@Dao
abstract class PluginDao : BaseDao<Plugin> {

    @Query("SELECT * FROM Plugin WHERE id = :id LIMIT 1")
    abstract fun get(id: Long): Plugin?

    @Query("SELECT * FROM Plugin WHERE title = :title LIMIT 1")
    abstract fun get(title: String): Plugin?

    @Query("SELECT * FROM Plugin WHERE uuid = :uuid")
    abstract fun getByUuid(uuid: String): Plugin?

    @Query("SELECT * FROM Plugin ORDER BY `id` DESC")
    abstract fun getAll(): MutableList<Plugin>

    @Query("SELECT * FROM Plugin ORDER BY `id` DESC LIMIT :pageSize OFFSET :offset")
    abstract fun getAll(pageSize: Int, offset: Int): MutableList<Plugin>

    @Transaction
    open fun listSpaces(): MutableList<Plugin> {
        val list = mutableListOf<Plugin>()
        list.addAll(getAll())
        return list
    }

    @Query("SELECT count(*) FROM Plugin")
    abstract fun count(): Int
}