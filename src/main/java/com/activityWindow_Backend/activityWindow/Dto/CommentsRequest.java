package com.activityWindow_Backend.activityWindow.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentsRequest {

    private Long postId;
    private String text;
    private String Recipient;

}
