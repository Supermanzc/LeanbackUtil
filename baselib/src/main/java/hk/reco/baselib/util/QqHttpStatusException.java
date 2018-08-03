package hk.reco.baselib.util;

/**
 * 创 建 人： 程超
 * 创建日期：2018/5/3 13:06
 * 修改时间：
 * 修改备注：
 */

public class QqHttpStatusException extends Exception {
    private int httpStatusCode;

    public QqHttpStatusException(int httpStatusCode, String message) {
        super(message);
        this.httpStatusCode = httpStatusCode;
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }
}
