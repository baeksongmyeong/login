package hello.login.web;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import hello.login.web.argumentresolver.Login;
import hello.login.web.session.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final MemberRepository memberRepository;
    private final SessionManager sessionManager;

    // 로그인 결과 쿠키 적용전 홈 화면 컨트롤러
//    @GetMapping("/")
//    public String homeV1() {
//        return "home";
//    }

    // 로그인 결과 쿠키 적용후 홈 화면 컨트롤러
//    @GetMapping("/")
//    public String homeV2(
//        @CookieValue(name = "memberId", required = false) Long memberId,
//        Model model
//    ) {
//        // 로그인 전에는 쿠키가 없음. 이에 대한 처리
//        if (memberId == null) {
//            return "home";
//        }
//
//        // 로그인 후에는 쿠키가 있음. 이에 대한 처리
//        Member loginMember = memberRepository.findById(memberId);
//
//        // 사용자가 삭제된 경우에 대한 처리
//        if (loginMember == null) {
//            return "home";
//        }
//
//        // 사용자 정보를 모델에 담아서 View 에 전달
//        model.addAttribute("member", loginMember);
//
//        return "loginHome";
//    }
    
    // 내가 만든 세션 매니저를 적용한 홈 화면 컨트롤러
//    @GetMapping
//    public String homeV3(
//        HttpServletRequest request,
//        Model model
//    ) {
//        // HTTP 요청 내부에 쿠키 중 세션과 관련된 것을 찾아서 값(UUID) 을 가져온다. - getSession 이 처리
//        // 없으면 로그인 할 수 없는 상태인 것이다. - getSession 이 처리
//        // 있으면 세션 저장소에서 해당 UUID 와 연결되는 사용자를 조회한다.  - getSession 이 처리
//        //     없으면 로그인 할 수 없는 상태인 것이다.
//        //     있으면 해당 사용자를 화면에 전달한다.
//        Object session = sessionManager.getSession(request);
//        if (session == null) return "home";
//        model.addAttribute("member", (Member) session);
//        return "loginHome";
//    }

    // 서블릿이 자동 제공하는 HttpSession 을 적용한 홈 컨트롤러
//    @GetMapping
//    public String homeV4(
//            HttpServletRequest request,
//            Model model
//    ) {
//        // 세션 확인
//        HttpSession session = request.getSession(false);
//        // 세션이 없으면 로그아웃상태
//        if (session == null) return "home";
//        // 세션이 있으면 회원정보 확인
//        Member loginMember = (Member) session.getAttribute(SessionConst.LOGIN_MEMBER);
//        // 세션에 회원정보가 없으면 로그아웃 상태
//        if (loginMember == null) return "home";
//        // 회원정보를 모델에 저장
//        model.addAttribute("member", loginMember);
//        return "loginHome";
//    }

    // 스프링이 제공하는 세션을 편리하게 사용할 수 있는 @SessionAttribute 어노테이션 적용 컨트롤러
//    @GetMapping("/")
//    public String homeV5(
//            @SessionAttribute(name = SessionConst.LOGIN_MEMBER, required = false) Member loginMember,
//            Model model
//    ) {
//        if (loginMember == null) return "home";
//        model.addAttribute("member", loginMember);
//        return "loginHome";
//    }

    @GetMapping("/")
    public String homeLoginV3ArgumentResolver(
            @Login Member loginMember,
            Model model
    ) {
        if (loginMember == null) return "home";
        model.addAttribute("member", loginMember);
        return "loginHome";
    }
}