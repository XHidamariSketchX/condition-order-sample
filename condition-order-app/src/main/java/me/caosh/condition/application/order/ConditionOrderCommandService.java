package me.caosh.condition.application.order;

import me.caosh.condition.domain.model.order.ConditionOrder;

/**
 * Created by caosh on 2017/8/2.
 */
public interface ConditionOrderCommandService {
    void save(ConditionOrder conditionOrder);

    void update(ConditionOrder conditionOrder);

    void updateDynamicProperties(ConditionOrder conditionOrder);

    void remove(Long orderId);
}
