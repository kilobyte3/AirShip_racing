package apps._game.playground;

import apps.Animation;
import apps.FileStorage;
import apps._game.AudioWrapper;
import apps._game.GameApplication;
import apps._game.playground.items.Baseitem;
import apps._game.playground.items.elements.Island;
import java.applet.AudioClip;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;

/**
 * a játékos léghajója.
 *
 * @author Kis Balázs kilobyte@freemail.hu
 */
final public class Airship
{
    public final static short TANK_AMOUNT = 150; // "benzintank" mérete
    private final static short BLINK_AMOUNT = 50; // ennyi ideig villog

    // a hajó különféle állapotokat vehet fel, mindig egyszerre csak egyet
    public enum shipStates {
        NORMAL,      // egyhelyben áll
        LEFT,        // balraugrik
        RIGHT,       // jobbraugrik
        UP,          // feljebbmegy
        FALLING,     // zuhan
        GOINGTOEXIT  // kinavigál magától
    };
    private shipStates shipState;

    private final AudioClip waaaaaSound;
    private float pX,pY, lendulet;
    private short pBlink, air;
    private int timer_movingr, timer_movingl;
    private final Animation upAnim, leftAnim, fallAnim, rightAnim;
    private final BufferedImage playerPicture;
    private final Graphics targetSurface;
    private boolean upkeypressing, leftkeypressing, rightkeypressing = false;
    private final GameApplication gameAppRef;
    private final Island island;

    /**
     * konstruktor
     */
    Airship(Graphics at, GameApplication game)
    {
        targetSurface = at;
        gameAppRef = game;
        upAnim = new Animation("Gfx/player_up.png", 20,5, 2);
        leftAnim = new Animation("Gfx/player_left.gif", 20,3, 2);
        rightAnim = new Animation("Gfx/player_right.gif", 20,3, 2);
        waaaaaSound = FileStorage.loadSound("Sounds/die.wav");
        fallAnim = new Animation("Gfx/fall.gif", 10,3);
        playerPicture = FileStorage.loadPicture("Gfx/player.gif");
        island = new Island();
        island.init();
    }

    /**
     * hajó állapota
     */
    public shipStates getShipState()
    {
        return shipState;
    }

    /**
     * hajó alapállapotba hozatala
     */
    public void reset()
    {
        pX = 0;
        pY = 0;
        lendulet = 1;
        shipState = shipStates.NORMAL;
        pBlink = BLINK_AMOUNT;
        air = TANK_AMOUNT;
    }

    /**
     * hajó kirajzolása, figyelembevéve az állapotait
     */
    public void draw()
    {
        if (pBlink % 2 == 0) // player megjelenítése/villogtatása
        {
            switch(shipState)
            {
                case NORMAL :
                    targetSurface.drawImage(playerPicture, Math.round(pX), Math.round(pY), null);
                    break;
                case UP :
                    upAnim.draw(targetSurface, Math.round(pX), Math.round(pY));
                    break;
                case RIGHT :
                    rightAnim.draw(targetSurface, Math.round(pX), Math.round(pY));
                    break;
                case LEFT :
                    leftAnim.draw(targetSurface, Math.round(pX), Math.round(pY));
                    break;
                case FALLING :
                    targetSurface.drawImage(playerPicture, Math.round(pX), Math.round(pY), null);
                    fallAnim.draw(targetSurface, (int)pX+5,(int)pY-11);
                    break;
                case GOINGTOEXIT :
                    targetSurface.drawImage(playerPicture, Math.round(pX), Math.round(pY), null);
                    break;
            }
        }
    }

    /**
     * animációk éltetése, fizika
     */
    public void update()
    {
        upAnim.update();
        leftAnim.update();
        rightAnim.update();
        fallAnim.update();
        if (shipState != shipStates.FALLING && shipState != shipStates.GOINGTOEXIT && shipState != shipStates.NORMAL)
        {
            // visszaugrasztjuk normálba, mivel az irányitás "nyomogatós" jellegû
            shipState = shipStates.NORMAL;
        }

        if (pBlink > 0)
        {
            pBlink--; // villogás
        }

        // csak akkor irányithatunk, ha nem navigálunk a célba épp, illetve ha nem szakadt ki a gumink :)
        if (shipState != shipStates.FALLING && shipState != shipStates.GOINGTOEXIT)
        {
            // jobbra ugrás
            if (timer_movingr > 0)
            {
                timer_movingr--;
                shipState = shipStates.RIGHT;
                increasePx(2);
            }

            // balra ugrás
            if (timer_movingl > 0)
            {
                timer_movingl--;
                shipState = shipStates.LEFT;
                increasePx(-2);
            }

            // lendület, gravitáció, fizika
            increasePy(lendulet);
            lendulet+= 0.25f;
            if (lendulet >= 2)
            {
                lendulet = 2;
            }

            if (upkeypressing)
            {
                air--;
                if (air > 0)
                {
                    lendulet-= 0.4f;
                }
                if (lendulet <= -4)
                {
                    lendulet = -4;
                }
                if (shipState == shipStates.NORMAL)
                {
                    shipState = shipStates.UP;
                }
            }
            if (pY <= 0)
            {
                pY = 0;
                lendulet = 1;
            }
            if (timer_movingr < 0)
            {
                increasePx(1);
                shipState = shipStates.RIGHT;
            }

            // elértük a pálya végét és közel vagyunk a szigethez?
            if (!Baseitem.DO_SCROLLING && pX > MapConstruction.ARENA_WIDTH - island.getCacheW() - 16 && shipState != shipStates.GOINGTOEXIT)
            {
                shipState = shipStates.GOINGTOEXIT;
                AudioWrapper.play(FileStorage.loadMusic("Musics/win.mid"));
            }
        }
        else
        {
            if (shipState != shipStates.GOINGTOEXIT)
            {
                increasePy(6); // valami kiszakította a légballont, most épp zuhanásban vagyunk
                if (pY > MapConstruction.ARENA_HEIGHT - fallAnim.getHeight())
                {
                    reset();
                }
            }
        }
    }

