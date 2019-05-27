package ch.fhnw.chargingstationsfx.presentationmodel;

import javafx.beans.property.Property;

public class ValueChangeCommand <T> implements Command {
    private RootPM  rootPM;
    private Property <T>  property;
    private T  oldValue;
    private T  newValue;


    public ValueChangeCommand(RootPM rootPM, Property<T> property, T oldValue, T newValue) {
        this.rootPM = rootPM;
        this.property = property;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

        public void undo() {
        rootPM.setPropertyChangeListenerForUndoSupport(property, oldValue);
    }

    public void redo() {
        rootPM.setPropertyChangeListenerForUndoSupport(property, newValue);
    }


}




