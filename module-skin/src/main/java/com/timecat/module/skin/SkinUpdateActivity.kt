package com.timecat.module.skin

import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.timecat.data.bmob.ext.bmob.requestBlock
import com.timecat.data.bmob.ext.net.allSkin
import com.timecat.identity.readonly.RouterHub
import com.timecat.layout.ui.entity.BaseAdapter
import com.timecat.module.skin.adapter.CloudSkinItem
import com.timecat.module.skin.adapter.RestoreDefaultSkinItem
import com.timecat.module.skin.database.Skin
import com.timecat.module.skin.database.SkinDatabase
import com.timecat.page.base.friend.toolbar.BaseListActivity
import com.xiaojinzi.component.anno.RouterAnno
import com.zpj.downloader.ZDownloader
import io.reactivex.disposables.Disposable
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-09-07
 * @description 插件更新管理，所有插件的更新，用户手动更新
 * @usage
 */
@RouterAnno(hostAndPath = RouterHub.SKIN_SkinUpdateActivity)
class SkinUpdateActivity : BaseListActivity() {
    val adapter = BaseAdapter(null)

    override fun title(): String = "插件更新"
    override fun getAdapter(): RecyclerView.Adapter<*> = adapter

    var dispose: Disposable? = null
    override fun loadData() {
        mStatefulLayout?.showLoading("检查更新中")
        lifecycleScope.launch(Dispatchers.IO) {
            val allSkin = SkinDatabase.forFile(context).skinDao().getAll()
            loadCloud(allSkin)
        }
    }

    private fun loadCloud(allSkin: List<Skin>) {
        dispose?.dispose()
        dispose = requestBlock {
            query = allSkin().apply {
                whereContainedIn("objectId", allSkin.map { it.uuid })
            }
            onSuccess = { blocks ->
                lifecycleScope.launch(Dispatchers.IO) {
                    val missions = ZDownloader.getAllMissions()
                    val uuids = blocks.map { it.objectId }
                    val items = blocks.map { block ->
                        val localSkin = allSkin.find { it.uuid in uuids }
                        val mission = missions.find { localSkin?.apkFile(this@SkinUpdateActivity)?.parent == it.downloadPath }
                        CloudSkinItem(context, block, mission, localSkin)
                    }
                    val restoreDefaultSkin = RestoreDefaultSkinItem(context)
                    withContext(Dispatchers.Main) {
                        mStatefulLayout?.showContent()
                        adapter.reload(listOf(restoreDefaultSkin) + items)
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        dispose?.dispose()
    }
}