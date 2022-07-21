package apps._game.playground.items;

/**
 * �tk�z�sobjektum, le�rja, hogy mik t�rt�nhetnek egy t�rgyyal, ha vele �tk�z�s t�rt�nt.
 * lehettek volna interf�szek is ak�r, de �gy r�videbb a v�gs� k�d, �s k�nnyebb be�ll�tani.
 *
 * @author Kis Bal�zs kilobyte@freemail.hu
 */
final public class Collision
{
    private Baseitem obj;
    private short life = 0;
    private short air = 0;
    private boolean isRemoveable = false;
    private int confuseAirshipX = 0;
    private int confuseAirshipY = 0;
    private int bonusPoint = 0;
    private float lendulet = 0f;

    /**
     * �tk�z�tt objektum maga, ugyan gyakran nincs �rtelme ezt is �tadni, m�gis j�l j�het a k�s�bbiekben
     */
    public Collision setObject(Baseitem value)
    {
        obj = value;
        return this;
    }

    /**
     * �tk�z�tt objektum visszaad�sa
     */
    public Baseitem getObject()
    {
        return obj;
    }

    /**
     * be�ll�tja, hogy mennyi �letet adjon, vagy cs�kkentsen
     */
    public Collision setLife(short value)
    {
        life = value;
        return this;
    }

    /**
     * �let lek�rdez�se
     */
    public short getLife()
    {
        return life;
    }

    /**
     * be�llitja, hogy milyen �rt�kre �ll�tsa a leveg�tartalmat
     */
    public Collision setAir(short value)
    {
        air = value;
        return this;
    }

    /**
     * leveg�mennyis�g lek�rdez�se
     */
    public short getAir()
    {
        return air;
    }

    /**
     * a t�rgy elt�nik meg�rint�se ut�n
     */
    public Collision setRemoveable()
    {
        isRemoveable = true;
        return this;
    }

    /**
     * �tk�z�s eset�n a t�rgy t�rlend�?
     */
    public boolean getIsRemoveable()
    {
        return isRemoveable;
    }

    /**
     * egy negativ-pozitiv X �rt�kkel eltolja a j�t�kost
     */
    public Collision setConfuseAirshipX(int value)
    {
        confuseAirshipX = value;
        return this;
    }

    /**
     * mennyi az X tengely� eltol�s?
     */
    public int getConfuseAirshipX()
    {
        return confuseAirshipX;
    }

    /**
     * egy negativ-pozitiv Y �rt�kkel eltolja a j�t�kost
     */
    public Collision setConfuseAirshipY(int value)
    {
        confuseAirshipY = value;
        return this;
    }

    /**
     * mennyi az Y tengely� eltol�s?
     */
    public int getConfuseAirshipY()
    {
        return confuseAirshipY;
    }

    /**
     * mennyi b�nusz pontot kapjon e t�rgy felv�telekor?
     */
    public Collision setBonusPoint(int value)
    {
        bonusPoint = value;
        return this;
    }

    /**
     * b�nuszpont lek�rdez�se
     */
    public int getBonusPoint()
    {
        return bonusPoint;
    }

    /**
     * befoly�solja a lend�letet
     */
    public Collision setLendulet(float value)
    {
        lendulet = value;
        return this;
    }

    /**
     * lend�let lek�rdez�se
     */
    public float getLendulet()
    {
        return lendulet;
    }
}