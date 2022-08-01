package hello.login.web.filter;

import hello.login.web.SessionConst;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.PatternMatchUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Slf4j
public class LoginCheckFilter implements Filter {

    // 로그인 필터 SKIP 대상 요청 URL
    private static final String[] whitelist = {"/", "/members/add", "/login", "/logout","/css/*"};

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        String requestURI = httpServletRequest.getRequestURI();

        try {
            log.info("인증체크 필터 - 시작 {}", requestURI);

            // 로그인 필터 SKIP 대상이 아니면
            if (!PatternMatchUtils.simpleMatch(whitelist, requestURI)) {
                HttpSession session = httpServletRequest.getSession(false);
                if (session == null) {
                    log.info("인증체크 필터 - 세션이 없음. 미인증 사용자 요청 {}", requestURI);
                    httpServletResponse.sendRedirect("/login?redirectURL=" + requestURI);
                    return;
                } else {
                    if (session.getAttribute(SessionConst.LOGIN_MEMBER) == null) {
                        log.info("인증체크 필터 - 세션에 로그인 정보가 없음. 미인증 사용자 요청 {}", requestURI);
                        httpServletResponse.sendRedirect("/login?redirectURL=" + requestURI);
                        return;
                    } else {
                        log.info("인증체크 필터 - 세션에 로그인 정보가 있음. 인증 사용자 요청 {}", requestURI);
                        chain.doFilter(request, response);
                    }
                }
            } else {
                log.info("인증체크 필터 - 대상이 아님 {}", requestURI);
                chain.doFilter(request, response);
            }

        } catch ( Exception e) {
            throw e;
        } finally {
            log.info("인증체크 필터 - 종료 {}", requestURI);
        }

    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("인증 체크 필터가 초기화 됩니다.");
    }

    @Override
    public void destroy() {
        log.info("인증 체크 필터가 소멸합니다.");
    }
}
