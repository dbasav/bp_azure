package com.cache.bp.bpcashed;



import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.AsyncConfigurerSupport;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;
import java.util.concurrent.Executor;

@SpringBootApplication
@EnableJpaAuditing
@EnableTransactionManagement
@EnableAsync
@EnableScheduling
@EnableSwagger2
@Slf4j
public class BpcashedApplication extends AsyncConfigurerSupport {

	public static void main(String[] args) {
		SpringApplication.run(BpcashedApplication.class, args);
	}

	@Override
	public Executor getAsyncExecutor() {
		log.debug("MsgReqService thread started");
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(2);
		executor.setMaxPoolSize(2);
		executor.setQueueCapacity(100);
		executor.setThreadNamePrefix("MsgReqService-");
		executor.initialize();
		return executor;
	}


	@Bean
	public Docket apiDocket() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.cache.bp.bpcashed"))
				.paths(PathSelectors.any())
				.build()
				.apiInfo(getApiInfo());
	}

	private ApiInfo getApiInfo() {
		ApiInfo apiInfo = new ApiInfo(
				"Webhook Service",
				"Example webhook service",
				"VERSION",
				"TERMS OF SERVICE URL",
				new Contact("NAME", "URL", "EMAIL"),
				"LICENSE",
				"LICENSE URL",
				Collections.emptyList()
		);
		return apiInfo;
	}

}
