package web.projekat.ProjekatSpring.controller;

import web.projekat.ProjekatSpring.entity.DTO.PrijavaDTO;
import web.projekat.ProjekatSpring.entity.Clan;
import web.projekat.ProjekatSpring.entity.FitnessCentar;
import web.projekat.ProjekatSpring.entity.Sala;
import web.projekat.ProjekatSpring.entity.Termin;
import web.projekat.ProjekatSpring.entity.Trener;
import web.projekat.ProjekatSpring.entity.Trening;
import web.projekat.ProjekatSpring.entity.DTO.TerminDTO;
import web.projekat.ProjekatSpring.service.ClanService;
import web.projekat.ProjekatSpring.service.FitnessCentarService;
import web.projekat.ProjekatSpring.service.SalaService;
import web.projekat.ProjekatSpring.service.TerminService;
import web.projekat.ProjekatSpring.service.TrenerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping(value = "/api/clan")
public class ClanController {

	 private final TerminService terminService;
	 private final FitnessCentarService fitnessCentarService;
	 private final SalaService salaService;
	 private final ClanService clanService;
	// constructor-based dependency injection
	 @Autowired
	 public ClanController(TerminService terminService, FitnessCentarService fitnessCentarService, SalaService salaService, ClanService clanService) {
	    this.terminService = terminService;
	    this.fitnessCentarService = fitnessCentarService;
	    this.salaService = salaService;
	    this.clanService = clanService;	    
     }
	 
