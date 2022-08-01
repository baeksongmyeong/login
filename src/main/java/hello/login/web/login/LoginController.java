package hello.login.web.login;

import hello.login.domain.member.Member;
import hello.login.web.SessionConst;
import hello.login.web.session.SessionManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Iterator;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;
    private final SessionManager sessionManager;

    // 로그인 화면
    @GetMapping("/login")
    public String loginForm(
            @ModelAttribute("loginForm") LoginForm loginForm
    ) {
        return "login/loginForm";
    }

    // 로그인 처리
//    @PostMapping("/login")
//    public String loginV1(
//            @Validated @ModelAttribute("loginForm") LoginForm loginForm,
//            BindingResult bindingResult,
//            RedirectAttributes redirectAttributes,
//            HttpServletResponse response
//    ) {
//        // BindingResult 에 오류가 있으면 로그인 화면을 다시 그려서 보내줌
//        if (bindingResult.hasErrors()) {
//            log.info("login - bindingResult={}", bindingResult);
//            return "login/loginForm";
//        }
//
//        // 로그인 서비스 호출
//        Member loginMember = loginService.login(loginForm.getLoginId(), loginForm.getPassword());
//        log.info("login ? = {}", loginMember);
//
//        // 조회된 멤버가 없다면 ObjectError 로 처리 및 로그인 화면을 다시 그려서 보내줌
//        if (null == loginMember) {
//            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
//            return "login/loginForm";
//        }
//
//        // 조회된 멤버가 있다면, 해당 멤버의 로그인 ID 를 쿠키에 담아서 클라이언트에게 보내준다.
//        Cookie idCookie = new Cookie("memberId", String.valueOf(loginMember.getId()));
//        response.addCookie(idCookie);
//
//        // 첫화면으로 PRG 처리
//        return "redirect:/";
//    }

//    @PostMapping("/login")
//    public String loginV2(
//            @ModelAttribute(name = "loginForm") LoginForm loginForm,
//            BindingResult bindingResult,
//            HttpServletResponse response
//    ) {
//        // BindingResult 에 오류가 있다면, 로그인 화면을 다시 렌더링
//        if (bindingResult.hasErrors()) return "login/loginForm";
//
//        // 아이디와 암호로 사용자 조회
//        Member loginMember = loginService.login(loginForm.getLoginId(), loginForm.getPassword());
//
//        // 사용자가 없다면, 오류메시지 설정 후 로그인 화면을 다시 렌더링
//        if (loginMember == null) {
//            bindingResult.reject("loginFail","아이디 또는 암호가 유효하지 않습니다.");
//            return "login/loginForm";
//        }
//
//        // 세션 저장소에 uuid 와 로그인한 유저를 저장
//        // 쿠키에 uuid 를 저장
//        sessionManager.createSession(loginMember, response);
//
//        // 홈 컨트롤러로 리다이렉트
//        return "redirect:/";
//    }

    // 서블릿이 자동 제공하는 HttpSession 을 적용한 로그인 컨트롤러
//    @PostMapping("/login")
//    public String loginV3(
//            @ModelAttribute(name = "loginForm") LoginForm loginForm,
//            BindingResult bindingResult,
//            HttpServletRequest request
//    ) {
//        // 타입변환 오류 처리
//        if(bindingResult.hasErrors()) return "login/loginForm";
//        // 회원조회
//        Member loginMember = loginService.login(loginForm.getLoginId(), loginForm.getPassword());
//        if (loginMember == null) {
//            bindingResult.reject("loginFail", "아이디나 암호가 유효하지 않습니다.");
//            return "login/loginForm";
//        }
//        // HTTP 요청에 세션이 있으면 기존 세션 반환. 없으면 HTTP 요청에 세션 생성. 저장소를 만드는것?
//        HttpSession session = request.getSession(true);
//        // 세션에 키 ("loginMember") / 값 (loginMember) 저장
//        session.setAttribute(SessionConst.LOGIN_MEMBER, loginMember);
//
//        return "redirect:/";
//    }

    // 로그인시 요청된 URL 이 있으면 해당 URL 로 이동하는 기능 추가
    @PostMapping("/login")
    public String loginV4(
            @ModelAttribute(name = "loginForm") LoginForm loginForm,
            BindingResult bindingResult,
            @RequestParam(defaultValue = "/") String redirectURL,
            HttpServletRequest request
    ) {
        if (bindingResult.hasErrors()) return "login/loginForm";

        log.info("loginV4 - redirectURL={}", redirectURL);
        request.getParameterMap().forEach((k, d) -> log.info("key {} : data {}", k, d));

        Member member = loginService.login(loginForm.getLoginId(), loginForm.getPassword());

        if (member == null) {
            bindingResult.reject("loginFail", "아이디나 암호가 유효하지 않습니다.");
            return "login/loginForm";
        }

        HttpSession session = request.getSession(true);
        session.setAttribute(SessionConst.LOGIN_MEMBER, member);
        return "redirect:" + redirectURL;
    }



    // 로그아웃 처리
    // 로그인 시 생성해서 클라이언트에게 제공했던 쿠키를 만료시켜야 한다
    // 만료시키는 방법은 기존 쿠키명과 동일하게 쿠키를 만들고, maxAge 를 0 으로 설정하면 된다.
//    @PostMapping("/logout")
//    public String logoutV1(
//            HttpServletResponse response
//    ) {
//        Cookie cookie = new Cookie("memberId", null);
//        cookie.setMaxAge(0);
//        response.addCookie(cookie);
//        return "redirect:/";
//    }

    // 내가 만든 세션 매니저를 적용한 컨트롤러
//    @PostMapping("/logout")
//    public String logoutV2(
//            HttpServletRequest request
//    ) {
//        // 쿠키목록에서 세션과 관련된 쿠키 검색
//        // 쿠키가 없으면 처리 없음
//        // 쿠키가 있으면 해당 값으로 세션 저장소의 데이터 삭제
//        sessionManager.expire(request);
//        return "redirect:/";
//    }
    
    // 서블릿이 자동 제공하는 HttpSession 을 적용한 로그아웃 컨트롤러
    @PostMapping("logout")
    public String logoutV3(
        HttpServletRequest request
    ) {
        HttpSession session = request.getSession(false);
        if (session != null) session.invalidate();
        return "redirect:/";
    }



}
