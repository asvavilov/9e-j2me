import java.util.Random;
import javax.microedition.lcdui.*;

public class TfrmGame extends Canvas implements Runnable {
  /*
    stol: TImage;
    GroupBox1: TGroupBox;
    GroupBox2: TGroupBox;
    lbl0: TLabel;
    btnStart: TSpeedButton;
    btnExit: TSpeedButton;
    gamer0: TLabel;
    gamer1: TLabel;
    gamer2: TLabel;
    pause: TTimer;
    idshow: TSpeedButton;
    sprav: TSpeedButton;
    igrok1: TImage;
    igrok2: TImage;
    igrok0: TImage;
    Panel1: TPanel;
    lbank: TLabel;
    bank: TImage;
    lblKon: TLabel;
    status: TStaticText;
    Image1: TImage;
    Image2: TImage;
    Image3: TImage;
  */
  Thread thread;
  Image black, red, mast;
  String cardsn; //колода
  int curIgrok; //сходивший игрок
  int curIndex; //карта, кот. сходили
  int gindex; //индекс игрока в таблице
  String filedir;
  String filerub, karti;
  String vibor;
  int nhod;
  int vibr[]=new int[3];
  int kar[]=new int[3]; //кол-во сделанных ходов игроков
  int chhod;
  int ball; //очки игрока
  int dball; //плата за пропуск
  int kon; //кон
  int kol; //счетчик, ожидание
  int selected; //выбранная карта
  TCard karta[][]=new TCard[3][12]; //0..2 - игрок, 0..11 - карты
  int pole[][]=new int[9][4];
  boolean pause;
  boolean isGame;
  TLabel lbl0, lblKon, status, lbl_hod1, lbl_hod2; //lbl_hod? - для отладки
  Image img_hod1, img_hod2;
  
  String data[]=new String[]{
    "00000",     "", "0000",     "",  "000",     "",   "00",     "",
        "0",     "",    "1",   "10",   "11",  "100",  "110",  "111",
     "1000", "1100", "1110", "1111","10000","11000","11100","11110",
    "11111",     "",  "101",     "", "1010",     "", "1101",     "",
     "1011",     "", "1001",     "","10100",     "","11010",     "",
    "10110",     "","10010",     "","11101",     "","11011",     "",
    "10111",     "","11001",     "","10101",     "","10011",     "",
    "10001",     "",   "01",     "",  "011",     "", "0111",     "",
    "01111",     "",  "010",     "", "0100",     "", "0110",     "",
     "0101",     "","01000",     "","01100",     "","01010",     "",
    "01110",     "","01001",     "","01101",     "","01011",     "",
    "00100",     "", "0010",     "",  "001",     "","00110",     "",
     "0011",     "","00111",     "","00101",     "","00010",     "",
     "0001",     "","00011",     "","00001"
    };



  public TfrmGame() {
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    setFullScreenMode(true);
    thread=new Thread(this);
    //thread.start();
    
    black=Image.createImage(getClass().getResourceAsStream("/black.png"));
    red=Image.createImage(getClass().getResourceAsStream("/red.png"));
    mast=Image.createImage(getClass().getResourceAsStream("/mast.png"));
    
    status=new TLabel(0, 0, "");
    /*lbl0=new TLabel(0,10,"игрок");
    lblKon=new TLabel(0,20,"кон");*/
    
    lbl_hod1=new TLabel(70,getHeight()-app.out.fontHeight[0]*2-10,"--");
    lbl_hod2=new TLabel(100,getHeight()-app.out.fontHeight[0]*2-10,"--");
    img_hod1=Image.createImage(app.out.fontWidth[0]*3, app.out.fontHeight[0]+5);
    img_hod2=Image.createImage(app.out.fontWidth[0]*3, app.out.fontHeight[0]+5);

//    gameInit();
  }
  
