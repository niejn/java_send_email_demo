package mail;

import util.MailSenderInfo;
import util.MyAuthenticator;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;

/**
 * Created by taylor on 6/3/15.
 */
public class Mail {

    //to multiple reciver
    public static void sendEmail(){

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


        MyAuthenticator authenticator=null;
        Properties pro=mailInfo.getProperties();
        Address[] addressTO=null;
        if(pro.get("toAddress")!=""){
            String[] addresses=null;
            addresses=pro.getProperty("toAddress").split(";");
            addressTO=new InternetAddress[addresses.length];
            for(int i=0;i<addresses.length;i++){
                try{
                    addressTO[i]=new InternetAddress(addresses[i]);
                }catch (AddressException e){
                    e.printStackTrace();
                }
            }
        }


        if(mailInfo.isValidate()){
            authenticator=new MyAuthenticator(mailInfo.getUserName(),mailInfo.getPassword());
        }

        Session sendMailSession=Session.getDefaultInstance(pro,authenticator);

        try{
            Message mailMessage=new MimeMessage(sendMailSession);

            Address from =new InternetAddress(mailInfo.getFromAddress());
            mailMessage.setFrom(from);

            mailMessage.setRecipients(Message.RecipientType.TO,addressTO);

            mailMessage.setSubject(mailInfo.getSubject());
            mailMessage.setSentDate(new Date());
            mailMessage.setText(mailInfo.getContent());
            Multipart multipart=new MimeMultipart();
            if(mailInfo.getAttachFileNames().length!=0){
                for(int i=0;i<mailInfo.getAttachFileNames().length;i++){
                    DataSource source=new FileDataSource(mailInfo.getAttachFileNames()[i]);
                    BodyPart bodyPart=new MimeBodyPart();
                    bodyPart.setDataHandler(new DataHandler(source));
                    String [] ss=mailInfo.getAttachFileNames()[i].split("/");
                    bodyPart.setFileName(ss[ss.length-1]);
                    multipart.addBodyPart(bodyPart);


                }

                BodyPart bodyPart=new MimeBodyPart();
                bodyPart.setContent(mailInfo.getContent(),"text/html");
                multipart.addBodyPart(bodyPart);
                mailMessage.setContent(multipart);

            }
            Transport.send(mailMessage);
        }catch (Exception e){
            e.printStackTrace();
        }





    }
}
