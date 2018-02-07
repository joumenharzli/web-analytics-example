/*
 * Copyright (C) 2018 Joumen Harzli
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 *
 */

package com.github.joumenharzli.shop.config;

import java.util.Arrays;
import java.util.Collections;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;

import com.github.joumenharzli.shop.web.error.RestErrorDto;
import com.github.joumenharzli.shop.web.error.RestFieldsErrorsDto;
import com.google.common.collect.Sets;

import io.swagger.annotations.ApiModel;
import springfox.documentation.builders.*;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger Configuration
 *
 * @author Joumen Harzli
 */
@Configuration
@ConditionalOnProperty(prefix = "application.swagger", value = "enabled")
@EnableSwagger2
public class SwaggerConfiguration {

  private final ApplicationProperties applicationProperties;

  public SwaggerConfiguration(ApplicationProperties applicationProperties) {
    this.applicationProperties = applicationProperties;
  }

  @Bean
  public Docket api() {
    return new Docket(DocumentationType.SWAGGER_2)
        .apiInfo(apiInfo())
        .globalOperationParameters(Collections.singletonList(acceptLanguageHeader()))
        .useDefaultResponseMessages(false)
        .globalResponseMessage(RequestMethod.GET, Arrays.asList(badRequest(), internalServerError()))
        .globalResponseMessage(RequestMethod.POST, Arrays.asList(badRequest(), internalServerError()))
        .groupName("api")
        .produces(Sets.newHashSet(MediaType.APPLICATION_JSON_VALUE))
        .consumes(Sets.newHashSet(MediaType.APPLICATION_JSON_VALUE))
        .select()
        .apis(RequestHandlerSelectors.basePackage("com.github.joumenharzli"))
        .paths(PathSelectors.any())
        .build();
  }

  private ApiInfo apiInfo() {
    return new ApiInfoBuilder()
        .title(applicationProperties.getSwagger().getTitle())
        .description(applicationProperties.getSwagger().getDescription())
        .contact(new Contact("Joumen Ali Harzli",
            applicationProperties.getSwagger().getRepositoryUrl(),
            ""))
        .license(applicationProperties.getSwagger().getLicenseName())
        .licenseUrl(applicationProperties.getSwagger().getLicenseUrl())
        .version(applicationProperties.getVersion())
        .build();
  }

  private Parameter acceptLanguageHeader() {
    if (applicationProperties.getLocalization().isEnabled()) {
      return new ParameterBuilder()
          .name("Accept-language")
          .description("Use the specified locale instead of English US")
          .modelRef(new ModelRef("string"))
          .parameterType("header")
          .required(false)
          .build();
    } else {
      return null;
    }
  }


  private ResponseMessage internalServerError() {
    return responseMessage(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Something unexpected went wrong", RestErrorDto.class);
  }

  private ResponseMessage badRequest() {
    return responseMessage(HttpStatus.BAD_REQUEST.value(), "Request content is invalid", RestFieldsErrorsDto.class);
  }

  private ResponseMessage responseMessage(int status, String message, Class clazz) {
    return new ResponseMessageBuilder()
        .code(status)
        .message(message)
        .responseModel(new ModelRef(AnnotationUtils.findAnnotation(clazz, ApiModel.class).value()))
        .build();
  }

}
