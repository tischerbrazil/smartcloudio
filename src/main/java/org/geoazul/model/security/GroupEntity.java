/*
 *
 */

package org.geoazul.model.security;

import org.hibernate.annotations.Nationalized;
import jakarta.persistence.*;

/**
 * 
 */

@NamedQueries({
        @NamedQuery(name="getGroupIdsByParent", query="select u.id from GroupEntity u where u.parent = :parent"),
        @NamedQuery(name="getGroupIdsByNameContaining", query="select u.id from GroupEntity u where u.realm.id = :realm and u.name like concat('%',:search,'%') order by u.name ASC"),
        @NamedQuery(name="getTopLevelGroupIds", query="select u.id from GroupEntity u where u.parent is null and u.realm.id = :realm"),
        @NamedQuery(name="getGroupCount", query="select count(u) from GroupEntity u where u.realm.id = :realm"),
        @NamedQuery(name="getTopLevelGroupCount", query="select count(u) from GroupEntity u where u.realm.id = :realm and u.parent is null")
})
@Entity
@Table(name="SEC_GROUP")
public class GroupEntity {
    @Id
    @Column(name="ID", length = 36)
    @Access(AccessType.PROPERTY) // we do this because relationships often fetch id, but not entity.  This avoids an extra SQL
    protected String id;

    @Nationalized
    @Column(name = "NAME")
    protected String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_GROUP")
    private GroupEntity parent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REALM_ID")
    private RealmEntity realm;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
   
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RealmEntity getRealm() {
        return realm;
    }

    public void setRealm(RealmEntity realm) {
        this.realm = realm;
    }

    public GroupEntity getParent() {
        return parent;
    }

    public void setParent(GroupEntity parent) {
        this.parent = parent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (!(o instanceof GroupEntity)) return false;

        GroupEntity that = (GroupEntity) o;

        if (!id.equals(that.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
