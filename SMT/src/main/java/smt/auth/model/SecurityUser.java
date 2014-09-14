package smt.auth.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class SecurityUser implements User, UserDetails {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	private Long id;
	private String username;
	private String password;
	private Set<Role> roles;
	
	
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
