package com.algo.ingite;

import org.apache.ignite.Ignition;
import org.apache.ignite.client.ClientCache;
import org.apache.ignite.client.IgniteClient;
import org.apache.ignite.configuration.ClientConfiguration;

public class ClientApp {
    public static void main(String[] args) {
        ClientConfiguration cfg=new ClientConfiguration().setAddresses("127.0.0.1:10800");
        try(IgniteClient client= Ignition.startClient(cfg)){
            ClientCache<Integer, String> cache=client.cache("mycache");
            String msg=cache.get(1);
            System.out.println(msg);
            client.close();
        }
    }
}