package Interfaces;

import java.lang.reflect.Method;

import Global.RucksackContainer;

public abstract class ITaskHandler extends IRucksack {
    /**
     * Property to store accepted ITask names. 
     */
    private String[] acceptedTasks;
    
    
    private RucksackContainer rcontainer;

    /**
     * Initialises the TaskHandler.
     * @param acceptedITasks
     */
    public ITaskHandler(RucksackContainer ruckcontainer, String...acceptedITasks){ 
    	super(ruckcontainer);
    	
    	this.rcontainer = ruckcontainer;
    	this.acceptedTasks = acceptedITasks; 
    }
    
    public RucksackContainer GetRucksackContainer() { return this.rcontainer; }

    /***
     * Determines if a task is accepted by this TaskHandler.
     * @param task
     * @return if given ({@link Interfaces.ITask}) task is accepted
     */
    public boolean AcceptsTask(ITask task){
        for (String t : this.acceptedTasks) {
            if (t.equalsIgnoreCase(task.getClass().getSimpleName())){
                return true;
            } else continue;
        }
        return false;
    }
    
    public ITaskHandler createInstance(RucksackContainer rcontainer, String...acceptedITasks) {
        try {
            return this.getClass().getConstructor(RucksackContainer.class).newInstance(rcontainer);
        } catch (Exception e) {
            throw new AssertionError(e);
        }
    }

    /***
     * Method to handle the task if accepted by TaskHandler
     * @param givenTask - Task given to the handler.
     */
    public void HandleTask(ITask givenTask) {
    	try 
		{
			for (Pair p : givenTask.GetPairArguments()) {
				Class<?> base = this.getClass();
				Method method = base.getMethod(p.Key(), String.class);
				method.invoke(this.createInstance(this.GetRucksackContainer(),this.acceptedTasks), p.Value());
				//System.out.println("[" + givenTask.GetPairArguments().indexOf(p) +"] Task<" + p.Key() + "> running.");
			}
		} 
		catch (Exception e) { 
			e.printStackTrace(); 
		}
    }
}
