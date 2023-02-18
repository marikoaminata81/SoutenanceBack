package com.bezkoder.spring.login.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "video")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Video {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idvideo;
    private String titre;
    private String imagecouverture;
    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(cascade = CascadeType.ALL)
    private List<Like> likes = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL)
    private List<Commentaire> commentaires = new ArrayList<>();

    public void like(User user){
        Like like = new Like(this,user);
        likes.add(like);
    }

}
