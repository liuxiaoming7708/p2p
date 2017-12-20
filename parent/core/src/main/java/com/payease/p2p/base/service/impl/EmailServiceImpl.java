package com.payease.p2p.base.service.impl;

import com.payease.p2p.base.domain.MailVerify;
import com.payease.p2p.base.domain.Userinfo;
import com.payease.p2p.base.mapper.MailVerifyMapper;
import com.payease.p2p.base.service.IEmailService;
import com.payease.p2p.base.service.IUserInfoService;
import com.payease.p2p.base.util.BidConst;
import com.payease.p2p.base.util.JSONResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;
import java.util.UUID;

/**
 * Created by liuxiaoming on 2017/6/22.
 */
@Service
public class EmailServiceImpl implements IEmailService{

    @Autowired
    private MailVerifyMapper mailVerifyMapper;
    @Autowired
    private IUserInfoService userInfoService;

    @Value("${mail.applicationURL}")
    private String applicationURL;
    @Value("${mail.host}")
    private String host;
    @Value("${mail.port}")
    private String port;
    @Value("${mail.username}")
    private String username;
    @Value("${mail.password}")
    private String password;


    @Override
    public JSONResult sendVerifyEmail(String email) {
        //判断用户是否已经绑定邮箱
        Userinfo current = this.userInfoService.getCurrent();
        JSONResult jsonResult = new JSONResult();
        if(!current.getIsBindEmail()){
            //如果没有绑定
            //生成一个uuid
            String uuid = UUID.randomUUID().toString();
            //构造邮件内容并发送
            StringBuilder content = new StringBuilder(100)
                    .append("这是验证邮件，点击<a href='").append(this.applicationURL)
                    .append("/bindEmail.do?uuid=").append(uuid).append("'>这里</a>，有效期是")
                    .append(BidConst.VERIFYEMAIL_VAILDATE_DAY).append("天");
            try {
                //发送邮件
                System.out.println("发送邮件："+content.toString());
                sendEmail(email,content.toString() );


                //创建一个MailVerify对象
                MailVerify mailVerify = new MailVerify();
                mailVerify.setLogininfoId(current.getId());
                mailVerify.setEmail(email);
                mailVerify.setSendTime(new Date());
                mailVerify.setUuid(uuid);
                this.mailVerifyMapper.insert(mailVerify);
                jsonResult.setSuccess(true);
                jsonResult.setMsg("success");
            }catch (Exception e){
                e.printStackTrace();
                jsonResult.setSuccess(false);
                jsonResult.setMsg("发送验证邮件失败！！失败原因："+e.getMessage());
                throw new RuntimeException("发送验证邮件失败！！失败原因："+e.getMessage());
            }

        }
        return jsonResult;
    }


    private void sendEmail(String email,String content) throws Exception{
        // 创建Properties 类用于记录邮箱的一些属性
        final Properties props = new Properties();
        // 表示SMTP发送邮件，必须进行身份验证
        props.put("mail.smtp.auth", "true");
        //此处填写SMTP服务器
        props.put("mail.smtp.host", host);
        //端口号，QQ邮箱给出了两个端口，但是另一个我一直使用不了，所以就给出这一个587
        props.put("mail.smtp.port", port);
        // 此处填写你的账号
        props.put("mail.user", username);
        // 此处的密码就是前面说的16位STMP口令
        props.put("mail.password", password);

        // 构建授权信息，用于进行SMTP进行身份验证
        Authenticator authenticator = new Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {
                // 用户名、密码
                String userName = props.getProperty("mail.user");
                String password = props.getProperty("mail.password");
                return new PasswordAuthentication(userName, password);
            }
        };
        // 使用环境属性和授权信息，创建邮件会话
        Session mailSession = Session.getInstance(props, authenticator);
        // 创建邮件消息
        MimeMessage message = new MimeMessage(mailSession);
        // 设置发件人
        InternetAddress form = new InternetAddress(
                props.getProperty("mail.user"));
        message.setFrom(form);

        // 设置收件人的邮箱
        InternetAddress to = new InternetAddress(email);
        message.setRecipient(Message.RecipientType.TO, to);

        // 设置邮件标题
        message.setSubject("测试邮件");

        // 设置邮件的内容体
        message.setContent("这是一封测试邮件 --刘晓明 "+content, "text/html;charset=UTF-8");

        // 最后当然就是发送邮件啦
        Transport.send(message);
//        JavaMailSenderImpl sender = new JavaMailSenderImpl();
//        //设置服务器地址
//        sender.setHost(this.host);
//        //创建一个邮件
//         MimeMessage message = sender.createMimeMessage();
//         MimeMessageHelper messageHelper = new MimeMessageHelper(message,"UTF-8");
//        //设置邮件相关内容
//        messageHelper.setTo(email);
//        // 这里有两个参数,第一个form是用来规定发件人的邮箱地址的,第二个是发件人的名字,可以自定义
//        messageHelper.setFrom("Admin@xmg.com", "系统管理员");
//        messageHelper.setSubject("邮件标题");
//        //设置邮件内容
//        messageHelper.setText(content.toString(),true);
//        //设置发送准备(发件人的账号密码)
//        sender.setUsername(username);
//        sender.setPassword(password);
//
//        Properties prop =  new Properties();
//        prop.put("mail.smtp.auth","true");
//        prop.put("mail.smtp.timeout","25000");
//        sender.setJavaMailProperties(prop);
//        sender.send(message);
    }
}
