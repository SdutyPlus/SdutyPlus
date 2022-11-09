package com.d205.sdutyplus.domain.feed.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QFeed is a Querydsl query type for Feed
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFeed extends EntityPathBase<Feed> {

    private static final long serialVersionUID = 719707946L;

    public static final QFeed feed = new QFeed("feed");

    public final BooleanPath banYN = createBoolean("banYN");

    public final StringPath content = createString("content");

    public final StringPath imgUrl = createString("imgUrl");

    public final DateTimePath<java.time.LocalDateTime> regTime = createDateTime("regTime", java.time.LocalDateTime.class);

    public final NumberPath<Long> seq = createNumber("seq", Long.class);

    public final NumberPath<Long> writerSeq = createNumber("writerSeq", Long.class);

    public QFeed(String variable) {
        super(Feed.class, forVariable(variable));
    }

    public QFeed(Path<? extends Feed> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFeed(PathMetadata metadata) {
        super(Feed.class, metadata);
    }

}

