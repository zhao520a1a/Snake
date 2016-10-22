import java.awt.*;
import java.awt.event.*;

 /**
 *代表蛇
 * @author 小鑫哦
 *
 */
public class Snake {
	/**
	 * 表示蛇头；
	 */
	Node head = null;
	/**
	 * 表示蛇尾；
	 */
	Node tail = null;
	/**
	 * 蛇的节数；
	 */
	int size = 0;

	Yard y ;
	Node n = new Node(20, 20, Direction.L);
	
	public Snake(Yard y) {
		this.y = y;
		head = n;
		tail = n;
		size = 1;
	}
	
	
	class Node {
		private  int w = Yard.BLOCK_SIZE;//小格宽度；
		private  int h = Yard.BLOCK_SIZE;//小格长度；
		public int row;//所在行；
		public int col;//所在列；
		Node prev = null;//上一个小格；
		Node next = null;//下一个小格；
		Direction dir;//小格的运动方向；
		
		public Node(int ROWS, int COLS, Direction dir) {
			this.row = ROWS;
			this.col = COLS;
			this.dir = dir;
		}

		/**
		 * 画出小格；
		 * @param g
		 */
		void draw (Graphics g) {
			Color c = g.getColor();
			g.setColor(Color.ORANGE);
			g.fillRect(col * w, row * h , w, h);
			g.setColor(c);
		}
		
	}
	
	/**
	 * 画出蛇；
	 * @param g
	 */
	void draw (Graphics g) {
		if(size <= 0 )  return ;
		move();
		for (Node n=head; n!=null; n=n.next) {
			n.draw(g);
		}
	}
	
	/**
	 * 键盘监听，从而改变蛇头的方向；
	 * @param e
	 */
	public void keyPressed(KeyEvent e) {
		int k = e.getKeyCode();
		switch (k) {
		case KeyEvent.VK_LEFT:
			if(head.dir != Direction.R)
				head.dir = Direction.L;
			break;
		case KeyEvent.VK_UP:
			if(head.dir != Direction.D)
				head.dir = Direction.U;
			break;
		case KeyEvent.VK_RIGHT:
			if(head.dir != Direction.L)
				head.dir = Direction.R;
			break;
		case KeyEvent.VK_DOWN:
			if(head.dir != Direction.U)
				head.dir = Direction.D;
			break;
		}
		
	}

	/**
	 * 让蛇移动起来；
	 */
	void move() { 
		addToHead();
		deleteFromTail();
		cheakDead();
	}
	
	/**
	 *这个方法的作用是在蛇头加一格；
	 */
	void addToHead() {
		Node nn = null;
		switch (head.dir) {
		case L:
			nn = new Node(head.row, head.col - 1, Direction.L);
			break;
		case U:
			nn = new Node(head.row - 1, head.col, Direction.U);
			break;
		case R:
			nn = new Node(head.row, head.col + 1, Direction.R);
			break;
		case D:
			nn = new Node(head.row + 1, head.col, Direction.D);
			break;
		}
		nn.next = head;
		head.prev = nn;
		head = nn;
		size ++;
	}
	
	/**
	 * 这个方法的作用是在蛇尾减一格；
	 */
	void deleteFromTail() {
		tail = tail.prev;
		tail.next = null;
		size--;
	}
	
	/**
	 * 表示蛇吞蛋这个行为；
	 */
	public boolean eat(Egg e) {
		if(this.getRect().intersects(e.getRect())){
			addToTail();
			y.setScore(y.getScore() + 5);
			return true;
		}
		return false;
		
	}
	
	/**
	 * 此方法为了此后进行蛇和蛋的碰撞判断，因此先得到蛇所占的位置和面积；
	 * @return 蛇所占的位置和面积；
	 */
	private Rectangle getRect(){
		return new Rectangle(head.col * head.w, head.row * head.h, head.w, head.h);
	}
	
	/**
	 * 此方法用于当蛇吃到蛋后，在蛇尾加一格；
	 */
	public void addToTail() {
		Node nn = null;
		switch(tail.dir){
		case L:
			nn = new Node(tail.col + 1, tail.row, Direction.L);
			break;
		case U:
			nn = new Node(tail.col, tail.row + 1, Direction.U);
			break;
		case R:
			nn = new Node(tail.col - 1, tail.row, Direction.R);
			break;
		case D:
			nn = new Node(tail.col, tail.row - 1, Direction.D);
			break;
		}
		nn.prev = tail;
		tail.next = nn;
		tail = nn;
		size++;
	}
	
	/**
	 * 判断蛇是否死亡，即：蛇运动是否出界或撞到自己的身体；
	 */
	private boolean cheakDead() {
		//检查蛇运动是否出界；
		if(head.col < 0 || head.row <2 || head.col > Yard.COLS || head.row > Yard.COLS) {
			y.setGameOver(true);
			return true;
		}
		//检查蛇是否撞到自己的身体；
		for(Node n=head.next; n!=null; n=n.next) {
			if(head.col == n.col && head.row == n.row){		
				y.setGameOver(true);
			    return true;
			}
		}
		return false;
	}
	
}
