package com.d205.sdutyplus.domain.user.entity;

import com.d205.sdutyplus.global.entity.Job;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@DynamicInsert
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "INT UNSIGNED")
    private Long seq;

    @Column(nullable = false, name="email", length = 50)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name="social_type", length = 10)
    private SocialType socialType;

    @Column(name="nickname", length = 20, unique = true)
    @ColumnDefault("null")
    private String nickname;

    @OneToOne
    @JoinColumn(name = "job")
    private Job job;

    @Column(name="fcm_token", length = 250)
    private String fcmToken;

    @Column(name="img_url", length = 200)
    @ColumnDefault("null")
    private String imgUrl;

    @Column(name = "del_YN", columnDefinition="tinyint(1) default 0")
    private boolean delYN;

    @Column(name = "ban_YN", columnDefinition="tinyint(1) default 0")
    private boolean banYN;

    @Column(name = "reg_time")
    private LocalDateTime regTime;

    @Column(name = "last_report")
    @ColumnDefault("0")
    private LocalDate lastReport;

    @Column(name = "continuous")
    @ColumnDefault("0")
    private Long continuous;
}
