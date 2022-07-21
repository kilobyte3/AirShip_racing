package apps._game.playground.items.elements;

import apps._game.playground.MapConstruction;
import apps._game.playground.items.Collision;
import apps._game.playground.items.VisualItem;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * hal
 *
 * Created on 2008. m�rcius 3., 11:10
 *
 * @author Kis Bal�zs kilobyte@freemail.hu
 */
public class Fish extends VisualItem
{
    private int lastframe;

    @Override
    public void init(int x, int y)
    {
        // mivel alulra ker�l mindig, csak az x param�terre van sz�ks�g�nk,
        // az y figyelmen kiv�l marad. Persze lehet, hogy ezzel a megk�zel�t�ssel nem mindenki �rt egyet.
        super.init(x, 0);
        getAnimationInstance().init("Gfx/fish.gif", 12,5, 3);
        lastframe = getAnimationInstance().getAnimation().getFrame();
        increaseY(MapConstruction.ARENA_HEIGHT - getCacheH() - 10);
    }

    @Override
    public void updateScrolling()
    {
        super.updateScrolling();
        if (getAnimationInstance().getAnimation().getFrame() != lastframe)
        {
            if (getSpeedWhenVisible() != 0)
            {
                increaseX(-5);
            }
            lastframe = getAnimationInstance().getAnimation().getFrame();
        }
    }

    @Override
    public Collision effectOnCollision()
    {
        return new Collision().setObject(this).setLife((short)-1);
    }

    @Override
    public void loadFromStream(DataInputStream f) throws IOException
    {
        setX(f.readInt());
        init(getX(), getY());
    }

    @Override
    public void saveToStream(DataOutputStream f) throws IOException
    {
        f.writeInt(getX());
    }
}