package com.zcloud.realm;

import java.util.Collection;
import java.util.HashSet;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import com.zcloud.entity.PageData;
import com.zcloud.service.system.UsersService;
import com.zcloud.util.Const;
import com.zcloud.util.Jurisdiction;
 
/**
 * 说明：Shiro身份认证
 * 作者：luoxiaobao
 * 官网：www.qdkjchina.com
 */
public class MyShiroRealm extends AuthorizingRealm {
 
    @Autowired
    @Lazy
    private UsersService usersService;
    /**
     * 登录认证
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken)authenticationToken; 	//UsernamePasswordToken用于存放提交的登录信息
	    PageData pd = new PageData();
	    pd.put("USERNAME", token.getUsername());
			try {
				pd = usersService.findByUsername(pd);
				if (pd != null){
				    return new SimpleAuthenticationInfo(pd.getString("USERNAME"), pd.getString("PASSWORD"), getName());
		        }
			} catch (Exception e) {
				return null;
			}
		return null;
    }
	@Override
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
		String USERNAME = (String) super.getAvailablePrincipal(principals);
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
		Session session = Jurisdiction.getSession();
		Collection<String> shiroSet= new HashSet<String>();
		shiroSet = (Collection<String>)session.getAttribute(USERNAME + Const.SHIROSET);
		if(null != shiroSet){
			info.addStringPermissions(shiroSet);
			return info;
		}else {
			return null;
		}
	}

}
