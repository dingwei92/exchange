package test.StreamTest;

import com.topsci.BootStart;
import com.topsci.service.SendService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.binding.BinderAwareChannelResolver;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;

@Component
@RunWith(SpringRunner.class)
@SpringBootTest(classes = BootStart.class)
public class StreamTest {
    @Autowired
    private SendService sendService;
    @Autowired
    private BinderAwareChannelResolver resolver;
    private static final Logger logger = LoggerFactory.getLogger(StreamTest.class);

    public static void main(String[] args) {

    }

    /*原生mq测试*/
    @Test
    public void testStream1() {
     /*   //创建连接工厂
        ConnectionFactory factory = new ConnectionFactory();
        //设置RabbitMQ相关信息
        factory.setHost("localhost");
        factory.setUsername("guest");
        factory.setPassword("guest");
        factory.setPort(5672);
        //创建一个新的连接
        Connection connection = null;
        Channel channel = null;
        try {
            connection = factory.newConnection();
            //创建一个通道
            channel = connection.createChannel();
            //  声明一个队列
            channel.queueDeclare("dw1", false, false, false, null);
            String message = "Hello RabbitMQ";
            //发送消息到队列中
            channel.basicPublish("", "dw1", null, message.getBytes("UTF-8"));
            System.out.println("Producer Send +'" + message + "'");
            channel.close();
            connection.close();
            //关闭通道和连接
        } catch (Exception e) {
            e.printStackTrace();
        }*/

    }

    /*测试生产者默认通道*/
    @Test
    public void testStream2() {
        String msg = "hello stream ...";
        sendService.send(msg);
        logger.info("success");
    }

    /*测试生产者动态Destination*/
    @Test
    public void testStream3() {
        resolver.resolveDestination("IOT_TOPIC").send(MessageBuilder.createMessage("你好",
                new MessageHeaders(Collections.singletonMap(MessageHeaders.CONTENT_TYPE, null))));
        logger.info("success");
    }

    /*测试exchange*/
    @Test
    public void testStream4() {
        sendService.dynamicSend("RECEIVE_TOPIC", "你好RECEIVE_TOPIC", null);
        sendService.dynamicSend("RECEIVE_TOPIC_STATUS", "你好RECEIVE_TOPIC_STATUS", null);
    }


}