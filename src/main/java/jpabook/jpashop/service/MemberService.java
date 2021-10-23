package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true) // spring 에서 제공하는 어노테이션을 쓰는걸 권장함
@RequiredArgsConstructor // final 로 선언된 항목의 인젝션(생성자를 만들어줌)
public class MemberService {

    //    @Autowired // 인젝션
    /*
    @Autowired 를 바로 헀을 때 단점이 있다.
    바꾸질 못하기 때문이다.

    그래서 더 좋은 방법은 '생성자 인젝션'을 사용한다.
    테스트 케이스를 사용할 때, 어디에 의존하는지 명확히 인지할 수 있다.
     */
    private final MemberRepository memberRepository; // 변경할 일이 없기에 final 선언

    /*
     * 회원 가입
     */
    @Transactional // 쓰기에는 readonly = false
    public Long join(Member member) {
        validateDuplicateMember(member); //중복 회원 검증
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
        /*
         * 해당로직에도 허점이 있는데, 멀티쓰레드형태로 동시에 같은 이름으로 회원가입을 할 때이다.
         * 그럴 때는 member_name 을 유니크 제약조건을 두어 쓰는걸 권장한다.
         */
    }

    //회원 전체 조회
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    //회원 조회
    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }

    @Transactional
    public void update(Long id, String name) {
        Member member = memberRepository.findOne(id);
        member.setName(name);
    }
}
