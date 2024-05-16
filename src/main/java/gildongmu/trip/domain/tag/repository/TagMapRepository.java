package gildongmu.trip.domain.tag.repository;


import gildongmu.trip.domain.post.entity.Post;
import gildongmu.trip.domain.tag.entity.TagMap;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagMapRepository extends JpaRepository<TagMap, Long> {

    List<TagMap> findAllByPost(Post post);
}
