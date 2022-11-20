package com.d205.sdutyplus.domain.warn.entity;

import com.d205.sdutyplus.domain.feed.entity.Feed;
import com.d205.sdutyplus.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "warn_feed")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WarnFeed {
    @Id
    @Column(columnDefinition = "INT UNSIGNED")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne
    @JoinColumn(name ="user_seq")
    private User user;

    @ManyToOne
    @JoinColumn(name ="feed_seq")
    private Feed feed;

    @Builder
    public WarnFeed(User user, Feed feed) {
        this.user = user;
        this.feed = feed;
    }
}
