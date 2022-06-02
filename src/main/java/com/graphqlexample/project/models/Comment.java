package com.graphqlexample.project.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@Entity
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "comments")
public class Comment implements Serializable {
  @Id
  @Column(name = "ID")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @Column(name = "content")
  private String content;
  @Column(name = "published_date_time")
  private LocalDate publishedDate;
  private transient String formattedDate;

  @ManyToOne
  @JoinColumn(name = "post_id", nullable = false, updatable = false)
  private Post post;

  public Comment(String content, LocalDate parse, Post post) {
    this.setContent(content);
    this.setPublishedDate(parse);
    this.setPost(post);
  }
}
