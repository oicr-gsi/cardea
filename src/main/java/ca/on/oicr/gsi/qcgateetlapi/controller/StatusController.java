package ca.on.oicr.gsi.qcgateetlapi.controller;

import ca.on.oicr.gsi.status.ConfigurationSection;
import ca.on.oicr.gsi.status.Header;
import ca.on.oicr.gsi.status.NavigationMenu;
import ca.on.oicr.gsi.status.SectionRenderer;
import ca.on.oicr.gsi.status.ServerConfig;
import ca.on.oicr.gsi.status.StatusPage;
import java.io.IOException;
import java.util.stream.Stream;
import java.io.OutputStream;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class StatusController{
  public static final ServerConfig SERVER_CONFIG =
      new ServerConfig() {

        @Override
        public String documentationUrl() {
          return "swagger-ui.html";
        }

        @Override
        public Stream<Header> headers() {
          return Stream.empty();
        }

        @Override
        public String name() {
          return "QC Gate ETL API";
        }

        @Override
        public Stream<NavigationMenu> navigation() {
          return Stream.empty();
        }
      };

  @GetMapping
  public void showStatus(HttpServletResponse response) throws IOException {
    response.setContentType("text/html;charset=utf-8");
    response.setStatus(HttpServletResponse.SC_OK);
    try (OutputStream output = response.getOutputStream()) {
      new StatusPage(SERVER_CONFIG) {

        @Override
        protected void emitCore(SectionRenderer renderer) {
        }

        @Override
        public Stream<ConfigurationSection> sections() {
          return Stream.empty();
        }
      }.renderPage(output);
    }
  }
}
