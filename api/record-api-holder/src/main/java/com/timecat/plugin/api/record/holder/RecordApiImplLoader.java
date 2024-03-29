/*
 * Tencent is pleased to support the open source community by making Tencent Shadow available.
 * Copyright (C) 2019 THL A29 Limited, a Tencent company.  All rights reserved.
 *
 * Licensed under the BSD 3-Clause License (the "License"); you may not use
 * this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 *     https://opensource.org/licenses/BSD-3-Clause
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.timecat.plugin.api.record.holder;

import android.content.Context;

import com.tencent.shadow.core.common.InstalledApk;
import com.tencent.shadow.dynamic.apk.ApkClassLoader;
import com.tencent.shadow.dynamic.apk.ChangeApkContextWrapper;
import com.tencent.shadow.dynamic.apk.ImplLoader;
import com.timecat.plugin.api.record.RecordApiFactory;
import com.timecat.plugin.api.record.RecordApiWrapper;

import java.io.File;

final class RecordApiImplLoader extends ImplLoader {
    //指定实现类在apk中的路径
    private static final String FACTORY_CLASS_NAME = "com.timecat.plugin.impl.record.RecordApiImpl";
    private static final String[] REMOTE_PLUGIN_MANAGER_INTERFACES = new String[]
            {
                    "com.tencent.shadow.core.common",
                    //注意将宿主自定义接口加入白名单
                    "com.timecat.plugin.api.record"
            };
    final private Context applicationContext;
    final private InstalledApk installedApk;

    RecordApiImplLoader(Context context, File apk) {
        applicationContext = context.getApplicationContext();
        File root = new File(applicationContext.getFilesDir(), "RecordApiImplLoader");
        File odexDir = new File(root, Long.toString(apk.lastModified(), Character.MAX_RADIX));
        odexDir.mkdirs();
        installedApk = new InstalledApk(apk.getAbsolutePath(), odexDir.getAbsolutePath(), null);
    }

    RecordApiWrapper load() {
        ApkClassLoader apkClassLoader = new ApkClassLoader(
                installedApk,
                getClass().getClassLoader(),
                loadWhiteList(installedApk),
                1
        );

        Context contextForApi = new ChangeApkContextWrapper(
                applicationContext,
                installedApk.apkFilePath,
                apkClassLoader
        );

        try {
            RecordApiFactory factory = apkClassLoader.getInterface(
                    RecordApiFactory.class,
                    FACTORY_CLASS_NAME
            );
            return factory.build(contextForApi);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected String[] getCustomWhiteList() {
        return REMOTE_PLUGIN_MANAGER_INTERFACES;
    }
}
