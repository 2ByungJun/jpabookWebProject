package jpabook.jpashop;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class) // spring 에서 사용할 것이다.
@SpringBootTest // springbootTest 사용
public class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Test
    @Transactional //Spring 껄 쓰는걸 권장 //test에 있는 경우 롤백한다.
    @Rollback(false) //false시, 롤백하지않고 그대로 커밋한다.
    public void testMember() throws Exception{
        //given
        Member member = new Member();
        member.setUsername("memberA");

        //when
        long savedId = memberRepository.save(member);
        Member findMember = memberRepository.find(savedId);

        //then
        // 검증
        Assertions.assertThat(findMember.getId()).isEqualTo(member.getId());
        Assertions.assertThat(findMember.getUsername()).isEqualTo(member.getUsername());

        Assertions.assertThat(findMember).isEqualTo(member); // == 비교
        // ture
        // why? 같은 영속성 컨텍스트에서는 동일한 엔티티로 취급하기에 true
        //      1차캐시에서 비교하기 때문에 그대로 꺼내온다.
    }
}