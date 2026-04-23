package org.geoazul.model.security;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Nationalized;
import jakarta.persistence.Access;
import jakarta.persistence.AccessType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name="SEC_ROLE", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "NAME", "CLIENT_REALM_CONSTRAINT" })
})
@NamedQueries({
        @NamedQuery(name="RoleEntity.getClientRoles", query="select role from RoleEntity role where role.client = :client"),
        @NamedQuery(name="RoleEntity.getClientRoleIds", query="select role.id from RoleEntity role where role.client.id = :client"),
        @NamedQuery(name="RoleEntity.getClientRoleByName", query="select role from RoleEntity role where role.name = :name and role.client = :client"),
        @NamedQuery(name="RoleEntity.getClientRoleIdByName", query="select role.id from RoleEntity role where role.name = :name and role.client.id = :client"),
        @NamedQuery(name="RoleEntity.getRealmRoles", query="select role from RoleEntity role where role.clientRole = false and role.realm = :realm"),
        @NamedQuery(name="RoleEntity.getRealmRolesEntity", query="select role from RoleEntity role where role.clientRole = false and role.realm.id = :realm"),
        @NamedQuery(name="RoleEntity.getRealmRoleIds", query="select role.id from RoleEntity role where role.clientRole = false and role.realm.id = :realm"),
        @NamedQuery(name="RoleEntity.getRealmRoleByName", query="select role from RoleEntity role where role.clientRole = false and role.name = :name and role.realm = :realm"),
        @NamedQuery(name="RoleEntity.getRealmRoleIdByName", query="select role.id from RoleEntity role where role.clientRole = false and role.name = :name and role.realm.id = :realm")
})

public class RoleEntity {
	
	 public static final String REALM_ROLES = "RoleEntity.getRealmRolesEntity";
	
    @Id
    @Column(name="ID", length = 36)
    @Access(AccessType.PROPERTY) // we do this because relationships often fetch id, but not entity.  This avoids an extra SQL
    private String id;

    @Nationalized
    @Column(name = "NAME")
    private String name;
    @Nationalized
    @Column(name = "DESCRIPTION")
    private String description;

    // hax! couldn't get constraint to work properly
    @Column(name = "REALM_ID")
    private String realmId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "REALM")
    private RealmEntity realm;

    @Column(name="CLIENT_ROLE")
    private boolean clientRole;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CLIENT")
    private ClientEntity client;

    // Hack to ensure that either name+client or name+realm are unique. Needed due to MS-SQL as it don't allow multiple NULL values in the column, which is part of constraint
    @Column(name="CLIENT_REALM_CONSTRAINT", length = 36)
    private Long clientRealmConstraint;

    @ManyToMany(fetch = FetchType.LAZY, cascade = {})
    @JoinTable(name = "COMPOSITE_ROLE", joinColumns = @JoinColumn(name = "COMPOSITE"), inverseJoinColumns = @JoinColumn(name = "CHILD_ROLE"))
    private Set<RoleEntity> compositeRoles = new HashSet<>();

    @OneToMany(cascade = CascadeType.REMOVE, orphanRemoval = true, mappedBy="role")
    @Fetch(FetchMode.SELECT)
    @BatchSize(size = 20)
    protected List<RoleAttributeEntity> attributes = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRealmId() {
        return realmId;
    }

    public void setRealmId(String realmId) {
        this.realmId = realmId;
    }

    public Collection<RoleAttributeEntity> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<RoleAttributeEntity> attributes) {
        this.attributes = attributes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<RoleEntity> getCompositeRoles() {
        return compositeRoles;
    }

    public void setCompositeRoles(Set<RoleEntity> compositeRoles) {
        this.compositeRoles = compositeRoles;
    }

    public boolean isClientRole() {
        return clientRole;
    }

    public void setClientRole(boolean clientRole) {
        this.clientRole = clientRole;
    }

    public RealmEntity getRealm() {
        return realm;
    }

    public void setRealm(RealmEntity realm) {
        this.realm = realm;
        this.clientRealmConstraint = realm.getId();
    }

    public ClientEntity getClient() {
        return client;
    }

    public void setClient(ClientEntity client) {
        this.client = client;
        if (client != null) {
            this.clientRealmConstraint = client.getId();
        }
    }

    public Long getClientRealmConstraint() {
        return clientRealmConstraint;
    }

    public void setClientRealmConstraint(Long clientRealmConstraint) {
        this.clientRealmConstraint = clientRealmConstraint;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (!(o instanceof RoleEntity)) return false;

        RoleEntity that = (RoleEntity) o;

        if (!id.equals(that.getId())) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
