package hello.login.web.session;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;

@Slf4j
@RestController
public class SessionInfoController {

    @GetMapping("/session-info")
    public String sessionInfo(
            HttpServletRequest request
    ) {
        HttpSession oldSession = request.getSession(false);
        if (oldSession == null) return "세션이 없습니다.";

        oldSession.getAttributeNames().asIterator()
                .forEachRemaining(name -> log.info("session name={}, value={}",
                        name, oldSession.getAttribute(name)));

        log.info("sessionId={}", oldSession.getId());
        log.info("maxInactiveInterval={}", oldSession.getMaxInactiveInterval());
        log.info("creationTime={}", new Date(oldSession.getCreationTime()));
        log.info("lastAccessedTime={}", new Date(oldSession.getLastAccessedTime()));
        log.info("isNew={}", oldSession.isNew());

        return "세션 출력";

    }
}
