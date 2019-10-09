package Global;

public class Properties{
	
	public static final String EMPTY_STRING = "";
	
    /** Stores all Generic properties such as server running flag. */
    public static class Generic{
        public static boolean ROBOT_RUNNING = true;
    }

    /** Stores all properties used for the server */
    public static class Server{
        public static final int PORT = 5984;
        
        //Change this IP to PC IP
        public static final String IP = "192.168.70.186";
    }
    
    public static class Map{
    	 public static final int CELL_SIZE = 25;
    	 public static final int ROWS = 6;
    	 public static final int COLUMNS = 6;
    	 public static final double OBSTRUCTION_DISTANCE = 0.23;
    	 public static final int INACCESSIBLE_CELL_VALUE = 999;
    }
    
    public static class Regex{
    	public static final String COMMAND_NAME = "^\\w+";
    	public static final String CAPTURE_ARGS = "(\\w+)\\=(\\w+)(?:,)?";
    }
    
    public static class Tasks{
    	public static final String PACKAGE = "Tasks";
    	public static final String PACKAGE_STR_FORMAT = "%1$s.%2$s";
    }
}