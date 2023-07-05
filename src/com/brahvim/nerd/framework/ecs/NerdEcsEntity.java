package com.brahvim.nerd.framework.ecs;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.stream.Stream;

public final class NerdEcsEntity implements Serializable {

	// region Fields.
	public static final long serialVersionUID = -84636463676L;

	// Nope, no use in keeping the name here!
	// If you want speed, let there be a `HashMap` in `NerdEcsManager`!

	protected final transient NerdEcsManager MANAGER;
	protected final transient NerdEcsEntity ENTITY = this;

	private final LinkedList<NerdEcsComponent> COMPONENTS = new LinkedList<>();
	// endregion

	protected NerdEcsEntity(final NerdEcsManager p_manager) {
		this.MANAGER = p_manager;
	}

	// region Dynamic component list queries. (PLEASE! No variadic overloads...)
	/**
	 * Checks if a given {@link NerdEcsComponent} exists. As simple as that!
	 *
	 * @return ..if this exact component was held by this entity.
	 */
	public boolean hasComponent(final NerdEcsComponent p_component) {
		if (p_component != null)
			for (final NerdEcsComponent c : this.COMPONENTS)
				if (c == p_component)
					return true;
		return false;
	}

	/**
	 * Checks if a given {@link NerdEcsComponent} exists, and calls the given
	 * callback, if so.
	 *
	 * @param p_component   is the component to find. Duh.
	 * @param p_taskIfFound is the task performed if the component is found.
	 * @return ..if this exact component was held by this entity.
	 */
	public <ComponentT extends NerdEcsComponent> boolean hasComponent(
			final ComponentT p_component,
			final Consumer<ComponentT> p_taskIfFound) {
		if (p_component != null)
			for (final NerdEcsComponent c : this.COMPONENTS)
				if (c == p_component) {
					p_taskIfFound.accept(p_component);
					return true;
				}
		return false;
	}

	/**
	 * Checks if a given {@link NerdEcsComponent} exists. No matter it does or not,
	 * your callbacks are always called with the one you submitted!
	 *
	 * @param p_component      is the component to find. Duh.
	 * @param p_taskIfFound    is the task performed if the component is found.
	 * @param p_taskIfNotFound is the task performed if the component is not found.
	 * @return ..if this exact component was held by this entity.
	 */
	public <ComponentT extends NerdEcsComponent> boolean hasComponent(
			final ComponentT p_component,
			final Runnable p_taskIfNotFound,
			final Consumer<ComponentT> p_taskIfFound) {
		if (p_component != null)
			for (final NerdEcsComponent c : this.COMPONENTS)
				if (c == p_component) {
					p_taskIfFound.accept(p_component);
					return true;
				}

		p_taskIfNotFound.run();
		return false;
	}

	/**
	 * Tells if <i>absolutely <b>any</b></i> instance of the provided subclass of
	 * {@link NerdEcsComponent} is held by this entity.
	 *
	 * @param p_componentClass is the {@link Class} you need to pass!
	 *                         (Usually {@code ClassName.class}
	 *                         or {@code someObject.getClass()})
	 */
	public boolean hasComponentOfClass(final Class<? extends NerdEcsComponent> p_componentClass) {
		if (p_componentClass != null)
			for (final NerdEcsComponent c : this.COMPONENTS)
				if (c.getClass() == p_componentClass)
					return true;

		return false;
	}

	/**
	 * Tells if <i>absolutely <b>any</b></i> instance of the provided subclass of
	 * {@link NerdEcsComponent} is held by this entity.
	 *
	 * @param p_componentClass is the {@link Class} you need to pass!
	 *                         (Usually {@code ClassName.class}
	 *                         or {@code someObject.getClass()})
	 * @param p_taskIfFound    is performed if a component of the given type exists.
	 */
	@SuppressWarnings("unchecked")
	public <ComponentT extends NerdEcsComponent> boolean hasComponent(
			final Class<ComponentT> p_componentClass,
			final Consumer<ComponentT> p_taskIfFound) {
		if (p_componentClass != null)
			for (final NerdEcsComponent c : this.COMPONENTS)
				if (c.getClass() == p_componentClass) {
					p_taskIfFound.accept((ComponentT) c);
					return true;
				}

		return false;
	}

	/**
	 * Tells if <i>absolutely <b>any</b></i> instance of the provided subclass of
	 * {@link NerdEcsComponent} is held by this entity.
	 *
	 * @param p_componentClass is the {@link Class} you need to pass!
	 *                         (Usually {@code ClassName.class}
	 *                         or {@code someObject.getClass()})
	 * @param p_taskIfFound    is performed if a component of the given type exists.
	 * @param p_taskIfNotFound is performed if a component of the given type does
	 *                         not exist.
	 */
	@SuppressWarnings("unchecked")
	public <ComponentT extends NerdEcsComponent> boolean hasComponent(
			final Class<ComponentT> p_componentClass,
			final Runnable p_taskIfNotFound,
			final Consumer<ComponentT> p_taskIfFound) {
		if (p_componentClass != null)
			for (final NerdEcsComponent c : this.COMPONENTS)
				if (c.getClass() == p_componentClass) {
					p_taskIfFound.accept((ComponentT) c);
					return true;
				}

		p_taskIfNotFound.run();
		return false;
	}

