/**
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 */

package com.dell.cpsd.paqx.fru.rest.dto.vcenter;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * TODO: Document usage.
 * <p>
 * Copyright &copy; 2017 Dell Inc. or its subsidiaries.  All Rights Reserved.
 * </p>
 *
 * @version 1.0
 * @since 1.0
 */
@XmlRootElement
public class VirtualMachineId
{
    private String uuid;

    public String getUuid()
    {
        return uuid;
    }

}

