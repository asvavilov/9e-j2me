import javax.microedition.lcdui.*;

public class TfrmHelp extends Canvas{
  /**
   * constructor
   */
  public TfrmHelp() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  
  private void jbInit() throws Exception {
    setFullScreenMode(true);
  }
  
  /**
   * paint
   * @param g 
   */
  public void paint(Graphics g) {
    app.BACKCOLOR=0xf0caa6;
    app.graphics.setColor(app.BACKCOLOR);
    app.graphics.fillRect(0,0,app.width,app.height);
    app.out.align=app.out.alLeft;
    app.out.draw("справка...", 0, 0);
    //g.drawString("справка...",0,0,Graphics.TOP|Graphics.LEFT);
    g.drawImage(app.buffer,0,0,0);
  }
  
  /**
   * Called when a key is pressed.
   * @param keyCode 
   */
  protected  void keyPressed(int keyCode) {
    switch (getGameAction(keyCode)) {
      case Canvas.FIRE:
        Display.getDisplay(app.instance).setCurrent(app.frmMain);
        break;
    }
  }
  
   /**
   * Called when action should be handled
   * @param command 
   * @param displayable 
   */
  public void commandAction(Command command, Displayable displayable) {
  }
  
}
