/* Copyright (c) 2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.niftygui.NiftyJmeDisplay;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.controls.Label;
import de.lessvoid.nifty.controls.ListBox;
import de.lessvoid.nifty.controls.NiftyControl;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.controls.TextFieldChangedEvent;
import de.lessvoid.nifty.controls.label.builder.LabelBuilder;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.elements.render.TextRenderer;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.input.NiftyInputMapping;
import de.lessvoid.nifty.input.keyboard.KeyboardInputEvent;
import de.lessvoid.nifty.screen.KeyInputHandler;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import java.util.ArrayList;
import java.util.List;
import naftoreiclag.villagefive.util.HistoryArray;
import naftoreiclag.villagefive.util.KeyKeys;

public class DevConsoleAppState extends AbstractAppState implements ScreenController, KeyInputHandler {
    
    private Main app;
    NiftyJmeDisplay niftyDisplay;
    Nifty nifty;
    
    TextField textField;
    Element outputBox;
    
    String input;
    Screen screen;
    
    NiftyInputMapping mapping = new NiftyInputMapping() {
        @Override
        public NiftyInputEvent convert(KeyboardInputEvent inputEvent) {
            
            boolean pressed = inputEvent.isKeyDown();
            int key = inputEvent.getKey();
            
            if(pressed) {
                if(key == KeyKeys.consoleKey) {
                    return NiftyInputEvent.ConsoleToggle;
                } else if(key == KeyboardInputEvent.KEY_RETURN) {
                    return NiftyInputEvent.Activate;
                } else if(key == KeyboardInputEvent.KEY_TAB) {
                    if(inputEvent.isShiftDown()) {
                        return NiftyInputEvent.PrevInputElement;
                    } else {
                        return NiftyInputEvent.NextInputElement;
                    }
                }
            }
            
            return null;
        }
    };
    
    @Override
    public void initialize(AppStateManager stateManager, Application application) {
        super.initialize(stateManager, application);
        
        app = (Main) application;
        
        niftyDisplay = new NiftyJmeDisplay(app.getAssetManager(), app.getInputManager(), app.getAudioRenderer(), app.getGuiViewPort());
        nifty = niftyDisplay.getNifty();
        nifty.fromXml("Interface/DeveloperConsole.xml", "hide", this);
        app.getGuiViewPort().addProcessor(niftyDisplay);
    }

    @Override
    public void bind(Nifty nifty, Screen screen) {
        screen.addPreKeyboardInputHandler(mapping, this);
        this.screen = screen;
        textField = screen.findNiftyControl("input", TextField.class);
        outputBox = screen.findElementByName("output");
        prepareConsole();
    }
    
    private void sendInput() {
        if(input.equals("") || input == null) { return; }
        
        
        /*
        ConsoleOutputEntry e = new ConsoleOutputEntry(input);
        entries.add(e);
        
        System.out.println(outputBox.getHeight());
        */
        
        write(input);
        
        
        textField.setText("");
        input = "";
    }
    
    private void write(String input)
    {
        entries.add(new ConsoleOutputEntry(input));
        
        for(int i = 0; i < lines.length; ++ i)
        {
            ConsoleOutputEntry e = entries.get(i);
            
            if(e != null) {
                lines[i].setText(e.message);
            } else {
                lines[i].setText("");
            }
        }
    }
    
    private int lineHeight = 15;
    
    ConsoleElement[] lines;
    private void prepareConsole() {
        int height = outputBox.getHeight();
        
        int numLines = height / lineHeight;
        
        lines = new ConsoleElement[numLines];
        
        for(int i = numLines - 1; i >= 0; -- i) {
            lines[i] = new ConsoleElement(nifty, screen, outputBox);
        }
    }
    
    // I need this because...
    private class ConsoleElement {
        Element panel;
        Element label;
        ConsoleElement(Nifty nifty, Screen screen, Element outputBox)
        {
            // Portable javadoc v
            //new LabelBuilder()
            //new PanelBuilder()
            
            panel = new PanelBuilder() {{
                childLayoutHorizontal();
                height(lineHeight + "px");
            }}.build(nifty, screen, outputBox);
            
            label = new LabelBuilder() {{
                alignLeft();
                label("");
            }}.build(nifty, screen, panel);
        }
        
        // Max efficiency by removing a label and then immidately readding one.
        void setText(final String text) {
            label.markForRemoval();
            label = new LabelBuilder() {{
                alignLeft();
                label(text);
            }}.build(nifty, screen, panel);
        }
    }
    
    HistoryArray<ConsoleOutputEntry> entries = new HistoryArray<ConsoleOutputEntry>(30);
    private class ConsoleOutputEntry
    {
        final String message;
        
        ConsoleOutputEntry(String text) {
            this.message = text;
        }
    }
    
    @Override
    public void setEnabled(boolean enable) {
        super.setEnabled(enable);
        
        if(this.initialized){
            if(enable) {
                nifty.gotoScreen("show");
            } else {
                nifty.gotoScreen("hide");
            }
        }
    }
    
    @Override
    public void update(float tpf) {}
    
    @Override
    public void cleanup() {}

    @Override
    public boolean keyEvent(NiftyInputEvent inputEvent) {
        if(inputEvent == NiftyInputEvent.ConsoleToggle) {
            this.setEnabled(false);
            return true;
        } else if(inputEvent == NiftyInputEvent.Activate) {
            sendInput();
        }
        return false;
    }

    @NiftyEventSubscriber(id = "input")
    public void onTextfieldChange(final String id, final TextFieldChangedEvent event) {
        input = event.getText();
    }

    @Override
    public void onStartScreen() {}

    @Override
    public void onEndScreen() {}

}
