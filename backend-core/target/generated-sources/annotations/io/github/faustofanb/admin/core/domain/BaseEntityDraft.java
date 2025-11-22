package io.github.faustofanb.admin.core.domain;

import java.lang.Long;
import java.time.LocalDateTime;
import java.util.Collections;
import org.babyfish.jimmer.Draft;
import org.babyfish.jimmer.internal.GeneratedBy;
import org.babyfish.jimmer.lang.OldChain;
import org.babyfish.jimmer.meta.ImmutablePropCategory;
import org.babyfish.jimmer.meta.ImmutableType;

@GeneratedBy(
        type = BaseEntity.class
)
public interface BaseEntityDraft extends BaseEntity, Draft {
    BaseEntityDraft.Producer $ = Producer.INSTANCE;

    @OldChain
    BaseEntityDraft setId(long id);

    @OldChain
    BaseEntityDraft setCreatedTime(LocalDateTime createdTime);

    @OldChain
    BaseEntityDraft setUpdatedTime(LocalDateTime updatedTime);

    @OldChain
    BaseEntityDraft setCreatedBy(Long createdBy);

    @OldChain
    BaseEntityDraft setUpdatedBy(Long updatedBy);

    @GeneratedBy(
            type = BaseEntity.class
    )
    class Producer {
        static final Producer INSTANCE = new Producer();

        public static final ImmutableType TYPE = ImmutableType
            .newBuilder(
                "0.9.96",
                BaseEntity.class,
                Collections.emptyList(),
                null
            )
            .id(-1, "id", long.class)
            .add(-1, "createdTime", ImmutablePropCategory.SCALAR, LocalDateTime.class, false)
            .add(-1, "updatedTime", ImmutablePropCategory.SCALAR, LocalDateTime.class, false)
            .add(-1, "createdBy", ImmutablePropCategory.SCALAR, Long.class, true)
            .add(-1, "updatedBy", ImmutablePropCategory.SCALAR, Long.class, true)
            .build();

        private Producer() {
        }
    }
}
