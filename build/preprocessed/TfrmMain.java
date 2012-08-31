import javax.microedition.lcdui.*;

public class TfrmMain extends Canvas {
  //TLabel lbl_head;
  TMenu main_menu;
  Image nine1, nine2, menu;

  public TfrmMain() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    setFullScreenMode(true);
    app.width=getWidth();
    app.height=getHeight();
//app.width=132;app.height=176;//temp

    app.buffer=Image.createImage(app.width,app.height);
    nine1=Image.createImage(getClass().getResourceAsStream("/9e.png"));
    nine2=Image.createImage(getClass().getResourceAsStream("/nine.png"));
    menu=Image.createImage(getClass().getResourceAsStream("/menu.png"));
    //lbl_head=new TLabel(0,(app.height+nine2.getHeight())/2,"карточная игра \"Девятка\"\n\nАвтор: Вавилов Александр\nshurik83@mail.ru");
    main_menu=new TMenu(nine1.getHeight()+10);
  }

  protected void paint(Graphics g) {
    app.BACKCOLOR=0xf0caa6;
//g.setColor(0xffffff);g.fillRect(0,0,250,350);g.setColor(0);//temp

    app.graphics.setColor(app.BACKCOLOR);
    app.graphics.fillRect(0,0,app.width,app.height);

    app.graphics.drawImage(nine1,app.width/2,0,Graphics.HCENTER|Graphics.TOP);
    app.graphics.drawImage(nine2,app.width/2,app.height/2+main_menu.items.length*app.out.fontHeight[0],Graphics.HCENTER|Graphics.VCENTER);
    app.graphics.drawImage(menu,app.width,app.height,Graphics.RIGHT|Graphics.BOTTOM);

    //lbl_head.repaint();
    main_menu.repaint();

    g.drawImage(app.buffer,0,0,0);
  }


  protected void keyPressed(int keyCode) {
    switch (getGameAction(keyCode)) {
      case Canvas.UP:
        main_menu.up();
        break;
      case Canvas.DOWN:
        main_menu.down();
        break;
      case Canvas.FIRE:
        switch (main_menu.indexItem) {
          case 0://game
            Display.getDisplay(app.instance).setCurrent(app.frmGame);
            app.frmGame.gameInit();
            break;
          case 1://help
            Display.getDisplay(app.instance).setCurrent(app.frmHelp);
            break;
          case 2://info
            Alert alert_info=new Alert("Карточная игра \"Девятка\"");
            alert_info.setTimeout(Alert.FOREVER);
            alert_info.setString("Автор: Вавилов Александр (Shurik83)\nshurik83@mail.ru");
            Display.getDisplay(app.instance).setCurrent(alert_info, this);
            break;
        case 3://exit
            app.quitApp();
            break;
        }
        break;
    }
  }

}
