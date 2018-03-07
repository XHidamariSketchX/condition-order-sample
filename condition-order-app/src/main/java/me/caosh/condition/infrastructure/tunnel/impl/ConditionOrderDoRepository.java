package me.caosh.condition.infrastructure.tunnel.impl;

import me.caosh.condition.infrastructure.tunnel.model.ConditionOrderDO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * Created by caosh on 2017/8/3.
 */
public interface ConditionOrderDoRepository extends CrudRepository<ConditionOrderDO, Long> {

    @Query("select c from ConditionOrderDO c where c.deleted = 0 and c.orderId = ?1")
    ConditionOrderDO findNotDeleted(Long orderId);

    @Query("select c from ConditionOrderDO c where c.deleted = 0 and c.orderState = 1")
    List<ConditionOrderDO> findAllMonitoring();

    @Query("select c from ConditionOrderDO c where customerNo = ?1 and c.deleted = 0 and c.orderState in (1,2)")
    List<ConditionOrderDO> findMonitoring(String customerNo);
}
