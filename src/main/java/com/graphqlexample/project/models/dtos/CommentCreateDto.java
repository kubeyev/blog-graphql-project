package com.graphqlexample.project.models.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import com.graphqlexample.project.models.entities.Post;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentCreateDto {
  String content;
  String publishedDate;
  Post post;
}
