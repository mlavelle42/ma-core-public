/*
 * Copyright 2005 Joe Walker
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.directwebremoting.dwrp;

import org.directwebremoting.extend.OutboundContext;
import org.directwebremoting.extend.OutboundVariable;

/**
 * An OutboundVariable that can not be recursive.
 * @author Joe Walker [joe at getahead dot ltd dot uk]
 */
public class ErrorOutboundVariable extends AbstractOutboundVariable implements OutboundVariable
{
    /**
     * Default ctor that leaves blank (not null) members
     * @param code the access for the inited code
     * @param outboundContext The conversion context
     * @param errorMessage Some message for the developer to see.
     * @param forceInline true to force inline status, false to let the system decide
     */
    public ErrorOutboundVariable(OutboundContext outboundContext, String errorMessage, boolean forceInline)
    {
        super(outboundContext);
        this.errorMessage = errorMessage;

        if (forceInline)
        {
            forceInline(true);
        }
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.dwrp.AbstractOutboundVariable#getNotInlineDefinition()
     */
    protected NotInlineDefinition getNotInlineDefinition()
    {
        return new NotInlineDefinition("var " + getVariableName() + "=null;", "");
    }

    /* (non-Javadoc)
     * @see org.directwebremoting.dwrp.AbstractOutboundVariable#getInlineDefinition()
     */
    protected String getInlineDefinition()
    {
        return "null";
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return "Error:null";
    }

    /**
     * A message for the developer saying what has gone wrong.
     */
    private String errorMessage;
}
