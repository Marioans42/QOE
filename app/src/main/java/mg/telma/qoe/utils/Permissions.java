package mg.telma.qoe.utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public class Permissions {

    public static void permitted(Activity a, String[] ss, int code) {
        for (String s : ss) {
            if (ContextCompat.checkSelfPermission(a, s) != PackageManager.PERMISSION_GRANTED) {
                try {
                    ActivityCompat.requestPermissions(a, ss, code);
                } catch (ActivityNotFoundException e) {
                }
            }
        }
    }

    public static boolean permitted(Fragment f, String[] ss, int code) {

        if (Build.VERSION.SDK_INT < 16)
            return true;
        for (String s : ss) {
            if (ContextCompat.checkSelfPermission(f.getContext(), s) != PackageManager.PERMISSION_GRANTED) {
                try {
                    f.requestPermissions(ss, code);
                } catch (ActivityNotFoundException e) {
                    return true;
                }
                return false;
            }
        }
        return true;
    }
}
