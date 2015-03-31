/* Copyright (c) 2015 "Naftoreiclag" https://github.com/Naftoreiclag
 *
 * Distributed under the Apache License Version 2.0 (http://www.apache.org/licenses/)
 * See accompanying file LICENSE
 */

package naftoreiclag.villagefive.console;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.niftygui.NiftyJmeDisplay;
import de.lessvoid.nifty.Nifty;
import de.lessvoid.nifty.NiftyEventSubscriber;
import de.lessvoid.nifty.builder.PanelBuilder;
import de.lessvoid.nifty.controls.TextField;
import de.lessvoid.nifty.controls.TextFieldChangedEvent;
import de.lessvoid.nifty.controls.label.builder.LabelBuilder;
import de.lessvoid.nifty.elements.Element;
import de.lessvoid.nifty.input.NiftyInputEvent;
import de.lessvoid.nifty.input.NiftyInputMapping;
import de.lessvoid.nifty.input.keyboard.KeyboardInputEvent;
import de.lessvoid.nifty.screen.KeyInputHandler;
import de.lessvoid.nifty.screen.Screen;
import de.lessvoid.nifty.screen.ScreenController;
import naftoreiclag.villagefive.Main;
import naftoreiclag.villagefive.util.HistoryArray;
import naftoreiclag.villagefive.util.KeyKeys;

public class DevConsoleAppState extends AbstractAppState implements ScreenController, KeyInputHandler {
    
    // Recall prepareConsole(); after resizing the screen.
    
    // TODO: fix scrollwheel
    
    // How many lines are remembered
    public static final int historyLength = 100;
    
    // Height of each line. Used to calculate the size of the console.
    public static final int lineHeight = 16;
    
    // Why do I need this?
    Main app;
    NiftyJmeDisplay niftyDisplay;
    Nifty nifty;
    Screen screen;
    
    // Important elements declared in the xml file
    TextField textField;
    Element outputBox;
    
    // Getting input from the textfield.
    String textFieldContents;
    @NiftyEventSubscriber(id = "input")
    public void onTextfieldChange(final String id, final TextFieldChangedEvent event) {
        textFieldContents = event.getText();
    }
    
    // Mappings for this screen. After all, I am a ScreenController. It's my job to do this.
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
    
    // Called by JME3 when the AppState is intialized. However, the gui may not yet be, so all that stuff is in bind();
    @Override
    public void initialize(AppStateManager stateManager, Application application) {
        super.initialize(stateManager, application);
        
        // Remember the application
        app = (Main) application;
        
        // Begin Nifty stuff.
        niftyDisplay = new NiftyJmeDisplay(app.getAssetManager(), app.getInputManager(), app.getAudioRenderer(), app.getGuiViewPort());
        nifty = niftyDisplay.getNifty();
        nifty.fromXml("Interface/DeveloperConsole.xml", "hide", this);
        app.getGuiViewPort().addProcessor(niftyDisplay);
    }

    // Called by Nifty when the gui has started up.
    @Override
    public void bind(Nifty nifty, Screen screen) {
        screen.addPreKeyboardInputHandler(mapping, this);
        this.screen = screen;
        textField = screen.findNiftyControl("input", TextField.class);
        outputBox = screen.findElementByName("output");
        prepareConsole();
        printLine("This is a console. You have no idea how long this took to make.\nType \"help\" for help.");
    }
    
    // When the user presses "Enter"
    private void sendInput() {
        if(textFieldContents == null) { return; }
        
        // Remove any preceeding spaces.
        for(int i = 0; i < textFieldContents.length(); ++ i) {
            if(textFieldContents.charAt(i) != ' ') {
                if(i > 0) {
                    textFieldContents = textFieldContents.substring(i);
                }
                break;
            } else if(i == textFieldContents.length() - 1) {
                textFieldContents = "";
            }
        }
        
        // Ignore empty inputs
        if(!textFieldContents.equals("")) {
            printLine(textFieldContents);
        }
        
        // Regardless, clear
        textField.setText("");
        textFieldContents = "";
    }
    
    // Each line has more information than just being a string.
    HistoryArray<ConsoleLine> outputHistory = new HistoryArray<ConsoleLine>(historyLength);
    private class ConsoleLine {
        final String message;
        int repeats = 0;
        
        ConsoleLine(String text) {
            this.message = text;
        }
        
        @Override
        public boolean equals(Object other) {
            if(other instanceof ConsoleLine) {
                ConsoleLine o = (ConsoleLine) other;
                
                return o.message.equals(this.message);
            } else {
                return false;
            }
        }
    }
    
    // Print a line to the console. Can accept strings with \n.
    private void printLine(String message) {
        // Repeat this method for each line
        String[] lines = message.split("\n");
        for(String line : lines) {
            
            ConsoleLine addMe = new ConsoleLine(line);
            ConsoleLine mostRecent = outputHistory.get(0);
            
            // Check if this line is a repeat
            if(addMe.equals(mostRecent)) {
                ++ mostRecent.repeats;
            } else {
                // Append this line
                outputHistory.add(addMe);
            }

            // Update all the output panels to show the lines
            for(int i = 0; i < outputContainers.length; ++ i) {
                ConsoleLine e = outputHistory.get(i);

                if(e != null) {
                    if(e.repeats == 0) {
                        outputContainers[i].setText(e.message);
                    } else {
                        outputContainers[i].setText(e.message + "[x" + e.repeats + "]");
                    }
                } else {
                    outputContainers[i].setText("");
                }
            }
        }
    }
    
    // Lines are shown using Nifty by making this array of panels each containing a label. I've made this class to make management simpler.
    private class ConsoleElement {
        Element panel;
        Element label;
        String lastText = "";
        ConsoleElement(Nifty nifty, Screen screen, Element outputBox) {
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
        
        // Example of Java efficiency at its finest.
        void setText(String message) {
            if(message == null) {
                message = "";
            }
            if(!lastText.equals(message)) {
                label.markForRemoval();
                final String dumbJava = message;
                label = new LabelBuilder() {{
                    alignLeft();
                    label(dumbJava);
                }}.build(nifty, screen, panel);
                lastText = dumbJava;
            }
        }
        
        String getText() {
            return lastText;
        }

        void removeSelf() {
            panel.markForRemoval();
        }
    }
    
    // All the line-holding containers, with zero being the lowest element.
    ConsoleElement[] outputContainers;
    
    // Populate the "id=output" panel with the containers.
    private void prepareConsole() {
        // Calculate how many lines we can fit in there.
        int height = outputBox.getHeight();
        int numLines = height / lineHeight;
        
        // If there are already containers in the panel, clear remove those.
        if(outputContainers != null) {
            for(int i = 0; i < numLines; ++ i) {
                outputContainers[i].removeSelf();
            }
        }
        
        // Make a new array and populate it.
        outputContainers = new ConsoleElement[numLines];
        for(int i = numLines - 1; i >= 0; -- i) {
            outputContainers[i] = new ConsoleElement(nifty, screen, outputBox);
        }
    }
    
    // Called by JME3. When switching between enabled and disabled states, simply switch between Nifty Screens.
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

    // Disables the console and flushes input
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

    // Unused stuff below
    
    @Override
    public void update(float tpf) {}
    @Override
    public void cleanup() {}
    @Override
    public void onStartScreen() {}
    @Override
    public void onEndScreen() {}

}
