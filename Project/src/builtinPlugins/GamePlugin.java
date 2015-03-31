package builtinPlugins;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JPanel;

import export.Message;
import export.MessageHandler;
import export.Plugin;

public class GamePlugin implements Plugin {
	public static final String NAME = "Game Plugin";
	public static final int ENEMY_COUNT = 5;
	protected static final long REPAINT_INTERVAL_MS = 30;
	protected static final long ENEMY_SLEEP_INTERVAL = 50;
	public static enum Direction {NORTH, SOUTH, EAST, WEST};
	
	private List<Enemy> enemies;
	private Hero hero;
	private GameComponent game;
	private MultiKeyPressListener listener;
	
	private MessageHandler handler;

	@Override
	public void Load(JPanel renderPanel, MessageHandler messageHandler) {
		renderPanel.setFocusable(true);
		game = new GameComponent();
		game.setPreferredSize(renderPanel.getPreferredSize());
		listener = new MultiKeyPressListener();
		game.setFocusable(true);
		game.requestFocus();
		game.addKeyListener(listener);
		this.handler = messageHandler;
		
		hero = new Hero(renderPanel);
		enemies = new LinkedList<Enemy>();
		for (int i=0; i<ENEMY_COUNT; i++) {
			enemies.add(new Enemy(renderPanel));
		}
		
		
		renderPanel.add(game);
		renderPanel.revalidate();
		renderPanel.repaint();
		
	}

	@Override
	public void Run() {
		game.requestFocus();
		game.requestFocusInWindow();
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					while(true) {
						for (Enemy e: enemies) {
							e.move(hero);
							e.kill(hero);
						}
						Thread.sleep(ENEMY_SLEEP_INTERVAL);
					}
				}
				catch (InterruptedException e) {
					//just let it die.
				}
			}
			
		}).start();
		
		new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					while(true) {
						LinkedList<Direction> dirs = new LinkedList<>();
						for (int c : listener.pressed) {
							if (c == KeyEvent.VK_UP) {
								dirs.add(Direction.NORTH);
							} else if (c == KeyEvent.VK_DOWN) {
								dirs.add(Direction.SOUTH);
							} else if (c == KeyEvent.VK_LEFT) {
								dirs.add(Direction.WEST);
							} else if (c == KeyEvent.VK_RIGHT) {
								dirs.add(Direction.EAST);
							}
						}
						hero.move(dirs);
						Thread.sleep(ENEMY_SLEEP_INTERVAL/2);
					}
				}
				catch (InterruptedException e) {
					//just let it die.
				}
			}
			
		}).start();
		
	}

	@Override
	public void Stop() {
		handler.sendMessage(new Message(NAME,"Letting the GC handle removing all the stuff."));
	}

	@Override
	public String getIdentifier() {
		return NAME;
	}
	
	private abstract class Drawable {
		abstract void drawOn(Graphics2D g);
	}
	
	private class Hero extends Drawable {
		private int x;
		private int y;
		private boolean dead;
		private int radius = 5;
		
		public Hero(JComponent drawOn) {
			dead = false;
			x = drawOn.getWidth()/2;
			y = drawOn.getHeight()/2;
		}
		
		public void move(List<Direction> directions) {
			if (!dead) {
				for (Direction d : directions) {
					switch(d) {
					case EAST:
						x++;
						break;
					case NORTH:
						y--;
						break;
					case SOUTH:
						y++;
						break;
					case WEST:
						x--;
						break;
					default:
						break;
					}
				}
			}
		}

		@Override
		void drawOn(Graphics2D g) {
			g.setColor(Color.GREEN);
			g.fillOval(x-radius, y-radius, radius*2, radius*2);
		}
	}
	
	private class Enemy extends Drawable {
		private int x;
		private int y;
		private int radius = 10;
		
		public Enemy(JComponent drawOn) {
			Random r = new Random();
			x = r.nextInt(drawOn.getWidth());
			y = r.nextInt(drawOn.getHeight());
		}

		@Override
		void drawOn(Graphics2D g) {
			g.setColor(Color.RED);
			g.fillOval(x-radius, y-radius, radius*2, radius*2);
		}
		
		public void move(Hero h) {
			if (h.dead) {
				return;
			}
			if (h.x > x) {
				x++;
			} else if (h.x < x) {
				x--;
			}
			
			if (h.y > y) {
				y++;
			} else if (h.y < y) {
				y--;
			}
		}
		
		public void kill(Hero h) {
			if (h.dead) {
				return;
			}
			if (h.x == this.x && h.y == this.y) {
				h.dead = true;
				handler.sendMessage(new Message(NAME, "GAME OVER"));
			}
		}
		
	}
	
	private class GameComponent extends JComponent {

		/**
		 * 
		 */
		private static final long serialVersionUID = 8414617867016979386L;

		public GameComponent() {
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

			for (Enemy e : enemies) {
				e.drawOn(g2);
			}
			hero.drawOn(g2);
		}
	}
	
	class MultiKeyPressListener implements KeyListener {

	    // Set of currently pressed keys
	    public final Set<Integer> pressed = new HashSet<>();

	    @Override
	    public synchronized void keyPressed(KeyEvent e) {
	        pressed.add(e.getKeyCode());
	    }

	    @Override
	    public synchronized void keyReleased(KeyEvent e) {
	        pressed.remove(e.getKeyCode());
	    }

	    @Override
	    public void keyTyped(KeyEvent e) {/* Not used */ }
	}

}
