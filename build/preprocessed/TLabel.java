import javax.microedition.lcdui.*;
/*
 попробовать сделать болле универсальным,
 чтобы потом можно было использовать класс TList
 как расширение текущего (TList extends TLabel)
*/

public class TLabel extends VisualComponent {
  String text;
  public TLabel(int x, int y, String text) {
    super(x,y);
    this.text=text;
  }

  public void repaint() {
    String s[]=app.out.prepare(this.text);
    Display.getDisplay(app.instance).getCurrent();
    app.graphics.setColor(app.BACKCOLOR);
    //app.graphics.fillRect(0,this.posY,app.width,s.length*app.out.fontHeight[0]);
    app.graphics.fillRect(this.posX,this.posY,app.width-this.posX,s.length*app.out.fontHeight[0]);
    app.out.draw(s,this.posX,this.posY);
  }
}
