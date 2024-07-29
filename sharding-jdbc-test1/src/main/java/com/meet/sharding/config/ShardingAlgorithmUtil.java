package com.meet.sharding.config;

/**
 * @program: meet-boot
 * @ClassName ShardingAlgorithmUtil
 * @description:
 * @author: MT
 * @create: 2023-07-19 18:14
 **/

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 分片算法工具类
 */
public class ShardingAlgorithmUtil {

    /**
     * 获取年份
     */
    public static String getYearByMillisecond(long millisecond) {
        return new SimpleDateFormat("yyyy").format(new Date(millisecond * 1000L));
    }

    /**
     * 获取年月
     */
    public static String getYearJoinMonthByMillisecond(long millisecond) {
        return new SimpleDateFormat("yyyyMM").format(new Date(millisecond * 1000L));
    }
}