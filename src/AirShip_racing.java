import apps._game.GameApplication;
import apps._mapCreator.MapCreatorApplication;

/**
 * belépési osztály
 *
 * Created on 2008. február 26., 20:50
 * @author Kis Balázs kilobyte@freemail.hu
 */
final public class AirShip_racing
{
    public static void main(String[] args)
    {
        /**************************************************
         * itt kell választani a két "alkalmazás" között: *
         * 0 - maga a játék                               *
         * 1 - egyszerü pályaszerkesztõ                   *
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