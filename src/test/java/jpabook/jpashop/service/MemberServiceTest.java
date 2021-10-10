package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@RunWith(SpringRunner.class) // JUnit 실행할 때 스프링러너랑 테스트할래
@SpringBootTest // 스프링 컨테이너 안에서 돌릴거다.
@Transactional  // 테스트가 끝나면 모두 rollback
public class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    EntityManager em;

    @Test
//    @Rollback(false) // 롤백하지 않고 DB에 커밋하여 등록쿼리를 확인할 수 있다.
    public void 회원가입() throws Exception {
        //given
        Member member = new Member();
        member.setName("kim");

        //when
        Long saveId = memberService.join(member);

        //then
//        em.flush(); // 롤백 대신 사용(DB에 데이터 남는걸 방지)
        assertEquals(member, memberRepository.findOne(saveId));
    }

    @Test(expected = IllegalStateException.class)
    public void 중복_회원_예외() throws Exception {
        //given
        Member member1 = new Member();
        member1.setName("kim");

        Member member2 = new Member();
        member2.setName("kim");

        //when
        memberService.join(member1);
        memberService.join(member2);  //동일 이름, 예외 발생지점!

        /*
        @Test(expected = IllegalStateException.class) 해결
          try{
            memberService.join(member2);
          }catch (IllegalStateException e){
            return;
          }
        */

        //then
        fail("예외가 발생해야 한다."); // 여기 오면 안된다는 코드
    }

}