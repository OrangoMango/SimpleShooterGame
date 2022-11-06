package com.orangomango.simpleshooter;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Enemy{
	private double x, y;
	private Player player;
	public static final double WIDTH = 40;
	private static final double SPEED = 2;
	
	public Enemy(Player p, double x, double y){
		this.player = p;
		this.x = x;
		this.y = y;
	}
	
	private boolean checkCollision(){
		for (int i = 0; i < MainApplication.enemies.size(); i++){
			Enemy e = MainApplication.enemies.get(i);
			if (e != this){
				if (e.collided(this.x, this.y, WIDTH, WIDTH)){
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean collided(double x, double y, double w1, double w2){
		// Check if the distance between the center of the the 2 enemies is less than the total width (diameter)
		return Math.sqrt(Math.pow(this.x+w1/2-x-w2/2, 2)+Math.pow(this.y+w1/2-y-w2/2, 2)) <= w1/2+w2/2;
	}
	
	public void render(GraphicsContext gc){
		gc.setFill(Color.BLUE);
		gc.fillOval(this.x, this.y, WIDTH, WIDTH);
		double distance = Math.sqrt(Math.pow(this.x-this.player.getX(), 2)+Math.pow(this.y-this.player.getY(), 2));
		if (distance <= 60){
			// Damage to player
			this.player.takeDamage(5);
		} else {
			double angle = Math.atan2(this.player.getY()-this.y, this.player.getX()-this.x);
			this.x += Math.cos(angle)*SPEED;
			if (checkCollision()){
				this.x -= Math.cos(angle)*SPEED;
			}
			this.y += Math.sin(angle)*SPEED;
			if (checkCollision()){
				this.y -= Math.sin(angle)*SPEED;
			}
		}
	}
}
