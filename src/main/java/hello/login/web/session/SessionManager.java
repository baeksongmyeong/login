package hello.login.web.session;

import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class SessionManager {

    // 쿠키 이름
    public static final String SESSION_COOKIE_NAME = "mySessionId";

    // 세션 저장소
    private static Map<String, Object> sessionStroe = new ConcurrentHashMap<>();

    // 세션 생성 및 쿠키 등록
    public void createSession(
            Object value,
            HttpServletResponse httpServletResponse
    ) {
        // 세션 저장소에 저장할 키 생성
        String sessionId = UUID.randomUUID().toString();

        // 세션 저장소에 키, 값 저장
        sessionStroe.put(sessionId, value);

        // 쿠키 생성 및 HTTP 응답에 추가
        Cookie mySessionCookie = new Cookie(SESSION_COOKIE_NAME, sessionId);
        httpServletResponse.addCookie(mySessionCookie);
    }

    // 세션으로 저장소 조회
    public Object getSession(
            HttpServletRequest request
    ) {
        // HTTP 요청에서 "mySessionId" 이름의 쿠키 검색
        Cookie foundCookie = findCookie(request, SESSION_COOKIE_NAME);

        // 쿠키 검색결과가 없으면 null 리턴
        if (foundCookie == null) return null;

        // 쿠키에 값으로 세션 저장소에 저장된 정보 조회
        return sessionStroe.get(foundCookie.getValue());
    }

    // 세션 만료 - 세션 저장소에서 삭제
    public void expire(
            HttpServletRequest request
    ) {
        // HTTP 요청에서 "mySessionId" 이름의 쿠키 검색
        Cookie foundCookie = findCookie(request, SESSION_COOKIE_NAME);

        // 쿠키 검색결과가 없으면 처리없음
        if (foundCookie == null) return;

        // 세션과 연결되는 key 로 세션 저장소 데이터 삭제
        sessionStroe.remove(foundCookie.getValue());
    }

    // HTTP 요청에서 특정 이름의 쿠키를 찾는다.
    // 없으면 null 을 반환한다.
    private Cookie findCookie(
            HttpServletRequest request,
            String cookieName
    ) {
        // HTTP 요청의 전체 쿠키 조회
        Cookie[] cookies = request.getCookies();

        // 쿠키가 하나도 없으면 null 리턴
        if (cookies == null) return null;

        // 특정 이름의 쿠키 찾기
        return Arrays.stream(cookies).filter(cookie -> cookie.getName().equals(cookieName)).findAny().orElse(null);
    }
}
