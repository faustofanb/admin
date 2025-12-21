package io.github.faustofan.admin.shared.common.context;

/**
 * Holder for the AppContext using ThreadLocal storage.
 */
public class AppContextHolder {

    // InheritableThreadLocal to allow child threads to inherit the context
    private static final ThreadLocal<AppContext> CONTEXT = new InheritableThreadLocal<>();

    // Private constructor to prevent instantiation
    private AppContextHolder() {
    }

    /**
     * Get the current AppContext.
     * @return the current AppContext
     */
    public static AppContext getContext() {
        return CONTEXT.get();
    }

    /**
     * Set the current AppContext.
     * @param context the AppContext to set
     */
    public static void setContext(AppContext context) {
        CONTEXT.set(context);
    }

    /**
     * Clear the current AppContext.
     */
    public static void clearContext() {
        CONTEXT.remove();
    }
}
