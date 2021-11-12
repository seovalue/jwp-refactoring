package kitchenpos.menu.application;

import kitchenpos.exception.NonExistentException;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.repository.MenuGroupRepository;
import kitchenpos.menu.domain.repository.MenuRepository;
import kitchenpos.menu.ui.dto.MenuRequest;
import kitchenpos.menu.ui.dto.MenuResponse;
import kitchenpos.menu.ui.dto.MenuUpdateRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class MenuService {
    private final MenuRepository menuRepository;
    private final MenuGroupRepository menuGroupRepository;
    private final MenuProductService menuProductService;

    public MenuService(MenuRepository menuRepository,
                       MenuGroupRepository menuGroupRepository,
                       MenuProductService menuProductService
    ) {
        this.menuRepository = menuRepository;
        this.menuGroupRepository = menuGroupRepository;
        this.menuProductService = menuProductService;
    }

    @Transactional
    public MenuResponse create(final MenuRequest menuRequest) {
        MenuGroup menuGroup = menuGroupRepository.findById(menuRequest.getMenuGroupId())
                .orElseThrow(() -> new NonExistentException("menuGroup을 찾을 수 없습니다."));
        Menu menu = new Menu(menuRequest.getName(), menuRequest.getPrice(), menuGroup);
        Menu savedMenu = menuRepository.save(menu);
        menuProductService.addMenuToMenuProduct(menuRequest, savedMenu);
        return MenuResponse.from(savedMenu);
    }

    public List<MenuResponse> list() {
        return MenuResponse.from(menuRepository.findAll());
    }

    @Transactional
    public MenuResponse update(Long menuId, MenuUpdateRequest menuUpdateRequest) {
        Menu menu = menuRepository.findById(menuId)
                .orElseThrow(() -> new NonExistentException("menu를 찾을 수 없습니다."));
        menu.update(menuUpdateRequest.getName(), menuUpdateRequest.getPrice());
        menuRepository.save(menu);
        return MenuResponse.from(menu);
    }
}
