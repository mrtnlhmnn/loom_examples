package de.accso.loom.part2_scopedvalues.framework;

import de.accso.loom.part2_scopedvalues.context.RegionCode;
import de.accso.loom.part2_scopedvalues.context.User;
import java.util.UUID;

public class Framework implements Callback {

    private Application app;

    public Framework(Application app) {
        this.app = app;
    }

    private static final ScopedValue<UUID>       correlationIdCtx = ScopedValue.newInstance();
    private static final ScopedValue<RegionCode>    regionCodeCtx = ScopedValue.newInstance();
    private static final ScopedValue<User>                userCtx = ScopedValue.newInstance();

    public void serveRequest(RegionCode regionCode, User user,
                             Request request)
    {
        ScopedValue
                    // set context
                   .where(correlationIdCtx, correlationIdCtx.isBound() ? getCorrelationId() : UUID.randomUUID())
                   .where(regionCodeCtx,    regionCode)
                   .where(userCtx,          user)
              .run(() ->
                  app.handle((Callback) this, request)
              );

        // no need to remove context explicitely, context is no longer bound!
        assert( ! correlationIdCtx.isBound() );
        assert( !    regionCodeCtx.isBound() );
        assert( !          userCtx.isBound() );
    }

    @Override
    public RegionCode getRegion() {
        return regionCodeCtx.get();
    }

    @Override
    public UUID getCorrelationId() {
        return correlationIdCtx.get();
    }

    @Override
    public User getUser() {
        return userCtx.get();
    }
}

