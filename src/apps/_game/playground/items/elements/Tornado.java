package apps._game.playground.items.elements;

import apps._game.playground.items.Collision;
import apps._game.playground.items.VisualItem;
import java.util.Random;

/**
 * tornádó
 *
 * Created on 2008. március 3., 11:45
 *
 * @author Kis Balázs kilobyte@freemail.hu
 */
public class Tornado extends VisualItem
{
    @Override
    public void init(int x, int y)
    {
        super.init(x,y);
        getAnimationInstance().init("Gfx/tor.gif", 18,3, 2);
    }

    @Override
    public Collision effectOnCollision()
    {
        final Collision output = new Collision().setObject(this);
        switch(new Random().nextInt(11))
        {
            case 0 : output.setConfuseAirshipX(1); break;
            case 1 : output.setConfuseAirshipX(2); break;
            case 2 : output.setConfuseAirshipX(3); break;
            case 3 : output.setConfuseAirshipY(1); break;
            case 4 : output.setConfuseAirshipY(2); break;
            case 5 : output.setConfuseAirshipY(3); break;
            case 6 : output.setConfuseAirshipX(-1); break;
            case 7 : output.setConfuseAirshipX(-2); break;
            case 8 : output.setConfuseAirshipX(-3); break;
            case 9 : output.setConfuseAirshipY(-1); break;
            case 10: output.setConfuseAirshipY(-2); break;
            case 11: output.setConfuseAirshipY(-3); break;
        }
        return output;
    }
}