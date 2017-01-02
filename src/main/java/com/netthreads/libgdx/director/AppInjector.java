package com.netthreads.libgdx.director;

/**
 * Central injector which holds our singletons.
 */
public class AppInjector {
    /**
     * Instance injector.
     */
    private static Injector injector = null;

    public static synchronized Injector getInjector() {
        if (injector == null) {
            injector = new DefaultInjector().withInstallDefault();
        }

        return injector;
    }

    private static synchronized void setInjector(Injector injector) {
        if (AppInjector.injector != null) throw new IllegalStateException("Already configured");
        AppInjector.injector = injector;
    }

    public static Configuration configure() {
        return new Configuration();
    }

    public static class Configuration {
        private Injector injector;

        Configuration() {
        }

        public Configuration setInjector(Injector injector) {
            this.injector = injector;
            return this;
        }

        public void commit() {
            AppInjector.setInjector(injector);
        }
    }
}
