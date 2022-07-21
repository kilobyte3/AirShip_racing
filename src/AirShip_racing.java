import apps._game.GameApplication;
import apps._mapCreator.MapCreatorApplication;

/**
 * bel�p�si oszt�ly
 *
 * Created on 2008. febru�r 26., 20:50
 * @author Kis Bal�zs kilobyte@freemail.hu
 */
final public class AirShip_racing
{
    public static void main(String[] args)
    {
        /**************************************************
         * itt kell v�lasztani a k�t "alkalmaz�s" k�z�tt: *
         * 0 - maga a j�t�k                               *
         * 1 - egyszer� p�lyaszerkeszt�                   *
         **************************************************/
        switch(0)
        {
            case 0 :
                new GameApplication().run();
                break;
            case 1 :
                new MapCreatorApplication().run();
                break;
        }
    }
}