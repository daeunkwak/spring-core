package hello.core.order;

import hello.core.discount.DiscountPolicy;
import hello.core.discount.FixDiscountPolicy;
import hello.core.discount.RateDiscountPolicy;
import hello.core.member.Member;
import hello.core.member.MemberRepository;
import hello.core.member.MemoryMemberRepository;

public class OrderServiceImpl implements OrderService {

    private final MemberRepository memberRepository;
    private final DiscountPolicy discountPolicy;

    //private final MemberRepository memberRepository = new MemoryMemberRepository();

    // DIP 위반 : 추상(인터페이스)에만 의존해야하는데 구체(구현)에도 의존하고 있음
    //private final DiscountPolicy discountPolicy = new FixDiscountPolicy();

    // 이렇게 하면 구현체 의존은 없지만 NPE 발생 (구현체 없음)
    //private DiscountPolicy discountPolicy;

    //    private final DiscountPolicy discountPolicy = new FixDiscountPolicy();

    // 할인 정책을 변경하려면 클라이언트인 `OrderServiceImpl` 코드를 고쳐야 한다. -> OCP 위반
    //private final DiscountPolicy discountPolicy = new RateDiscountPolicy();

    // 생성자 주입
    public OrderServiceImpl(MemberRepository memberRepository, DiscountPolicy
            discountPolicy) {
        this.memberRepository = memberRepository;
        this.discountPolicy = discountPolicy;
    }

    @Override
    public Order createOrder(Long memberId, String itemName, int itemPrice) {
        Member member = memberRepository.findById(memberId);
        int discountPrice = discountPolicy.discount(member, itemPrice);
        return new Order(memberId, itemName, itemPrice, discountPrice);
    }
}
