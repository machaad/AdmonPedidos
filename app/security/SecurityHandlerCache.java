package security;


import be.objectify.deadbolt.java.ConfigKeys;
import be.objectify.deadbolt.java.DeadboltHandler;
import be.objectify.deadbolt.java.cache.HandlerCache;
import play.Play;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.Map;

@Singleton
public class SecurityHandlerCache implements HandlerCache
{
    private final DeadboltHandler defaultHandler = new SecurityDeadboltHandler();
    private final DeadboltHandler junitHandler = new JunitDeadboltHandler();
    private final Map<String, DeadboltHandler> handlers = new HashMap<>();

    public SecurityHandlerCache()
    {
        if(Play.isTest()){
            handlers.put(ConfigKeys.DEFAULT_HANDLER_KEY, junitHandler);
        }else{
            handlers.put(ConfigKeys.DEFAULT_HANDLER_KEY, defaultHandler);
        }
    }

    @Override
    public DeadboltHandler apply(final String key)
    {
        return handlers.get(key);
    }

    @Override
    public DeadboltHandler get()
    {
        return defaultHandler;
    }
}