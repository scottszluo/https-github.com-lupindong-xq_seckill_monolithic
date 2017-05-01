package net.lovexq.seckill.common.exception;

import net.lovexq.seckill.common.utils.constants.AppConstants;

/**
 * 异常基类
 *
 * @author LuPindong
 * @time 2017-04-23 00:56
 */
class GeneralException extends RuntimeException {

    private static final long serialVersionUID = -1798591545534800076L;

    private Long errorCode = AppConstants.UNKNOWN_ERROR;

    protected GeneralException(Long errorCode) {
        super();
        this.errorCode = errorCode;
    }

    protected GeneralException(Long errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    protected GeneralException(Long errorCode, Throwable cause) {
        super(cause);
        this.errorCode = errorCode;
    }

    protected GeneralException(Long errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    /**
     * 获取最底层的异常
     *
     * @param cause 异常
     * @return 最底层的异常
     */
    private static Throwable getFloorThrowable(Throwable cause) {
        Throwable next = cause.getCause();
        if (next == null) {
            return cause;
        } else {
            return getFloorThrowable(next);
        }
    }

    public Long getErrorCode() {
        return errorCode;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (getMessage() != null) {
            sb.append(getMessage());
        }
        sb.append(getErrorCode());
        return sb.toString();
    }
}
