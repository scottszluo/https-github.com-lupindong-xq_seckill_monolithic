package net.lovexq.seckill.common.exception;

import net.lovexq.seckill.common.utils.constants.AppConstants;

/**
 * 系统级别异常.
 */
public class SystemException extends GeneralException {
    private static final long serialVersionUID = -1491071032089115497L;

    public SystemException(Long errorCode) {
        super(errorCode);
    }

    public SystemException(String message) {
        super(AppConstants.DEFAULT_SYS_ERROR, message);
    }

    public SystemException(String message, Throwable cause) {
        super(AppConstants.DEFAULT_SYS_ERROR, message, cause);
    }

    public SystemException(Long errorCode, String message) {
        super(errorCode, message);
    }

    public SystemException(Long errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    public SystemException(Long errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }

}