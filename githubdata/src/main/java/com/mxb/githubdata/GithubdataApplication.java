package com.mxb.githubdata;

import com.alibaba.cloud.nacos.ribbon.NacosRule;
import com.netflix.loadbalancer.IRule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import tk.mybatis.spring.annotation.MapperScan;

@MapperScan("com.mxb.githubdata.repository.mapper")
@SpringBootApplication
@EnableDiscoveryClient
public class GithubdataApplication {

	public static void main(String[] args) {
		SpringApplication.run(GithubdataApplication.class, args);
	}

	@Bean
	@Scope(value = "prototype")
	public IRule loadBalanceRule()
	{
		return new NacosRule();
	}

}
