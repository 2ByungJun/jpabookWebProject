package jpabook.jpashop.api;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

//@Controller @ResponseBody // 합친 것이 REST 스타일인 Controller
@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    @GetMapping("/api/v1/members")
    public List<Member> membersV1() {
        // v1 : !문제점, 원치않는 정보까지 누설되게 됨. (orders 정보까지)
        // @JsonIgnore : 사용하면 제한을 막을 수는 있지만 이렇게 되면 다양한 호출에 대응하지 못한다.
        return memberService.findMembers();
    }

    @GetMapping("/api/v2/members")
    public Result membersV2() {
        List<Member> findMembers = memberService.findMembers();
        List<MemberDto> collect = findMembers.stream()
                .map(m -> new MemberDto(m.getName()))
                .collect(Collectors.toList());

        return new Result(collect.size(), collect);
    }

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private int count; // 유연하게 수정할 수 있다.
        private T data;
    }

    @Data
    @AllArgsConstructor
    static class MemberDto {
        private String name;
    }

    @PostMapping("/api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {
        // *  @Valid 검증함 > JSON 으로 온 body 를 Member 로 만들어줌
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    /*
    v1 과 v2 의 차이점?
    v1 : request class 를 안만들어도 되는데, api 스펙이 변경되면 위험한 방식이다.
    * v2 : api 스펙이 변경되면 에러로 알려줄 수 있다.
     */
    @PostMapping("api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request) {
        Member member = new Member();
        member.setName(request.getName());

        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    @PutMapping("/api/v2/members/{id}")
    public UpdateMemberResponse updateMemberV2(
            @PathVariable("id") Long id,
            @RequestBody @Valid UpdateMemberRequest request) {

        // 보통 여기서 마치거나, id 정도 반환함
        // Member를 반환하는 로직으로 만들어도 되는데, 이러면 영속성에서 벗어난 Member를 반환하는 꼴이기에
        // 다시 조회하는 방법을 사용한다.
        memberService.update(id, request.getName());
        Member findMember = memberService.findOne(id);
        return new UpdateMemberResponse(findMember.getId(), findMember.getName());
    }


    @Data
    static class CreateMemberRequest {
        // * Member 도메인을 변경하는 것이 아닌 api 스펙에서 유지보수 해야한다.
        // @NotEmpty
        private String name;
    }


    @Data
    static class CreateMemberResponse {
        private Long id;

        public CreateMemberResponse(Long id) {
            this.id = id;
        }
    }

    @Data
    static class UpdateMemberRequest {
        private String name;
    }

    @Data
    @AllArgsConstructor
    static class UpdateMemberResponse {
        private Long id;
        private String name;
    }

}
