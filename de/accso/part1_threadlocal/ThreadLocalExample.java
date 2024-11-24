package de.accso.part1_threadlocal;

import de.accso.part1_threadlocal.context.RegionCode;
import de.accso.part1_threadlocal.context.User;
import de.accso.part1_threadlocal.framework.Application;
import de.accso.part1_threadlocal.framework.Framework;
import de.accso.part1_threadlocal.framework.Request;

import static de.accso.part1_threadlocal.context.RegionCode.NA;

public class ThreadLocalExample {
    public static void main(String[] args) {
        Framework framework = setupFramework();

        sendRequest(framework);
    }

    private static Framework setupFramework() {
        Application application = new MyApp();
        Framework framework = new Framework(application);
        return framework;
    }

    private static void sendRequest(Framework framework) {
        // these scoped values could come from a config setting or from a user login, respectively
        RegionCode regionCode = NA;
        User user = new User("jane123", true);

        Request request = new Request("Jane", "Doe", "New York");
        framework.serveRequest(regionCode, user, request);
    }
}
