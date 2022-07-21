package apps._game.playground;

import apps.FileStorage;
import apps._game.AudioWrapper;
import apps._game.GameApplication;
import apps._game.playground.items.Baseitem;
import apps._game.playground.items.Collision;
import apps._game.playground.items.VisualItem;
import apps._game.playground.items.elements.Island;
import general.GeneralFile;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 * Created on 2008. február 29., 15:02
 *
 * itt valósítottam meg a valódi játékot, irányitás, ütközésvizsgálat stb
 *
 * @author Kis Balázs kilobyte@freemail.hu
 */
final public class Playground
{
    private final BufferedImage heartPicture;
    private int level, point, introducingTick, life, pickedUpLufi,maxLufi, gameOverTick;
    private final Font font1, font2, font3, font4;
    private final java.applet.AudioClip bonusSound, lifeSound;
    private final MapConstruction palya;
    private boolean showStatistics = false;
    private final Graphics targetSurfaceRef;
    private final GameApplication gameAppRef;
    private final Airship airship;

    /**
     * konstruktor
     */
    public Playground(Graphics targetSurface, GameApplication gameApp)
    {
        targetSurfaceRef = targetSurface;
        gameAppRef = gameApp;
        palya = new MapConstruction(targetSurface);
        Baseitem.palyaRef = palya;
        airship = new Airship(targetSurface, gameAppRef);
        heartPicture = FileStorage.loadPicture("Gfx/heart.gif");
        bonusSound = FileStorage.loadSound("Sounds/bonus.wav");
        lifeSound = FileStorage.loadSound("Sounds/life.wav");
        font1 = new Font("Arial", Font.PLAIN, 24);
        font2 = new Font("Times New Roman", Font.PLAIN, 36);
        font3 = new Font("Arial", Font.ITALIC, 10);
        font4 = new Font("Arial", Font.PLAIN, 18);
    }

    /**
     * a játék újrainditása
     */
    public void restart()
    {
        level = 1;
        point = 0;
        life = 3;
    }

