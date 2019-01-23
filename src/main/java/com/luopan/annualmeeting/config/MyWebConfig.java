package com.luopan.annualmeeting.config;

import com.luopan.annualmeeting.interceptor.AnnualMeetingEndInterceptor;
import com.luopan.annualmeeting.interceptor.AnnualMeetingStartInterceptor;
import com.luopan.annualmeeting.interceptor.CompanyInterceptor;
import com.luopan.annualmeeting.task.MyJobFactory;
import org.quartz.Scheduler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@Configuration
public class MyWebConfig implements WebMvcConfigurer {

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(companyInterceptor()).addPathPatterns("/**")
        .excludePathPatterns("/user/login") // 登录
        .excludePathPatterns("/person/faceSignIn") //人脸识别签到
        .excludePathPatterns("/swagger-resources/**", "/webjars/**", "/v2/**",
            "/swagger-ui.html/**"); // swagger
  }
  
  @Bean
  public AnnualMeetingStartInterceptor annualMeetingStartInterceptor() {
    return new AnnualMeetingStartInterceptor();
  }

  @Bean
  public AnnualMeetingEndInterceptor annualMeetingEndInterceptor() {
    return new AnnualMeetingEndInterceptor();
  }

  @Bean
  public CompanyInterceptor companyInterceptor() {
    return new CompanyInterceptor();
  }

  /**
   * websocket
   */
  @Bean
  public ServerEndpointExporter serverEndpointExporter() {
    return new ServerEndpointExporter();
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

  /*****************quartz配置********************/
  @Bean
  public MyJobFactory myJobFactory() {
    return new MyJobFactory();
  }

  @Bean
  public SchedulerFactoryBean schedulerFactoryBean() {
    SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
    schedulerFactoryBean.setJobFactory(myJobFactory());
    return schedulerFactoryBean;
  }

  @Bean
  public Scheduler scheduler() {
    return schedulerFactoryBean().getScheduler();
  }

}
