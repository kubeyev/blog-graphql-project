package com.graphqlexample.project.inputs;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostInput {
  String title;
  String content;
  String publishedDate;
}
