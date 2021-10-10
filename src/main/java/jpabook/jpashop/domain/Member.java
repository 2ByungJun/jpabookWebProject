package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;

    @Embedded // '내장타입을 포함했다.' 라는 뜻.
    private Address adress;

    // order 테이블에 있는 meber 에 대해 값을 가직고 있는 거울일 뿐이다.
    @OneToMany(mappedBy = "member") // 일 대 다
    private List<Order> orders = new ArrayList<>();

}
