package com.xyc.demo.sentinel;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.context.ContextUtil;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.csp.sentinel.util.TimeUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class SimpleDemo {
    public static void main(String[] args) throws Exception {
        testFlowRuleForCaller();
    }
    public static void initFlowRuleForCaller(){
        List<FlowRule> rules = new ArrayList<>();
        FlowRule rule = new FlowRule();
        //定义资源名
        rule.setResource("echo");
        //定义阈值类型
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        //定义阈值
        rule.setCount(2);
//        //定义限制调用者
//        rule.setLimitApp("caller");
        rule.setStrategy(RuleConstant.STRATEGY_CHAIN);
        rule.setRefResource("c3");rule.setLimitApp("other1");
        rules.add(rule);

        FlowRule rule1 = new FlowRule();
        rule1.setResource("echo");
        rule1.setGrade(RuleConstant.FLOW_GRADE_QPS);
        rule1.setStrategy(RuleConstant.STRATEGY_CHAIN);
        rule1.setRefResource("c3");
        rule1.setLimitApp("other");
        rule1.setCount(3);
        rules.add(rule1);
        FlowRuleManager.loadRules(rules);
    }


    public static void testFlowRuleForCaller(){
        initFlowRuleForCaller();
        for (int i = 0; i < 5; i++) {
            ContextUtil.enter("c3","default");
            Entry entry = null;
            try {
                entry = SphU.entry("echo");
                System.out.println("访问成功");
            } catch (BlockException e) {
                System.out.println("网络异常，请刷新！");
            }finally {
                if (entry != null){
                    entry.exit();
                }
            }
        }
    }
    private static void initDegradeRule() {
        List<DegradeRule> rules = new ArrayList<>();
        DegradeRule rule = new DegradeRule();
        rule.setResource("KEY");
        // set threshold RT, 10 ms
        rule.setCount(10);
        rule.setGrade(RuleConstant.DEGRADE_GRADE_RT);
        rule.setTimeWindow(10);
        rules.add(rule);
        DegradeRuleManager.loadRules(rules);
    }


    private static AtomicInteger pass = new AtomicInteger();
    private static AtomicInteger block = new AtomicInteger();
    private static AtomicInteger total = new AtomicInteger();

    private static volatile boolean stop = false;
    private static final int threadCount = 100;
    private static int seconds = 60 + 40;

    public static void test2() throws Exception {

        tick();
        initDegradeRule();

        for (int i = 0; i < threadCount; i++) {
            Thread entryThread = new Thread(new Runnable() {

                @Override
                public void run() {
                    while (true) {
                        Entry entry = null;
                        try {
                            TimeUnit.MILLISECONDS.sleep(5);
                            entry = SphU.entry("KEY");
                            // token acquired
                            pass.incrementAndGet();
                            // sleep 600 ms, as rt
                            TimeUnit.MILLISECONDS.sleep(600);
                        } catch (Exception e) {
                            block.incrementAndGet();
                        } finally {
                            total.incrementAndGet();
                            if (entry != null) {
                                entry.exit();
                            }
                        }
                    }
                }

            });
            entryThread.setName("working-thread");
            entryThread.start();
        }
    }
    private static void tick() {
        Thread timer = new Thread(new TimerTask());
        timer.setName("sentinel-timer-task");
        timer.start();
    }
    static class TimerTask implements Runnable {

        @Override
        public void run() {
            long start = System.currentTimeMillis();
            System.out.println("begin to statistic!!!");
            long oldTotal = 0;
            long oldPass = 0;
            long oldBlock = 0;

            while (!stop) {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                }

                long globalTotal = total.get();
                long oneSecondTotal = globalTotal - oldTotal;
                oldTotal = globalTotal;

                long globalPass = pass.get();
                long oneSecondPass = globalPass - oldPass;
                oldPass = globalPass;

                long globalBlock = block.get();
                long oneSecondBlock = globalBlock - oldBlock;
                oldBlock = globalBlock;

                System.out.println(TimeUtil.currentTimeMillis() + ", total:" + oneSecondTotal
                        + ", pass:" + oneSecondPass + ", block:" + oneSecondBlock);

                if (seconds-- <= 0) {
                    stop = true;
                }
            }

            long cost = System.currentTimeMillis() - start;
            System.out.println("time cost: " + cost + " ms");
            System.out.println("total:" + total.get() + ", pass:" + pass.get()
                    + ", block:" + block.get());
            System.exit(0);
        }
    }
}
