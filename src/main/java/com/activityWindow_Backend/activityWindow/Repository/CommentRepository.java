package com.activityWindow_Backend.activityWindow.Repository;


import com.activityWindow_Backend.activityWindow.Model.Comment;
import com.activityWindow_Backend.activityWindow.Model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository  extends JpaRepository<Comment, Long> {

    Optional<Comment> findById(Long id);

    long deleteByPost(Post post);

    List<Comment> findByPost(Post post);


}
