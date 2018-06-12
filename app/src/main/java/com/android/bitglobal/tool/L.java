package com.android.bitglobal.tool;

import com.android.bitglobal.BuildConfig;
import com.orhanobut.logger.LogLevel;
import com.orhanobut.logger.Logger;

/**
 * 日志类
 * Created by elbert on 2017/3/28.
 */

public class L {
    protected static final String TAG = "Bitglobal";

    static {
        Logger.init(TAG)                 // default PRETTYLOGGER or use just init()
                .methodCount(3)                 // default 2
                .hideThreadInfo()               // default shown
                .logLevel(LogLevel.FULL);        // default LogLevel.FULL
//                .methodOffset(2);                // default 0
    }

    public L() {
    }

    /**
     * Send a VERBOSE log message.
     *
     * @param msg The message you would like logged.
     */
    public static void v(String msg) {
        if (BuildConfig.DEBUG)
            Logger.v(msg);
    }

    /**
     * Send a DEBUG log message.
     *
     * @param msg The message you would like logged.
     */
    public static void d(String msg) {
        if (BuildConfig.DEBUG)
            Logger.d(msg);
    }

    /**
     * Send an INFO log message.
     *
     * @param msg The message you would like logged.
     */
    public static void i(String msg) {
        if (BuildConfig.DEBUG)
            Logger.i(msg);
    }

    /**
     * Send an ERROR log message.
     *
     * @param msg The message you would like logged.
     */
    public static void e(String msg) {
        if (BuildConfig.DEBUG)
            Logger.e(msg);
    }

    /**
     * Send a WARN log message
     *
     * @param msg The message you would like logged.
     */
    public static void w(String msg) {
        if (BuildConfig.DEBUG)
            Logger.w(msg);
    }

    private static String buildMessage(String msg) {

        //通过StackTraceElement获取当前打印日志的类名和方法名，这个用来代替我们平时手写的TAG值。
        // StackTrace用栈的形式保存了方法的调用信息
        StackTraceElement caller = new Throwable().fillInStackTrace().getStackTrace()[2];

        return caller.getClassName() + "." + caller.getMethodName() + "(): " + msg;
    }
}
