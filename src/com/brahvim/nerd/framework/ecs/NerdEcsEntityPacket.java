package com.brahvim.nerd.framework.ecs;

import java.io.Serializable;

/* `package` */ class NerdEcsEntityPacket implements Serializable {

	@SuppressWarnings("unused")
	private final String NAME;

	@SuppressWarnings("unused")
	private final NerdEcsEntity ENTITY;

	public NerdEcsEntityPacket(final String p_name, final NerdEcsEntity p_entity) {
		this.NAME = p_name;
		this.ENTITY = p_entity;
	}

}
