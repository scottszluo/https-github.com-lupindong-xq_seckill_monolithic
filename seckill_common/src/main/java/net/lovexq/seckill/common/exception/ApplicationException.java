package net.lovexq.seckill.common.exception;

import net.lovexq.seckill.common.utils.constants.AppConstants;

/**
 * 应用程序级别异常
 * 
 * @author MAJUNTAO.20110620
 * 
 */
public class ApplicationException extends GeneralException {
	private static final long serialVersionUID = -426775244083897672L;

	public ApplicationException(Long errorCode) {
		super(errorCode);
	}
	
	public ApplicationException(String message) {
		super(AppConstants.DEFAULT_APP_ERROR, message);
	}

	public ApplicationException(String message, Throwable cause) {
		super(AppConstants.DEFAULT_APP_ERROR, message, cause);
	}

	public ApplicationException(Long errorCode, String message) {
		super(errorCode, message);
	}

	public ApplicationException(Long errorCode, Throwable cause) {
		super(errorCode, cause);
	}

	public ApplicationException(Long errorCode, String message, Throwable cause) {
		super(errorCode, message, cause);
	}

}