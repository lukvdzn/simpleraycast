
public class Ray {
	
	private final double angle;
	private final Vector playerPos;
	private final boolean rayFacingUp;
	private final boolean rayFacingRight;
	private final double angleTanConstant;
	
	private int textureSlice; // needed for drawing actual texture walls
	
	
	public Ray(double angle, Vector playerPos)
	{
		this.angle = angle;
		this.playerPos = playerPos;
		rayFacingUp = angle <= 180 && angle >= 0;
		rayFacingRight = (angle <= 90 && angle >= 0) || (angle >= 270 && angle < 360);
		this.angleTanConstant = Math.tan(Math.toRadians(angle));
	}
	
	
	/**find the wall with the closest proximity to player*/
	public Vector findWallRelativeToAngle()
	{
		Vector vh = findHorizontalIntersection(), vv = findVerticalIntersection();
		
		if(vh == null)
		{
			textureSlice = (int) (vv.getY() % 64);
			return vv;
		}
		else if(vv == null) 
		{
			textureSlice = (int) (vh.getX() % 64);
			return vh;
		}
		
		double distToWallHorizontal = getDistanceRelativeToPlayer(vh), 
				distToWallVertical = getDistanceRelativeToPlayer(vv);
		
		if(distToWallHorizontal < distToWallVertical) 	
		{
			textureSlice = (int) (vh.getX() % 64);
			return vh;
		}
		textureSlice = (int) (vv.getY() % 64);
		return vv;
	}
	
	/**Get distance to vector relative to Player*/
	public double getDistanceRelativeToPlayer(Vector v)
	{
		double x = v.getX() - playerPos.getX();
		double y = v.getY() - playerPos.getY();
		return (int)Math.sqrt((x*x) + (y*y));
	}
	
	/**find Horizontal Line Intersection in the worldMap*/
	private Vector findHorizontalIntersection()
	{
		//If the ray is facing to the left or to the right parallel to the projection plane then there is no horizontal intersection
		if(angle == 0 || angle == 180) return null;
		
		double intersectY = (Math.floor(playerPos.getY() / 64.0) * 64.0) + (rayFacingUp ? -1 : 64);
		double intersectX = playerPos.getX() + (playerPos.getY() - intersectY) / angleTanConstant;
		
		int yInc =  rayFacingUp ? -64 : 64;
		double res = angleTanConstant;
		double xInc = (int)(64.0 / res) * ((rayFacingRight && res < 0) || (!rayFacingRight && res > 0)? -1 : 1);
		
		int hit = checkWall(intersectX, intersectY);
		
		while(hit <= 0)
		{
			if(hit < 0) return null;
			intersectX += xInc;
			intersectY += yInc;
			hit = checkWall(intersectX, intersectY);
		}
		return new Vector(intersectX, intersectY);
	}
	
	/**find Vertical Line Intersection in the worldMap*/
	private Vector findVerticalIntersection()
	{
		//If the ray is facing up or down perpendicular to the projection plane then there is no vertical intersection
		if(angle == 90 || angle == 270) return null;
				
		double intersectX = (Math.floor(playerPos.getX() / 64.0) * 64.0) + (rayFacingRight ? 64 : -1);
		double intersectY = playerPos.getY() + (playerPos.getX() - intersectX) * angleTanConstant;
	
		int xInc =  rayFacingRight ? 64 : -64;
		double res = angleTanConstant;
		double yInc = (64.0 * res) * ((rayFacingUp && res > 0) || (!rayFacingUp && res < 0)? -1 : 1);
		
		int hit = checkWall(intersectX, intersectY);
		
		while(hit <= 0)
		{
			if(hit < 0) return null;
			intersectX += xInc;
			intersectY += yInc;
			hit = checkWall(intersectX, intersectY);
		}
		return new Vector(intersectX, intersectY);
	}
	
	public static int checkWall(double x, double y)
	{
		if(x < 0 || y < 0)
			return -1;
		int currentX = (int)(x / 64), currentY = (int)(y / 64);
		if(currentX >= WorldMap.worldMap.length || currentX < 0 || currentY >= WorldMap.worldMap.length || currentY < 0)
			return -1;
		return WorldMap.worldMap[currentX][currentY];
	}
}
