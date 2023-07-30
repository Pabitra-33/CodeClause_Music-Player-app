import javax.swing.*;//here we add the swing properties
import java.awt.*;//here we add the awt components
import java.awt.event.ActionEvent; //added action event packages
import java.awt.event.ActionListener;//added actionlistener to my code to do as click
import java.io.File;//for input and output operation
import java.io.IOException;//to handle exceptions
import javax.sound.sampled.*;//to handle sound exception

public class MusicPlayer extends JFrame implements ActionListener { //extending the main class to Jframe
    private JButton browseButton, playButton, pauseButton, stopButton;//added button components
    private JList<String> audioList;//taken a audiolist of Jtype
    private DefaultListModel<String> listModel;//take a listmodel
    private Clip clip; // a clip variable

    public MusicPlayer() { //creating a constructor
        setTitle("Music Streaming Application");//title of our frame
        setSize(400, 300);//setting height and width
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//add default close operation
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        listModel = new DefaultListModel<>();
        audioList = new JList<>(listModel);

        browseButton = new JButton("Browse");
        playButton = new JButton("Play");
        pauseButton = new JButton("Pause");
        stopButton = new JButton("Stop");

        browseButton.addActionListener(this);//refer to current object of same instance
        playButton.addActionListener(this);
        pauseButton.addActionListener(this);
        stopButton.addActionListener(this);

        JPanel controlPanel = new JPanel(new FlowLayout());
        controlPanel.add(playButton);
        controlPanel.add(pauseButton);
        controlPanel.add(stopButton);

        JScrollPane listScrollPane = new JScrollPane(audioList);

        add(browseButton, BorderLayout.NORTH);
        add(listScrollPane, BorderLayout.CENTER);
        add(controlPanel, BorderLayout.SOUTH);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == browseButton) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setMultiSelectionEnabled(true);

            int result = fileChooser.showOpenDialog(this);//to choose file/audio
            if (result == JFileChooser.APPROVE_OPTION) {
                File[] selectedFiles = fileChooser.getSelectedFiles();
                for (File file : selectedFiles) {
                    listModel.addElement(file.getAbsolutePath());
                }
            }
        } else if (e.getSource() == playButton) {
            String selectedAudio = audioList.getSelectedValue();
            if (selectedAudio != null) {
                try {
                    if (clip != null && clip.isOpen()) {
                        clip.close();
                    }

                    AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(selectedAudio));
                    clip = AudioSystem.getClip();
                    clip.open(audioInputStream);
                    clip.start();
                } catch (LineUnavailableException | IOException | UnsupportedAudioFileException ex) {
                    ex.printStackTrace();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select an audio to play.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } else if (e.getSource() == pauseButton) {
            if (clip != null && clip.isOpen() && clip.isRunning()) {
                clip.stop();
            }
        } else if (e.getSource() == stopButton) {
            if (clip != null && clip.isOpen()) {
                clip.stop();
                clip.setFramePosition(0);
            }
        }
    }

    public static void main(String[] args) { //main method
                SwingUtilities.invokeLater(() -> {
            MusicPlayer musicPlayer = new MusicPlayer();//create object of the class
            musicPlayer.setVisible(true);
        });
    }
}
