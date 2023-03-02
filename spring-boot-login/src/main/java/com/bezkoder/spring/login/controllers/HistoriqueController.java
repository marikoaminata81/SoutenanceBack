package com.bezkoder.spring.login.controllers;

import com.bezkoder.spring.login.models.Historique;
import com.bezkoder.spring.login.models.User;
import com.bezkoder.spring.login.payload.response.MessageResponse;
import com.bezkoder.spring.login.repository.HistoriqueRepository;
import com.bezkoder.spring.login.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/historique")
@CrossOrigin(origins = "*", maxAge = 3600)
public class HistoriqueController {

    @Autowired
    private HistoriqueRepository historiqueRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/lesHistoriqueParFerme/{ferme}")
    public List<Historique> mesHistos(@PathVariable("ferme") User user){
        return historiqueRepository.findByUser(user);
    }


    @GetMapping("/lister")
    public List<Historique> lesHistos(){
        return historiqueRepository.findAll();
    }

    @DeleteMapping(path = "/supprimer/{idhistorique}", name = "supprimer")
    //  @ResponseStatus(HttpStatus.NO_CONTENT)
    public MessageResponse supprimer(@PathVariable Long idhistorique) {
        Historique historique = historiqueRepository.findById(idhistorique).get();


        this.historiqueRepository.delete(historique);
        MessageResponse message = new MessageResponse("Historique supprimé avec succès !", true);
        return message;
    }
}