package cn.ok.examples;

import cn.ok.domains.Message;
import cn.ok.factories.KieSessionFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.kie.api.runtime.KieSession;

import java.util.ArrayList;
import java.util.List;

/**
 * @author kyou on 2019-05-23 19:54
 */
@Slf4j
public class HelloWorldTest {
    // kieSession name 需要与 kmodule.xml 文件中指定的名称保持一致。
    private static final String KieSessionName = "HelloWorldKS";
    private KieSession kieSession;

    @Before
    public void setUp() {
        log.debug("HelloWorldTest start.");
    }

    @After
    public void tearDown() {

        // and then dispose the session
        if (kieSession != null) {
            log.debug("Dispose the kieSession.");
            kieSession.dispose();
        }

        log.debug("KieSessionFactoryTest end.");
    }


    /**
     * 本方法测试连续插入多个实例后，一次执行规则的运行效果。
     */
    @Test
    public void multiInsert() {
        log.debug("multiInsert(): ");
        kieSession = KieSessionFactory.getKieSession(KieSessionName);
        kieSession.setGlobal("list", new ArrayList<>());

        Message message1 = new Message();
        message1.setMessage("Goodbye World.");
        message1.setStatus(Message.GOODBYE);
        kieSession.insert(message1);

        Message message2 = new Message();
        message2.setMessage("Hello World.");
        message2.setStatus(Message.HELLO);
        kieSession.insert(message2);

        kieSession.fireAllRules();

        List list = (ArrayList) kieSession.getGlobal("list");
        log.info("list: {}", list.toString());
    }

    /**
     * 本方法测试反复插入一个实例后，一次执行规则的运行效果。
     */
    @Test
    public void multiInsert1() {
        log.debug("multiInsert1(): ");
        kieSession = KieSessionFactory.getKieSession(KieSessionName);
        kieSession.setGlobal("list", new ArrayList<>());

        Message message1 = new Message();
        message1.setMessage("Hello World.");
        message1.setStatus(Message.HELLO);
        kieSession.insert(message1);

        message1.setMessage("Goodbye World.");
        message1.setStatus(Message.GOODBYE);
        kieSession.insert(message1);

        kieSession.fireAllRules();

        List list = (ArrayList) kieSession.getGlobal("list");
        log.info("list: {}", list.toString());
    }

}
