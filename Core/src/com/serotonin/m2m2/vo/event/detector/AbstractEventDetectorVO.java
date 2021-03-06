/**
 * Copyright (C) 2016 Infinite Automation Software. All rights reserved.
 * @author Terry Packer
 */
package com.serotonin.m2m2.vo.event.detector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.serotonin.json.JsonException;
import com.serotonin.json.JsonReader;
import com.serotonin.json.ObjectWriter;
import com.serotonin.json.type.JsonArray;
import com.serotonin.json.type.JsonObject;
import com.serotonin.json.type.JsonValue;
import com.serotonin.m2m2.db.dao.AbstractDao;
import com.serotonin.m2m2.db.dao.EventDetectorDao;
import com.serotonin.m2m2.db.dao.EventHandlerDao;
import com.serotonin.m2m2.i18n.ProcessResult;
import com.serotonin.m2m2.i18n.TranslatableMessage;
import com.serotonin.m2m2.module.EventDetectorDefinition;
import com.serotonin.m2m2.rt.event.detectors.AbstractEventDetectorRT;
import com.serotonin.m2m2.vo.AbstractVO;
import com.serotonin.m2m2.vo.event.AbstractEventHandlerVO;
import com.serotonin.m2m2.vo.event.EventTypeVO;
import com.serotonin.m2m2.web.mvc.rest.v1.model.events.detectors.AbstractEventDetectorModel;
import com.serotonin.validation.StringValidation;

/**
 * @author Terry Packer
 *
 */
public abstract class AbstractEventDetectorVO<T extends AbstractEventDetectorVO<T>> extends AbstractVO<T>{

	private static final long serialVersionUID = 1L;

	public static final String XID_PREFIX = "ED_";
	
	/* Source of the detector */
	protected int sourceId;
	
	private List<String> addedEventHandlerXids = null;
	
	/**
	 * Our defintion
	 */
	protected EventDetectorDefinition<T> definition;
	
	/**
     * Return the Model Representation of the Event Detector Source
     * @return
     */
    public AbstractEventDetectorModel<T> asModel(){
    	return this.definition.createModel(this);
    }
	
	/**
	 * What event type do we generate
	 * @return
	 */
	public abstract EventTypeVO getEventType();
	

	/**
	 * Create the runtime 
	 * @return
	 */
	public abstract AbstractEventDetectorRT<T> createRuntime();

	/**
	 * Is our event Rtn Applicable?
	 * @return
	 */
	public abstract boolean isRtnApplicable();
	
	/**
	 * Return the configuration description for this handlers
	 * @return
	 */
	protected abstract TranslatableMessage getConfigurationDescription(); 
	
	/**
	 * Our type name defintion
	 * @return
	 */
	public String getDetectorType(){
		return this.definition.getEventDetectorTypeName();
	}
	
	/**
	 * Our source type name
	 * @return
	 */
	public String getDetectorSourceType(){
		return this.definition.getSourceTypeName();
	}
	
    public TranslatableMessage getDescription() {
        if (!StringUtils.isBlank(name))
            return new TranslatableMessage("common.default", name);
        return getConfigurationDescription();
    }
	
	@Override
	public String getTypeKey(){
		return "event.audit.pointEventDetector";
	}
	
    /**
     * Deprecated as we should just use the name. Leaving here as I believe these are probably accessed on the legacy page via DWR.
     * @param alias
     */
	@Deprecated
	public String getAlias() {
		return name;
	}

	/**
	 * Deprecated as we should just use the name. Leaving here as I believe these are probably accessed on the legacy page via DWR.
	 * @param alias
	 */
	@Deprecated
	public void setAlias(String alias) {
		this.name = alias;
	}
	
	public int getSourceId(){
		return this.sourceId;
	}
	public void setSourceId(int id){
		sourceId = id;
	}
	public void addEventHandlers(List<String> eventHandlerXids) {
	    if(addedEventHandlerXids == null)
	        addedEventHandlerXids = new ArrayList<String>(eventHandlerXids.size());
	    addedEventHandlerXids.addAll(eventHandlerXids);
	}
	public List<String> getAddedEventHandlers() {
	    return addedEventHandlerXids;
	}
	
	public EventDetectorDefinition<T> getDefinition() {
		return definition;
	}

	@SuppressWarnings("unchecked")
	public void setDefinition(EventDetectorDefinition<?> definition) {
		this.definition = (EventDetectorDefinition<T>) definition;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected AbstractDao<T> getDao(){
		return (AbstractDao<T>) EventDetectorDao.instance;
	}

	/*
	 * Override so we can allow for blank names
	 */
	@Override
	public void validate(ProcessResult response) {
	    if (StringUtils.isBlank(xid))
            response.addContextualMessage("xid", "validate.required");
        else if (StringValidation.isLengthGreaterThan(xid, 50))
            response.addMessage("xid", new TranslatableMessage("validate.notLongerThan", 50));
        else if (!isXidUnique(xid, id))
            response.addContextualMessage("xid", "validate.xidUsed");

	    // allow blank names
        if (!StringUtils.isBlank(name)) {
            if (StringValidation.isLengthGreaterThan(name, 255))
                response.addMessage("name", new TranslatableMessage("validate.notLongerThan", 255));
        }
        
        if(addedEventHandlerXids != null)
            for(String ehXid : addedEventHandlerXids) {
                AbstractEventHandlerVO<?> eh = EventHandlerDao.instance.getByXid(ehXid);
                if(eh == null)
                    response.addMessage("handlers", new TranslatableMessage("emport.eventHandler.missing", ehXid));
            }
	}

	@Override
    public void jsonWrite(ObjectWriter writer) throws IOException, JsonException {
        writer.writeEntry("type", this.definition.getEventDetectorTypeName());
        writer.writeEntry("sourceType", this.definition.getSourceTypeName());
        writer.writeEntry("xid", xid);
        writer.writeEntry("alias", name);
        
        /* Event handler references are not exported here because there would be a circular dependency
        *  with the eventTypes array in the handler, and since there are other event types that was deemed
        *  the more versatile. One can create handler mappings through this array, but you cannot have both 
        *  items refer to one another in the JSON if both are new, so this is not exported.
        */
        //writer.writeEntry("handlers", EventHandlerDao.instance.getEventHandlerXids(getEventType()));
    }

    @Override
    public void jsonRead(JsonReader reader, JsonObject jsonObject) throws JsonException {
        name = jsonObject.getString("alias");
        
        //In keeping with data points, the import can only add mappings
        //  The "handlers" key is removed by the EventDetectorRowMapper
        JsonArray handlers = jsonObject.getJsonArray("handlers");
        if(handlers != null) {
            addedEventHandlerXids = new ArrayList<String>(handlers.size());
            Iterator<JsonValue> iter = handlers.iterator();
            while(iter.hasNext())
                addedEventHandlerXids.add(iter.next().toString());
        }
        
        
    }
}
