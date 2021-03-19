package com.epam.specimenbase.catalog.apiservice;

import com.epam.specimenbase.catalog.apiservice.endpoints.UserController;
import com.epam.specimenbase.catalog.domain.InvalidCredentialsException;
import com.epam.specimenbase.catalog.ports.UseCaseFactory;
import io.javalin.Javalin;
import io.javalin.plugin.openapi.OpenApiOptions;
import io.javalin.plugin.openapi.OpenApiPlugin;
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
        app.get("/static/sign-up", new VueComponent("<sign-up></sign-up>"));

        UserController userController = new UserController(useCaseFactory);
        app.get("/user-details", userController::getUserDetails);
        app.post("/register-user", userController::registerUser);
        app.post("/login", userController::logInUser);
        app.post("/logout", userController::logOut);

        app.exception(InvalidCredentialsException.class, (e, ctx) -> ctx.status(HttpStatus.SC_UNAUTHORIZED));
    }

    private OpenApiOptions getOpenApiOptions() {
        Info applicationInfo = new Info()
                .version("1.0")
                .description("Specimen Base");
        return new OpenApiOptions(applicationInfo)
                .path("/swagger-docs")
                .swagger(new SwaggerOptions("/swagger").title("Specimen Base Documentation"));
    }
}
