import javax.microedition.midlet.*;
import javax.microedition.lcdui.*;

public class app extends MIDlet {
  static app instance;
  static int width, height;
  static Graphics graphics;
  static Image buffer;

  static int BACKCOLOR=0xf0caa6;

  static TfrmMain frmMain;
  static TfrmGame frmGame;
  static TfrmHelp frmHelp;

  public app() {
    frmGame=new TfrmGame();
    frmMain = new TfrmMain();
    frmHelp = new TfrmHelp();
    instance = this;
    //buffer = Image.createImage(app.width, app.height);
    graphics = buffer.getGraphics();
    app.out.fontX=new Image[2];
    app.out.gc = 0xff;

    try {
      app.out.fontX[0]=Image.createImage(getClass().getResourceAsStream("/font1.png"));
      app.out.fontX[1]=Image.createImage(getClass().getResourceAsStream("/font2.png"));
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void startApp() {
    Display.getDisplay(this).setCurrent(frmMain);
  }

  public void pauseApp() {
  }

  public void destroyApp(boolean unconditional) {
  }

  public static void quitApp() {
    instance.destroyApp(true);
    instance.notifyDestroyed();
    instance = null;
  }

  public static class out {
    private static int curX, curY, curW, clipX, clipY, clipW;
    private static char gc;
    static final int fontWidth[] = {6, 8};
    static final int fontHeight[] = {9, 12};
    static Image[] fontX; //два шрифта (маленький и большой)
    static final int alLeft = 0;
    static final int alCenter = 1;
    static final int alRight = 2;
    static int align=0;
    static final String FontArray = " !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\nАБВГДЕЖЗ"
      +(char)0x418
      +"ЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдежзийклмнопрстуфхцчшщъыьэюя";

    private static String[] explode(String s, char delimiter)
    {
      String t = s;
      int i = 1, x = 0;
      while ( (x = t.indexOf(delimiter, ++x)) >= 0) i++;
      String result[] = new String[i];
      i = x = 0;
      while ( (x = t.indexOf(delimiter)) >= 0) {
        result[i] = t.substring(0, x);
        t = t.substring(x + 1);
        i++;
      }
      result[i] = t;
      return result;
    }

    private static String[] explode2(String s) {
      String t = "";
      int pos = 0, old_pos = 0, rel_pos = 0, cur_len = 0;
      //int cols = (app.width) / fontWidth[0];
      int cols = curW / fontWidth[0];
      while ( (pos = s.indexOf(" ", pos)) >= 0) {
        pos++;
        cur_len = pos - old_pos;
        if (rel_pos + cur_len > cols) {
          rel_pos = 0;
          t += "\n";
        }
        rel_pos += cur_len;
        t += s.substring(old_pos, pos);
        old_pos = pos;
      }
      cur_len = s.length() - old_pos;
      if (rel_pos + cur_len > cols) t += "\n";
      t += s.substring(old_pos);
      //System.out.println(t);
      String result[] = explode(t, '\n');
      return result;
    }

    private static void drawChar(char c) {
      int dx, dy, charposX, charposY;
      if (c == '\n') {
        //curX = 1;
        curX=clipX;
        curY += fontHeight[0];
      }
      else {
        int n = FontArray.indexOf(c);
        if (n == -1) {
          app.graphics.drawChar(c, curX, curY, 20);
        }
        else {
          gc = c;
          dx = fontWidth[0];
          dy = fontHeight[0];
          charposY = n / 16;
          charposX = n - charposY * 16;
          app.graphics.drawRegion(fontX[0], charposX * fontWidth[0], charposY * fontHeight[0], dx, dy, 0, curX, curY, 20);
        }
        curX += fontWidth[0];
      }
    }

    public static String[] prepare(String s) {
      return prepare(s, 0, app.width);
    }

    public static String[] prepare(String s, int clX, int clW) {
      curX=clipX=clX;
      curW=clipW=clW;
      int k = 0;
      String st[] = explode(s, '\n');
      String st2[][] = new String[st.length][];
      int lines = 0;
      for (int i = 0; i < st.length; i++) {
        st2[i] = explode2(st[i]);
        lines += st2[i].length;
      }
      String result[] = new String[lines];
      for (int i = 0; i < st.length; i++) {
        for (int j = 0; j < st2[i].length; j++) {
          result[k++] = st2[i][j];
        }
      }
      return result;
    }

    public static void draw(String s, int clX, int clY) {
      app.out.draw(app.out.prepare(s), clX, clY, app.width-clipX);
    }

    public static void draw(String s, int clX, int clY, int clW) {
      app.out.draw(app.out.prepare(s, clX, clW), clX, clY, clW);
    }

    public static void draw(String s[], int clX, int clY) {
      app.out.draw(s, clX, clY, app.width-clX, 0, 0);
    }

    public static void draw(String s[], int clX, int clY, int clW) {
      app.out.draw(s, clX, clY, clW, 0, 0);
    }

    public static void draw(String s[], int clX, int clY, int clW, int start, int rows) {
      curX = clipX = clX;
      curY = clipY = clY;
      curW = clipW = clW;
      int lines = s.length;
      if (start + rows > lines) rows = lines - start;
      if (rows == 0) rows = lines;
      for (int i = 0; i < s.length; i++) {
        if (i >= start && i < start + rows) {
          int sw=s[i].length()*fontWidth[0];
          switch (align) {
            case alCenter:
              curX+=(clipW-sw)/2;
              break;
            case alRight:
              curX+=clipW-sw;
              break;
          }
          for (int j = 0; j < s[i].length(); j++) {
            drawChar(s[i].charAt(j));
          }
          drawChar('\n');
        }
      }
    }
  }

}

