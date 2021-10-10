# TimeCatModule-Plugin
时光猫组件 插件宿主

业务组件应该只负责插件的下载、升级、安装
提供 api 使用插件、插件市场页、插件管理页、插件详情页
不提供插件新增页
插件新增页是官方定制插件中的页面，以便动态更新
插件新增页是有特殊需求的用户（开发者、设计师，统称为造物主）才使用的页面，一般用户用不到，所以放到定制插件里

定制插件：指官方专门为某一部分业务推出的插件

## 插件升级

必须保证最大兼容性，所以插件接口稳定，不用升级也能用

在市场页、管理页、详情页，在使用插件时必须升级
在其他页，仅使用插件，不必升级

总结：有提示升级的地方，必须升级。没提示升级的地方，保证正常使用。

Caused by: java.lang.ClassNotFoundException: Didn't find class "com.tencent.shadow.dynamic.loader.impl.LoaderFactoryImpl" on path: DexPathList[[zip file "/data/user/0/com.timecat.plugin.fake/files/ShadowPluginManager/UnpackedPlugin/test-dynamic-manager/c86ad64370f56ea66c038772a8114d5d/plugin-release.zip/plugin-runtime-release.apk"],nativeLibraryDirectories=[/data/user/0/com.timecat.plugin.fake/files/ShadowPluginManager/UnpackedPlugin/test-dynamic-manager/lib/B0F44E88-D998-48B9-A625-270883DF5BC7_lib, /system/lib64, /system/product/lib64]]