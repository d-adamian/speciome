package com.epam.specimenbase.catalog.apiservice;

import com.epam.specimenbase.catalog.ports.UseCaseFactory;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.plugin.openapi.OpenApiOptions;
import io.javalin.plugin.openapi.OpenApiPlugin;
import io.javalin.plugin.openapi.dsl.OpenApiBuilder;
import io.javalin.plugin.openapi.dsl.OpenApiDocumentation;
import io.javalin.plugin.openapi.ui.SwaggerOptions;
import io.javalin.plugin.rendering.vue.VueComponent;
import io.swagger.v3.oas.models.info.Info;
import org.apache.http.HttpStatus;

public final class ApiServiceMain {
    private final UseCaseFactory useCaseFactory;

    public ApiServiceMain() {
        useCaseFactory = new ServiceUseCaseFactory();
    }

    public static void main(String[] args) {
        // TODO: get port from command line parameters
        new ApiServiceMain().startService(8080);
    }

    private void startService(int port) {
        Javalin app = Javalin.create(
                config -> {
                    config.enableWebjars();
                    config.registerPlugin(new OpenApiPlugin(getOpenApiOptions()));
                }
        ).start(port);
        app.get("/", new VueComponent("<main-page></main-page>"));

        OpenApiDocumentation userDetailsDoc = OpenApiBuilder.document()
                .result(Integer.toString(HttpStatus.SC_OK), String.class);
        app.get("/user-details", OpenApiBuilder.documented(userDetailsDoc, this::handleGetUserDetails));
    }

    private OpenApiOptions getOpenApiOptions() {
        Info applicationInfo = new Info()
                .version("1.0")
                .description("Specimen Base");
        return new OpenApiOptions(applicationInfo)
                .path("/swagger-docs")
                .swagger(new SwaggerOptions("/swagger").title("Specimen Base Documentation"));
    }

    private void handleGetUserDetails(Context ctx) {
        var response = useCaseFactory.getUserDetails().getUserDetails(null);
        ctx.json(response.getEmail());
    }
}
