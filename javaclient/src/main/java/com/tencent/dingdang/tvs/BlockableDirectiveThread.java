package com.tencent.dingdang.tvs;

import com.tencent.dingdang.tvs.message.response.Directive;

import java.util.concurrent.BlockingQueue;

/**
 * This thread takes a queue which will be filled with directives and dispatches them to the given
 * {@link DirectiveDispatcher} as they are added to the queue. This thread also supports blocking
 * the dispatching of directives.
 */
public class BlockableDirectiveThread extends Thread {
    private final BlockingQueue<Directive> directiveQueue;
    private final DirectiveDispatcher directiveDispatcher;
    private volatile boolean block;

    public BlockableDirectiveThread(BlockingQueue<Directive> directiveQueue,
            DirectiveDispatcher directiveDispatcher) {
        this(directiveQueue, directiveDispatcher, BlockableDirectiveThread.class.getSimpleName());
    }

    public BlockableDirectiveThread(BlockingQueue<Directive> directiveQueue,
            DirectiveDispatcher directiveDispatcher, String name) {
        this.directiveQueue = directiveQueue;
        this.directiveDispatcher = directiveDispatcher;
        setName(name);
    }

    public synchronized void block() {
        block = true;
    }

    public synchronized void unblock() {
        block = false;
        notify();
    }

    public synchronized void clear() {
        directiveQueue.clear();
    }

    @Override
    public void run() {
        while (true) {
            try {
                synchronized (this) {
                    if (block) {
                        wait();
                    }
                }
                Directive directive = directiveQueue.take();
                directiveDispatcher.dispatch(directive);
            } catch (InterruptedException e) {
            }
        }
    }
}
