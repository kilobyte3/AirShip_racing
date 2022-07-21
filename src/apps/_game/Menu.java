package apps._game;

import apps.Animation;
import apps.FileStorage;
import apps._game.playground.MapConstruction;
import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

/**
 * menürendszer
 *
 * @author Kis Balázs kilobyte@freemail.hu
 */
final public class Menu
{
    private final int menuCellSpacing = 25;
    private final BufferedImage menuPicture;
    public int menufunc = 1, submenu = 1;
    private final Font menufont, font2;
    private final Animation lufiAnim;
    private final AudioClip menuSound;
    public boolean backgrounds = true;
    private final GameApplication game;

    /**
     * konstruktor
     */
    public Menu(GameApplication game)
    {
        this.game = game;
        menuPicture = FileStorage.loadPicture("Gfx/menu.png");
        lufiAnim = new Animation("Gfx/lufi.png", 10, 4);
        menuSound = FileStorage.loadSound("Sounds/bonus.wav");
        menufont = new Font("Arial", Font.PLAIN, 14);
        font2 = new Font("Times", Font.PLAIN, 10);
    }

    /**
     * szöveg kiirása középre
     */
    private void centerWrite(int y, String str, Graphics at, Color color)
    {
        at.setColor(color);
        at.drawString(str, (int)(MapConstruction.ARENA_WIDTH - at.getFontMetrics().getStringBounds(str, at).getWidth())/2, y);
    }

    /**
     * menü renderelése
     */
    public void draw(Graphics at)
    {
        String s;
        at.drawImage(menuPicture, 0,0, null);
        at.setFont(menufont);
        switch(submenu)
        {
            case 1 : centerWrite(40+(menuCellSpacing*0), "New game", at, new Color(128,0,0));
                     centerWrite(40+(menuCellSpacing*1), "Options", at, new Color(128,0,0));
                     centerWrite(40+(menuCellSpacing*2), "Credit", at, new Color(128,0,0));
                     centerWrite(40+(menuCellSpacing*3),"Exit", at, new Color(128,0,0));
                     lufiAnim.draw(at, 20, 30+((menufunc-1)*menuCellSpacing));
                     lufiAnim.draw(at, 100,30+((menufunc-1)*menuCellSpacing));
                     break;
            case 2 : if (AudioWrapper.enabled) { s = "on"; } else { s = "off"; }
                     centerWrite(50+(menuCellSpacing*0), "Sounds "+s, at, new Color(128,0,0));
                     if (backgrounds)
                     {
                         s = "on";
                     }
                     else
                     {
                         s = "off";
                     }
                     centerWrite(50+(menuCellSpacing*1), "Backs "+s, at, new Color(128,0,0));
                     centerWrite(50+(menuCellSpacing*2), "Back", at, new Color(128,0,0));
                     lufiAnim.draw(at, 20, 40+((menufunc-1)*menuCellSpacing));
                     lufiAnim.draw(at, 100,40+((menufunc-1)*menuCellSpacing));
                     break;
            case 3 : centerWrite(40, "AirShip Racing", at, new Color(128,128,0));
                     centerWrite(70, "Copyright (C)", at, new Color(128,0,0));
                     centerWrite(90, "Kilobyte", at, new Color(128,0,0));
                     at.setColor(new Color(0,0,0));
                     at.setFont(font2);
                     at.drawString("<ENTER>", 11,138);
                     break;
            case 4 : if (AudioWrapper.enabled) { s = "on"; } else { s = "off"; }
                     centerWrite(30+(menuCellSpacing*0), "Sounds "+s, at, new Color(128,0,0));
                     if (backgrounds) { s = "on"; } else { s = "off"; }
                     centerWrite(30+(menuCellSpacing*1), "Backs "+s, at, new Color(128,0,0));
                     centerWrite(30+(menuCellSpacing*2), "Mainmenu", at, new Color(128,0,0));
                     centerWrite(30+(menuCellSpacing*3), "Close game", at, new Color(128,0,0));
                     centerWrite(30+(menuCellSpacing*4), "Back", at, new Color(128,0,0));
                     lufiAnim.draw(at, 16, 20+((menufunc-1)*menuCellSpacing));
                     lufiAnim.draw(at, 104,20+((menufunc-1)*menuCellSpacing));
                     break;
        }
    }

    /**
     * animációk
     */
    public void update()
    {
        lufiAnim.update();
    }

    /**
     * billentyûleütés történt
     */
    public void keyPressed(KeyEvent e)
    {
        if (e.getKeyCode() == KeyEvent.VK_UP)
        {
            menufunc--;
            AudioWrapper.play(menuSound);
            switch(submenu)
            {
                case 1 :
                    if (menufunc <= 0) { menufunc = 4; }
                    break;
                case 2 :
                    if (menufunc <= 0) { menufunc = 3; }
                    break;
                case 4 :
                    if (menufunc <= 0) { menufunc = 5; }
                    break;
            }
        }

        if (e.getKeyCode() == KeyEvent.VK_DOWN)
        {
            menufunc++;
            AudioWrapper.play(menuSound);
            switch(submenu)
            {
                case 1 :
                    if (menufunc >= 5) { menufunc = 1; }
                    break;
                case 2 :
                    if (menufunc >= 4) { menufunc = 1; }
                    break;
                case 4 :
                    if (menufunc >= 6) { menufunc = 1; }
                    break;
            }
        }

        if (e.getKeyCode() == KeyEvent.VK_ENTER)
        {
            if ((submenu == 1) && (menufunc == 1)) // new game
            {
                game.gameState = GameApplication.gameStates.INGAME;
                game.playground.restart();
                game.playground.introduce();
                return;
            }
            if (submenu == 1 && menufunc == 3) { submenu = 3; menufunc = 1; return; }
            if (submenu == 1 && menufunc == 2) { submenu = 2; menufunc = 1; return; }
            if (submenu == 1 && menufunc == 4) { System.exit(0); }
            if (submenu == 2 && menufunc == 1) { AudioWrapper.enabled = !AudioWrapper.enabled; return; }
            if (submenu == 2 && menufunc == 2) { backgrounds = !backgrounds; return; }
            if (submenu == 2 && menufunc == 3) { submenu = 1; menufunc = 2; return; }
            if (submenu == 3)                  { submenu = 1; menufunc = 3; return; }
            if (submenu == 4 && menufunc == 1) { AudioWrapper.enabled = !AudioWrapper.enabled; return; }
            if (submenu == 4 && menufunc == 2) { backgrounds = !backgrounds; return; }
            if (submenu == 4 && menufunc == 3) { submenu = 1; menufunc = 1; return; }
            if (submenu == 4 && menufunc == 4) { System.exit(0); }
            if (submenu == 4 && menufunc == 5) { game.gameState = GameApplication.gameStates.INGAME; }
        }
    }
}