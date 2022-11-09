package com.d205.sdutyplus.domain.user.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = 1470304260L;

    public static final QUser user = new QUser("user");

    public final BooleanPath banYN = createBoolean("banYN");

    public final BooleanPath delYN = createBoolean("delYN");

    public final StringPath email = createString("email");

    public final StringPath fcmToken = createString("fcmToken");

    public final StringPath imgUrl = createString("imgUrl");

    public final NumberPath<Long> job = createNumber("job", Long.class);

    public final StringPath nickname = createString("nickname");

    public final DateTimePath<java.time.LocalDateTime> regTime = createDateTime("regTime", java.time.LocalDateTime.class);

    public final NumberPath<Long> seq = createNumber("seq", Long.class);

    public final EnumPath<SocialType> socialType = createEnum("socialType", SocialType.class);

    public QUser(String variable) {
        super(User.class, forVariable(variable));
    }

    public QUser(Path<? extends User> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUser(PathMetadata metadata) {
        super(User.class, metadata);
    }

}

