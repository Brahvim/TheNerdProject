package com.brahvim.nerd.framework.ecs;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public class NerdEcsEntity implements Serializable {

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
		if (!this.hasComponentOfClass(p_componentClass))
			return this.attachComponent(p_componentClass);
		else
			return null;
	}
	// endregion

	// region From `LinkedList`.
	// public Iterator<NerdEcsComponent> descendingIterator() {
	// return this.COMPONENTS.descendingIterator();
	// }

	public NerdEcsComponent element() {
		return this.COMPONENTS.element();
	}

	public NerdEcsComponent get(final int p_index) {
		return this.COMPONENTS.get(p_index);
	}

	public NerdEcsComponent getFirst() {
		return this.COMPONENTS.getFirst();
	}

	public NerdEcsComponent getLast() {
		return this.COMPONENTS.getLast();
	}

	public int indexOf(final NerdEcsComponent p_component) {
		return this.COMPONENTS.indexOf(p_component);
	}

	public int lastIndexOf(final NerdEcsComponent p_component) {
		return this.COMPONENTS.lastIndexOf(p_component);
	}

	// public ListIterator<NerdEcsComponent> listIterator(final int index) {
	// return this.COMPONENTS.listIterator(index);
	// }

	// public boolean offer(final NerdEcsComponent p_component) {
	// return this.COMPONENTS.offer(p_component);
	// }

	// public boolean offerFirst(final NerdEcsComponent p_component) {
	// return this.COMPONENTS.offerFirst(p_component);
	// }

	// public boolean offerLast(final NerdEcsComponent p_component) {
	// return this.COMPONENTS.offerLast(p_component);
	// }

	public NerdEcsComponent peek() {
		return this.COMPONENTS.peek();
	}

	public NerdEcsComponent peekFirst() {
		return this.COMPONENTS.peekFirst();
	}

	public NerdEcsComponent peekLast() {
		return this.COMPONENTS.peekLast();
	}

	// public NerdEcsComponent poll() {
	// return this.COMPONENTS.poll();
	// }

	// public NerdEcsComponent pollFirst() {
	// return this.COMPONENTS.pollFirst();
	// }

	// public NerdEcsComponent pollLast() {
	// return this.COMPONENTS.pollLast();
	// }

	// public NerdEcsComponent pop() {
	// return this.COMPONENTS.pop();
	// }

	// public void push(final NerdEcsComponent p_component) {
	// this.COMPONENTS.push(p_component);
	// }

	// public NerdEcsComponent remove() {
	// return this.COMPONENTS.remove();
	// }

	// public boolean remove(final NerdEcsComponent p_component) {
	// return this.COMPONENTS.remove(p_component);
	// }

	// public NerdEcsComponent remove(final int p_index) {
	// return this.COMPONENTS.remove(p_index);
	// }

	// public NerdEcsComponent removeFirst() {
	// return this.COMPONENTS.removeFirst();
	// }

	// public boolean removeFirstOccurrence(final NerdEcsComponent p_component) {
	// return this.COMPONENTS.removeFirstOccurrence(p_component);
	// }

	// public NerdEcsComponent removeLast() {
	// return this.COMPONENTS.removeLast();
	// }

	// public boolean removeLastOccurrence(final NerdEcsComponent p_component) {
	// return this.COMPONENTS.removeLastOccurrence(p_component);
	// }

	// public NerdEcsComponent set(final int index, final NerdEcsComponent element)
	// {
	// return this.COMPONENTS.set(index, element);
	// }

	public int size() {
		return this.COMPONENTS.size();
	}

	public Spliterator<NerdEcsComponent> spliterator() {
		return this.COMPONENTS.spliterator();
	}

	public NerdEcsComponent[] toArray() {
		return (NerdEcsComponent[]) this.COMPONENTS.toArray();
	}

	public <T> T[] toArray(final T[] p_array) {
		return this.COMPONENTS.toArray(p_array);
	}

	public Iterator<NerdEcsComponent> iterator() {
		return this.COMPONENTS.iterator();
	}

	// public ListIterator<NerdEcsComponent> listIterator() {
	// return this.COMPONENTS.listIterator();
	// }

	public List<NerdEcsComponent> subList(final int p_beginIndex, final int p_endIndex) {
		return this.COMPONENTS.subList(p_beginIndex, p_endIndex);
	}

	public boolean containsAll(final Collection<?> p_collection) {
		return this.COMPONENTS.containsAll(p_collection);
	}

	public boolean isEmpty() {
		return this.COMPONENTS.isEmpty();
	}

	// public boolean removeAll(final Collection<?> p_collection) {
	// return this.COMPONENTS.removeAll(p_collection);
	// }

	// public boolean retainAll(final Collection<?> p_collection) {
	// return this.COMPONENTS.retainAll(p_collection);
	// }

	public Stream<NerdEcsComponent> parallelStream() {
		return this.COMPONENTS.parallelStream();
	}

	// public boolean removeIf(final Predicate<? super NerdEcsComponent> p_filter) {
	// return this.COMPONENTS.removeIf(p_filter);
	// }

	public Stream<NerdEcsComponent> stream() {
		return this.COMPONENTS.stream();
	}

	public <T> T[] toArray(final IntFunction<T[]> p_generator) {
		return this.COMPONENTS.toArray(p_generator);
	}

	public void forEach(final Consumer<? super NerdEcsComponent> p_action) {
		this.COMPONENTS.forEach(p_action);
	}
	// endregion

}
