package kitchenpos.domain;

import kitchenpos.exception.FieldNotValidException;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_table_id", nullable = false)
    private OrderTable orderTable;

    private String orderStatus;

    @Column(nullable = false)
    private LocalDateTime orderedTime;

    protected Order() {
    }

    public Order(OrderTable orderTable) {
        this(null, orderTable, null, LocalDateTime.now());
    }

    public Order(OrderTable orderTable, String orderStatus) {
        this(null, orderTable, orderStatus, LocalDateTime.now());
    }

    public Order(OrderTable orderTable, String orderStatus, LocalDateTime orderedTime) {
        this(null, orderTable, orderStatus, orderedTime);
    }

    public Order(Long id, OrderTable orderTable, String orderStatus, LocalDateTime orderedTime) {
        validate(orderTable);
        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        if (Objects.isNull(this.orderStatus) || this.orderStatus.isEmpty()) {
            this.orderStatus = OrderStatus.MEAL.name();
        }
        this.orderedTime = orderedTime;
    }

    private void validate(OrderTable orderTable) {
        if (orderTable.isEmpty()) {
            throw new FieldNotValidException("주문 테이블이 유효하지 않습니다.");
        }
    }

    public boolean isNotCompleted() {
        return Objects.equals(orderStatus, OrderStatus.MEAL.name()) ||
                Objects.equals(orderStatus, OrderStatus.COOKING.name());
    }

    public Long getId() {
        return id;
    }

    public OrderTable getOrderTable() {
        return orderTable;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public LocalDateTime getOrderedTime() {
        return orderedTime;
    }

    public void updateOrderStatus(String orderStatus) {
        if (!isNotCompleted()) {
            throw new IllegalArgumentException("주문이 이미 완료된 상태입니다.");
        }
        this.orderStatus = orderStatus;
    }

    public Long getOrderTableId() {
        return orderTable.getId();
    }
}
