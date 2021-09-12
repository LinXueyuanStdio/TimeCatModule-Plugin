package com.timecat.module.plugin.manager

import android.content.Context
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONObject
import com.timecat.module.plugin.database.Plugin
import java.io.File
import java.io.Serializable

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/6/27
 * @description null
 * @usage null
 */
data class PluginInfo(
    var uuid: String,
    var type: Long,
    var name: String,

    var managerVersionCode: Int,
    var managerVersionName: String,
    var managerUrl: String,
    /**
     * 动态加载的插件管理apk
     */
    var managerFilename: String,
) : Serializable {
    companion object {
        fun fromJson(json: String) = fromJson(JSON.parseObject(json))
        fun fromJson(jsonObject: JSONObject): PluginInfo {
            val uuid: String = jsonObject.getString("uuid")
            val type: Long = jsonObject.getLong("type")
            val name: String = jsonObject.getString("name")

            val managerVersionCode: Int = jsonObject.getInteger("managerVersionCode")
            val managerVersionName: String = jsonObject.getString("managerVersionName")
            val managerUrl: String = jsonObject.getString("managerUrl")
            val managerFilename: String = jsonObject.getString("managerFilename")
            return PluginInfo(
                uuid, type, name,
                managerVersionCode, managerVersionName,
                managerUrl, managerFilename,
            )
        }
    }

    fun toJsonObject(): JSONObject {
        val jsonObject = JSONObject()
        jsonObject["uuid"] = uuid
        jsonObject["type"] = type
        jsonObject["name"] = name

        jsonObject["managerVersionCode"] = managerVersionCode
        jsonObject["managerVersionName"] = managerVersionName
        jsonObject["managerUrl"] = managerUrl
        jsonObject["managerFilename"] = managerFilename
        return jsonObject
    }

    override fun toString(): String = toJsonObject().toJSONString()

    fun toLocalPlugin(): Plugin = Plugin(0, uuid, type, name, managerVersionCode, managerVersionName)
}

data class PartPlugin(
    val partKey: String,
    val activityList: List<String> = listOf(),
    val serviceList: List<String> = listOf(),
) : Serializable {
    companion object {
        fun fromJson(json: String) = fromJson(JSON.parseObject(json))
        fun fromJson(jsonObject: JSONObject): PartPlugin {
            val partKey: String = jsonObject.getString("partKey")
            val activityList = jsonObject.getStringList("activityList")
            val serviceList = jsonObject.getStringList("serviceList")
            return PartPlugin(partKey, activityList, serviceList)
        }

        fun JSONObject.getStringList(key: String): MutableList<String> {
            return getJSONArray(key)?.toListString() ?: mutableListOf()
        }

        fun JSONArray.toListString(): MutableList<String> {
            val list: MutableList<String> = mutableListOf()
            for (i in this) {
                list.add(i.toString())
            }
            return list
        }
    }

    fun toJsonObject(): JSONObject {
        val jsonObject = JSONObject()
        jsonObject["partKey"] = partKey
        jsonObject["activityList"] = activityList
        jsonObject["serviceList"] = serviceList
        return jsonObject
    }
}

fun JSONObject.getPartPluginList(key: String): MutableList<PartPlugin> {
    return getJSONArray(key)?.toPartPluginList() ?: mutableListOf()
}

fun JSONArray.toPartPluginList(): MutableList<PartPlugin> {
    val list: MutableList<PartPlugin> = mutableListOf()
    for (i in this) {
        list.add(PartPlugin.fromJson(i as JSONObject))
    }
    return list
}
