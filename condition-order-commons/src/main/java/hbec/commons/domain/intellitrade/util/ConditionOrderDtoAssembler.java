package hbec.commons.domain.intellitrade.util;

import hbec.commons.domain.intellitrade.conditionorder.ConditionOrderDTO;
import hbec.intellitrade.conditionorder.domain.ConditionOrder;
import hbec.intellitrade.conditionorder.domain.orders.ConditionOrderBuilder;
import me.caosh.autoasm.AutoAssemblers;
import me.caosh.autoasm.ConvertibleBuilder;
import me.caosh.autoasm.MappedClass;

/**
 * @author caosh/caoshuhao@touker.com
 * @date 2018/3/23
 */
public class ConditionOrderDtoAssembler {
    private static final ConditionOrderDtoAssembler CODE_COVERAGE = new ConditionOrderDtoAssembler();

    private ConditionOrderDtoAssembler() {
    }

    public static ConditionOrderDTO assemble(ConditionOrder conditionOrder) {
        return AutoAssemblers.getDefault().assemble(conditionOrder, ConditionOrderDTO.class);
    }

    public static ConditionOrder disassemble(ConditionOrderDTO conditionOrderDTO) {
        Class<? extends ConvertibleBuilder> conditionBuilderClass =
                conditionOrderDTO.getCondition().getClass().getAnnotation(MappedClass.class).builderClass();
        return AutoAssemblers.getDefault()
                             .useBuilder(new ConditionOrderBuilder(conditionBuilderClass))
                             .disassemble(conditionOrderDTO)
                             .build();
    }
}
