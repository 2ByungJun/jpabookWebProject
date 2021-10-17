package jpabook.jpashop.controller;

import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {

        private final ItemService itemService;

        @GetMapping("/items/new")
        public String createForm(Model model){
            model.addAttribute("form", new BookForm()); // html 에서 추적할 수 있게 도와줌
            return "items/createItemForm";
        }

        @PostMapping("/items/new")
        public String create(BookForm form){

            // 실무에서는 setter 는 모두 날리고, 도메인에서 관리
            Book book = new Book();
            book.setName(form.getName());
            book.setPrice(form.getPrice());
            book.setStockQuantity(form.getStockQuantity());
            book.setAuthor(form.getAuthor());
            book.setIsbn(form.getIsbn());

            itemService.saveItem(book);
            return "redirect:/items";
        }

        @GetMapping("/items")
        public String list(Model model){
            List<Item> items = itemService.findItems();
            model.addAttribute("items", items);
            return "items/itemList";
        }

        @GetMapping("/items/{itemId}/edit")
        public String updateItemForm(@PathVariable("itemId") Long itemId, Model model){
            Book item = (Book) itemService.findOne(itemId);

            BookForm form = new BookForm();
            form.setId(item.getId());
            form.setName(item.getName());
            form.setPrice(item.getPrice());
            form.setStockQuantity(item.getStockQuantity());
            form.setAuthor(item.getAuthor());
            form.setIsbn(item.getIsbn());

            model.addAttribute("form", form);
            return "items/updateItemForm";
        }

        @PostMapping("/items/{itemId}/edit") // itemId는 form 에서 넘어오기에 @PathVariable 안써도 상관없다.
        public String updateItem(@PathVariable("itemId") Long itemId, @ModelAttribute("form") BookForm form){

            Book book = new Book();
            book.setId(form.getId());
            book.setName(form.getName());
            book.setPrice(form.getPrice());
            book.setStockQuantity(form.getStockQuantity());
            book.setAuthor(form.getAuthor());
            book.setIsbn(form.getIsbn());

            itemService.saveItem(book);
            return "redirect:/items";
        }
}
