package de.accso.part1_threadlocal.framework;

import de.accso.part1_threadlocal.context.RegionCode;
import de.accso.part1_threadlocal.context.User;
import java.util.UUID;

public interface Callback {
    UUID       getCorrelationId();
    RegionCode getRegion();
    User       getUser();
}