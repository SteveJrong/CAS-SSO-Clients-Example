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
package com.cas.client.config.shiro;

import java.util.HashSet;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.cas.CasRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import com.cas.client.config.cas.CasConfiguration;
import com.cas.client.service.UserService;

/**
 * 自定义的Shiro Realm类
 * 
 * @author Steve Jrong
 * @since 1.0 - 2018年1月30日 下午7:20:07
 * @see CasRealm
 */
public class CustomCasRealm extends CasRealm {

	@Autowired
	private UserService userService;
	
	/**
	 * 初始化自定义Realm时执行的方法
	 * <br/>在这里初始化自定义Realm的一些属性。如：Cas服务器的基本配置信息等
	 */
	@PostConstruct // 使用@PostConstruct注解后，此方法将会在加载Servlet时运行，与Servle中的init()方法等效，在系统启动时仅加载一次
    public void initProperty(){
        setCasServerUrlPrefix(CasConfiguration.casServerUrl);
        // 客户端回调地址
        setCasService(CasConfiguration.casClientDomainUrl + CasConfiguration.casClientFilterUrlPrefix);
    }
	
	/**
	 * 为登录到的用户赋角色和权限
	 * <br/>当用户访问需要授权的请求时会调用此方法，但并不是每次都会调用，调用过后有一定的时间限制，期间不再调用
	 */
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
		String loginUserName = (String) super.getAvailablePrincipal(principalCollection);
		boolean checkResult = userService.existsThisUser(loginUserName); // 到数据库查是否有此对象
		
		if (checkResult) {
			SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo(); // 创建存储用户的角色权限类
			// 用户的角色集合
			authorizationInfo.setRoles(new HashSet<String>(){
				private static final long serialVersionUID = -2453766267535072792L;
				{
					add("ROLE_SUPERADMIN");
				}
			});
			// 用户的角色对应的所有权限
			List<String> roleList = userService.getRoleListByUser(loginUserName);
			for (String role : roleList) {
				authorizationInfo.addStringPermission(role);
			}
			return authorizationInfo;
		}
		return null;
	}
}