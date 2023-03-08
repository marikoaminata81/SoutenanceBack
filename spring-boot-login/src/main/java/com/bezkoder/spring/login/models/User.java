package com.bezkoder.spring.login.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import java.util.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Entity
@Table(name = "users",
       uniqueConstraints = {
           @UniqueConstraint(columnNames = "username"),
           @UniqueConstraint(columnNames = "numero")
       })
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;
  private String nom;
  private String prenom;
  private String username;
  private String numero;
  private String email;
  private String intro;
  private String password;
  private String Adresse;
  private String photo;
  private Integer followerCount=0;
  private Integer followingCount=0;

  @ManyToMany(fetch = FetchType.LAZY)
  @JoinTable(name = "user_roles",
          joinColumns = @JoinColumn(name = "user_id"),
          inverseJoinColumns = @JoinColumn(name = "role_id"))
  private Set<Role> roles = new HashSet<>();

  @JsonIgnore
  @ManyToMany
  @JoinTable(
          name = "follow_users",
          joinColumns = @JoinColumn(name = "followed_id"),
          inverseJoinColumns = @JoinColumn(name = "follower_id")
  )
  private List<User> followerUsers = new ArrayList<>();

  @JsonIgnore
  @ManyToMany(mappedBy = "followerUsers")
  private List<User> followingUsers = new ArrayList<>();
@JsonIgnore
  @OneToMany(mappedBy = "author", cascade = CascadeType.REMOVE)
  private List<Video> VideoList = new ArrayList<>();

  @OneToMany(mappedBy = "author", cascade = CascadeType.REMOVE)
  private List<Produit> ProduitList = new ArrayList<>();

  @JsonIgnore
  @ManyToMany(mappedBy = "likeList")
  private List<Video> likedPosts = new ArrayList<>();

  @JsonIgnore
  @ManyToMany(mappedBy = "likeList")
  private List<Commentaire> likedComments = new ArrayList<>();


  public User(String nom,String prenom,String username, String numero, String photo,String password ) {
    this.nom = nom;
    this.prenom = prenom;
    this.username = username;
    this.numero = numero;
    this.photo = photo;
    this.password = password;

  }


  public User(String username, String nom, String prenom, String password,  Collection<? extends GrantedAuthority> authorities) {
    this.username=username;
    this.nom=nom;
    this.prenom=prenom;
    this.password=password;



  }
}
