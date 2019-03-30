
import java.awt.Canvas;
import java.awt.Dimension;

import javax.swing.JFrame;

public class Display {

	private Game game;
	private JFrame frame;
	private Canvas canvas;
	
	private String title;
	private int width, height;
	
	public Display(String title, int width, int height, Game game){
		this.title = title;
		this.width = width;
		this.height = height;
		this.game = game;
		
		init();
	}
	
	private void init(){
		frame = new JFrame(title);
		frame.setSize(width, height);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.setLocationRelativeTo(null);
		
		canvas = new Canvas();
		canvas.setPreferredSize(new Dimension(width, height));
		canvas.setMaximumSize(new Dimension(width, height));
		canvas.setMinimumSize(new Dimension(width, height));
		canvas.setFocusable(true); //focus on canvas
		canvas.addKeyListener(game);
		
		frame.add(canvas);
		frame.pack();
		frame.setVisible(true);
	}

	public Canvas getCanvas(){
		return canvas;
	}
}
