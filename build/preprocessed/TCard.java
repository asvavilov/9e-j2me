import javax.microedition.lcdui.*;

public class TCard extends VisualComponent {
  static final int value_width=10;
  static final int value_height=10;
  static final int mast_width=7;
  static final int mast_height=9;
  static final int padding=2;
  static final int arcWidth=10;
  static final int arcHeight=10;
  Image img_mast;
  int mast;
  int value;
  static int count=0;
  int index=0;
  int offset;
  
  boolean enabled;
  boolean visible;
  
  private final int BACKCOLOR=0xfff0f0;//0x00ffff;

  public TCard(int x, int y, int value, int mast) {
    super(x, y, 3*app.out.fontWidth[1],3*app.out.fontHeight[1]);
    this.value=value;
    this.mast=mast;
    this.enabled=true;
    this.visible=false;
    this.index=this.count;
    this.count++;
  }

  public void hod() {
    this.posX=this.value*10+80;
    this.posY=this.mast*40+10;
    this.enabled=false;
  }
  
  public void repaint() {
    //String s[]=app.prepareString(this.text);
    if (!this.visible) return;
    if (app.frmGame.selected<12 && app.frmGame.selected==this.index) {
      offset=2*this.width/3;
      app.graphics.setColor(0);
      app.graphics.drawRoundRect(posX-1-offset, posY-1, this.width+2, this.height+2, arcWidth, arcHeight);
      app.graphics.setColor(0xffffff);
    } else {
      offset=0;
      app.graphics.setColor(BACKCOLOR);
    }
    app.graphics.fillRoundRect(posX-offset,posY,this.width,this.height,arcWidth,arcHeight);
    app.graphics.setColor(0x000000);
    app.graphics.drawRoundRect(posX-offset,posY,this.width,this.height,arcWidth,arcHeight);
    if (value>=1 && value<=9 && mast>=1 && mast<=4) {
      app.graphics.drawRegion(app.frmGame.mast, mast_width * (mast - 1), 0,
                              mast_width, mast_height, 0,
                              this.width + this.posX - mast_width - padding - offset,
                              this.posY + padding, Graphics.LEFT | Graphics.TOP);
      app.graphics.drawRegion( ( (this.mast == 1 || this.mast == 2) ?
                                (app.frmGame.red) : (app.frmGame.black)),
                              value_width * (value - 1), 0, value_width,
                              value_height, 0, this.posX + padding - offset,
                              this.posY + padding, Graphics.LEFT | Graphics.TOP);
    }
  }
}
