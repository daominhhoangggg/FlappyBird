package flappybird;

import pkg2dgamesframework.AFrameOnImage;
import pkg2dgamesframework.Animation;
import pkg2dgamesframework.GameScreen;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class FlappyBird extends GameScreen {
    private BufferedImage birds;    // Biến lấy ảnh
    private Animation bird_anim;    // Biến xử lý hiệu ứng chuyển động

    public static float g = 0.15f;     // Biến gia tốc trọng trường

    // Tạo đối tượng từ các lớp
    private Bird bird;
    private Ground ground;
    private ChimneyGroup chimneyGroup;

    private int Point = 0;      // Điểm số
    private int Highscore = 0;  // Điểm cao

    // Các Màn Hình Game
    private int BEGIN_SCREEN = 0;
    private int GAMEPLAY_SCREEN = 1;
    private int GAMEOVER_SCREEN = 2;

    private int CurrentScreen = BEGIN_SCREEN;   // Khởi tạo Màn hình hiện tại

    public FlappyBird() {
        super(800,600);

        try {
            birds = ImageIO.read(new File("Assets/bird_sprite.png"));   // Đọc file ảnh
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        // Tạo các chuyển động của Bird
        bird_anim = new Animation(70);
        AFrameOnImage f;

        f = new AFrameOnImage(0, 0, 60, 60);
        bird_anim.AddFrame(f);
        f = new AFrameOnImage(60, 0,60,60);
        bird_anim.AddFrame(f);
        f = new AFrameOnImage(120,0,60,60);
        bird_anim.AddFrame(f);
        f = new AFrameOnImage(60, 0,60,60);
        bird_anim.AddFrame(f);

        bird = new Bird(350, 250, 50, 50);  // Khởi tạo Bird
        ground = new Ground();                          // Khởi tạo Mặt Đất
        chimneyGroup = new ChimneyGroup();              // Khởi tạo Ống Khói

        BeginGame();    // Khởi tạo Trò Chơi
    }

    public static void main(String[] args) {
        new FlappyBird();
    }   // Hàm Main

    private void resetGame() {
        bird.setPos(350, 250);
        bird.setVt(0);
        bird.setLive(true);
        Point = 0;
        chimneyGroup.resetChimney();
    }

    @Override
    public void GAME_UPDATE(long deltaTime) {
        // Phương thức này sẽ xử lý các tương tác trong Game
        if(CurrentScreen == BEGIN_SCREEN) {
            if(Point > Highscore) Highscore = Point;    // Cập nhật điểm cao
            resetGame();
        } else if(CurrentScreen == GAMEPLAY_SCREEN) {
            // Xử lý khi Đang trong trò chơi
            if(bird.getLive()) bird_anim.Update_Me(deltaTime);

            bird.update(deltaTime);
            ground.Update();
            chimneyGroup.update();

            // Xử lý va chạm ống khói
            for (int i = 0; i < ChimneyGroup.SIZE; i++) {
                if(bird.getRect().intersects(chimneyGroup.getChimney(i).getRect())) {
                    if(bird.getLive()) bird.fallSound.play();
                    bird.setLive(false);
                }
            }

            // Xử lý tính điểm khi bay qua một cặp ống khói
            for (int i = 0; i < ChimneyGroup.SIZE; i++) {
                if(bird.getPosX() > chimneyGroup.getChimney(i).getPosX() && !chimneyGroup.getChimney(i).getIsBehindBird() &&
                        i%2 == 0 && bird.getLive()) {
                    Point++;
                    bird.getPointSound.play();
                    chimneyGroup.getChimney(i).setIsBehindBird(true);
                }
            }

            // Xử lý chạm đất
            if(bird.getPosY() + bird.getH() > ground.getYGround()) {
                if(bird.getLive()) bird.fallSound.play();
                CurrentScreen = GAMEOVER_SCREEN;
            }
        }
    }


    @Override
    public void GAME_PAINT(Graphics2D g2) {
        // Phương thức này để hiển các đối tượng trong game
        Font bold = new Font("Verdana", Font.BOLD, 20);
        Font plain = new Font("Verdana", Font.PLAIN, 20);

        g2.setColor(Color.decode("#b8daef"));
        g2.fillRect(0, 0, MASTER_WIDTH, MASTER_HEIGHT);

        chimneyGroup.paint(g2);     // Hiển thị ống khói
        ground.Paint(g2);           // Hiển thị Mặt Đất

        // Hiển thị Bird
        if(bird.getIsFlying())
            bird_anim.PaintAnims((int) bird.getPosX(), (int) bird.getPosY(), birds, g2, 0, -1);
        else
            bird_anim.PaintAnims((int) bird.getPosX(), (int) bird.getPosY(), birds, g2, 0, 0 );

        // Giao diện các màn hình game
        g2.setFont(plain);
        if(CurrentScreen == GAMEOVER_SCREEN) {
            g2.setColor(Color.red);
            g2.drawString("Press space to retry", 250, 340);
        }

        if(CurrentScreen == BEGIN_SCREEN) {
            g2.setColor(Color.red);
            g2.drawString("Press space to play game", 250, 340);

            g2.setFont(bold);
            g2.drawString("Highscore: " + Highscore, 20, 80);
        }

        // Hiển thị điểm số
        g2.setColor(Color.red);
        g2.setFont(bold);
        g2.drawString("Point: " + Point, 20, 50);
    }

    @Override
    public void KEY_ACTION(KeyEvent e, int Event) {
        // Phương thức này dùng để xử lý sự kiên nhấn phím
        if(Event == KEY_PRESSED) {
            if(CurrentScreen == BEGIN_SCREEN) {
                CurrentScreen = GAMEPLAY_SCREEN;
            } else if(CurrentScreen == GAMEPLAY_SCREEN) {
                if(bird.getLive()) bird.fly();
            } else {
                CurrentScreen = BEGIN_SCREEN;
            }
        }
    }
}
