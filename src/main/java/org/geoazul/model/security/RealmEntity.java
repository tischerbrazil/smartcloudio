package org.geoazul.model.security;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import org.geoazul.model.Contador;

import com.erp.modules.commonClasses.BaseEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.hypersistence.utils.hibernate.id.Tsid;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToOne;

@Table(name="SEC_REALM")
@Entity
@NamedQueries({
	    @NamedQuery(name="getRealmById", query="select realm from RealmEntity realm where realm.id = :id"),
        @NamedQuery(name="getRealmByName", query="select realm from RealmEntity realm where realm.name = :name"),
        @NamedQuery(name="getRealmDefault", query="select realm from RealmEntity realm where realm.id = :id"),
        @NamedQuery(name="getRealmAll", query="select realm from RealmEntity realm"),
})
public class RealmEntity extends BaseEntity  {
	
	public static final String REALM_ID_GET = "getRealmById";
	public static final String REALM_DEFAULT = "realmDefault";
	public static final String REALM_NAME_GET = "getRealmByName";
	public static final String REALM_ALL = "getRealmAll";
    
    @Column(name="ENABLED")
	private boolean enabled;

    @Column(name="NAME", unique = true)
    private String name;

    @Column(name = "DISPLAYNAME")
	private String displayName;
	
	@Column(name = "DISPLAYNAMEHTML")
	private String displayNameHtml;

    @Column(name="REGISTRATION_ALLOWED")
    private boolean registrationAllowed;
    
    // required registrationAllowed on
    @Column(name = "REG_EMAIL_AS_USERNAME")
    private boolean registrationEmailAsUsername;
    
    @Column(name="EDIT_USERNAME_ALLOWED")
    private boolean editUsernameAllowed;

    @Column(name="RESET_PASSWORD_ALLOWED")
    private boolean resetPasswordAllowed;
    
    @Column(name="REMEMBER_ME")
    private boolean rememberMe;
    	    
    @Column(name="VERIFY_EMAIL")
    private Boolean verifyEmail;
    
    @Column(name="LOGIN_WITH_EMAIL_ALLOWED")
    private boolean loginWithEmailAllowed;
 
    @OneToMany(fetch = FetchType.LAZY, cascade ={CascadeType.REMOVE}, orphanRemoval = true, mappedBy = "realm")
    private Collection<RealmAttributeEntity> attributes = new ArrayList<RealmAttributeEntity>();
    
    @OneToMany(fetch = FetchType.LAZY, cascade ={CascadeType.REMOVE}, orphanRemoval = true)
	@JoinTable(name="SEC_REALM_DEFAULT_ROLES", joinColumns = { @JoinColumn(name="REALM_ID")}, inverseJoinColumns = { @JoinColumn(name="ROLE_ID")})
	private Collection<RoleEntity> defaultRoles = new ArrayList<RoleEntity>();

	@OneToMany(fetch = FetchType.LAZY, cascade ={CascadeType.REMOVE}, orphanRemoval = true, mappedBy = "realm")
	private Collection<GroupEntity> groups = new ArrayList<>();
	
	@OneToMany(fetch = FetchType.LAZY, cascade ={CascadeType.REMOVE}, orphanRemoval = true, mappedBy = "realm")
    private Collection<Contador> visitor = new ArrayList<>();
		
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.DETACH)
	@JoinColumn(name = "father_id", nullable = true)
	@JsonIgnore
	private RealmEntity father;

	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.DETACH, mappedBy = "father")
	private List<RealmEntity> childrensRealms = new ArrayList<RealmEntity>();
    
    public RealmEntity(Long id, String name, String displayName) {
    	this.id = id;
    	this.displayName = displayName;
    	this.name = name;
    	this.editUsernameAllowed = false;
    	this.enabled = true;
    	this.loginWithEmailAllowed= true;
    	this.registrationAllowed = true;
    	this.registrationEmailAsUsername = false;
    	this.rememberMe = true;
    	this.resetPasswordAllowed = true;
    	this.verifyEmail = true;
	}
    
    

	public RealmEntity() {
		super();
	}



	

    public boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
	
	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getDisplayNameHtml() {
		return displayNameHtml;
	}

	public void setDisplayNameHtml(String displayNameHtml) {
		this.displayNameHtml = displayNameHtml;
	}

    public boolean getRegistrationAllowed() {
        return registrationAllowed;
    }

    public void setRegistrationAllowed(boolean registrationAllowed) {
        this.registrationAllowed = registrationAllowed;
    }

    public boolean getRegistrationEmailAsUsername() {
        return registrationEmailAsUsername;
    }

    public void setRegistrationEmailAsUsername(boolean registrationEmailAsUsername) {
        this.registrationEmailAsUsername = registrationEmailAsUsername;
    }

    public boolean getRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
    }

    public Boolean getVerifyEmail() {
        return verifyEmail;
    }

    public void setVerifyEmail(Boolean verifyEmail) {
        this.verifyEmail = verifyEmail;
    }
    
    public boolean getLoginWithEmailAllowed() {
        return loginWithEmailAllowed;
    }

    public void setLoginWithEmailAllowed(boolean loginWithEmailAllowed) {
        this.loginWithEmailAllowed = loginWithEmailAllowed;
    }
    
    public boolean getResetPasswordAllowed() {
        return resetPasswordAllowed;
    }

    public void setResetPasswordAllowed(boolean resetPasswordAllowed) {
        this.resetPasswordAllowed = resetPasswordAllowed;
    }

    public boolean getEditUsernameAllowed() {
        return editUsernameAllowed;
    }

    public void setEditUsernameAllowed(boolean editUsernameAllowed) {
        this.editUsernameAllowed = editUsernameAllowed;
    }

    public Collection<RealmAttributeEntity> getAttributes() {
		return attributes;
	}

	public void setAttributes(Collection<RealmAttributeEntity> attributes) {
		this.attributes = attributes;
	}

    public Collection<RoleEntity> getDefaultRoles() {
        return defaultRoles;
    }

    public void setDefaultRoles(Collection<RoleEntity> defaultRoles) {
        this.defaultRoles = defaultRoles;
    }
      
    public Collection<GroupEntity> getGroups() {
        return groups;
    }

    public void setGroups(Collection<GroupEntity> groups) {
        this.groups = groups;
    }
    
	public Collection<Contador> getVisitor() {
		return visitor;
	}

	public void setVisitor(Collection<Contador> visitor) {
		this.visitor = visitor;
	}
	
	public List<RealmEntity> getChildrensRealms() {
		return childrensRealms;
	}

	public void setChildrensRealms(List<RealmEntity> childrensRealms) {
		this.childrensRealms = childrensRealms;
	}

	
	public RealmEntity getFather() {
		return father;
	}

	public void setFather(RealmEntity father) {
		this.father = father;
	}

}

