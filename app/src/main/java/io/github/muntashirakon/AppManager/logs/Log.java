package io.github.muntashirakon.AppManager.logs;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.Locale;

/**
 * A wrapper for {@link android.util.Log} that supports printf formatting.
 */
public class Log {

    public static void v(@NonNull String tag, @NonNull String msg) {
        android.util.Log.v(tag, msg);
    }

    public static void v(@NonNull String tag, @NonNull String format, @Nullable Object... args) {
        android.util.Log.v(tag, format(format, args));
    }

    public static void v(@NonNull String tag, @NonNull String msg, @Nullable Throwable tr) {
        android.util.Log.v(tag, msg, tr);
    }

    public static void d(@NonNull String tag, @NonNull String msg) {
        android.util.Log.d(tag, msg);
    }

    public static void d(@NonNull String tag, @NonNull String format, @Nullable Object... args) {
        android.util.Log.d(tag, format(format, args));
    }

    public static void d(@NonNull String tag, @NonNull String msg, @Nullable Throwable tr) {
        android.util.Log.d(tag, msg, tr);
    }

    public static void i(@NonNull String tag, @NonNull String msg) {
        android.util.Log.i(tag, msg);
    }

    public static void i(@NonNull String tag, @NonNull String format, @Nullable Object... args) {
        android.util.Log.i(tag, format(format, args));
    }

    public static void i(@NonNull String tag, @NonNull String msg, @Nullable Throwable tr) {
        android.util.Log.i(tag, msg, tr);
    }

    public static void w(@NonNull String tag, @NonNull String msg) {
        android.util.Log.w(tag, msg);
    }

    public static void w(@NonNull String tag, @NonNull String format, @Nullable Object... args) {
        android.util.Log.w(tag, format(format, args));
    }

    public static void w(@NonNull String tag, @NonNull String msg, @Nullable Throwable tr) {
        android.util.Log.w(tag, msg, tr);
    }

    public static void w(@NonNull String tag, @Nullable Throwable tr) {
        android.util.Log.w(tag, tr);
    }

    public static void e(@NonNull String tag, @NonNull String msg) {
        android.util.Log.e(tag, msg);
    }

    public static void e(@NonNull String tag, @NonNull String format, @Nullable Object... args) {
        android.util.Log.e(tag, format(format, args));
    }

    public static void e(@NonNull String tag, @NonNull String msg, @Nullable Throwable tr) {
        android.util.Log.e(tag, msg, tr);
    }

    public static void e(@NonNull String tag, @Nullable Throwable tr) {
        android.util.Log.e(tag, "An error occurred", tr);
    }

    private static String format(String format, Object... args) {
        try {
            return (args == null || args.length == 0) ? format : String.format(Locale.US, format, args);
        } catch (Exception e) {
            return format + " [Formatting Failed: " + e.getMessage() + "]";
        }
    }
}
