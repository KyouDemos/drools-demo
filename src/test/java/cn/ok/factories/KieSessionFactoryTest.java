package cn.ok.factories;

import cn.ok.domains.Applicant;
import cn.ok.domains.Message;
import lombok.extern.slf4j.Slf4j;
import org.drools.core.command.runtime.rule.InsertObjectCommand;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.StatelessKieSession;
import org.kie.api.runtime.rule.FactHandle;
import org.kie.internal.command.CommandFactory;

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

        // and then dispose the session
        if (kieSession != null) {
            log.debug("Dispose the kieSession.");
            kieSession.dispose();
        }

        log.debug("KieSessionFactoryTest end.");
    }

    @Test
    public void getKieSession() {
        kieSession = KieSessionFactory.getKieSession(KieSessionName);

        // 第一个数据对象
        // Once the session is created, the application can interact with it
        // In this case it is setting a global as defined in the
        // org/drools/examples/helloworld/HelloWorldTest.drl file
        kieSession.setGlobal("list", new ArrayList<>());

        Message message = new Message();
        message.setMessage("Hello World");
        message.setStatus(Message.HELLO);

        // The application can insert facts into the session
        FactHandle fh = kieSession.insert(message);
        // and fire the rules
        kieSession.fireAllRules();

        List list = (ArrayList) kieSession.getGlobal("list");
        log.info("list1: {}", list.toString());
        Assert.assertEquals("[Hello World, Goodbye World (M)]", list.toString());

        // 规则运行时，对 Message.message 进行了修改。
        log.info("Message.message has changed to \"{}\"", message.getMessage());

        // 第二个数据对象
        // 初始化全局变量
        kieSession.setGlobal("list", new ArrayList<>());
        message.setStatus(Message.GOODBYE);

        // 再次向规则中插入 message 对象，需要执行 update() 函数，第一个参数是要替换的上一次动作的返回值。
        kieSession.update(fh, message);
        // and fire the rules
        kieSession.fireAllRules();

        list = (ArrayList) kieSession.getGlobal("list");
        log.info("list2: {}", list.toString());
        Assert.assertEquals("[Goodbye World (M)]", list.toString());
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


    @Test
    public void getStatelessKieSession() {
        StatelessKieSession statelessKieSession = KieSessionFactory.getStatelessKieSession("StatelessKS");

        Applicant applicant = new Applicant("Mr John Smith", 16, true);
        log.debug(applicant.toString());

        InsertObjectCommand ioCommand = new InsertObjectCommand(applicant);
        statelessKieSession.execute(ioCommand);
        log.debug(applicant.toString());

        applicant.setAge(20);
        statelessKieSession.execute(CommandFactory.newInsert(applicant));

        log.debug(applicant.toString());
        Assert.assertFalse(applicant.isValid());
    }
}
