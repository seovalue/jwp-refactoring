package kitchenpos.dto.table;

import kitchenpos.domain.Table;

public class TableCreateRequest {

    private int numberOfGuests;
    private boolean empty;

    public TableCreateRequest() {
        this(true, 0);
    }

    public TableCreateRequest(boolean empty) {
        this(empty, 0);
    }

    public TableCreateRequest(boolean empty, int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public Table toEntity() {
        return new Table(numberOfGuests, empty);
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}