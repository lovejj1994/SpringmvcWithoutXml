package spring.springmvc01.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import spring.springmvc01.bean.Pan;
import spring.springmvc01.repository.PanRepository;

@Service
public class UserService implements UserDetailsService,BaseService{

	@Autowired
	@Qualifier("jdbcRepository")
	private PanRepository jdbcRepository;
	
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Pan pan = jdbcRepository.findHomeByUserName(username);
		List<String> roles = jdbcRepository.findRolesByUserName(username);
		
		if(pan!=null){
			List<GrantedAuthority> authorities = Lists.newArrayList();
			
			for(String s : roles)
				authorities.add(new SimpleGrantedAuthority(s));
			
			return new User(pan.getName(), pan.getPassWord(), authorities);
		}else{
			throw new UsernameNotFoundException("没有找到用户");
		}
		
	}
	
}
