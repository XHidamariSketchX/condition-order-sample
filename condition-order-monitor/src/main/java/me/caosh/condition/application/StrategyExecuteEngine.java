package me.caosh.condition.application;

import com.google.common.eventbus.Subscribe;
import hbec.intellitrade.common.market.RealTimeMarket;
import me.caosh.condition.domain.model.market.event.RealTimeMarketPushEvent;
import me.caosh.condition.domain.model.order.ConditionOrder;
import me.caosh.condition.domain.model.order.event.ConditionOrderCreateCommandEvent;
import me.caosh.condition.domain.model.order.event.ConditionOrderDeleteCommandEvent;
import me.caosh.condition.domain.model.order.event.ConditionOrderUpdateCommandEvent;
import me.caosh.condition.domain.model.signalpayload.SignalPayload;
import me.caosh.condition.domain.model.strategy.container.StrategyContainer;
import me.caosh.condition.infrastructure.eventbus.MonitorEventBus;
import me.caosh.condition.infrastructure.repository.MonitorRepository;
import me.caosh.condition.infrastructure.timer.event.TimerEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.Map;

/**
 * Created by caosh on 2017/8/9.
 */
@Component
public class StrategyExecuteEngine {
    private static final Logger logger = LoggerFactory.getLogger(StrategyExecuteEngine.class);

    private final StrategyContainer strategyContainer = new StrategyContainer();
    private final MonitorRepository monitorRepository;

    public StrategyExecuteEngine(MonitorRepository monitorRepository) {
        this.monitorRepository = monitorRepository;
    }

    @PostConstruct
    public void init() {
        MonitorEventBus.EVENT_SERIALIZED.register(this);

        for (ConditionOrder conditionOrder : monitorRepository.getAllOrders()) {
            strategyContainer.add(conditionOrder);
        }
    }

    @Subscribe
    public void onRealTimeMarketPushEvent(RealTimeMarketPushEvent e) {
        Map<String, RealTimeMarket> realTimeMarketMap = e.getMarketMap();
        logger.info("---------------- Start checking ----------------");

        Collection<SignalPayload> signalPayloads = strategyContainer.onMarketUpdate(e.getMarketMap().values());

        logger.info("---------------- Finish checking, count={} ----------------", strategyContainer.size());
    }

    @Subscribe
    public void onTimer(TimerEvent e) {
        Collection<SignalPayload> signalPayloads = strategyContainer.onSecondTick();
    }

    @Subscribe
    public void onConditionOrderCreateCommand(ConditionOrderCreateCommandEvent e) {
        ConditionOrder conditionOrder = e.getConditionOrder();
        monitorRepository.save(conditionOrder);
        strategyContainer.add(conditionOrder);
        logger.info("Create condition order ==> {}", conditionOrder);
    }

    @Subscribe
    public void onConditionOrderUpdateCommand(ConditionOrderUpdateCommandEvent e) {
        ConditionOrder conditionOrder = e.getConditionOrder();
        monitorRepository.update(conditionOrder);
        strategyContainer.add(conditionOrder);
        logger.info("Update condition order ==> {}", conditionOrder);
    }

    @Subscribe
    public void onConditionOrderDeleteCommand(ConditionOrderDeleteCommandEvent e) {
        Long orderId = e.getOrderId();
        monitorRepository.remove(orderId);
        strategyContainer.remove(orderId.intValue());
        logger.info("Remove condition order ==> {}", orderId);
    }
}
