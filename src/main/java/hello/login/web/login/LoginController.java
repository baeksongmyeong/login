package hello.login.web.login;

import hello.login.domain.member.Member;
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

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Controller
@RequiredArgsConstructor
public class LoginController {

    private final LoginService loginService;

    // 로그인 화면
    @GetMapping("/login")
    public String loginForm(
            @ModelAttribute("loginForm") LoginForm loginForm
    ) {
        return "login/loginForm";
    }

    // 로그인 처리
    @PostMapping("/login")
    public String login(
            @Validated @ModelAttribute("loginForm") LoginForm loginForm,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            HttpServletResponse response
    ) {
        // BindingResult 에 오류가 있으면 로그인 화면을 다시 그려서 보내줌
        if (bindingResult.hasErrors()) {
            log.info("login - bindingResult={}", bindingResult);
            return "login/loginForm";
        }
        
        // 로그인 서비스 호출
        Member loginMember = loginService.login(loginForm.getLoginId(), loginForm.getPassword());
        log.info("login ? = {}", loginMember);
        
        // 조회된 멤버가 없다면 ObjectError 로 처리 및 로그인 화면을 다시 그려서 보내줌
        if (null == loginMember) {
            bindingResult.reject("loginFail", "아이디 또는 비밀번호가 맞지 않습니다.");
            return "login/loginForm";
        }

        // 조회된 멤버가 있다면, 해당 멤버의 로그인 ID 를 쿠키에 담아서 클라이언트에게 보내준다.
        Cookie idCookie = new Cookie("memberId", String.valueOf(loginMember.getId()));
        response.addCookie(idCookie);

        // 첫화면으로 PRG 처리
        return "redirect:/";
    }

    // 로그아웃 처리
    // 로그인 시 생성해서 클라이언트에게 제공했던 쿠키를 만료시켜야 한다
    // 만료시키는 방법은 기존 쿠키명과 동일하게 쿠키를 만들고, maxAge 를 0 으로 설정하면 된다.
    @PostMapping("/logout")
    public String logout(
            HttpServletResponse response
    ) {
        Cookie cookie = new Cookie("memberId", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/";
    }
}
