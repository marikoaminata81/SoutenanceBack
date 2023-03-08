package com.bezkoder.spring.login.repository;

import com.bezkoder.spring.login.models.User;
import com.bezkoder.spring.login.models.Video;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface VideoRepository extends JpaRepository<Video,Long> {
    List<Video> findVideoByAuthor(User author);
    List<Video> findVideoByAuthorIdIn(List<Long> followingUserIds, Pageable pageable);
    List<Video> findVideoBySharedPost(Video video);
   // List<Video> findVideoByPostTags(Tag tag, Pageable pageable);
    @Query(value = "SELECT DISTINCT post_likes.* FROM video,users,post_likes WHERE post_likes.liker_id=:idUser AND post_likes.post_id=:idPost",nativeQuery = true)
    List<Object> verifierLikeVideoByUser(@Param("idPost") Long idPost,@Param("idUser") Long idUser);

    @Query(value = "select titre,imagecouverture,url, like_count, comment_count from video;"
            ,nativeQuery = true)
    List<Video> ListeDesVideo(Video video);
}
