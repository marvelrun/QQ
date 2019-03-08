package server;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Area;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.*;
import com.sun.awt.AWTUtilities;

public class ServerUI extends JFrame implements ActionListener {
	Image img;
	int xx, yy;
	boolean isDragging;
	JButton jmin, jclose, jstart, jend;

	public void initServerUI() {
		ImageIcon background = new ImageIcon("image\\mark.png");
		JLabel back = new JLabel(background);
		back.setBounds(0, 0, background.getIconWidth(), background.getIconHeight());
		MediaTracker mt = new MediaTracker(this);
		try {
			img = ImageIO.read(new File("image\\mark.png"));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		mt.addImage(img, 0);
		try {
			mt.waitForAll(); // 开始加载由此媒体跟踪器跟踪的所有图像
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		try {
			initialize(); // 窗体形状初始化
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 各按钮
		jmin = new JButton();
		jmin.setBounds(80, 405, 20, 20);
		jmin.setBorderPainted(false);
		jmin.setToolTipText("最小化");
		jmin.setOpaque(false);
		jmin.setContentAreaFilled(false);
		jmin.addActionListener(this);

		jclose = new JButton();
		jclose.setBounds(650, 400, 40, 40);
		jclose.setBorderPainted(false);
		jclose.setToolTipText("关闭");
		jclose.setOpaque(false);	
		jclose.setContentAreaFilled(false);
		jclose.addActionListener(this);
		
		jstart = new JButton();
		jstart.setBounds(177, 630, 150, 150);
		jstart.setBorderPainted(false);
		jstart.setToolTipText("启动服务器");
		jstart.setOpaque(false);
		jstart.setContentAreaFilled(false);
		jstart.addActionListener(this);
		
		jend = new JButton();
		jend.setBounds(473, 629, 150, 150);
		jend.setBorderPainted(false);
		jend.setToolTipText("终止服务器");
		jend.setOpaque(false);
		jend.setContentAreaFilled(false);
		jend.addActionListener(this);
		// 设置窗体图标
				Image icon = new ImageIcon("image//mark.png").getImage();
				this.setIconImage(icon);
		this.add(jmin);
		this.add(jclose);
		this.add(jstart);
		this.add(jend);
		this.add(back);
		this.setVisible(true);
	}

	// 窗体初始化
	private void initialize() throws IOException {
		// 设定窗体大小和图片一样大
		this.setSize(img.getWidth(null), img.getHeight(null));
		// 设定禁用窗体装饰，这样就取消了默认的窗体结构
		this.setUndecorated(true);
		// 调用AWTUtilities的setWindowShape方法设定本窗体为制定的Shape形状
		AWTUtilities.setWindowShape(this, getImageShape(img));
		// 设定窗体可见度
		AWTUtilities.setWindowOpacity(this,0.8f);

		this.setLocationRelativeTo(null);
		this.setAlwaysOnTop(true);
		// 监听鼠标移动事件
		this.addMouseMotionListener(new MouseAdapter() {
			public void mouseDragged(MouseEvent e) {
				if (isDragging == true) {
					int left = getLocation().x;
					int top = getLocation().y;
					setLocation(left + e.getX() - xx, top + e.getY() - yy);
				}
			}
		});
		this.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				isDragging = true;
				xx = e.getX();
				yy = e.getY();
			}

			public void mouseReleased(MouseEvent e) {
				isDragging = false;
			}
		});
	}

	/**
	 * 将Image图像转换为Shape图形
	 * 
	 * @param img
	 * @return
	 */
	public Shape getImageShape(Image img) {
		ArrayList<Integer> x = new ArrayList<Integer>();
		ArrayList<Integer> y = new ArrayList<Integer>();
		int width = img.getWidth(null);// 图像宽度
		int height = img.getHeight(null);// 图像高度
		// 筛选像素
		// 首先获取图像所有的像素信息
		PixelGrabber pgr = new PixelGrabber(img, 0, 0, -1, -1, true);
		try {
			pgr.grabPixels();
		} catch (InterruptedException ex) {
			ex.getStackTrace();
		}
		int pixels[] = (int[]) pgr.getPixels();

		// 循环像素
		for (int i = 0; i < pixels.length; i++) {
			// 筛选，将不透明的像素的坐标加入到坐标ArrayList x和y中
			int alpha = getAlpha(pixels[i]);
			if (alpha == 0) {
				continue;
			} else {
				x.add(i % width > 0 ? i % width - 1 : 0);
				y.add(i % width == 0 ? (i == 0 ? 0 : i / width - 1) : i / width);
			}
		}

		// 建立图像矩阵并初始化(0为透明,1为不透明)
		int[][] matrix = new int[height][width];
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				matrix[i][j] = 0;
			}
		}

		// 导入坐标ArrayList中的不透明坐标信息
		for (int c = 0; c < x.size(); c++) {
			matrix[y.get(c)][x.get(c)] = 1;
		}

		/*
		 * 由于Area类所表示区域可以进行合并，我们逐一水平"扫描"图像矩阵的每一行，
		 * 将不透明的像素生成为Rectangle，再将每一行的Rectangle通过Area类的rec 对象进行合并，最后形成一个完整的Shape图形
		 */
		Area rec = new Area();
		int temp = 0;

		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				if (matrix[i][j] == 1) {
					if (temp == 0)
						temp = j;
					else if (j == width) {
						if (temp == 0) {
							Rectangle rectemp = new Rectangle(j, i, 1, 1);
							rec.add(new Area(rectemp));
						} else {
							Rectangle rectemp = new Rectangle(temp, i, j - temp, 1);
							rec.add(new Area(rectemp));
							temp = 0;
						}
					}
				} else {
					if (temp != 0) {
						Rectangle rectemp = new Rectangle(temp, i, j - temp, 1);
						rec.add(new Area(rectemp));
						temp = 0;
					}
				}
			}
			temp = 0;
		}
		return rec;
	}

	/**
	 * 取得透明度
	 * 
	 * @param pixel
	 * @return
	 */
	private int getAlpha(int pixel) {
		return (pixel >> 24) & 0xff;
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == jmin) {
			this.setExtendedState(Frame.ICONIFIED);
		} else if (e.getSource() == jclose) {
			System.exit(0);
		} else if (e.getSource() == jstart) {
			new MyServer().setServer(8765);
			new SignServer().setServer(8766);
		} else if (e.getSource() == jend) {
		}
	}
}