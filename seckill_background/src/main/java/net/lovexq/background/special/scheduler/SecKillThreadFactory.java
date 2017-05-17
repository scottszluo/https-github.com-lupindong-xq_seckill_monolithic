package net.lovexq.background.special.scheduler;

import java.util.concurrent.ThreadFactory;

/**
 * @author LuPindong
 * @time 2017-05-17 14:02
 */
public class SecKillThreadFactory implements ThreadFactory {

    private String name;

    public SecKillThreadFactory(String name) {
        this.name = name;
    }

    @Override
    public Thread newThread(Runnable run) {
        return new Thread(run, name);
    }

}