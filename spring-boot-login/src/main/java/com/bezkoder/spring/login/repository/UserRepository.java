package com.bezkoder.spring.login.repository;

import java.util.List;
import java.util.Optional;

import com.bezkoder.spring.login.models.Commentaire;
import com.bezkoder.spring.login.models.Video;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.bezkoder.spring.login.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByUsername(String username);

  Boolean existsByUsername(String username);

  Boolean existsByNumero(String numero);
  User findByEmail(String email);
  Optional<User> findById(Long id);
  List<User> findUsersByFollowerUsers(User user);
  List<User> findUsersByFollowingUsers(User user);
  List<User> findUsersByLikedPosts(Video video);
  List<User> findUsersByLikedComments(Commentaire comment);

  @Query(value = "select * from users u " +
          "where concat(u.username) like %:username% " +
          "order by u.username asc,",
          nativeQuery = true)
  List<User> findUsersByUsername(String username);
}
