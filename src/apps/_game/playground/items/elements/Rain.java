package apps._game.playground.items.elements;

import apps._game.playground.items.Collision;
import apps._game.playground.items.VisualItem;

/**
 * es�felh�, �nmag�ban is l�tezhet, de illik f�l� felh�t is defini�lni
 *
 * Created on 2008. m�rcius 3., 15:21
 *
 * @author Kis Bal�zs kilobyte@freemail.hu
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