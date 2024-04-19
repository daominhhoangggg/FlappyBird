package flappybird;

import pkg2dgamesframework.Objects;

import java.awt.*;

public class Chimney extends Objects {
    private Rectangle rect;                 // Biến bao bọc Ống Khói, để tương tác với Bird
    private boolean isBehindBird = false;   // Biến dùng để tính điểm
    public Chimney(int x, int y, int w, int h) {
        super(x, y, w, h);
        rect = new Rectangle(x, y, w, h);
    }

    public void update() {
        setPosX(getPosX() - 2);     // Cho Ống Khói trôi theo Mặt đất
        rect.setLocation((int) this.getPosX(), (int) this.getPosY()); // Cập nhật phần bao bọc
    }

    // Setter và Getter
    public Rectangle getRect() {
        return rect;
    }

    public void setIsBehindBird (boolean b) {
        isBehindBird = b;
    }
    public boolean getIsBehindBird () {
        return isBehindBird;
    }
}
