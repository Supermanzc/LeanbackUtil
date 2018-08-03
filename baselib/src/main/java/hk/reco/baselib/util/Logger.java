package hk.reco.baselib.util;

import android.util.Log;

import hk.reco.baselib.BuildConfig;
import hk.reco.baselib.Config;


/**
 * 封装原有的日志信息，通过配置控制不同类型的日志输出。
 */
public final class Logger {

    private static final boolean IS_VERBOSE = Config.OPEN_LOGGER;
    private static final boolean IS_DEBUG = Config.OPEN_LOGGER;
    private static final boolean IS_INFO = Config.OPEN_LOGGER;
    private static final boolean IS_WARN = Config.OPEN_LOGGER;
    private static final boolean IS_ERROR = Config.OPEN_LOGGER;
    private static final String PREFIX = ">>>>>";

    public static void v(String tag, String msg) {
        if (IS_VERBOSE) {
            Log.v(tag, PREFIX + msg);
        }
    }

    public static void d(String tag, String msg) {
        if (IS_DEBUG) {
            Log.d(tag, PREFIX + msg);
        }
    }

    public static void i(String tag, String msg) {
        if (IS_INFO) {
            Log.i(tag, PREFIX + msg);
        }
    }

    public static void w(String tag, String msg) {
        if (IS_WARN) {
            Log.w(tag, PREFIX + msg);
        }
    }

    public static void e(String tag, String msg) {
        if (IS_ERROR) {
            Log.e(tag, PREFIX + msg);
        }
    }

    /**
     * Print log but print 120 characters at most in every line.
     *
     * @param tag
     * @param msg
     */
    public static void printLog(String tag, String msg) {
        if (IS_DEBUG && msg != null) {
            int count = msg.length();
            int lineCount = 120;
            for (int i = 0; i < count; i = i + lineCount) {
                if (i + lineCount <= count) {
                    Log.d(tag, msg.substring(i, i + lineCount));
                } else {
                    Log.d(tag, msg.substring(i, count));
                }
            }
        }
    }


    /**
     * Print log but print 4000 characters at most in every line.
     *
     * @param tag
     * @param msg
     */
    public static void printLog2(String tag, String msg) {
        if (IS_DEBUG && msg != null) {
            int count = msg.length();
            int lineCount = 3000;
            for (int i = 0; i < count; i = i + lineCount) {
                if (i + lineCount <= count) {
                    Log.d(tag, msg.substring(i, i + lineCount));
                } else {
                    Log.d(tag, msg.substring(i, count));
                }
            }
        }
    }

}
