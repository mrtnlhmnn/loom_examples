package de.accso.part1_threadlocal.framework;

import de.accso.part1_threadlocal.context.RegionCode;
import de.accso.part1_threadlocal.context.User;
import java.util.UUID;

public class Framework implements Callback {

    private Application app;

    public Framework(Application app) {
        this.app = app;
    }

    public  static final ThreadLocal<UUID>       correlationIdTL = new ThreadLocal<>();
    private static final ThreadLocal<RegionCode>    regionCodeTL = new ThreadLocal<>();
    private static final ThreadLocal<User>                userTL = new ThreadLocal<>();

    public void serveRequest(RegionCode regionCode, User user,
                             Request request)
    {
        // set context
        correlationIdTL.set(UUID.randomUUID());
        regionCodeTL.set(regionCode);
        userTL.set(user);

        app.handle((Callback) this, request);

        // need to remove the context explicitely
        correlationIdTL.remove();
        regionCodeTL.remove();
        userTL.remove();
    }

    @Override
    public RegionCode getRegion() {
        return regionCodeTL.get();
    }

    @Override
    public UUID getCorrelationId() {
        return correlationIdTL.get();
    }

    @Override
    public User getUser() {
        return userTL.get();
    }
}

