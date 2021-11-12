package kitchenpos.order.domain.repository;

import kitchenpos.order.domain.OrderLineItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderLineItemRepository extends JpaRepository<OrderLineItem, Long> {
    @Query("select ol from OrderLineItem ol where ol.menu.id = :menuId")
    List<OrderLineItem> findAllByMenuId(Long menuId);
}
