/*
 * **********************************************************************
 *
 *  Copyright (C) 2010 - 2014
 *
 *  [Component.java]
 *  JACPFX Project (https://github.com/JacpFX/JacpFX/)
 *  All rights reserved.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an "AS IS"
 *  BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 *  express or implied. See the License for the specific language
 *  governing permissions and limitations under the License.
 *
 *
 * *********************************************************************
 */
package org.jacp.test.components;

import javafx.event.Event;
import org.jacp.test.main.ApplicationLauncherMessagingTest;
import org.jacpfx.api.annotations.Resource;
import org.jacpfx.api.annotations.component.Component;
import org.jacpfx.api.annotations.lifecycle.PostConstruct;
import org.jacpfx.api.annotations.lifecycle.PreDestroy;
import org.jacpfx.api.annotations.method.OnAsyncMessage;
import org.jacpfx.api.message.Message;
import org.jacpfx.rcp.component.CallbackComponent;
import org.jacpfx.rcp.componentLayout.FXComponentLayout;
import org.jacpfx.rcp.context.Context;
import org.jacpfx.rcp.util.FXUtil;

import java.util.ResourceBundle;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

/**
 * A simple JacpFX FXML UI component
 *
 * @author <a href="mailto:amo.ahcp@gmail.com"> Andy Moncsek</a>
 */

@Component(id = ComponentIds.CallbackComponentMessagingTest1, active = true, resourceBundleLocation = "bundles.languageBundle", localeID = "en_US")
public class CallbackComponentMessagingTest1 implements CallbackComponent {

    private final Logger log = Logger.getLogger(CallbackComponentMessagingTest1.class
            .getName());

;

    @Resource
    private static Context context;

    public static AtomicInteger counter = new AtomicInteger(10000);
    public static CountDownLatch wait = new CountDownLatch(1);

    @Override
    /**
     * The handleAction method always runs outside the main application thread. You can create new nodes, execute long running tasks but you are not allowed to manipulate existing nodes here.
     */
    public Object handle(final Message<Event, Object> action) {


        return null;
    }

    @OnAsyncMessage(String.class)
    public Object onStringMessage(final Message<Event, Object> action) {
        if (action.messageBodyEquals(FXUtil.MessageUtil.INIT)) {
            ApplicationLauncherMessagingTest.latch.countDown();
        }
        else {
            if (counter.get() > 1) {
                counter.decrementAndGet();

            } else {
                System.out.println("Component "+ComponentIds.CallbackComponentMessagingTest1+" FINISH");
                if (wait.getCount() > 0) wait.countDown();

                return null;
            }
        }

        return "message";
    }

    public static void fireMessage() {
        context.send("id14.id010", "message");
    }

    public static void fireBurst(final int count) {
        Thread t = new Thread(() -> {
            for (int i = 0; i < count; i++) {
                getContext().send("id14.id010", "message");
            }
        });
        t.setDaemon(true);
        t.start();
    }

    public static synchronized Context getContext() {
        return context;
    }


    @PostConstruct
    /**
     * The @OnStart annotation labels methods executed when the component switch from inactive to active state
     * @param arg0
     * @param resourceBundle
     */
    public void onStartComponent(final FXComponentLayout arg0,
                                 final ResourceBundle resourceBundle) {
        this.log.info("run on start  of CallbackComponentMessagingTest1: "+this);

    }

    @PreDestroy
    /**
     * The @OnTearDown annotations labels methods executed when the component is set to inactive
     * @param arg0
     */
    public void onTearDownComponent(final FXComponentLayout arg0) {
        this.log.info("run on tear down of CallbackComponentMessagingTest1 "+this);

    }


}
