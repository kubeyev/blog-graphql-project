package com.graphqlexample.project.models.entities;

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
  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false, updatable = false)
  private User user;


  public Post(String title, String content, LocalDate parse, User user) {
    this.setTitle(title);
    this.setContent(content);
    this.setPublishedDate(parse);
    this.setUser(user);
  }

  public Post(String title, String content, String publishedDate, User user) {
    this.setTitle(title);
    this.setContent(content);
    this.setPublishedDate(LocalDate.parse(publishedDate));
    this.setUser(user);
  }
}
