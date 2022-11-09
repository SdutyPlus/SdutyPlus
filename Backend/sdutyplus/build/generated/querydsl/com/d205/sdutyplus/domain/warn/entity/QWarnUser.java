package com.d205.sdutyplus.domain.warn.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QWarnUser is a Querydsl query type for WarnUser
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QWarnUser extends EntityPathBase<WarnUser> {

    private static final long serialVersionUID = -275877915L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QWarnUser warnUser = new QWarnUser("warnUser");

    public final com.d205.sdutyplus.domain.user.entity.QUser fromUser;

    public final NumberPath<Long> seq = createNumber("seq", Long.class);

    public final com.d205.sdutyplus.domain.user.entity.QUser toUser;

    public QWarnUser(String variable) {
        this(WarnUser.class, forVariable(variable), INITS);
    }

    public QWarnUser(Path<? extends WarnUser> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QWarnUser(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QWarnUser(PathMetadata metadata, PathInits inits) {
        this(WarnUser.class, metadata, inits);
    }

    public QWarnUser(Class<? extends WarnUser> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.fromUser = inits.isInitialized("fromUser") ? new com.d205.sdutyplus.domain.user.entity.QUser(forProperty("fromUser")) : null;
        this.toUser = inits.isInitialized("toUser") ? new com.d205.sdutyplus.domain.user.entity.QUser(forProperty("toUser")) : null;
    }

}

