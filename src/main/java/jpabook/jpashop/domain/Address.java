package jpabook.jpashop.domain;

import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable // '어딘가에 내장이 되어있다.' 라는 뜻...개인 테이블 (생략가능)
@Getter // 값 타입은 별도로 Setter 가 필요없음.
public class Address {

    private String city;
    private String street;
    private String zipcode;

    // JPA 단계에서는 무분별하게 호출됨을 방지에 protected 까지 허용
    // 다른 사용자는 JPA 스택 때문에 선언한 것이라고 인지하고 건드리지 않게 됨.
    protected Address() {
    }

    // 생성자 없이 선언하면 에러가 발생함.
    // JPA 에서는 기본 default 생성자를 선언하고 사용해야함
    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
