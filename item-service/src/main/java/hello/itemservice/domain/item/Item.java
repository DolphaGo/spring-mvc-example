package hello.itemservice.domain.item;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @ToString
@NoArgsConstructor
public class Item {
    private Long id;
    private String itemName;
    private Integer price; // null일수도 있음을 상기하는 것. int로 안쓰는 이유. 수량 값이 안들어 올 수도 있다는 것
    private Integer quantity;

    public Item(final String itemName, final Integer price, final Integer quantity) {
        this.itemName = itemName;
        this.price = price;
        this.quantity = quantity;
    }
}
