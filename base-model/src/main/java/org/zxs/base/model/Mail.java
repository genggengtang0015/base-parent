package org.zxs.base.model;

public class Mail {
	
	private String protocol; //邮件协议，例如"stmp"， 非空
	
	private String mailHost; //邮件服务器，例如"smtp.qq.com"，非空
	
	private boolean isSSL;//是否需要SSL验证，qq邮箱需要SSL验证，非空
	
	private String fromMail; //发件人，例如xxxx@qq.com  非空
	
	private String user; //发件邮箱用户名   非空
	
	private String password;  //发件邮箱密码或者授权码  非空
	
	private String toMail[];  //收件人，xxxx@qq.com  非空
	
	private String mailContent; //邮件正文  非空
		
	private String mailTitle; //邮件主题 
	
	private String filePath[];//附件路径，可以有多个路径
	
	private String port; //发件人邮箱服务器端口号,非空

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getMailHost() {
		return mailHost;
	}

	public void setMailHost(String mailHost) {
		this.mailHost = mailHost;
	}

	public boolean isSSL() {
		return isSSL;
	}

	public void setSSL(boolean isSSL) {
		this.isSSL = isSSL;
	}

	public String getFromMail() {
		return fromMail;
	}

	public void setFromMail(String fromMail) {
		this.fromMail = fromMail;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String[] getToMail() {
		return toMail;
	}

	public void setToMail(String[] toMail) {
		this.toMail = toMail;
	}

	public String getMailContent() {
		return mailContent;
	}

	public void setMailContent(String mailContent) {
		this.mailContent = mailContent;
	}

	public String getMailTitle() {
		return mailTitle;
	}

	public void setMailTitle(String mailTitle) {
		this.mailTitle = mailTitle;
	}

	public String[] getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath[]) {
		this.filePath = filePath;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

}
