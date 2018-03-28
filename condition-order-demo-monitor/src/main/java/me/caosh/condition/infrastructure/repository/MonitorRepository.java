package me.caosh.condition.infrastructure.repository;

import hbec.intellitrade.condorder.domain.ConditionOrder;

/**
 * Created by caosh on 2017/8/11.
 */
public interface MonitorRepository {
    Iterable<ConditionOrder> getAllOrders();

    void update(ConditionOrder conditionOrder);

    void remove(Long orderId);
}
