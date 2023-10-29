package piscine;

import java.time.LocalDate;
import java.time.LocalDateTime;

import dao.Connexion;
import dao.CoursDAO;
import dao.EmployeDAO;
import dao.OffreDAO;
import dao.PiscineDAO;
import dao.TicketDAO;
import dao.UtilisationDAO;

public class TestDAO {

	public static void main(String[] args) {

		//--------------------------------TEST CONNEXION-----------------------------------------
		//		Connexion.getInstance();
		//		Connexion.fermer();

		//------------------------------Creation de donnees pour l'exam-----------------------------------------
		//set nombre utilisations de l'offre duo à 20 au lieu de 10
		Offre offre = OffreDAO.getInstance().read(2);
		offre.setNbUtilisations(20);
		OffreDAO.getInstance().update(offre);
		//------------les cours-----------
		//Cours classique
		Cours cours1 = new Cours(LocalDateTime.parse("2023-08-22T09:00"),LocalDateTime.parse("2023-08-22T10:30"),EmployeDAO.getInstance().read(1),PiscineDAO.getInstance().read(1),10);
		CoursDAO.getInstance().create(cours1);
		Cours cours2 = new Cours(LocalDateTime.parse("2023-09-20T10:00"),LocalDateTime.parse("2023-09-20T11:00"),EmployeDAO.getInstance().read(1),PiscineDAO.getInstance().read(2),10);
		CoursDAO.getInstance().create(cours2);
		Cours cours3 = new Cours(LocalDateTime.parse("2023-06-15T11:00"),LocalDateTime.parse("2023-06-15T12:00"),EmployeDAO.getInstance().read(2),PiscineDAO.getInstance().read(1),10);
		CoursDAO.getInstance().create(cours3);
		Cours cours4 = new Cours(LocalDateTime.parse("2023-11-22T08:00"),LocalDateTime.parse("2023-08-22T10:00"),EmployeDAO.getInstance().read(3),PiscineDAO.getInstance().read(2),10);
		CoursDAO.getInstance().create(cours4);

		//Cours dans x temps, exemple : 1 semaine, cours de 2 heures
		Cours coursSoon = new Cours(LocalDateTime.now().plusWeeks(1).plusHours(16),LocalDateTime.now().plusWeeks(1).plusHours(18),EmployeDAO.getInstance().read(2),PiscineDAO.getInstance().read(1),10);
		CoursDAO.getInstance().create(coursSoon);

		//Cours pas affiche dans la liste (<ajd)
		Cours coursVieux = new Cours(LocalDateTime.parse("2023-01-03T08:30"),LocalDateTime.parse("2023-03-01T10:30"),EmployeDAO.getInstance().read(2),PiscineDAO.getInstance().read(1),10);
		CoursDAO.getInstance().create(coursVieux);

		//Cours pas affiche dans la liste (pas de places dispo)
		Cours coursSansPlaces = new Cours(LocalDateTime.parse("2023-07-01T08:30"),LocalDateTime.parse("2023-07-01T10:30"),EmployeDAO.getInstance().read(2),PiscineDAO.getInstance().read(1),0);
		CoursDAO.getInstance().create(coursSansPlaces);

		//------------les tickets-----------
		Offre offre1 = OffreDAO.getInstance().read(1);
		Offre offre2 = OffreDAO.getInstance().read(2);
		Offre offre3 = OffreDAO.getInstance().read(3);
		LocalDate ldNow = LocalDate.now();
		LocalDateTime ldtNow = LocalDateTime.now();
		//------------ticket solo-----------
		Ticket ticketOffre1 = new Ticket("b9c10c16-abe4-4f8f-ba63-bc661b8a5ac8", offre1, LocalDate.now(), LocalDate.now().plusDays(offre1.getValidite()));
		TicketDAO.getInstance().create(ticketOffre1);
		//------------ticket duo-----------
		Ticket ticketOffre2 = new Ticket("eb5de67b-ab0a-49a5-8bd6-621c8397c4fb", offre2, LocalDate.now(), LocalDate.now().plusDays(offre2.getValidite()));
		TicketDAO.getInstance().create(ticketOffre2);
		//------------ticket cours-----------
		Ticket ticketOffre3 = new Ticket("60183dc5-a955-4c05-8d18-533c60a4cac2", offre3, ldNow, LocalDate.now().plusDays(offre3.getValidite()));
		TicketDAO.getInstance().create(ticketOffre3);
		//------------ticket solo perime-----------
		Ticket ticketSoloPerime = new Ticket("12068517-cfc6-40f6-b63e-43d473bf8bab", offre1, ldNow, ldNow);
		TicketDAO.getInstance().create(ticketSoloPerime);
		//------------ticket solo solde vide-----------
		Ticket ticketSoloSoldeVide = new Ticket("bc661b8a5ac8-b63e-4c05-ab0a-533c60a4cac2", offre1, ldNow, LocalDate.now().plusDays(offre3.getValidite()));
		TicketDAO.getInstance().create(ticketSoloSoldeVide);
		//------------ticket cours solde vide-----------
		Ticket ticketCoursSoldeVide = new Ticket("eb5de67b-a955-4c05-8d18-b9c10c16", offre3, ldNow, LocalDate.now().plusDays(offre3.getValidite()));
		TicketDAO.getInstance().create(ticketCoursSoldeVide);

		//------------les utilisations-----------
		//------------vider le solde du ticket solo-----------
		for (int i = 0; i < ticketSoloSoldeVide.getOffre().getNbUtilisations(); i++) {
			Utilisation utilSolo = new Utilisation(ldtNow, ticketSoloSoldeVide);
			ldtNow = ldtNow.plusSeconds(5);
			UtilisationDAO.getInstance().create(utilSolo);			
		}
		//------------vider le solde du ticket cours-----------
		for (int i = 0; i < ticketCoursSoldeVide.getOffre().getNbUtilisations(); i++) {
			Utilisation utilCours = new Utilisation(ldtNow, ticketCoursSoldeVide);
			UtilisationDAO.getInstance().create(utilCours);	
		}
		
		System.out.println("Les données de la bd ont été créées");
		
		
		//------------------------------PISCINE-----------------------------------------
		//Read
		//		System.out.println(PiscineDAO.getInstance().read(1));

		//Create 
		//		Piscine pis = new Piscine("Test_create",AdresseDAO.getInstance().read(2));
		//		PiscineDAO.getInstance().create(pis);
		//		System.out.println(pis);

		//Update 
		//		Piscine pis = PiscineDAO.getInstance().read(3);
		//		System.out.println(pis);
		//		pis.setNom("Test2");
		//		PiscineDAO.getInstance().update(pis);
		//		System.out.println(pis);

		//Delete 
		//		Piscine pis = PiscineDAO.getInstance().read(3);
		//		PiscineDAO.getInstance().delete(pis);


		//-------------------------------TICKET-----------------------------------------		
		//Read 
		//		System.out.println(TicketDAO.getInstance().read("eb5de67b-ab0a-49a5-8bd6-621c8397c4fb"));

		//Create 		
		//		Ticket ticket = new Ticket("eb5de67b-ab0a-49a5-8bd6-621c8397c4fb",OffreDAO.getInstance().read(2),LocalDate.now(), LocalDate.now().plusDays(300));
		//		TicketDAO.getInstance().create(ticket);
		//		System.out.println(ticket);

		//Update 
		//		Ticket ticket = TicketDAO.getInstance().read("eb5de67b-ab0a-49a5-8bd6-621c8397c4fb");
		//		ticket.setDate_achat(LocalDate.parse("2023-10-11"));
		//		ticket.setDate_exp(LocalDate.parse("2024-10-11"));
		//		TicketDAO.getInstance().update(ticket);
		//		System.out.println(ticket);

		//Delete 
		//		Ticket ticket = TicketDAO.getInstance().read("eb5de67b-ab0a-49a5-8bd6-621c8397c4fb");
		//		System.out.println(ticket);
		//		TicketDAO.getInstance().delete(ticket);

		//AjouterParticipant
		//		TicketDAO.getInstance().ajouterParticipation(CoursDAO.getInstance().read(3), TicketDAO.getInstance().read("eb5de67b-ab0a-49a5-8bd6-621c8397c4fb"));

		//GetNombreParticipant
		//		System.out.println(TicketDAO.getInstance().getNombreParticipant(3));


		//-----------------------------EMPLOYE-----------------------------------------		
		//Read 
		//		System.out.println(EmployeDAO.getInstance().read(5));

		//Create 
		//		Piscine pis = PiscineDAO.getInstance().read(2);
		//		List<Piscine> lesPiscines = new ArrayList<Piscine>();
		//		lesPiscines.add(pis);
		//		Employe emp = new Employe("Testnom","TestPrenom",AdresseDAO.getInstance().read(2),lesPiscines);
		//		EmployeDAO.getInstance().create(emp);
		//		System.out.println(emp);

		//Update 
		//		Employe emp = EmployeDAO.getInstance().read(5);
		//		emp.setNom("NomTest");
		//		emp.setPrenom("PrenomTest");
		//		emp.setAdr(AdresseDAO.getInstance().read(1));
		//		EmployeDAO.getInstance().update(emp);
		//		System.out.println(emp);

		//Delete 
		//		Employe emp = EmployeDAO.getInstance().read(5);
		//		System.out.println(emp);
		//		EmployeDAO.getInstance().delete(emp);

		//ReadAll
		//		System.out.println(EmployeDAO.getInstance().readAll());


		//-----------------------------OFFRE-----------------------------------------		
		//Read 
		//		System.out.println(OffreDAO.getInstance().read(2));

		//Create 
		//		Offre offre = new Offre("NomOffre",30,9.99f,8);
		//		OffreDAO.getInstance().create(offre);
		//		System.out.println(offre);

		//Update 
		//		Offre offre = OffreDAO.getInstance().read(4);
		//		offre.setNom("TestNomOffre");
		//		offre.setValidite(10);;
		//		offre.setTarif(2.99f);;
		//		offre.setNbUtilisations(3);
		//		OffreDAO.getInstance().update(offre);
		//		System.out.println(offre);

		//Delete 
		//		Offre offre = OffreDAO.getInstance().read(4);
		//		System.out.println(offre);
		//		OffreDAO.getInstance().delete(offre);

		//ReadAll
		//		System.out.println(OffreDAO.getInstance().readAll());


		//-----------------------------ADRESSE-----------------------------------------		
		//Read 
		//		System.out.println(AdresseDAO.getInstance().read(3));

		//Create 
		//		Adresse adr = new Adresse("Dallas",75051,13,"Star's avenue");
		//		AdresseDAO.getInstance().create(adr);
		//		System.out.println(adr);

		//Update 
		//		Adresse adr = AdresseDAO.getInstance().read(5);
		//		adr.setVille("Houston");
		//		adr.setCode_postal(75052);
		//		adr.setNum_rue(18);
		//		adr.setRue("Blue Jackaranda");
		//		AdresseDAO.getInstance().update(adr);
		//		System.out.println(adr);

		//Delete 
		//		Adresse adr = AdresseDAO.getInstance().read(5);
		//		System.out.println(adr);
		//		AdresseDAO.getInstance().delete(adr);


		//-----------------------------ADMIN-----------------------------------------		
		//Read 
		//		System.out.println(AdminDAO.getInstance().read(4));

		//Create 
		//		Admin adm = new Admin(EmployeDAO.getInstance().read(3),"M.Arthur","MotDePasse");
		//		AdminDAO.getInstance().create(adm);
		//		System.out.println(adm);

		//Update 
		//		Admin adm = AdminDAO.getInstance().read(3);
		//		adm.setIdentifiant("Test");
		//		adm.setMdp("mdp");
		//		AdminDAO.getInstance().update(adm);
		//		System.out.println(adm);

		//Delete 
		//		Admin adm = AdminDAO.getInstance().read(3);
		//		System.out.println(adm);
		//		AdminDAO.getInstance().delete(adm);

		//ReadFromLogin
		//		System.out.println(AdminDAO.getInstance().readFromLogin("test"));


		//-----------------------------COURS-----------------------------------------		
		//Read 
		//		System.out.println(CoursDAO.getInstance().read(2));

		//Create 
		//		Cours cours = new Cours(LocalDateTime.parse("2023-01-01T09:00"),LocalDateTime.parse("2023-01-01T10:30"),EmployeDAO.getInstance().read(2),PiscineDAO.getInstance().read(1),10);
		//		CoursDAO.getInstance().create(cours);
		//		System.out.println(cours);

		//Update 
		//		Cours cours = CoursDAO.getInstance().read(4);
		//		cours.setHoraireDebut(LocalDateTime.parse("2023-09-20T10:00"));
		//		cours.setHoraireFin(LocalDateTime.parse("2023-09-20T11:00"));
		//		cours.setEmploye(EmployeDAO.getInstance().read(1));
		//		cours.setPiscine(PiscineDAO.getInstance().read(2));
		//		CoursDAO.getInstance().update(cours);
		//		System.out.println(cours);

		//Delete 
		//		Cours cours = CoursDAO.getInstance().read(4);
		//		System.out.println(cours);
		//		CoursDAO.getInstance().delete(cours);

		//ReadAll
		//		System.out.println(CoursDAO.getInstance().readAll());


		//-----------------------------UTILISATION-----------------------------------------		
		//Read 
		//		System.out.println(UtilisationDAO.getInstance().read(LocalDateTime.parse("2023-09-20T10:10"),"eb5de67b-ab0a-49a5-8bd6-621c8397c4fb"));

		//Create 
		//		Utilisation util = new Utilisation(LocalDateTime.parse("2023-09-20T10:10"),TicketDAO.getInstance().read("eb5de67b-ab0a-49a5-8bd6-621c8397c4fb"));
		//		UtilisationDAO.getInstance().create(util);
		//		System.out.println(util);

		//Delete 
		//		Utilisation util = UtilisationDAO.getInstance().read(LocalDateTime.parse("2023-09-20T10:10:00.000"),"eb5de67b-ab0a-49a5-8bd6-621c8397c4fb");
		//		System.out.println(util);
		//		UtilisationDAO.getInstance().delete(util);

		//GetNombreUtilisation
		//		System.out.println(UtilisationDAO.getInstance().getNombreUtilisation("eb5de67b-ab0a-49a5-8bd6-621c8397c4fb"));


		//-----------------------------FIN CONNEXION-----------------------------------------		
		Connexion.fermer(); 

	}

}
