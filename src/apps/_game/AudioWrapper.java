package apps._game;

import java.applet.AudioClip;
import java.net.URL;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequencer;

/**
 * audio (hang, zene) becsomagol�sa, csup�n az�rt, hogy az enged�lyezetts�g vizsg�l�s�t ne kelljen mindenhol elv�gezni
 *
 * @author Kis Bal�zs kilobyte@freemail.hu
 */
public final class AudioWrapper
{
    public static boolean enabled = true;
    private static Sequencer seq = null;

    /**
     * hang lej�tsz�sa
     */
    public static void play(AudioClip obj)
    {
        if (enabled)
        {
            obj.play();
        }
    }

    /**
     * zene (MIDI) lej�tsz�sa
     */
    public static void play(URL obj)
    {
        if (enabled)
        {
            try
            {
                if (seq == null)
                {
                    seq = MidiSystem.getSequencer();
                }
                else
                {
                    seq.stop();
                }
                seq.setSequence(MidiSystem.getSequence(obj));
                seq.open();
                seq.start();
            }
            catch(Exception ex) {}
        }
    }
}