package cn.ok.examples;

import cn.ok.domains.Message;
import cn.ok.factories.KieSessionFactory;
import lombok.extern.slf4j.Slf4j;
import org.kie.api.runtime.KieSession;

import java.util.ArrayList;

/**
 * @author kyou on 2019-05-17 09:38
 */
@Slf4j
public class HelloWorld {
    private static final String KieSessionName = "HelloWorldKS";


    public static void main(String[] args) {
        KieSession kieSession = KieSessionFactory.getKieSession(KieSessionName);

        // Once the session is created, the application can interact with it
        // In this case it is setting a global as defined in the
        // org/drools/examples/helloworld/HelloWorld.drl file
        kieSession.setGlobal("list", new ArrayList<Object>());

        // The application can insert facts into the session
        Message message = new Message();
        message.setMessage("Hello World");
        message.setStatus(Message.HELLO);
        kieSession.insert(message);

        // and fire the rules
        kieSession.fireAllRules();

        // and then dispose the session
        kieSession.dispose();
    }
}
