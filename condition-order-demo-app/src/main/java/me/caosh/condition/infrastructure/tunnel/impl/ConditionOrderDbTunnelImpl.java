package me.caosh.condition.infrastructure.tunnel.impl;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import hbec.intellitrade.conditionorder.domain.ConditionOrder;
import me.caosh.condition.infrastructure.repository.assembler.ConditionOrderDoAssembler;
import me.caosh.condition.infrastructure.tunnel.ConditionOrderDbTunnel;
import me.caosh.condition.infrastructure.tunnel.model.ConditionOrderDO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author caosh/caoshuhao@touker.com
 * @date 2018/3/7
 */
@Repository
public class ConditionOrderDbTunnelImpl implements ConditionOrderDbTunnel {
    private final ConditionOrderDoRepository conditionOrderDoRepository;

    public ConditionOrderDbTunnelImpl(ConditionOrderDoRepository conditionOrderDoRepository) {
        this.conditionOrderDoRepository = conditionOrderDoRepository;
    }

    @Override
    public void save(ConditionOrder conditionOrder) {
        ConditionOrderDO conditionOrderDO = ConditionOrderDoAssembler.assemble(conditionOrder);
        conditionOrderDoRepository.save(conditionOrderDO);
    }

    @Override
    public void remove(Long orderId) {
        ConditionOrderDO conditionOrderDO = conditionOrderDoRepository.findOne(orderId);
        if (conditionOrderDO != null) {
            conditionOrderDO.setDeleted(true);
            conditionOrderDoRepository.save(conditionOrderDO);
        }
    }

    @Override
    public Optional<ConditionOrder> findOne(Long orderId) {
        ConditionOrderDO conditionOrderDO = conditionOrderDoRepository.findNotDeleted(orderId);
        if (conditionOrderDO == null) {
            return Optional.absent();
        }
        ConditionOrder conditionOrder = ConditionOrderDoAssembler.disassemble(conditionOrderDO);
        return Optional.of(conditionOrder);
    }

    @Override
    public List<ConditionOrder> findAllMonitoring() {
        List<ConditionOrderDO> conditionOrderDOs = conditionOrderDoRepository.findAllMonitoring();
        return Lists.transform(conditionOrderDOs, new Function<ConditionOrderDO, ConditionOrder>() {
            @Override
            public ConditionOrder apply(ConditionOrderDO conditionOrderDO) {
                return ConditionOrderDoAssembler.disassemble(conditionOrderDO);
            }
        });
    }
}