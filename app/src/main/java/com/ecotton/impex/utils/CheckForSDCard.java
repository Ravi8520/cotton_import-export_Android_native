package com.ecotton.impex.utils;

import android.os.Environment;

public class CheckForSDCard {
    public boolean isSDCardPresent() {
        return Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).equals(
                Environment.MEDIA_MOUNTED);
    }
}
