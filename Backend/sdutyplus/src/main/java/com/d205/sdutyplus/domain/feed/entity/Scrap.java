package com.d205.sdutyplus.domain.feed.entity;

import com.d205.sdutyplus.domain.user.entity.User;
import lombok.*;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Scrap {
    @Id
    @Column(columnDefinition = "INT UNSIGNED")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="user_seq")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name ="feed_seq")
    private Feed feed;

    @Builder
    public Scrap(User user, Feed feed){
        this.user = user;
        this.feed = feed;
    }
}
