package cn.ok.factories;

import cn.ok.domains.Message;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.kie.api.runtime.KieSession;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * @author kyou on 2019-05-22 07:59
 */
@Slf4j
public class KieSessionFactoryTest {
    // kieSession name 需要与 kmodule.xml 文件中指定的名称保持一致。
    private static final String KieSessionName = "HelloWorldKS";
    private static final String FILE_PATH = "src/main/resources/cn/ok/examples/helloworld/helloworld.txt";

    private KieSession kieSession;

    @Before
    public void setUp() {
        log.debug("KieSessionFactoryTest start.");
    }

    @After
    public void tearDown() {

        if (kieSession != null) {
            // and then dispose the session
            kieSession.dispose();
        }

        log.debug("KieSessionFactoryTest end.");
    }

    @Test
    public void getKieSession() {
        kieSession = KieSessionFactory.getKieSession(KieSessionName);

        // Once the session is created, the application can interact with it
        // In this case it is setting a global as defined in the
        // org/drools/examples/helloworld/HelloWorld.drl file
        kieSession.setGlobal("list", new ArrayList<>());

        Message message = new Message();
        message.setMessage("Hello World");
        message.setStatus(Message.HELLO);

        // The application can insert facts into the session
        kieSession.insert(message);

        // and fire the rules
        kieSession.fireAllRules();

        List list = (ArrayList) kieSession.getGlobal("list");
        log.info(list.toString());

        Assert.assertEquals("[Hello World, Good Bye]", list.toString());
    }

    @Test
    public void getKieSessionFromStream() throws IOException {
        // 假设字符流从文件中读取。
        String fileStrings = new String(Files.readAllBytes(Paths.get(FILE_PATH)));
        log.info("fileStrings: \n{}", fileStrings);

        KieSession kieSession = KieSessionFactory.getKieSessionFromStream(fileStrings);
        kieSession.setGlobal("list", new ArrayList<>());

        Message message = new Message();
        message.setMessage("Hello World");
        message.setStatus(Message.HELLO);

        // The application can insert facts into the session
        kieSession.insert(message);

        // and fire the rules
        kieSession.fireAllRules();

        List list = (ArrayList) kieSession.getGlobal("list");
        log.info(list.toString());

        Assert.assertEquals("[Hello World, Good Bye]", list.toString());
    }
}
