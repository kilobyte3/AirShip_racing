package apps._game;

import apps._game.playground.MapConstruction;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;

/**
 * tüzijáték effekt
 *
 * 2008. március 12., 20:38
 * @author Kis Balázs kilobyte@freemail.hu
 */
public class Fireworks
{
    /**
     * belsõ osztály, egy pont leirására
     */
    private class Point
    {
        private double x,y, addX,addY, addYr;
        private int timeToLive;
        private final Color color;

        /**
         * pont generálása
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
         * kirajzolás adott felületre
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
         * fizika mûködtetése
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
     * konstruktor, inicializálás
     */
    public Fireworks()
    {
        pontok = new <Point>ArrayList();
    }

    /**
     * tüzijáték elõállitása
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
     * összes pont éltetése
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
     * megjelenités adott felületre
     */
    public void draw(Graphics at)
    {
        for(Point pont : pontok)
        {
            pont.draw(at);
        }
    }
}