
import java.awt.*;
import java.util.Random;

/**
 * 代表蛋
 * @author 小鑫哦
 *
 */

public class Egg {
	int w = Yard.BLOCK_SIZE;//蛋的宽度；
	int h = Yard.BLOCK_SIZE;//蛋的高度；
	int row;//所在行数；
	int col;//所在列数；
	Color color = Color.RED;//蛋的颜色；
	
	private static Random r = new Random();//随机数生成器；
	
	public Egg(int row, int col) {
		this.row = row;
		this.col = col;
	}
	
	//为了不让蛋出现在窗口条中，故行数的初始化值一定要大于等于3；
	public Egg() {
		this(r.nextInt(Yard.ROWS - 3) + 3, r.nextInt(Yard.COLS) );
	}

	/**
	 * 画出蛋；
	 * @param g
	 */
	public void draw (Graphics g) {
		Color c = g.getColor();
		g.setColor(color);
		g.fillOval(col * w, row * h, w, h);
		//让蛋的颜色红绿色变换；
		if(color == Color.RED) {
			color = Color.GREEN;
		}
		else{
			color = Color.RED;
		}
		g.setColor(c);
		
	}
	
	/**
	 * 此方法为了此后进行蛇和蛋的碰撞判断，因此先得到蛋所占的位置和面积；
	 * @return 蛋所占的位置和面积；
	 */
	public Rectangle getRect() {
		return new Rectangle(col * w, row * h, w, h);
	}
	
	/**
	 * 此方法是为了当蛇吃到蛋后，让蛋随机在一个地方再现；
	 */
	public void reappear() {
		this.row = r.nextInt(Yard.ROWS - 3) + 3;
		this.col = r.nextInt(Yard.COLS);
	}


	
}
