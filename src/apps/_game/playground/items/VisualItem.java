package apps._game.playground.items;

import apps._game.playground.MapConstruction;
import java.awt.Graphics;
import java.awt.Rectangle;

/**
 * "l�that�" elemet jel�l. Jelen eseten ugyan minden elem l�that�, de elk�pzelhet�,
 * hogy egyszer lesznek majd k�p n�lk�li, befoly�sol� elemek, m�retek n�lk�l
 *
 * @author Kis Bal�zs kilobyte@freemail.hu
 */
public abstract class VisualItem extends Baseitem
{
    public SoundEffect soundEffect = null;
    public AnimationEffect animationEffect = null;
    public PictureEffect pictureEffect = null;
    private int cacheW = 0;
    private int cacheH = 0;

    /**
     * sz�less�g�nek be�llit�sa
     */
    protected void setCachedWidth(int value)
    {
        cacheW = value;
    }

    /**
     * magass�g�nak be�llit�sa
     */
    protected void setCachedHeight(int value)
    {
        cacheH = value;
    }

    /**
     * sz�less�g�nek lek�rdez�se
     */
    public int getCacheW()
    {
        return cacheW;
    }

    /**
     * magass�g�nak lek�rdez�se
     */
    public int getCacheH()
    {
        return cacheH;
    }

    /**
     * teljesen l�that�?
     */
    public boolean isFullyVisible()
    {
        return new Rectangle(0,0, MapConstruction.ARENA_WIDTH,MapConstruction.ARENA_HEIGHT).contains(new Rectangle(getX(), getY(), getCacheW(), getCacheH()));
    }

    /**
     * �rinti ezt a t�glalapot?
     */
    public boolean doesImpact(Rectangle r)
    {
        return r.intersects(new Rectangle(getX(),getY(), getCacheW(),getCacheH()));
    }

    /**
     * be�llitja, mi t�rt�njen �tk�z�sre
     */
    public abstract Collision effectOnCollision();

    /**
     * hangeffekt l�trehoz�sa ha kell, �s visszaad�sa
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
     * anim�ci� l�trehoz�sa ha kell, �s visszaad�sa
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
     * egyedi k�p l�trehoz�sa ha kell, �s visszaad�sa
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
     * elem renderel�se
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
     * elemek anim�l�sa
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