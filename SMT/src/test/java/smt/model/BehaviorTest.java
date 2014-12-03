package smt.model;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class BehaviorTest {

	@Test
	public void readValueFromJsonNode() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		
		Behavior b1 = new Behavior();
		b1.setId(1L);
		b1.setDescription("description");
		
		JsonNode node = mapper.valueToTree(b1);
		((ObjectNode) node).put("test","test");
		((ObjectNode) node).remove("endAge");
		((ObjectNode) node).put("id", "XXX");
		
		
		System.out.println(node.toString());
		
		Behavior b2 = new Behavior();
		try {
			b2 = mapper.treeToValue(node, Behavior.class);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		assertEquals(b1.getId(), b2.getId());	
		
		
	}

}
