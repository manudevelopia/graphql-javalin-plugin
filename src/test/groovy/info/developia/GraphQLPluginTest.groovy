package info.developia

import graphql.schema.DataFetcher
import io.javalin.Javalin
import spock.lang.Specification

class GraphQLPluginTest extends Specification {

    def "application has a greeting"() {
        given:
        var graphQLOptions = GraphQLOptions.builder()
                .addPackage('io.javalin.examples')
                .registerQuery('hello', sayHello)
                .registerQuery('bye', sayBye).build()
        when:
        var server = Javalin.create(config -> {
            config.registerPlugin(new GraphQLPlugin(graphQLOptions))
        }).start(7000)
        then:
        noExceptionThrown()
        cleanup:
        server.stop()
    }

    private final DataFetcher<String> sayHello = ctx -> {
        return "Hello ${ctx.getArgument('name')}"
    }

    private final DataFetcher<String> sayBye = ctx -> {
        return "Bye-bye ${ctx.getArgument('name')}"
    }
}
