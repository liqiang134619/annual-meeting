package com.luopan.annualmeeting.config;

import com.luopan.annualmeeting.interceptor.AnnualMeetingEndInterceptor;
import com.luopan.annualmeeting.interceptor.AnnualMeetingStartInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class MyWebConfig implements WebMvcConfigurer {

  @Override
  public void addInterceptors(InterceptorRegistry registry) {

  }

  @Bean
  public AnnualMeetingStartInterceptor annualMeetingStartInterceptor() {
    return new AnnualMeetingStartInterceptor();
  }

  @Bean
  public AnnualMeetingEndInterceptor annualMeetingEndInterceptor() {
    return new AnnualMeetingEndInterceptor();
  }

  /*****************swagger2配置********************/
  @Bean
  public Docket createRestApi() {
    return new Docket(DocumentationType.SWAGGER_2)
        .apiInfo(apiInfo())
        .select()
        .apis(RequestHandlerSelectors.basePackage("com.luopan.annualmeeting.controller"))
        .paths(PathSelectors.any())
        .build();
  }

  private ApiInfo apiInfo() {
    return new ApiInfoBuilder()
        .title("年会小程序api文档")
        .version("1.0")
        .build();
  }

}
