package com.chenm.user_manager.user.util;

import org.springframework.util.StringUtils;

import com.chenm.user_manager.common.model.ResultModel;

/**
 * 验证工具类
 * @author admin
 *
 */
public class ValidityUtil {
    //数字
    public static final String REG_NUMBER = ".*\\d+.*";
    //小写字母
    public static final String REG_UPPERCASE = ".*[A-Z]+.*";
    //大写字母
    public static final String REG_LOWERCASE = ".*[a-z]+.*";
    //特殊符号
    public static final String REG_SYMBOL = ".*[~!@#$%^&*()_+|<>,.?/:;'\\[\\]{}\"]+.*";
 
    public static ResultModel checkPasswordRule(String password){
    	if(StringUtils.isEmpty(password) ) {
    		return ResultModel.error("密码不能为空");
    	}
        if (password.length() <8 ||password.length() >32) {
        	return ResultModel.error("密码长度在8-12位");
        }
        int i = 0;
        if (password.matches(REG_NUMBER)) i++;
        if (password.matches(REG_LOWERCASE))i++;
        if (password.matches(REG_UPPERCASE)) i++;
        if (password.matches(REG_SYMBOL)) i++;
 
        if (i  < 3 )  {
        	return ResultModel.error("密码复杂度包含字符，数字，英文大小写 至少三种");
        }
 
        return ResultModel.ok();
    }


}
