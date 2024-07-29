package com.meet.sharding.config;

import org.apache.shardingsphere.api.sharding.standard.PreciseShardingAlgorithm;
import org.apache.shardingsphere.api.sharding.standard.PreciseShardingValue;
import org.springframework.stereotype.Component;

import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

/**
 * 库精确分片算法
 *
 */
@Component
public class PreciseDatabaseShardingAlgorithm implements PreciseShardingAlgorithm<Long> {

    /**
     * 库精确分片算法
     *
     * @param availableTargetNames 所有配置的库列表
     * @param shardingValue        分片值
     * @return 所匹配库的结果
     */
    @Override
    public String doSharding(Collection<String> availableTargetNames,
                             PreciseShardingValue<Long> shardingValue) {
        // 分片键值
        Long value = shardingValue.getValue();

        // 库后缀
        String yearStr = ShardingAlgorithmUtil.getYearByMillisecond(value);
        if (value <= 0) {
            throw new UnsupportedOperationException("preciseShardingValue is null");
        }
        for (String availableTargetName : availableTargetNames) {
            if (availableTargetName.endsWith(yearStr)) {
                return availableTargetName;
            }
        }
        throw new UnsupportedOperationException();
    }
}