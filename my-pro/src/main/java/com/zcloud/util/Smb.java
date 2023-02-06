package com.zcloud.util;

import com.jcraft.jsch.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;

import org.omg.CORBA.SystemException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

public class Smb {
	 private static String host="39.103.206.194";

	    private static Integer port=22;

	    private static String user="root";

	    private static String password="Lxiaobao890206";



	    private static String basePath="/mnt/qask/file/";

	    public static void sshSftp(MultipartFile file, String fileName,String path) throws Exception {
	        Session session = null;
	        Channel channel = null;
	        JSch jsch = new JSch();
	        if (port<= 0) {
	            //连接服务器，采用默认端口
	            session = jsch.getSession(user, host);
	        } else {
	            //采用指定的端口连接服务器
	            session = jsch.getSession(user, host, port);
	        }

	        //如果服务器连接不上，则抛出异常
	        if (session == null) {
	            throw new Exception("session is null");
	        }
	        //设置登陆主机的密码
	        session.setPassword(password);
	        //设置第一次登陆的时候提示，可选值：(ask | yes | no)
	        session.setConfig("StrictHostKeyChecking", "no");
	        //设置登陆超时时间
	        session.connect(30000);
	        OutputStream outstream = null;

	        try {
	            //创建sftp通信通道
	            channel = (Channel) session.openChannel("sftp");
	            channel.connect(1000);
	            ChannelSftp sftp = (ChannelSftp) channel;
	            //进入服务器指定的文件夹
//	            File dir = new File(basePath+"/"+path);
//	            dir.setWritable(true, false);
//	            if(!dir.exists()){
//	                dir.mkdirs();
//	            }
	            createDir(basePath+path,sftp);
	            sftp.cd(basePath+path);
	            //列出服务器指定的文件列表
//	            Vector v = sftp.ls("*");
//	            for(int i=0;i<v.size();i++){
//	                System.out.println(v.get(i));
//	            }
	            //以下代码实现从本地上传一个文件到服务器，如果要实现下载，对换以下流就可以了

	            sftp.put(file.getInputStream(),fileName);

	        } catch (Exception e) {
	            e.printStackTrace();
	        } finally {
	            //关流操作
	            if (outstream != null) {
	                outstream.flush();
	                outstream.close();
	            }
	            if (session != null) {
	                session.disconnect();
	            }
	            if (channel != null) {
	                channel.disconnect();
	            }
	        }
	    }
	    public static void createDir(String createpath, ChannelSftp sftp) {
	    	  try {
	    	   if (isDirExist(createpath,sftp)) {
	    	    sftp.cd(createpath);
	    	   }
	    	   String pathArry[] = createpath.split("/");
	    	   StringBuffer filePath = new StringBuffer("/");
	    	   for (String path : pathArry) {
	    	    if (path.equals("")) {
	    	     continue;
	    	    }
	    	    filePath.append(path + "/");
	    	    if (isDirExist(filePath.toString(),sftp)) {
	    	     sftp.cd(filePath.toString());
	    	    } else {
	    	     // 建立目录
	    	     sftp.mkdir(filePath.toString());
	    	     // 进入并设置为当前目录
	    	     sftp.cd(filePath.toString());
	    	    }
	    	   }
	    	   sftp.cd(createpath);
	    	  } catch (SftpException e) {
	    	  // throw new SystemException("创建路径错误：" + createpath);
	    	  }
	    	 }
	    public static boolean isDirExist(String directory , ChannelSftp sftp) {
	    	  boolean isDirExistFlag = false;
	    	  try {
	    	   SftpATTRS sftpATTRS = sftp.lstat(directory);
	    	   isDirExistFlag = true;
	    	   return sftpATTRS.isDir();
	    	  } catch (Exception e) {
	    	   if (e.getMessage().toLowerCase().equals("no such file")) {
	    	    isDirExistFlag = false;
	    	   }
	    	  }
	    	  return isDirExistFlag;
	    	 }
	}
	

