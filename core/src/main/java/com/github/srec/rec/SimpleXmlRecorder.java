/*
 * Copyright 2010 Victor Tatai
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for
 * the specific language governing permissions and limitations under the License.
 */

package com.github.srec.rec;

import com.github.srec.command.base.ValueCommand;
import com.github.srec.command.method.MethodCallEventCommand;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * A simple recorder which records XML. Besides regular recording, it allows the following shortcuts:
 * <ul>
 * <li>Ctrl-D: Pause/resume recording</li>
 * <li>Ctrl-Alt-S: Take a screenshot of the entire screen</li>
 * <li>Ctrl-Shift-S: Take a screenshot of the current open frame</li>
 * <li>Ctrl-Alt-Shift-S: Take a screenshot of the current open internal frame</li>
 * </ul>
 *
 * @author Victor Tatai
 */
public class SimpleXmlRecorder implements RecorderEventCallback {
    private PrintWriter writer;
    private Recorder recorder = new Recorder(this);
    private MethodCallEventCommand lastEvent;

    public SimpleXmlRecorder(PrintWriter writer) {
        this.writer = writer;
    }

    public void start(String className, String[] params) {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {
            @Override
            public boolean dispatchKeyEvent(KeyEvent keyEvent) {
                if (keyEvent.getID() != KeyEvent.KEY_PRESSED) return false;
                if (!keyEvent.isControlDown()) return false;
                if (keyEvent.getKeyCode() == KeyEvent.VK_D) {
                    if (recorder.isRecording()) {
                        recorder.setRecording(false);
                        writeLastEvent();
                    } else {
                        recorder.setRecording(true);
                    }
                }
                return false;
            }
        });
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                SimpleXmlRecorder.this.stop();
            }
        });
        // Print test suite and test case declarations
        writer.println("<suite>");
        writer.println("  <test_case>");

        recorder.init();
        recorder.setRecording(true);
        try {
            Class<?> cl = Class.forName(className);
            Method m = cl.getMethod("main", String[].class);
            m.invoke(null, new Object[] {params});
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public void stop() {
        recorder.shutdown();
        writeLastEvent();

        // Print test suite and test case end tags
        writer.println("  </test_case>");
        writer.println("</suite>");
        writer.flush();

        writer.close();
    }

    private void writeLastEvent() {
        if (lastEvent != null) {
            writer.println(serialize(lastEvent));
            lastEvent = null;
        }
        writer.flush();
    }

    @Override
    public void addEvent(MethodCallEventCommand event) {
        System.out.println("Event: " + event);
        if (lastEvent != null) {
            writer.println(serialize(lastEvent));
        }
        lastEvent = event;
    }

    private String serialize(MethodCallEventCommand event) {
        return "<" + event.getName() + " " + serialize(event.getParameters()) + "/>";
    }

    private String serialize(Map<String, ValueCommand> parameters) {
        StringBuilder strb = new StringBuilder();
        for (Map.Entry<String, ValueCommand> entry : parameters.entrySet()) {
            strb.append(entry.getKey()).append("=\"").append(entry.getValue().getValue(null)).append("\" ");
        }
        return strb.toString();
    }

    @Override
    public void replaceLastEvent(MethodCallEventCommand event) {
        lastEvent = event;
    }

    public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, IOException {
        if (args.length == 0 || args.length > 2) {
            System.err.println("Usage: " + SimpleXmlRecorder.class.getSimpleName() + " <main class name> [output file]");
            System.err.println("If output file is not specified recording is redirected to stdout.");
            return;
        }
        SimpleXmlRecorder sr;
        if (args.length == 1) {
            sr = new SimpleXmlRecorder(new PrintWriter(System.out, true));
        } else {
            sr = new SimpleXmlRecorder(new PrintWriter(new FileWriter(args[1])));
        }
        sr.start(args[0], new String[0]);
    }
}
