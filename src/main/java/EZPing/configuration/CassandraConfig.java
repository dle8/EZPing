package EZPing.configuration;

import org.springframework.cassandra.core.keyspace.CreateKeyspaceSpecification;
import org.springframework.context.annotation.Bean;
import org.springframework.data.cassandra.config.CassandraClusterFactoryBean;
import org.springframework.data.cassandra.config.java.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.mapping.BasicCassandraMappingContext;
import org.springframework.data.cassandra.mapping.CassandraMappingContext;

import java.util.Arrays;

public class CassandraConfig extends AbstractCassandraConfiguration {
    @Override
    protected String getKeyspaceName() {
        return "EZPing";
    }

    @Bean
    @Override
    public CassandraClusterFactoryBean cluster() {
        CassandraClusterFactoryBean cluster = new CassandraClusterFactoryBean();
        cluster.setContactPoints("localhost");
        cluster.setPort(9042);
        cluster.setKeyspaceCreations(
                Arrays.asList(
                        new CreateKeyspaceSpecification("EZPing")
                        .ifNotExists()
                        .withSimpleReplication(3)
                )
        );
        cluster.setStartupScripts(Arrays.asList(
                "USE EZPing",
                "CREATE TABLE IF NOT EXISTS messages (" +
                        "username text," +
                        "chatRoomId text, " +
                        "date timestamp," +
                        "fromUser text," +
                        "toUser text," +
                        "text text," +
                        "PRIMARY KEY ((username, chatRoomId), date)" +
                        ") WITH CLUSTERING ORDER BY (date ASC)"
                )
        );
        return cluster;
    }

    @Bean
    @Override
    public CassandraMappingContext cassandraMapping()
        throws ClassNotFoundException {
            return new BasicCassandraMappingContext();
        }
}