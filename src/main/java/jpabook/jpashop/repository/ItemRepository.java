package jpabook.jpashop.repository;

import jpabook.jpashop.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;

    public void save(Item item){
        if(item.getId() == null){
            em.persist(item); // 아이디 값이 없다는 것은 완전히 새로 생성된 객체
        }else{
            em.merge(item);
            // 주의! 모든 파라미터로 전달받은 새로운 값을 교체해버린다! (실무에서는 위험)
            //      ( null 조차도)
            // 1. merge 실행
            // 2. 1차 캐시 엔티티 찾기 (영속성인 상태를) -- 다만 거의 준영속 상태라 없다.
            // 2-1. 없을 경우, DB 에서 조회하여 가져온다.
            // 3. mergeItem 의 값을 채워주어 영속상태로 만든다.
            // 4. 영속상태인 아이템을 반환한다.
        }
    }

    public Item findOne(Long id){
        return em.find(Item.class, id);
    }

    public List<Item> findAll(){
        return em.createQuery("select i from Item i", Item.class)
                .getResultList();
    }

}
