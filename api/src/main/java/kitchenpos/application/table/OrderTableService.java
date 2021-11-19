package kitchenpos.application.table;

import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTables;
import kitchenpos.domain.table.repository.OrderTableRepository;
import kitchenpos.event.OrderStatusCheckEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class OrderTableService {
    private final OrderTableRepository orderTableRepository;
    private final OrderStatusCheckEventPublisher orderStatusCheckEventPublisher;

    public OrderTableService(OrderTableRepository orderTableRepository, OrderStatusCheckEventPublisher orderStatusCheckEventPublisher) {
        this.orderTableRepository = orderTableRepository;
        this.orderStatusCheckEventPublisher = orderStatusCheckEventPublisher;
    }

    public List<OrderTable> findAllOrderTables(List<Long> orderTableIds) {
        List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(orderTableIds);
        validateOrderTables(orderTableIds, orderTables);
        return orderTables;
    }

    private void validateOrderTables(List<Long> orderTableIds, List<OrderTable> orderTables) {
        if (orderTableIds.size() != orderTables.size()) {
            throw new IllegalArgumentException("요청과 조회 값의 결과가 다릅니다.");
        }
    }

    @Transactional
    public void ungroup(Long tableGroupId) {
        OrderTables orderTables = new OrderTables(orderTableRepository.findAllByTableGroupId(tableGroupId));
        orderTables.ungroupAll(orderStatusCheckEventPublisher);
    }
}
