package com.meet.elasticesearch.controller;

import com.meet.elasticesearch.entity.Book;
import com.meet.elasticesearch.service.BookService;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @program: meet-boot
 * @ClassName BookController
 * @description:
 * @author: MT
 * @create: 2023-07-20 10:37
 **/
@RestController
public class BookController {
    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping("/book")
    public Map<String, String> addBook(@RequestBody Book book) {

        bookService.addBook(book);
        Map<String, String> map = new HashMap<>();
        map.put("msg", "ok");
        return map;
    }

    @GetMapping("/book/search")
    public List<Book> searchBook(String key) {
        return bookService.searchBook(key);
    }
}