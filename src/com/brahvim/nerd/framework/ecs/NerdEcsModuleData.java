package com.brahvim.nerd.framework.ecs;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

public class NerdEcsModuleData implements Serializable {

	public static final long serialVersionUID = -6488574946L;

	// // Loooooooooong declaration!:
	// (Class<? extends NerdEcsSystem<? extends NerdEcsComponent>>[]) Set
	// .<Class<? extends NerdEcsSystem<? extends NerdEcsComponent>>>of(null, null,
	// null).toArray();

	protected long numUnnamedEntities = 1;
	protected NerdEcsSystem<?>[] ecsSystems;
	protected LinkedList<NerdEcsEntity> entities;
	protected LinkedList<NerdEcsComponent> components;
	protected HashMap<String, NerdEcsEntity> nameToEntityMap;
	protected HashMap<Class<? extends NerdEcsComponent>, HashSet<NerdEcsComponent>> classesToComponentsMap;

	@SuppressWarnings("unused")
	private NerdEcsModuleData() {
	}

	protected NerdEcsModuleData(final NerdEcsModule p_ecs) {
		this.entities = p_ecs.ENTITIES;
		this.ecsSystems = p_ecs.ecsSystems;
		this.components = p_ecs.COMPONENTS;
		this.nameToEntityMap = p_ecs.NAME_TO_ENTITY_MAP;
		this.numUnnamedEntities = p_ecs.numUnnamedEntities;
		this.classesToComponentsMap = p_ecs.CLASSES_TO_COMPONENTS_MAP;
	}

}
