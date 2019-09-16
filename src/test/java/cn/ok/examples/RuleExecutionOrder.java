package cn.ok.examples;

import cn.ok.domains.Message;
import cn.ok.factories.KieSessionFactory;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.kie.api.runtime.KieSession;

/**
 * @author kyou on 2019-05-28 07:55
 */
@Slf4j
public class RuleExecutionOrder {
    private KieSession kieSession;

    public static long test() {

        long s = System.currentTimeMillis();
        KieSession kieSession = KieSessionFactory.getKieSession("RuleExecutionOrderKS");
        long e = System.currentTimeMillis();
        log.debug("KieSessionFactory.getKieSession: {}{}", e - s, "ms");

        Message message = new Message();
        message.setMessage("message_" + 3000);
        message.setStatus(3000);
        kieSession.insert(message);

        s = System.currentTimeMillis();
        kieSession.fireAllRules();
        e = System.currentTimeMillis();

        kieSession.dispose();
        log.debug("kieSession.fireAllRules: {}{}", e - s, "ms");
        return e - s;
    }

    public static void main(String[] args) {
        long s = 0;
        int cnt = 100;

        for (int i = 0; i < cnt; i++) {
            s += test();
            if (i == 0) {
                s = 0;
            }
        }

        log.debug("平均耗时: {}ms", s / (cnt - 1));
    }

    @Before
    public void setUp() {
        log.debug("HelloWorldTest start.");
    }

//    @Test
//    public void doTest() {

    @After
    public void tearDown() {

        // and then dispose the session
        if (kieSession != null) {
            log.debug("Dispose the kieSession.");
            kieSession.dispose();
        }

        log.debug("KieSessionFactoryTest end.");
    }
}
