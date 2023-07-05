package com.brahvim.nerd.framework.ecs;

import java.io.Serializable;

import com.brahvim.nerd.io.NerdByteSerialUtils;

public class NerdEcsComponentSerialPacket implements Serializable {

    public static final long serialVersionUID = 8482347342466L;

    public final long versionNumber;

    public NerdEcsComponentSerialPacket() {
        this.versionNumber = NerdByteSerialUtils.getClassHierarchyDepthOf(this.getClass());

        if (this.versionNumber == 0)
            throw new UnsupportedOperationException(
                    "If you wish to use a `NerdEcsComponentPacket`, please make your own.");
    }

}
