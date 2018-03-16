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
package com.cas.client.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cas.client.common.web.BaseController;

/**
 * 主页Controller
 * 
 * @author Steve Jrong
 * @since 1.0 - 2018年1月28日 下午7:37:43
 */
@Controller
public class IndexController extends BaseController {
	
	/**
	 * 主页
	 * @param request
	 * @param map
	 * @return
	 */
	@GetMapping("/index")
	public String toIndex(HttpServletRequest request, ModelMap map) {
		map.put("currentUser", super.getCurrentUserInfo());
		return "index";
	}
	
	@GetMapping("/home")
	public String toHome() {
		return "home";
	}
	
	/**
	 * 获取系统信息
	 * @param request
	 * @return
	 */
	@GetMapping("/info")
	public @ResponseBody Object toJson(HttpServletRequest request) {
		System.out.println(" -- 调用了info() -- ");
		return "{\"timestamp\":\"" + System.nanoTime() + "\",\"user\":\"" + request.getRemoteUser() + "\",\"sessionid\":\"" + request.getSession().getId() + "\"}";
	}
}
