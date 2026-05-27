package org.example.database;

import org.example.config.Neo4jConfig;
import org.neo4j.driver.AuthTokens;
import org.neo4j.driver.Driver;
import org.neo4j.driver.GraphDatabase;

public final class Neo4jConnection implements AutoCloseable {
    private final Driver driver;

    public Neo4jConnection(Neo4jConfig config) {
        this.driver = GraphDatabase.driver(
                config.uri(),
                AuthTokens.basic(config.user(), config.password())
        );
    }

    public void verify() {
        driver.verifyConnectivity();
    }

    public int testQuery() {
        return driver.executableQuery("RETURN 1 AS result")
                .execute()
                .records()
                .getFirst()
                .get("result")
                .asInt();
    }

    public Driver driver() {
        return driver;
    }

    @Override
    public void close() {
        driver.close();
    }
}
