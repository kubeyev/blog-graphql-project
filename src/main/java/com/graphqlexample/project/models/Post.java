package com.graphqlexample.project.models;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;


@Data
@Entity
@Table(name = "posts")
@EqualsAndHashCode
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

  public String getFormattedDate() {
    return getFormattedDate().toString();
  }
}
