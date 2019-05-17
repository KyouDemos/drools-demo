package cn.ok.factories;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

/**
 * @author kyou on 2019-05-17 09:31
 */
public class KieSessionFactory {
    public static KieSession getKieSession(String ksName) {
        // From the kie services, a container is created from the classpath
        KieServices ks = KieServices.Factory.get();
        KieContainer kc = ks.getKieClasspathContainer();

        // From the container, a session is created based on
        // its definition and configuration in the META-INF/kmodule.xml file
        return kc.newKieSession(ksName);
    }
}
