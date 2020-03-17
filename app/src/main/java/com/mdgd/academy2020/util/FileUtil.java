package com.mdgd.academy2020.util;

import android.content.Context;
import android.net.Uri;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FileUtil {

    public static void copy(Context ctx, String from, String into) throws IOException {
        try (final InputStream input = new BufferedInputStream(new FileInputStream(new File(Uri.parse(from).getPath())));
             final OutputStream output = resolveStream(ctx, into)) {
            copy(input, output);
        } catch (Throwable e) {
            throw e;
        }
    }

    public static void copy(InputStream from, OutputStream into) throws IOException {
        final byte[] data = new byte[1024];
        int count;
        while ((count = from.read(data)) != -1) {
            into.write(data, 0, count);
        }
        into.flush();
    }

    private static OutputStream resolveStream(Context ctx, String into) throws IOException {
        final int idx = into.lastIndexOf(File.separator);
        if (ctx.getFilesDir().getPath().equals(into.substring(0, idx))) {
            return ctx.openFileOutput(into.substring(idx + 1), Context.MODE_PRIVATE);
        } else {
            final File file = new File(into);
            if (!file.exists()) {
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            return new BufferedOutputStream(new FileOutputStream(file));
        }
    }
}
