package com.cache.bp.bpcashed.util;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisShardInfo;

public class App {
    public static void main( String[] args )
    {

        boolean useSsl = false;
        String cacheHostname = "bpcache.redis.cache.windows.net";//System.getenv("REDISCACHEHOSTNAME");
        String cachekey = "4joPjj9SrCA6YVreZrv5SyJYrS+Wnrih8eJ5vOIl5YQ=" ; //System.getenv("REDISCACHEKEY");

        // Connect to the Azure Cache for Redis over the SSL port using the key.
        JedisShardInfo shardInfo = new JedisShardInfo(cacheHostname, 6379, useSsl);
        shardInfo.setPassword(cachekey); /* Use your access key. */
        Jedis jedis = new Jedis(shardInfo);

        // Perform cache operations using the cache connection object...

        // Simple PING command
        System.out.println( "\nCache Command  : Ping" );
        System.out.println( "Cache Response : " + jedis.ping());

        // Simple get and put of integral data types into the cache
        System.out.println( "\nCache Command  : GET Message" );
        System.out.println( "Cache Response : " + jedis.get("Message"));

        System.out.println( "\nCache Command  : SET Message" );
        System.out.println( "Cache Response : " + jedis.set("Message", "Hello! The cache is working from Java!"));

        // Demonstrate "SET Message" executed as expected...
        System.out.println( "\nCache Command  : GET Message" );
        System.out.println( "Cache Response : " + jedis.get("Message"));

        // Get the client list, useful to see if connection list is growing...
        System.out.println( "\nCache Command  : CLIENT LIST" );
        System.out.println( "Cache Response : " + jedis.clientList());

        jedis.close();
    }
}
