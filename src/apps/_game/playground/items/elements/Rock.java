package apps._game.playground.items.elements;

import apps._game.playground.items.Collision;
import apps._game.playground.items.INoSaveable;
import apps._game.playground.items.VisualItem;

/**
 * ledobott kõ, a "Bird" ejti, önmagában nem használatos
 *
 * Created on 2008. március 3., 21:12
 *
 * @author Kis Balázs kilobyte@freemail.hu
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