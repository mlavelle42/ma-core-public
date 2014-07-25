/**
 * Copyright (C) 2014 Infinite Automation Software. All rights reserved.
 * @author Terry Packer
 */
package com.serotonin.m2m2.web.mvc.rest.v1.mapping;

import java.io.IOException;
import java.io.StringWriter;

import org.apache.log4j.Logger;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.serotonin.json.JsonException;
import com.serotonin.json.JsonWriter;
import com.serotonin.json.spi.JsonSerializable;
import com.serotonin.m2m2.Common;

/**
 * @author Terry Packer
 *
 */
public class SeroJsonSerializer extends JsonSerializer<JsonSerializable>{

	private static Logger LOG = Logger.getLogger(SeroJsonSerializer.class);

	
	
	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.JsonSerializer#serialize(java.lang.Object, com.fasterxml.jackson.core.JsonGenerator, com.fasterxml.jackson.databind.SerializerProvider)
	 */
	@Override
	public void serialize(JsonSerializable value,
			JsonGenerator paramJsonGenerator,
			SerializerProvider paramSerializerProvider) throws IOException,
			JsonProcessingException {
		//Simply use the Sero JSON for now
				StringWriter sw = new StringWriter();
				JsonWriter writer = new JsonWriter(Common.JSON_CONTEXT, sw);
				//TODO Make optional somehow
		        int prettyIndent = 3;
		        writer.setPrettyIndent(prettyIndent);
		        writer.setPrettyOutput(true);
		        
				try {
					writer.writeObject(value);
					writer.flush();
					paramJsonGenerator.writeRaw(sw.toString());
				} catch (JsonException e) {
					LOG.error(e);
				}
		
	}
	
	
	/* (non-Javadoc)
	 * @see com.fasterxml.jackson.databind.JsonSerializer#serializeWithType(java.lang.Object, com.fasterxml.jackson.core.JsonGenerator, com.fasterxml.jackson.databind.SerializerProvider, com.fasterxml.jackson.databind.jsontype.TypeSerializer)
	 */
	@Override
	public void serializeWithType(JsonSerializable value, JsonGenerator jgen,
			SerializerProvider provider, TypeSerializer typeSer)
			throws IOException, JsonProcessingException {
		  typeSer.writeTypePrefixForScalar(value, jgen);
		  serialize(value, jgen, provider);
		  typeSer.writeTypeSuffixForScalar(value, jgen);
	}
	

}
