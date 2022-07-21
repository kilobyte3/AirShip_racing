package apps._game;

import apps._game.playground.MapConstruction;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;

/**
 * t�zij�t�k effekt
 *
 * 2008. m�rcius 12., 20:38
 * @author Kis Bal�zs kilobyte@freemail.hu
 */
public class Fireworks
{
    /**
     * bels� oszt�ly, egy pont leir�s�ra
     */
    private class Point
    {
        private double x,y, addX,addY, addYr;
        private int timeToLive;
        private final Color color;

        /**
         * pont gener�l�sa
         */
        private Point(int x, int y, Color color)
        {
            Random generator = new Random();

            this.x = x;
            this.y = y;
            double d = (generator.nextDouble() * 6) + 2;
            addX = (generator.nextDouble() * d) - (d/2);
            addY = (generator.nextDouble() * d) - (d/2);
            addYr = (generator.nextDouble() * 0.3) + 0.1;
            this.color = color;
            timeToLive = generator.nextInt(10)+20;
        }

        /**
         * kirajzol�s adott fel�letre
         */
        private void draw(Graphics at)
        {
            at.setColor(color);
            if (timeToLive > 0 && timeToLive < 15)
            {
                at.drawRect((int)Math.round(x),(int)Math.round(y), 0,0);
            }
            if (timeToLive >= 15 && timeToLive < 30)
            {
                at.drawRect((int)Math.round(x),(int)Math.round(y), 1,1);
            }
        }

        /**
         * fizika m�k�dtet�se
         */
        private void update()
        {
            x = x + addX;
            y = y + addY;
            addY = addY + addYr;
            timeToLive--;
        }
    }

    private ArrayList<Point> pontok;

    /**
     * konstruktor, inicializ�l�s
     */
    public Fireworks()
    {
        pontok = new <Point>ArrayList();
    }

    /**
     * t�zij�t�k el��llit�sa
     */
    public void generate()
    {
        Random generator = new Random();

        Color color = null;
        switch(generator.nextInt(6))
        {
            case 0 : color = new Color(255,0,0); break;
            case 1 : color = new Color(0,255,0); break;
            case 2 : color = new Color(0,0,255); break;
            case 3 : color = new Color(255,255,255); break;
            case 4 : color = new Color(255,255,0); break;
            case 5 : color = new Color(255,0,255); break;
        }

        int x = generator.nextInt(MapConstruction.ARENA_WIDTH);
        int y = generator.nextInt(MapConstruction.ARENA_HEIGHT);

        for(int i = 0; i <= 64; i++)
        {
            pontok.add(new Point(x,y, color));
        }
    }

    /**
     * �sszes pont �ltet�se
     */
    public void update()
    {
        for(int i = 0; i < pontok.size(); i++)
        {
            pontok.get(i).update();
            if (pontok.get(i).x > MapConstruction.ARENA_WIDTH ||
                pontok.get(i).x < 0 ||
                pontok.get(i).y > MapConstruction.ARENA_HEIGHT ||
                pontok.get(i).timeToLive <= 0)
            {
                pontok.remove(i);
                i--;
            }
        }
    }

    /**
     * megjelenit�s adott fel�letre
     */
    public void draw(Graphics at)
    {
        for(Point pont : pontok)
        {
            pont.draw(at);
        }
    }
}