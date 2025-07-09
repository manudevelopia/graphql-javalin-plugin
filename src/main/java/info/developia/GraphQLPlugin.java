package info.developia;

import io.javalin.config.JavalinConfig;
import io.javalin.plugin.Plugin;

import java.util.function.Consumer;

class GraphQLPlugin extends Plugin<GraphQLPlugin.Config> {
    private GraphQLService graphQLService;

    public GraphQLPlugin() {
        super(null, new Config());
    }

    public GraphQLPlugin(Consumer<Config> graphQlPluginConfig) {
        super(graphQlPluginConfig, new Config());
    }

    public static class Config {
        public String path = "/graphql";
        public String schema = "schema.graphqls";
    }

    @Override
    public void onInitialize(JavalinConfig config) {
        config.router.mount(router -> {
            router.post(pluginConfig.path, ctx -> graphQLService.handleRequest(ctx));
            graphQLService = new GraphQLService(pluginConfig);
        });
    }
}