	@SuppressWarnings("unchecked")
	public <ComponentT extends NerdEcsComponent> ComponentT getComponent(final Class<ComponentT> p_componentClass) {
		for (final NerdEcsComponent c : this.COMPONENTS)
			if (c.getClass().equals(p_componentClass))
				return (ComponentT) c;

		return null;
	}

	public <ComponentT extends NerdEcsComponent> ComponentT attachComponent(final Class<ComponentT> p_componentClass) {
		ComponentT toRet = null;

		// region Construction!
		try {
			toRet = p_componentClass.getConstructor().newInstance();
		} catch (final InstantiationException | SecurityException | IllegalArgumentException
				| InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
			e.printStackTrace();
		}
		// endregion

		if (toRet != null) {
			this.COMPONENTS.add(toRet);
			this.MANAGER.addComponent(toRet);
		}

		return toRet;
	}

	public void removeAllComponentsOfCondition(final Predicate<NerdEcsComponent> p_ifStatement) {
		final LinkedList<NerdEcsComponent> toRemove = new LinkedList<>();

		for (final NerdEcsComponent c : this.COMPONENTS)
			if (p_ifStatement.test(c))
				toRemove.add(c);

		this.COMPONENTS.removeAll(toRemove);
	}

	public void removeAllComponentsTyped(final Class<? extends NerdEcsComponent> p_componentClass) {
		final LinkedList<NerdEcsComponent> toRemove = new LinkedList<>();

		for (final NerdEcsComponent c : this.COMPONENTS)
			if (c.getClass().equals(p_componentClass))
				toRemove.add(c);

		this.COMPONENTS.removeAll(toRemove);
	}

	@SuppressWarnings("unchecked")
	public <ComponentT extends NerdEcsComponent> ComponentT removeComponent(final Class<ComponentT> p_componentClass) {
		ComponentT toRet = null;

		for (final NerdEcsComponent c : this.COMPONENTS)
			if (c.getClass().equals(p_componentClass))
				toRet = (ComponentT) c;

		if (toRet != null) {
			this.COMPONENTS.remove(toRet);
			this.MANAGER.removeComponent(toRet);
		}

		return toRet;
	}

	/**
	 * @param <ComponentT>     The type of the component - any
	 *                         {@link NerdEcsComponent}.
	 * @param p_componentClass is the component's respective {@link Class}.
	 * @return {@code null} if the component already exists.
	 */
	public <ComponentT extends NerdEcsComponent> ComponentT attachComponentIfAbsent(
			final Class<ComponentT> p_componentClass) {
		if (!(p_componentClass == null || this.hasComponentOfClass(p_componentClass)))
			return this.attachComponent(p_componentClass);
		else
			return null;
	}

	// endregion

	// region From `LinkedList`.
	public Iterator<NerdEcsComponent> getComponentsDescendingIterator() {
		return this.COMPONENTS.descendingIterator();
	}

	public NerdEcsComponent getComponentFromIndex(final int p_index) {
		return this.COMPONENTS.get(p_index);
	}

	public NerdEcsComponent getFirstComponent() {
		return this.COMPONENTS.getFirst();
	}

	public NerdEcsComponent getLastComponent() {
		return this.COMPONENTS.getLast();
	}

	public int getIndexOfComponent(final NerdEcsComponent p_component) {
		return this.COMPONENTS.indexOf(p_component);
	}

	public int getLastIndexOfComponent(final NerdEcsComponent p_component) {
		return this.COMPONENTS.lastIndexOf(p_component);
	}

	public int getComponentCount() {
		return this.COMPONENTS.size();
	}

	public <T> T[] getComponentsArray(final T[] p_array) {
		return this.COMPONENTS.toArray(p_array);
	}

	public List<NerdEcsComponent> getComponentSubList(final int p_beginIndex, final int p_endIndex) {
		return this.COMPONENTS.subList(p_beginIndex, p_endIndex);
	}

	public boolean hasComponentsFrom(final Collection<?> p_collection) {
		return this.COMPONENTS.containsAll(p_collection);
	}

	public boolean hasNoComponents() {
		return this.COMPONENTS.isEmpty();
	}

	public Stream<NerdEcsComponent> getComponentsParallelStream() {
		return this.COMPONENTS.parallelStream();
	}

	public Stream<NerdEcsComponent> getComponentStream() {
		return this.COMPONENTS.stream();
	}

	public <T> T[] getComponentsArray(final IntFunction<T[]> p_generator) {
		return this.COMPONENTS.toArray(p_generator);
	}

	public NerdEcsComponent[] getComponentsArray() {
		return this.COMPONENTS.toArray(new NerdEcsComponent[0]);
	}

	public void forEachComponent(final Consumer<? super NerdEcsComponent> p_action) {
		this.COMPONENTS.forEach(p_action);
	}

	@SuppressWarnings("unchecked")
	public LinkedList<NerdEcsComponent> getComponentsListClone() {
		return (LinkedList<NerdEcsComponent>) this.COMPONENTS.clone();
	}
	// endregion

}