  protected void paint(Graphics g) {
    int i,j,i2,j2;
    app.BACKCOLOR=0xf0caa6;
    app.graphics.setColor(app.BACKCOLOR);
    app.graphics.fillRect(0,0,app.width,app.height);

    //app.graphics.drawImage(black,0,50,Graphics.LEFT|Graphics.TOP);
    //app.graphics.drawImage(red,0,60,Graphics.LEFT|Graphics.TOP);
    //app.graphics.drawImage(mast,10,75,Graphics.LEFT|Graphics.TOP);

    app.out.align=app.out.alLeft;
    
    /*lbl0.repaint();
    lblKon.repaint();*/
    status.repaint();
    
    lbl_hod1.repaint();
    lbl_hod2.repaint();
    app.graphics.drawImage(img_hod1, 70, app.height-app.out.fontHeight[0]-5, 0);
    app.graphics.drawImage(img_hod2, 100, app.height-app.out.fontHeight[0]-5, 0);
    
    for (i=0;i<3;i++) {
      for (j=0;j<karta[i].length;j++) {
        //!!!перерисовывать с учетом z-order
        if (karta[i][j].enabled) {
          karta[i][j].repaint();
        }
      }
    }
    for (j=0;j<4;j++) {
      for (i=0;i<9;i++) {
        if (pole[i][j]>0) {
          if (pole[i][j]<100) {
            i2=0;
          } else {
            i2=pole[i][j]/100;
          }
          j2=Integer.parseInt(rightstr("00"+String.valueOf(pole[i][j]), 2));
          karta[i2][j2-1].repaint();
        }
      }
    }
    //app.out.draw("ходит игрок # "+String.valueOf(chhod), 0, 100);
    g.drawImage(app.buffer,0,0,0);

  }

  protected void keyPressed(int keyCode) {
    switch (keyCode) {
      case Canvas.KEY_POUND:
      case Canvas.KEY_STAR:
        Display.getDisplay(app.instance).setCurrent(app.frmMain);
        break;
      default:
        switch (getGameAction(keyCode)) {
          case Canvas.UP:
            if (!isGame) break;
            do {
              selected--;
              if (selected<0) selected=11;
            } while (!karta[0][selected].enabled);
            repaint();
            break;
          case Canvas.DOWN:
            if (!isGame) break;
            do {
              selected++;
              if (selected>11) selected=0;
            } while (!karta[0][selected].enabled);
            repaint();
            break;
          case Canvas.FIRE:
            if (!isGame) break;
            if (selected >= 0 && selected < 12) {
              kartaClick(selected);
              repaint();
            }
            break;
        }
    }
  }

