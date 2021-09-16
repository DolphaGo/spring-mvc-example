package hello.itemservice.domain.item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
public class ItemRepository {

    // 멀티 스레드 환경에서는 HashMap이 아니라, ConcurrentHashMap을 사용해야 한다. (동시 접근하면 시퀀스가 꼬일 수 있음)
    // 또한 sequence도 atomicLong을 사용해야 한다.

    private static final Map<Long, Item> store = new HashMap<>(); // static
    private static long sequence = 0L; // static

    public Item save(Item item) {
        item.setId(++sequence);
        store.put(item.getId(), item);
        return item;
    }

    public Item findById(Long id) {
        return store.get(id);
    }

    public List<Item> findAll() {
        return new ArrayList<>(store.values());
    }

    public void update(Long itemId, Item updateParam) { // 참고로 updateParam 같은 경우엔 다른 타입을 명시해주는 것이 좋다. updateParam.getId() 는 쓰는 필드가 아니지만, 자리를 차지하고 있기 때문에 개발자 입장에서 혼돈이 올 수 있기에 좋은 코드가 아니다.
        final Item findItem = findById(itemId);
        findItem.setItemName(updateParam.getItemName());
        findItem.setPrice(updateParam.getPrice());
        findItem.setQuantity(updateParam.getQuantity());
    }

    public void clearStore() {
        store.clear();
    }
}
