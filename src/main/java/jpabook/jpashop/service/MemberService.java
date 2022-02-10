package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

//    @Autowired
    private final MemberRepository memberRepository;

    // = @AllArgsConstructor : 모든 필드에 대해서 생성자 지정
    // = @RequiredArgsConstructor : final 필드에 대해서 생성자 지정

    /**
     * 회원 등록, id반환
     */
    @Transactional //데이터 변경을 위한 트랜잭션 생성(readOnly = false)
    public Long join(Member member) {
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        //중복회원이라면 EXCEPTION 발생시킴
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if(!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    /**
     * 회원 조회
     */
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }

    /**
     * 로그인 기능
     */
    public boolean signIn(Member member) {
        List<Member> findMember = memberRepository.findByLoginId(member.getLoginId());
        if(findMember.isEmpty())
            return false;
        else if(findMember.get(0).getLoginPw().equals(member.getLoginPw()))
            return true;
        return false;
    }
}
