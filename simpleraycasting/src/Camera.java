import java.awt.Color;
import java.awt.Graphics;


public class Camera {
	
	private static final int ROTSPEED = 5;
	private static final int SPRINTSPEED = 10;
	
	private static final int fov = 60;
	private final int w;
	private final int h;
	private final double dist;
	private final double angleInc;
	
	private Vector pos;
	private double currentAngle;
	
	public Camera(int w, int h) 
	{
		this.w = w;
		this.h = h;
		this.dist = ((w/ 2.0) / Math.tan(Math.toRadians(fov/2)));
		this.angleInc = (double)fov / w;
		pos = new Vector(65,65);
		currentAngle = 0;
	}
	
	public void update(Graphics g) 
	{
		if(currentAngle < 0)
			currentAngle = 360 + currentAngle;
		else if(currentAngle >= 360)
			currentAngle -= 360;

		double angle = currentAngle + 30;
		
		for(int i = 0; i < w; i++)
		{
			if(angle >= 360) angle -= 360;
			else if(angle < 0) angle = 360 + angle;
			Ray ray = new Ray(angle, pos);
			Vector wall = ray.findWallRelativeToAngle();
			double distance = ray.getDistanceRelativeToPlayer(wall);
			
			
			//remove fishEye effect
			distance = (distance * Math.cos(Math.toRadians(currentAngle - angle)));
			double projectedWallHeight = (WorldMap.cube / distance) * dist;
			int y = (int)((h / 2.0) - (projectedWallHeight/2.0));
			
			drawRect(g, i, y, 1, (int)projectedWallHeight, getColor(Ray.checkWall(wall.getX(), wall.getY())));
		
			angle -= angleInc;
		}
	}
	
	public Color getColor(int wall)
	{
		switch(wall)
		{
			case 2: return Color.BLUE;
			case 3: return Color.CYAN;
		}
		return Color.RED;
	}
	
	public void drawRect(Graphics g, int x, int y, int w, int h, Color color)
	{
		g.setColor(color);
		g.fillRect(x, y, w, h);
	}
	
	public void rotate(boolean left)
	{
		currentAngle += (left) ? ROTSPEED : -(ROTSPEED);
	}
	
	public void moveForward()
	{	
		switch((int)currentAngle)
		{
			case 0:
				pos.setX(pos.getX() + 5);
				return;
			case 90:
				pos.setY(pos.getY() - 5);
				return;
			case 180:
				pos.setX(pos.getX() - 5);
				return;
			case 270:
				pos.setY(pos.getY() + 5);
				return;
		}
		
		boolean rayFacingUp = currentAngle <= 180 && currentAngle >= 0;
		boolean rayFacingRight = (currentAngle <= 90 && currentAngle >= 0) || (currentAngle >= 270 && currentAngle < 360);
		
		//sin(delta) = dy / moveStep;
		double deltaAngle = findDeltaAngle();
		double dy = Math.sin(Math.toRadians(deltaAngle)) * SPRINTSPEED;
		double dx = Math.sqrt((SPRINTSPEED * SPRINTSPEED) - (dy * dy));
		
		dx *= (rayFacingRight ? 1 : -1);
		dy *= (rayFacingUp ? -1 : 1);
		pos.incX(dx);
		pos.incY(dy);
	}
	
	public double findDeltaAngle()
	{
		if(currentAngle > 90 && currentAngle < 180)
			return 180 - currentAngle;
		else if(currentAngle > 180 && currentAngle < 270)
			return currentAngle - 180;
		else if(currentAngle > 270 && currentAngle < 360)
			return 360 - currentAngle;
		return currentAngle;
	}
}
