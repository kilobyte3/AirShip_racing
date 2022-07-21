package apps._game.playground.items.elements;

import apps._game.playground.Airship;
import apps._game.playground.items.Collision;
import apps._game.playground.items.VisualItem;

/**
 * �zemanyag
 *
 * Created on 2008. m�rcius 1., 16:24
 * @author Kis Bal�zs kilobyte@freemail.hu
 */
public class Fuel extends VisualItem
{
    @Override
    public void init(int x, int y)
    {
        super.init(x,y);
        getAnimationInstance().init("Gfx/fuel.gif", 18,3, 4);
    }

    @Override
    public Collision effectOnCollision()
    {
        return new Collision().setObject(this).setRemoveable().setAir(Airship.TANK_AMOUNT);
    }
}