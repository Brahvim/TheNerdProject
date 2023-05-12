package com.brahvim.nerd.framework.ecs;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.util.LinkedList;

public class NerdEcsEntity implements Serializable {

    public static final long serialVersionUID = -84636463676L;

    protected final transient NerdEcsEntity ENTITY = this;

    protected transient NerdEcsManager ecsMan;

    private final LinkedList<NerdEcsComponent> COMPONENTS = new LinkedList<>();

    protected NerdEcsEntity() {
    }

    // region Dynamic component list queries. (PLEASE! No variadic overloads...)
    @SuppressWarnings("unchecked")
    public <ComponentT extends NerdEcsComponent> ComponentT getComponent(final Class<ComponentT> p_componentClass) {
        for (final NerdEcsComponent c : this.COMPONENTS)
            if (c.getClass().equals(p_componentClass))
                return (ComponentT) c;

        return null;
    }

    public <ComponentT extends NerdEcsComponent> ComponentT addComponent(final Class<ComponentT> p_componentClass) {
        ComponentT toRet = null;

        // region Construction!
        try {
            toRet = p_componentClass.getConstructor().newInstance();
        } catch (final InstantiationException e) {
            e.printStackTrace();
        } catch (final IllegalAccessException e) {
            e.printStackTrace();
        } catch (final IllegalArgumentException e) {
            e.printStackTrace();
        } catch (final InvocationTargetException e) {
            e.printStackTrace();
        } catch (final NoSuchMethodException e) {
            e.printStackTrace();
        } catch (final SecurityException e) {
            e.printStackTrace();
        }
        // endregion

        if (toRet != null) {
            this.COMPONENTS.add(toRet);
            this.ecsMan.addComponent(toRet);
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
            this.ecsMan.removeComponent(toRet);
        }

        return toRet;
    }
    // endregion

}
