package cn.ok.examples;

import cn.ok.domains.Message;
import cn.ok.factories.KieSessionFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.kie.api.runtime.KieSession;

/**
 * @author kyou on 2019-05-28 07:55
 */
@Slf4j
public class RuleExecutionOrder {
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

    @Test
    public void doTest() {
        kieSession = KieSessionFactory.getKieSession("RuleExecutionOrderKS");

        Message message1 = new Message();
        message1.setMessage("Goodbye World");
        message1.setStatus(Message.GOODBYE);
        kieSession.insert(message1);

        kieSession.fireAllRules();


        log.debug(message1.toString());
    }
}