    /**
     * belépés egy pályára, ha elfogytak a pályák, akkor jutalom!
     */
    public void introduce()
    {
        AudioWrapper.play(FileStorage.loadMusic("Musics/begin.mid"));
        URL map = GeneralFile.getFile("Maps/"+level+".mar");
        if (map != null)
        {
            DataInputStream str;
            try
            {
                str = new DataInputStream(GeneralFile.getFile("Maps/"+level+".mar").openStream());
                if (!palya.loadFromStream(str))
                {
                    str = null;
                }
            }
            catch(IOException ex)
            {
                str = null;
                Logger.getLogger(Playground.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (str == null)
            {
                JOptionPane.showMessageDialog(GeneralFile.comp, "A fájl megnyitása közben [Maps/"+level+".mar] hiba történt", "", JOptionPane.INFORMATION_MESSAGE);
            }
            Baseitem.DO_SCROLLING = true;
            introducingTick = 0;
            pickedUpLufi = 0;
            maxLufi = palya.getCountOfThisItem("Lufi");
            showStatistics = false;
            gameOverTick = 0;
            airship.reset();
        }
        else
        {
            AudioWrapper.play(FileStorage.loadMusic("Musics/intro.mid"));
            gameAppRef.gameState = GameApplication.gameStates.THEEND;
        }
    }

    /**
     * billentyûleütés
     */
    public void keyPressed(KeyEvent e)
    {
        airship.keyPressed(e);
        if (e.getKeyCode() == 107)
        {
            life++;
        }
        if (e.getKeyCode() == KeyEvent.VK_ENTER && showStatistics)
        {
            level++;
            introduce();
        }
    }

    /**
     * billentyû elengedés
     */
    public void keyRelease(KeyEvent e)
    {
        airship.keyRelease(e);
    }

    /**
     * renderelés
     */
    public void draw()
    {
        // kértünk háttérképet?
        if (gameAppRef.menu.backgrounds)
        {
            palya.drawBackground();
        }
        else
        {
            targetSurfaceRef.setColor(new Color(192,210,226));
            targetSurfaceRef.fillRect(0,0, MapConstruction.ARENA_WIDTH,MapConstruction.ARENA_HEIGHT);
        }

        // game over?
        if (life <= 0)
        {
            targetSurfaceRef.setColor(new Color(255,0,0));
            targetSurfaceRef.setFont(font2);
            final FontMetrics fm = targetSurfaceRef.getFontMetrics();
            Rectangle2D area = fm.getStringBounds("GAME", targetSurfaceRef);
            targetSurfaceRef.drawString("GAME", (int)(MapConstruction.ARENA_WIDTH - area.getWidth())/2, 60);
            area = fm.getStringBounds("OVER", targetSurfaceRef);
            targetSurfaceRef.drawString("OVER", (int)(MapConstruction.ARENA_WIDTH - area.getWidth())/2, 100);
            return ;
        }

        if (introducingTick <= 10)
        {
            targetSurfaceRef.setColor(new Color(255,0,0));
            targetSurfaceRef.setFont(font1);
            final FontMetrics fm = targetSurfaceRef.getFontMetrics();
            Rectangle2D area = fm.getStringBounds("LEVEL #"+level, targetSurfaceRef);
            targetSurfaceRef.drawString("LEVEL #"+level, (int)(MapConstruction.ARENA_WIDTH - area.getWidth())/2, 60);
            targetSurfaceRef.setColor(new Color(32,32,0));
            targetSurfaceRef.setFont(font2);
            area = fm.getStringBounds("GO!!", targetSurfaceRef);
            targetSurfaceRef.drawString("GO!!", (int)(MapConstruction.ARENA_WIDTH - area.getWidth())/2, 100);
        }
        else
        {
            palya.draw();

            if (!showStatistics)
            {
                airship.draw();
            }

            palya.drawWater();

            // élet, pont
            for(int i = life; i > 0; i--)
            {
                targetSurfaceRef.drawImage(heartPicture, MapConstruction.ARENA_WIDTH-(i*(heartPicture.getWidth()+1)),0, null);
            }
            targetSurfaceRef.setFont(font3);
            targetSurfaceRef.drawString(""+point, 0,10);
            targetSurfaceRef.setColor(new Color(128,0,0));
            targetSurfaceRef.fillRect(0,154, airship.getAir() / 2,5);

            if (showStatistics)
            {
                targetSurfaceRef.setColor(new Color(255,0,0));
                targetSurfaceRef.setFont(font4);
                FontMetrics fm = targetSurfaceRef.getFontMetrics();
                Rectangle2D area = fm.getStringBounds("LEVEL #"+level, targetSurfaceRef);
                targetSurfaceRef.drawString("LEVEL #"+level, (int)(MapConstruction.ARENA_WIDTH - area.getWidth())/2, 40);
                area = fm.getStringBounds("COMPLETED", targetSurfaceRef);
                targetSurfaceRef.drawString("COMPLETED", (int)(MapConstruction.ARENA_WIDTH - area.getWidth())/2, 60);

                targetSurfaceRef.setColor(new Color(0,0,0));
                targetSurfaceRef.setFont(font3);
                targetSurfaceRef.drawString("You gained: "+pickedUpLufi+"/"+maxLufi+ " points.", 10, 90);

                targetSurfaceRef.setFont(font4);
                targetSurfaceRef.drawString("<ENTER>", 0,130);
            }
        }
    }

    /**
     * elemek éltetése
     */
    public void update()
    {
        if (showStatistics)
        {
            return;
        }

        // nincs több élet?
        if (life <= 0)
        {
            gameOverTick++;
            if (gameOverTick >= 50)
            {
                gameAppRef.gameState = GameApplication.gameStates.MENU;
                gameAppRef.menu.submenu = 1;
                gameAppRef.menu.menufunc = 1;
            }
            return;
        }

        palya.updateAnims();
        if (Baseitem.DO_SCROLLING)
        {
            palya.checkAndDeleteItemsLeftScreen();

            // elértük már a célt jelképezõ szigetet?
            for(int j = 0; j < palya.count(); j++)
            {
                if (palya.get(j) instanceof Island && ((VisualItem)palya.get(j)).isFullyVisible())
                {
                    // minden tárgy görgetését leállitjuk, kivéve a gyorsabban repülõket
                    for(int k = 0; k < palya.count(); k++)
                    {
                        if (palya.get(k).getSpeed() == 1)
                        {
                            palya.get(k).setSpeed(0);
                        }
                        else
                        {
                            palya.get(k).setSpeed(1);
                        }
                    }
                    Baseitem.DO_SCROLLING = false;
                    break;
                }
            }
        }

        airship.update();

        if (airship.isInWater())
        {
            airship.reset();
            life--;

        }
        if (airship.goingToExitAndHasReachedTheIsland())
        {
            showStatistics = true;
            return;
        }

        // ütközés vizsgálat
        if (airship.getShipState() != Airship.shipStates.FALLING)
        {
            for(Map.Entry<Integer, Collision> entry : palya.getAllImpactEffects(airship.getHitbox()).entrySet())
            {
                if ("Lufi".equals(entry.getValue().getObject().getClass().getSimpleName()))
                {
                    pickedUpLufi++;
                }
                if (entry.getValue().getLife() > 0)
                {
                    life+= entry.getValue().getLife();
                    AudioWrapper.play(lifeSound);
                }
                if (!airship.isBlinking() && airship.getShipState() != Airship.shipStates.GOINGTOEXIT && entry.getValue().getLife() < 0)
                {
                    life--;
                    airship.startFalldown();
                }
                if (entry.getValue().getAir() != 0)
                {
                    AudioWrapper.play(bonusSound);
                    airship.setAir(entry.getValue().getAir());
                }
                if (entry.getValue().getBonusPoint() != 0)
                {
                    AudioWrapper.play(bonusSound);
                    point+= entry.getValue().getBonusPoint();
                }
                airship.increaseLendulet(entry.getValue().getLendulet());
                airship.increasePx(entry.getValue().getConfuseAirshipX());
                airship.increasePy(entry.getValue().getConfuseAirshipY());
                if (entry.getValue().getIsRemoveable())
                {
                    palya.delete(entry.getKey());
                }
            }
        }

        if (Baseitem.DO_SCROLLING)
        {
            palya.moveWater();
        }

        if (introducingTick <= 10)
        {
            introducingTick++;
        }
        else
        {
            if (Baseitem.DO_SCROLLING)
            {
                palya.offsetBackgroundX(-1);
            }
        }
    }
}