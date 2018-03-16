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
package com.cas.client.config.cas;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.Filter;

import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.cas.CasFilter;
import org.apache.shiro.cas.CasSubjectFactory;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.jasig.cas.client.session.SingleSignOutFilter;
import org.jasig.cas.client.session.SingleSignOutHttpSessionListener;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.filter.DelegatingFilterProxy;

import com.cas.client.config.shiro.CustomCasRealm;

/**
 * Cas客户端配置
 * <p>用于配置受到Cas认证服务器保护的客户端基本信息配置，以确保客户端能够正常与Cas认证服务器进行交互验证。</p>
 * 
 * @author Steve Jrong
 * @since 1.0 - 2018年1月28日 下午2:52:20
 */
@Configuration
public class CasConfiguration {
	
	/**
	 * Cas认证服务器的访问地址
	 * <p>此地址包括域名和Cas认证服务器的项目名称</p>
	 * <p>例如：</p>
	 * <blockquote>
	 * 	<p>https://cas.server.com:8443/cas</p>
	 * 	<ul>
	 * 		<li>cas.server.com:8443/ 表示Cas认证服务器的域名</li>
	 * 		<li>/cas 表示Cas认证服务器的项目名称</li>
	 * </ul>
	 * </blockquote>
	 */
	public static final String casServerUrl = "https://cas.server.com:8443/cas";
	
	
	/**
	 * Cas认证服务器的登录请求访问前缀
	 * <p>例如：</p>
	 * <blockquote>
	 * 	<p>/login</p>
	 * </blockquote>
	 */
	public static final String casServerLoginPrefix = "/login";
	
	/**
	 * Cas认证服务器的退出的请求地址前缀
	 * <p>例如：</p>
	 * <blockquote>
	 * 	<p>/logout</p>
	 * </blockquote>
	 */
	public static final String casServerLogoutUrl = "/logout";
	
	/**
	 * Cas客户端暴露出的访问域名地址
	 * <p>此地址仅包括Cas客户端对外暴露进行访问的域名地址</p>
	 * <p>例如：</p>
	 * <blockquote>
	 * 	<p>http://cas.client.com:8081</p>
	 * </blockquote>
	 */
	public static final String casClientDomainUrl = "http://cas.client.com:8081";
	
	/**
	 * Cas客户端请求Cas认证服务器进行登录验证时的请求地址前缀
	 * <p>此地址前缀将在Cas客户端和Cas认证服务器信息交换时用到，同时在后续设置Apache Shiro的过滤器（shiroFilter）时需要将此前缀第一个加入到过滤规则中</p>
	 * <p>例如：</p>
	 * <blockquote>
	 * 	<p>/cas</p>
	 * </blockquote>
	 */
	public static final String casClientFilterUrlPrefix = "/cas";
	
	/**
	 * Cas客户端请求Cas认证服务器登录的地址
	 * <p>此退出地址的请求格式为：<br/>Cas认证服务器的访问地址 + Cas认证服务器的登录请求访问前缀 + "?service" + Cas客户端暴露出的访问域名地址 </p>
	 * <p>例如：</p>
	 * <blockquote>
	 * 	<p>https://cas.server.com:8443/cas/login?service=http://cas.client.com:8081/cas</p>
	 * </blockquote>
	 */
	public static final String casLoginUrl = casServerUrl + casServerLoginPrefix + "?service=" + casClientDomainUrl + casClientFilterUrlPrefix;
	
	/**
	 * Cas客户端请求Cas认证服务器退出的地址
	 * <p>此退出地址的请求格式为：<br/>Cas认证服务器的访问地址 + Cas认证服务器的退出的请求访问前缀 + "?service" + Cas客户端暴露出的访问域名地址 </p>
	 * <p>例如：</p>
	 * <blockquote>
	 * 	<p>https://cas.server.com:8443/cas/logout?service=http://cas.client.com:8081</p>
	 * </blockquote>
	 */
	public static final String casLogoutUrl = casServerUrl + casServerLogoutUrl + "?service=" + casClientDomainUrl;
	
