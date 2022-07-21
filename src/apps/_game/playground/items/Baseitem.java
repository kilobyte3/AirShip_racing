package apps._game.playground.items;

import apps._game.playground.MapConstruction;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * minden, a p�ly�t meghat�roz� dolognak vannak k�z�s adatai (sebess�g, koordin�ta)
 * az �sszes objektum ebb�l �p�l fel, �s ez �nmag�ban nemis l�tezhet (absztakt)
 *
 * @author Kis Bal�zs kilobyte@freemail.hu
 */
public abstract class Baseitem
{
    public final static String PACKAGE_OF_ITEMS = "apps._game.playground.items.elements";

    private int x,y, speed,speedWhenVisible, offsetx,offsety;
    private boolean deleteItself;
    public static MapConstruction palyaRef = null;
    public static boolean DO_SCROLLING = true; // lehet automatikus g�rget�s vagy nem?

    /**
     * az elem "�ltet�se"
     */
    abstract public void updateAnims();

    /**
     * elemek g�rget�se
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
     * esem�ny, amely akkor v�lt�dik ki, ha az objektumon v�grehajtott�k az updateScrolling() met�dust
     * ig�ny eset�n fel�l lehet irni
     */
    public void onUpdate()
    {
    }

    /**
     * pozici� inicializ�l�s
     */
    public void init()
    {
        init(0, 0);
    }

    /**
     * pozici� �s sebess�gek inicializ�l�s
     */
    public void init(int x, int y)
    {
        init(x, y, 1, 1);
    }

    /**
     * pozici�, sebess�gek �s eltol�s inicializ�l�s
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
     * x koordin�ta lek�rdez�se
     */
    public int getX()
    {
        return x;
    }

    /**
     * x koordin�ta be�llit�sa
     */
    public void setX(int value)
    {
        x = value;
    }

    /**
     * y koordin�ta lek�rdez�se
     */
    public int getY()
    {
        return y;
    }

    /**
     * y koordin�ta be�llit�sa
     */
    public void setY(int value)
    {
        y = value;
    }

    /**
     * norm�l g�rget�si sebess�g lek�rdez�se
     */
    public int getSpeed()
    {
        return speed;
    }

    /**
     * g�rget�si sebess�g akkor, mikor az objektum l�that�
     */
    public int getSpeedWhenVisible()
    {
        return speedWhenVisible;
    }

    /**
     * norm�l g�rget�si sebess�g be�llit�sa
     */
    public void setSpeed(int value)
    {
        speed = value;
    }

    /**
     * be�llitja azt a g�rget�si sebess�get, amivel a t�rgy akkor mozog, ha l�that�
     */
    public void setSpeedWhenVisible(int value)
    {
        speedWhenVisible = value;
    }

    /**
     * x tengely� eltol�s lek�rdez�se
     */
    protected int getOffsetX()
    {
        return offsetx;
    }

    /**
     * x tengely� eltol�s be�llit�sa
     */
    public void setOffsetX(int value)
    {
        offsetx = value;
    }

    /**
     * y tengely� eltol�s lek�rdez�se
     */
    protected int getOffsetY()
    {
        return offsety;
    }

    /**
     * x tengely� eltol�s be�llit�sa
     */
    protected void setOffsetY(int value)
    {
        offsety = value;
    }

    /**
     * t�rgy g�rget�se
     */
    protected void decreaseXwithSpeed()
    {
        x-= speed;  // l�tjuk az objektumot, n�h�ny (pl. sas) "begyors�t" ekkor
    }

    /**
     * t�rgy g�rget�se ha �ppen l�that�
     */
    protected void decreaseXwithSpeedWhenVisible()
    {
        x-= speedWhenVisible; // rendes scroll, elvileg mindig 1, de pl. a p�lyagener�torn�l ezt ki kell kapcsolni
    }

    /**
     * x koordin�ta eltol�sa (negativ is lehet)
     */
    public void increaseX(int with)
    {
        x+= with;
    }

    /**
     * y koordin�ta eltol�sa (negativ is lehet)
     */
    public void increaseY(int with)
    {
        y+= with;
    }

    /**
     * objektum adatainak ment�se folyamba
     * bizonyos esetben az objektumok a k�perny� tetej�re - alj�ra vannak tapasztva,
     * ekkor c�lszer� fel�lirni a met�dust, hogy csak az x koordin�t�val foglalkozzon
     * "kimenthetetlen" objektumokat ne igy csin�ljunk, csatoljuk r�juk a "INoSaveable" interf�szt!
     */
    public void saveToStream(DataOutputStream f) throws IOException
    {
        f.writeInt(x);
        f.writeInt(y);
    }

    /**
     * objektum inicializ�l�sa folyamb�l
     * bizonyos esetben az objektumok a k�perny� tetej�re - alj�ra vannak tapasztva,
     * ekkor c�lszer� fel�lirni a met�dust, hogy csak az x koordin�t�val foglalkozzon
     * "bet�lthetetlen" objektumokat ne igy csin�ljunk, csatoljuk r�juk a "INoSaveable" interf�szt!
     */
    public void loadFromStream(DataInputStream f) throws IOException
    {
        x = f.readInt();
        y = f.readInt();
        init(x, y);
    }

    /**
     * megnyomjuk az �nmegsemmisit�s gombot
     */
    protected void setDeleteItself()
    {
        deleteItself = true;
    }

    /**
     * az elemet t�r�lni kell?
     */
    public boolean getDeleteItself()
    {
        return deleteItself;
    }
}