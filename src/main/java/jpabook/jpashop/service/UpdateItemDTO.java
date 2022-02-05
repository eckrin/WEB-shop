package jpabook.jpashop.service;

import jpabook.jpashop.controller.BookForm;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UpdateItemDTO {
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    private String author;
    private String isbn;

    public UpdateItemDTO(BookForm form) {
        this.id = form.getId();
        this.name = form.getName();
        this.price = form.getPrice();
        this.stockQuantity = form.getStockQuantity();
        this.author = form.getAuthor();
        this.isbn = form.getIsbn();
    }
}
