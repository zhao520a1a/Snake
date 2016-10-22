/*
 * 第一版：先开发出一个窗口；
 * 步骤：1.设置窗口的属性；
 * 		 2.编写paint方法，画出网格；
 * 第二版：创建一个Snake类及它的内部类Node类
 * 步骤：1.先出创建出Snake类的基本属性和Node类，考虑它们的构造方法 ；
 * 		 2.再创建出一些Snake类应有的方法（draw(),addToHead(), deleteFromTail()）；
 * 第三版：让蛇移动起来；
 * 步骤：1.建立一个PaintThread线程，应用双缓冲来停止闪烁，重写updata()方法；
 *		 2.编写键盘的监听;
 *		 3.编写move方法，addToHead方法,deleteFromTail；
 * 第四版：创建一个egg类；
 * 步骤：1.属性和构造方法；
 * 		2.draw()方法；
 *		3.在Snake类中编写eat方法（要调用双方的getRect()方法,判断碰撞）；
 * 		4.遍写reappear()方法，让egg吃掉后又重新出现；
 *第五版：①让蛇出界死掉，撞到自己的尾巴也死掉；
 * 步骤：1.在Snake类中编写cheakDead()方法；	
 *        ②定义一个记分机制；每吃一个egg涨5分；打印游戏结束信息
 * 步骤: 1.创建一个Score属性；
 * 		 2.在paint方法中打印出信息；
 * 		 3.在eat中添加分数的计算；
 *       4.打印“Game Over”信息，用g.setFont(---)方法设置字体；
 *			③按F2重新开始游戏；
 *步骤：1.监听键盘F2键；
 *		2.编写restart()方法；
 *
 */	       

import java.awt.*;
import java.awt.event.*;

/**
 *这个类代表贪吃蛇的活动场所
 * @author 小鑫哦
 *
 */
public class Yard extends Frame {
	PaintThread pt = new PaintThread();
	
	/**
	 * 表示行数；
	 */
	public static int ROWS = 30;
	/**
	 * 表示列数；
	 */
 	public static int COLS = 30;
 	/**
	 * 表示小格的大小长度；
	 */
 	public static int BLOCK_SIZE = 15;
 	
 	
 	/**
 	 * 相当于一张“虚拟图片”，为了实现双缓冲；
 	 */
 	Image offScreenImage = null;
 	
 	Snake s = new Snake(this);
 	Egg e = new Egg(15, 15);
 	
 	/**
 	 * 这是一个判断游戏是否结束的量；
 	 */
 	private boolean gameOver = false;
 	/**
 	 * 这是一个记录游戏分数的量；
 	 */
 	private int score = 0;
 	
 	//创建游戏窗口；
	void lanuch() {
		this.setLocation(200,100);
		this.setSize(COLS * BLOCK_SIZE, ROWS * BLOCK_SIZE);
		this.setTitle("贪食蛇");
		this.setBackground(Color.BLACK);
		this.setVisible(true);
		this.setResizable(false);
		this.addWindowListener( new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		this.addKeyListener( new KeyMonitor());
		
		new Thread(pt).start();//开启线程；

	}
		
	
	public void paint (Graphics g) {
		Color c = g.getColor();
		g.fillRect(0, 0, COLS * BLOCK_SIZE, ROWS * BLOCK_SIZE);
		g.setColor(Color.BLUE);
		//画横线；
		for (int i = 1; i < COLS; i++) {
			g.drawLine(0, i * BLOCK_SIZE, ROWS * BLOCK_SIZE, i * BLOCK_SIZE);
		}
		//画竖线；
		for (int i = 1; i < ROWS; i++) {
			g.drawLine(i * BLOCK_SIZE, 0,  i * BLOCK_SIZE, ROWS * BLOCK_SIZE);
		}
		
		if(s.eat(e)){
			e.reappear();
		}
		s.draw(g);
		e.draw(g);
		
		//打印分数；
		g.setColor(Color.PINK);
		g.drawString("score:" + score, 10, 50 );
		
		g.setColor(Color.WHITE);
		if(gameOver) {
			//设置字体，宋体加粗50号；
			g.setFont(new Font("宋体", Font.BOLD, 50));
			g.drawString("GAME OVER!", 5 * BLOCK_SIZE, 13 * BLOCK_SIZE);
		}
		g.setColor(c);
		
	}
	
	/**
	 * 用双缓冲解决闪烁问题；
	 */
	public void update(Graphics g) {
		if(offScreenImage == null){
			offScreenImage = this.createImage(ROWS * BLOCK_SIZE, COLS * BLOCK_SIZE);
		}
		Graphics gOffScreen = offScreenImage.getGraphics();
		//将所有的东西画在“虚拟图片”上
		paint(gOffScreen);
		//再将“虚拟图片”一下子放在屏幕上
		g.drawImage(offScreenImage, 0, 0, null);
	}

	
	private class PaintThread implements Runnable {
		public void run() {
			while(!gameOver) {
				repaint();
				try {
					Thread.sleep(100);  
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
		
		public void reStart() {
			s = new Snake(Yard.this);//new出一条新的蛇
			gameOver = false;
			new Thread (pt).start();//重新开启线程；
			System.out.println("restar!");
		}
	}
	
	
	private class KeyMonitor extends KeyAdapter {

		public void keyPressed(KeyEvent e) {
			int k = e.getKeyCode();
			if(k == KeyEvent.VK_F2) { 
				//只有游戏结束才能重新开始；
				if(gameOver){
					pt.reStart();
				}
			}
			s.keyPressed(e);
		}
	}

	
	public boolean isGameOver() {
		return gameOver;
	}

	
	public void setGameOver(boolean gameOver) {
		this.gameOver = gameOver;
	}

	/**
	 * 拿到所得的分数
	 * @return 分数
	 */
	public int getScore() {
		return score;
	}

	/**
	 * 设置所得的分数
	 * @param score 分数
	 */
	public void setScore(int score) {
		this.score = score;
	}

	
	public static void main(String[] args) {
		new Yard().lanuch();
	}
	
}
