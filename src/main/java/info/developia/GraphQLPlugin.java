package info.developia;

import io.javalin.config.JavalinConfig;
import io.javalin.plugin.Plugin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static info.developia.Tools.readResourceFile;

class GraphQLPlugin extends Plugin<Void> {
    private final Logger LOG = LoggerFactory.getLogger(GraphQLPlugin.class);
    private final GraphQLService graphQLService;
    private final GraphQLOptions graphQLOptions;

    public GraphQLPlugin(GraphQLOptions graphQLOptions) {
        this.graphQLOptions = graphQLOptions;
        this.graphQLService = new GraphQLService(graphQLOptions);
    }

    @Override
    public void onInitialize(JavalinConfig config) {
        config.router.mount(router -> router.post(graphQLOptions.path(), graphQLService::handleRequest));
        if (graphQLOptions.playground())
            config.router.mount(router -> {
                        router.get("/", ctx -> ctx.redirect(graphQLOptions.playgroundPath()));
                        router.get(graphQLOptions.playgroundPath(), ctx -> {
                            ctx.result(getPlaygroundHtml());
                            ctx.contentType("text/html");
                        });
                    }
            );
    }

    private String getPlaygroundHtml() throws IOException {
        return readResourceFile(graphQLOptions.playgroundHtmlFilename())
                .replace("{{PATH}}", graphQLOptions.path());
    }
}
