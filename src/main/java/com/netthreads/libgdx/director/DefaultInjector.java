package com.netthreads.libgdx.director;

import com.netthreads.libgdx.sound.SoundCache;
import com.netthreads.libgdx.sound.SoundDefinitions;
import com.netthreads.libgdx.texture.SoundAssetManager;
import com.netthreads.libgdx.texture.TextureAssetManager;
import com.netthreads.libgdx.texture.TextureCache;
import com.netthreads.libgdx.texture.TextureDefinitions;

import java.util.HashMap;

/**
 * Default injector
 */
public class DefaultInjector implements Injector {
    private final HashMap<Class<?>, Creator<?>> creatorMap = new HashMap<Class<?>, Creator<?>>();
    private final HashMap<Class<?>, Object> instanceMap = new HashMap<Class<?>, Object>();

    public DefaultInjector withInstallDefault() {
        synchronized (creatorMap) {
            return (DefaultInjector) this
                    .bind(Director.class, new SimpleCreator<Director>())
                    .bind(SoundAssetManager.class, new Creator<SoundAssetManager>() {
                        @Override
                        public SoundAssetManager createInstance(Class<SoundAssetManager> type) {
                            return new SoundAssetManager(getInstance(SoundDefinitions.class));
                        }
                    })
                    .bind(SoundCache.class, new SimpleCreator<SoundCache>())
                    .bind(TextureAssetManager.class, new Creator<TextureAssetManager>() {
                        @Override
                        public TextureAssetManager createInstance(Class<TextureAssetManager> type) {
                            return new TextureAssetManager(getInstance(TextureDefinitions.class));
                        }
                    })
                    .bind(TextureCache.class, new SimpleCreator<TextureCache>());
        }
    }

    @Override
    public <T> Injector bind(Class<T> type, Creator<T> creator) {
        creatorMap.put(type, creator);
        return this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T getInstance(Class<T> type) {
        synchronized (creatorMap) {
            T instance = (T) instanceMap.get(type);
            if (instance == null) {
                Creator<T> creator = (Creator<T>) creatorMap.get(type);
                if (creator == null)
                    throw new IllegalStateException("'" + type.getName() + "' must be bound before getting");
                instance = creator.createInstance(type);
                instanceMap.put(type, instance);
            }
            return instance;
        }
    }

    public static class SimpleCreator<T> implements Creator<T> {
        @Override
        public T createInstance(Class<T> type) {
            try {
                return type.newInstance();
            } catch (InstantiationException e) {
                throw new IllegalArgumentException(e);
            } catch (IllegalAccessException e) {
                throw new IllegalArgumentException(e);
            }
        }
    }
}
