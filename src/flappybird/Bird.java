package flappybird;

import pkg2dgamesframework.Objects;
import pkg2dgamesframework.SoundPlayer;

import java.awt.*;
import java.io.File;

public class Bird extends Objects {
    private float vt = 0;               // Biến vận tốc
    private boolean isFlying = false;   // Biến thể hiện đang bay, để hiển thị animation
    private Rectangle rect;             // Biến Bao bọc Bird, để tương tác với Ống Khói
    private boolean isLive = true;      // Biến để biết GameOver hay chưa
    public SoundPlayer flapSound, fallSound, getPointSound;

    public Bird(int x, int y, int w, int h) {
        super(x, y, w ,h);
        rect = new Rectangle(x, y, w, h);

        // Lấy file âm thanh
        flapSound = new SoundPlayer(new File("Assets/flap.wav"));
        fallSound = new SoundPlayer(new File("Assets/fall.wav"));
        getPointSound = new SoundPlayer(new File("Assets/getpoint.wav"));
    }

    // Setter và Getter
    public void setLive(boolean b) {
        isLive = b;
    }
    public boolean getLive() {
        return isLive;
    }

    public Rectangle getRect() {
        return rect;
    }

    public void setVt(float vt) {
        this.vt = vt;
    }

    public void update(long deltaTime) {
        vt+=FlappyBird.g;   // vận tốc tăng theo gia tốc

        this.setPosY(this.getPosY() + vt);  // Điều chỉnh độ cao thấp của Bird
        this.rect.setLocation((int) this.getPosX(), (int) this.getPosY());  // Điều chỉnh hình bao bọc

        if(vt<0) isFlying = true;   // Vận tốc âm thì Bird đang bay (hướng lên trên)
        else isFlying = false;      // Còn lại thì hướng sang ngang
    }

    public void fly() {
        vt = -3;
        flapSound.play();
    }

    public boolean getIsFlying() {
        return isFlying;
    }
}
