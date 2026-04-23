/*
 * Copyright 2016 Red Hat, Inc. and/or its affiliates
 * and other contributors as indicated by the @author tags.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.geoazul.model.security;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;
import java.io.Serializable;

import org.example.kickoff.model.Person;

/**
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision: 1 $
 */
//@NamedQueries({
//        @NamedQuery(name="usersInRole", query="select u from UserRoleMappingEntity m, Person u where m.roleId=:roleId and u.id=m.user"),        
  //      @NamedQuery(name="userHasRole", query="select m from UserRoleMappingEntity m where m.user = :user and m.roleId = :roleId"),
  //      @NamedQuery(name="userRoleMappings", query="select m from UserRoleMappingEntity m where m.user = :user"),
  //      @NamedQuery(name="userRoleMappingIds", query="select m.roleId from UserRoleMappingEntity m where m.user = :user"),
  //      @NamedQuery(name="deleteUserRoleMappingsByRealm", query="delete from  UserRoleMappingEntity mapping where mapping.user IN (select u from Person u)"),
  //      @NamedQuery(name="deleteUserRoleMappingsByRole", query="delete from UserRoleMappingEntity m where m.roleId = :roleId"),
   //     @NamedQuery(name="deleteUserRoleMappingsByUser", query="delete from UserRoleMappingEntity m where m.user = :user"),
   //     @NamedQuery(name="grantRoleToAllUsers", query="insert into UserRoleMappingEntity (roleId, user) select role.id, user from RoleEntity role, Person user where role.id = :roleId")
//
//})
@Table(name="SEC_USER_ROLE_MAPPING")
@Entity
@IdClass(UserRoleMappingEntity.Key.class)
public class UserRoleMappingEntity  {

    @Id
    @ManyToOne(fetch= FetchType.LAZY)
    @JoinColumn(name="USER_ID")
    protected Person user;

    @Id
    @Column(name = "ROLE_ID")
    protected String roleId;

    public Person getUser() {
        return user;
    }

    public void setUser(Person user) {
        this.user = user;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }


    public static class Key implements Serializable {

        protected Person user;

        protected String roleId;

        public Key() {
        }

        public Key(Person user, String roleId) {
            this.user = user;
            this.roleId = roleId;
        }

        public Person getUser() {
            return user;
        }

        public String getRoleId() {
            return roleId;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Key key = (Key) o;

            if (!roleId.equals(key.roleId)) return false;
            if (!user.equals(key.user)) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = user.hashCode();
            result = 31 * result + roleId.hashCode();
            return result;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (!(o instanceof UserRoleMappingEntity)) return false;

        UserRoleMappingEntity key = (UserRoleMappingEntity) o;

        if (!roleId.equals(key.roleId)) return false;
        if (!user.equals(key.user)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = user.hashCode();
        result = 31 * result + roleId.hashCode();
        return result;
    }

}
