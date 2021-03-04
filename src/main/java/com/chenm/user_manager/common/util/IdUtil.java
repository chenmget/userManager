package com.chenm.user_manager.common.util;

import java.util.UUID;

/**
 * 主键生成工具类
 * @author admin
 *
 */
public class IdUtil {
	public static String getUUID() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
