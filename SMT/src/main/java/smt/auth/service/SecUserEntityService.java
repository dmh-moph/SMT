package smt.auth.service;

import org.springframework.data.domain.Page;

import com.fasterxml.jackson.databind.JsonNode;

import smt.auth.model.SecurityUser;
import smt.webUI.ResponseJSend;

public interface SecUserEntityService {

	public SecurityUser getSecUserByLogin(String Login);

	public ResponseJSend<Page<SecurityUser>> findSecurityUserByExample(JsonNode node,
			Integer pageNum);

	public ResponseJSend<Long> saveSecurityUser(JsonNode node, SecurityUser user);

	public ResponseJSend<Long> deleteSecurityUser(Long id);

	public SecurityUser findSecurityUserById(Long id);

}
