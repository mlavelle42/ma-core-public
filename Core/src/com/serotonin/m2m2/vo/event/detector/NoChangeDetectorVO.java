/**
 * Copyright (C) 2016 Infinite Automation Software. All rights reserved.
 * @author Terry Packer
 */
package com.serotonin.m2m2.vo.event.detector;

import com.serotonin.m2m2.DataTypes;
import com.serotonin.m2m2.i18n.ProcessResult;
import com.serotonin.m2m2.i18n.TranslatableMessage;
import com.serotonin.m2m2.rt.event.detectors.AbstractEventDetectorRT;
import com.serotonin.m2m2.rt.event.detectors.NoChangeDetectorRT;

/**
 * @author Terry Packer
 *
 */
public class NoChangeDetectorVO extends TimeoutDetectorVO<NoChangeDetectorVO>{

	private static final long serialVersionUID = 1L;
	
	public NoChangeDetectorVO() {
		super(new int[] { DataTypes.BINARY, DataTypes.MULTISTATE, DataTypes.NUMERIC, DataTypes.ALPHANUMERIC });
		this.setDuration(1);
	}
	
	@Override
    public void validate(ProcessResult response) {
        super.validate(response);
        if(duration <= 0)
            response.addContextualMessage("duration", "validate.greaterThanZero");
    }
	
	/* (non-Javadoc)
	 * @see com.serotonin.m2m2.vo.event.detector.AbstractEventDetectorVO#createRuntime()
	 */
	@Override
	public AbstractEventDetectorRT<NoChangeDetectorVO> createRuntime() {
		return new NoChangeDetectorRT(this);
	}

	/* (non-Javadoc)
	 * @see com.serotonin.m2m2.vo.event.detector.AbstractEventDetectorVO#getConfigurationDescription()
	 */
	@Override
	protected TranslatableMessage getConfigurationDescription() {
		return new TranslatableMessage("event.detectorVo.noChange", getDurationDescription());
	}

}
