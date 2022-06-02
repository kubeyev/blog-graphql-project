package com.graphqlexample.project.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.graphqlexample.project.models.Post;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentCreateDto {
  String content;
  String publishedDate;
  Post post;
}
