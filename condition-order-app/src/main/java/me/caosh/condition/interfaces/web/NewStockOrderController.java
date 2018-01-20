package me.caosh.condition.interfaces.web;

import me.caosh.condition.application.order.ConditionOrderCommandService;
import me.caosh.condition.domain.model.order.ConditionOrder;
import me.caosh.condition.domain.model.order.TradeCustomer;
import me.caosh.condition.domain.model.order.constant.OrderState;
import me.caosh.condition.domain.model.order.newstock.NewStockOrder;
import me.caosh.condition.infrastructure.repository.ConditionOrderRepository;
import me.caosh.condition.infrastructure.repository.impl.ConditionOrderIdGenerator;
import me.caosh.condition.interfaces.assembler.NewStockOrderCommandAssembler;
import me.caosh.condition.interfaces.command.NewStockOrderCreateCommand;
import me.caosh.condition.interfaces.command.NewStockOrderUpdateCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

/**
 * Created by caosh on 2017/8/9.
 */
@RestController
@RequestMapping("/newstock")
public class NewStockOrderController {

    private final ConditionOrderIdGenerator idGenerator;
    private final ConditionOrderCommandService conditionOrderCommandService;
    private final ConditionOrderRepository conditionOrderRepository;

    @Autowired
    public NewStockOrderController(ConditionOrderIdGenerator idGenerator,
                                   ConditionOrderCommandService conditionOrderCommandService,
                                   ConditionOrderRepository conditionOrderRepository) {
        this.idGenerator = idGenerator;
        this.conditionOrderCommandService = conditionOrderCommandService;
        this.conditionOrderRepository = conditionOrderRepository;
    }

    @RequestMapping("/create")
    public Long create(@Valid NewStockOrderCreateCommand command) {
        Long orderId = idGenerator.nextId();
        TradeCustomer customerIdentity = new TradeCustomer(303348, "010000061086");
        NewStockOrder timeOrder = NewStockOrderCommandAssembler.assemble(orderId, customerIdentity, command);
        conditionOrderCommandService.save(timeOrder);
        return orderId;
    }

    @RequestMapping("/update")
    public Long update(@Valid NewStockOrderUpdateCommand command) {
        Long orderId = command.getOrderId();
        Optional<ConditionOrder> conditionOrderOptional = conditionOrderRepository.findOne(orderId);
        // TODO: duplicated codes
        if (!conditionOrderOptional.isPresent()) {
            return -1L;
        }
        ConditionOrder conditionOrder = conditionOrderOptional.get();
        if (conditionOrder.getOrderState() != OrderState.ACTIVE && conditionOrder.getOrderState() != OrderState.PAUSED) {
            return -2L;
        }
        NewStockOrder oldOrder = (NewStockOrder) conditionOrder;
        NewStockOrder newOrder = NewStockOrderCommandAssembler.merge(oldOrder, command);
        conditionOrderCommandService.update(newOrder);
        return orderId;
    }
}
