package com.github.joine.daemon.job;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.zen.elasticjob.spring.boot.annotation.ElasticJobConfig;
import lombok.extern.slf4j.Slf4j;

/**
 * @author JenphyJohn
 * @date 2018/2/7
 * 测试Job
 */
@Slf4j
@ElasticJobConfig(cron = "0 0 0/1 * * ?", shardingTotalCount = 3,
        shardingItemParameters = "0=joine1,1=joine2,2=joine3",
        startedTimeoutMilliseconds = 5000L,
        completedTimeoutMilliseconds = 10000L,
        eventTraceRdbDataSource = "dataSource")
public class JoineSimpleJob implements SimpleJob {
    /**
     * 业务执行逻辑
     *
     * @param shardingContext 分片信息
     */
    @Override
    public void execute(ShardingContext shardingContext) {
        log.info("shardingContext:{}", shardingContext);
    }
}
