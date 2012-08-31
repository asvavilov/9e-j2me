import javax.microedition.lcdui.*;

public class TMenu extends VisualComponent {
  String[] items;
  short indexItem;
  Image marker;
  int markerColor;
  public TMenu(int top) {
    super(1,top);
    items=new String[]{"начать игру","помощь","справка","выход"};
    indexItem=0;
    markerColor=0x00ff00;
  }

  public void up() {
    indexItem--;
    if (indexItem<0) {
      indexItem=(short)(items.length-1);
    }
    app.frmMain.repaint();
  }

  public void down() {
    indexItem++;
    if (indexItem>=items.length) {
      indexItem=0;
    }
    app.frmMain.repaint();
  }

  public void repaint() {
    Display.getDisplay(app.instance).getCurrent();
    //app.graphics.setColor(app.BACKCOLOR);
    app.out.align=app.out.alCenter;
    app.graphics.setColor(this.markerColor);
    app.graphics.drawRoundRect(0, this.posY+app.out.fontHeight[0]*this.indexItem, app.width-1, app.out.fontHeight[0], 5, 5);
    app.graphics.setColor(0xff,0xcc,0x66);
    //app.graphics.fillRect(0,this.posY,app.width,items[i].length()*app.fontHeight[0]);
    app.out.draw(items, this.posX, this.posY);
    
  }

}
