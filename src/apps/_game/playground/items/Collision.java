package apps._game.playground.items;

/**
 * ütközésobjektum, leírja, hogy mik történhetnek egy tárgyyal, ha vele ütközés történt.
 * lehettek volna interfészek is akár, de így rövidebb a végsõ kód, és könnyebb beállítani.
 *
 * @author Kis Balázs kilobyte@freemail.hu
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
     * ütközött objektum maga, ugyan gyakran nincs értelme ezt is átadni, mégis jól jöhet a késõbbiekben
     */
    public Collision setObject(Baseitem value)
    {
        obj = value;
        return this;
    }

    /**
     * ütközött objektum visszaadása
     */
    public Baseitem getObject()
    {
        return obj;
    }

    /**
     * beállítja, hogy mennyi életet adjon, vagy csökkentsen
     */
    public Collision setLife(short value)
    {
        life = value;
        return this;
    }

    /**
     * élet lekérdezése
     */
    public short getLife()
    {
        return life;
    }

    /**
     * beállitja, hogy milyen értékre állítsa a levegõtartalmat
     */
    public Collision setAir(short value)
    {
        air = value;
        return this;
    }

    /**
     * levegõmennyiség lekérdezése
     */
    public short getAir()
    {
        return air;
    }

    /**
     * a tárgy eltûnik megérintése után
     */
    public Collision setRemoveable()
    {
        isRemoveable = true;
        return this;
    }

    /**
     * ütközés esetén a tárgy törlendõ?
     */
    public boolean getIsRemoveable()
    {
        return isRemoveable;
    }

    /**
     * egy negativ-pozitiv X értékkel eltolja a játékost
     */
    public Collision setConfuseAirshipX(int value)
    {
        confuseAirshipX = value;
        return this;
    }

    /**
     * mennyi az X tengelyû eltolás?
     */
    public int getConfuseAirshipX()
    {
        return confuseAirshipX;
    }

    /**
     * egy negativ-pozitiv Y értékkel eltolja a játékost
     */
    public Collision setConfuseAirshipY(int value)
    {
        confuseAirshipY = value;
        return this;
    }

    /**
     * mennyi az Y tengelyû eltolás?
     */
    public int getConfuseAirshipY()
    {
        return confuseAirshipY;
    }

    /**
     * mennyi bónusz pontot kapjon e tárgy felvételekor?
     */
    public Collision setBonusPoint(int value)
    {
        bonusPoint = value;
        return this;
    }

    /**
     * bónuszpont lekérdezése
     */
    public int getBonusPoint()
    {
        return bonusPoint;
    }

    /**
     * befolyásolja a lendületet
     */
    public Collision setLendulet(float value)
    {
        lendulet = value;
        return this;
    }

    /**
     * lendület lekérdezése
     */
    public float getLendulet()
    {
        return lendulet;
    }
}