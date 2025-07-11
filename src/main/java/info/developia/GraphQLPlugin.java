package info.developia;

import graphql.schema.DataFetcher;
import io.javalin.config.JavalinConfig;
import io.javalin.plugin.Plugin;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;
import java.util.function.Consumer;

class GraphQLPlugin extends Plugin<GraphQLPlugin.Config> {
    private final Logger LOG = LoggerFactory.getLogger(GraphQLPlugin.class);
    private final GraphQLService graphQLService = new GraphQLService(pluginConfig);

    public GraphQLPlugin() {
        super(null, new Config());
    }

    public GraphQLPlugin(Consumer<Config> graphQlPluginConfig) {
        super(graphQlPluginConfig, new Config());
    }

    public static class Config {
        public String path = "/graphql";
        public String schema = "schema.graphqls";
        public boolean playground = true;
        public String playgroundPath = "/playground";
        public String playgroundHtmlFilename = "playground/index.html";
        public Map<String, DataFetcher> queries;
    }

    @Override
    public void onInitialize(JavalinConfig config) {
        config.router.mount(router -> router.post(pluginConfig.path, graphQLService::handleRequest));
        if (pluginConfig.playground)
            config.router.mount(router -> router.get(pluginConfig.playgroundPath, ctx -> {
                ctx.result(getPlaygroundHtml(pluginConfig));
                ctx.contentType("text/html");
            }));
    }

    private static String getPlaygroundHtml(Config pluginConfig) throws IOException {
        var playgroundHtmlFilename = "src/main/resources/" + pluginConfig.playgroundHtmlFilename;
        var html = Files.readString(new File(playgroundHtmlFilename).toPath());
        return html.replace("{{PATH}}", pluginConfig.path);
    }
}
