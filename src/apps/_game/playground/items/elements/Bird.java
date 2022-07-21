package apps._game.playground.items.elements;

import apps.FileStorage;
import apps._game.AudioWrapper;
import apps._game.playground.MapConstruction;
import apps._game.playground.items.Collision;
import apps._game.playground.items.VisualItem;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

/**
 * mad�r, mely k�veket dob�l
 *
 * Created on 2008. m�rcius 3., 17:13
 * @author Kis Bal�zs kilobyte@freemail.hu
 */
public class Bird extends VisualItem
{
    private BufferedImage rockPicture = null;
    private boolean hasGotRock = true;

    @Override
    public void init(int x, int y)
    {
        super.init(x,y, 3,1);
        getAnimationInstance().init("Gfx/grif.gif", 8,4, 3);
        getSoundInstance().init("Sounds/stone.wav");
        rockPicture = FileStorage.loadPicture("Gfx/stone.gif");
    }

    @Override
    public void draw(Graphics at)
    {
        super.draw(at);
        if (hasGotRock)
        {
            at.drawImage(rockPicture, getX()+5+getOffsetX(),getY()+8, null);
        }
    }

    @Override
    public void onUpdate()
    {
        if (hasGotRock && getX() <= MapConstruction.ARENA_WIDTH / 2)
        {
            hasGotRock = false;
            AudioWrapper.play(getSoundInstance().getSound());
            final Rock rock = new Rock();
            rock.init(getX(), getY());
            palyaRef.add(rock);
        }
    }

    @Override
    public Collision effectOnCollision()
    {
        return new Collision().setObject(this);
    }
}