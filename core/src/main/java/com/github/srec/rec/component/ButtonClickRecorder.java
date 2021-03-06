package com.github.srec.rec.component;

import com.github.srec.command.method.MethodCallEventCommand;
import com.github.srec.rec.EventRecorder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static com.github.srec.util.Utils.createParameterMap;
import static org.apache.commons.lang.StringUtils.isBlank;

/**
 * Understands recording button clicks.
 *
 * @author Vivek Prahlad
 */
public class ButtonClickRecorder extends AbstractComponentRecorder implements ActionListener {

    public ButtonClickRecorder(EventRecorder recorder) {
        super(recorder, AbstractButton.class);
    }

    void componentShown(Component component) {
        button(component).addActionListener(this);
    }

    private AbstractButton button(Component event) {
        return (AbstractButton) event;
    }

    void componentHidden(Component component) {
        button(component).removeActionListener(this);
    }

    protected boolean matchesComponentType(AWTEvent event) {
        Object source = event.getSource();
        return (source instanceof JButton
                && !source.getClass().getName().matches(".*Combo.*|javax\\.swing\\.plaf.*|FilePane.*"))
                || source.getClass() == JToggleButton.class;
    }

    public void actionPerformed(ActionEvent e) {
        AbstractButton button = (AbstractButton) e.getSource();
        recorder.record(new MethodCallEventCommand("click", button, null,
                createParameterMap("locator", extractComponentLocator(button))));
    }

    private String extractComponentLocator(AbstractButton button) {
        return isBlank(button.getText()) ? (isBlank(button.getName()) ? extractButtonCenterString(button) : button.getName()) : "text=" + button.getText();
    }

    private String extractButtonCenterString(AbstractButton button) {
        return "x=" + (button.getX() + button.getWidth() / 2) + ",y=" + (button.getY() + button.getHeight() / 2);
    }
}
