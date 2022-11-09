package com.d205.sdutyplus.domain.task.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QSubTask is a Querydsl query type for SubTask
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QSubTask extends EntityPathBase<SubTask> {

    private static final long serialVersionUID = -1601415150L;

    public static final QSubTask subTask = new QSubTask("subTask");

    public final StringPath content = createString("content");

    public final NumberPath<Long> seq = createNumber("seq", Long.class);

    public final NumberPath<Long> taskSeq = createNumber("taskSeq", Long.class);

    public QSubTask(String variable) {
        super(SubTask.class, forVariable(variable));
    }

    public QSubTask(Path<? extends SubTask> path) {
        super(path.getType(), path.getMetadata());
    }

    public QSubTask(PathMetadata metadata) {
        super(SubTask.class, metadata);
    }

}

