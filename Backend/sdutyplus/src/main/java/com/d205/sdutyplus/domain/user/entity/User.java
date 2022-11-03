package com.d205.sdutyplus.domain.user.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.sql.Time;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long seq;

    @Column(nullable = false, name="email", length = 50, unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name="social_type")
    private SocialType socialType;

    @Column(name="nickname", length = 20, unique = true)
    private String nickname;

    @Column(name="job")
    private long job;

    @Column(name="fcm_token")
    private String fcmToken;

    @Column(name="img_url")
    private String imgUrl;

    @Column(name="reset_time")
    private Time resetTime;

    @Column(name="reset_cron")
    private String resetCron;

    @Column(name = "del_YN", nullable = false)
    private boolean delYN;

    @Column(name = "ban_YN", nullable = false)
    private boolean banYN;

}
