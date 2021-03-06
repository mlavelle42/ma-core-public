/**
 * Copyright (C) 2014 Infinite Automation Software. All rights reserved.
 * @author Terry Packer
 */
package com.serotonin.m2m2.web.mvc.rest.v1.mapping;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * @author Terry Packer
 *
 */
abstract class UnitMixin {

	@JsonIgnore abstract boolean isStandardUnit();
}
