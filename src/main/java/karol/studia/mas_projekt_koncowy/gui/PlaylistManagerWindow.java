package karol.studia.mas_projekt_koncowy.gui;

import karol.studia.mas_projekt_koncowy.model.*;
import karol.studia.mas_projekt_koncowy.repository.AppUserRepository;
import karol.studia.mas_projekt_koncowy.repository.AudioContentRepository;
import karol.studia.mas_projekt_koncowy.repository.PlaylistRepository;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class PlaylistManagerWindow extends JFrame {

    private final AppUserRepository appUserRepository;
    private final AudioContentRepository audioContentRepository;
    private final PlaylistRepository playlistRepository;
    private AppUser loggedAppUser;

    private DefaultListModel<String> entriesModel;
    private JList<Playlist> playlistJList;

    public PlaylistManagerWindow(AppUserRepository appUserRepository, AudioContentRepository audioContentRepository,
            PlaylistRepository playlistRepository) {

        this.appUserRepository = appUserRepository;
        this.audioContentRepository = audioContentRepository;
        this.playlistRepository = playlistRepository;

        this.loggedAppUser = appUserRepository.findByUserNameWithPlaylists("janek_mlody")
                .orElseThrow(() -> new RuntimeException("This AppUser doesnt exist!"));

        setTitle("Panel Zarządzania playlistami użytkownika: " + loggedAppUser.getUserName());
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setBackground(Color.LIGHT_GRAY);

        setLayout(new BorderLayout(10, 10));

        getRootPane().setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        DefaultListModel<Playlist> playlistModel = new DefaultListModel<>();

        for (Playlist p : loggedAppUser.getPlaylists()) {
            playlistModel.addElement(p);
        }

        playlistJList = new JList<>(playlistModel);
        playlistJList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                    boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                if (value instanceof Playlist p) {
                    setText("🎧" + p.getName());
                }
                return this;
            }
        });
        playlistJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane playlistScroll = new JScrollPane(playlistJList);
        playlistScroll.setPreferredSize(new Dimension(250, 0));
        playlistScroll.setBorder(BorderFactory.createTitledBorder("Twoje playlisty     "));

        add(playlistScroll, BorderLayout.WEST);

        entriesModel = new DefaultListModel<>();
        JList<String> entriesJList = new JList<>(entriesModel);


        JScrollPane entriesScroll = new JScrollPane(entriesJList);
        entriesScroll.setBorder(BorderFactory.createTitledBorder("Utwory na wybranej playliście     "));
        add(entriesScroll, BorderLayout.CENTER);
        playlistJList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                updateEntriesList();
            }
        });

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBorder(BorderFactory.createTitledBorder("Dodawanie nowego utworu"));

        JComboBox<AudioContent> audioContentComboBox = new JComboBox<>();

        for (AudioContent ac : audioContentRepository.findByStatus(ContentStatus.PUBLISHED)) {
            audioContentComboBox.addItem(ac);
        }

        audioContentComboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
                    boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                if (value instanceof AudioContent ac) {
                    String explicitAnnotation = ac.getIsExplicit() ? "(E)" : "";
                    setText(ac.getTitle() + explicitAnnotation);
                }
                return this;
            }
        });

        JButton addButton = new JButton("Dodaj treść");
        addButton.setForeground(new Color(25, 135, 84));
        bottomPanel.add(new JLabel("Wybierz utwór z bazy:"));
        bottomPanel.add(audioContentComboBox);
        bottomPanel.add(addButton);

        add(bottomPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> {
            Playlist selectedPlaylist = playlistJList.getSelectedValue();
            AudioContent selectedAudioContent = (AudioContent) audioContentComboBox.getSelectedItem();

            if (selectedPlaylist == null) {
                JOptionPane.showMessageDialog(this, "Najpierw wybierz playlistę z lewej strony!", "Ostrzeżenie",
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            try {

                Playlist freshPlaylist = playlistRepository.findByIdWithEntries(selectedPlaylist.getId())
                        .orElseThrow(() -> new RuntimeException("Playlista nie istnieje!"));
                AudioContent freshAudioContent = audioContentRepository
                        .findByIdWithPlaylistEntries(selectedAudioContent.getId())
                        .orElseThrow(() -> new RuntimeException("Utwór nie istnieje!"));

                loggedAppUser.addAudioContentToPlaylist(freshAudioContent, freshPlaylist);
                playlistRepository.save(freshPlaylist);

                updateEntriesList();

                JOptionPane.showMessageDialog(this, "Dodano utwór: " + selectedAudioContent.getTitle(), "Sukces!",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception ex) {
                String msg = ex.getMessage();
                if (msg != null) {
                    if (msg.contains("already on the playlist")) {
                        msg = "Ten utwór znajduje się już na wybranej playliście! Wybierz inny!";
                    } else if (msg.contains("Explicit content is restricted")) {
                        msg = "Przepraszamy, ten utwór oznaczony jest jako wulgarny (E) i nie może zostać dodany przez osobę niepełnoletnią. Wybierz inny!";
                    } else if (msg.contains("cannot edit a playlist that does not belong to you")) {
                        msg = "Nie masz uprawnień, aby edytować cudzą playlistę!";
                    }
                }
                JOptionPane.showMessageDialog(this, msg, "Błąd dodawania", JOptionPane.ERROR_MESSAGE);
            }
        });

        Font font = new Font("SansSerif", Font.PLAIN, 16);
        playlistJList.setFont(font);
        entriesJList.setFont(font);
        audioContentComboBox.setFont(font);
    }

    private void updateEntriesList() {
        entriesModel.clear();

        Playlist selected = playlistJList.getSelectedValue();

        if (selected != null) {
            Playlist freshPlaylist = playlistRepository.findByIdWithEntries(selected.getId()).orElse(null);

            if (freshPlaylist != null) {
                for (PlaylistEntry entry : freshPlaylist.getPlaylistEntry()) {

                    entriesModel.addElement("🎶 " + "'" + entry.getAudioContent().getTitle() + "'-" +
                            entry.getAudioContent().getCreator().getUserName() + "______" +
                            entry.getAudioContent().getFormatedDuration());

                }
            }
        }
    }
}
