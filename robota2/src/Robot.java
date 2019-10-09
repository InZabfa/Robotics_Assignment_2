import Handlers.RobotHandler;

public class Robot{
    public static void main(String[] args){
         try {
        	 RobotHandler robot = new RobotHandler();
             robot.Start();
         } catch (Exception e) {
             e.printStackTrace();
         }
    }
}