  /*synchronized*/ public void run() {
    do {
      ;
    } while (true);
    /*try {
      if (chhod!=0)
        Thread.sleep(1000);
      pause=false;
      games();
      //gameInit();
      repaint();
    } catch (InterruptedException ex) {
      ex.printStackTrace();
    }*/
  }


//=================================================================
public void gameInit() {
  String koloda_full="111213142122232431323334414243445152535461626364717273748182838491929394";
  String koloda;
  int i,j,r,card_v,card_m;
  
  selected=12;
  TCard.count=0;
  
  koloda=koloda_full;
  
  Random rnd=new Random();
  for (j=0;j<3;j++) {
    for (i=0;i<12;i++) {
      r=rnd.nextInt((int)(koloda.length()/2))*2;
      card_v=Integer.parseInt(String.valueOf(koloda.charAt(r)));
      card_m=Integer.parseInt(String.valueOf(koloda.charAt(r+1)));
      if (j==0) {
        karta[j][i]=new TCard(app.width-3*app.out.fontWidth[1]-5, i*(app.height-3*app.out.fontHeight[1])/12+5, card_v, card_m);
      } else {
        //karta[j][i]=new TCard(app.out.fontWidth[1], (j-1)*app.height/2+i*(app.height-app.out.fontHeight[1])/12+5, card_v, card_m);
        karta[j][i]=new TCard(app.out.fontWidth[1]*(j-1)*7/2+10, i*(app.height-3*app.out.fontHeight[1])/12+5, card_v, card_m);
      }
      karta[j][i].visible=true;
      if (card_v==4 && card_m==1) {
        chhod=j;
      }
      koloda=koloda.substring(0, r)+koloda.substring(r+2);
    }
  }
  sort();
  StartClr();
  
  
  //thread.start();
  games();
  
}

//=================================================================
private void sort() {
  int i, j, minK, mid_v, mid_m;
  for (j=0;j<=10;j++) {
    minK=j;
    for (i=j+1;i<=11;i++) {
      if ((karta[0][minK].mast>karta[0][i].mast) || (karta[0][minK].mast==karta[0][i].mast && karta[0][minK].value>karta[0][i].value)) {
        minK=i;
      }
    }
    mid_v=karta[0][minK].value;
    mid_m=karta[0][minK].mast;
    karta[0][minK].value=karta[0][j].value;
    karta[0][minK].mast=karta[0][j].mast;
    karta[0][j].value=mid_v;
    karta[0][j].mast=mid_m;
  }
}

//=================================================================
private String decode(double b) {
  String s="";
  //декодирует десятичное число(byte) в двоичное (string)
  while (b!=1 && b!=0) {
    s=String.valueOf(b % 2)+s;
    b=java.lang.Math.floor(b / 2);
  }
  return String.valueOf(b)+s;
}

//=================================================================
private String ife(int g, int k, int m) {
  String result;
  int i;
  //возвращаемые значения:
  //0 - нет карты на данное место
  //1 - есть карта на данное место
  result="0";
  for (i=0; i<=11; i++) {
    if (karta[g][i].enabled) {
      if (karta[g][i].value==k && karta[g][i].mast==m) {
        result="1";
      }
    }
  }
  return result;
}


//=================================================================
private void games() {
  int n;
  
  if (pause) {
    //pause();
    pause = false;
    //return;
  }

  vibr[0] = -1;
  vibr[1] = -1;
  vibr[2] = -1;

  //lbl0.text=String.valueOf(ball);
  //lblKon.text=String.valueOf(kon);

  if (kar[0] == 12) {
    //все карты выложены выиграл 0 игрок
    //btnStart.Enabled:=true;
    //kar.Enabled:=false;
    vibr[0]=12;
    vibr[1]=12;
    vibr[2]=12;
    //status.Color:=clAqua;
    status.text="Вы выиграли! Поздравляем!";
    ball=ball+kon;
    kon=0;
    saveTable();
    pause=true;
    isGame=false;
    return;
  }

  if (kar[1] == 12) {
    //все карты выложены выиграл 1 игрок
    //btnStart.Enabled:=true;
    //kar.Enabled:=false;
    vibr[0]=12;
    vibr[1]=12;
    vibr[2]=12;
    //status.Color:=clAqua;
    status.text="Выиграла Sonya.";
    kon=0;
    saveTable();
    pause=true;
    isGame=false;
    return;
  }

  if (kar[2] == 12) {
    //все карты выложены выиграл 2 игрок
    //btnStart.Enabled:=true;
    //kar.Enabled:=false;
    vibr[0]=12;
    vibr[1]=12;
    vibr[2]=12;
    //status.Color:=clAqua;
    status.text="Выиграла Tanya.";
    kon=0;
    saveTable();
    pause=true;
    isGame=false;
    return;
  }

  //lbank.Color:=clGray;
  //gamer0.Color:=clBlack;
  //gamer1.Color:=clBlack;
  //gamer2.Color:=clBlack;
  switch (chhod) {
    case 0:
      //gamer0.Color:=clGreen;
      break;
    case 1:
      //gamer1.Color:=clGreen;
      break;
    case 2:
      //gamer2.Color:=clGreen;
      break;
  }

  if (chhod==0) {
    vibor = "";
    for (n=0; n<=11; n++) {
      if (karta[0][n].enabled) {
        switch (karta[0][n].value) {
          case 1:
          case 2:
          case 3:
            if (pole[karta[0][n].value-1+1][karta[0][n].mast-1] != 0) {
              vibor+=(n<10?"0":"")+String.valueOf(n);
            }
            break;
          case 4:
            vibor+=(n<10?"0":"")+String.valueOf(n);
            break;
          case 5:
          case 6:
          case 7:
          case 8:
          case 9:
            if (pole[karta[0][n].value-1-1][karta[0][n].mast-1] != 0) {
              vibor+=(n<10?"0":"")+String.valueOf(n);
            }
            break;
        }
      }
    }
    if (vibor == "") {
      //нет карт чтобы сходить
      //gamer0.Color:=clRed;
      //lbank.Color:=clBlue;
      status.text="У Вас нет карт, чтобы ходить.";
      ball=ball-dball;
      kon=kon+dball;
      vibr[0] = 12;
      chhod = chhod + 1;
      //wait;
      //pause();
      pause=true;
      //thread.start();
      games();
      vibr[0] = -1;
      return;
    }
    status.text="Ваш ход.";
    //pause();
    pause=true;
  }

  if (chhod==1) {
    status.text="Ходит Sonya.";
    hod(1);
    //pause();
    pause=true;
    return;
  }
  if (chhod==2) {
    status.text="Ходит Tanya.";
    hod(2);
    //pause();
    pause=true;
    return;
  }

}

//=================================================================
private void hod(int ind) {
  boolean en=false;
  if (vibr[ind] != -1) {
    en=true;
  } else {
    do {
      prvibr(ind);
      if (vibr[ind] == 12) {
        en=true;
        break;
      }
    } while (!karta[ind][vibr[ind]].enabled);
  }
  if (!en) {
    curIgrok=ind;
    curIndex=vibr[ind];
    //karta[ind][vibr[ind]].Picture.LoadFromFile(cardsn+inttostr(karta[ind,vibr[ind]].tag)+'.jpg');
    //karta[ind][vibr[ind]].Parent:=frmMain;
    karta[ind][vibr[ind]].hod();
    if (karta[ind][vibr[ind]].value < 4) {
      //karta[ind][vibr[ind]].BringToFront
    } else {
      //karta[ind][vibr[ind]].SendToBack;
    }

    pole[karta[ind][vibr[ind]].value-1][karta[ind][vibr[ind]].mast-1] = Integer.parseInt(String.valueOf(ind)+rightstr("0"+String.valueOf(vibr[ind]+1), 2));
    kar[ind]++;
    
    if (ind==1) {
      lbl_hod1.text=String.valueOf(karta[ind][vibr[ind]].value)+String.valueOf(karta[ind][vibr[ind]].mast);
      
      img_hod1=Image.createImage(app.out.fontWidth[0]*3, app.out.fontHeight[0]+5);
      img_hod1.getGraphics().drawRegion(app.frmGame.mast, TCard.mast_width * (karta[ind][vibr[ind]].mast - 1), 0,
                              TCard.mast_width, TCard.mast_height, 0,
                              10,
                              0, Graphics.LEFT | Graphics.TOP);
      img_hod1.getGraphics().drawRegion( ( (karta[ind][vibr[ind]].mast == 1 || karta[ind][vibr[ind]].mast == 2) ?
                                (app.frmGame.red) : (app.frmGame.black)),
                              TCard.value_width * (karta[ind][vibr[ind]].value - 1), 0, TCard.value_width,
                              TCard.value_height, 0, 0,
                              0, Graphics.LEFT | Graphics.TOP);

    } else if (ind==2) {
      lbl_hod2.text=String.valueOf(karta[ind][vibr[ind]].value)+String.valueOf(karta[ind][vibr[ind]].mast);
      
      img_hod2=Image.createImage(app.out.fontWidth[0]*3, app.out.fontHeight[0]+5);
      img_hod2.getGraphics().drawRegion(app.frmGame.mast, TCard.mast_width * (karta[ind][vibr[ind]].mast - 1), 0,
                              TCard.mast_width, TCard.mast_height, 0,
                              10,
                              0, Graphics.LEFT | Graphics.TOP);
      img_hod2.getGraphics().drawRegion( ( (karta[ind][vibr[ind]].mast == 1 || karta[ind][vibr[ind]].mast == 2) ?
                                (app.frmGame.red) : (app.frmGame.black)),
                              TCard.value_width * (karta[ind][vibr[ind]].value - 1), 0, TCard.value_width,
                              TCard.value_height, 0, 0,
                              0, Graphics.LEFT | Graphics.TOP);

    }
    
  }
  chhod++;
  if (chhod == 3) chhod = 0;
  //thread.start();
  games();
}


//=================================================================
private String midstr(String str, int begin, int len) {
  int indA, indB;
  indA=begin-1;
  indB=indA+len;
  if (indA>str.length()) {
    indA=(str.length()>0?str.length()-1:0);
  }
  if (indB>str.length()) {
    indB=(str.length()>0?str.length()-1:0);
  }
  return str.substring(indA, indB);
}
private String leftstr(String str, int len) {
  if (len>str.length()) {
    len=(str.length()>0?str.length()-1:0);
  }
  return str.substring(0, len);
}
private String rightstr(String str, int len) {
  String s;
  if (len>str.length()) {
    s="";
  } else {
    s=str.substring(str.length()-len);
  }
  return s;
}

//=================================================================
private void prvibr(int ind) {
  Random rand;
  int n, ni, k, m, i, rnd, r, rayting;
  String s, max, maxs;
  String vibors[][];
   //{хранит индексы карт}
   //{и рейтинги из 0 и 1}
   //{и рейтинги 0..?}
  vibor = "";
  vibors=new String[3][12];
  ni=0;
  for (n=0; n<=11; n++) {
    if (karta[ind][n].enabled) {
      switch (karta[ind][n].value) {
        case 1:
        case 2:
        case 3:
          if (pole[(karta[ind][n].value-1) + 1][karta[ind][n].mast-1] != 0) {
            vibors[0][ni]=String.valueOf(n);
            ni=ni+1;
          }
          break;
        case 4:
          vibors[0][ni]=String.valueOf(n);
          ni=ni+1;
          break;
        case 5:
        case 6:
        case 7:
        case 8:
        case 9:
          if (pole[(karta[ind][n].value-1) - 1][karta[ind][n].mast-1] != 0) {
            vibors[0][ni]=String.valueOf(n);
            ni=ni+1;
          }
          break;
      }
    }
  }
  if (ni==0) {
    //нет карт чтобы сходить
    //gamer?.Color:=clRed;
    //lbank.Color:=clBlue;
    if (ind==1) {
      status.text="Sonya пропускает ход и платит штраф.";
      lbl_hod1.text="--";
    } else if (ind==2) {
      status.text="Tanya пропускает ход и платит штраф.";
      lbl_hod2.text="--";
    }
    vibr[ind] = 12;
    kon=kon+dball;
    //wait;
    //pause();
    pause=true;
    return;
  }

  for (n = 0; n<=ni-1; n++) {
    //выбор лучшей карты из списка доступных номеров
    k=karta[ind][Integer.parseInt(vibors[0][n])].value;
    m=karta[ind][Integer.parseInt(vibors[0][n])].mast;
    switch (k) {
      case 1: vibors[1][n]="1"; break;
      case 2: vibors[1][n]=ife(ind,k-1,m); break;
      case 3: vibors[1][n]=ife(ind,k-1,m)+ife(ind,k-2,m); break;
      case 4: vibors[1][n]=ife(ind,k-1,m)+ife(ind,k-2,m)+ife(ind,k-3,m)+ife(ind,k+1,m)+ife(ind,k+2,m)+ife(ind,k+3,m)+ife(ind,k+4,m)+ife(ind,k+5,m); break;
      case 5: vibors[1][n]=ife(ind,k+1,m)+ife(ind,k+2,m)+ife(ind,k+3,m)+ife(ind,k+4,m); break;
      case 6: vibors[1][n]=ife(ind,k+1,m)+ife(ind,k+2,m)+ife(ind,k+3,m); break;
      case 7: vibors[1][n]=ife(ind,k+1,m)+ife(ind,k+2,m); break;
      case 8: vibors[1][n]=ife(ind,k+1,m); break;
      case 9: vibors[1][n]="1"; break;
    }
  }

  rayting=0;
  for (r=0; r<=108; r++) {
    s=data[r];
    for (i=0; i<=ni-1; i++) {
      if (s=="" && i==0) rayting++; //шаг увеличения зависит от ni
      if (s==vibors[1][i]) vibors[2][i]=String.valueOf(rayting);
      if (karta[ind][Integer.parseInt(vibors[0][i])].value==4) {
        if (s==midstr(vibors[1][i],1,3)) vibors[2][i]=rightstr("000"+String.valueOf(rayting),3)+vibors[2][i];
        if (s==midstr(vibors[1][i],4,5))
          vibors[2][i]=vibors[2][i]+rightstr("000"+String.valueOf(rayting),3);
        if (vibors[2][i]!=null && vibors[2][i].length()==6) {
          if (leftstr(vibors[2][i],3).compareTo(rightstr(vibors[2][i],3))>=0) {
            vibors[2][i]=String.valueOf(Integer.parseInt(leftstr(vibors[2][i],3)));
          } else {
            vibors[2][i]=String.valueOf(Integer.parseInt(rightstr(vibors[2][i],3)));
          }
        }
      }
    }
  }

  max=rightstr("000"+vibors[2][0],3);
  if (ni==1)
    maxs=rightstr("00"+vibors[0][0],2);
    else
    maxs=rightstr("00"+vibors[0][0],2);
    for (i=1; i<=ni-1; i++) {
      if (String.valueOf(max).compareTo(String.valueOf(vibors[2][i]))<0) {
        max=rightstr("000"+vibors[2][i],3);
        maxs=rightstr("00"+vibors[0][i],2);
      }
      if (String.valueOf(max)==String.valueOf(vibors[2][i])) {
        maxs=maxs+rightstr("00"+vibors[0][i],2);
      }
    }

  rand=new Random();
  rnd=((int)(java.lang.Math.floor(rand.nextInt(maxs.length())/2)))*2+1;
  vibr[ind]=Integer.parseInt(midstr(maxs,rnd,2));
  //wait;
  //pause();
  /*
   showmessage('ind игрок:'+#13+
      vibors[0,0]+'_'+vibors[0,1]+'_'+vibors[0,2]+'_'+vibors[0,3]+#13+
      vibors[1,0]+'_'+vibors[1,1]+'_'+vibors[1,2]+'_'+vibors[1,3]+#13+
      vibors[2,0]+'_'+vibors[2,1]+'_'+vibors[2,2]+'_'+vibors[2,3]);
  */
}

//=================================================================
private void saveTable() {
  /*if (gindex>1) {
    frmID.igroki.Cells[2,gindex]:=inttostr(ball);
      frmID.igroki.Cells[1,gindex]:=inttostr
              (strtoint(frmID.igroki.Cells[1,gindex])+1);
  }*/
}

//=================================================================
private void StartClr() {
  int i, j2, i2;

  pause=false;
  isGame=true;
  kon=0;
  vibr[0] = -1;
  vibr[1] = -1;
  vibr[2] = -1;
  kar[0] = 0;
  kar[1] = 0;
  kar[2] = 0;
  vibor="";
  nhod = 0;
  kol=0;
  curIgrok=4;

  for (j2=0; j2<=3; j2++)
    for (i2=0; i2<=8; i2++) pole[i2][j2]=0;

  /*for (i=0; i<=11; i++) {
    //karta[0][i].Parent:=frmMain.kar;
    //karta[0][i].enabled=true;
    //karta[0][i].posY=10;
    //karta[0][i].posX=i*65+7;
    //karta[0][i].visible=true;
    //karta[1][i].visible=false;
    //karta[2][i].visible=false;
  }*/

  //status.Color:=clWhite;

}

//=================================================================
public void pause() {
  repaint();
  serviceRepaints();
  try {
    Thread.sleep(1000L);
  } catch (Exception e) {}
  pause=false;
}

/*
//=================================================================
procedure TfrmMain.pauseTimer(Sender: TObject);
begin
inc(kol);
if curIgrok<4 then
  karta[curIgrok,curIndex].Visible:=
                            not(karta[curIgrok,curIndex].Visible);
if kol>9 then
  begin
  if curIgrok<4 then karta[curIgrok,curIndex].Visible:=true;
  pause=false;
  curIgrok:=4;
  kol:=0;
  games;
  exit;
  end;

end;

//=================================================================
procedure TfrmMain.idshowClick(Sender: TObject);
begin
if messagebox(frmMain.WindowHandle,'Текущая игра будет прервана!',
      'Сменить игрока?', mb_okcancel+mb_iconwarning)=1 then
  begin
  frmMain.Hide;
  btnStart.Enabled:=true;
  kar.Enabled:=false;
  StartClr;
  razdrub;
  frmID.Show;
  end;
end;

//=================================================================
procedure TfrmMain.FormClose(Sender: TObject; var Action: TCloseAction);
begin
if messagebox(frmMain.WindowHandle,'Вы уверены, что '+#13+#10
  +'хотите покинуть программу?','Выход',mb_okcancel+mb_iconquestion)
  = idok then
  begin
  frmID.saveTable;
  application.Terminate;
  end
  else
  action:=caNone;
end;

//=================================================================
procedure TfrmMain.Image3Click(Sender: TObject);
begin
if pause==true then
  begin
  pause=false;
  if curIgrok<4 then karta[curIgrok,curIndex].Visible:=true;
  curIgrok:=4;
  kol:=0;
  games;
  exit;
  end;

end;
*/



/*
procedure TfrmMain.FormCreate(Sender: TObject);
var
f: file of byte;
i, j, c, b, bl: byte;
begin

//frmMain.Width:=800;
//frmMain.Height:=600;

filedir:=extractfiledir(application.ExeName);
for i:=0 to 11 do
begin
karta[0,i]:=TImage.Create(self);
karta[0,i].Parent:=frmMain.kar;
  karta[0,i].Width:=65;
karta[0,i].Height:=97;
karta[0,i].Stretch:=true;
karta[0,i].AutoSize:=false;
  karta[0,i].Left:=i*65+7;
  karta[0,i].Top:=10;
karta[0,i].ComponentIndex:=i;
karta[0,i].OnClick:=kartaClick;
  karta[0,i].Cursor:=crHandPoint;
end;

for j:=1 to 2 do
  begin
  for i:=0 to 11 do
    begin
    karta[j,i]:=TImage.Create(self);
    karta[j,i].Parent:=frmMain;
    karta[j,i].Width:=65;
    karta[j,i].Height:=97;
    karta[j,i].Stretch:=true;
    karta[j,i].AutoSize:=false;
    karta[j,i].Visible:=false;
    end;
  end;

dball:=1;

//если файл существует, то
//чтение из файла variant.dbc, расшифровка и запись в массив
if fileexists(filedir+'\variant.dbc') then
  begin
  c:=0;
  assignfile(f,filedir+'\variant.dbc');
  reset(f);
  while not(eof(f)) do
    begin
    read(f,bl);//длина
    read(f,b);//двоичный код
    if b=255 then
      begin
      data[c]:='';
      end
      else
      begin
      data[c]:=decode(b);
      data[c]:=rightstr('0000'+data[c],bl);
      end;
    inc(c);
    end;
  end;

razdrub();
end;
*/


//=================================================================
private void kartaClick(int index) {
  int m, n1;
  boolean en=false;
  
  if (!isGame) return;
  
  //откл. таймера и можно ходить, если ход игрока0
  if (pause) {
    pause=false;
    if (curIgrok<4)
      karta[curIgrok][curIndex].visible=true;
    curIgrok=4;
    kol=0;
    //thread.start();
    games();
  }
  if (chhod==0) {

    if (vibr[0] < 0 || vibr[0] > 11) {
      if (vibr[0] != 12 && index != 12) {
        vibr[0]=index;
        m=(int)(vibor.length() / 2);
        for (n1=1; n1<=m; n1++)
          if (Integer.parseInt(midstr(vibor, n1*2-1, 2)) == index) {
            vibor="mozhno";
            break;
          }

        if (vibor=="mozhno") {
          //karta[0,index].Parent:=frmMain;
          karta[0][index].hod();
          selected=12;
          if (karta[0][index].value < 4) {
            //karta[0][index].BringToFront
          } else {
            //karta[0][Index].SendToBack;
          }

          status.text="";
          pole[karta[0][index].value-1][karta[0][index].mast-1] = Integer.parseInt(String.valueOf(index+1));
          kar[0]++;//счетчик ходов игрока
          //выделять карту
          curIgrok=0;
          curIndex=index;
          //wait;
          en=false;
        } else {
          en=true;
        }
      }
      if (!en) {
        chhod++;
        if (chhod == 3) chhod = 0;
        if (chhod != 0) {
          //thread.start();
          games();
        }
      }
    }
    vibr[0] = -1;
  }
}

//=================================================================
private void razdrub() {
  int i;
  /*if fileexists(filedir+'\rub.jpg') then
      for i:=0 to 11 do
        karta[0,i].Picture.LoadFromFile(filedir+'\rub.jpg')
    else
      for i:=0 to 11 do
        karta[0,i].Picture:=stol.Picture;
*/
}



}
