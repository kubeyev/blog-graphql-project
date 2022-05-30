package com.graphqlexample.project.models;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;


@Data
@Entity
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "posts")
public class Post implements Serializable {
  @Id
  @Column(name = "ID")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(name = "title")
  private String title;
  @Column(name = "content")
  private String content;
  @Column(name = "published_date_time")
  private LocalDate publishedDate;
  private transient String formattedDate;

  public Post(String title, String content, LocalDate parse) {
    this.setTitle(title);
    this.setContent(content);
    this.setPublishedDate(parse);
  }

  public String getFormattedDate() {
    return getFormattedDate().toString();
  }
}
