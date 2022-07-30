package com.cotton.importexport.utils;

import android.os.Environment;

public class CheckForSDCard {
    public boolean isSDCardPresent() {
        if (Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        }
        return false;
    }
}
