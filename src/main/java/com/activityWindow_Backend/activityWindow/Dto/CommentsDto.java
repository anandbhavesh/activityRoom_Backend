package com.activityWindow_Backend.activityWindow.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentsDto {
    private Long id;
    private Long postId;
    private String duration;
    private String text;
    private String userName;
    private String filename;
}