package io.github.faustofanb.admin.core.domain;

import org.babyfish.jimmer.sql.IdView;
import org.babyfish.jimmer.sql.ManyToOne;
import org.babyfish.jimmer.sql.MappedSuperclass;
import org.babyfish.jimmer.sql.OneToMany;

import java.util.List;

@MappedSuperclass
public interface TreeEntity<E extends TreeEntity<E>> extends BaseEntity {

    @IdView("parent")
    Long parentId();

    Integer sort();

    @ManyToOne
    E parent();

    @OneToMany(mappedBy = "parent")
    List<E> children();
}
