package apps._game.playground.items.elements;

import apps.FileStorage;
import apps._game.playground.MapConstruction;
import apps._game.playground.items.Baseitem;
import apps._game.playground.items.Collision;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 * "beboruló" felhõ
 *
 * Created on 2008. március 3., 16:44
 * @author Kis Balázs kilobyte@freemail.hu
 */
public class CloudB extends Cloud
{
    private BufferedImage cloudPicture, cloudPictureD = null;
    private int tick;

    @Override
    public void init(int x, int y)
    {
        super.init(x, y, 1,1);
        cloudPicture = FileStorage.loadPicture("Gfx/cloud.gif");
        cloudPictureD = FileStorage.loadPicture("Gfx/cloud_b.gif");
        setCachedWidth(cloudPicture.getWidth());
        setCachedHeight(cloudPicture.getHeight());
        restartTick();
    }

    @Override
    public void updateScrolling()
    {
        super.updateScrolling();
        if (getX()+getOffsetX() < MapConstruction.ARENA_WIDTH && getX()+getOffsetX() > 0)
        {
            if (tick == 3)
            {
                Lightin l = new Lightin();
                l.init(getX()+6, getY()+getCacheH());
                Baseitem.palyaRef.add(l);
            }
            tick++;
            if (tick >= 70)
            {
                restartTick();
            }
        }
    }

    private void restartTick()
    {
        tick = 1;
    }

    @Override
    public void draw(Graphics at)
    {
        if ((tick >=  0 && tick <= 10) ||
            (tick >= 16 && tick <= 20) ||
            (tick >= 50 && tick <= 70))
        {
            at.drawImage(cloudPicture,  getX()+getOffsetX(),getY(), null);
        }
        else
        {
            at.drawImage(cloudPictureD, getX()+getOffsetX(),getY(), null);
        }
    }

    @Override
    public Collision effectOnCollision()
    {
        // meghivjuk a régit (az ütközési effektusok ugyanazok), de az objektumot erre kell átállitani
        return super.effectOnCollision().setObject(this);
    }
}