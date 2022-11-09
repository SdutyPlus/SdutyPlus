package com.d205.sdutyplus.domain.off.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QOffUser is a Querydsl query type for OffUser
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QOffUser extends EntityPathBase<OffUser> {

    private static final long serialVersionUID = -546541729L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QOffUser offUser = new QOffUser("offUser");

    public final com.d205.sdutyplus.domain.user.entity.QUser fromUser;

    public final NumberPath<Long> seq = createNumber("seq", Long.class);

    public final com.d205.sdutyplus.domain.user.entity.QUser toUser;

    public QOffUser(String variable) {
        this(OffUser.class, forVariable(variable), INITS);
    }

    public QOffUser(Path<? extends OffUser> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QOffUser(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QOffUser(PathMetadata metadata, PathInits inits) {
        this(OffUser.class, metadata, inits);
    }

    public QOffUser(Class<? extends OffUser> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.fromUser = inits.isInitialized("fromUser") ? new com.d205.sdutyplus.domain.user.entity.QUser(forProperty("fromUser")) : null;
        this.toUser = inits.isInitialized("toUser") ? new com.d205.sdutyplus.domain.user.entity.QUser(forProperty("toUser")) : null;
    }

}

