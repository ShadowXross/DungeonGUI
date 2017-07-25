package GridGUI;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class BackGroundPane extends JPanel implements Runnable{

    /**
	 * Created to stop the compiler from complaining
	 */
	private static final long serialVersionUID = -6912546907806584621L;
	
	private Image background;
    private Image actingImg;
    private Image greenArrow;
    private Image redMinus;
	private Thread animator;
	private int x,y;
	private boolean down;
	private final int B_WIDTH = 1400;
    public static final int B_HEIGHT = 920;
    private final int DELAY = 1;
    private volatile boolean runThread;
    
    public BackGroundPane(Image image) {     
        this.background = image;      
    }

    public void startDownAnimation(int x, int y, Image animationImage)
    {
    	actingImg = animationImage;
    	setStart(x,y);
    	runThread = true;
    	this.down = true;
    	// Need to make a new Thread every time
    	// Otherwise get illegal state error
    	animator = new Thread(this);
    	animator.start();
    }
    
    public void startUpAnimation(int x, int y, Image animationImage)
    {
    	actingImg = animationImage;
    	setStart(x,y);
    	runThread = true;
    	this.down = false;
    	// Need to make a new Thread every time
    	// Otherwise get illegal state error
    	animator = new Thread(this);
    	animator.start();
    }
    
    public void startHealAnimation(int x, int y)
    {
    	actingImg = greenArrow;
    	setStart(x,y);
    	runThread = true;
    	down = false;
    	// Need to make a new Thread every time
    	// Otherwise get illegal state error
    	animator = new Thread(this);
    	animator.start();
    }
    
    public void startNegativeStatusAnimation(int x, int y)
    {
    	actingImg = redMinus;
    	setStart(x,y);
    	runThread = true;
    	// Need to make a new Thread every time
    	// Otherwise get illegal state error
    	animator = new Thread(this);
    	animator.start();
    }
    
    private void setStart(int x, int y)
    {
    	 this.x = x;
         this.y = y;
    }
    
    @Override
    public Dimension getPreferredSize() {
//        return background == null ? new Dimension(0, 0) : new Dimension(background.getWidth(this), background.getHeight(this));
    	return background == null ? new Dimension(0, 0) : new Dimension(B_WIDTH, B_HEIGHT);   
    }

    private Image loadImage(String path, int width, int height) {
        ImageIcon swordIcon = new ImageIcon(this.getClass().getResource(path));
        Image image = swordIcon.getImage(); // transform it 
		Image scaledImage = image.getScaledInstance( 125, 250, java.awt.Image.SCALE_DEFAULT); // scale it the smooth way  
		return scaledImage;
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        if (background != null) {                
            g.drawImage(background, 0, 0, this.getWidth(), this.getHeight(), null);
        }
   
    }

    // Paint children paints ON TOP of children i.e the animated gifs 
    @Override
    protected void paintChildren(Graphics g) {
    	  super.paintChildren(g);
    	  if(runThread)
          {
          	drawSword(g);
          }
    }
    private void drawSword(Graphics g) {

        g.drawImage(actingImg, x, y, this);
        Toolkit.getDefaultToolkit().sync();
    }

    private void cycleDown() {

        y += 1;

        if (y > B_HEIGHT) {
            runThread = false;
        }
    }
    
    private void cycleUp() {

        y -= 1;

        if (y < 0) {
            runThread = false;
        }
    }
    
    @Override
    public void run() {

        if(down)
        {
        	while (runThread) 
        	{
                cycleDown();
                repaint();
                paintAnimation();
            }
        }
        else
        {
        	while (runThread) 
        	{
                cycleUp();
                repaint();
                paintAnimation();
            }
        }
    }
    
    private void paintAnimation()
    {
    	long beforeTime, timeDiff, sleep;

        beforeTime = System.currentTimeMillis();
        
    	timeDiff = System.currentTimeMillis() - beforeTime;
        sleep = DELAY - timeDiff;

        if (sleep < 0) {
            sleep = 2;
        }

        try {
            Thread.sleep(sleep);
        } catch (InterruptedException e) {
            System.out.println("Interrupted: " + e.getMessage());
        }

        beforeTime = System.currentTimeMillis();
    }
}
