package com.brahvim.nerd.framework.ecs;

import java.io.Externalizable;

import com.brahvim.nerd.io.NerdByteSerialUtils;

public abstract class NerdEcsComponentPacket<PacketComponentT extends NerdEcsComponent> implements Externalizable {

    public static final long serialVersionUID = 8482347342466L;

    protected PacketComponentT component;

    private long versionNumber;

    protected NerdEcsComponentPacket() {
    }

    protected NerdEcsComponentPacket(final PacketComponentT p_component) {
        this.component = p_component;
        this.versionNumber = NerdByteSerialUtils.getClassHierarchyDepthOf(this.getClass());

        if (this.versionNumber == 0)
            throw new UnsupportedOperationException(
                    "If you wish to use a `" + this.getClass().getSimpleName() + "`, please make your own.");
    }

    public long getVersionNumber() {
        return this.versionNumber;
    }

}
