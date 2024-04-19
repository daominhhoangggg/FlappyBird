package flappybird;

import pkg2dgamesframework.QueueList;

import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;

public class ChimneyGroup {
    private QueueList<Chimney> chimneys;    // Tạo hàng đợi ống khói
    private BufferedImage chimneyImage, chimneyImage2;  // Ảnh Ống khói xuôi, ngược

    public static int SIZE = 6;             // Kích thước của hàng đợi
    private int topChimneyY = -350;         // Tọa độ ống khói phía trên
    private int bottomChimneyY = 180;       // Tọa độ ống khói phía dưới

    public Chimney getChimney(int i) {
        return chimneys.get(i);
    }
    public int getRandomY() {
        Random random = new Random();
        int a;
        a = random.nextInt(10);
        // Thay đổi giá trị số để tăng/giảm khoảng các giữa các ống khói
        return a*30;
    }

    public ChimneyGroup() {     // Hàm hiển thị ống khói
        try {
            chimneyImage = ImageIO.read(new File("Assets/chimney.png"));
            chimneyImage2 = ImageIO.read(new File("Assets/chimney_.png"));
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        chimneys = new QueueList<>();

        Chimney cn;

        for(int i = 0; i < SIZE/2; i++) {
            // Lấy random
            int deltaY = getRandomY();

            // Nhập tọa độ kèm giá trị random
            cn = new Chimney(830+i*300, bottomChimneyY + deltaY, 74, 400);
            chimneys.push(cn);

            cn = new Chimney(830+i*300, topChimneyY + deltaY, 74, 400);
            chimneys.push(cn);

        }
    }

    public void resetChimney() {
        chimneys = new QueueList<>();

        Chimney cn;

        for(int i = 0; i < SIZE/2; i++) {

            int deltaY = getRandomY();

            cn = new Chimney(830+i*300, bottomChimneyY + deltaY, 74, 400);
            chimneys.push(cn);

            cn = new Chimney(830+i*300, topChimneyY + deltaY, 74, 400 );
            chimneys.push(cn);
        }
    }

    public void update() {
        for(int i = 0; i < SIZE; i++) {
            chimneys.get(i).update();
        }

        // Nếu ống khói đầu tiên trôi quá tọa độ -74 thì chuyển về cuối
        if(chimneys.get(0).getPosX() < -74) {

            int deltaY = getRandomY();

            // Chuyển ống khói phía dưới
            Chimney cn;
            cn = chimneys.pop();
            cn.setPosX(chimneys.get(4).getPosX() + 300);
            cn.setPosY(bottomChimneyY + deltaY);
            cn.setIsBehindBird(false);
            chimneys.push(cn);

            // Chuyển ống khói phía trên
            cn = chimneys.pop();
            cn.setPosX(chimneys.get(4).getPosX());
            cn.setPosY(topChimneyY + deltaY);
            cn.setIsBehindBird(false);
            chimneys.push(cn);
        }
    }

    public void paint(Graphics2D g2) {
        for(int i = 0; i < SIZE; i++) {
            if(i%2==0)
                g2.drawImage(chimneyImage,(int) chimneys.get(i).getPosX(),(int) chimneys.get(i).getPosY(), null);
            else
                g2.drawImage(chimneyImage2,(int) chimneys.get(i).getPosX(),(int) chimneys.get(i).getPosY(), null);
        }
    }
}
