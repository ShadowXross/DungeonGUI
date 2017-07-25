package GridGUI;


public class GameState extends Thread{
	    	
	   	  private final Object GUI_INITIALIZATION_MONITOR = new Object();
	      private boolean pauseThreadFlag = false;
	      protected boolean exit = false;
	    
	   	  public GameState(boolean pauseThreadFlag) { 
	   		  super("GameState");
	   		  this.pauseThreadFlag = pauseThreadFlag; 
	   	  }
	   	  
	   	  public boolean getpauseThreadFlag() { 
	   		  return pauseThreadFlag; 
	   	  }
	   	  
	   	  public void setPauseThreadFlag(boolean pauseThreadFlag) { 
	   		  this.pauseThreadFlag = pauseThreadFlag; 
	   	  }
	   	  
	      @Override
	      public void run() {
	    	  while(!exit)
	    	  {
	    		  checkForPause();
	    	  }
	    	 
	    	 }

	        private void checkForPause() {
	            synchronized (GUI_INITIALIZATION_MONITOR) {
	                while (pauseThreadFlag) {
	                    try {
	                        GUI_INITIALIZATION_MONITOR.wait();
	                    } catch (Exception e) {}
	                }
	            }
	        }

	        public void pauseThread() throws InterruptedException {
	            pauseThreadFlag = true;
	        }

	        public void resumeThread() {
	            synchronized(GUI_INITIALIZATION_MONITOR) {
	                pauseThreadFlag = false;
	                GUI_INITIALIZATION_MONITOR.notify();
	            }
	        }
}
