package com.github.joine.gateway.component.listener;

import com.netflix.zuul.FilterFileManager;
import com.netflix.zuul.FilterLoader;
import com.netflix.zuul.groovy.GroovyCompiler;
import com.netflix.zuul.groovy.GroovyFileFilter;
import com.netflix.zuul.monitoring.MonitoringHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.embedded.EmbeddedServletContainerInitializedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @Author: JenphyJohn
 * @Date: 2018/11/27 2:34 PM
 */
@Slf4j
@Component
@ConditionalOnProperty("zuul.groovy.path")
public class GroovyLoadInitListener {
    @Value("${zuul.groovy.path}")
    private String groovyPath;

    @EventListener(value = {EmbeddedServletContainerInitializedEvent.class})
    public void init() {
        MonitoringHelper.initMocks();
        FilterLoader.getInstance().setCompiler(new GroovyCompiler());
        FilterFileManager.setFilenameFilter(new GroovyFileFilter());
        try {
            FilterFileManager.init(10, groovyPath);
        } catch (Exception e) {
            log.error("初始化网关Groovy 文件失败 {}", e);
        }
        log.warn("初始化网关Groovy 文件成功");
    }
}
