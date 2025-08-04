package com.algo.ingite;
import org.apache.ignite.*;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.events.CacheEvent;
import org.apache.ignite.events.DiscoveryEvent;
import org.apache.ignite.events.Event;
import org.apache.ignite.events.EventType;
import org.apache.ignite.lang.IgnitePredicate;

import javax.sql.ConnectionEvent;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class IgniteServer {
    public static void main(String[] args) {
        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.

        IgniteConfiguration config=new IgniteConfiguration();
        try(Ignite ignite= Ignition.start("ignite_config.xml")){
            ignite.configuration().getIncludeEventTypes();
            System.out.println("Ignite Server Started...");

            IgniteEvents events=ignite.events();
            IgnitePredicate<CacheEvent> localListener = evt -> {
                System.out.println("Received event [evt=" + evt.name() + ", key=" + evt.key() + ", oldVal=" + evt.oldValue()
                        + ", newVal=" + evt.newValue());
                System.out.println(evt.id() );
                System.out.println(evt.eventNode().hostNames());

                return true; // Continue listening.
            };

// Subscribe to the cache events that are triggered on the local node.
            events.localListen(localListener, EventType.EVT_CACHE_OBJECT_PUT, EventType.EVT_CACHE_OBJECT_READ,
                    EventType.EVT_CACHE_OBJECT_REMOVED);

            IgnitePredicate<Event> evtListener= evt ->{
                System.out.println("Received event evt= "+evt.name()+ ", id="+evt.id()+ " (str)="+evt);
                return true;
            };
            events.localListen(evtListener, EventType.EVT_CLIENT_NODE_RECONNECTED, EventType.EVT_CLIENT_NODE_DISCONNECTED);

            IgnitePredicate<DiscoveryEvent> nodeListener= evt ->{
                System.out.println("Received event evt= "+evt.name()+ ", id="+evt.id()+ " hostname="+ evt.eventNode().hostNames()+ " (str)="+evt);
                return true;
            };
            events.localListen(nodeListener, EventType.EVT_NODE_JOINED);



            IgniteCluster cluster=ignite.cluster();
            ignite.compute(cluster).broadcast(
                    () -> System.out.println(">>> Hello Node: "+cluster.ignite().cluster().localNode().id())
            );

            IgniteCache<Integer, String> cache=ignite.getOrCreateCache("mycache");
            cache.put(1, "test");
            String message=cache.get(1);
            System.out.println("message: "+message);
            while(true){
                Thread.sleep(1000l);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

//        IgniteCache<Integer, String> cache=ignite.cache("mycache");
//        cache.put(1, "test");
//        String message=cache.get(1);


    }
}