    /**
     * már vizbeestünk?
     * ha már zuhanunk, vagy kinavigálunk, akkor nem számit
     */
    public boolean isInWater()
    {
        return
            shipState != shipStates.FALLING &&
            shipState != shipStates.GOINGTOEXIT &&
            pY > MapConstruction.ARENA_HEIGHT-playerPicture.getHeight();
    }

    /**
     * ha kinavigálás van, akkor azt lépteti, és TRUE a visszatérés ha már a szigeten (jobb alsó sarok) vagyunk
     */
    public boolean goingToExitAndHasReachedTheIsland()
    {
        if (shipState == shipStates.GOINGTOEXIT)
        {
            if (pX < MapConstruction.ARENA_WIDTH-playerPicture.getWidth()-1)
            {
                increasePx(1);
            }
            else
            {
                if (pY < MapConstruction.ARENA_HEIGHT-island.getCacheH())
                {
                    increasePy(1);
                }
            }
            if (pX >= MapConstruction.ARENA_WIDTH-playerPicture.getWidth()-1 && pY >= MapConstruction.ARENA_HEIGHT-island.getCacheH())
            {
                return true;
            }
        }
        return false;
    }

    /**
     * billentyûleütés
     */
    public void keyPressed(KeyEvent e)
    {
        if (e.getKeyCode() == KeyEvent.VK_UP)
        {
            upkeypressing = true;
        }

        if (e.getKeyCode() == KeyEvent.VK_RIGHT)
        {
            if (!rightkeypressing && timer_movingr == 0 && shipState != shipStates.GOINGTOEXIT)
            {
                rightAnim.setFrameAt(0);
                timer_movingr = 6;
            }
            rightkeypressing = true;
        }

        if (e.getKeyCode() == KeyEvent.VK_LEFT && shipState != shipStates.GOINGTOEXIT)
        {
            if (!leftkeypressing && timer_movingl == 0)
            {
                leftAnim.setFrameAt(0);
                timer_movingl = 6;
            }
            leftkeypressing = true;
        }

        if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
        {
            gameAppRef.gameState = GameApplication.gameStates.MENU;
            gameAppRef.menu.menufunc = 1;
            gameAppRef.menu.submenu = 4;
        }
    }

    /**
     * billentyû elengedés
     */
    public void keyRelease(KeyEvent e)
    {
        switch(e.getKeyCode())
        {
            case KeyEvent.VK_UP :
                upkeypressing = false;
                break;
            case KeyEvent.VK_RIGHT :
                rightkeypressing = false;
                break;
            case KeyEvent.VK_LEFT :
                leftkeypressing = false;
                break;
        }
    }

    /**
     * mennyi levegõ van a tankban?
     */
    public int getAir()
    {
        return air;
    }

    /**
     * léghajónk hitbox-ja (ütközési négyzet)
     */
    public Rectangle getHitbox()
    {
        return new Rectangle(Math.round(pX),Math.round(pY), playerPicture.getWidth(),playerPicture.getHeight());
    }

    /**
     * kiszakadt a gumink, zuhanunk!
     */
    public void startFalldown()
    {
        shipState = shipStates.FALLING;
        AudioWrapper.play(waaaaaSound);
    }

    /**
     * villogás
     */
    public int getBlink()
    {
        return pBlink;
    }

    /**
     * éppen villogunk?
     */
    public boolean isBlinking()
    {
        return getBlink() != 0;
    }

    /**
     * levegõmennyíség beállitása
     */
    public void setAir(short value)
    {
        air = value;
    }

    /**
     * lendület növelése
     */
    public void increaseLendulet(float with)
    {
        lendulet+= with;
    }

    /**
     * játékos jobbraléptetése, figyelve, hogy nem megy ki a képernyõrõl
     */
    public void increasePx(int with)
    {
        pX = pX + with;
        if (pX > MapConstruction.ARENA_WIDTH-playerPicture.getWidth())
        {
            pX = MapConstruction.ARENA_WIDTH-playerPicture.getWidth();
        }
        if (pX < 2)
        {
            pX = 2;
        }
    }

    /**
     * játékos y koordinátájának növelése
     */
    public void increasePy(float with)
    {
        pY+= with;
    }

    /**
     * játékos y koordinátájának növelése
     */
    public void increasePy(int with)
    {
        increasePy((float)with);
    }
}