package com.activityWindow_Backend.activityWindow.Mapper;


import com.activityWindow_Backend.activityWindow.Dto.CommentsDto;
import com.activityWindow_Backend.activityWindow.Model.Comment;
import com.activityWindow_Backend.activityWindow.Model.Post;
import com.activityWindow_Backend.activityWindow.Model.User;
import com.github.marlonlom.utilities.timeago.TimeAgo;
import org.springframework.stereotype.Service;

@Service
public class CommentMapper {
    public Comment map(String text, Post post, User user, String fileName, String fileType, byte[] data){
        Comment comment = new Comment();
        comment.setData(data);
        comment.setFileName(fileName);
        comment.setFileType(fileType);
        comment.setUser(user);
        comment.setPost(post);
        comment.setText(text);
        comment.setCreatedDate(java.time.Instant.now());

        return comment;

    }


    public CommentsDto mapToDto(Comment comment){

        CommentsDto commentResponse = new CommentsDto();
        commentResponse.setId(comment.getId());
        commentResponse.setText(comment.getText());
        commentResponse.setUserName(comment.getUser().getUsername());
        commentResponse.setPostId(comment.getPost().getPostId());
        commentResponse.setDuration(TimeAgo.using(comment.getCreatedDate().toEpochMilli()));
        commentResponse.setFilename(comment.getFileName());

        return commentResponse;

    }

}
