package com.chenm.user_manager.common.model;


/**
 * 公共返回类
 * @author admin
 *
 * @param <T>
 */
public class ResultModel<T> {
	
	private static final String SUCCESS_CODE="0";//成功
	private static final String ERROR_CODE="-1";//失败
	
	
	
	public ResultModel() {
		super();
	}

	public ResultModel(T data) {
		super();
		this.data = data;
	}

	private String code;
	private String message;
	private Boolean success;
	private T data;
	
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

	public Boolean getSuccess() {
		return success;
	}

	public void setSuccess(Boolean success) {
		this.success = success;
	}

	
	 public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	//成功静态方法
    public static <T> ResultModel<T> ok(T data) {
    	ResultModel r = new ResultModel(data);
        r.setSuccess(true);
        r.setCode(SUCCESS_CODE);
        r.setMessage("成功");
        return r;
    }
    
    public static <T> ResultModel<T> ok() {
    	ResultModel r = new ResultModel();
        r.setSuccess(true);
        r.setCode(SUCCESS_CODE);
        r.setMessage("成功");
        return r;
    }

    //失败静态方法
    public static <T> ResultModel<T> error(String code,String message) {
    	ResultModel r = new ResultModel();
        r.setSuccess(false);
        r.setCode(ERROR_CODE);
        r.setMessage(message);
        return r;
    }
    
  //失败静态方法
    public static <T> ResultModel<T> error(String message) {
        return error(ERROR_CODE,message);
    }
    
    public boolean isSuccess() {
    	return success;
    }
    

}
