package info.developia;

import graphql.ExecutionInput;
import graphql.GraphQL;
import graphql.schema.DataFetcher;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeRuntimeWiring;
import io.javalin.http.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

class GraphQLService {
    private final Logger LOG = LoggerFactory.getLogger(GraphQLService.class);
    private final GraphQL graphQL;

    public GraphQLService(GraphQLPlugin.Config pluginConfig) {
        graphQL = startGraphQL(pluginConfig);
    }

    private GraphQL startGraphQL(GraphQLPlugin.Config pluginConfig) {
        try {
            var schema = Files.readString(new File("src/main/resources/" + pluginConfig.schema).toPath());
            var wiring = getRuntimeWiring(pluginConfig.queries);
            return GraphQL.newGraphQL(new SchemaGenerator().makeExecutableSchema(new SchemaParser().parse(schema), wiring)).build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private RuntimeWiring getRuntimeWiring(Map<String, DataFetcher> queries) {
        return RuntimeWiring.newRuntimeWiring()
                .type(TypeRuntimeWiring.newTypeWiring("Query").dataFetchers(queries))
                .build();
    }

    void handleRequest(Context ctx) {
        Map<String, Object> request = ctx.bodyStreamAsClass(Map.class);
        var query = (String) request.get("query");
        var operationName = (String) request.get("operationName");
        var variables = (Map<String, Object>) request.getOrDefault("variables", Map.of());
        var result = graphQL.execute(ExecutionInput.newExecutionInput()
                .query(query)
                .operationName(operationName)
                .variables(variables)
                .build());
        ctx.json(result.toSpecification());
    }
}
