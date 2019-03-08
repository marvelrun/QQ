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
			mt.waitForAll(); // ��ʼ�����ɴ�ý����������ٵ�����ͼ��
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		try {
			initialize(); // ������״��ʼ��
		} catch (IOException e) {
			e.printStackTrace();
		}
		// ����ť
		jmin = new JButton();
		jmin.setBounds(80, 405, 20, 20);
		jmin.setBorderPainted(false);
		jmin.setToolTipText("��С��");
		jmin.setOpaque(false);
		jmin.setContentAreaFilled(false);
		jmin.addActionListener(this);

		jclose = new JButton();
		jclose.setBounds(650, 400, 40, 40);
		jclose.setBorderPainted(false);
		jclose.setToolTipText("�ر�");
		jclose.setOpaque(false);	
		jclose.setContentAreaFilled(false);
		jclose.addActionListener(this);
		
		jstart = new JButton();
		jstart.setBounds(177, 630, 150, 150);
		jstart.setBorderPainted(false);
		jstart.setToolTipText("����������");
		jstart.setOpaque(false);
		jstart.setContentAreaFilled(false);
		jstart.addActionListener(this);
		
		jend = new JButton();
		jend.setBounds(473, 629, 150, 150);
		jend.setBorderPainted(false);
		jend.setToolTipText("��ֹ������");
		jend.setOpaque(false);
		jend.setContentAreaFilled(false);
		jend.addActionListener(this);
		// ���ô���ͼ��
				Image icon = new ImageIcon("image//mark.png").getImage();
				this.setIconImage(icon);
		this.add(jmin);
		this.add(jclose);
		this.add(jstart);
		this.add(jend);
		this.add(back);
		this.setVisible(true);
	}

	// �����ʼ��
	private void initialize() throws IOException {
		// �趨�����С��ͼƬһ����
		this.setSize(img.getWidth(null), img.getHeight(null));
		// �趨���ô���װ�Σ�������ȡ����Ĭ�ϵĴ���ṹ
		this.setUndecorated(true);
		// ����AWTUtilities��setWindowShape�����趨������Ϊ�ƶ���Shape��״
		AWTUtilities.setWindowShape(this, getImageShape(img));
		// �趨����ɼ���
		AWTUtilities.setWindowOpacity(this,0.8f);

		this.setLocationRelativeTo(null);
		this.setAlwaysOnTop(true);
		// ��������ƶ��¼�
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
	 * ��Imageͼ��ת��ΪShapeͼ��
	 * 
	 * @param img
	 * @return
	 */
	public Shape getImageShape(Image img) {
		ArrayList<Integer> x = new ArrayList<Integer>();
		ArrayList<Integer> y = new ArrayList<Integer>();
		int width = img.getWidth(null);// ͼ����
		int height = img.getHeight(null);// ͼ��߶�
		// ɸѡ����
		// ���Ȼ�ȡͼ�����е�������Ϣ
		PixelGrabber pgr = new PixelGrabber(img, 0, 0, -1, -1, true);
		try {
			pgr.grabPixels();
		} catch (InterruptedException ex) {
			ex.getStackTrace();
		}
		int pixels[] = (int[]) pgr.getPixels();

		// ѭ������
		for (int i = 0; i < pixels.length; i++) {
			// ɸѡ������͸�������ص�������뵽����ArrayList x��y��
			int alpha = getAlpha(pixels[i]);
			if (alpha == 0) {
				continue;
			} else {
				x.add(i % width > 0 ? i % width - 1 : 0);
				y.add(i % width == 0 ? (i == 0 ? 0 : i / width - 1) : i / width);
			}
		}

		// ����ͼ����󲢳�ʼ��(0Ϊ͸��,1Ϊ��͸��)
		int[][] matrix = new int[height][width];
		for (int i = 0; i < height; i++) {
			for (int j = 0; j < width; j++) {
				matrix[i][j] = 0;
			}
		}

		// ��������ArrayList�еĲ�͸��������Ϣ
		for (int c = 0; c < x.size(); c++) {
			matrix[y.get(c)][x.get(c)] = 1;
		}

		/*
		 * ����Area������ʾ������Խ��кϲ���������һˮƽ"ɨ��"ͼ������ÿһ�У�
		 * ����͸������������ΪRectangle���ٽ�ÿһ�е�Rectangleͨ��Area���rec ������кϲ�������γ�һ��������Shapeͼ��
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
	 * ȡ��͸����
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