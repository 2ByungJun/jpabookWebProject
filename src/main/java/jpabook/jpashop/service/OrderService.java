package jpabook.jpashop.service;

import jpabook.jpashop.domain.Delivery;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    /*
    주문
     */
    @Transactional
    public Long order(Long memberId, Long itemId, int count){

        //엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        //배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAdress());

        //주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        /*
        ** 생성방지( 생성메서드로 제약해주는 방법 ) **
        * @NoArgsConstructor(access = AccessLevel.PROTECTED)
        * new OrderItem();
         */

        //주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);

        //주문 저장
        orderRepository.save(order); // CascadeType.ALL 이 존재하여 연관 테이블에도 자동으로 persist 가 된다.

        return order.getId();
    }

    /*
    주문 취소
     */
    @Transactional
    public void cancelOrder(Long orderId){

        //주문 조회
        Order order = orderRepository.findOne(orderId);

        //주문 취소
        order.cancel();
        /*
        ** JPA 의 강점이 나타나는 로직 **
        * 엔티티의 데이터만 바꿔주면 변경내역 감지를 통해 DB에 업데이트 쿼리가 적용된다.
        * 만약 JPA 가 아니면 재고수량을 재증가 시키는 로직 및 orderItem 에 따로 업데이트 쿼리를 작성해야 한다.
         */
   }

    /*
    주문 검색
     */
//    public List<Order> findOrders(OrderSearch orderSearch){
//        return orderRepository.findAll(orderSearch);
//    }
}
