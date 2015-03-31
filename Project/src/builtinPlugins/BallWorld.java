package builtinPlugins;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JComponent;
import javax.swing.JPanel;

import export.Message;
import export.MessageHandler;
import export.Plugin;


public class BallWorld implements Plugin, Runnable {
	public static final int MAX_BALLS = 10;
	public static final int MAX_RADIUS = 50;
	public static final int REPAINT_INTERVAL_MS = 30;
	public static final String NAME = "Bouncing Ball Plugin - Not Dynamically Loaded";
	
	private MessageHandler handler;
	private JPanel renderingPanel;
	private BallWorldComponent drawingCanvas;
	
	private ArrayList<Ball> balls;
	private Thread running;

	@Override
	public void Load(JPanel renderPanel, MessageHandler messageHandler) {
		this.handler = messageHandler;
		this.renderingPanel = renderPanel;
		this.drawingCanvas = new BallWorldComponent();
		this.drawingCanvas.setPreferredSize(renderingPanel.getPreferredSize());
		renderingPanel.add(drawingCanvas);
		renderingPanel.revalidate();
		renderingPanel.repaint();
		balls = new ArrayList<>();
	}

	@Override
	public void Run() {
		Random rand = new Random();
		int numBalls = rand.nextInt(MAX_BALLS) + 1;
		for (int i=0; i<numBalls; i++) {
			float r = rand.nextFloat();
			float g = rand.nextFloat();
			float b = rand.nextFloat();
			Color randomColor = new Color(r, g, b);
			int radius = rand.nextInt(MAX_RADIUS);
			Ball newBall = new Ball(50, renderingPanel.getWidth(), 50, renderingPanel.getY() + renderingPanel.getHeight(),radius, randomColor);
			balls.add(newBall);
		}
		running = new Thread(this);
		running.start();
	}
	
	@Override
	public void run() {
			try {
				while (true) {
					Thread.sleep(REPAINT_INTERVAL_MS/2);
					for (Ball b: balls) {
						b.update(drawingCanvas.getWidth(), drawingCanvas.getHeight());
					}
				}
				
			} catch (InterruptedException e) {
				//stop when interrupted.
			}	
	}

	@Override
	public void Stop() {
		this.running.interrupt();
		handler.sendMessage(new Message(NAME, "Exiting. Bye!"));
		this.renderingPanel.removeAll();
	}

	@Override
	public String getIdentifier() {
		return NAME;
	}
	
	private class Ball {
		int x;
		int y;
		int radius;
		Color fill;
		int dx;
		int dy;
		
		public Ball(int minX, int maxX, int minY, int maxY, int radius, Color color) {
			Random r = new Random();
			x = r.nextInt(maxX-minX) + minX;
			y = r.nextInt(maxY - minY) + minY;
			fill = color;
			this.radius = radius;
			dx = r.nextBoolean() ? -1 : 1;
			dy = r.nextBoolean() ? -1 : 1;
		}
		
		public void update(int maxX, int maxY) {
			x += dx;
			y += dy;
			if (y >= maxY || y <= 0) {
				dy *= -1;
			}
			if (x >= maxX || x <= 0) {
				dx *= -1;
			}
		}
		
		public void drawOn(Graphics2D g) {
			g.setColor(this.fill);
			g.fillOval(x-radius, y-radius, radius*2, radius*2);;
		}
	}
	
	private class BallWorldComponent extends JComponent {
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 7693697806914214369L;

		public BallWorldComponent() {
			Runnable repainter = new Runnable() {
				@Override
				public void run() {
					// Periodically asks Java to repaint this component
					try {
						while (true) {
							Thread.sleep(REPAINT_INTERVAL_MS);
							repaint();
						}
					} catch (InterruptedException exception) {
						// Stop when interrupted
					}
				}
			};
			new Thread(repainter).start();
		}
		
		@Override
		protected void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;

			for (Ball d : balls) {
				d.drawOn(g2);
			}
		}
	}

}
