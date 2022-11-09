package com.d205.sdutyplus.domain.jwt.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QJwt is a Querydsl query type for Jwt
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QJwt extends EntityPathBase<Jwt> {

    private static final long serialVersionUID = 622027380L;

    public static final QJwt jwt = new QJwt("jwt");

    public final StringPath accessToken = createString("accessToken");

    public final StringPath refreshToken = createString("refreshToken");

    public final NumberPath<Long> seq = createNumber("seq", Long.class);

    public final NumberPath<Long> userSeq = createNumber("userSeq", Long.class);

    public QJwt(String variable) {
        super(Jwt.class, forVariable(variable));
    }

    public QJwt(Path<? extends Jwt> path) {
        super(path.getType(), path.getMetadata());
    }

    public QJwt(PathMetadata metadata) {
        super(Jwt.class, metadata);
    }

}

