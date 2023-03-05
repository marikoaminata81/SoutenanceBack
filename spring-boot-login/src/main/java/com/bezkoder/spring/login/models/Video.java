package com.bezkoder.spring.login.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "video")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titre;
    private String imagecouverture;
    private String url;
    private Integer likeCount;
    private Integer commentCount;
    private Integer shareCount;

    @Column(nullable = false)
    private Boolean isTypeShare;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "author_id")
    private User author;

   // @JsonIgnore
    @OneToMany(mappedBy = "video", cascade = CascadeType.REMOVE)
    private List<Commentaire> postComments = new ArrayList<>();

    @JsonIgnore
    @ManyToMany
    @JoinTable(
            name = "post_likes",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "liker_id")
    )
    private List<User> likeList = new ArrayList<>();

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "shared_post_id")
    private Video sharedPost;

    //@JsonIgnore
    @OneToMany(mappedBy = "sharedPost")
    private List<Video> shareList = new ArrayList<>();

    /*@ManyToMany
    @JoinTable(
            name = "post_tags",
            joinColumns = @JoinColumn(name = "video_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private List<Tag> postTags = new ArrayList<>();
*/

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Video video = (Video) o;
        return Objects.equals(id, video.id) && Objects.equals(author, video.author);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, author);
    }
}


