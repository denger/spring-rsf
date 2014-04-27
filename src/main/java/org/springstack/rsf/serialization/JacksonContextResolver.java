package org.springstack.rsf.serialization;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.Provider;

import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.module.SimpleModule;

@Provider
@Produces(MediaType.APPLICATION_JSON)
public class JacksonContextResolver implements ContextResolver<ObjectMapper>{
	
	private ObjectMapper objectMapper;
	
	public JacksonContextResolver(){
		this.objectMapper = new ObjectMapper();
		registerDefaultModule();
	}

	@Override
	public ObjectMapper getContext(Class<?> type) {
		return this.objectMapper;
	}

	protected void registerDefaultModule(){
		SimpleModule module = new SimpleModule("DEFAULT_MODULE", new Version(1, 0, 0, null));
		objectMapper.registerModule(module);
	}
}
