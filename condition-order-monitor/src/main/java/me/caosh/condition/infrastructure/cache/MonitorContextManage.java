package me.caosh.condition.infrastructure.cache;

import me.caosh.condition.domain.model.monitor.MonitorContext;

/**
 * Created by caosh on 2017/8/14.
 *
 * @author caoshuhao@touker.com
 */
public interface MonitorContextManage {
    Iterable<MonitorContext> getAll();

    void save(MonitorContext monitorContext);

    void update(MonitorContext monitorContext);

    void remove(Long orderId);
}
