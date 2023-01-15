package com.activityWindow_Backend.activityWindow.Controler;


import com.activityWindow_Backend.activityWindow.Dto.PostRequest;
import com.activityWindow_Backend.activityWindow.Dto.PostResponse;
import com.activityWindow_Backend.activityWindow.Services.PostService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/api/posts/")
@AllArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostResponse> createPost(@RequestBody PostRequest postRequest) {
        return status(HttpStatus.OK).body(postService.save(postRequest));
    }

    @PostMapping("delete/")
    public ResponseEntity<String> deletePost(@RequestBody List<Long> id) {
        postService.deletePost(id);
        return ResponseEntity.status(OK).body("Deleted Successfully!!");
    }


    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPost(@PathVariable("id") Long id) {
        return status(HttpStatus.OK).body(postService.getPost(id));
    }


    @GetMapping("by-user/{name}")
    public ResponseEntity<List<PostResponse>> getPostsByUsername(@PathVariable("name") String username) {
        return status(HttpStatus.OK).body(postService.getPostsByUsername(username));
    }

    @GetMapping("by-Product/{Product}")
    public ResponseEntity<List<PostResponse>> getPostsByProduct(@PathVariable("Product") String Product) {
        return status(HttpStatus.OK).body(postService.getPostsByProduct(Product));

    }

    @GetMapping("by-keyword/{keyword}/{Product}")
    public ResponseEntity<List<PostResponse>> getPostsByKeyword(@PathVariable("keyword") String keyword, @PathVariable("Product") String Product) {
        return status(HttpStatus.OK).body(postService.getPostsByKeyword(keyword,Product));
    }

}
