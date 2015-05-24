package smt.auth.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import smt.auth.service.CustomUserDetailsService;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name="SEC_USER")
@SequenceGenerator(name="SEC_USER_SEQ", sequenceName="SEC_USER_SEQ", allocationSize=1)
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
public class SecurityUser implements User, UserDetails, Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5184808609144313548L;



	public static Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);
	


	@Id
	@GeneratedValue(strategy=GenerationType.SEQUENCE ,generator="SEC_USER_SEQ")
	@Column(name="ID")
	private Long id;
	
	@Basic
	@Column(name="USERNAME")
	private String username;

	@Basic
	@Column(name="PASSWORD")
	private String password;
	
	@OneToMany
	@JoinTable(name="SEC_USER_ROLE",
	  	joinColumns=@JoinColumn(name="USER_ID"),
	    inverseJoinColumns=@JoinColumn(name="ROLE_ID")
	)
	private Set<SecurityRole> securityRoles;
	
	public SecurityUser() {
		
	}
	
	public SecurityUser(Long id, String username, String password, Set<SecurityRole> roles) {
		this.setId(id);
		this.setPassword(password);
		this.setSecurityRoles(roles);
		this.setUsername(username);
	}
	

	public void setUsername(String username) {
		this.username=username;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public void setId(Long id) {
		this.id = id;
	}


	@Override
	public Collection<GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> autorities = new ArrayList<GrantedAuthority>();
		if(this.getRoles() != null) {
			for(Role role : this.getRoles()) {
				SimpleGrantedAuthority authority = new SimpleGrantedAuthority(role.getName());
				autorities.add(authority);
				
			}
		}
		
		
		return autorities;
	}

	@Override
	public String getPassword() {
		return this.password;
	}

	@Override
	public String getUsername() {
		return this.username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}


	@Override
	public Long getId() {
		return this.id;
	}

	public Set<SecurityRole> getSecurityRoles() {
		return this.securityRoles;
	}
	
	public void setSecurityRoles(Set<SecurityRole> securityRoles) {
		this.securityRoles = securityRoles;
	}

	@Override
	public Set<Role> getRoles() {
		Set<Role> roles = new HashSet<Role>();
		for(SecurityRole r : this.securityRoles) {
			roles.add(r);
		}
		return roles;
	}


	@Override
	public String toString() {
		return "SecurityUser [id=" + id + ", username=" + username
				+ ", password=" + password + ", roles=" + securityRoles + "]";
	}
	

}
