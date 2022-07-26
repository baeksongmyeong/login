package hello.login.web.member;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequestMapping("/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

    // 회원가입폼
    @GetMapping("/add")
    public String addForm(
            @ModelAttribute(name = "member") Member member
    ) {
        return "members/addMemberForm";
    }

    // 회원가입
    @PostMapping("/add")
    public String save(
            @Validated @ModelAttribute(name = "member") Member member,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            log.info("save - bindingResult={}", bindingResult);
            return "members/addMemberForm";
        }

        Member savedMember = memberRepository.save(member);

        redirectAttributes.addAttribute("id", savedMember.getId());
        redirectAttributes.addAttribute("status", true);

        return "redirect:/";
    }
}
