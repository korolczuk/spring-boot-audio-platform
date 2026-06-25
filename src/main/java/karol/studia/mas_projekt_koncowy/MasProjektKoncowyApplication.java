package karol.studia.mas_projekt_koncowy;

import jakarta.transaction.Transactional;
import karol.studia.mas_projekt_koncowy.gui.PlaylistManagerWindow;
import karol.studia.mas_projekt_koncowy.model.*;
import karol.studia.mas_projekt_koncowy.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.LocalDate;
import java.util.EnumSet;
import java.util.Set;

@SpringBootApplication
public class MasProjektKoncowyApplication {

	public static void main(String[] args) {

		System.setProperty("java.awt.headless", "false");

		SpringApplication.run(MasProjektKoncowyApplication.class, args);
	}

	@Bean
	public CommandLineRunner testBazyDanych(
			AppUserRepository appUserRepository,
			MusicCreatorRepository musicCreatorRepository,
			PodcastCreatorRepository podcastCreatorRepository,
			SystemConfigRepository systemConfigRepository,
			SubscriptionRepository subscriptionRepository,
			SongRepository songRepository,
			PodcastEpisodeRepository podcastEpisodeRepository,
			PlaylistRepository playlistRepository,
			PlaylistEntryRepository playlistEntryRepository,
			AudioContentRepository audioContentRepository,
			TransactionTemplate transactionTemplate){
		return args -> transactionTemplate.execute(status -> {

			if (systemConfigRepository.count() > 0) {
				System.out.println("---- The database already contains data, skipping initial initialization ----");
				javax.swing.SwingUtilities.invokeLater(() -> {
					PlaylistManagerWindow window = new PlaylistManagerWindow(appUserRepository, audioContentRepository, playlistRepository);
					window.setVisible(true);
				});
				return null;
			}

			System.out.println("---- Konfiguracja Systemu ----");
			SystemConfig config = SystemConfig.builder()
					.id(1L)
					.build();
			systemConfigRepository.save(config);

			System.out.println("---- Tworzenie Subskrypcji ----");
			StudentSubscription subStudent = StudentSubscription.builder()
					.cardID("111122223333444455556666")
					.studentNumber("s31080")
					.universityName("Polsko-Japońska Akademia Technik Komputerowych")
					.lastVerificationDate(LocalDate.now().minusMonths(2))
					.build();

			FamilySubscription subFamily = FamilySubscription.builder()
					.cardID("999988887777666655554444")
					.numberOfMembers(4)
					.build();

			StandardSubscription subStandard = StandardSubscription.builder()
					.cardID("123412341234123412341234")
					.downloadLimit(100)
					.maxActiveDevices(3)
					.build();

			System.out.println("---- Tworzenie Użytkowników ----");
			AppUser userAdult = AppUser.builder()
					.firstName("Karol")
					.lastName("Kowalski")
					.userName("karol_student")
					.email("karol543@example.com")
					.isAdmin(true)
					.hashPassword("haslo123")
					.birthDate(LocalDate.of(2000, 5, 20))
					.address(Address.builder().city("Warszawa").country("Polska").build())
					.build();
			userAdult.assignSubscription(subStudent);
			appUserRepository.save(userAdult);

			AppUser userMinor = AppUser.builder()
					.firstName("Jan")
					.lastName("Nowak")
					.userName("janek_mlody")
					.email("jan@example.com")
					.hashPassword("haslo321")
					.birthDate(LocalDate.now().minusYears(15))
					.address(Address.builder().city("Kraków").country("Polska").build())
					.build();
			userMinor.assignSubscription(subFamily);
			appUserRepository.save(userMinor);

			AppUser userFree = AppUser.builder()
					.firstName("Anna")
					.lastName("Zaradna")
					.userName("ania_free")
					.email("ania@example.com")
					.hashPassword("haslo111")
					.birthDate(LocalDate.of(1985, 11, 5))
					.address(Address.builder().city("Poznań").country("Polska").build())
					.build();
			appUserRepository.save(userFree);

			System.out.println("---- Tworzenie Twórców ----");
			MusicCreator vocalist = MusicCreator.builder()
					.firstName("Janko")
					.lastName("Muzykant")
					.userName("JankoMuzykant")
					.email("jm@muzyk.pl")
					.hashPassword("haslo1")
					.description("Robie super muze")
					.isVerified(false)
					.mainGenre("Jazz")
					.role(Set.of(MusicCreatorRole.Vocalist))
					.vocalType("Sopran")
					.vocalRange("c1-h3")
					.build();
			musicCreatorRepository.save(vocalist);

			MusicCreator instrumentalist = MusicCreator.builder()
					.firstName("Piotr")
					.lastName("Rubik")
					.userName("Klaskacz")
					.email("piotr@rubik.pl")
					.hashPassword("klasku123")
					.description("Klaszczę i gram")
					.isVerified(true)
					.mainGenre("Classical")
					.role(Set.of(MusicCreatorRole.Instrumentalist))
					.mainInstrument("Fortepian")
					.instrumentFamily("Klawiszowe")
					.build();
			musicCreatorRepository.save(instrumentalist);

			PodcastCreator podcaster = PodcastCreator.builder()
					.firstName("Andrzej")
					.lastName("Gawędziarz")
					.userName("Gawedziarz22")
					.email("podcast@gawedziarz.pl")
					.hashPassword("podcast123")
					.description("Rozmawiam o życiu")
					.isVerified(true)
					.tag(Set.of("Edukacja", "Rozmowy"))
					.mainRecordingLanguage("Polski")
					.typicalUploadDay("Środa")
					.build();
			podcastCreatorRepository.save(podcaster);

			MusicCreator dawidPodsiadlo = MusicCreator.builder()
					.firstName("Dawid")
					.lastName("Podsiadlo")
					.userName("dawid_p")
					.email("dawid@example.com")
					.hashPassword("secret12")
					.description("Polski wokalista i autor tekstów")
					.isVerified(true)
					.role(EnumSet.of(MusicCreatorRole.Vocalist))
					.vocalType("Baryton")
					.vocalRange("G2-C5")
					.mainGenre("Pop")
					.build();
			musicCreatorRepository.save(dawidPodsiadlo);

			PodcastCreator imponderabilia = PodcastCreator.builder()
					.firstName("Karol")
					.lastName("Paciorek")
					.userName("karol_pac")
					.email("karol@example.com")
					.hashPassword("haslo123")
					.description("Twórca wywiadów")
					.isVerified(true)
					.tag(Set.of("Wywiady", "Kultura"))
					.mainRecordingLanguage("Polski")
					.typicalUploadDay("Czwartek")
					.build();
			podcastCreatorRepository.save(imponderabilia);

			System.out.println("---- Tworzenie Utworów (AudioContent) ----");
			Song song1 = Song.builder()
					.title("Poranny Jazz")
					.duration(210)
					.releaseDate(LocalDate.now().minusDays(10))
					.isExplicit(false)
					.genre(Set.of("Jazz", "Chill"))
					.label("Silesia Records")
					.build();
			vocalist.addSong(song1);

			Song song2 = Song.builder()
					.title("Szybkie tempo")
					.duration(185)
					.releaseDate(LocalDate.now().minusDays(2))
					.isExplicit(true)
					.genre(Set.of("Rap"))
					.label("Underground")
					.build();
			vocalist.addSong(song2);

			Song song3 = Song.builder()
					.title("Sonata Jesienna")
					.duration(420)
					.releaseDate(LocalDate.now().minusYears(1))
					.isExplicit(false)
					.genre(Set.of("Classical"))
					.label("Symphony EU")
					.build();
			instrumentalist.addSong(song3);

			Song song4 = Song.builder()
					.title("Małomiasteczkowy")
					.duration(214) // 3 minuty 34 sekundy
					.releaseDate(LocalDate.of(2018, 6, 6))
					.isExplicit(false)
					.genre(Set.of("Pop", "Indie"))
					.label("Sony Music")
					.build();
			dawidPodsiadlo.addSong(song4);

			Song song5 = Song.builder()
					.title("Trofea (Zablokowany za prawa autorskie)")
					.duration(223)
					.releaseDate(LocalDate.of(2019, 5, 24))
					.isExplicit(false)
					.genre(Set.of("Pop"))
					.label("Sony Music")
					.build();
			dawidPodsiadlo.addSong(song5);

			Song song6 = Song.builder()
					.title("Wpadłem tu tylko na chwilę")
					.duration(190)
					.releaseDate(LocalDate.now())
					.isExplicit(true)
					.genre(Set.of("Alternatywa"))
					.label("Niezależna")
					.build();
			dawidPodsiadlo.addSong(song6);

			PodcastEpisode podcast1 = PodcastEpisode.builder()
					.title("Przyszłość sztucznej inteligencji")
					.duration(4500) // 1h 15m
					.releaseDate(LocalDate.of(2023, 11, 10))
					.isExplicit(false)
					.tag(Set.of("Technologia", "AI"))
					.episodeDescription("Długa rozmowa o przyszłości technologii i maszyn.")
					.build();
			imponderabilia.addPodcastEpisode(podcast1);

			PodcastEpisode podcast2 = PodcastEpisode.builder()
					.title("Mroczne sekrety internetu")
					.duration(5400) // 1h 30m
					.releaseDate(LocalDate.of(2023, 11, 17))
					.isExplicit(true)
					.tag(Set.of("Internet", "True Crime"))
					.episodeDescription("Treści drastyczne.")
					.build();
			imponderabilia.addPodcastEpisode(podcast2);


			PodcastEpisode podcast3 = PodcastEpisode.builder()
					.title("Jak programować w Javie")
					.duration(600)
					.releaseDate(LocalDate.now().minusDays(1))
					.isExplicit(false)
					.tag(Set.of("IT", "Java", "Nauka"))
					.episodeDescription("Rozmawiamy o obiektowości.")
					.build();
			podcaster.addPodcastEpisode(podcast3);



			songRepository.save(song1);
			songRepository.save(song2);
			songRepository.save(song3);
			songRepository.save(song4);
			songRepository.save(song5);
			songRepository.save(song6);
			podcastEpisodeRepository.save(podcast1);
			podcastEpisodeRepository.save(podcast2);
			podcastEpisodeRepository.save(podcast3);


			Playlist play1 = Playlist.builder()
					.name("Moje ulubiuone")
					.build();

			Playlist play2 = Playlist.builder()
					.name("Super utworki")
					.description("Nic dodac nic ujac")
					.build();

			Playlist play3 = Playlist.builder()
					.name("Cos do biegania")
					.build();

			Playlist play4 = Playlist.builder()
					.name("Do spania")
					.build();

			Playlist play5 = Playlist.builder()
					.name("Moje ulubione utwory")
					.build();

			userAdult.addPlaylist(play1);
			userAdult.addPlaylist(play3);
			userAdult.addPlaylist(play4);
			userMinor.addPlaylist(play2);
			userMinor.addPlaylist(play5);

			song1.approve(userAdult);
			song2.approve(userAdult);
			song3.approve(userAdult);
			song4.approve(userAdult);
			song5.approve(userAdult);
			song5.ban(userAdult);
			song6.approve(userAdult);

			podcast1.approve(userAdult);
			podcast2.approve(userAdult);
			podcast3.approve(userAdult);


			userAdult.addAudioContentToPlaylist(song2, play1);
			userAdult.addAudioContentToPlaylist(song1, play1);
			userAdult.addAudioContentToPlaylist(song3, play1);
			userMinor.addAudioContentToPlaylist(song1, play2);
			userMinor.addAudioContentToPlaylist(song3, play2);
			userMinor.addAudioContentToPlaylist(song4, play5);
			userMinor.addAudioContentToPlaylist(podcast1, play5);



			javax.swing.SwingUtilities.invokeLater(() -> {
				PlaylistManagerWindow window = new PlaylistManagerWindow(appUserRepository, audioContentRepository, playlistRepository);
				window.setVisible(true);
			});


			return null;

		});
	}



}
