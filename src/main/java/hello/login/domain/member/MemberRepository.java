package hello.login.domain.member;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.*;

@Slf4j
@Repository
public class MemberRepository {

    private static Map<Long, Member> store = new HashMap<>();
    private static Long sequence = 0L;

    // 회원 신규 등록
    public Member save(Member member) {
        member.setId(++sequence);
        log.info("save - member={}", member);
        store.put(member.getId(), member);
        return member;
    }

    // 회원 번호로 회원 찾기
    public Member findById(Long id) {
        return store.get(id);
    }

    // 회원 아이디로 회원 찾기
    public Optional<Member> findByLoginId(String loginId) {
        // Map 의 Value 인 Member 객체 내 loginId 필드를 꺼내 값을 비교해야 함
        return findAll().stream().filter(member -> member.getLoginId().equals(loginId)).findFirst();
    }

    public List<Member> findAll() {
        return new ArrayList<>(store.values());
    }

    public void clearStore() {
        store.clear();
    }

}
