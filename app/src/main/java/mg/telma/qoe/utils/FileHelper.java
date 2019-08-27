package mg.telma.qoe.utils;

import android.content.Context;
import android.net.Uri;
import android.text.format.DateFormat;

import androidx.annotation.NonNull;
import androidx.documentfile.provider.DocumentFile;

import java.io.File;
import java.util.Date;

public class FileHelper {
    public static DocumentFile getFile(Context context, @NonNull String phoneNumber) throws Exception {
        String date = (String) DateFormat.format("yyyyMMddHHmmss", new Date());
        String filename = date + "_" + cleanNumber(phoneNumber);

        return getStorageFile(context).createFile("audio/3gpp", filename);
    }

    private static String cleanNumber(String phoneNumber) {
        return phoneNumber.replaceAll("[^0-9]", "");
    }

    private static DocumentFile getStorageFile(Context context) {
        Uri uri = Uri.fromFile(context.getFilesDir());
        String scheme = uri.getScheme();
        if (scheme == null || scheme.equals("file")) {
            return DocumentFile.fromFile(new File(uri.getPath()));
        } else {
            return DocumentFile.fromTreeUri(context, uri);
        }
    }

    public static boolean isStorageWritable(Context context) {
        return getStorageFile(context).canWrite();
    }
}
