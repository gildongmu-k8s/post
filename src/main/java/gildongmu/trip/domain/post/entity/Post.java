package gildongmu.trip.domain.post.entity;


import gildongmu.trip.domain.BaseTimeEntity;
import gildongmu.trip.domain.Image.entity.Image;
import gildongmu.trip.domain.bookmark.entity.Bookmark;
import gildongmu.trip.domain.post.constant.MemberGender;
import gildongmu.trip.domain.post.constant.Status;
import gildongmu.trip.dto.UserInfo;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "posts")
@DynamicInsert
public class Post extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    @NotNull
    @Column
    private String destination;

    @NotNull
    @Column(length = 100)
    private String title;

    @NotNull
    @Column(columnDefinition = "TEXT")
    private String content;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    private String thumbnail;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images;

    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(columnDefinition = "VARCHAR(6) DEFAULT 'NONE'")
    private MemberGender memberGender;

    @NotNull
    private Short participants;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "VARCHAR(20) DEFAULT 'OPEN'")
    private Status status;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL)
    private Set<Bookmark> bookmarks;

    @ColumnDefault("0")
    private Long viewCount;

    @Transient
    private UserInfo writer;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Participant> postParticipants;


    @Builder(toBuilder = true)
    public Post(Long userId, String destination, String title, String content,
                LocalDate startDate, LocalDate endDate, String thumbnail, List<Image> images,
                MemberGender memberGender, Short participants, Status status,
                Set<Bookmark> bookmarks, Long viewCount, List<Comment> comments, List<Participant> postParticipants) {
        this.userId = userId;
        this.destination = destination;
        this.title = title;
        this.content = content;
        this.startDate = startDate;
        this.endDate = endDate;
        this.thumbnail = thumbnail;
        this.images = images;
        this.memberGender = memberGender;
        this.participants = participants;
        this.status = status;
        this.bookmarks = bookmarks;
        this.viewCount = viewCount;
        this.comments = comments;
        this.postParticipants = postParticipants;
    }

    public void updateDestination(String destination) {
        this.destination = destination;
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void updateStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public void updateEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public void updateThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public void updateImages(List<Image> images) {
        this.images = images;
    }

    public void updateGender(MemberGender memberGender) {
        this.memberGender = memberGender;
    }

    public void updateParticipants(Short participants) {
        this.participants = participants;
    }

    public void updateStatus(Status status) {
        this.status = status;
    }

    public void addWriter(UserInfo userInfo){
        this.writer = userInfo;
    }
}
