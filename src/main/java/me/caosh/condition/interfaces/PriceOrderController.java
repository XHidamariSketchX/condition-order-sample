package me.caosh.condition.interfaces;

import me.caosh.condition.application.order.ConditionOrderCommandService;
import me.caosh.condition.domain.model.order.*;
import me.caosh.condition.infrastructure.repository.impl.ConditionOrderIdGenerator;
import me.caosh.condition.interfaces.assembler.PriceOrderAssembler;
import me.caosh.condition.interfaces.command.PriceOrderCreateCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by caosh on 2017/8/9.
 */
@RestController
@RequestMapping("/price")
public class PriceOrderController {
    
    @Autowired
    private ConditionOrderIdGenerator idGenerator;

    @Autowired
    private ConditionOrderCommandService conditionOrderCommandService;
    
    @RequestMapping("/create")
    public Integer create(PriceOrderCreateCommand command) {
        Long orderId = idGenerator.nextId();
        PriceOrder priceOrder = PriceOrderAssembler.assemblePriceOrder(orderId, command);
        conditionOrderCommandService.save(priceOrder);
        return 1;
    }

}
