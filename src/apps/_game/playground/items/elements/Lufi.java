package apps._game.playground.items.elements;

import apps._game.playground.items.Collision;
import apps._game.playground.items.VisualItem;

/**
 * lufi
 *
 * Created on 2008. március 2., 17:20
 *
 * @author Kis Balázs kilobyte@freemail.hu
 */
public class Lufi extends VisualItem
{
    @Override
    public void init(int x, int y)
    {
        super.init(x,y);
        getAnimationInstance().init("Gfx/score.gif", 18,4, 8);
    }

    @Override
    public Collision effectOnCollision()
    {
        return new Collision().setObject(this).setRemoveable().setBonusPoint(150);
    }
}