package smt.auth.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

@Entity
@Table(name="SEC_USER")
@SequenceGenerator(name="SEC_USER_SEQ", sequenceName="SEC_USER_SEQ", allocationSize=1)
@JsonIdentityInfo(generator=ObjectIdGenerators.PropertyGenerator.class, property="id")
public class SecurityUser implements User, UserDetails {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

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
	
	@Transient
	private Set<Role> roles;
	
	public SecurityUser() {
		
	}
	
	public SecurityUser(Long id, String username, String password, Set<Role> roles) {
		this.setId(id);
		this.setPassword(password);
		this.setRoles(roles);
		this.setUsername(username);
	}
	
	public SecurityUser(User user) {
		if(user != null) {
			this.setId(user.getId());
			this.setPassword(user.getPassword());
			this.setRoles(user.getRoles());
			this.setUsername(user.getUsername());
		}
	}


	private void setUsername(String username) {
		this.username=username;
	}


	private void setRoles(Set<Role> roles) {
		this.roles = roles;
	}


	private void setPassword(String password) {
		this.password = password;
	}


	private void setId(Long id) {
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


	@Override
	public Set<Role> getRoles() {
		return this.roles;
	}


	@Override
	public String toString() {
		return "SecurityUser [id=" + id + ", username=" + username
				+ ", password=" + password + ", roles=" + roles + "]";
	}
	

}
