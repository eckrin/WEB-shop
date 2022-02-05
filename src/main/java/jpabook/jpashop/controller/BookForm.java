package jpabook.jpashop.controller;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

@Getter @Setter
public class BookForm {

    private Long id; //상품은 수정기능을위해 id 필요

    private String name;
    private int price;
    private int stockQuantity;

    private String author;
    private String isbn;

}
