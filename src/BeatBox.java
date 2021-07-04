import javax.sound.midi.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

public class BeatBox {
    static BeatBox beatBox;
    JPanel labelPanel = new JPanel();
    JPanel checkBoxPanel = new JPanel();
    JPanel buttonPanel = new JPanel();
    String[] labels = {"Bass Drum","Closed Hi-Hat", "Open Hi-Hat", "Acoustic Snare", "Crash Cymbal", "Hand Clap",
                       "High Tom", "Hi Bongo", "Maracas", "Whistle", "Low Conga", "Cowbell", "Vibralap", "Low-mid Tom",
                        "High Agoo", "Open Hi Conga"};
    int[] instruments = {35,42,46,38,49,39,50,60,70,72,64,56,58,47,67,63};
    JCheckBox[] checkBoxes = new JCheckBox[256];
    CheckBoxItemListener checkBoxItemListener = new CheckBoxItemListener();
    StartButtonActionListener startButtonActionListener = new StartButtonActionListener();
    public  static void main (String[] args) {
        beatBox =  new BeatBox();
        beatBox.go();
    }


    void go(){
        beatBox.build_interface();



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
            checkBox.addItemListener(checkBoxItemListener);


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

    void initInstruments(){
        for (int instrument: instruments){

        }
    }


    class CheckBoxItemListener implements ItemListener {
        @Override
        public void itemStateChanged(ItemEvent e) {
            for (int i = beatBox.checkBoxes.length; i>0; i--){
                if (beatBox.checkBoxes[i-1].isSelected()==true) {
                    int position = i % 16;
                    int instrument = i/16;
                    System.out.println(labels[instrument]+position);
                }
            }
        }
    }

    class StartButtonActionListener implements ActionListener{
        Sequencer sequencer;
        Track track;

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

        @Override
        public void actionPerformed(ActionEvent e) {
            try {
                sequencer = MidiSystem.getSequencer();
                sequencer.open();
                Sequence sequence = new Sequence(Sequence.PPQ,4);
                track = sequence.createTrack();
                for (int i = 0; i<instruments.length-1;i++){
                    noteAdd(192,i,instruments[i],100,1);
                }


                noteAdd(144,1,40,100,5);
                noteAdd(128,1,40,100,40);
                noteAdd(144,2,50,100,10);
                noteAdd(128,2,60,100,15);
                noteAdd(144,3,70,100,20);
                noteAdd(128,3,70,100,45);
                noteAdd(144,4,40,100,50);
                noteAdd(128,4,40,100,75);

                sequencer.setSequence(sequence);
                sequencer.setTempoInBPM(220);
                sequencer.start();

                System.out.println("Started");
            } catch (Exception exception) {
                exception.printStackTrace();
            }





        }
    }
}

