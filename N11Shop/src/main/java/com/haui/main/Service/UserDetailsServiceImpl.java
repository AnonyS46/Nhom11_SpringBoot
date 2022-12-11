package com.haui.main.Service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.haui.main.Dao.RoleDao;
import com.haui.main.Dao.UserDao;

import org.springframework.security.core.userdetails.User;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private RoleDao roleDao;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		com.haui.main.Entity.User appUser = this.userDao.findByEmail(username);
			
		if(appUser == null) {
			System.out.println("User not found! "+username);
			throw new UsernameNotFoundException("User " + username + " was not found in database");		
		}
		else {
			System.out.println("User found! "+username);
		}
		
	//	System.out.println("Found User: "+appUser);
		
		List<String> roleNames = this.roleDao.getRoleNames(appUser.getId());
		List<GrantedAuthority> grandList = new ArrayList<GrantedAuthority>();
		
		if(roleNames!=null) {
			for(String role: roleNames) {
				GrantedAuthority authority = new SimpleGrantedAuthority(role);
				grandList.add(authority);
			}
		}
		
		UserDetails userDetails = (UserDetails) new User(appUser.getEmail(), appUser.getPassword(), grandList);
		
		return userDetails;
	}

}
