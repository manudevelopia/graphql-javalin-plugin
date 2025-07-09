package info.developia

import io.javalin.Javalin
import spock.lang.Specification

class GraphQLPluginTest extends Specification {

    def "application has a greeting"() {

        when:
        Javalin.create(config -> {
            config.registerPlugin(new GraphQLPlugin((userConfig) -> {
                userConfig.limit = 10
                userConfig.limites = 10
            }))
        }).start(7000)

        then:
        result != null
    }
}
