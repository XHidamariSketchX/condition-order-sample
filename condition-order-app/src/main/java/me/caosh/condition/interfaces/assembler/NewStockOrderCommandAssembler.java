package me.caosh.condition.interfaces.assembler;

import hbec.intellitrade.condorder.domain.OrderState;
import hbec.intellitrade.condorder.domain.TradeCustomerInfo;
import me.caosh.condition.domain.model.order.newstock.NewStockOrder;
import me.caosh.condition.domain.model.order.newstock.NewStockPurchaseCondition;
import me.caosh.condition.domain.util.DateFormats;
import me.caosh.condition.interfaces.command.NewStockOrderCreateCommand;
import me.caosh.condition.interfaces.command.NewStockOrderUpdateCommand;
import org.joda.time.LocalTime;


/**
 * Created by caosh on 2017/8/9.
 */
public class NewStockOrderCommandAssembler {
    public static NewStockOrder assemble(Long orderId, TradeCustomerInfo tradeCustomerInfo, NewStockOrderCreateCommand command) {
        OrderState orderState = OrderState.ACTIVE;
        LocalTime purchaseTime = LocalTime.parse(command.getPurchaseTime(), DateFormats.HH_MM_SS);
        NewStockPurchaseCondition newStockPurchaseCondition = new NewStockPurchaseCondition(purchaseTime);
        return new NewStockOrder(orderId, tradeCustomerInfo, newStockPurchaseCondition, orderState);
    }

    public static NewStockOrder merge(NewStockOrder oldOrder, NewStockOrderUpdateCommand command) {
        OrderState orderState = OrderState.ACTIVE;
        LocalTime purchaseTime = LocalTime.parse(command.getPurchaseTime(), DateFormats.HH_MM_SS);
        NewStockPurchaseCondition newStockPurchaseCondition = new NewStockPurchaseCondition(purchaseTime);
        return new NewStockOrder(oldOrder.getOrderId(), oldOrder.getCustomer(),
                newStockPurchaseCondition, orderState);
    }

    private NewStockOrderCommandAssembler() {
    }
}
