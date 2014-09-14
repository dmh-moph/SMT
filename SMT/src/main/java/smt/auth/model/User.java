package smt.auth.model;

import java.util.Set;

public interface User {
	public Long getId();
	public String getUsername();
	public String getPassword();
	public Set<Role> getRoles();
	
}
