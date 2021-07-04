import javax.sound.midi.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.Arrays;

public class BeatBox {
    static BeatBox beatBox;
    JPanel labelPanel = new JPanel();
    JPanel checkBoxPanel = new JPanel();
    JPanel buttonPanel = new JPanel();
    Orchestra orchestra = new Orchestra();
    static JCheckBox[] checkBoxes = new JCheckBox[256];
    StartButtonActionListener startButtonActionListener = new StartButtonActionListener();
    String[] labels = {"Bass Drum","Closed Hi-Hat", "Open Hi-Hat", "Acoustic Snare", "Crash Cymbal", "Hand Clap",
            "High Tom", "Hi Bongo", "Maracas", "Whistle", "Low Conga", "Cowbell", "Vibralap", "Low-mid Tom",
            "High Agoo", "Open Hi Conga"};
    public  static void main (String[] args) {
        beatBox =  new BeatBox();
        beatBox.go();
    }

    void go(){
        beatBox.build_interface();
        orchestra.initInstruments();

    }
    void build_interface(){
        JFrame frame = new JFrame();
        frame.setSize(1200,530);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        frame.getContentPane().add(BorderLayout.WEST, labelPanel);
        frame.getContentPane().add(BorderLayout.CENTER, checkBoxPanel);
        frame.getContentPane().add(BorderLayout.EAST, buttonPanel);

        labelPanel.setLayout( new BoxLayout(labelPanel, BoxLayout.Y_AXIS));
        checkBoxPanel.setLayout(new GridLayout(16,16));

        beatBox.createLabels();
        beatBox.createCheckBoxes();
        beatBox.createButtons();

        frame.pack();
        frame.setVisible(true);
    }

    void createLabels(){
        Font font = new Font("TimesRoman", Font.BOLD, 22);

        for (int i=labels.length; i>0; i--){
            JLabel label = new JLabel(labels[labels.length - i]);
            label.setFont(font);
            labelPanel.add(label);
        }
    }
    void createCheckBoxes(){

       for (int i = checkBoxes.length; i>0; i--){
            JCheckBox checkBox = new JCheckBox();
            checkBoxes[checkBoxes.length-i] = checkBox;
            checkBox.setSelected(false);
            checkBoxPanel.add(checkBox);
        }

    }
    void  createButtons(){
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        Font font = new Font("TimesRoman", Font.BOLD, 22);
        JButton startButton= new JButton("Start");
        JButton stopButton= new JButton("Stop");
        JButton tempoUpButton= new JButton("tempoUp");
        JButton tempoDownButton= new JButton("tempoDown");
        startButton.setFont(font);
        stopButton.setFont(font);
        tempoUpButton.setFont(font);
        tempoDownButton.setFont(font);
        buttonPanel.add(startButton);
        buttonPanel.add(stopButton);
        buttonPanel.add(tempoUpButton);
        buttonPanel.add(tempoDownButton);
        startButton.addActionListener(startButtonActionListener);
    }

    class StartButtonActionListener implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            orchestra.createTrack();
            orchestra.playTrack();


        }
    }
}

class  Orchestra {
    Sequencer sequencer;
    Sequence sequence;
    Track track;
    ArrayList<Integer> instruments ;

    void noteAdd(int comm, int chan, int one, int two, int tick) {
        MidiEvent event = null;
        try {
            ShortMessage a = new ShortMessage();
            a.setMessage(comm,chan, one, two);
            event = new MidiEvent(a, tick);
            track.add(event);

        } catch (Exception e) {
        }
    }


    void initInstruments(){
        try {
            instruments = new ArrayList<Integer> (Arrays.asList(new Integer[] {35, 42, 46, 38, 49, 39, 50, 60, 70, 72, 64, 56, 58, 47, 67, 63}));
            sequencer = MidiSystem.getSequencer();
            sequencer.open();
            sequence = new Sequence(Sequence.PPQ, 4);
            track = sequence.createTrack();
        } catch (Exception exception) {
                    exception.printStackTrace();}
            for (int instrument : instruments ) {
                noteAdd(192, instruments.indexOf(instrument)+1,instrument, 100, 1);
            }
            }


    void createTrack() {
        for (int i=0; BeatBox.checkBoxes.length-1>i;i++){
            if (BeatBox.checkBoxes[i].isSelected()) {
                int position = (i) % 16;
                int instrument = (i)/16;
                int tick = (position)*8+1;
                int note = position+50;
                noteAdd(144, instrument, note,100, tick);
            //    position=(position-1)/8;
                System.out.println("Instrument" +instruments.get(instrument) + ", Note: "+ note+" , Tick: "+ tick);
                noteAdd(128, instrument, note, 100, tick);

            }

        }

                   /*     noteAdd(144, 1, 94,100, 5);
                        noteAdd(128, 1, 40, 100, 40);
                        noteAdd(144, 2, 50, 100, 10);
                        noteAdd(128, 2, 60, 100, 15);
                        noteAdd(144, 3, 70, 100, 20);
                        noteAdd(128, 3, 70, 100, 45);
                        noteAdd(144, 4, 94, 100, 50);
                        noteAdd(128, 4, 40, 100, 75);*/
                    }
   void playTrack() {
       try {
                        sequencer.setSequence(sequence);
                        sequencer.setTempoInBPM(220);
                        sequencer.start();}
       catch (Exception exception) {}
    }

}
