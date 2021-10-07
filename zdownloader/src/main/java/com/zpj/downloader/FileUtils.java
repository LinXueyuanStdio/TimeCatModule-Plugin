package com.zpj.downloader;

import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/10/7
 * @description null
 * @usage null
 */
public class FileUtils {

    public enum FileType {
        VIDEO,
        MUSIC,
        IMAGE,
        EBOOK,
        ARCHIVE,
        HTML,
        TXT,
        APK,
        TORRENT,
        PDF,
        PPT,
        DOC,
        SWF,
        CHM,
        XLS,
        UNKNOWN
    }

    private static final String[] IMG = {".bmp", ".jpg", ".jpeg", ".png", ".tiff", ".gif", ".pcx", ".tga", ".exif", ".fpx", ".svg", ".psd",
                                         ".cdr", ".pcd", ".dxf", ".ufo", ".eps", ".ai", ".raw", ".wmf"};
    private static final String[] VIDEO = {".mp4", ".avi", ".mov", ".wmv", ".asf", ".navi", ".3gp", ".mkv", ".f4v", ".rmvb", ".webm", ".flv", ".rm", ".ts", ".vob", ".m2ts"};
    private static final String[] MUSIC = {".mp3", ".wma", ".wav", ".mod", ".ra", ".cd", ".md", ".asf", ".aac", ".vqf", ".ape", ".mid", ".ogg", ".m4a", ".vqf", ".flac", ".midi"};
    private static final String[] ARCHIVE = {".zip", ".rar", ".7z", ".iso", ".tar", ".gz", ".bz"};
    private static final String[] EBOOK = {".epub", ".umb", ".wmlc", ".pdb", ".mdx", ".xps"};

    public static FileType getFileType(String fileName) {
        fileName = fileName.toLowerCase();
        if (fileName.endsWith(".torrent")) {
            return FileType.TORRENT;
        } else if (fileName.endsWith(".txt")) {
            return FileType.TXT;
        } else if (fileName.endsWith(".apk")) {
            return FileType.APK;
        } else if (fileName.endsWith(".pdf")) {
            return FileType.PDF;
        } else if (fileName.endsWith(".doc") || fileName.endsWith(".docx")) {
            return FileType.DOC;
        } else if (fileName.endsWith(".ppt") || fileName.endsWith(".pptx")) {
            return FileType.PPT;
        } else if (fileName.endsWith(".xls") || fileName.endsWith(".xlsx")) {
            return FileType.XLS;
        } else if (fileName.endsWith(".htm") || fileName.endsWith(".html")) {
            return FileType.HTML;
        } else if (fileName.endsWith(".swf")) {
            return FileType.SWF;
        } else if (fileName.endsWith(".chm")) {
            return FileType.CHM;
        }

        for (String s : IMG) {
            if (fileName.endsWith(s)) {
                return FileType.IMAGE;
            }
        }
        for (String s : VIDEO) {
            if (fileName.endsWith(s)) {
                return FileType.VIDEO;
            }
        }
        for (String s : ARCHIVE) {
            if (fileName.endsWith(s)) {
                return FileType.ARCHIVE;
            }
        }
        for (String s : MUSIC) {
            if (fileName.endsWith(s)) {
                return FileType.MUSIC;
            }
        }
        for (String s : EBOOK) {
            if (fileName.endsWith(s)) {
                return FileType.EBOOK;
            }
        }
        return FileType.UNKNOWN;
    }

    /**
     * 根据文件后缀名获得对应的MIME类型。
     *
     * @param file the file
     */
    public static String getMIMEType(File file) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(file.getAbsolutePath());
        if (!TextUtils.isEmpty(extension)) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.toLowerCase());
        }
        return type;
    }

    public static void closeIO(Closeable... closeables) {
        if (null == closeables || closeables.length <= 0) {
            return;
        }
        for (Closeable cb : closeables) {
            try {
                if (null == cb) {
                    continue;
                }
                cb.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public static long getAvailableSize() {
        String sdcard = Environment.getExternalStorageState();
        String state = Environment.MEDIA_MOUNTED;
        File file = Environment.getExternalStorageDirectory();
        StatFs statFs = new StatFs(file.getPath());
        if(sdcard.equals(state)) {
            //获得Sdcard上每个block的size
            long blockSize = statFs.getBlockSizeLong();
            //获取可供程序使用的Block数量
            long blockavailable = statFs.getAvailableBlocksLong();
            //计算标准大小使用：1024，当然使用1000也可以
            return blockSize * blockavailable;
        } else {
            return -1;
        }
    }
}
