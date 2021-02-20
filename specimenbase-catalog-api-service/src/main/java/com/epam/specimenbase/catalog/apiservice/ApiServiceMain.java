package com.epam.specimenbase.catalog.apiservice;

import com.epam.specimenbase.catalog.domain.GetUserDetails;
import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.plugin.openapi.OpenApiOptions;
import io.javalin.plugin.openapi.OpenApiPlugin;
import io.javalin.plugin.openapi.ui.SwaggerOptions;
import io.swagger.v3.oas.models.info.Info;

public final class ApiServiceMain {

    public static void main(String[] args) {
        // TODO: get port from command line parameters
        Javalin app = Javalin.create(
                javalinConfig -> javalinConfig.registerPlugin(new OpenApiPlugin(getOpenApiOptions()))
        ).start(8080);
        app.get("/", ApiServiceMain::handleGetUserDetails);
    }

    private static OpenApiOptions getOpenApiOptions() {
        Info applicationInfo = new Info()
                .version("1.0")
                .description("Specimen Base");
        return new OpenApiOptions(applicationInfo)
                .path("/swagger-docs")
                .swagger(new SwaggerOptions("/swagger").title("Specimen Base Documentation"));
    }

    private static void handleGetUserDetails(Context ctx) {
        GetUserDetails useCase = new GetUserDetails();
        var response = useCase.execute();
        ctx.json(response.getEmail());
    }
}
