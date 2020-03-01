package com.mdgd.academy2020.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Process;

import androidx.core.app.ActivityCompat;

public class PermissionsUtil {

    public interface PermissionCommand {

        void exec(final String permission);
    }

    /**
     * return true if has permission, false other way
     */
    @TargetApi(16)
    public static boolean checkPermissions(final Context ctx, final String... permissions) {
        return checkPermissionAndExec(ctx, null, permissions);
    }

    /**
     * return true if has all permission, false other way and requests non-granted permissions
     */
    @TargetApi(16)
    public static boolean requestPermissionsIfNeed(final Activity ctx, final int requestCode, final String... permissions) {
        return checkPermissionAndExec(ctx, permission -> ActivityCompat.requestPermissions(ctx, new String[]{permission}, requestCode), permissions);
    }

    @TargetApi(16)
    private static boolean checkPermissionAndExec(final Context ctx, final PermissionCommand cmd, final String... permissions) {
        if (ctx == null || permissions == null) {
            return false;
        }
        boolean result = true;
        for (String p : permissions) {
            if (ctx.checkPermission(p, Process.myPid(), Process.myUid()) != PackageManager.PERMISSION_GRANTED) {
                result = false;
                if (cmd != null) {
                    cmd.exec(p);
                }
            }
        }
        return result;
    }

    public static boolean areAllPermissionsGranted(int[] grantResults) {
        for (int i : grantResults) {
            if (i != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return grantResults.length != 0; // if array is empty - return false
    }
}
