package com.cas.client.config.cas;
/**
 * Copyright 2018 Steve Jrong - https://www.stevejrong.top

 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at

 *     http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
//package com.cas.client.config.system;
//
//import org.springframework.boot.web.servlet.FilterRegistrationBean;
//import org.springframework.context.annotation.Configuration;

//import net.unicon.cas.client.configuration.CasClientConfigurerAdapter;
//import net.unicon.cas.client.configuration.EnableCasClient;

/**
 * 自动配置的Cas客户端
 * 需要使用 cas-client-autoconfig-support 插件 
 * 
 * @author Steve Jrong
 * 
 * create date: 2018年1月22日 下午9:20:09
 */
/*@Configuration
@EnableCasClient
public class CasAutoConfiguration extends CasClientConfigurerAdapter {
	@Override
	public void configureAuthenticationFilter(FilterRegistrationBean authenticationFilter) {
		super.configureAuthenticationFilter(authenticationFilter);
		authenticationFilter.getInitParameters().put("authenticationRedirectStrategyClass",
				"com.patterncat.CustomAuthRedirectStrategy");
	}
}*/