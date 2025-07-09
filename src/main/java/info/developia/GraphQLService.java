package info.developia;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import graphql.ExecutionInput;
import graphql.GraphQL;
import graphql.schema.DataFetcher;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeRuntimeWiring;
import io.javalin.http.Context;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

class GraphQLService {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final GraphQL graphQL;

    public GraphQLService(GraphQLPlugin.Config pluginConfig) {
        graphQL = startGraphQL(pluginConfig.schema);
    }

    private GraphQL startGraphQL(String schemaFilename) {
        try {
            var schema = Files.readString(new File("src/main/resources/" + schemaFilename).toPath());
            var wiring = getRuntimeWiring();
            return GraphQL.newGraphQL(new SchemaGenerator().makeExecutableSchema(new SchemaParser().parse(schema), wiring)).build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static RuntimeWiring getRuntimeWiring() {
        Map<String, DataFetcher> queryFetchers = Map.of("hello", env -> "Hello " + env.getArgument("name"));
        return RuntimeWiring.newRuntimeWiring()
                .type(TypeRuntimeWiring.newTypeWiring("Query").dataFetchers(queryFetchers))
                .build();
    }

    void handleRequest(Context ctx) {
        var request = parse(ctx.body());
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

    private Map<String, Object> parse(String json) {
        try {
            return objectMapper.readValue(json, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
