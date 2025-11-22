package io.github.faustofanb.admin.module.file.domain.entity;

import org.babyfish.jimmer.sql.Entity;
import org.babyfish.jimmer.sql.Table;

import io.github.faustofanb.admin.core.domain.BaseEntity;

@Entity
@Table(name = "sys_file")
public interface SysFile extends BaseEntity {

    String fileName();

    String fileType();

    long fileSize();

    String url();

    String storageType(); // LOCAL, MINIO

    String bucket();

    String filePath();

    String md5();

    long tenantId();
}
