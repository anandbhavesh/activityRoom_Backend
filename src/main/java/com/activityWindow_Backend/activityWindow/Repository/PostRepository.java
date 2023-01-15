package com.activityWindow_Backend.activityWindow.Repository;


import com.activityWindow_Backend.activityWindow.Model.Post;
import com.activityWindow_Backend.activityWindow.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Optional<Post>  findByPostId(Long postId);



    List<Post> findByUser(User user);

    List<Post> findByProduct(String Product);

    List<Post> findByPostNameContaining(String PostName);

    List<Post> findBySegment(String segment);

    List<Post> findBySegmentAndProduct(String segment, String Product );

    @Query("SELECT p FROM Post p WHERE p.postName LIKE CONCAT('%', :finalSearchValue,'%') AND p.segment=:segment AND p.product=:Product")
    List<Post> findByPostNameContainingAndSegmentAndProduct(@Param("finalSearchValue") String finalSearchValue, @Param("segment") String segment, @Param("Product") String Product );


    @Query("SELECT p FROM Post p WHERE p.postName LIKE CONCAT('%', :finalSearchValue,'%') AND p.segment=:segment")
    List<Post> findByPostNameContainingAndSegment(@Param("finalSearchValue") String finalSearchValue, @Param("segment") String segment);

    @Query("SELECT p FROM Post p WHERE p.postName LIKE CONCAT('%', :finalSearchValue,'%') AND p.product=:Product")
    List<Post> findByPostNameContainingAndProduct(@Param("finalSearchValue") String finalSearchValue,@Param("Product") String Product );





}
