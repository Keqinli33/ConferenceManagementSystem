package controllers;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Date;
import javax.mail.Transport;
import java.util.Properties;
/**
 * Created by Ling on 2017/3/28.
 */
public class SendEmail {
    public static String myEmailAccount = "linghl0915@163.com";
    public static String myEmailPassword = "Hayley-4869";
    public static String myEmailSMTPHost = "smtp.163.com";

    public static void SendEmail(String email, String tmp_pwd){
        try {
            Properties props = new Properties();                    // 参数配置
            props.setProperty("mail.transport.protocol", "smtp");   // 使用的协议（JavaMail规范要求）
            props.setProperty("mail.smtp.host", myEmailSMTPHost);   // 发件人的邮箱的 SMTP 服务器地址
            props.setProperty("mail.smtp.auth", "true");
            final String smtpPort = "465";
            props.setProperty("mail.smtp.port", smtpPort);
            props.setProperty("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.setProperty("mail.smtp.socketFactory.fallback", "false");
            props.setProperty("mail.smtp.socketFactory.port", smtpPort);
            Session session = Session.getDefaultInstance(props);
            session.setDebug(true);

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(myEmailAccount, "某宝网", "UTF-8"));
            message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(email, "XX用户", "UTF-8"));
            message.setSubject("打折钜惠", "UTF-8");
            message.setContent("XX用户你好, 今天全场5折, 快来抢购, 错过今天再等一年。。。", "text/html;charset=UTF-8");
            message.saveChanges();


            Transport transport = session.getTransport();
            transport.connect(myEmailAccount, myEmailPassword);
            transport.sendMessage(message, message.getAllRecipients());

            transport.close();

        } catch (Exception e){
            e.printStackTrace();
        }

    }
}
