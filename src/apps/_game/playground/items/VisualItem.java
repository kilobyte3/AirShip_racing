package apps._game.playground.items;

import apps._game.playground.MapConstruction;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * "látható" elemet jelöl. Jelen eseten ugyan minden elem látható, de elképzelhetõ,
 * hogy egyszer lesznek majd kép nélküli, befolyásoló elemek, méretek nélkül
 *
 * @author Kis Balázs kilobyte@freemail.hu
 */
public abstract class VisualItem extends Baseitem
{
    public SoundEffect soundEffect = null;
    public AnimationEffect animationEffect = null;
    public PictureEffect pictureEffect = null;
    private int cacheW = 0;
    private int cacheH = 0;

    /**
     * szélességének beállitása
     */
    protected void setCachedWidth(int value)
    {
        cacheW = value;
    }

    /**
     * magasságának beállitása
     */
    protected void setCachedHeight(int value)
    {
        cacheH = value;
    }

    /**
     * szélességének lekérdezése
     */
    public int getCacheW()
    {
        return cacheW;
    }

    /**
     * magasságának lekérdezése
     */
    public int getCacheH()
    {
        return cacheH;
    }

    /**
     * teljesen látható?
     */
    public boolean isFullyVisible()
    {
        return new Rectangle(0,0, MapConstruction.ARENA_WIDTH,MapConstruction.ARENA_HEIGHT).contains(new Rectangle(getX(), getY(), getCacheW(), getCacheH()));
    }

    /**
     * érinti ezt a téglalapot?
     */
    public boolean doesImpact(Rectangle r)
    {
        return r.intersects(new Rectangle(getX(),getY(), getCacheW(),getCacheH()));
    }

    /**
     * beállitja, mi történjen ütközésre
     */
    public abstract Collision effectOnCollision();

    /**
     * hangeffekt létrehozása ha kell, és visszaadása
     */
    protected SoundEffect getSoundInstance()
    {
        if (soundEffect == null)
        {
            soundEffect = new SoundEffect(this);
        }
        return soundEffect;
    }

    /**
     * animáció létrehozása ha kell, és visszaadása
     */
    protected AnimationEffect getAnimationInstance()
    {
        if (animationEffect == null)
        {
            animationEffect = new AnimationEffect(this);
        }
        return animationEffect;
    }

    /**
     * egyedi kép létrehozása ha kell, és visszaadása
     */
    protected PictureEffect getPictureInstance()
    {
        if (pictureEffect == null)
        {
            pictureEffect = new PictureEffect(this);
        }
        return pictureEffect;
    }

    /**
     * elem renderelése
     */
    public void draw(Graphics at)
    {
        if (animationEffect != null)
        {
            animationEffect.draw(at, getX()+getOffsetX(), getY());
        }
        if (pictureEffect != null)
        {
            pictureEffect.draw(at, getX()+getOffsetX(), getY());
        }
    }

    /**
     * elemek animálása
     */
    @Override
    public void updateAnims()
    {
        if (this.animationEffect != null)
        {
            this.animationEffect.update();
        }
    }
}