package apps._game.playground.items.elements;

import apps._game.playground.MapConstruction;
import apps._game.playground.items.Collision;
import apps._game.playground.items.INoSaveable;
import apps._game.playground.items.VisualItem;
import java.awt.Graphics;

/**
 * vill�ml�s, �nmag�ban nem szabad haszn�lni, a CloudB gener�lja
 * kiment�sre sem ker�l. "Haszn�lat" ut�n t�rl�dik!
 *
 * Created on 2008. m�rcius 3., 15:42
 *
 * @author Kis Bal�zs kilobyte@freemail.hu
 */
public class Lightin extends VisualItem implements INoSaveable
{
    private byte tick = 0;

    @Override
    public void init(int x, int y)
    {
        super.init(x,y);
        getAnimationInstance().init("Gfx/flash.gif", 40,3, 2);
        getSoundInstance().init("Sounds/flash.wav");
        this.setCachedHeight(getAnimationInstance().getAnimation().getHeight()*2);
        this.setCachedWidth(-1); // egy kis csal�s: igy csak akkor fog lek�ldeni minket a vill�m, amikor t�nylegesen is becsap :)
    }

    @Override
    public void updateScrolling()
    {
        super.updateScrolling();

        // ez az�rt kell, mert k�l�nben minden felh� egyforma id�k�z�nk�nt vill�mlana
        if (getX()+getOffsetX() < MapConstruction.ARENA_WIDTH && getX()+getOffsetX() > 0)
        {
            tick++;

            if (tick == 20 || tick == 31)
            {
                getSoundInstance().play();
            }

            if (tick >= 20 && tick <= 49)
            {
                setCachedWidth(getAnimationInstance().getAnimation().getWidth());
            }

            if (tick >= 50)
            {
                tick = 50;
                this.setCachedWidth(-1);
                setDeleteItself();
            }
        }
    }

    @Override
    public void draw(Graphics at)
    {
        if (getX()+getOffsetX() < MapConstruction.ARENA_WIDTH && getX()+getOffsetX() > 0)
        {
            if (getCacheW() != -1)
            {
                getAnimationInstance().getAnimation().draw(at, getX()+getOffsetX(),getY());
                getAnimationInstance().getAnimation().draw(at, getX()+getOffsetX(),getY()+getAnimationInstance().getAnimation().getHeight());
            }
        }
    }

    @Override
    public Collision effectOnCollision()
    {
        return new Collision().setObject(this).setLife((short)-1);
    }
}