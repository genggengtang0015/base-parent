package org.zxs.utils;

import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.zxs.base.model.Mail;

public class SendMail {
		
	/**
	 * 发送邮件
	 * @param mail 参数实体类
	 * @throws AddressException
	 * @throws MessagingException
	 * @throws GeneralSecurityException
	 * @throws Exception
	 */
	public static void sendMail(Mail mail) throws AddressException, MessagingException, GeneralSecurityException,Exception{
		
		Properties props = new Properties();
		props.put("mail.transport.protocol", mail.getProtocol());// 发送邮件协议名称
		props.put("mail."+mail.getProtocol()+".host", mail.getMailHost());//设置邮件服务器主机名
		props.put("mail."+mail.getProtocol()+".auth", "true");//发送服务器需要身份验证
		props.setProperty("mail."+mail.getProtocol()+".port", mail.getPort());//发件人邮箱服务器端口号
		if(mail.isSSL()){
			//SSL安全认证
			props.setProperty("mail."+mail.getProtocol()+".socketFactory.class", "javax.net.ssl.SSLSocketFactory");
			props.setProperty("mail.+"+mail.getProtocol()+".socketFactory.fallback", "false");
			props.setProperty("mail."+mail.getProtocol()+".socketFactory.port", mail.getPort());
	        
		}
		Session session = Session.getDefaultInstance(props);//根据属性新建一个邮件会话  
		Message message = new MimeMessage(session);//由邮件会话新建一个消息对象  
		message.setFrom(new InternetAddress(mail.getFromMail()));//设置发件人的地址
		
		//设置收件人地址,并设置其接收类型为TO
		for(int i=0;i<mail.getToMail().length;i++){
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(mail.getToMail()[i]));
		}
		message.setSubject(mail.getMailTitle());//设置标题  
		Multipart mp = new MimeMultipart();//向multipart对象中添加邮件的各个部分内容，包括文本内容和附件
		BodyPart bp = new MimeBodyPart();
		bp.setContent(mail.getMailContent(),"text/html;charset=UTF-8");//邮件正文
		mp.addBodyPart(bp);
	    if (mail.getFilePath() != null && mail.getFilePath().length>0) {
	    	for(int i=0;i<mail.getFilePath().length;i++){
	    		BodyPart attachmentBodyPart = new MimeBodyPart();
		        // 根据附件路径获取文件
		        FileDataSource dataSource = new FileDataSource(mail.getFilePath()[i]);
		        attachmentBodyPart.setDataHandler(new DataHandler(dataSource));
		        //MimeUtility.encodeWord可以避免文件名乱码
		        attachmentBodyPart.setFileName(MimeUtility.encodeWord(dataSource.getFile().getName()));
		        mp.addBodyPart(attachmentBodyPart);
	    	}
	    }
		message.setContent(mp);//邮件内容
		message.setSentDate(new Date());//设置发信时间  
		message.saveChanges();//存储邮件信息  
		
		//发送邮件
		Transport transport = session.getTransport();
		try{
			transport.connect(mail.getMailHost(),mail.getUser(), mail.getPassword());
			transport.sendMessage(message, message.getAllRecipients());//发送邮件,其中第二个参数是所有已设好的收件人地址
		}finally{
			transport.close();
		}
	}
	
}
