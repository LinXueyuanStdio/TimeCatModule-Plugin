package com.timecat.module.skin.database

import androidx.room.Dao
import androidx.room.Query
import com.timecat.data.room.BaseDao

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/9/5
 * @description null
 * @usage null
 */
@Dao
abstract class SkinDao : BaseDao<Skin> {

    @Query("SELECT * FROM Skin WHERE id = :id LIMIT 1")
    abstract fun get(id: Long): Skin?

    @Query("SELECT * FROM Skin WHERE title = :title LIMIT 1")
    abstract fun get(title: String): Skin?

    @Query("SELECT * FROM Skin WHERE uuid = :uuid")
    abstract fun getByUuid(uuid: String): Skin?

    @Query("SELECT * FROM Skin ORDER BY `id` DESC")
    abstract fun getAll(): MutableList<Skin>

    @Query("SELECT * FROM Skin WHERE uuid IN(:ids) ORDER BY `id` DESC")
    abstract fun getAll(ids: List<String>): MutableList<Skin>

    @Query("SELECT * FROM Skin ORDER BY `id` DESC LIMIT :pageSize OFFSET :offset")
    abstract fun getAll(pageSize: Int, offset: Int): MutableList<Skin>

    @Query("SELECT count(*) FROM Skin")
    abstract fun count(): Int
}