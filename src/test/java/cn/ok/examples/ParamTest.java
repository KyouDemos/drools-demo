package cn.ok.examples;

import cn.ok.domains.BlackList;
import cn.ok.domains.Message;
import cn.ok.factories.KieSessionFactory;
import com.google.common.base.Stopwatch;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.FactHandle;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 规则中带有参数表形式测试
 *
 * @author kyou on 2019-09-04 14:43:59
 */
@Slf4j
public class ParamTest {
    // kieSession name 需要与 kmodule.xml 文件中指定的名称保持一致。
    private static final String KieSessionName = "ParamTestKS";
    private KieSession kieSession;

    @Before
    public void setUp() {
        log.debug("ParamTest start.");
    }

    @After
    public void tearDown() {

        // and then dispose the session
        if (kieSession != null) {
            log.debug("Dispose the kieSession.");
            kieSession.dispose();
        }

        log.debug("ParamTest end.");
    }

    /**
     * 本方法测试一个规则用到一个参数表（100W），情况下平均用时。
     * 100W参数数据，注意调整JVM内存
     */
    @Test
    public void oneRuleOneParamTest() {
        kieSession = KieSessionFactory.getKieSession(KieSessionName);

        List<BlackList> lstParam = new ArrayList<>();
        for (int i = 0; i < 1000000; i++) {
            BlackList blackList = new BlackList();
            blackList.setId(i);
            blackList.setName("name_" + i);
            blackList.setNote("note_" + i);
            lstParam.add(blackList);
        }

        // 设置全局变量
        kieSession.setGlobal("lstBlackList", lstParam);

        int cnt = 100;
        long elapsedTotal = 0L;

        // 统计规则执行时间
        Stopwatch stopwatch = Stopwatch.createUnstarted();

        for (int i = 0; i < cnt; i++) {
            Message msg = new Message();
            msg.setStatus(i);
            msg.setMessage("msg_" + i);

            // 开始计时
            stopwatch.reset().start();

            // 插入业务数据
            FactHandle factHandle = kieSession.insert(msg);

            // 执行规则
            kieSession.fireAllRules();

            // 清理业务数据
            kieSession.delete(factHandle);

            // 输出计时结果
            long elapsed = stopwatch.elapsed(TimeUnit.MILLISECONDS);
            log.info("elapsed: {}ms", elapsed);

            elapsedTotal += elapsed;
        }

        stopwatch.stop();

        log.info("avg elapsed: {}ms", elapsedTotal / cnt);
    }
}
