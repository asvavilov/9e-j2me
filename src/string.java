import javax.microedition.lcdui.Image;

public class string {
  private int curX, curY;
  private char gc;
  static int fontWidth[] = {6, 8};
  static int fontHeight[] = {9, 12};
  static Image[] fontX; //два шрифта (маленький и большой)
  static final int alLeft = 0;
  static final int alCenter = 1;
  static final int alRight = 2;
  static final String FontArray = " !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\nАБВГДЕЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдежзийклмнопрстуфхцчшщъыьэюя";
  public String value;

  public string(String value) {
    this.value=value;
    gc = 0xff;
  }
  private String[] explode(String s, char delimiter)
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

  private String[] explode2(String s) {
    String t = "";
    int pos = 0, old_pos = 0, rel_pos = 0, cur_len = 0;
    int cols = (app.width) / fontWidth[0];
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

  private void drawChar(char c) {
    int dx, dy, charposX, charposY;
    if (c == '\n') {
      curX = 1;
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
        app.graphics.drawRegion(fontX[0], charposX * fontWidth[0],
                                charposY * fontHeight[0], dx, dy, 0, curX, curY,
                                20);
      }
      curX += fontWidth[0];
    }
  }

  public String[] prepare(String s) {
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

  public void draw(String s, int xxx, int yyy) {
    this.draw(this.prepare(s), xxx, yyy);
  }

  public void draw(String s[], int xxx, int yyy) {
    this.drawStr(s, xxx, yyy, 0, 0);
  }

  public void drawStr(String s[], int xxx, int yyy, int start, int rows) {
    curX = xxx;
    curY = yyy;
    int lines = s.length;
    if (start + rows > lines) rows = lines - start;
    if (rows == 0) rows = lines;
    for (int i = 0; i < s.length; i++) {
      if (i >= start && i < start + rows) {
        for (int j = 0; j < s[i].length(); j++) {
          drawChar(s[i].charAt(j));
        }
        drawChar('\n');
      }
    }
  }

}
