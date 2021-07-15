package hello.servlet.domain.member;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

class MemberRepositoryTest {

    MemberRepository memberRepository = MemberRepository.getInstance();

    @AfterEach
    void afterEach() {
        memberRepository.clearStore();
    }

    @Test
    void save() {
        // given
        final Member member = new Member("hello", 20);

        // when
        final Member savedMember = memberRepository.save(member);

        // then
        final Member findMember = memberRepository.findById(savedMember.getId());
        assertThat(findMember).isEqualTo(savedMember);
    }

    @Test
    void findAll() {
        // given
        final Member member1 = new Member("member1", 20);
        final Member member2 = new Member("member2", 30);
        final Member member3 = new Member("member3", 40);

        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);

        // when
        final List<Member> result = memberRepository.findAll();

        // then
        assertThat(result.size()).isEqualTo(3);
        assertThat(result).contains(member1, member2, member3);
    }

}
