package hello.login.web;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberRepository memberRepository;

    // 로그인 결과 쿠키 적용전 홈 화면 컨트롤러
    //@GetMapping("/")
    public String home() {
        return "home";
    }

    // 로그인 결과 쿠키 적용후 홈 화면 컨트롤러
    @GetMapping("/")
    public String home(
        @CookieValue(name = "memberId", required = false) Long memberId,
        Model model
    ) {
        // 로그인 전에는 쿠키가 없음. 이에 대한 처리
        if (memberId == null) {
            return "home";
        }

        // 로그인 후에는 쿠키가 있음. 이에 대한 처리
        Member loginMember = memberRepository.findById(memberId);

        // 사용자가 삭제된 경우에 대한 처리
        if (loginMember == null) {
            return "home";
        }

        // 사용자 정보를 모델에 담아서 View 에 전달
        model.addAttribute("member", loginMember);

        return "loginHome";
    }
}