package com.graphqlexample.project.models.dtos;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostUpdateDto {
  Long id;
  String title;
  String content;
  String publishedDate;
}
