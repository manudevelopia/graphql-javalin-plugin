package info.developia;

import io.javalin.Javalin;

public class Launcher {
    public static void main(String[] args) {
        Javalin.create(config -> {
            config.registerPlugin(new GraphQLPlugin());
//            config.registerPlugin(new GraphQLPlugin(graphQl -> graphQl.path = "/graphql"));
        }).start(7000);
    }
}
