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
package com.cas.client.dto;

import java.io.Serializable;

/**
 * DTO - 登录后的用户DTO
 * @author Steve Jrong
 * create date: 2018年1月27日 下午3:35:25
 */
public class CurrentUserInfoDTO implements Serializable {
	
	private static final long serialVersionUID = -3414705839767338809L;

	private String userIdentity;
	
	private String userFullName;
	
	private String userPassword;
	
	private String userGender;
	
	private String userAge;
	
	private String userModifyDate;
	
	private String userCreateDate;

	public String getUserIdentity() {
		return userIdentity;
	}

	public void setUserIdentity(String userIdentity) {
		this.userIdentity = userIdentity;
	}

	public String getUserFullName() {
		return userFullName;
	}

	public void setUserFullName(String userFullName) {
		this.userFullName = userFullName;
	}

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public String getUserGender() {
		return userGender;
	}

	public void setUserGender(String userGender) {
		this.userGender = userGender;
	}

	public String getUserAge() {
		return userAge;
	}

	public void setUserAge(String userAge) {
		this.userAge = userAge;
	}

	public String getUserModifyDate() {
		return userModifyDate;
	}

	public void setUserModifyDate(String userModifyDate) {
		this.userModifyDate = userModifyDate;
	}

	public String getUserCreateDate() {
		return userCreateDate;
	}

	public void setUserCreateDate(String userCreateDate) {
		this.userCreateDate = userCreateDate;
	}
}