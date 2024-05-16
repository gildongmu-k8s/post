package gildongmu.trip.domain.comment.entity;


import gildongmu.trip.domain.BaseTimeEntity;
import gildongmu.trip.domain.post.entity.Post;
import gildongmu.trip.dto.transfer.UserInfo;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "comments")
public class Comment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @NotNull
    @ColumnDefault("FALSE")
    private boolean secret;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Comment parent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    private Long userId;

    @OneToMany(mappedBy = "parent", orphanRemoval = true)
    private List<Comment> children = new ArrayList<>();

    @Transient
    private UserInfo writer;

    @Builder
    public Comment(String content, boolean secret, Post post, Long userId, Comment parent) {
        this.content = content;
        this.secret = secret;
        this.post = post;
        this.userId = userId;
        this.parent = parent;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void updateSecret(boolean secret) {
        this.secret = secret;
    }


    public void addWriter(UserInfo userInfo){
        this.writer = userInfo;
    }
}