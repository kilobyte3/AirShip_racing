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
 * p�lyaszerkezet
 *
 * Created on 2008. m�rcius 1., 12:19
 *
 * @author Kis Bal�zs kilobyte@freemail.hu
 */
public final class MapConstruction
{
    public static final short ARENA_WIDTH  = 128; // p�lya sz�less�ge
    public static final short ARENA_HEIGHT = 160; // p�lya magass�ga

    // h�tt�rk�pek
    public static String[] backgrounds = {
        "bg1.png",
        "bg2.jpg",
        "bg3.jpg",
        "bg4.jpg",
        "bg5.jpg"
    }; // mindig a lista v�g�re irj!

    private final ArrayList<Baseitem> list; // ez a lista tartja nyilv�n a p�lyaelemeket (lufi, mad�r, csillag....)
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
     * p�lya bet�lt�s f�jlb�l
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
                    // tal�n logikailag nem a legjobb, hogy ittvan, de �gy helyes, ha ilyenkor r�gt�n kiv�g
                    JOptionPane.showMessageDialog(GeneralFile.comp, s+" nincs meg!", "", JOptionPane.INFORMATION_MESSAGE);
                    System.exit(1);
                    return false;
                }
                if (!hasBeenCleared) // ezt az�rt kellett igy, mert ha esetleg nincs meg a f�jl, akkoris t�r�ln� a megl�v� tartalm�t
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
     * hull�mz�s eltol�sa, v�gtelenit�se
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
     * h�tt�rk�p be�llit�sa "backgrounds" t�mb index�re
     */
    public void setBackgroundIndex(byte index)
    {
        backgroundIndex = index;
        backgroundIndexImg = FileStorage.loadPicture("Gfx/" + MapConstruction.backgrounds[backgroundIndex]);
    }

    /**
     * be�llitott h�tt�rk�p index�nek lek�rdez�se
     */
    public byte getBackgroundIndex()
    {
        return backgroundIndex;
    }

    /**
     * elem hozz�ad�sa a p�ly�hoz
     */
    public void add(Baseitem obj)
    {
        list.add(obj);
    }

    /**
     * p�lyaelemek anim�l�sa
     */
    public void updateAnims()
    {
        // mivel egyes elemek t�rl�dhetnek, vagy ak�r �jjak is l�trej�hetnek, ez�rt itt a "for (Baseitem item : list)" megold�s kiv�telt dob!
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
     * a k�perny�r�l "kig�rd�lt" elemek t�rl�se, takar�koss�gi okokb�l
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
     * h�tt�rk�pek kirajzol�sa
     */
    public void drawBackground()
    {
        at.drawImage(backgroundIndexImg, backgroundOffsetX-backgroundIndexImg.getWidth(),0, null);
        at.drawImage(backgroundIndexImg, backgroundOffsetX,0, null);
        at.drawImage(backgroundIndexImg, backgroundOffsetX+backgroundIndexImg.getWidth(),0, null);
    }

    /**
     * elemek kirajzol�sa
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
     * viz renderel�se
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
     * h�tt�rk�p g�rget�se, v�gtelenit�se
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
     * az elemek X ir�ny� eltol�s�nak be�llit�sa.
     * nem biztos, hogy fizikailag el kell tolni �ket, igy is lehet g�rgetni
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
     * a "rect" t�glalap �ltal lefedett (�tk�z�tt) objektumok �tk�z�sviselked�s�nek lek�rdez�se (t�bb is lehet)
     */
    public Map<Integer, Collision> getAllImpactEffects(Rectangle rect)
    {
        Map<Integer, Collision> output = new HashMap<Integer, Collision>();
        for(int i = 0; i < list.size(); i++)
        {
            Baseitem item = get(i);
            if (item instanceof VisualItem)
            {
                if (((VisualItem)item).doesImpact(rect)) // tal�lat
                {
                    output.put(i, ((VisualItem)get(i)).effectOnCollision());
                }
            }
        }
        return output;
    }

    /**
     * p�lyaelem lek�rdez�se
     */
    public Baseitem get(int index)
    {
        return (Baseitem) list.get(index);
    }

    /**
     * p�lyaelem t�rl�se
     */
    public void delete(int index)
    {
        list.remove(index);
    }

    /**
     * h�ny p�lyaelem�nk van?
     */
    public int count()
    {
        return list.size();
    }

    /**
     * h�ny itemClassname nev� objektumunk van?
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
     * p�lyaelemek elt�volit�sa
     */
    public void clear()
    {
        list.clear();
    }

    /**
     * l�trehozunk egy "className" nev� objektumot, ha nincs ilyen oszt�lyunk, akkor null-t adunk vissza.
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
     * p�lya elment�se f�jlba
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
     * p�lyatartalom sz�veges form�tumban val� legener�l�sa,
     * arra az eseben, ha k�dban szeretn�nk (majd) elrejteni a p�ly�t
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