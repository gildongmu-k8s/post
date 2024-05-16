package gildongmu.trip.domain.tag.repository;

import gildongmu.trip.domain.tag.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    Optional<Tag> findById(Long tagId);

    Optional<Tag> findByTagName(String tagName);

}
