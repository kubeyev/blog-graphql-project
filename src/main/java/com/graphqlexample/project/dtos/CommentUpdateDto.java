package com.graphqlexample.project.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CommentUpdateDto {
  Long id;
  String content;
  String publishedDate;
}
