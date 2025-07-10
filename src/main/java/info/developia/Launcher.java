package info.developia;

import io.javalin.Javalin;

public class Launcher {
    public static void main(String[] args) {
//        var graphQLOption = GraphQLOptions("/graphql", ContextExample())
//                .addPackage("io.javalin.examples")
//                .register(QueryExample(message))
//                .register(MutationExample(message))
//                .register(SubscriptionExample())
//                .context()

        Javalin.create(config -> {
            config.useVirtualThreads = true;
            config.registerPlugin(new GraphQLPlugin());
//            config.registerPlugin(new GraphQLPlugin(graphQl -> graphQl.path = "/graph"));
        }).start(7000);
    }
}
