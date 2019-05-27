package ch.fhnw.chargingstationsfx.presentationmodel;

import javafx.beans.property.Property;

public class ValueChangeCommand <T> implements Command {

    private final RootPM root;
    private final Property<T> property;
    private final T oldValue;
    private final T newValue;


    public ValueChangeCommand(RootPM root, Property<T> property, T oldValue, T newValue) {
        this.root = root;
        this.property = property;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public void undo() {
        root.setPropertyValueWithoutUndoSupport(property, oldValue);
    }

    public void redo() {
        root.setPropertyValueWithoutUndoSupport(property, newValue);
    }
}