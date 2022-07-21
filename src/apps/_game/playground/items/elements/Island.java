package apps._game.playground.items.elements;

import apps._game.playground.MapConstruction;
import apps._game.playground.items.Collision;
import apps._game.playground.items.VisualItem;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * sziget
 *
 * Created on 2008. március 3., 21:54
 *
 * @author Kis Balázs kilobyte@freemail.hu
 */
public class Island extends VisualItem
{
    @Override
    public void init(int x, int y)
    {
        // mivel alulra kerül mindig, csak az x paraméterre van szükségünk,
        // az y figyelmen kivül marad. Persze lehet, hogy ezzel a megközelítéssel nem mindenki ért egyet.
        super.init(x, 0);
        getPictureInstance().init("Gfx/island.gif");
        increaseY(MapConstruction.ARENA_HEIGHT - getCacheH() - 10);
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

    @Override
    public Collision effectOnCollision()
    {
        return new Collision().setObject(this);
    }
}