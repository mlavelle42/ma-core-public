/*
    Copyright (C) 2014 Infinite Automation Systems Inc. All rights reserved.
    @author Matthew Lohbihler
 */
package com.serotonin.m2m2.vo.event;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.serotonin.ShouldNeverHappenException;
import com.serotonin.json.JsonException;
import com.serotonin.json.JsonReader;
import com.serotonin.json.ObjectWriter;
import com.serotonin.json.spi.JsonProperty;
import com.serotonin.json.type.JsonObject;
import com.serotonin.m2m2.Common;
import com.serotonin.m2m2.db.dao.AbstractDao;
import com.serotonin.m2m2.db.dao.DataPointDao;
import com.serotonin.m2m2.db.dao.DataSourceDao;
import com.serotonin.m2m2.i18n.TranslatableJsonException;
import com.serotonin.m2m2.i18n.TranslatableMessage;
import com.serotonin.m2m2.rt.event.AlarmLevels;
import com.serotonin.m2m2.rt.event.compound.CompoundEventDetectorRT;
import com.serotonin.m2m2.rt.event.compound.ConditionParseException;
import com.serotonin.m2m2.rt.event.compound.LogicalOperator;
import com.serotonin.m2m2.rt.event.type.EventType;
import com.serotonin.m2m2.vo.AbstractVO;
import com.serotonin.m2m2.vo.DataPointVO;
import com.serotonin.m2m2.vo.User;
import com.serotonin.m2m2.vo.dataSource.DataSourceVO;
import com.serotonin.m2m2.vo.permission.Permissions;
import com.serotonin.web.dwr.DwrResponseI18n;
import com.serotonin.web.i18n.LocalizableMessage;

/**
 * This class is not working yet, its left here for the time when 
 * we decide to use Compound detectors.
 * 
 * @author Matthew Lohbihler
 */
public class CompoundEventDetectorVO<T extends AbstractVO<T>> extends AbstractVO<T> {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final String XID_PREFIX = "CED_";

    private int id = Common.NEW_ID;
    private String xid;
    @JsonProperty
    private String name;
    private int alarmLevel = AlarmLevels.NONE;
    @JsonProperty
    private boolean returnToNormal = true;
    @JsonProperty
    private boolean disabled = false;
    @JsonProperty
    private String condition;

    public boolean isNew() {
        return id == Common.NEW_ID;
    }

    public EventTypeVO getEventType() {
        return new EventTypeVO(EventType.EventTypeNames.COMPOUND, null, id, -1, new TranslatableMessage(
                "common.default", name), alarmLevel);
    }

    //@Override
    public String getTypeKey() {
        return "event.audit.compoundEventDetector";
    }

    public void validate(DwrResponseI18n response) {
        if (StringUtils.isBlank(name))
            response.addContextualMessage("name", "compoundDetectors.validation.nameRequired");

        validate(condition, response);
    }

    public static void validate(String condition, DwrResponseI18n response) {
        try {
            User user = Common.getUser();
            final boolean admin = Permissions.hasAdmin(user);
            if(!admin)
                Permissions.ensureDataSourcePermission(user);

            LogicalOperator l = CompoundEventDetectorRT.parseConditionStatement(condition);
            List<String> keys = l.getDetectorKeys();

            // Get all of the point event detectors.
            List<DataPointVO> dataPoints = DataPointDao.instance.getDataPoints(null, true);

            // Create a lookup of data sources.
            Map<Integer, DataSourceVO<?>> dss = new HashMap<>();
            for (DataSourceVO<?> ds : DataSourceDao.instance.getAll())
                dss.put(ds.getId(), ds);

            for (String key : keys) {
                if (!key.startsWith(SimpleEventDetectorVO.POINT_EVENT_DETECTOR_PREFIX))
                    continue;

                boolean found = false;
                for (DataPointVO dp : dataPoints) {
                    if (!admin && !Permissions.hasDataSourcePermission(user, dss.get(dp.getDataSourceId())))
                        continue;

//                    for (AbstractPointEventDetectorVO<?> ped : dp.getEventDetectors()) {
//                        if (ped.getEventDetectorKey().equals(key) && ped.isRtnApplicable()) {
//                            found = true;
//                            break;
//                        }
//                    }

                    if (found)
                        break;
                }

                if (!found)
                    throw new ConditionParseException(new LocalizableMessage("compoundDetectors.validation.invalidKey"));
            }
        }
        catch (ConditionParseException e) {
            response.addMessage("condition", e.getLocalizableMessage());
            if (e.isRange()) {
                response.addData("range", true);
                response.addData("from", e.getFrom());
                response.addData("to", e.getTo());
            }
        }
    }

    public CompoundEventDetectorRT createRuntime() {
        return new CompoundEventDetectorRT(this);
    }

    //@Override
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getXid() {
        return xid;
    }

    public void setXid(String xid) {
        this.xid = xid;
    }

    public int getAlarmLevel() {
        return alarmLevel;
    }

    public void setAlarmLevel(int alarmLevel) {
        this.alarmLevel = alarmLevel;
    }

    public boolean isReturnToNormal() {
        return returnToNormal;
    }

    public void setReturnToNormal(boolean returnToNormal) {
        this.returnToNormal = returnToNormal;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    //
    //
    // Serialization
    //
    @Override
    public void jsonWrite(ObjectWriter writer) throws IOException, JsonException {
        writer.writeEntry("xid", xid);
        writer.writeEntry("alarmLevel", AlarmLevels.CODES.getCode(alarmLevel));
    }

    @Override
    public void jsonRead(JsonReader reader, JsonObject jsonObject) throws JsonException {
        String text = jsonObject.getString("alarmLevel");
        if (text != null) {
            alarmLevel = AlarmLevels.CODES.getId(text);
            if (!AlarmLevels.CODES.isValidId(alarmLevel))
                throw new TranslatableJsonException("emport.error.scheduledEvent.invalid", "alarmLevel", text,
                        AlarmLevels.CODES.getCodeList());
        }
    }

	/* (non-Javadoc)
	 * @see com.serotonin.m2m2.vo.AbstractVO#getDao()
	 */
	@Override
	protected AbstractDao<T> getDao() {
		throw new ShouldNeverHappenException("Un-implemented.");
	}
}
