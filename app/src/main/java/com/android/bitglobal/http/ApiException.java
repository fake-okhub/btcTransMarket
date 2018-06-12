package com.android.bitglobal.http;

/**
 * xiezuofei
 * 2016-08-02 13:20
 * 793169940@qq.com
 */
public class ApiException extends RuntimeException {

    public static final int USER_NOT_EXIST = 100;
    public static final int WRONG_PASSWORD = 101;

    public ApiException(int resultCode) {
        this(getApiExceptionMessage(resultCode));
    }

    public ApiException(String detailMessage) {
        super(detailMessage);
    }

    /**
     * 由于服务器传递过来的错误信息直接给用户看的话，用户未必能够理解
     * 需要根据错误码对错误信息进行一个转换，在显示给用户
     *
     * @param code
     * @return
     */
    private static String getApiExceptionMessage(int code) {
        String message = "";
        switch (code) {
            case USER_NOT_EXIST:
                message = "User not exist";
                break;
            case WRONG_PASSWORD:
                message = "Wrong username or password";
                break;
            default:
                message = "Unknown error";

        }
        return message;
    }
}

