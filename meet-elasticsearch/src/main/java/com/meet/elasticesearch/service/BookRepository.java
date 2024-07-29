package com.meet.elasticesearch.service;

import com.meet.elasticesearch.entity.Book;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;


import java.util.List;

/**
 * 关系型数据库mysql Repository
 *
 * @author mt
 */
public interface BookRepository extends ElasticsearchRepository<Book, String> {

    List<Book> findByTitleOrAuthor(String title, String author);

}