package gildongmu.trip.domain.bookmark.repository;


import gildongmu.trip.domain.bookmark.entity.Bookmark;
import gildongmu.trip.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    boolean existsByUserIdAndPost(Long userId, Post post);

    void deleteByUserIdAndPost(Long userId, Post post);

    List<Bookmark> findByUserId(Long userId);

    Long countByPost(Post post);
}
