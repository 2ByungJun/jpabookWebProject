package jpabook.jpashop.domain;

import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable // '어딘가에 내장이 되어있다.' 라는 뜻...개인 테이블 (생략가능)
@Getter
public class Address {

    private String city;
    private String street;
    private String zipcode;
}
