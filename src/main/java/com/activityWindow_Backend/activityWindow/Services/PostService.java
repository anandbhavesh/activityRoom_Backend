package com.activityWindow_Backend.activityWindow.Services;


import com.activityWindow_Backend.activityWindow.Dto.PostRequest;
import com.activityWindow_Backend.activityWindow.Dto.PostResponse;
import com.activityWindow_Backend.activityWindow.Mapper.PostMapper;
import com.activityWindow_Backend.activityWindow.Model.Post;
import com.activityWindow_Backend.activityWindow.Model.User;
import com.activityWindow_Backend.activityWindow.Repository.CommentRepository;
import com.activityWindow_Backend.activityWindow.Repository.PostRepository;
import com.activityWindow_Backend.activityWindow.Repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final PostMapper postMapper;
    private final CommentRepository commentRepository;

    public PostResponse save(PostRequest postRequest) {

        Post post = postRepository.save(postMapper.map(postRequest, authService.getCurrentUser()));

        return postMapper.mapToDto(post);
    }


    public String deletePost(List<Long> id){
        for(Long no: id) {
            Post  post = postRepository.findByPostId(no)
                    .orElseThrow(() -> new RuntimeException("post id not available"));
            commentRepository.deleteByPost(post);
            postRepository.deleteById(no);

        }
        return "deleted sucessfully";
    }


    public PostResponse getPost(Long id) {

         Post  post = postRepository.findByPostId(id)
                    .orElseThrow(() -> new RuntimeException("post id not available"));

        return postMapper.mapToDto(post);
    }



    public List<PostResponse> getPostsByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("username not available"));
        return postRepository.findByUser(user)
                .stream()
                .map(postMapper::mapToDto)
                .collect(toList());
    }



    public List<PostResponse> getPostsByProduct(String Product) {

        return postRepository.findByProduct(Product)
                .stream()
                .map(postMapper::mapToDto)
                .collect(toList());
    }



    public List<PostResponse> getPostsByKeyword(String keyword, String product)  {


        String initialSearchValue = keyword.toUpperCase();

        // ActivityName is ECO no or ISS no here
        String ActivityName="null";
        String segment="null";
        String finalSearchValue ="";
        int h=0;
        String[] arr = initialSearchValue.split(" ");

        for(String k:arr) {

            if(k.contains("ACTY")) {
                ActivityName=k;
                h=1;
            }

            if(k.contains("S1")|| k.contains("S2")|| k.contains("S3")|| k.contains("S4")|| k.contains("S5")||
                    k.contains("S6")|| k.contains("S7") || k.contains("S8") || k.contains("S9"))
            {
                segment=k;
                h=1;
            }

            if(h==0)
                finalSearchValue =  finalSearchValue + " " + k ;
            h=0;

        }

        if(finalSearchValue.length()>0) {
            finalSearchValue = finalSearchValue.substring(1);
            System.out.println(finalSearchValue + " " + finalSearchValue.length() + "  " + segment + "  " + segment.length());
            System.out.println();
        }


        if(ActivityName!="null"){
            List<Post> listwithActivityname = postRepository.findByPostNameContaining(ActivityName);

            if(listwithActivityname.size()>0)
                return listwithActivityname.stream()
                        .map(postMapper::mapToDto)
                        .collect(toList());
        }


        if(finalSearchValue.length()>0){

            if(segment!="null"){

                if(product.equals("null")) {
                    List<Post> listwithDescAndSegment = postRepository.findByPostNameContainingAndSegment(finalSearchValue, segment);

                    if (listwithDescAndSegment.size() > 0)
                        return listwithDescAndSegment.stream()
                                .map(postMapper::mapToDto)
                                .collect(toList());

                }
                else {
                    List<Post> listwithSegmentAndProductAndDesc =  postRepository.findByPostNameContainingAndSegmentAndProduct(finalSearchValue,segment, product);

                    if(listwithSegmentAndProductAndDesc.size()>0)
                        return listwithSegmentAndProductAndDesc.stream()
                                .map(postMapper::mapToDto)
                                .collect(toList());

                }
            }
            else {
                if(product.equals("null")){
                    List<Post> listwithDesc =  postRepository.findByPostNameContaining(finalSearchValue);

                    if(listwithDesc.size()>0)
                        return listwithDesc.stream()
                                .map(postMapper::mapToDto)
                                .collect(toList());

                }
                else{
                    List<Post> listwithProductAndDesc =  postRepository.findByPostNameContainingAndProduct(finalSearchValue, product);

                    if(listwithProductAndDesc.size()>0)
                        return listwithProductAndDesc.stream()
                                .map(postMapper::mapToDto)
                                .collect(toList());

                }

            }

        }
        else{
            if(product.equals("null")){
                List<Post> listwithSegment = postRepository.findBySegment(segment);

                if(listwithSegment.size()>0)
                    return listwithSegment.stream()
                            .map(postMapper::mapToDto)
                            .collect(toList());
            }
            else{
                List<Post> listwithSegmentAndProduct = postRepository.findBySegmentAndProduct(segment, product);

                if(listwithSegmentAndProduct.size()>0)
                    return listwithSegmentAndProduct.stream()
                            .map(postMapper::mapToDto)
                            .collect(toList());

            }


        }






        List<PostResponse> noValuelist = new ArrayList<>();
        return noValuelist;
    }


}