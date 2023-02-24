package com.bezkoder.spring.login.repository;

import com.bezkoder.spring.login.models.User;
import com.bezkoder.spring.login.models.Video;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface VideoRepository extends JpaRepository<Video,Long> {
    List<Video> findVideoByAuthor(User author, Pageable pageable);
    List<Video> findVideoByAuthorIdIn(List<Long> followingUserIds, Pageable pageable);
    List<Video> findVideoBySharedPost(Video video, Pageable pageable);
   // List<Video> findVideoByPostTags(Tag tag, Pageable pageable);

    @Query(value = "select titre,imagecouverture,url, like_count, comment_count, share_count from video;"
            ,nativeQuery = true)
    List<Video> ListeDesVideo(Video video);
}
