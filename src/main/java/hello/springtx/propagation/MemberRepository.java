package hello.springtx.propagation;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;

//    @Transactional
    public void save(Member member) {
        log.info("member 저장");
        em.persist(member);
    }

    /**
     * PK로 조회하지 않고 username으로 조회하기 때문에 JPQL을 직접 사용해야함
     */
    public Optional<Member> find(String username) {
        return em.createQuery("select m from Member m where m.username = :username", Member.class)
                .setParameter("username", username)
                .getResultList().stream().findAny();
    }
}
