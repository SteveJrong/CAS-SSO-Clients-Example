package com.cas.client.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.cas.client.service.UserService;

@Service("userService")
public class UserServiceImpl implements UserService {

	@Override
	public boolean existsThisUser(String userName) {
		return true;
	}

	@Override
	public List<String> getRoleListByUser(String userName) {
		return new ArrayList<String>(){
			private static final long serialVersionUID = 4801068401111275487L;
			{
				add("manage");
			}
		};
	}
}
