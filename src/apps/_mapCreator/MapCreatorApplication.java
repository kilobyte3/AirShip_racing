package apps._mapCreator;

import apps._game.GameApplication;
import apps._game.playground.MapConstruction;
import apps._game.playground.items.Baseitem;
import apps._game.playground.items.INoSaveable;
import apps._game.playground.items.VisualItem;
import general.ClassFinderInPackage;
import general.GeneralFile;
import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.Timer;
import javax.swing.filechooser.FileFilter;

/**
 * egyszer� p�lyakre�tor
 *
 * @author Kis Bal�zs kilobyte@freemail.hu
 */
final public class MapCreatorApplication extends JFrame implements ActionListener, MouseMotionListener, MouseListener, AdjustmentListener, AWTEventListener
{
    private Timer timer1;
    private Baseitem actualSelectedItem;
    private int overlapitemIndex = -1;
    private int mx = -128, my = -128;
    private JScrollBar scrollBox1;
    private int scrollBox1prevVal = 0;
    private JPanel drawingSurface;
    private JComboBox comboBox1, comboBox2;
    private MapConstruction map;
    private JButton saveButton, loadButton;
    private JFileChooser openDialog1, saveDialog1;
    private Image targetBuffer;
    private Graphics targetBufferGraphics = null;

    /**
     * minden inicializ�l�s a run()-ban van!
     */
    public MapCreatorApplication()
    {
    }

