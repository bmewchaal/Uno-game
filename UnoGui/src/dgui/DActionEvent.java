package dgui;

/**
 * Custom action event class for the D GUI layer.
 */
public class DActionEvent {
    private Object source;
    private String actionCommand;
    private int id;
    
    /**
     * Create a new DActionEvent with the specified source
     */
    public DActionEvent(Object source) {
        this(source, "");
    }
    
    /**
     * Create a new DActionEvent with the specified source and action command
     */
    public DActionEvent(Object source, String actionCommand) {
        this(source, 0, actionCommand);
    }
    
    /**
     * Create a new DActionEvent with the specified source, id, and action command
     */
    public DActionEvent(Object source, int id, String actionCommand) {
        this.source = source;
        this.id = id;
        this.actionCommand = actionCommand;
    }
    
    /**
     * Get the source of the event
     */
    public Object getSource() {
        return source;
    }
    
    /**
     * Get the action command of the event
     */
    public String getActionCommand() {
        return actionCommand;
    }
    
    /**
     * Get the ID of the event
     */
    public int getID() {
        return id;
    }
    
    /**
     * Set the action command of the event
     */
    public void setActionCommand(String actionCommand) {
        this.actionCommand = actionCommand;
    }
    
    /**
     * Get a string representation of the event
     */
    @Override
    public String toString() {
        return getClass().getName() + "[source=" + source + ",actionCommand=" + actionCommand + "]";
    }
}
