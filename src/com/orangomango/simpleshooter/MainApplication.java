package com.orangomango.simpleshooter;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.layout.StackPane;
import javafx.scene.Scene;
import javafx.scene.canvas.*;
import javafx.scene.paint.Color;
import javafx.animation.*;
import javafx.util.Duration;
import javafx.scene.input.KeyCode;

import java.util.*;

public class MainApplication extends Application{
	private static final int HEIGHT = 600;
	private static final int WIDTH = 800;
	private static final double SPEED = 3;
	private Player player;
	private Map<KeyCode, Boolean> keys = new HashMap<>();
	public static List<Enemy> enemies = new ArrayList<>();
	
	public static void main(String[] args){
		launch(args);
	}
	
	public static void shedule(long time, Runnable r){
		new Thread(() -> {
			try {
				Thread.sleep(time);
				r.run();
			} catch (InterruptedException ex){
				ex.printStackTrace();
			}
		}).start();
	}
	
	@Override
	public void start(Stage stage){
		stage.setTitle("Simple shooter game");
		
		StackPane pane = new StackPane();
		Canvas canvas = new Canvas(WIDTH, HEIGHT);
		canvas.setFocusTraversable(true);
		GraphicsContext gc = canvas.getGraphicsContext2D();
		pane.getChildren().add(canvas);
		
		this.player = new Player(50, 50);
		
		Timeline loop = new Timeline(new KeyFrame(Duration.millis(1000.0/40), e -> update(gc)));
		loop.setCycleCount(Animation.INDEFINITE);
		loop.play();
		
		spawnEnemies();
		
		canvas.setOnKeyPressed(e -> this.keys.put(e.getCode(), true));
		canvas.setOnKeyReleased(e -> this.keys.put(e.getCode(), false));
		canvas.setOnMousePressed(e -> this.player.shoot(e.getX(), e.getY()));
		canvas.setOnMouseDragged(e -> this.player.shoot(e.getX(), e.getY()));
		
		Scene scene = new Scene(pane, WIDTH, HEIGHT);
		stage.setScene(scene);
		stage.show();
	}
	
	private void spawnEnemies(){
		Thread spawner = new Thread(() -> {
			try {
				Random random = new Random();
				while (true){
					double x = random.nextDouble()*WIDTH;
					double y = random.nextDouble()*HEIGHT;
					this.enemies.add(new Enemy(this.player, x, y));
					Thread.sleep(2000);
				}
			} catch (InterruptedException ex){
				ex.printStackTrace();
			}
		});
		spawner.setDaemon(true);
		spawner.start();
	}
	
	private void update(GraphicsContext gc){
		gc.clearRect(0, 0, WIDTH, HEIGHT);
		gc.setFill(Color.LIME);
		gc.fillRect(0, 0, WIDTH, HEIGHT);
		for (int i = 0; i < enemies.size(); i++){
			Enemy e = enemies.get(i);
			e.render(gc);
			for (int j = 0; j < Player.bullets.size(); j++){
				if (e.collided(Player.bullets.get(j).getX(), Player.bullets.get(j).getY(), Enemy.WIDTH, Bullet.WIDTH)){
					Player.bullets.remove(j);
					enemies.remove(i);
					i++;
					break;
				}
			}
		}
		this.player.render(gc);

		if (this.keys.getOrDefault(KeyCode.W, false)){
			this.player.move(0, -SPEED);
		}
		if (this.keys.getOrDefault(KeyCode.A, false)){
			this.player.move(-SPEED, 0);
		}
		if (this.keys.getOrDefault(KeyCode.S, false)){
			this.player.move(0, SPEED);
		}
		if (this.keys.getOrDefault(KeyCode.D, false)){
			this.player.move(SPEED, 0);
		}
		
		// Draw hp bar
		gc.setFill(Color.GREEN);
		gc.fillRect(50, HEIGHT-80, 100*(this.player.getHp()/100.0), 30);
		gc.setStroke(Color.BLACK);
		gc.strokeRect(50, HEIGHT-80, 100, 30);
	}
}
