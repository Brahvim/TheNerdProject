package com.brahvim.nerd.io.asset_loader;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import com.brahvim.nerd.papplet_wrapper.Sketch;

// Keeping this outside saves typing!:
public abstract class AssetLoader<AssetT> {
    private static final HashMap<Class<? extends AssetLoader<?>>, AssetLoader<?>> INSTANCES
    // ...We have `11` asset loaders for Processing's data structures!:
            = new HashMap<>(11);

    protected AssetLoader() {
    }

    // Thank ChatGPT for this!:
    public static <AssetT, LoaderT extends AssetLoader<AssetT>> LoaderT getInstance(Class<LoaderT> p_loaderClass) {
        // ...Do we have an instance in our pool?:
        if (!AssetLoader.INSTANCES.containsKey(p_loaderClass)) {
            try {
                // Instantiate the subclass and add it to the map!:
                AssetLoader.INSTANCES.put(
                        p_loaderClass, p_loaderClass.getDeclaredConstructor().newInstance());
            } catch (final InstantiationException | IllegalAccessException | NoSuchMethodException
                    | InvocationTargetException e) {
                e.printStackTrace();
                return null;
            }
        }

        // Cast and return the singleton instance!11!:
        return p_loaderClass.cast(AssetLoader.INSTANCES.get(p_loaderClass));
    }

    /**
     * @throws AssetLoaderFailedException when a failure occurs.
     * @throws IllegalArgumentException   if the options passed to the loader
     *                                    weren't meant for it.
     */
    public abstract AssetT fetchData(
            final Sketch p_sketch, final String p_path, final AssetLoaderOptions... p_options)
            throws AssetLoaderFailedException, IllegalArgumentException;

}
