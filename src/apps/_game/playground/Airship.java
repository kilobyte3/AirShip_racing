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
 * a j�t�kos l�ghaj�ja.
 *
 * @author Kis Bal�zs kilobyte@freemail.hu
 */
final public class Airship
{
    public final static short TANK_AMOUNT = 150; // "benzintank" m�rete
    private final static short BLINK_AMOUNT = 50; // ennyi ideig villog

    // a haj� k�l�nf�le �llapotokat vehet fel, mindig egyszerre csak egyet
    public enum shipStates {
        NORMAL,      // egyhelyben �ll
        LEFT,        // balraugrik
        RIGHT,       // jobbraugrik
        UP,          // feljebbmegy
        FALLING,     // zuhan
        GOINGTOEXIT  // kinavig�l mag�t�l
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
     * haj� �llapota
     */
    public shipStates getShipState()
    {
        return shipState;
    }

    /**
     * haj� alap�llapotba hozatala
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
     * haj� kirajzol�sa, figyelembev�ve az �llapotait
     */
    public void draw()
    {
        if (pBlink % 2 == 0) // player megjelen�t�se/villogtat�sa
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
     * anim�ci�k �ltet�se, fizika
     */
    public void update()
    {
        upAnim.update();
        leftAnim.update();
        rightAnim.update();
        fallAnim.update();
        if (shipState != shipStates.FALLING && shipState != shipStates.GOINGTOEXIT && shipState != shipStates.NORMAL)
        {
            // visszaugrasztjuk norm�lba, mivel az ir�nyit�s "nyomogat�s" jelleg�
            shipState = shipStates.NORMAL;
        }

        if (pBlink > 0)
        {
            pBlink--; // villog�s
        }

        // csak akkor ir�nyithatunk, ha nem navig�lunk a c�lba �pp, illetve ha nem szakadt ki a gumink :)
        if (shipState != shipStates.FALLING && shipState != shipStates.GOINGTOEXIT)
        {
            // jobbra ugr�s
            if (timer_movingr > 0)
            {
                timer_movingr--;
                shipState = shipStates.RIGHT;
                increasePx(2);
            }

            // balra ugr�s
            if (timer_movingl > 0)
            {
                timer_movingl--;
                shipState = shipStates.LEFT;
                increasePx(-2);
            }

            // lend�let, gravit�ci�, fizika
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

            // el�rt�k a p�lya v�g�t �s k�zel vagyunk a szigethez?
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
                increasePy(6); // valami kiszak�totta a l�gballont, most �pp zuhan�sban vagyunk
                if (pY > MapConstruction.ARENA_HEIGHT - fallAnim.getHeight())
                {
                    reset();
                }
            }
        }
    }

    /**
     * m�r vizbeest�nk?
     * ha m�r zuhanunk, vagy kinavig�lunk, akkor nem sz�mit
     */
    public boolean isInWater()
    {
        return
            shipState != shipStates.FALLING &&
            shipState != shipStates.GOINGTOEXIT &&
            pY > MapConstruction.ARENA_HEIGHT-playerPicture.getHeight();
    }

    /**
     * ha kinavig�l�s van, akkor azt l�pteti, �s TRUE a visszat�r�s ha m�r a szigeten (jobb als� sarok) vagyunk
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
     * billenty�le�t�s
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
     * billenty� elenged�s
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
     * mennyi leveg� van a tankban?
     */
    public int getAir()
    {
        return air;
    }

    /**
     * l�ghaj�nk hitbox-ja (�tk�z�si n�gyzet)
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
     * villog�s
     */
    public int getBlink()
    {
        return pBlink;
    }

    /**
     * �ppen villogunk?
     */
    public boolean isBlinking()
    {
        return getBlink() != 0;
    }

    /**
     * leveg�menny�s�g be�llit�sa
     */
    public void setAir(short value)
    {
        air = value;
    }

    /**
     * lend�let n�vel�se
     */
    public void increaseLendulet(float with)
    {
        lendulet+= with;
    }

    /**
     * j�t�kos jobbral�ptet�se, figyelve, hogy nem megy ki a k�perny�r�l
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
     * j�t�kos y koordin�t�j�nak n�vel�se
     */
    public void increasePy(float with)
    {
        pY+= with;
    }

    /**
     * j�t�kos y koordin�t�j�nak n�vel�se
     */
    public void increasePy(int with)
    {
        increasePy((float)with);
    }
}