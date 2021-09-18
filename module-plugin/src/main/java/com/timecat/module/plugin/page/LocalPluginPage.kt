package com.timecat.module.plugin.page

import android.content.Context
import android.os.Environment
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.afollestad.materialdialogs.files.fileChooser
import com.afollestad.materialdialogs.list.listItemsMultiChoice
import com.blankj.utilcode.util.FileUtils
import com.timecat.element.alert.ToastUtil
import com.timecat.layout.ui.business.form.*
import com.timecat.layout.ui.business.setting.NextItem
import com.timecat.middle.block.ext.form
import com.timecat.middle.block.ext.showDialog
import com.timecat.middle.block.page.BaseSelectorPage
import com.timecat.module.plugin.R
import com.timecat.module.plugin.database.Plugin
import com.timecat.module.plugin.database.PluginDatabase
import com.timecat.module.plugin.database.TYPE_PluginEnter
import com.timecat.module.plugin.database.TYPE_RecordApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.util.*

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/9/18
 * @description null
 * @usage null
 */
class LocalPluginPage : BaseSelectorPage() {
    override fun title(): String = "从本地安装"

    data class FormData(
        var file: File = Environment.getDataDirectory(),
        var uuid: String = UUID.randomUUID().toString(),
        var type: Long = TYPE_PluginEnter,
        var title: String = "",

        var managerVersionCode: Int = 0,
        var managerVersionName: String = "1.0.0",
    ) {
        fun from(record: Plugin) {
            title = record.title
        }
    }

    val formData: FormData = FormData()

    override fun addSettingItems(context: Context, container: ViewGroup) {
        container.apply {
            val titleItem = OneLineInput("插件名", formData.title) {
                formData.title = it ?: ""
            }
            OneLineInput("唯一标示码", formData.uuid) {
                formData.uuid = it ?: UUID.randomUUID().toString()
            }
            NumberInput("版本号", "${formData.managerVersionCode}").also {
                it.onTextChange = {
                    formData.managerVersionCode = (it ?: "0").toIntOrNull() ?: 0
                }
            }
            OneLineInput("版本名", formData.managerVersionName) {
                formData.managerVersionName = it ?: "1.0.0"
            }
            val typeNext = Next("该插件已实现的接口") {
                selectType(it)
            }

            Divider()
            val pluginNext = Next("选择插件文件") {
                selectPluginFile(it)
            }
            Divider()

            context.form(container) {
                useRealTimeValidation(disableSubmit = true)

                inputLayout(titleItem.inputLayout) {
                    isNotEmpty().description("请输入名称!")
                }

                next(typeNext, "必须选择该插件已实现的接口", false) {
                    isNotEmpty().description("请选择该插件已实现的接口!")
                }
                next(pluginNext, "必须选择一个插件文件", false) {
                    isNotEmpty().description("请选择一个插件文件!")
                }

                submitWith(btnOk) { result ->
                    ok()
                }
            }
        }
    }

    private fun selectType(nextItem: NextItem) {
        context.showDialog {
            val items = listOf(
                "主页面接口",
                "符文系统接口",
            )
            listItemsMultiChoice(items = items, initialSelection = intArrayOf(0)) { d, idxs, selectedItems ->
                var typeState = 0L
                if (0 in idxs) typeState = typeState or TYPE_PluginEnter
                if (1 in idxs) typeState = typeState or TYPE_RecordApi
                formData.type = typeState
                nextItem.hint = selectedItems.joinToString(", ")
            }
            positiveButton(R.string.ok)
            negativeButton(R.string.cancel)
        }
    }

    private fun selectPluginFile(nextItem: NextItem) {
        context.showDialog {
            fileChooser(context, filter = {
                it.isDirectory || it.endsWith(".apk")
            }) { _, file ->
                formData.file = file
                nextItem.hint = file.name
            }
            positiveButton(R.string.ok)
            negativeButton(R.string.cancel)
        }
    }

    override fun ok() {
        lifecycleScope.launch(Dispatchers.IO) {
            save()
            withContext(Dispatchers.Main) {
                ToastUtil.ok("创建成功")
                finishFragment(true)
            }
        }
    }

    open suspend fun save() {
        //TODO 将插件apk复制到目标文件夹下（由 schema 决定），再保存元数据到数据库
        val plugin = Plugin(
            0, UUID.randomUUID().toString(),
            formData.type, formData.title,
            formData.managerVersionCode, formData.managerVersionName
        )
        context?.let { context ->
            val targetApkFile = plugin.managerApkFile(context)
            FileUtils.copy(formData.file, targetApkFile)
            val db = PluginDatabase.forFile(context, PluginDatabase.NAME)
            db.pluginDao().insert(plugin)
        }
    }
}