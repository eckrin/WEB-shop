package jpabook.jpashop.controller;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
@RequiredArgsConstructor
public class LoginController {

    private final MemberService memberService;

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("memberForm", new MemberForm());
        return "login/signInForm";
    }

    @PostMapping("/login")
    public String login(MemberForm form) {

        Member member = new Member();
        member.setLoginId(form.getLoginId());
        member.setLoginPassword(form.getLoginPw());

        memberService.join(member);

        return "login/signInForm";
    }
}
