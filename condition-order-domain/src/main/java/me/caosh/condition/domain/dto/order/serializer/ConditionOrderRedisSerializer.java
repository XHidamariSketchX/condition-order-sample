package me.caosh.condition.domain.dto.order.serializer;

import me.caosh.condition.domain.dto.order.ConditionOrderDTO;
import me.caosh.condition.domain.dto.order.assembler.ConditionOrderDTOAssembler;
import me.caosh.condition.domain.model.order.ConditionOrder;
import me.caosh.condition.domain.util.ConditionOrderGSONUtils;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.nio.charset.Charset;

/**
 * Created by caosh on 2017/8/13.
 */
public class ConditionOrderRedisSerializer implements RedisSerializer<ConditionOrder> {
    @Override
    public byte[] serialize(ConditionOrder conditionOrder) throws SerializationException {
        ConditionOrderDTO conditionOrderDTO = ConditionOrderDTOAssembler.toDTO(conditionOrder);
        String json = ConditionOrderGSONUtils.getGSON().toJson(conditionOrderDTO);
        return json.getBytes(Charset.forName("utf-8"));
    }

    @Override
    public ConditionOrder deserialize(byte[] bytes) throws SerializationException {
        String json = new String(bytes, Charset.forName("utf-8"));
        ConditionOrderDTO conditionOrderDTO = ConditionOrderGSONUtils.getGSON().fromJson(json, ConditionOrderDTO.class);
        return ConditionOrderDTOAssembler.fromDTO(conditionOrderDTO);
    }
}
