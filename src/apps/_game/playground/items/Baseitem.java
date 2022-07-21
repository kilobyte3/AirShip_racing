package apps._game.playground.items;

import apps._game.playground.MapConstruction;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * minden, a pályát meghatározó dolognak vannak közös adatai (sebesség, koordináta)
 * az összes objektum ebbõl épül fel, és ez önmagában nemis létezhet (absztakt)
 *
 * @author Kis Balázs kilobyte@freemail.hu
 */
public abstract class Baseitem
{
    public final static String PACKAGE_OF_ITEMS = "apps._game.playground.items.elements";

    private int x,y, speed,speedWhenVisible, offsetx,offsety;
    private boolean deleteItself;
    public static MapConstruction palyaRef = null;
    public static boolean DO_SCROLLING = true; // lehet automatikus görgetés vagy nem?

    /**
     * az elem "éltetése"
     */
    abstract public void updateAnims();

    /**
     * elemek görgetése
     */
    public void updateScrolling()
    {
        onUpdate();
        if (DO_SCROLLING)
        {
            if (getX() <= MapConstruction.ARENA_WIDTH)
            {
                decreaseXwithSpeed();
            }
            else
            {
                decreaseXwithSpeedWhenVisible();
            }
        }
    }

    /**
     * esemény, amely akkor váltódik ki, ha az objektumon végrehajtották az updateScrolling() metódust
     * igény esetén felül lehet irni
     */
    public void onUpdate()
    {
    }

    /**
     * pozició inicializálás
     */
    public void init()
    {
        init(0, 0);
    }

    /**
     * pozició és sebességek inicializálás
     */
    public void init(int x, int y)
    {
        init(x, y, 1, 1);
    }

    /**
     * pozició, sebességek és eltolás inicializálás
     */
    protected void init(int x, int y, int speed, int speedWhenVisible)
    {
        deleteItself = false;
        this.setX(x);
        this.setY(y);
        this.setSpeed(speed);
        this.setSpeedWhenVisible(speedWhenVisible);
        this.setOffsetX(0);
        this.setOffsetY(0);
    }

    /**
     * x koordináta lekérdezése
     */
    public int getX()
    {
        return x;
    }

    /**
     * x koordináta beállitása
     */
    public void setX(int value)
    {
        x = value;
    }

    /**
     * y koordináta lekérdezése
     */
    public int getY()
    {
        return y;
    }

    /**
     * y koordináta beállitása
     */
    public void setY(int value)
    {
        y = value;
    }

    /**
     * normál görgetési sebesség lekérdezése
     */
    public int getSpeed()
    {
        return speed;
    }

    /**
     * görgetési sebesség akkor, mikor az objektum látható
     */
    public int getSpeedWhenVisible()
    {
        return speedWhenVisible;
    }

    /**
     * normál görgetési sebesség beállitása
     */
    public void setSpeed(int value)
    {
        speed = value;
    }

    /**
     * beállitja azt a görgetési sebességet, amivel a tárgy akkor mozog, ha látható
     */
    public void setSpeedWhenVisible(int value)
    {
        speedWhenVisible = value;
    }

    /**
     * x tengelyû eltolás lekérdezése
     */
    protected int getOffsetX()
    {
        return offsetx;
    }

    /**
     * x tengelyû eltolás beállitása
     */
    public void setOffsetX(int value)
    {
        offsetx = value;
    }

    /**
     * y tengelyû eltolás lekérdezése
     */
    protected int getOffsetY()
    {
        return offsety;
    }

    /**
     * x tengelyû eltolás beállitása
     */
    protected void setOffsetY(int value)
    {
        offsety = value;
    }

    /**
     * tárgy görgetése
     */
    protected void decreaseXwithSpeed()
    {
        x-= speed;  // látjuk az objektumot, néhány (pl. sas) "begyorsít" ekkor
    }

    /**
     * tárgy görgetése ha éppen látható
     */
    protected void decreaseXwithSpeedWhenVisible()
    {
        x-= speedWhenVisible; // rendes scroll, elvileg mindig 1, de pl. a pályagenerátornál ezt ki kell kapcsolni
    }

    /**
     * x koordináta eltolása (negativ is lehet)
     */
    public void increaseX(int with)
    {
        x+= with;
    }

    /**
     * y koordináta eltolása (negativ is lehet)
     */
    public void increaseY(int with)
    {
        y+= with;
    }

    /**
     * objektum adatainak mentése folyamba
     * bizonyos esetben az objektumok a képernyõ tetejére - aljára vannak tapasztva,
     * ekkor célszerû felülirni a metódust, hogy csak az x koordinátával foglalkozzon
     * "kimenthetetlen" objektumokat ne igy csináljunk, csatoljuk rájuk a "INoSaveable" interfészt!
     */
    public void saveToStream(DataOutputStream f) throws IOException
    {
        f.writeInt(x);
        f.writeInt(y);
    }

    /**
     * objektum inicializálása folyamból
     * bizonyos esetben az objektumok a képernyõ tetejére - aljára vannak tapasztva,
     * ekkor célszerû felülirni a metódust, hogy csak az x koordinátával foglalkozzon
     * "betölthetetlen" objektumokat ne igy csináljunk, csatoljuk rájuk a "INoSaveable" interfészt!
     */
    public void loadFromStream(DataInputStream f) throws IOException
    {
        x = f.readInt();
        y = f.readInt();
        init(x, y);
    }

    /**
     * megnyomjuk az önmegsemmisités gombot
     */
    protected void setDeleteItself()
    {
        deleteItself = true;
    }

    /**
     * az elemet törölni kell?
     */
    public boolean getDeleteItself()
    {
        return deleteItself;
    }
}