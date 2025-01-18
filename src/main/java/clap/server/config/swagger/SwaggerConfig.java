//package clap.server.global.config.swagger;
//
//import clap.server.global.annotation.swagger.ApiErrorCodes;
//import io.swagger.v3.oas.models.Components;
//import io.swagger.v3.oas.models.OpenAPI;
//import io.swagger.v3.oas.models.Operation;
//import io.swagger.v3.oas.models.examples.Example;
//import io.swagger.v3.oas.models.info.Info;
//import io.swagger.v3.oas.models.media.Content;
//import io.swagger.v3.oas.models.media.MediaType;
//import io.swagger.v3.oas.models.responses.ApiResponse;
//import io.swagger.v3.oas.models.responses.ApiResponses;
//import io.swagger.v3.oas.models.security.SecurityRequirement;
//import io.swagger.v3.oas.models.security.SecurityScheme;
//import io.swagger.v3.oas.models.servers.Server;
//import org.springdoc.core.customizers.OperationCustomizer;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Profile;
//import org.springframework.web.method.HandlerMethod;
//
//import java.util.List;
//
//@Configuration
//public class SwaggerConfig {
//
//    @Value("${swagger.server.url}")
//    private String serverUrl;
//
//    @Bean
//    @Profile("local")
//    public OpenAPI localOpenAPI() {
//        return createOpenAPI(getLocalServer());
//    }
//
//    @Bean
//    @Profile("dev")
//    public OpenAPI devOpenAPI() {
//        return createOpenAPI(getDevServer());
//    }
//
//    private OpenAPI createOpenAPI(Server server) {
//        return new OpenAPI()
//                .components(new Components()
//                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
//                                .type(SecurityScheme.Type.HTTP)
//                                .scheme("bearer")
//                                .bearerFormat("JWT")
//                                .in(SecurityScheme.In.HEADER)
//                                .name("Authorization")))
//                .servers(List.of(server))
//                .info(new Info().title("CLAP API").version("1.0"))
//                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"));
//    }
//
//    private Server getLocalServer() {
//        return new Server()
//                .url(serverUrl)
//                .description("Local Server");
//    }
//
//    private Server getDevServer() {
//        return new Server()
//                .url(serverUrl)
//                .description("Development Server");
//    }
//
//    @Bean
//    public OperationCustomizer customize() {
//        return (Operation operation, HandlerMethod handlerMethod) -> {
//            ApiErrorCodes apiErrorCodes = handlerMethod.getMethodAnnotation(ApiErrorCodes.class);
//
//            if (apiErrorCodes != null) {
//                generateErrorCodeResponse(operation, apiErrorCodes.investmentErrors());
//            }
//
//            return operation;
//        };
//    }
//
//    private void generateErrorCodeResponse(Operation operation,  InvestmentErrorCode[] investmentErrors) {
//        ApiResponses responses = operation.getResponses();
//
//        if (investmentErrors != null) {
//            for (InvestmentErrorCode errorCode : investmentErrors) {
//                ErrorExampleHolder errorExampleHolder = getErrorExampleHolder(errorCode);
//                addExamplesToResponses(responses, errorExampleHolder); // 단일 에러 예제 추가
//            }
//        }
//    }
//
//    private ErrorExampleHolder getErrorExampleHolder(BaseErrorCode errorCode) {
//        return ErrorExampleHolder.builder()
//                .example(getSwaggerExample(errorCode))
//                .name(errorCode.getCustomCode())
//                .code(errorCode.getHttpStatus().value())
//                .build();
//    }
//
//    /**
//     * {@code @ApiErrorCodes} 어노테이션이 존재할 경우 {@code ApiResponses}에 {@code Example}를 추가하는 메소드
//     *
//     * @param responses
//     * @param errorExampleHolder
//     */
//    private void addExamplesToResponses(ApiResponses responses, ErrorExampleHolder errorExampleHolder) {
//        String responseKey = String.valueOf(errorExampleHolder.getCode());
//        ApiResponse apiResponse = responses.computeIfAbsent(responseKey, k -> new ApiResponse());
//
//        Content content = apiResponse.getContent();
//        if (content == null) {
//            content = new Content();
//            apiResponse.setContent(content);
//        }
//
//        MediaType mediaType = content.computeIfAbsent("application/json", k -> new MediaType());
//        mediaType.addExamples(errorExampleHolder.getName() , errorExampleHolder.getExample());
//        responses.addApiResponse(responseKey, apiResponse);
//    }
//
//    /**
//     * {@code BaseErrorCode}를 통해 {@code Example}를 생성하는 메소드
//     *
//     * @param errorCode
//     * @return Example 객체
//     */
//    private Example getSwaggerExample(BaseErrorCode errorCode) {
//        ApplicationResponse<Void> response = ApplicationResponse.onFailure(errorCode.getCustomCode(), errorCode.getMessage(), null);
//        Example example = new Example();
//        example.setValue(response);
//
//        return example;
//    }
//}