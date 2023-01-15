package com.activityWindow_Backend.activityWindow.Mapper;


import com.activityWindow_Backend.activityWindow.Dto.PostRequest;
import com.activityWindow_Backend.activityWindow.Dto.PostResponse;
import com.activityWindow_Backend.activityWindow.Model.Post;
import com.activityWindow_Backend.activityWindow.Model.User;
import com.activityWindow_Backend.activityWindow.Repository.CommentRepository;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostMapper {

    @Autowired
    private CommentRepository commentRepository;
    public Post map(PostRequest postRequest, User user){
       Post post = new Post();
       post.setPostName(postRequest.getPostName());
       post.setSegment(postRequest.getSegment());
       post.setProduct(postRequest.getProduct());
       post.setDescription(postRequest.getDescription());
       post.setUser(user);
       post.setUrl(postRequest.getUrl());
       post.setCreatedDate(java.time.Instant.now());

       return post;

    }

    public PostResponse mapToDto(Post post){

        PostResponse postResponse = new PostResponse();
        postResponse.setPostName(post.getPostName());
        postResponse.setId(post.getPostId());
        postResponse.setSegment(post.getSegment());
        postResponse.setUrl(post.getUrl());
        postResponse.setDescription(post.getDescription());
        postResponse.setProduct(post.getProduct());
        postResponse.setUserName(post.getUser().getUsername());
        postResponse.setDuration(TimeAgo.using(post.getCreatedDate().toEpochMilli()));
        postResponse.setCommentCount(commentRepository.findByPost(post).size());

        return postResponse;

    }
}
