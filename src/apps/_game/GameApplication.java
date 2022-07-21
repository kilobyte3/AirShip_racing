package apps._game;

import apps.FileStorage;
import apps._game.playground.MapConstruction;
import apps._game.playground.Playground;
import general.GeneralFile;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * játék applikáció, globális átfogó (ablak, idõzitõ)
 *
 * @author Kis Balázs kilobyte@freemail.hu
 */
final public class GameApplication extends JFrame implements ActionListener, KeyListener
{
    public final static int TIMER_INTERVAL = 50;
    public enum gameStates { LOADING, MENU, INGAME, THEEND };
    public GameApplication.gameStates gameState;
    public BufferedImage splashPicture;
    private Image canvas = null;
    private Graphics targetSurface;
    public Menu menu;
    private Timer timer1;
    public Playground playground;
    private Fireworks fireworks = null;
    private int fireworkstick = 0;

    /**
     * minden inicializálás a run()-ban van!
     */
    public GameApplication()
    {
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == timer1)
        {
            switch(gameState)
            {
                case MENU :
                    menu.update();
                    break;
                case INGAME :
                    playground.update();
                    break;
                case THEEND :
                    if (fireworks == null)
                    {
                        fireworks = new Fireworks();
                    }
                    if (fireworkstick == 0)
                    {
                        fireworks.generate();
                    }
                    fireworks.update();
                    fireworkstick++;
                    if (fireworkstick >= 16)
                    {
                        fireworkstick = 0;
                    }
                    break;
            }
            repaint();
        }
    }

    @Override
    public void keyTyped(KeyEvent e)
    {
    }

    @Override
    public void keyPressed(KeyEvent e)
    {
        switch(gameState)
        {
            case LOADING : break;
            case MENU    : menu.keyPressed(e); break;
            case INGAME  : playground.keyPressed(e); break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e)
    {
        switch(gameState)
        {
            case INGAME :
                playground.keyRelease(e);
                break;
        }
    }

    public void run()
    {
        GeneralFile.comp = this; // ez kell neki, ronda, de a dialógusablaknak és a médiakövetõnek kell!
        setTitle("Airship racing");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        gameState = gameStates.LOADING;
        getContentPane().add(new JPanel() {
            @Override
            protected void paintComponent(Graphics gr)
            {
                //super.paintComponent(gr); szerintem ez nem kell
                if (targetSurface != null)
                {
                    targetSurface.clearRect(0,0, this.getWidth(),this.getHeight());
                    switch(gameState)
                    {
                        case LOADING :
                            targetSurface.drawImage(splashPicture, 0,0, this);
                            break;
                        case MENU :
                            menu.draw(targetSurface);
                            break;
                        case INGAME :
                            playground.draw();
                            break;
                        case THEEND :
                            targetSurface.drawImage(splashPicture, 0,0, null);
                            targetSurface.setColor(new Color(255,0,0));
                            targetSurface.setFont(new Font("Times New Roman", Font.PLAIN, 36));
                            final FontMetrics fm = targetSurface.getFontMetrics();
                            Rectangle2D area = fm.getStringBounds("GAME", targetSurface);
                            targetSurface.drawString("GAME", (int)(MapConstruction.ARENA_WIDTH - area.getWidth())/2, 60);
                            area = fm.getStringBounds("OVER", targetSurface);
                            targetSurface.drawString("OVER", (int)(MapConstruction.ARENA_WIDTH - area.getWidth())/2, 100);
                            fireworks.draw(targetSurface);
                            break;
                    }

                    // kijelzés, nyújtva
                    gr.drawImage(canvas, 0,0, getContentPane().getWidth(),getContentPane().getHeight(), this);
                }
            }
        });
        pack();
        final Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds(
            (screen.width - MapConstruction.ARENA_WIDTH) / 2,
            (screen.height - MapConstruction.ARENA_HEIGHT) / 2,
            MapConstruction.ARENA_WIDTH+(getWidth()-getContentPane().getWidth()),
            MapConstruction.ARENA_HEIGHT+(getHeight()-getContentPane().getHeight())
        );
        setVisible(true); // ez kell, különben nem hozza létre a Canvast
        canvas = createImage(MapConstruction.ARENA_WIDTH, MapConstruction.ARENA_HEIGHT);
        targetSurface = canvas.getGraphics();
        splashPicture = FileStorage.loadPicture("Gfx/splash.png");
        addKeyListener(this);
        AudioWrapper.play(FileStorage.loadMusic("Musics/intro.mid"));
        timer1 = new Timer(TIMER_INTERVAL, this);
        timer1.start();
        menu = new Menu(this);
        playground = new Playground(targetSurface, this);
        try { Thread.sleep(1000); } catch(InterruptedException ex) { }
        gameState = GameApplication.gameStates.MENU; // betöltés vége, a menübe kerülünk
    }
}