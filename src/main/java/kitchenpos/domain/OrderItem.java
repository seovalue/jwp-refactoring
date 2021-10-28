package kitchenpos.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    private Orders orders;

    @ManyToOne(fetch = FetchType.LAZY)
    private Menu menu;

    @Column(nullable = false)
    private long quantity;

    protected OrderItem() {
    }

    public OrderItem(Menu menu, long quantity) {
        this(null, null, menu, quantity);
    }

    public OrderItem(Orders orders, Menu menu, long quantity) {
        this(null, orders, menu, quantity);
    }

    public OrderItem(Long seq, Orders orders, Menu menu, long quantity) {
        this.seq = seq;
        this.orders = orders;
        this.menu = menu;
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public Orders getOrder() {
        return orders;
    }

    public Menu getMenu() {
        return menu;
    }

    public long getQuantity() {
        return quantity;
    }

    public Long getOrderId() {
        return orders.getId();
    }

    public Long getMenuId() {
        return menu.getId();
    }
}