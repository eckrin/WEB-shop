package jpabook.jpashop.domain;


import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="orders")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {

    @Id @GeneratedValue
    @Column(name="order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL) //영속성 전이
    private List<OrderItem> orderItems = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status; // 주문상태

    //연관관계 편의 메소드, 양방향 세팅
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    //생성 메서드 - 여러가지 연관관계로 인해 복잡한 경우 생성메서드 별도로 만들기//
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) {
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for(OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    //비지니스 로직//
    /**
     * 주문 취소
     */
    public void cancel() {
        if(delivery.getStatus() == DeliveryStatus.COMP) //배송이 완료되면
            throw new IllegalStateException("이미 배송 완료");
        this.setStatus(OrderStatus.CANCEL); //별도의 로직을 만들지 않아도 JPA가 상태를 바꾸어준다
        for (OrderItem orderItem : this.orderItems) {
            orderItem.cancel();
        }
    }

    //조회 로직//
    /**
     * 전체 주문가격 조회
     */
    public int getTotalPrice() {
        int totalPrice = 0;
        for(OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
//       = return orderItems.stream().mapToInt(OrderItem::getTotalPrice).sum();
    }


}
