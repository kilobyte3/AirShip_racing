package apps._game.playground.items.elements;

import apps._game.playground.items.Collision;
import apps._game.playground.items.INoSaveable;
import apps._game.playground.items.VisualItem;

/**
 * ledobott k�, a "Bird" ejti, �nmag�ban nem haszn�latos
 *
 * Created on 2008. m�rcius 3., 21:12
 *
 * @author Kis Bal�zs kilobyte@freemail.hu
 */
public class Rock extends VisualItem implements INoSaveable
{
    @Override
    public void init(int x, int y)
    {
        super.init(x,y);
        getPictureInstance().init("Gfx/stone.gif");
    }

    @Override
    public void updateScrolling()
    {
        super.updateScrolling();
        increaseY(4);
    }

    @Override
    public Collision effectOnCollision()
    {
        return new Collision().setObject(this).setLife((short)-1);
    }
}