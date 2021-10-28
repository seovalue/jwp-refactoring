package kitchenpos.application;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Orders;
import kitchenpos.domain.TableGroup;
import kitchenpos.domain.repository.OrderRepository;
import kitchenpos.domain.repository.OrderTableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class OrderTableService {
    private final OrderTableRepository orderTableRepository;
    private final OrderRepository orderRepository;

    public OrderTableService(OrderTableRepository orderTableRepository, OrderRepository orderRepository) {
        this.orderTableRepository = orderTableRepository;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public List<OrderTable> create(List<OrderTable> savedOrderTables, TableGroup savedTableGroup) {
        List<OrderTable> orderTables = new ArrayList<>();
        for (final OrderTable savedOrderTable : savedOrderTables) {
            savedOrderTable.addTableGroup(savedTableGroup);
            savedOrderTable.changeEmpty(false);
            orderTables.add(orderTableRepository.save(savedOrderTable));
        }
        return orderTables;
    }

    public List<OrderTable> findAllOrderTables(List<Long> orderTableIds) {
        List<OrderTable> orderTables = orderTableRepository.findAllByIdIn(orderTableIds);
        validateOrderTables(orderTableIds, orderTables);
        return orderTables;
    }

    private void validateOrderTables(List<Long> orderTableIds, List<OrderTable> orderTables) {
        if (orderTableIds.size() != orderTables.size()) {
            throw new IllegalArgumentException();
        }
    }

    @Transactional
    public void ungroup(Long tableGroupId) {
        List<OrderTable> orderTables = orderTableRepository.findAllByTableGroupId(tableGroupId);
        for (OrderTable orderTable : orderTables) {
            List<Orders> orders = orderRepository.findAllByOrderTableId(orderTable.getId());
            orders.stream()
                    .filter(Orders::isNotCompleted)
                    .findAny()
                    .ifPresent(order -> {
                        throw new IllegalArgumentException("아직 조리 혹은 식사 중인 주문이 존재합니다.");
                    });
            orderTable.ungroup();
        }
    }
}