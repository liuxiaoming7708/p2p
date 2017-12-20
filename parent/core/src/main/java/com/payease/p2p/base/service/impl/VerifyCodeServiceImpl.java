package com.payease.p2p.base.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.payease.p2p.base.service.IVerifyCodeService;
import com.payease.p2p.base.util.*;
import com.payease.p2p.base.vo.VerifyCodeVO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.UUID;

/**
 * Created by liuxiaoming on 2017/6/20.
 */
@Service
public class VerifyCodeServiceImpl implements IVerifyCodeService {

    //@Value标签用来注入简单值
    @Value(value = "${sms.username}")
    private String username;
    @Value(value="${sms.password}")
    private String password;

    @Value(value = "${sms.apikey}")
    private String apiKey;

    @Value(value ="${sms.url}")
    private String url;


    @Override
    public JSONResult sendVerifyCode(String phoneNumber) {
        JSONResult result = new JSONResult();
        //得到session中的verifyCodeVO
        VerifyCodeVO vo = UserContext.getVerifyCode();
        if(vo == null//没有发过
                ||(vo != null && DateUtil.getSecondsBetween(vo.getSendTime(),new Date()) >= BidConst.SEND_VERIFYCODE_INTERVAL)//如果发过。要判断当前时间和发送时间的间隔时间
                ){

                    //生成一个验证码
                    String code = UUID.randomUUID().toString().substring(0,4);
                    //发送短信，短信发送成功了
                        try {
                            //创建一个URL对象
                            URL targetURL = new URL(this.url);
                            //从URL中获得一个连接对象
                            HttpURLConnection connection = (HttpURLConnection)targetURL.openConnection();
                            //设置输入参数
                            connection.setDoOutput(true);
                            connection.setRequestMethod("POST");
                            StringBuilder params = new StringBuilder(100)
                                    .append("username=").append(username)
                                    .append("&password=").append(password)
                                    .append("&apikey=").append(apiKey)
                                    .append("&mobile=").append(phoneNumber)
                                    .append("&content=")
                                        .append("您的验证码是：").append(code)
                                        .append("，有效时间为：").append(BidConst.VERIFYCODE_VAILDATE_SECOND).append("秒！");
                            System.out.println(params.toString().getBytes()+"-----参数为："+params.toString());
                            //写入参数

                            connection.getOutputStream().write(params.toString().getBytes());
                            //读入响应
                            String response = StreamUtils.copyToString(connection.getInputStream(), Charset.forName("UTF-8"));
                            JSONObject jsonObject=JSONObject.parseObject(response);
                             result = (JSONResult)JSONObject.toJavaObject(jsonObject, JSONResult.class);
                            System.out.println("响应值："+result.getMsg());
                            //发送成功
                            if(result.getMsg().equals("success")){
                                vo = new VerifyCodeVO(code,phoneNumber,new Date());
                                //把vo放大session当中
                                UserContext.putVerifyCode(vo);
                            }else{
                                throw new RuntimeException(result.getMsg());
                            }
                        }catch (Exception e){
                            throw new RuntimeException("发送短信失败！"+e.getMessage());
                        }
//                    System.out.println("发送一条短信给："+phoneNumber+"内容:验证码是："+code+
//                            "，有效时间："+BidConst.VERIFYCODE_VAILDATE_SECOND+"秒。");
//                    System.out.println("短信用户名："+username+"，短信密码："+password);
                 }else{
                    throw new RuntimeException("发送过于频繁！");
                 }
        return result;

    }

    @Override
    public boolean validate(String phoneNumber, String verifyCode) {
        VerifyCodeVO vo = UserContext.getVerifyCode();
        return vo !=null//发了验证码
                && vo.getPhoneNumber().equals(phoneNumber) //两次电话相同
                && vo.getCode().equals(verifyCode) //两次验证码相同
                && DateUtil.getSecondsBetween(vo.getSendTime(),new Date()) <=BidConst.VERIFYCODE_VAILDATE_SECOND;//短信在有效期之内
    }
}
