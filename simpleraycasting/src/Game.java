
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;

import javax.swing.SwingUtilities;

public class Game implements Runnable, KeyListener{
	
	private Camera camera;
	private Display display;
	public int width, height;
	public String title;
	
	private boolean running = false;
	private Thread thread;
	
	private BufferStrategy bs;
	private Graphics g;
	
	public Game(String title, int width, int height)
	{
		camera = new Camera(width, height);
		this.width = width;
		this.height = height;
		this.title = title;
	}
	
	public void initialise()
	{
		display = new Display(title, width, height, this);
	}
	
	private void update()
	{
		//Update
	}
	
	private void draw(){
		bs = display.getCanvas().getBufferStrategy();
		if(bs == null){
			display.getCanvas().createBufferStrategy(3);
			return;
		}
		g = bs.getDrawGraphics();
		//draw
		g.fillRect(0, 0, width, height);
		camera.update(g);
		//end drawing
		bs.show();
		g.dispose();
}
	public static void main(String [] args)
	{
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				new Game("RayCasting", 512, 384).start();
			}
		});
	}
	
	public void run(){	
		initialise();
		while(running){
			update();
			draw();
		}
		stop();
	}
	
	public synchronized void start(){
		if(running)
			return;
		running = true;
		thread = new Thread(this);
		thread.start();
	}
	
	public synchronized void stop(){
		if(!running)
			return;
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		switch(e.getKeyCode())
		{
			case KeyEvent.VK_RIGHT:
				camera.rotate(false);
				break;
			case KeyEvent.VK_LEFT:
				camera.rotate(true);
				break;
			case KeyEvent.VK_UP:
				camera.moveForward();
				break;
			case KeyEvent.VK_DOWN:
				break;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

}