	/**
	 * Cas客户端验证用户成功登录后跳转的地址前缀
	 * <p>例如：</p>
	 * <blockquote>
	 * 	<p>/home</p>
	 * </blockquote>
	 */
	public static final String loginSuccessPrefix = "/home";
	
	/**
	 * Cas客户端验证用户登录后无权访问后跳转的地址前缀
	 * <p>例如：</p>
	 * <blockquote>
	 * 	<p>/403</p>
	 * </blockquote>
	 */
	public static final String loginUnauthorizedPrefix = "/403";
	
	/**
	 * 设置EhCache缓存管理器以用于Shiro框架的信息数据缓存
	 * @return
	 */
	@Bean
	public EhCacheManager getEhCacheManager() {
		EhCacheManager ehCacheManager = new EhCacheManager();
		ehCacheManager.setCacheManagerConfigFile("classpath:ehcache-shiro.xml");
		return ehCacheManager;
	}
	
	/**
	 * 设置自定义的Realm数据域并将其添加到缓存管理器中
	 * @param cacheManager
	 * @return
	 */
	@Bean
    public CustomCasRealm myShiroCasRealm(EhCacheManager ehCacheManager) {
		CustomCasRealm realm = new CustomCasRealm();
        realm.setCacheManager(ehCacheManager);
        return realm;
    }
	
