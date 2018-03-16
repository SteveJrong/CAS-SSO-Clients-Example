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
package com.cas.client.common.web;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.PrincipalCollection;

import com.alibaba.fastjson.JSONObject;
import com.cas.client.dto.CurrentUserInfoDTO;

/**
 * Controller基类
 * @author Steve Jrong
 * @since 1.0 - 2018年1月28日 下午7:20:13
 */
public class BaseController {
	
	/**
	 * Cas认证服务器返回的用户认证信息数组应有的长度
	 * </p>
	 * 符合此长度表明数据正确，否则数据异常
	 */
	private static final int ARRAY_LENGTH_OF_CAS_RETURNED_LOGINED_USER_INFOMATION_BY_LIST = 2;
	
	/**
	 * 获取已登录到系统中的账户信息
	 * @return CurrentUserInfoDTO
	 */
	protected synchronized CurrentUserInfoDTO getCurrentUserInfo() {
		return getRemoteCasUserPrincipalsToList().size() == ARRAY_LENGTH_OF_CAS_RETURNED_LOGINED_USER_INFOMATION_BY_LIST ?
				JSONObject.parseObject(JSONObject.toJSONString(getRemoteCasUserPrincipalsToList().get(1)), CurrentUserInfoDTO.class) : new CurrentUserInfoDTO();
	}
	
	/**
	 * 获取Cas认证服务器返回的用户信息并以List的方式返回
	 * @return List
	 * @see PrincipalCollection
	 */
	private List<?> getRemoteCasUserPrincipalsToList(){
		Optional<List<?>> remoteCasUserPrincipals = Optional.ofNullable(SecurityUtils.getSubject().getPrincipals().asList());
		return remoteCasUserPrincipals.orElse(new CopyOnWriteArrayList<>());
	}
}
