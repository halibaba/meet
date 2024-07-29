package com.meet.elasticesearch.service;

import com.meet.elasticesearch.entity.Book;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

/**
 * @program: meet-boot
 * @ClassName BookService
 * @description:
 * @author: MT
 * @create: 2023-07-20 10:36
 **/
@Service
public class BookService {
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public void addBook(Book book) {
        // Save the book to Elasticsearch using the bookRepository
        bookRepository.save(book);
    }

    public List<Book> searchBook(String keyword) {
        // Search for books by title or author using the bookRepository
        return bookRepository.findByTitleOrAuthor(keyword, keyword);
    }
}