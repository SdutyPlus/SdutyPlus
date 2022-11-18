package com.d205.sdutyplus.domain.off.entity;

import com.d205.sdutyplus.domain.feed.entity.Feed;
import com.d205.sdutyplus.domain.user.entity.User;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "off_feed")
public class OffFeed {
    @Id
    @Column(name = "seq", columnDefinition = "INT UNSIGNED")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_seq")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "feed_seq")
    private Feed feed;

    @Builder
    public OffFeed(User user, Feed feed) {
        this.user = user;
        this.feed = feed;
    }
}
