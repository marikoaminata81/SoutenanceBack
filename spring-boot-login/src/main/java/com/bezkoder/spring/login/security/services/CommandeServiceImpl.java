package com.bezkoder.spring.login.security.services;

import com.bezkoder.spring.login.models.*;
import com.bezkoder.spring.login.payload.response.MessageResponse;
import com.bezkoder.spring.login.repository.CommandeRepository;
import com.bezkoder.spring.login.repository.HistoriqueRepository;
import com.bezkoder.spring.login.repository.PanierRepository;
import com.bezkoder.spring.login.repository.ProduitRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class CommandeServiceImpl implements CommandeService {
    @Autowired
    private HistoriqueRepository historiqueRepository;
    @Autowired
    private final PanierRepository panierRepository;
@Autowired
private NotificationService notificationService;
    @Autowired
    private CommandeRepository commandeRepository;
    @Autowired
    private final ProduitRepository produitRepository;

    @Autowired
    private UserService userService;
  /*  public CommandeServiceImpl(PanierRepository panierRepository,
                               ProduitRepository produitRepository) {
        this.panierRepository = panierRepository;
        this.produitRepository = produitRepository;

    }*/

    @Override
    public MessageResponse ajouter(Commande commande, User user) {

        double difference = 0.0;

        String codeCommende = "IKAGA" + user.getUsername();
        Random r = new Random();
        int randInt = r.nextInt(1000);
        User authUser = userService.getAuthenticatedUser();
        List<Panier> panier = panierRepository.findByUserAndEtat(user, true);
        commande.setDatecommande(LocalDate.now());
        commande.setUser(authUser);
        commande.setStatus("en cours");

        Double totaux = 0.0;
        Long totalQ = 0L;
        if (!panier.isEmpty()) {
            for (Panier panier1 : panier) {
                Optional<Produit> produit = produitRepository.findById(panier1.getProduits().get(0).getId());

                // =========================================
                // ICI ON AJOUTE HISTORIQUE DES VENTES
                Historique historique = new Historique();
                historique.setNomproduit(panier1.getProduits().get(0).getNom());
                historique.setNomclient(authUser.getNom());
                historique.setPrenomclient(authUser.getPrenom());
                historique.setNumeroclient(authUser.getUsername());
                historique.setDatevente(new Date());
                historique.setUser(panier1.getProduits().get(0).getAuthor());
                historique.setQuantite(panier1.getQuantite());
                historique.setPrixunitaire(panier1.getProduits().get(0).getPrix());
                historique.setMontanttotal(panier1.getTotalproduit());
                historiqueRepository.save(historique);

                // =========================================

                totalQ += panier1.getQuantite();
                totaux += (panier1.getTotalproduit() != null ? panier1.getTotalproduit().doubleValue() : 0D);
                panier1.setEtat(false);


            }
            commande.setMontanttotal(totaux);
            commande.setQuantitecommande(totalQ);
            commande.setCodecommande(codeCommende + randInt);

            // =========================================
            // ICI ON ATTRIBUE LES VALEURS DE USER QUI PASSE LA COMMANDE
            System.err.println("boolean: " +commande.getUser().equals(authUser));
            commandeRepository.save(commande);

// Si la commande n'a pas été passée par l'utilisateur connecté, envoyer une notification à l'utilisateur qui a passé la commande

            if (commande.getUser().equals(authUser)) {
                notificationService.sendNotificationCommande(
                        commande.getUser(),
                        authUser,
                        commande,
                        NotificationType.PASSER_COMMANDE.name()
                );
            }
            // =========================================

            MessageResponse message = new MessageResponse("Commande effectuée avec succès", true);
            return message;

        } else {
            commande.setUser(user);
            commande.setDatecommande(LocalDate.now());
            System.out.println("Votre panier est vide !");
            return new MessageResponse("Votre panier est vide !", false);
        }
    }
}
