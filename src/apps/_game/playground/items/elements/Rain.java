package apps._game.playground.items.elements;

import apps._game.playground.items.Collision;
import apps._game.playground.items.VisualItem;

/**
 * esõfelhõ, önmagában is létezhet, de illik fõlé felhõt is definiálni
 *
 * Created on 2008. március 3., 15:21
 *
 * @author Kis Balázs kilobyte@freemail.hu
 */
public class Rain extends VisualItem
{
    @Override
    public void init(int x, int y)
    {
        super.init(x,y);
        getAnimationInstance().init("Gfx/rain.gif", 30,3, 2);
    }

    @Override
    public Collision effectOnCollision()
    {
        return new Collision().setObject(this).setLendulet(0.44f);
    }
}