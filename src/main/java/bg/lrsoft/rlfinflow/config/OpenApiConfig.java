package bg.lrsoft.rlfinflow.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                contact = @Contact(
                        name = "Bryan S. K.",
                        url = "localhost:8080"
                ),
                description = "OpenApi documentation for RL-Fin-Flow project",
                title = "OpenApi specification - RL-FIN-FLOW-PROJECT",
                version = "1.0"
        )
)
public class OpenApiConfig {
}
