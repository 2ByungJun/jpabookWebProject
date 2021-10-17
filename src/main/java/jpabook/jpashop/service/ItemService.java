package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public void saveItem(Item item){
        itemRepository.save(item);
    }


    // 1. 변경 감지 기능 사용
    // merge 와 다르게 전달받은 파라미터를 값을 원하는대로 수정할 수 있다.
    @Transactional
    public Item updateItem(Long itemId, String name, int price, int stockQuantity){
        Item findItem = itemRepository.findOne(itemId);

        // set 방식이 아니라 findItem.change(price, name, stockQuantity) ... 이런 의미있는 메서드를 만드는 것이 좋다.
        findItem.setName(name);
        findItem.setPrice(price);
        findItem.setStockQuantity(stockQuantity);
        /// ....
        // 준영속 상태의 Book 을 Transactional 에 의해 커밋이 된다.
        // 이후 set 을 하여 변경하여 JPA 에 update 문이 적용된다.
        return findItem;
    }

    public List<Item> findItems(){
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId){
        return itemRepository.findOne(itemId);
    }

}
