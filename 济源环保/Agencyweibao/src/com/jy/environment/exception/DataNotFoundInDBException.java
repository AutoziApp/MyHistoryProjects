package com.jy.environment.exception;
/**
 * 当没有在数据库中找到数据时的异常
 * @author 
 */
public class DataNotFoundInDBException extends Exception
{
	private static final long	serialVersionUID	= -6823195538172828156L;
	
    public DataNotFoundInDBException() {
        super();
    }

    public DataNotFoundInDBException(String pDetailMessage) {
        super(pDetailMessage);
    }
}
