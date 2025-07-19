package info.developia;

import graphql.schema.DataFetcher;
import io.javalin.Javalin;

public class Launcher {

    public static void main(String[] args) {
        var launcher = new Launcher();
        launcher.start();
    }

    private void start() {
        var graphQLOptions = GraphQLOptions.builder()
                .addPackage("io.javalin.examples")
                .registerQuery("hello", sayHello)
                .registerQuery("bye", sayBye).build();
        var server = Javalin.create(config -> {
            config.useVirtualThreads = true;
            config.registerPlugin(new GraphQLPlugin(graphQLOptions));
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