	/**
	 * 设置单点退出的监听器
	 * <p>建议放在所有监听器的首位</p>
	 * @return
	 */
    @Bean
    public ServletListenerRegistrationBean<SingleSignOutHttpSessionListener> singleSignOutHttpSessionListener(){
        ServletListenerRegistrationBean<SingleSignOutHttpSessionListener> bean = new ServletListenerRegistrationBean<SingleSignOutHttpSessionListener>();
        bean.setListener(new SingleSignOutHttpSessionListener());
        bean.setEnabled(true);
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE); //设置优先级
        return bean;
    }
    
    /**
     * 设置单点退出的过滤器
     * <p>建议放在所有过滤器的首位</p>
     * @return
     */
    @Bean
    public FilterRegistrationBean singleSignOutFilter(){
        FilterRegistrationBean bean = new FilterRegistrationBean();
        bean.setName("singleSignOutFilter");
        bean.setFilter(new SingleSignOutFilter());
        bean.addUrlPatterns("/*");
        bean.setEnabled(true);
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE); // 设定加载权重为最高
        return bean;
    }
    
    /**
     * 设置 DelegatingFilterProxy 以将Servlet容器中的过滤器和Bean进行关联
     * @return
     */
    @Bean
    public FilterRegistrationBean delegatingFilterProxy() {
        FilterRegistrationBean filterRegistration = new FilterRegistrationBean();
        filterRegistration.setFilter(new DelegatingFilterProxy("shiroFilter")); // 为 DelegatingFilterProxy 过滤器代理类设置一个过滤器
        filterRegistration.addInitParameter("targetFilterLifecycle", "true"); // 将shiroFilter所关联Bean的生命周期交给Spring来管理
        filterRegistration.setEnabled(true); // 启用 DelegatingFilterProxy 代理
        filterRegistration.addUrlPatterns("/*"); // 过滤规则设定为全部请求
        return filterRegistration;
    }
    
    /**
     * 设置 lifecycleBeanPostProcessor 类来自动的对Bean的生命周期进行管理
     * @return
     */
    @Bean(name = "lifecycleBeanPostProcessor")
    public LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }
    
    /**
     * 设置AOP功能
     * @return
     */
    @Bean
    public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator autoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        autoProxyCreator.setProxyTargetClass(true);
        return autoProxyCreator;
    }
    
    /**
     * 设置适用于Apache Shiro的安全管理器（securityManager）
     * @param myShiroCasRealm
     * @return
     */
    @Bean(name = "securityManager")
    public DefaultWebSecurityManager getDefaultWebSecurityManager(CustomCasRealm customCasRealm) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(customCasRealm); // 设定安全管理器的Realm域的实例
        securityManager.setCacheManager(getEhCacheManager()); // 设定安全管理器的缓存实例
        securityManager.setSubjectFactory(new CasSubjectFactory()); // 设定Cas的SubjectFactory
        return securityManager;
    }
    
    /**
     * 设置 AuthorizationAttributeSourceAdvisor 类以解析Apache Shiro注解
     * @param securityManager
     * @return
     */
    @Bean
    public AuthorizationAttributeSourceAdvisor getAuthorizationAttributeSourceAdvisor(DefaultWebSecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor attributeSourceAdvisor = new AuthorizationAttributeSourceAdvisor();
        attributeSourceAdvisor.setSecurityManager(securityManager); // 设定安全管理器的实例
        return attributeSourceAdvisor;
    }
    
    /**
     * 设置Cas过滤器
     * @return
     */
    @Bean(name = "casFilter")
    public CasFilter getCasFilter() {
        CasFilter casFilter = new CasFilter();
        casFilter.setName("casFilter");
        casFilter.setEnabled(true);
        casFilter.setFailureUrl(casLoginUrl); // 设定在Cas认证服务器登录失败后跳转的地址。这里设为了登录失败后再次跳转至Cas认证服务器的登录地址以重试登录。此时，Apache Shiro会执行CasRealm类中的身份验证方法向服务器验证票据的合法性
        return casFilter;
    }
    
    /**
     * 配置Apache Shiro过滤器
     * @param securityManager
     * @param casFilter
     * @return
     */
    @Bean(name = "shiroFilter")
    public ShiroFilterFactoryBean getShiroFilterFactoryBean(DefaultWebSecurityManager securityManager, CasFilter casFilter) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager); // 设定安全管理器的实例
        shiroFilterFactoryBean.setLoginUrl(casLoginUrl); // Cas客户端请求Cas认证服务器登录的地址 
        shiroFilterFactoryBean.setSuccessUrl(loginSuccessPrefix); // 从Cas认证服务器登录成功跳转的地址。因登录成功后已经在Cas客户端操作了，故为地址前缀
        shiroFilterFactoryBean.setUnauthorizedUrl(loginUnauthorizedPrefix); // 从Cas认证服务器登录后无权访问跳转的地址。因登录成功后已经在Cas客户端操作了，故为地址前缀。之前还会在客户端执行CasRealm类中的权限验证方法，去加载此账户应有的权限，最后判断有无权限
        Map<String, Filter> filters = new HashMap<String, Filter>();
        filters.put("casFilter", casFilter);
        shiroFilterFactoryBean.setFilters(filters); // 添加casFilter到shiroFilter中
        loadShiroFilterChain(shiroFilterFactoryBean);
        return shiroFilterFactoryBean;
    }

    /**
     * 设置 Apache Shiro 的请求拦截规则
     * <p>规则如下：</p>
     * <blockquote>
     * 	<ul>
     * 		<li>anon 表示对此请求不拦截，具有完全访问权限</li>
     * 		<li>authc 表示需要用户登录后才可访问此请求</li>
     * 		<li>roles["admin"] 表示需要用户登录且具有角色名称为“admin”的才可访问此请求</li>
     * 		<li>perms["delete"] 表示需要用户登录且具有权限名称为“delete”的才可访问此请求</li>
     * </ul>
     * </blockquote>
     * @param shiroFilterFactoryBean
     */
	private void loadShiroFilterChain(ShiroFilterFactoryBean shiroFilterFactoryBean) {
		Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
		filterChainDefinitionMap.put(casClientFilterUrlPrefix, "casFilter"); // Apache Shiro集成Cas之后，需要首先将Cas的规则添加进来
		filterChainDefinitionMap.put("/home", "anon"); // 访问/home主页请求时无需权限
		filterChainDefinitionMap.put("/**", "authc"); // 其他请求一律需要登录后访问
		shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
	}
}