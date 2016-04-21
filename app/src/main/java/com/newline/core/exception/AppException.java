package com.newline.core.exception;

public class AppException extends RuntimeException {

    private static final long serialVersionUID = -2948146462511823734L;
    
    private String errorMsg;
    
    public AppException(String errorMsg){
        super(errorMsg);
        this.errorMsg = errorMsg;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

}
