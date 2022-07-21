package apps._game.playground.items.elements;

import apps._game.playground.items.Collision;
import apps._game.playground.items.VisualItem;

/**
 * felhõ
 *
 * @author Kis Balázs kilobyte@freemail.hu
 */
public class Cloud extends VisualItem
{
    @Override
    public void init(int x, int y)
    {
        super.init(x,y);
        getPictureInstance().init("Gfx/cloud.gif");
    }

    @Override
    public Collision effectOnCollision()
    {
        return new Collision().setObject(this).setLendulet(0.24f);
    }
}