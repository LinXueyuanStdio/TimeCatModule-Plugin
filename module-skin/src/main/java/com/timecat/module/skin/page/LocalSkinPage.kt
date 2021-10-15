package com.timecat.module.skin.page

import android.content.Context
import android.os.Environment
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.afollestad.materialdialogs.files.fileChooser
import com.blankj.utilcode.util.FileUtils
import com.timecat.element.alert.ToastUtil
import com.timecat.layout.ui.business.form.*
import com.timecat.layout.ui.business.setting.NextItem
import com.timecat.middle.block.ext.form
import com.timecat.middle.block.ext.showDialog
import com.timecat.middle.block.page.BaseSelectorPage
import com.timecat.module.skin.R
import com.timecat.module.skin.database.Skin
import com.timecat.module.skin.database.SkinDatabase
import com.timecat.module.skin.database.TYPE_None
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
class LocalSkinPage : BaseSelectorPage() {
    override fun title(): String = "从本地安装"

    data class FormData(
        var file: File = Environment.getDataDirectory(),
        var packageName: String = "",
        var type: Long = TYPE_None,
        var title: String = "",

        var managerVersionCode: Int = 0,
        var managerVersionName: String = "1.0.0",
    ) {
        fun from(record: Skin) {
            title = record.title
        }
    }

    val formData: FormData = FormData()

    override fun addSettingItems(context: Context, container: ViewGroup) {
        container.apply {
            val titleItem = OneLineInput("皮肤名", formData.title) {
                formData.title = it ?: ""
            }
            OneLineInput("唯一标示码", formData.packageName) {
                formData.packageName = it ?: ""
            }
            NumberInput("版本号", "${formData.managerVersionCode}").also {
                it.onTextChange = {
                    formData.managerVersionCode = (it ?: "0").toIntOrNull() ?: 0
                }
            }
            OneLineInput("版本名", formData.managerVersionName) {
                formData.managerVersionName = it ?: "1.0.0"
            }

            Divider()
            val skinNext = Next("选择皮肤文件") {
                selectSkinFile(it)
            }
            Divider()

            context.form(container) {
                useRealTimeValidation(disableSubmit = true)

                inputLayout(titleItem.inputLayout) {
                    isNotEmpty().description("请输入名称!")
                }

                next(skinNext, "必须选择一个皮肤文件", false) {
                    isNotEmpty().description("请选择一个皮肤文件!")
                }

                submitWith(btnOk) { result ->
                    ok()
                }
            }
        }
    }

    private fun selectSkinFile(nextItem: NextItem) {
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
        context?.let { context ->
            // 将插件apk复制到目标文件夹下（由 schema 决定），再保存元数据到数据库
            val skin = Skin(
                0, UUID.randomUUID().toString(), formData.packageName,
                formData.type, formData.title,
                formData.managerVersionCode, formData.managerVersionName
            )
            val targetApkFile = skin.apkFile(context)
            FileUtils.copy(formData.file, targetApkFile)
            val db = SkinDatabase.forFile(context, SkinDatabase.NAME)
            db.skinDao().insert(skin)
        }
    }
}