package cl.rlabs.redis.query;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConfiguration.ClusterConfiguration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@SpringBootApplication
public class Application {
	
	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(Application.class, args);
        if (args.length==2 || args.length==3) {
			String host = args[0];
			String password = (args.length==2?null:args[1]);
			String key= (args.length==2?args[1]:args[2]);
			System.out.println("Using Redis host(s):"+host);
			if (password!=null) System.out.println("Using password:"+password);
			System.out.println("Querying: "+key);
			
			RedisTemplate<String, String> redisTemplate = redisTemplate(args[0],password);
            String output = redisTemplate.opsForValue().get(key);
			System.out.println("Result:");
			System.out.println(output);

			System.exit(0);
		} else {
			System.err.println("Expected params: host:port key");
			System.err.println("or");
			System.err.println("Expected params: host:port password key");
		}
	}

   private static JedisConnectionFactory jedisConnectionFactory(String redisNodes, String redisPassword) {
      if (redisNodes==null || redisNodes.indexOf(":")==-1) redisNodes = "localhost:6379";
      String[] nodes = redisNodes.split(",");
      if (nodes.length==1) {
        String[]node = nodes[0].split(":");
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(node[0], Integer.parseInt(node[1]));
        if (redisPassword!=null)
          configuration.setPassword(RedisPassword.of(redisPassword));
        return new JedisConnectionFactory(configuration);
      }

      ClusterConfiguration configuration = new RedisClusterConfiguration();
      for (int i=0; i<nodes.length; i++) {
        String[]node = nodes[i].split(":");
        ((RedisClusterConfiguration) configuration).addClusterNode(new RedisNode(node[0],Integer.parseInt(node[1])));
      }
      if (redisPassword!=null)
        configuration.setPassword(RedisPassword.of(redisPassword));
      return new JedisConnectionFactory((RedisClusterConfiguration)configuration);
   }
    
   private static RedisTemplate<String, String> redisTemplate(String redisNodes, String redisPassword) {
       RedisTemplate<String, String> template = new RedisTemplate<String,String>();
       JedisConnectionFactory factory = jedisConnectionFactory(redisNodes,redisPassword);
       factory.afterPropertiesSet();
       template.setConnectionFactory(factory);
       template.setKeySerializer(new StringRedisSerializer());
       template.setValueSerializer(new StringRedisSerializer());
       template.afterPropertiesSet();
       return template;
   }

}
