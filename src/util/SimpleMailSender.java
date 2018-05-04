package util;

import java.util.Date;
import java.util.Properties;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
/**
 * Created by taylor on 9/24/14.
 */
public class SimpleMailSender {
    public boolean sendTextMail(MailSenderInfo mailInfo) {
        // 判断是否需要身份认证
        MyAuthenticator authenticator = null;
        Properties pro = mailInfo.getProperties();
        if (mailInfo.isValidate()) {
            // 如果需要身份认证，则创建一个密码验证器
            authenticator = new MyAuthenticator(mailInfo.getUserName(), mailInfo.getPassword());
        }
        // 根据邮件会话属性和密码验证器构造一个发送邮件的session
        Session sendMailSession = Session.getDefaultInstance(pro,authenticator);
        try {
            // 根据session创建一个邮件消息
            Message mailMessage = new MimeMessage(sendMailSession);
            // 创建邮件发送者地址
            Address from = new InternetAddress(mailInfo.getFromAddress());
            // 设置邮件消息的发送者
            mailMessage.setFrom(from);
            // 创建邮件的接收者地址，并设置到邮件消息中
            Address to = new InternetAddress(mailInfo.getToAddress());
            mailMessage.setRecipient(Message.RecipientType.TO,to);
            // 设置邮件消息的主题
            mailMessage.setSubject(mailInfo.getSubject());
            // 设置邮件消息发送的时间
            mailMessage.setSentDate(new Date());
            // 设置邮件消息的主要内容
            String mailContent = mailInfo.getContent();
            mailMessage.setText(mailContent);
//            mailMessage.setContent();
            //附件
            Multipart multipart=new MimeMultipart();

            if(mailInfo.isWithAttachment()){
                for(int i=0;i<mailInfo.getAttachFileNames().length;i++) {
                    DataSource source = new FileDataSource(mailInfo.getAttachFileNames()[i]);
                    String name = source.getName();
                    BodyPart bodyPart=new MimeBodyPart();
                    bodyPart.setDataHandler(new DataHandler(source));
                    String[]ss=mailInfo.getAttachFileNames()[i].split("/");
                    bodyPart.setFileName(ss[ss.length-1]);
                    multipart.addBodyPart(bodyPart);
                }
                BodyPart bodyPart=new MimeBodyPart();
                //msg.setContent(builder.mailContent, "text/html;charset=utf-8");
                bodyPart.setContent(mailInfo.getContent(),"text/html;charset=utf-8");
                multipart.addBodyPart(bodyPart);
//                mailMessage.setContent("<h1>This is actual message</h1>",
//                                            "text/html" );
                mailMessage.setContent(multipart);
            }

            // 发送邮件
            Transport.send(mailMessage);
            return true;
        } catch (MessagingException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * 以HTML格式发送邮件
     * @param mailInfo 待发送的邮件信息
     */
    public  static boolean sendHtmlMail(MailSenderInfo mailInfo){
        // 判断是否需要身份认证
        MyAuthenticator authenticator = null;
        Properties pro = mailInfo.getProperties();
        //如果需要身份认证，则创建一个密码验证器
        if (mailInfo.isValidate()) {
            authenticator = new MyAuthenticator(mailInfo.getUserName(), mailInfo.getPassword());
        }
        // 根据邮件会话属性和密码验证器构造一个发送邮件的session
        Session sendMailSession = Session.getDefaultInstance(pro,authenticator);
        try {
            // 根据session创建一个邮件消息
            Message mailMessage = new MimeMessage(sendMailSession);
            // 创建邮件发送者地址
            Address from = new InternetAddress(mailInfo.getFromAddress());
            // 设置邮件消息的发送者
            mailMessage.setFrom(from);
            // 创建邮件的接收者地址，并设置到邮件消息中
            Address to = new InternetAddress(mailInfo.getToAddress());
            // Message.RecipientType.TO属性表示接收者的类型为TO
            mailMessage.setRecipient(Message.RecipientType.TO,to);
            // 设置邮件消息的主题
            mailMessage.setSubject(mailInfo.getSubject());
            // 设置邮件消息发送的时间
            mailMessage.setSentDate(new Date());
            // MiniMultipart类是一个容器类，包含MimeBodyPart类型的对象
            Multipart mainPart = new MimeMultipart();
            // 创建一个包含HTML内容的MimeBodyPart
            BodyPart html = new MimeBodyPart();
            // 设置HTML内容
            html.setContent(mailInfo.getContent(), "text/html; charset=utf-8");
            mainPart.addBodyPart(html);
            // 将MiniMultipart对象设置为邮件内容
            mailMessage.setContent(mainPart);

//            //附件
//            BodyPart bodyPart=new MimeBodyPart();
//            Multipart multipart=new MimeMultipart();
//            if(mailInfo.getAttachFileNames().length!=0){
////                for(int i=0;i<mailInfo.getAttachFileNames().length;i++) {
//                DataSource source = new FileDataSource(mailInfo.getAttachFileNames()[0]);
//                String name = source.getName();
//                bodyPart.setDataHandler(new DataHandler(source));
//                bodyPart.setFileName(mailInfo.getAttachFileNames()[0]);
//                multipart.addBodyPart(bodyPart);
////                }
//            }
            String simpleHtml = "<html>\n" +
                    "    <head>\n" +
                    "        <title>通知邮件</title>\n" +
                    "        <meta charset=\"utf-8\"/>\n" +
                    "        <style type=\"text/css\">\n" +
                    "            body{\n" +
                    "                font-size: 10pt;\n" +
                    "            }\n" +
                    "            .header,.content,.footer{\n" +
                    "                width: 600px;\n" +
                    "                height: 180px;\n" +
                    "            }\n" +
                    "            .footer{\n" +
                    "                text-align: right;\n" +
                    "            }\n" +
                    "            #name{\n" +
                    "                color: grey;\n" +
                    "            }\n" +
                    "            #message{\n" +
                    "                color: grey;\n" +
                    "            }\n" +
                    "        </style>\n" +
                    "\n" +
                    "    </head>\n" +
                    "    <body>\n" +
                    "        <div class=\"header\">\n" +
                    "            <img src=\"http://www.zhku.edu.cn/images/logo.jpg\" alt=\"zhkulogo\"/>\n" +
                    "        </div>\n" +
                    "        <div class=\"content\">\n" +
                    "            你好，<span id=\"name\"></span>同学:<br/><br/>\n" +
                    "            <div id=\"message\">\n" +
                    "                <span>    </span><span id=\"message\"></span>\n" +
                    "            </div>\n" +
                    "            <div class=\"footer\">\n" +
                    "                校园通知(<span id=\"time\"></span>)\n" +
                    "            </div>\n" +
                    "        </div>\n" +
                    "\n" +
                    "    </body>\n" +
                    "</html>";
            String _content_type = "text/html";
            mailMessage.setContent(simpleHtml,
                    "text/html" );

            // 发送邮件
            Transport.send(mailMessage);
            return true;
        } catch (MessagingException ex) {
            ex.printStackTrace();
        }
        return false;
    }



}