	 @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
             produces = MediaType.APPLICATION_JSON_VALUE,
             value = "/prikazTermina")
	 public ResponseEntity<List<TerminDTO>> getNeprijavljeniTermini(@RequestBody TerminDTO getDTO) throws Exception {
		 //System.out.println(id);
		 List<Termin> terminList = this.terminService.findAll();
		 List<TerminDTO> terminDTOS = new ArrayList<>();
		 if(getDTO.getClanID() == null) {
			 return new ResponseEntity<>(terminDTOS, HttpStatus.OK);
		 }
		 Clan clan = this.clanService.findById(getDTO.getClanID());

	     if(getDTO.getZastita() == null && !getDTO.getZastita().equals("clan")) {
	    	 return new ResponseEntity<>(terminDTOS, HttpStatus.OK);
	     }
	     
	     Date compare = new Date();
	     for (Termin termin : terminList) {
	    	 if(!clan.getOdradjeniTermini().contains(termin) && termin.getPocetakTermina().after(compare)) {
	    	 TerminDTO terminDTO = new TerminDTO(termin.getId(), termin.getPocetakTermina(),
	    		termin.getKrajTermina(), termin.getTrajanjeTermina(), termin.getCenaTermina(), termin.getTrening().getNazivTreninga(),
	    		termin.getTrening().getOpis(), termin.getTrening().getTip());
	    	 	terminDTOS.add(terminDTO);
	    	 }
	     }

	     return new ResponseEntity<>(terminDTOS, HttpStatus.OK);
	 }
	 //NE RADI
	 @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
             produces = MediaType.APPLICATION_JSON_VALUE,
             value = "/prijavaTermina")
	 public ResponseEntity<String> prijavaNaTermin(@RequestBody TerminDTO getDTO) throws Exception {
		 Clan clan = this.clanService.findById(getDTO.getClanID());
		 System.out.println("ID clana je " + getDTO.getClanID());
		 System.out.println("Potvrda " + clan.getId());
		 Set<Termin> odradjeni = clan.getOdradjeniTermini();
		 Termin noviTermin = this.terminService.findOne(getDTO.getId());
		 System.out.println("ID termina je " + getDTO.getId());
		 System.out.println("Potvrda " + noviTermin.getId());
		 Set<Clan> clanovi = noviTermin.getClanoviOdradjenih();
		 Sala sala = noviTermin.getSala();
		 if(clanovi.size() < sala.getKapacitet()) {
			 System.out.println("stigao");
			 clanovi.add(clan);
			 odradjeni.add(noviTermin);
			 System.out.println("dodao");
			 /*Iterable<Termin> res1 = this.terminService.saveOdradjeni(odradjeni);
			 Iterable<Clan> res2 = this.clanService.saveClanovi(clanovi);
			 TerminDTO prijavaTermina = new TerminDTO("ok", res1, res2);*/
			 return new ResponseEntity<>(clan.getKorisnickoIme(), HttpStatus.OK);		
		 }
		 //TerminDTO prijavaTermina = new TerminDTO("pun", odradjeni, clanovi);
		 return new ResponseEntity<>("pun", HttpStatus.OK);		 
	 }
	 
	 @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
             produces = MediaType.APPLICATION_JSON_VALUE,
             value = "/prikazPrijavljenih")
	 public ResponseEntity<List<TerminDTO>> getPrijavljeniTermini(@RequestBody TerminDTO getDTO) throws Exception {
		 //System.out.println(id);
		 List<Termin> terminList = this.terminService.findAll();
		 List<TerminDTO> terminDTOS = new ArrayList<>();
		 if(getDTO.getClanID() == null) {
			 return new ResponseEntity<>(terminDTOS, HttpStatus.OK);
		 }
		 Clan clan = this.clanService.findById(getDTO.getClanID());

	     if(getDTO.getZastita() == null && !getDTO.getZastita().equals("clan")) {
	    	 return new ResponseEntity<>(terminDTOS, HttpStatus.OK);
	     }
	     
	     Date compare = new Date();
	     //System.out.println("Trenutni datum je " + compare);
	     
	     for (Termin termin : terminList) {
	    	 if(clan.getOdradjeniTermini().contains(termin) && termin.getPocetakTermina().after(compare)) {
	    		 //System.out.println("Datum održavanja termina je " + termin.getPocetakTermina().after(compare));
	    		 TerminDTO terminDTO = new TerminDTO(termin.getId(), termin.getPocetakTermina(),
	    				 termin.getKrajTermina(), termin.getTrajanjeTermina(), termin.getCenaTermina(), termin.getTrening().getNazivTreninga(),
	    				 termin.getTrening().getOpis(), termin.getTrening().getTip());
	    	 	 terminDTOS.add(terminDTO);
	    	 }
	     }

	     return new ResponseEntity<>(terminDTOS, HttpStatus.OK);
	 }
	 @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
             produces = MediaType.APPLICATION_JSON_VALUE,
             value = "/prikazOdradjenih")
	 public ResponseEntity<List<TerminDTO>> getOdradjeniTermini(@RequestBody TerminDTO getDTO) throws Exception {
		 //System.out.println(id);
		 List<Termin> terminList = this.terminService.findAll();
		 List<TerminDTO> terminDTOS = new ArrayList<>();
		 if(getDTO.getClanID() == null) {
			 return new ResponseEntity<>(terminDTOS, HttpStatus.OK);
		 }
		 Clan clan = this.clanService.findById(getDTO.getClanID());

	     if(getDTO.getZastita() == null && !getDTO.getZastita().equals("clan")) {
	    	 return new ResponseEntity<>(terminDTOS, HttpStatus.OK);
	     }
	     
	     Date compare = new Date();
	     //System.out.println("Trenutni datum je " + compare);
	     
	     for (Termin termin : terminList) {
	    	 if(clan.getOdradjeniTermini().contains(termin) && termin.getPocetakTermina().before(compare)) {
	    		 //System.out.println("Datum održavanja termina je " + termin.getPocetakTermina().after(compare));
	    		 TerminDTO terminDTO = new TerminDTO(termin.getId(), termin.getPocetakTermina(),
	    				 termin.getKrajTermina(), termin.getTrajanjeTermina(), termin.getCenaTermina(), termin.getTrening().getNazivTreninga(),
	    				 termin.getTrening().getOpis(), termin.getTrening().getTip());
	    	 	 terminDTOS.add(terminDTO);
	    	 }
	     }

	     return new ResponseEntity<>(terminDTOS, HttpStatus.OK);
	 }
	 //NE RADI, PROBAJ SAVE ILI UPDATE DODATI
	 @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
             produces = MediaType.APPLICATION_JSON_VALUE,
             value = "/odjavaTermina")
	 public ResponseEntity<String> odjavaTermina(@RequestBody TerminDTO getDTO) throws Exception {
		 Clan clan = this.clanService.findById(getDTO.getClanID());
		 /*System.out.println("ID clana je " + getDTO.getClanID());
		 System.out.println("Potvrda " + clan.getId());*/
		 Set<Termin> odradjeni = clan.getOdradjeniTermini();
		 Termin odjavaTermina = this.terminService.findOne(getDTO.getId());
		 /*System.out.println("ID termina je " + getDTO.getId());
		 System.out.println("Potvrda " + noviTermin.getId());*/
		 Set<Clan> clanovi = odjavaTermina.getClanoviOdradjenih();
		 System.out.println("stigao");
		 clanovi.remove(clan);
		 odradjeni.remove(odjavaTermina);
		 System.out.println("uklonio");
		 /*Iterable<Termin> res1 = this.terminService.saveOdradjeni(odradjeni);
		 Iterable<Clan> res2 = this.clanService.saveClanovi(clanovi);
		 TerminDTO prijavaTermina = new TerminDTO("ok", res1, res2);*/
		 return new ResponseEntity<>(clan.getKorisnickoIme(), HttpStatus.OK);	 
	 }
	 
	 @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
             produces = MediaType.APPLICATION_JSON_VALUE,
             value = "/prikazOcenjenih")
	 public ResponseEntity<List<TerminDTO>> getOcenjeniTermini(@RequestBody TerminDTO getDTO) throws Exception {
		 //System.out.println(id);
		 List<Termin> terminList = this.terminService.findAll();
		 List<TerminDTO> terminDTOS = new ArrayList<>();
		 if(getDTO.getClanID() == null) {
			 return new ResponseEntity<>(terminDTOS, HttpStatus.OK);
		 }
		 Clan clan = this.clanService.findById(getDTO.getClanID());

	     if(getDTO.getZastita() == null && !getDTO.getZastita().equals("clan")) {
	    	 return new ResponseEntity<>(terminDTOS, HttpStatus.OK);
	     }
	     
	     
	     for (Termin termin : terminList) {
	    	 if(clan.getOcenjeniTermini().contains(termin)) {
	    		 //System.out.println("Datum održavanja termina je " + termin.getPocetakTermina().after(compare));
	    		 TerminDTO terminDTO = new TerminDTO(termin.getId(), termin.getPocetakTermina(),
	    				 termin.getKrajTermina(), termin.getTrajanjeTermina(), termin.getCenaTermina(), termin.getTrening().getNazivTreninga(),
	    				 termin.getTrening().getOpis(), termin.getTrening().getTip());
	    	 	 terminDTOS.add(terminDTO);
	    	 }
	     }

	     return new ResponseEntity<>(terminDTOS, HttpStatus.OK);
	 }
	 
	 @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
             produces = MediaType.APPLICATION_JSON_VALUE,
             value = "/prikazNeocenjenih")
	 public ResponseEntity<List<TerminDTO>> getNeocenjeniTermini(@RequestBody TerminDTO getDTO) throws Exception {
		 //System.out.println(id);
		 List<Termin> terminList = this.terminService.findAll();
		 List<TerminDTO> terminDTOS = new ArrayList<>();
		 if(getDTO.getClanID() == null) {
			 return new ResponseEntity<>(terminDTOS, HttpStatus.OK);
		 }
		 Clan clan = this.clanService.findById(getDTO.getClanID());

	     if(getDTO.getZastita() == null && !getDTO.getZastita().equals("clan")) {
	    	 return new ResponseEntity<>(terminDTOS, HttpStatus.OK);
	     }
	     
	     Date compare = new Date();
	     //System.out.println("Trenutni datum je " + compare);
	     
	     for (Termin termin : terminList) {
	    	 if(clan.getOdradjeniTermini().contains(termin) && termin.getPocetakTermina().before(compare) && !clan.getOcenjeniTermini().contains(termin)) {
	    		 //System.out.println("Datum održavanja termina je " + termin.getPocetakTermina().after(compare));
	    		 TerminDTO terminDTO = new TerminDTO(termin.getId(), termin.getPocetakTermina(),
	    				 termin.getKrajTermina(), termin.getTrajanjeTermina(), termin.getCenaTermina(), termin.getTrening().getNazivTreninga(),
	    				 termin.getTrening().getOpis(), termin.getTrening().getTip());
	    	 	 terminDTOS.add(terminDTO);
	    	 }
	     }

	     return new ResponseEntity<>(terminDTOS, HttpStatus.OK);
	 }
}