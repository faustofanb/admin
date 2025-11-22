package io.github.faustofanb.admin.core.domain;

import java.lang.Integer;
import java.lang.Long;
import java.time.LocalDateTime;
import java.util.Collections;
import org.babyfish.jimmer.internal.GeneratedBy;
import org.babyfish.jimmer.lang.OldChain;
import org.babyfish.jimmer.meta.ImmutablePropCategory;
import org.babyfish.jimmer.meta.ImmutableType;

@GeneratedBy(
        type = TreeEntity.class
)
public interface TreeEntityDraft extends TreeEntity, BaseEntityDraft {
    TreeEntityDraft.Producer $ = Producer.INSTANCE;

    @OldChain
    TreeEntityDraft setId(long id);

    @OldChain
    TreeEntityDraft setCreatedTime(LocalDateTime createdTime);

    @OldChain
    TreeEntityDraft setUpdatedTime(LocalDateTime updatedTime);

    @OldChain
    TreeEntityDraft setCreatedBy(Long createdBy);

    @OldChain
    TreeEntityDraft setUpdatedBy(Long updatedBy);

    @OldChain
    TreeEntityDraft setSort(Integer sort);

    @GeneratedBy(
            type = TreeEntity.class
    )
    class Producer {
        static final Producer INSTANCE = new Producer();

        public static final ImmutableType TYPE = ImmutableType
            .newBuilder(
                "0.9.96",
                TreeEntity.class,
                Collections.singleton(BaseEntityDraft.Producer.TYPE),
                null
            )
            .add(-1, "sort", ImmutablePropCategory.SCALAR, Integer.class, true)
            .build();

        private Producer() {
        }
    }
}
