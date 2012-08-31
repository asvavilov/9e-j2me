import javax.microedition.lcdui.*;

abstract class VisualComponent {
  //Image img;
  int posX;
  int posY;
  int width;
  int height;
  VisualComponent(int x, int y) {
    this.posX=x;
    this.posY=y;
  }
  VisualComponent(int x, int y, int width, int height) {
    this.posX=x;
    this.posY=y;
    this.width=width;
    this.height=height;
  }
  abstract void repaint();
}
