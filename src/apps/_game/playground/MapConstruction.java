package apps._game.playground;

import apps.Animation;
import apps.FileStorage;
import apps._game.playground.items.Baseitem;
import apps._game.playground.items.Collision;
import apps._game.playground.items.INoSaveable;
import apps._game.playground.items.VisualItem;
import general.GeneralFile;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;

/**
 * pályaszerkezet
 *
 * Created on 2008. március 1., 12:19
 *
 * @author Kis Balázs kilobyte@freemail.hu
 */
public final class MapConstruction
{
    public static final short ARENA_WIDTH  = 128; // pálya szélessége
    public static final short ARENA_HEIGHT = 160; // pálya magassága

    // háttérképek
    public static String[] backgrounds = {
        "bg1.png",
        "bg2.jpg",
        "bg3.jpg",
        "bg4.jpg",
        "bg5.jpg"
    }; // mindig a lista végére irj!

    private final ArrayList<Baseitem> list; // ez a lista tartja nyilván a pályaelemeket (lufi, madár, csillag....)
    private byte backgroundIndex;
    public BufferedImage backgroundIndexImg;
    private final Graphics at; // mire rajzolunk
    private int backgroundOffsetX;
    private final Animation waterAnim;
    private int waterX;

    /**
     * konstruktor
     */
    public MapConstruction(Graphics at)
    {
        this.at = at;
        backgroundOffsetX = 0;
        setBackgroundIndex((byte)0);
        list = new ArrayList<Baseitem>();
        waterAnim = new Animation("Gfx/water.gif", 15,4, 4);
    }

