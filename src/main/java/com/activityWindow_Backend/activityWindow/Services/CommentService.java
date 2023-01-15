package com.activityWindow_Backend.activityWindow.Services;


import com.activityWindow_Backend.activityWindow.Dto.CommentsDto;
import com.activityWindow_Backend.activityWindow.Dto.CommentsRequest;
import com.activityWindow_Backend.activityWindow.Mapper.CommentMapper;
import com.activityWindow_Backend.activityWindow.Model.Comment;
import com.activityWindow_Backend.activityWindow.Model.Post;
import com.activityWindow_Backend.activityWindow.Model.User;
import com.activityWindow_Backend.activityWindow.Repository.CommentRepository;
import com.activityWindow_Backend.activityWindow.Repository.PostRepository;
import com.activityWindow_Backend.activityWindow.Repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
@Transactional
public class CommentService {



    private final CommentMapper commentMapper;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final MailService mailService;

    public void createComment(CommentsRequest commentsrequest, Optional<MultipartFile> File) throws Exception {


        Post post = postRepository.findByPostId(commentsrequest.getPostId())
                                      .orElseThrow(() -> new RuntimeException(commentsrequest.getPostId().toString() + "post not availavle"));

        String fileName = null;
        String fileType = null;
        byte[] data =null;

        try {
            if(File.isPresent()) {
                fileName = StringUtils.cleanPath(File.get().getOriginalFilename());
                fileType = File.get().getContentType();
                data = File.get().getBytes();
            }

            Comment comment = commentMapper.map(commentsrequest.getText(), post, authService.getCurrentUser(), fileName, fileType, data );
            commentRepository.save(comment);

        }

        catch (Exception e) {
            throw new Exception("Could not get data from File");
        }



        //mailservice

        String to= commentsrequest.getRecipient();
        User CommentedUser = authService.getCurrentUser();
        mailService.send(to, CommentedUser, post);

    }

    public List<CommentsDto> getCommentByPost(Long postId) {
        Post post = postRepository.findByPostId(postId)
                .orElseThrow(() -> new RuntimeException(postId.toString() + "post not availavle"));
        return commentRepository.findByPost(post)
                .stream()
                .map(commentMapper::mapToDto)
                .collect(toList());
    }


    public Comment getToBedownloadedFile(Long fileId) throws Exception {
        return commentRepository
                .findById(fileId)
                .orElseThrow(() -> new RuntimeException("File not found "));

    }


}
