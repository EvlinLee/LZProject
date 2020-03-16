/*
 * Copyright (C) 2006 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.by.lizhiyoupin.app.common.utils.storage.sd;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Pattern;
/**
 * Tools for managing files.  Not for public consumption.
 * @hide
 */
public class FileUtils {
    private static final String TAG = "FileUtils";
    public static final int S_IRWXU = 00700;
    public static final int S_IRUSR = 00400;
    public static final int S_IWUSR = 00200;
    public static final int S_IXUSR = 00100;
    public static final int S_IRWXG = 00070;
    public static final int S_IRGRP = 00040;
    public static final int S_IWGRP = 00020;
    public static final int S_IXGRP = 00010;
    public static final int S_IRWXO = 00007;
    public static final int S_IROTH = 00004;
    public static final int S_IWOTH = 00002;
    public static final int S_IXOTH = 00001;
    /** Regular expression for safe filenames: no spaces or metacharacters.
      *
      * Use a preload holder so that FileUtils can be compile-time initialized.
      */
    private static class NoImagePreloadHolder {
        public static final Pattern SAFE_FILENAME_PATTERN = Pattern.compile("[\\w%+,./=_-]+");
    }
    private static final File[] EMPTY = new File[0];
    /**
     * Set owner and mode of of given path.
     *
     * @param mode to apply through {@code chmod}
     * @param uid to apply through {@code chown}, or -1 to leave unchanged
     * @param gid to apply through {@code chown}, or -1 to leave unchanged
     * @return 0 on success, otherwise errno.
     */
//    public static int setPermissions(String path, int mode, int uid, int gid) {
//        try {
//            Os.chmod(path, mode);
//        } catch (ErrnoException e) {
//            Log.w(TAG, "Failed to chmod(" + path + "): " + e);
//            return e.errno;
//        }
//        if (uid >= 0 || gid >= 0) {
//            try {
//                Os.chown(path, uid, gid);
//            } catch (ErrnoException e) {
//                Log.w(TAG, "Failed to chown(" + path + "): " + e);
//                return e.errno;
//            }
//        }
//        return 0;
//    }
    /**
     * Perform an fsync on the given FileOutputStream.  The stream at this
     * point must be flushed but not yet closed.
     */
    public static boolean sync(FileOutputStream stream) {
        try {
            if (stream != null) {
                stream.getFD().sync();
            }
            return true;
        } catch (IOException e) {
        }
        return false;
    }
}