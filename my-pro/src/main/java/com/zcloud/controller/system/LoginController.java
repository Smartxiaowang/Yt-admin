package com.zcloud.controller.system;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zcloud.controller.base.BaseController;
import com.zcloud.entity.PageData;
import com.zcloud.entity.system.Role;
import com.zcloud.entity.system.User;
import com.zcloud.service.system.DepartmentService;
import com.zcloud.service.system.FHlogService;
import com.zcloud.service.system.RoleService;
import com.zcloud.service.system.UsersService;
import com.zcloud.util.BasicInfoUtil;
import com.zcloud.util.Const;
import com.zcloud.util.Jurisdiction;
import com.zcloud.util.Tools;

import net.sf.json.JSONObject;

/**
 * 说明：登录处理类
 * 作者：luoxiaobao
 * 官网：www.qdkjchina.com
 */
@Controller
@RequestMapping("/admin")
public class LoginController extends BaseController {
	
	@Autowired
    private UsersService usersService;
	@Autowired
    private RoleService roleService;
	@Autowired
    private FHlogService FHLOG;
	@Autowired
	private DepartmentService departmentService;
	
	/**请求登录验证管理用户接口
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/adminCheck", produces="application/json;charset=UTF-8")
	@ResponseBody
    public Object adminCheck() {
		Map<String,String> map = new HashMap<String,String>();
        try {
    		PageData pd = new PageData();
    		pd = this.getPageData();
    		String errInfo = "success";
    		String uri = "http://192.168.0.7:8082/zcouldProperty/admin/check";
    		String KEYDATA[] = pd.getString("KEYDATA").replaceAll("qdkjchina", "").split(",qd,");
    		if(null != KEYDATA && KEYDATA.length == 2){
    			String USERNAME = KEYDATA[0];							//登录过来的用户名
    			String PASSWORD  = KEYDATA[1];							//登录过来的密码
	            CloseableHttpClient client = null;
	            CloseableHttpResponse response = null;
	            try {
	                ObjectMapper objectMapper = new ObjectMapper();
	                Map<String, Object> data = new HashMap<String, Object>();
	                data.put("KEYDATA","qdkjchina"+ USERNAME +",qd," + PASSWORD );
	                data.put("tm", new Date().getTime());
	
	                HttpPost httpPost = new HttpPost(uri);
	                httpPost.setHeader(HTTP.CONTENT_TYPE, "application/x-www-form-urlencoded; charset=UTF-8");
//	                httpPost.setEntity(new StringEntity(objectMapper.writeValueAsString(data),
//	                        ContentType.create("application/x-www-form-urlencoded", "UTF-8")));
	                List params=new ArrayList();
	                //添加参数
	                params.add(new BasicNameValuePair("KEYDATA","qdkjchina"+ USERNAME +",qd," + PASSWORD));
	                httpPost.setEntity(new UrlEncodedFormEntity(params,"UTF-8"));
	
	                client = HttpClients.createDefault();
	                response = client.execute(httpPost);
	                HttpEntity entity = response.getEntity();
	                String result = EntityUtils.toString(entity);
	                System.out.println(result);
	                HashMap resultMap = JSON.parseObject(result, HashMap.class);//返回结果转换为map
	                
	                if(resultMap.get("result").equals("success")) {
	                	pd.put("USERNAME", "admin");
	                	PageData adminUser = this.usersService.findByUsername(pd);
	                	 Subject subject = SecurityUtils.getSubject();
	                     UsernamePasswordToken token = new UsernamePasswordToken(adminUser.getString("USERNAME"), adminUser.getString("PASSWORD"));
	                     token.setRememberMe(false);
         				subject.login(token);								//这一步在调用login(token)方法时,它会走到MyRealm.doGetAuthenticationInfo()方法中
	               
		                Role role = this.roleService.getRoleById("1");//获取系统管理员角色
		                Session session = Jurisdiction.getSession();
		                
		                User user = new User();
		                user.setRole(role);
			            user.setUSER_ID(resultMap.get("USER_ID").toString());
						user.setUSERNAME(resultMap.get("USERNAME").toString());
						user.setPASSWORD(adminUser.getString("PASSWORD"));
						user.setNAME(resultMap.get("NAME").toString());
						user.setROLE_ID("1");
						user.setLAST_LOGIN(adminUser.getString("LAST_LOGIN"));
						user.setIP(adminUser.getString("IP"));
						user.setSTATUS(adminUser.getString("STATUS"));
						user.setSKIN(adminUser.getString("SKIN"));
						session.setAttribute(Const.SESSION_USER, user);		//把当前用户放入session
						session.setAttribute(Const.SESSION_USERROL, user);		//把当前用户放入session
	                }else {
	                	errInfo = resultMap.get("result").toString();
	                	map.put("result", errInfo);
	                    return map;
	                }
	            } finally {
	                if (response != null) {
	                    response.close();
	                }
	                if (client != null) {
	                    client.close();
	                }
	            }
    		}else{
    			errInfo = "error";//缺少参数
    		}
    		map.put("result", errInfo);
    		return map;
        } catch (Exception e) {
            e.printStackTrace();
            String errInfo = "error";//缺少参数
            map.put("result", errInfo);
            return map;
        }
    }
	
	
	/**请求登录验证用户接口
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/check", produces="application/json;charset=UTF-8")
	@ResponseBody
	public Object check()throws Exception{
		Map<String,String> map = new HashMap<String,String>();
//		if(!BasicInfoUtil.checkValid()) {
//			String errInfo = "invalid";
//			map.put("result", errInfo);
//			return map;
//		}
		PageData pd = new PageData();
		pd = this.getPageData();
		String errInfo = "success";
		String KEYDATA[] = pd.getString("KEYDATA").replaceAll("qdkjchina", "").split(",qd,");
		if(null != KEYDATA && KEYDATA.length == 2){
			String USERNAME = KEYDATA[0];							//登录过来的用户名
			String PASSWORD  = KEYDATA[1];							//登录过来的密码
			UsernamePasswordToken token = new UsernamePasswordToken(USERNAME, new SimpleHash("SHA-1", USERNAME, PASSWORD).toString());
			Subject subject = SecurityUtils.getSubject();
			try {
				subject.login(token);								//这一步在调用login(token)方法时,它会走到MyRealm.doGetAuthenticationInfo()方法中
	        }catch(UnknownAccountException uae){
	            errInfo = "usererror";
	        }catch(IncorrectCredentialsException ice){
	        	errInfo = "usererror";
	        }catch(LockedAccountException lae){
	        	errInfo = "usererror";
	        }catch(ExcessiveAttemptsException eae){
	        	errInfo = "usererror4";
	        }catch (DisabledAccountException sae){
	        	errInfo = "usererror";
	        }catch(AuthenticationException ae){
	        	errInfo = "usererror";
	        }
	        if(subject.isAuthenticated()){							//验证是否登录成功
	        	removeSession(USERNAME);
	        	Session session = Jurisdiction.getSession();
	        	pd.put("USERNAME", USERNAME);
	            pd = usersService.findByUsername(pd);
	        	map.put("USERNAME", USERNAME);
	        	map.put("USER_ID",  pd.getString("USER_ID"));
	        	map.put("NAME", pd.getString("NAME"));
	        	map.put("DEPARTMENT_ID", pd.getString("DEPARTMENT_ID"));
//	        	map.put("PRECINCT_ID", pd.get("PRECINCT_ID")==null?"":pd.getString("PRECINCT_ID"));0e3880200e0a40e38c8140251adc1b8d
	        	map.put("PRECINCT_ID", pd.getString("PRECINCT_ID"));
	        	map.put("BASICINFO_ID", pd.getString("BASICINFO_ID"));
//	        	PageData dpd = new PageData();
//	        	dpd.put("DEPARTMENT_ID", pd.getString("DEPARTMENT_ID"));
//	        	dpd=departmentService.findById(dpd);
//	        	map.put("DEPARTMENT_NAME", dpd.getString("NAME"));
//	        	map.put("PARENT_NAME", dpd.getString("PARENT_NAME"));
	        	PageData rpd=roleService.findById(pd);
	        	map.put("ROLEID", rpd.getString("ROLE_ID"));
	        	map.put("ROLE_NAME", rpd.getString("ROLE_NAME"));
	        	map.put("USERBZ", pd.getString("BZ"));
	            User user = new User();
	            user.setUSER_ID(pd.getString("USER_ID"));
				user.setUSERNAME(pd.getString("USERNAME"));
				user.setPASSWORD(pd.getString("PASSWORD"));
				user.setNAME(pd.getString("NAME"));
				user.setROLE_ID(pd.getString("ROLE_ID"));
				user.setLAST_LOGIN(pd.getString("LAST_LOGIN"));
				user.setIP(pd.getString("IP"));
				user.setSTATUS(pd.getString("STATUS"));
				session.setAttribute(Const.SESSION_USER, user);		//把当前用户放入
				session.setAttribute(Const.USER_ID, pd.getString("USER_ID"));		//把当前用户企业放入session
				session.setAttribute(Const.DEPARTMENT_ID, pd.getString("DEPARTMENT_ID"));		//把当前用户放入session
				FHLOG.save(USERNAME, "成功登录系统");				//记录日志
	        }else{
	            token.clear();
	            errInfo = "usererror";
	        }
	        if(!"success".equals(errInfo))FHLOG.save(USERNAME, "尝试登录系统失败,用户名密码错误,无权限");
		}else{
			errInfo = "error";										//缺少参数
		}
		map.put("result", errInfo);
		return map;
	}
	
	
	/**系统用户注册接口
	 * @return
	 * @throws Exception 
	 */
	@RequestMapping(value="/register", produces="application/json;charset=UTF-8")
	@ResponseBody
	public Object register(@RequestParam("callback") String callback) throws Exception{
		Map<String,Object> map = new HashMap<String,Object>();
		PageData pd = new PageData();
		pd = this.getPageData();
		String result = "00";
		if(Tools.checkKey("USERNAME", pd.getString("FKEY"))){	//检验请求key值是否合法
			pd.put("USER_ID", this.get32UUID());	//ID 主键
			pd.put("ROLE_ID", "fhadminzhuche");		//角色ID fhadminzhuche 为注册用户
			pd.put("NUMBER", "");					//编号
			pd.put("PHONE", "");					//手机号
			pd.put("BZ", "注册用户");				//备注
			pd.put("LAST_LOGIN", "");				//最后登录时间
			pd.put("IP", "");						//IP
			pd.put("STATUS", "0");					//状态
			pd.put("SKIN", "pcoded-navbar navbar-image-3,navbar pcoded-header navbar-expand-lg navbar-light header-dark,");					//用户默认皮肤
			pd.put("EMAIL", "");
			pd.put("ROLE_IDS", "");
			pd.put("PASSWORD", new SimpleHash("SHA-1", pd.getString("USERNAME"), pd.getString("PASSWORD")).toString());	//密码加密
			if(null == usersService.findByUsername(pd)){	//判断用户名是否存在
				usersService.saveUser(pd); 					//执行保存
				FHLOG.save(pd.getString("USERNAME"), "新注册");
			}else{
				result = "01"; 	//用户名已存在
			}
		}else{
			result = "05";		//不合法的注册
		}
		map.put("result", result);
		JSONObject sresult = JSONObject.fromObject(map);;
		return callback+"("+sresult.toString()+")";
	}
	
	/**判断是否登录状态
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="/islogin")
	@ResponseBody
	public Object islogin()throws Exception{
		Map<String,String> map = new HashMap<String,String>();
		String errInfo = "success";
		Session session = Jurisdiction.getSession();
		if(null == session.getAttribute(Const.SESSION_USERNAME)) {
			errInfo = "errer";
		}
		map.put("result", errInfo);
		return map;
	}
	
	/**
	 * 清理session
	 */
	public void removeSession(String USERNAME){
		Session session = Jurisdiction.getSession();	//以下清除session缓存
		session.removeAttribute(Const.SESSION_USER);
		session.removeAttribute(USERNAME + Const.SESSION_ROLE_RIGHTS);
		session.removeAttribute(USERNAME + Const.SESSION_ALLMENU);
		session.removeAttribute(USERNAME + Const.SHIROSET);
		session.removeAttribute(Const.SESSION_USERNAME);
		session.removeAttribute(Const.SESSION_U_NAME);
		session.removeAttribute(Const.SESSION_USERROL);
		session.removeAttribute(Const.SESSION_RNUMBERS);
		session.removeAttribute(Const.SKIN);
	}
	
}
