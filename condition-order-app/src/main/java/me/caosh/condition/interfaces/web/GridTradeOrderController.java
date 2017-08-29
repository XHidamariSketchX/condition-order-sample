package me.caosh.condition.interfaces.web;

import me.caosh.condition.application.order.ConditionOrderCommandService;
import me.caosh.condition.domain.model.order.ConditionOrder;
import me.caosh.condition.domain.model.order.TradeCustomerIdentity;
import me.caosh.condition.domain.model.order.constant.OrderState;
import me.caosh.condition.domain.model.order.grid.GridTradeOrder;
import me.caosh.condition.infrastructure.repository.ConditionOrderRepository;
import me.caosh.condition.infrastructure.repository.impl.ConditionOrderIdGenerator;
import me.caosh.condition.interfaces.assembler.GirdTradeOrderCommandAssembler;
import me.caosh.condition.interfaces.command.GridTradeOrderCreateCommand;
import me.caosh.condition.interfaces.command.GridTradeOrderUpdateCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

/**
 * Created by caosh on 2017/8/9.
 */
@RestController
@RequestMapping("/grid")
public class GridTradeOrderController {

    private final ConditionOrderIdGenerator idGenerator;
    private final ConditionOrderCommandService conditionOrderCommandService;
    private final ConditionOrderRepository conditionOrderRepository;

    @Autowired
    public GridTradeOrderController(ConditionOrderIdGenerator idGenerator,
                                    ConditionOrderCommandService conditionOrderCommandService,
                                    ConditionOrderRepository conditionOrderRepository) {
        this.idGenerator = idGenerator;
        this.conditionOrderCommandService = conditionOrderCommandService;
        this.conditionOrderRepository = conditionOrderRepository;
    }

    @RequestMapping("/create")
    public Long create(@Valid GridTradeOrderCreateCommand command) {
        Long orderId = idGenerator.nextId();
        TradeCustomerIdentity customerIdentity = new TradeCustomerIdentity(303348, "010000061086");
        GridTradeOrder gridTradeOrder = GirdTradeOrderCommandAssembler.assemble(orderId, customerIdentity, command);
        conditionOrderCommandService.save(gridTradeOrder);
        return orderId;
    }

    @RequestMapping("/update")
    public Long update(@Valid GridTradeOrderUpdateCommand command) {
        Long orderId = command.getOrderId();
        Optional<ConditionOrder> conditionOrderOptional = conditionOrderRepository.findOne(orderId);
        if (!conditionOrderOptional.isPresent()) {
            return -1L;
        }
        ConditionOrder conditionOrder = conditionOrderOptional.get();
        if (conditionOrder.getOrderState() != OrderState.ACTIVE && conditionOrder.getOrderState() != OrderState.PAUSED) {
            return -2L;
        }
        GridTradeOrder oldOrder = (GridTradeOrder) conditionOrder;
        GridTradeOrder newOrder = GirdTradeOrderCommandAssembler.merge(oldOrder, command);
        conditionOrderCommandService.update(newOrder);
        return orderId;
    }
}
