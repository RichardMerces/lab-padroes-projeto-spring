package dio.lab_padroes_projeto_spring;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class SwaggerUiAutoOpener {

    @Value("${swagger.auto-open:true}")
    private boolean autoOpen;

    @Value("${server.port:8080}")
    private int serverPort;

    @EventListener(ApplicationReadyEvent.class)
    public void openSwaggerUi() {
        if (!autoOpen) return;

        String swaggerUrl = "http://localhost:" + serverPort + "/swagger-ui/index.html";
        String os = System.getProperty("os.name").toLowerCase();

        try {
            ProcessBuilder builder;

            if (os.contains("win")) {
                // Windows
                builder = new ProcessBuilder("rundll32", "url.dll,FileProtocolHandler", swaggerUrl);
            } else if (os.contains("mac")) {
                // macOS
                builder = new ProcessBuilder("open", swaggerUrl);
            } else if (os.contains("nix") || os.contains("nux")) {
                // Linux
                builder = new ProcessBuilder("xdg-open", swaggerUrl);
            } else {
                System.out.println("Sistema operacional não suportado para abertura automática.");
                return;
            }

            builder.inheritIO(); // opcional: herda stdout/stderr
            builder.start();

        } catch (IOException e) {
            System.err.println("Erro ao tentar abrir Swagger UI: " + e.getMessage());
        }
    }
}