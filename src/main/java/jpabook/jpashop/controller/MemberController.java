package jpabook.jpashop.controller;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/members/new")
    //Model:addAttribute를 통해서 컨트롤러에서 뷰로 넘어갈때 데이터 전달
    public String createForm(Model model) {
        model.addAttribute("memberForm", new MemberForm());
        return "members/createMemberForm";
    }

    @PostMapping("/members/new")
    //@Valid:필수적으로 넣어야하는것 명시
    //result에 hasErrors여도 form의 data는 유지된다.
    //굳이 Member엔티티가 아니라 form이나 DTO를 새로 만드는이유 : 두개가 정확하게 일치X
    //엔티티는 최대한 핵심 비지니스 로직에만 dependency가 존재하도록 설계하는 것이 중요하다.
   public String create(@Valid MemberForm form, BindingResult result) {

        if(result.hasErrors())
            return "members/createMemberForm";

        Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());

        Member member = new Member();
        member.setName(form.getName());
        member.setAddress(address);

        try {
            memberService.join(member);
        } catch (IllegalStateException e) {
            result.addError(new FieldError("memberForm", "name", e.getMessage()));
            return "members/createMemberForm";
        }
        return "redirect:/";
    }

    @GetMapping("/members")
    //엔티티를 손대지 않고 그대로 사용할 수 있는 상황이기때문에 엔티티를 그대로 이용
    public String list(Model model) {
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);
        return "members/memberList";
    }

}
