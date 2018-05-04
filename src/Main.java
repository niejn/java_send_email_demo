import util.MailSenderInfo;
import util.SimpleMailSender;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;

public class Main {

    public static void main(String[] args) {

        Date date =new Date();

        Properties properties=new Properties();
        try {
            InputStream inputStream=new FileInputStream(new File("email.properties"));
            properties.load(inputStream);
        } catch (Exception e) {
            System.out.println("no properties file found (email.properties)");
            e.printStackTrace();
        }
        MailSenderInfo mailInfo = new MailSenderInfo();
        mailInfo.setMailServerHost(properties.getProperty("smtp"));
        mailInfo.setMailServerPort(properties.getProperty("port"));
        mailInfo.setValidate(true);
        mailInfo.setUserName(properties.getProperty("from"));
        mailInfo.setPassword(properties.getProperty("password"));//您的邮箱密码
        mailInfo.setFromAddress(properties.getProperty("from"));
        mailInfo.setToAddress(properties.getProperty("to"));
        mailInfo.setSubject(properties.getProperty("subject"));
        mailInfo.setContent(properties.getProperty("content"));
        if(properties.getProperty("attachment").length()!=0){
            String[] filename= properties.getProperty("attachment").split(";");
            mailInfo.setAttachFileNames(filename);
        }else{
            mailInfo.setWithAttachment(false);
        }
        //这个类主要来发送邮件
        SimpleMailSender sms = new SimpleMailSender();
        //boolean result=sms.sendTextMail(mailInfo);//发送文体格式
        boolean result = SimpleMailSender.sendHtmlMail(mailInfo);//发送文体格式
        System.out.print("task end : "+(result?"successful":"fail")+"  @  "+date);
    }
}
