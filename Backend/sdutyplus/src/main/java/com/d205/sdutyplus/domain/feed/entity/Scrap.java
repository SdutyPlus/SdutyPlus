package com.d205.sdutyplus.domain.feed.entity;

import com.d205.sdutyplus.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Scrap {
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
    public Scrap(User user, Feed feed){
        this.user = user;
        this.feed = feed;
    }
}
