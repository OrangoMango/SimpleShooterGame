package com.orangomango.simpleshooter;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.util.*;

public class Player{
	private double x, y;
	public static List<Bullet> bullets = new ArrayList<>();
	private static final double WIDTH = 50;
	private boolean shooting = false, damage = false;
	private int hp = 100;
	
	public Player(double x, double y){
		this.x = x;
		this.y = y;
	}
	
	public double getX(){
		return this.x;
	}
	
	public double getY(){
		return this.y;
	}
	
	public int getHp(){
		return this.hp;
	}
	
	public void takeDamage(int dmg){
		if (damage) return;
		this.hp -= dmg;
		damage = true;
		MainApplication.shedule(150, () -> damage = false);
	}
	
	public void render(GraphicsContext gc){
		gc.setFill(Color.RED);
		gc.fillOval(this.x, this.y, WIDTH, WIDTH);
		for (int i = 0; i < this.bullets.size(); i++){
			this.bullets.get(i).render(gc);
		}
	}
	
	public void move(double x, double y){
		this.x += x;
		this.y += y;
	}
	
	public void shoot(double x, double y){
		if (shooting) return;
		shooting = true;
		MainApplication.shedule(150, () -> this.shooting = false);
		double angle = Math.atan2(y-this.y, x-this.x); // Radians
		Bullet b = new Bullet(angle, this.x+WIDTH/2, this.y+WIDTH/2);
		this.bullets.add(b);
	}
}
