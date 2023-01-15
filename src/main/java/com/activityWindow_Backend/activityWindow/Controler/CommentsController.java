package com.activityWindow_Backend.activityWindow.Controler;


import com.activityWindow_Backend.activityWindow.Dto.CommentsDto;
import com.activityWindow_Backend.activityWindow.Dto.CommentsRequest;
import com.activityWindow_Backend.activityWindow.Model.Comment;
import com.activityWindow_Backend.activityWindow.Services.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/comments/")
@AllArgsConstructor
public class CommentsController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<String> createComment(@RequestParam("file") Optional<MultipartFile>  File, @RequestParam("to") String Recepeient,
                                                @RequestParam("comments") String text, @RequestParam("postId") Long postid  )throws Exception
    {


        CommentsRequest commentsrequest = new CommentsRequest(postid,text,Recepeient);

        commentService.createComment(commentsrequest, File);

        return new ResponseEntity<>("Comments Saved",
                OK);
    }

    @GetMapping("/by-post/{postId}")
    public ResponseEntity<List<CommentsDto>> getAllCommentsForPost(@PathVariable Long postId) {
        return ResponseEntity.status(OK)
                .body(commentService.getCommentByPost(postId));
    }


    @GetMapping("/download/{fileId}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileId) throws Exception {
        Comment comment = null;

        comment = commentService.getToBedownloadedFile(Long.parseLong(fileId));
        return  ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(comment.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + comment.getFileName()
                                + "\"")
                .body(new ByteArrayResource(comment.getData()));
    }



}
