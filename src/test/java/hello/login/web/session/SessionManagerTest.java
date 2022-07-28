package hello.login.web.session;

import hello.login.domain.member.Member;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class SessionManagerTest {

    SessionManager sessionManager = new SessionManager();

    @Test
    @DisplayName("Mock 테스트 - 세션저장소")
    void sessionTest() {

        // 로그인이 되었음을 가정하고, 세션 저장소가 생성한 세션 ID 가 쿠키로 내려오는지 확인
        Member member = new Member();
        MockHttpServletResponse response = new MockHttpServletResponse();
        sessionManager.createSession(member, response);

        // 로그인 후, HTTP 요청을 하는것으로 가정. 위에서 생성한 쿠키를 요청에 추가. 그 후 세션 저장소를 조회
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setCookies(response.getCookies());
        Object findSession = sessionManager.getSession(request);
        Assertions.assertThat(findSession).isEqualTo(member);

        // 세션 종료 테스트 - 세션 저장소에서 삭제
        sessionManager.expire(request);
        Object expiredSession = sessionManager.getSession(request);
        Assertions.assertThat(expiredSession).isNull();


    }

}