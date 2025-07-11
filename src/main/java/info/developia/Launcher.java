package info.developia;

import graphql.schema.DataFetcher;
import io.javalin.Javalin;

import java.util.Map;

public class Launcher {

//        var graphQLOption = GraphQLOptions("/graphql", ContextExample())
//                .addPackage("io.javalin.examples")
//                .register(QueryExample(message))
//                .register(MutationExample(message))
//                .register(SubscriptionExample())
//                .context()

    public static void main(String[] args) {
        var launcher = new Launcher();
        launcher.start();
    }

    private void start() {
        var server = Javalin.create(config -> {
            config.useVirtualThreads = true;
            config.registerPlugin(new GraphQLPlugin(graphQl -> {
                graphQl.path = "/graph";
                graphQl.queries = Map.of(
                        "hello", sayHello,
                        "bye", sayBye
                );
            }));
        });
        server.start(7000);
    }

    private final DataFetcher<String> sayHello = ctx -> {
        return "Hello " + ctx.getArgument("name");
    };

    private final DataFetcher<String> sayBye = ctx -> {
        return "Bye-bye " + ctx.getArgument("name");
    };

}
