package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    @Transactional //DB의 상태를 변경하는 작업. 자동으로 commit, flush를 통해서 update쿼리가 날아감
    public void updateItem(UpdateItemDTO itemDTO) {

        Book findItem = (Book)itemRepository.findOne(itemDTO.getId()); //영속상태 조회(트랜잭션이 있는 서비스 계층에서 조회)
        //update할 필드만 가지고 수정
        //setter를 이용해서 바꾸기보다는 별도의 메소드를 이용하는 것이 좋음
        findItem.setName(itemDTO.getName());
        findItem.setPrice(itemDTO.getPrice());
        findItem.setStockQuantity(itemDTO.getStockQuantity());
        findItem.setAuthor(itemDTO.getAuthor());
        findItem.setIsbn(itemDTO.getIsbn());
//        itemRepository.save(findItem); > 영속상태이기때문에 merge가 불필요
    }

    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }
}
