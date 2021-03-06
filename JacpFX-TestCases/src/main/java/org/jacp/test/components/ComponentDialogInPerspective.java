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
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import org.jacp.test.dialogs.DialogDialogInComponentTest;
import org.jacp.test.dialogs.DialogScopePrototypeComponentTest;
import org.jacp.test.dialogs.DialogScopeSingletonComponentTest;
import org.jacp.test.main.ApplicationLauncherDialogInPerspectiveTest;
import org.jacpfx.api.annotations.Resource;
import org.jacpfx.api.annotations.component.View;
import org.jacpfx.api.annotations.lifecycle.PostConstruct;
import org.jacpfx.api.annotations.lifecycle.PreDestroy;
import org.jacpfx.api.message.Message;
import org.jacpfx.rcp.component.FXComponent;
import org.jacpfx.rcp.componentLayout.FXComponentLayout;
import org.jacpfx.rcp.components.managedFragment.ManagedFragmentHandler;
import org.jacpfx.rcp.context.Context;
import org.jacpfx.rcp.util.FXUtil;

import java.util.ResourceBundle;
import java.util.logging.Logger;

/**
 * A simple JacpFX FXML UI component
 *
 * @author <a href="mailto:amo.ahcp@gmail.com"> Andy Moncsek</a>
 */

@View(id = ComponentIds.ComponentDialogInPerspective, active = true, resourceBundleLocation = "bundles.languageBundle", localeID = "en_US", initialTargetLayoutId = "content0")
public class ComponentDialogInPerspective implements FXComponent {

    private final Logger log = Logger.getLogger(ComponentDialogInPerspective.class
            .getName());

    String current = "content0";
    Button button = new Button("NULLPOINTER");
    VBox container = new VBox();
    Label label = new Label();

    @Resource
    private static Context context;

    @Override
    /**
     * The handleAction method always runs outside the main application thread. You can create new nodes, execute long running tasks but you are not allowed to manipulate existing nodes here.
     */
    public Node handle(final Message<Event, Object> action) {

        return null;
    }

    @Override
    /**
     * The postHandleAction method runs always in the main application thread.
     */
    public Node postHandle(final Node arg0,
                           final Message<Event, Object> action) {
        if (action.messageBodyEquals(FXUtil.MessageUtil.INIT)) {

            button.setOnMouseClicked(new EventHandler<MouseEvent>() {

                @Override
                public void handle(MouseEvent event) {
                    // force nullpointer
                    String test=null;
                    int l = test.length();


                }
            });
            //button.setStyle("-fx-background-color: red");
            label.setText(" NULLPOINTER TEST");
            container.getChildren().addAll(button, label);
            ApplicationLauncherDialogInPerspectiveTest.latch.countDown();
        }  else if (action.messageBodyEquals("dialog1")) {
            ManagedFragmentHandler<DialogDialogInComponentTest> handler = context.getManagedFragmentHandler(DialogDialogInComponentTest.class);
            if (handler.getController() != null) {
                DialogDialogInComponentTest.latch.countDown();
            }
            if (handler.getFragmentNode() != null) {
                DialogDialogInComponentTest.latch.countDown();
            }
            handler.getController().init();
            return handler.getFragmentNode();
        }
        else if (action.messageBodyEquals("dialog2")) {
            ManagedFragmentHandler<DialogScopeSingletonComponentTest> handler = context.getManagedFragmentHandler(DialogScopeSingletonComponentTest.class);
            if (handler.getController() != null) {
                DialogScopeSingletonComponentTest.latch.countDown();
            }
            if (handler.getFragmentNode() != null) {
                DialogScopeSingletonComponentTest.latch.countDown();
            }
            handler.getController().init();
            ManagedFragmentHandler<DialogScopeSingletonComponentTest> handler2 = context.getManagedFragmentHandler(DialogScopeSingletonComponentTest.class);
             if(handler.getController().equals(handler2.getController())) {
                 DialogScopeSingletonComponentTest.latch.countDown();
             }

            return handler.getFragmentNode();
        }
        else if (action.messageBodyEquals("dialog3")) {
            ManagedFragmentHandler<DialogScopePrototypeComponentTest> handler = context.getManagedFragmentHandler(DialogScopePrototypeComponentTest.class);
            if (handler.getController() != null) {
                DialogScopePrototypeComponentTest.latch.countDown();
            }
            if (handler.getFragmentNode() != null) {
                DialogScopePrototypeComponentTest.latch.countDown();
            }
            handler.getController().init();

            ManagedFragmentHandler<DialogScopePrototypeComponentTest> handler3 = context.getManagedFragmentHandler(DialogScopePrototypeComponentTest.class);
             if(!handler.getController().equals(handler3.getController())) {
                 DialogScopePrototypeComponentTest.latch.countDown();
             }
            return handler.getFragmentNode();
        }

        return container;
    }

    public static void initDialog1() {
        context.send("dialog1");
    }
    public static void initDialog2() {
        context.send("dialog2");
    }
    public static void initDialog3() {
        context.send("dialog3");
    }

    @PostConstruct
    /**
     * The @OnStart annotation labels methods executed when the component switch from inactive to active state
     * @param arg0
     * @param resourceBundle
     */
    public void onStartComponent(final FXComponentLayout arg0,
                                 final ResourceBundle resourceBundle) {


    }

    @PreDestroy
    /**
     * The @OnTearDown annotations labels methods executed when the component is set to inactive
     * @param arg0
     */
    public void onTearDownComponent(final FXComponentLayout arg0) {
        this.log.info("run on tear down of ComponentRight ");

    }


}
