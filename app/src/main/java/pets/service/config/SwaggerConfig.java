package pets.service.config;

import static springfox.documentation.builders.PathSelectors.regex;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import pets.service.exception.ResourceNotFoundException;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
  @Bean
  public Docket docket() {
    return new Docket(DocumentationType.SWAGGER_2)
        .apiInfo(apiInfo())
        .select()
        .paths(
            regex(
                ".*/accounts.*|.*/accounttypes.*|.*/banks.*|.*/categories.*|.*/categorytypes.*|.*/merchants.*|.*/transactiontypes.*"))
        .build();
  }

  private ApiInfo apiInfo() {
    return new ApiInfoBuilder()
        .title("Service Layer for PETS App")
        .description("Service Layer for PETS App")
        .contact(new Contact("Bibek Aryal", "", ""))
        .license("Personal Use Only")
        .version("1.0.1")
        .build();
  }

  @Controller
  public static class SwaggerRedirectControllerDevelopment {
    @GetMapping("/")
    public String redirectToSwagger() {
      return "redirect:/swagger-ui/";
    }
  }

  @Controller
  @Profile(value = "production")
  public static class SwaggerRedirectControllerProduction {
    @GetMapping("/swagger-ui")
    public ResponseEntity<ResourceNotFoundException> redirectFromSwagger() {
      throw new ResourceNotFoundException("Swagger Not Available!!!");
    }
  }
}
