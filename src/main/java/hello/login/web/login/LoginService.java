package hello.login.web.login;

import hello.login.domain.member.Member;
import hello.login.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LoginService {

    private final MemberRepository memberRepository;

    public Member login(
            String loginId,
            String password
    ) {
        // 사용자 DB 에서 loginId 로 사용자 검색
        // 검색된 사용자의 password 와 입력된 password 일치여부 확인
        // 일치시 해당 사용자 리턴, 불일치시 null 리턴
        return memberRepository.findByLoginId(loginId).filter(member -> member.getPassword().equals(password)).orElse(null);
    }

}
