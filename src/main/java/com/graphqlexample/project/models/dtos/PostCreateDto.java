package com.graphqlexample.project.models.dtos;

import lombok.*;

@Data
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostCreateDto {
  String title;
  String content;
  String publishedDate;
}
