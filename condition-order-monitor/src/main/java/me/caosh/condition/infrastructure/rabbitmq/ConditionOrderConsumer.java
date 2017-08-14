package me.caosh.condition.infrastructure.rabbitmq;

import me.caosh.condition.domain.dto.order.ConditionOrderMonitorDTO;
import me.caosh.condition.domain.dto.order.assembler.ConditionOrderDTOAssembler;
import me.caosh.condition.domain.dto.order.converter.ConditionOrderGSONMessageConverter;
import me.caosh.condition.domain.model.constants.OrderCommandType;
import me.caosh.condition.domain.model.order.ConditionOrder;
import me.caosh.condition.domain.model.order.event.ConditionOrderCommandEvent;
import me.caosh.condition.domain.model.order.event.ConditionOrderDeleteCommandEvent;
import me.caosh.condition.domain.model.share.ValuedEnumUtil;
import me.caosh.condition.domain.util.EventBuses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.MessageListener;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Collections;

/**
 * Created by caosh on 2017/8/9.
 */
@Configuration
@ConfigurationProperties(prefix = "me.caosh.condition.conditionOrder")
@Component
public class ConditionOrderConsumer {
    private static final Logger logger = LoggerFactory.getLogger(ConditionOrderConsumer.class);

    private String exchangeName;
    private String queueName;
    private String routingKey;

    private final ConnectionFactory connectionFactory;
    private final AmqpAdmin amqpAdmin;
    private final MessageConverter messageConverter = new ConditionOrderGSONMessageConverter<>(ConditionOrderMonitorDTO.class);

    public void setExchangeName(String exchangeName) {
        this.exchangeName = exchangeName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public void setRoutingKey(String routingKey) {
        this.routingKey = routingKey;
    }

    public ConditionOrderConsumer(ConnectionFactory connectionFactory, AmqpAdmin amqpAdmin) {
        this.connectionFactory = connectionFactory;
        this.amqpAdmin = amqpAdmin;
    }

    @PostConstruct
    public void init() throws Exception {
        Queue queue = new Queue(queueName, false, false, false);
        Binding binding = new Binding(queue.getName(), Binding.DestinationType.QUEUE, exchangeName, routingKey, Collections.emptyMap());

        amqpAdmin.declareQueue(queue);
        amqpAdmin.declareBinding(binding);
        logger.info("=== Condition order consumer initialized ===");

        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        container.addQueues(queue);
        container.setMessageListener((MessageListener) message -> {
            ConditionOrderMonitorDTO conditionOrderMonitorDTO = (ConditionOrderMonitorDTO) messageConverter.fromMessage(message);
            logger.debug("Receive condition order message <== {}", conditionOrderMonitorDTO);
            if (conditionOrderMonitorDTO.getConditionOrderDTO() != null) {
                OrderCommandType orderCommandType = ValuedEnumUtil.valueOf(conditionOrderMonitorDTO.getOrderCommandType(),
                        OrderCommandType.class);
                ConditionOrder conditionOrder = ConditionOrderDTOAssembler.fromDTO(conditionOrderMonitorDTO.getConditionOrderDTO());
                EventBuses.DEFAULT.post(new ConditionOrderCommandEvent(orderCommandType, conditionOrder));
            } else {
                EventBuses.DEFAULT.post(new ConditionOrderDeleteCommandEvent(conditionOrderMonitorDTO.getOrderId()));
            }
        });
        container.start();
    }
}
