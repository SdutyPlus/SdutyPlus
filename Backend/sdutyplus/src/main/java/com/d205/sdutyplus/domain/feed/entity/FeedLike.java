package com.d205.sdutyplus.domain.feed.entity;

import com.d205.sdutyplus.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@Table(name = "feed_likes")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class FeedLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "feed_like_seq")
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_seq")
    private Feed feed;

    @Builder
    public FeedLike(User user, Feed feed){
        this.user = user;
        this.feed = feed;
    }
}