    /**
     * pálya betöltés fájlból
     */
    public boolean loadFromStream(DataInputStream str)
    {
        boolean hasBeenCleared = false;
        try
        {
            setBackgroundIndex(str.readByte());
            while(str.available() != 0)
            {
                String s = str.readUTF();
                Baseitem obj = getObjectByName(s);
                if (obj == null)
                {
                    // talán logikailag nem a legjobb, hogy ittvan, de úgy helyes, ha ilyenkor rögtön kivág
                    JOptionPane.showMessageDialog(GeneralFile.comp, s+" nincs meg!", "", JOptionPane.INFORMATION_MESSAGE);
                    System.exit(1);
                    return false;
                }
                if (!hasBeenCleared) // ezt azért kellett igy, mert ha esetleg nincs meg a fájl, akkoris törölné a meglévõ tartalmát
                {
                    clear();
                    backgroundOffsetX = 0;
                    waterX = 0;
                    hasBeenCleared = true;
                }
                obj.loadFromStream(str);
                add(obj);
            }
        }
        catch(IOException ex)
        {
            JOptionPane.showMessageDialog(GeneralFile.comp, "Exception: "+ex.getMessage(), "", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }

        return true;
    }

    /**
     * hullámzás eltolása, végtelenitése
     */
    public void moveWater()
    {
        waterX++;
        if (waterX >= waterAnim.getWidth())
        {
            waterX = 0;
        }
    }

    /**
     * háttérkép beállitása "backgrounds" tömb indexére
     */
    public void setBackgroundIndex(byte index)
    {
        backgroundIndex = index;
        backgroundIndexImg = FileStorage.loadPicture("Gfx/" + MapConstruction.backgrounds[backgroundIndex]);
    }

    /**
     * beállitott háttérkép indexének lekérdezése
     */
    public byte getBackgroundIndex()
    {
        return backgroundIndex;
    }

    /**
     * elem hozzáadása a pályához
     */
    public void add(Baseitem obj)
    {
        list.add(obj);
    }

    /**
     * pályaelemek animálása
     */
    public void updateAnims()
    {
        // mivel egyes elemek törlõdhetnek, vagy akár újjak is létrejöhetnek, ezért itt a "for (Baseitem item : list)" megoldás kivételt dob!
        for(int i = 0; i < list.size(); i++)
        {
            if (list.get(i).getDeleteItself())
            {
                delete(i);
                i--;
            }
            else
            {
                list.get(i).updateAnims();
                list.get(i).updateScrolling();
            }
        }
        waterAnim.update();
    }

    /**
     * a képernyõrõl "kigördült" elemek törlése, takarékossági okokból
     */
    public void checkAndDeleteItemsLeftScreen()
    {
        int w;
        for(int i = 0; i < list.size(); i++)
        {
            if (list.get(i) instanceof VisualItem)
            {
                w = ((VisualItem)list.get(i)).getCacheW();
            }
            else
            {
                w = 0;
            }
            if (((Baseitem) list.get(i)).getX() + w <= 0 || list.get(i).getY() > MapConstruction.ARENA_HEIGHT)
            {
                delete(i);
                i--;
            }
        }
    }

    /**
     * háttérképek kirajzolása
     */
    public void drawBackground()
    {
        at.drawImage(backgroundIndexImg, backgroundOffsetX-backgroundIndexImg.getWidth(),0, null);
        at.drawImage(backgroundIndexImg, backgroundOffsetX,0, null);
        at.drawImage(backgroundIndexImg, backgroundOffsetX+backgroundIndexImg.getWidth(),0, null);
    }

    /**
     * elemek kirajzolása
     */
    public void draw()
    {
        for(Baseitem item : list)
        {
            if (item instanceof VisualItem)
            {
                ((VisualItem)item).draw(at);
            }
        }
    }

    /**
     * viz renderelése
     */
    public void drawWater()
    {
        int x = 0;
        while(x <= ARENA_WIDTH+waterAnim.getWidth())
        {
            waterAnim.draw(at, x-waterX,145);
            x += waterAnim.getWidth();
        }
    }

    /**
     * háttérkép görgetése, végtelenitése
     */
    public void offsetBackgroundX(int with)
    {
        backgroundOffsetX = backgroundOffsetX + with;
        if (backgroundOffsetX + backgroundIndexImg.getWidth() < ARENA_WIDTH)
        {
            backgroundOffsetX = backgroundOffsetX + backgroundIndexImg.getWidth();
        }
        if (backgroundOffsetX - backgroundIndexImg.getWidth() > 0)
        {
            backgroundOffsetX = backgroundOffsetX - backgroundIndexImg.getWidth();
        }
    }

    /**
     * az elemek X irányú eltolásának beállitása.
     * nem biztos, hogy fizikailag el kell tolni õket, igy is lehet görgetni
     */
    public void setOffsetX(int value)
    {
        for(Baseitem item : list)
        {
            if (item instanceof VisualItem)
            {
                ((VisualItem)item).setOffsetX(value);
            }
        }
    }

    /**
     * a "rect" téglalap által lefedett (ütközött) objektumok ütközésviselkedésének lekérdezése (több is lehet)
     */
    public Map<Integer, Collision> getAllImpactEffects(Rectangle rect)
    {
        Map<Integer, Collision> output = new HashMap<Integer, Collision>();
        for(int i = 0; i < list.size(); i++)
        {
            Baseitem item = get(i);
            if (item instanceof VisualItem)
            {
                if (((VisualItem)item).doesImpact(rect)) // találat
                {
                    output.put(i, ((VisualItem)get(i)).effectOnCollision());
                }
            }
        }
        return output;
    }

    /**
     * pályaelem lekérdezése
     */
    public Baseitem get(int index)
    {
        return (Baseitem) list.get(index);
    }

    /**
     * pályaelem törlése
     */
    public void delete(int index)
    {
        list.remove(index);
    }

    /**
     * hány pályaelemünk van?
     */
    public int count()
    {
        return list.size();
    }

    /**
     * hány itemClassname nevû objektumunk van?
     */
    public int getCountOfThisItem(String itemClassname)
    {
        int result = 0;
        for(Baseitem item : list)
        {
            if (item.getClass().getSimpleName().equals(itemClassname))
            {
                result++;
            }
        }
        return result;
    }

    /**
     * pályaelemek eltávolitása
     */
    public void clear()
    {
        list.clear();
    }

    /**
     * létrehozunk egy "className" nevû objektumot, ha nincs ilyen osztályunk, akkor null-t adunk vissza.
     */
    public static Baseitem getObjectByName(String className)
    {
        try
        {
            Class cl = Class.forName(Baseitem.PACKAGE_OF_ITEMS+"."+className);
            if (cl == null)
            {
                throw new Exception(className + " nincs meg!");
            }
            Constructor con = cl.getConstructor();
            return (Baseitem)con.newInstance();
        }
        catch(Exception ex)
        {
            return null;
        }
    }

    /**
     * pálya elmentése fájlba
     */
    public void saveToStream(DataOutputStream file)
    {
        try {
            file.writeByte(backgroundIndex);
            for(Baseitem item : list)
            {
                if (item instanceof INoSaveable)
                {
                    continue;
                }
                file.writeUTF(item.getClass().getSimpleName());
                item.saveToStream(file);
            }
        } catch(IOException ex)
        {
            JOptionPane.showMessageDialog(GeneralFile.comp, "Exception: "+ex.getMessage(), "", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * pályatartalom szöveges formátumban való legenerálása,
     * arra az eseben, ha kódban szeretnénk (majd) elrejteni a pályát
     */
    public void saveToText(PrintWriter fajl)
    {
        for(int i = 0; i < count(); i++)
        {
            String s = get(i).getClass().getSimpleName();
            if (s.equals("Fish"))
            {
                fajl.println(".add(new Fish("+get(i).getX()+"));");
            }
            else
            {
                if (s.equals("Island"))
                {
                    fajl.println(".add(new Island("+get(i).getX()+"));");
                }
                else
                {
                    fajl.println(".add(new "+get(i).getClass().getSimpleName()+"("+get(i).getX()+","+get(i).getY()+"));");
                }
            }
        }
    }
}