    /**
     * akci�k
     */
    @Override
    public void actionPerformed(ActionEvent e)
    {
        // kiment�s
        if (e.getSource() == saveButton)
        {
            if (saveDialog1.showSaveDialog(this) == JFileChooser.APPROVE_OPTION)
            {
                String x = saveDialog1.getSelectedFile().getAbsolutePath();
                if (!x.toLowerCase().endsWith(".mar"))
                {
                    x += ".mar";
                }
                try
                {
                    DataOutputStream fajl = new DataOutputStream(new FileOutputStream(x));
                    map.saveToStream(fajl);
                    /*ObjectOutputStream fajl = new ObjectOutputStream(new FileOutputStream(x));
                    fajl.writeObject(map);
                       j� dolog ez a szerializ�lhat�s�g, de sokkal t�bb lemezter�letet ig�nyel, mintha
                       irn�k egy saj�t kiir� elj�r�st. Tov�bb�, ha B�RMIT v�ltoztatok az oszt�lyban, m�r nem fogadja el,
                       sz�val igy nincs �rtelme....*/
                    fajl.close();
                }
                catch(Exception ex)
                {
                    JOptionPane.showMessageDialog(this, "A f�jl �r�sa k�zben hiba t�rt�nt: "+ex.getMessage(), "", JOptionPane.INFORMATION_MESSAGE);
                }
                try
                {
                    PrintWriter txtfajl = new PrintWriter(new FileWriter(x+"T"));
                    map.saveToText(txtfajl);
                    txtfajl.close();
                }
                catch(IOException ex)
                {
                    JOptionPane.showMessageDialog(this, "A f�jl �r�sa k�zben hiba t�rt�nt: "+ex.getMessage(), "", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }

        // beolvas�s
        if (e.getSource() == loadButton)
        {
            if (openDialog1.showOpenDialog(this) == JFileChooser.APPROVE_OPTION)
            {
                DataInputStream str;
                try
                {
                    str = new DataInputStream(new FileInputStream(openDialog1.getSelectedFile().getAbsolutePath()));
                }
                catch(FileNotFoundException ex)
                {
                    str = null;
                    Logger.getLogger(MapCreatorApplication.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (!map.loadFromStream(str))
                {
                    str = null;
                }
                if (str == null)
                {
                    JOptionPane.showMessageDialog(this, "A f�jl megnyit�sa k�zben hiba t�rt�nt", "", JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                for(int i = 0; i < map.count(); i++)
                {
                    // g�rget�sek megakad�lyoz�sa
                    map.get(i).setSpeed(0);
                    map.get(i).setSpeedWhenVisible(0);
                }
                comboBox2.setSelectedIndex(map.getBackgroundIndex());
            }
        }

        // id�zit�
        if (e.getSource() == timer1)
        {
            map.updateAnims();
            repaint();
        }

        // t�rgy kiv�laszt�sa
        if (e.getSource() == comboBox1)
        {
            actualSelectedItem = MapConstruction.getObjectByName(comboBox1.getSelectedItem().toString());
            actualSelectedItem.init();
        }

        // h�tt�rk�p v�laszt�sa
        if (e.getSource() == comboBox2)
        {
            map.setBackgroundIndex((byte)comboBox2.getSelectedIndex());
        }
    }

    /**
     * az eg�r elmozdult
     */
    @Override
    public void mouseMoved(MouseEvent e)
    {
        mx = e.getX();
        my = e.getY();
        overlapitemIndex = -1;
        for(int i = 0; i < map.count(); i++)
        {
            if (map.get(i) instanceof VisualItem)
            {
                if (((VisualItem)map.get(i)).doesImpact(new Rectangle(mx+scrollBox1.getValue(),my, 1,1)))
                {
                    overlapitemIndex = i;
                    break;
                }
            }
        }
        drawingSurface.repaint();
    }

    /**
     * kattint�s
     */
    @Override
    public void mouseClicked(MouseEvent e)
    {
    }

    /**
     * kattint�s
     */
    @Override
    public void mousePressed(MouseEvent e)
    {
        final Baseitem newItem = MapConstruction.getObjectByName(comboBox1.getSelectedItem().toString());
        newItem.init(mx+scrollBox1.getValue(), my);
        map.add(newItem);
        map.setOffsetX(-scrollBox1.getValue());
    }

    /**
     * eg�rgomb elengedve
     */
    @Override
    public void mouseReleased(MouseEvent e)
    {
    }

    /**
     * az eg�r bel�pett a kliens ter�let�re
     */
    @Override
    public void mouseEntered(MouseEvent e)
    {
    }

    /**
     * az eg�r elhagyta a klienst
     */
    @Override
    public void mouseExited(MouseEvent e)
    {
        mx = -128;
        my = -128;
    }

    /**
     * g�rget�s�v
     */
    @Override
    public void adjustmentValueChanged(AdjustmentEvent e)
    {
        if (e.getSource() == scrollBox1)
        {
            map.setOffsetX(-scrollBox1.getValue());
            if (scrollBox1prevVal != scrollBox1.getValue())
            {
                map.offsetBackgroundX(scrollBox1prevVal - scrollBox1.getValue());
                scrollBox1prevVal = scrollBox1.getValue();
            }
        }
    }

    /**
     *
     */
    @Override
    public void mouseDragged(MouseEvent e)
    {
    }

    /**
     * init �s futtat�s
     */
    public void run()
    {
        Baseitem.DO_SCROLLING = false;
        GeneralFile.comp = this;
        setTitle("MapCreator of AirShip racing");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        Dimension Screen = Toolkit.getDefaultToolkit().getScreenSize();
        setLayout(new GridBagLayout());
        getContentPane().add(comboBox1 = new JComboBox());
        comboBox1.addActionListener(this);

        // h�tt�rk�pek
        getContentPane().add(comboBox2 = new JComboBox());
        for(String item : MapConstruction.backgrounds)
        {
            comboBox2.addItem(item);
        }
        comboBox2.addActionListener(this);

        // rajzfel�let
        GridBagConstraints contraints = new GridBagConstraints();
        contraints.fill = GridBagConstraints.NONE;
        contraints.gridx = 0;
        contraints.gridy = 1;
        contraints.weightx = 55;
        contraints.gridwidth = 2;
        getContentPane().add(drawingSurface = new JPanel()
        {
            @Override
            protected void paintComponent(Graphics gr)
            {
                if (map != null && actualSelectedItem != null && targetBufferGraphics != null)
                {
                    map.drawBackground();
                    map.draw();
                    map.drawWater();
                    actualSelectedItem.setX(mx);
                    actualSelectedItem.setY(my);
                    ((VisualItem)actualSelectedItem).draw(targetBufferGraphics);
                    if (overlapitemIndex != -1)
                    {
                        targetBufferGraphics.setColor(new Color(255,128,64));
                        Baseitem o = map.get(overlapitemIndex);
                        targetBufferGraphics.drawRect(o.getX()-scrollBox1.getValue(),o.getY(), ((VisualItem)o).getCacheW(),((VisualItem)o).getCacheH());
                    }
                    targetBufferGraphics.setColor(new Color(255,0,0));
                    targetBufferGraphics.drawRect(actualSelectedItem.getX(),actualSelectedItem.getY(), ((VisualItem)actualSelectedItem).getCacheW(),((VisualItem)actualSelectedItem).getCacheH());

                    gr.drawImage(targetBuffer, 0,0, this);
                }
            }
        }, contraints);
        drawingSurface.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
        drawingSurface.setPreferredSize(new Dimension(MapConstruction.ARENA_WIDTH, MapConstruction.ARENA_HEIGHT));
        drawingSurface.addMouseMotionListener(this);
        drawingSurface.addMouseListener(this);

        // g�rget�s�v
        contraints.fill = GridBagConstraints.NONE;
        contraints.gridx = 0;
        contraints.gridy = 2;
        contraints.ipady = 1;
        contraints.weightx = 0.5;
        contraints.gridwidth = 2;
        getContentPane().add(scrollBox1 = new JScrollBar(JScrollBar.HORIZONTAL, 0,1, 0,256*16), contraints);
        scrollBox1.setPreferredSize(new Dimension(256,16));
        scrollBox1.addAdjustmentListener(this);

        // p�lya ment�se gomb
        contraints.fill = GridBagConstraints.NONE;
        contraints.ipady = 1;
        contraints.gridx = 0;
        contraints.gridy = 3;
        contraints.weightx = 0.5;
        contraints.gridwidth = 1;
        getContentPane().add(saveButton = new JButton("Save map"), contraints);
        saveButton.addActionListener(this);
        saveDialog1 = new JFileChooser();
        saveDialog1.setCurrentDirectory(new File("."));
        saveDialog1.setFileFilter(new FileFilter()
        {
            @Override
            public boolean accept(File f)
            {
                return f.getName().toLowerCase().endsWith(".mar") || f.isDirectory();
            }

            @Override
            public String getDescription()
            {
                return "*.mar - Map of Airship Racing files";
            }
        });
        contraints.fill = GridBagConstraints.NONE;
        contraints.gridwidth = 1;
        contraints.gridx = 1;
        contraints.gridy = 3;
        contraints.weightx = 0.5;
        contraints.gridwidth = 1;

        // p�lya bet�lt�se gomb
        getContentPane().add(loadButton = new JButton("Load map"), contraints);
        loadButton.addActionListener(this);
        openDialog1 = new JFileChooser();
        openDialog1.setCurrentDirectory(new File("."));
        openDialog1.setFileFilter(new FileFilter()
        {
            @Override
            public boolean accept(File f)
            {
                return f.getName().toLowerCase().endsWith(".mar") || f.isDirectory();
            }
            @Override
            public String getDescription()
            {
                return "*.mar - Map of Airship Racing files";
            }
        });

        pack();
        setBounds((Screen.width - getWidth()) / 2, (Screen.height - getHeight()) / 2, getWidth(),getHeight());
        setResizable(false);
        setVisible(true);
        targetBuffer = createImage(MapConstruction.ARENA_WIDTH, MapConstruction.ARENA_HEIGHT);
        targetBufferGraphics = targetBuffer.getGraphics();
        // FONTOS!!! Itt fel kell jegyezni. Valamilyen megmagyar�zhatatlan ok miatt, ha
        // "targetBufferGraphics"-et t�rl�m, �s mindenhov� "targetBuffer.getGraphics()" ker�l, nem m�k�dik!!! Sajnos teh�t
        // nem tudunk egy v�ltoz�t sp�rolni. Tov�bb� a show() ut�n szabad csak haszn�lni

        map = new MapConstruction(targetBufferGraphics);
        Baseitem.palyaRef = map;

        // most hozz�adjuk a p�lya�pit�sben r�sztvehet� elemeket
        for(String className : ClassFinderInPackage.getClassesInPackage(Baseitem.PACKAGE_OF_ITEMS))
        {
            try
            {
                if (INoSaveable.class.isAssignableFrom(Class.forName(Baseitem.PACKAGE_OF_ITEMS+"."+className)))
                {
                    continue;
                }
            }
            catch(ClassNotFoundException ex)
            {
                JOptionPane.showMessageDialog(this, className+" oszt�ly nincs meg, bels� hiba!", "", JOptionPane.INFORMATION_MESSAGE);
                System.exit(1);
            }

            if (actualSelectedItem == null)
            {
                actualSelectedItem = MapConstruction.getObjectByName(className);
                actualSelectedItem.init();
            }
            comboBox1.addItem(className);
        }

        actionPerformed(new ActionEvent(comboBox2, ActionEvent.ACTION_PERFORMED, null));
        timer1 = new Timer(GameApplication.TIMER_INTERVAL, this);
        timer1.start();
        this.getToolkit().addAWTEventListener(this, AWTEvent.KEY_EVENT_MASK);
    }

    /**
     * "glob�lis" billenty�kezel�s, mert lehet, hogy egy komponens f�kuszban van, �s akkor csak neki sz�l az �zenet
     */
    @Override
    public void eventDispatched(AWTEvent event)
    {
        if (event instanceof KeyEvent)
        {
            KeyEvent key = (KeyEvent)event;
            if (key.getID() == KeyEvent.KEY_PRESSED)
            {
                // Handle key presses
                if (overlapitemIndex != -1 && key.getKeyCode() == KeyEvent.VK_DELETE)
                {
                    map.delete(overlapitemIndex);
                    overlapitemIndex = -1;
                }
                key.consume();
            }
        }
    }
}