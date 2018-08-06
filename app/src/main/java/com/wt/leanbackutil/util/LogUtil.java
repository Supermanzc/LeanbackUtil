package com.wt.leanbackutil.util;

/**
 * Author: junyan
 * 日志打印
 */
public class LogUtil {

    private static final boolean ISDEBUG = true;
    private static final String TAG = "YUE_LOG";

    private static String getFunctionName() {
        StackTraceElement[] sts = Thread.currentThread().getStackTrace();
        if (sts == null) {
            return null;
        }
        for (StackTraceElement st : sts) {
            if (st.isNativeMethod()) {
                continue;
            }
            if (st.getClassName().equals(Thread.class.getName())) {
                continue;
            }
            if (st.getClassName().equals(LogUtil.class.getName())) {
                continue;
            }

            return "[" + Thread.currentThread().getName() + "(" + Thread.currentThread().getId()
                    + "): " + st.getFileName() + ":" + st.getLineNumber() + "]";
        }
        return null;
    }

    private static String createMessage(String msg) {
        String functionName = getFunctionName();
        String message = (functionName == null ? msg : (functionName + " - " + msg));
        return message;
    }

    public static void v(String msg) {
        if (ISDEBUG) {
            String message = createMessage(msg);
            android.util.Log.v(TAG, message);
        }
    }

    public static void v(String msg, Throwable t) {
        if (ISDEBUG) {
            String message = createMessage(msg);
            android.util.Log.v(TAG, message, t);
        }
    }

    public static void d(String msg) {
        if (ISDEBUG) {
            String message = createMessage(msg);
            android.util.Log.d(TAG, message);
        }
    }

    public static void d(String msg, Throwable t) {
        if (ISDEBUG) {
            String message = createMessage(msg);
            android.util.Log.d(TAG, message, t);
        }
    }

    public static void i(String msg) {
        if (ISDEBUG) {
            String message = createMessage(msg);
            android.util.Log.i(TAG, message);
        }
    }

    public static void i(String msg, Throwable t) {
        if (ISDEBUG) {
            String message = createMessage(msg);
            android.util.Log.i(TAG, message, t);
        }
    }

    public static void w(String msg) {
        if (ISDEBUG) {
            String message = createMessage(msg);
            android.util.Log.w(TAG, message);
        }
    }

    public static void w(String msg, Throwable t) {
        if (ISDEBUG) {
            String message = createMessage(msg);
            android.util.Log.w(TAG, message, t);
        }
    }

    public static void e(String msg) {
        if (ISDEBUG) {
            String message = createMessage(msg);
            android.util.Log.e(TAG, message);
        }
    }

    public static void e(String msg, Throwable t) {
        if (ISDEBUG) {
            String message = createMessage(msg);
            android.util.Log.e(TAG, message, t);
        }
    }
}