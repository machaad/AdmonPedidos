package security;

import be.objectify.deadbolt.java.cache.HandlerCache;
import play.api.Configuration;
import play.api.Environment;
import play.api.inject.Binding;
import play.api.inject.Module;
import scala.collection.Seq;
import javax.inject.Singleton;

/**
 * Created by Interax on 13/01/2016.
 */
public class SecurityDeadboltHook extends Module
{
    @Override
    public Seq<Binding<?>> bindings(final Environment environment,
                                    final Configuration configuration)
    {
        return seq(bind(HandlerCache.class).to(SecurityHandlerCache.class).in(Singleton.class));
    }
}