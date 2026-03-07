package lld;
import java.io.Serial;
import java.io.Serializable;

public final class BillPughSingleton implements Serializable {

    // 1. Private constructor with a standard reflection defense
    private BillPughSingleton() {
        // This perfectly defends against reflection attacks that happen AFTER
        // the application has naturally initialized the Singleton.
        if (SingletonHelper.INSTANCE != null) {
            throw new IllegalStateException("Singleton instance already exists!");
        }
    }

    // 2. The Inner Static Helper Class
    // The JVM guarantees this is only loaded into memory when getInstance() is called,
    // and natively guarantees 100% thread safety without synchronized blocks.
    private static class SingletonHelper {
        private static final BillPughSingleton INSTANCE = new BillPughSingleton();
    }

    // 3. Global access point
    public static BillPughSingleton getInstance() {
        return SingletonHelper.INSTANCE;
    }

    // 4. Serialization Defense (Made private so it doesn't leak into the API)
    @Serial
    private Object readResolve() {
        return getInstance();
    }

    // 5. Cloning Defense
    @Override
    protected Object clone() throws CloneNotSupportedException {
        throw new CloneNotSupportedException("Singleton cannot be cloned!");
    }
}