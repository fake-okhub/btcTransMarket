package com.android.bitglobal.entity;

/**
 * Created by bitbank on 16/10/8.
 */
public class FileactionResult {
    private String code;
    private String message;
    private String fileName;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    @Override
    public String toString() {
        return "code=" + code
                + " message=" + message
                + " fileName=" + fileName
                + " | ";
    }
}
