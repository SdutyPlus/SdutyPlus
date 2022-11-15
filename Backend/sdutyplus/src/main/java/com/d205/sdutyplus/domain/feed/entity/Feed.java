package com.d205.sdutyplus.domain.feed.entity;

import com.d205.sdutyplus.domain.user.entity.User;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Feed {
    @Id @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(columnDefinition = "INT UNSIGNED")
    private Long seq;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_seq")
    private User writer;
    @Column(name = "img_url", length = 200, nullable = false)
    private String imgUrl;
    @Column(name = "content", length = 200, nullable = false)
    private String content;
    @Column(name = "ban_YN", nullable = false)
    @ColumnDefault("0")
    private Boolean banYN;
    @Column(name = "reg_time", nullable = false)
    @CreationTimestamp
    private LocalDateTime regTime;

    @Builder
    public Feed(User writer, String imgUrl, String content){
        this.writer = writer;
        this.imgUrl = imgUrl;
        this.content = content;
        this.banYN = false;
    }
}
