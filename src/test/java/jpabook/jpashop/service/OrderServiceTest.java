package jpabook.jpashop.service;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;


@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class OrderServiceTest {

    @Autowired EntityManager em;
    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;

    @Test
    public void 상품주문() throws Exception {
        //given
        Member member = createMember();
        Item book = createBook("JPA", 10000, 10);

        int orderCount = 2;

        //when
        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        //then
        Order getOrder = orderRepository.findOne(orderId);

        //주문 상태 확인
        Assertions.assertEquals(OrderStatus.ORDER, getOrder.getStatus());
        //주문 개수 확인
        Assertions.assertEquals(1, getOrder.getOrderItems().size());
        //주문 가격
        Assertions.assertEquals(10000 * orderCount, getOrder.getTotalPrice());
        //주문 수량 감소
        Assertions.assertEquals(8, book.getStockQuantity());

    }



    @Test(expected = NotEnoughStockException.class)
    public void 상품주문_재고수량초과() throws Exception {
        Member member = createMember();
        Item book = createBook("JPA", 10000, 10);

        int orderCount = 12;

        orderService.order(member.getId(), book.getId(), orderCount);

        fail("재고 부족 에러가 발생해야 한다.");

    }

    @Test
    public void 주문취소() throws Exception {
        Member member = createMember();
        Item book = createBook("JPA", 10000, 10);

        int orderCount = 2;

        Long orderId = orderService.order(member.getId(), book.getId(), orderCount);

        //when
        orderService.cancel(orderId);

        //then
        Order getOrder = orderRepository.findOne(orderId);

        assertEquals(OrderStatus.CANCEL, getOrder.getStatus()); //상태검증
        assertEquals(10, book.getStockQuantity()); //숫자검증

    }


    private Item createBook(String name, int orderPrice, int count) {
        Item book = new Book();
        book.setName(name);
        book.setPrice(orderPrice);
        book.setStockQuantity(count);
        em.persist(book);
        return book;
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울", "경기", "123-123"));
        em.persist(member);
        return member;
    }
}
