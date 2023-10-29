package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findCommentsByItem(Item item);

    @Query("SELECT c FROM Comment AS c WHERE c.item IN ?1")
    List<Comment> findCommentsByItems(List<Item> items);
}
