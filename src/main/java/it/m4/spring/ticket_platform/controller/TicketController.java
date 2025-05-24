package it.m4.spring.ticket_platform.controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.m4.spring.ticket_platform.model.Ticket;
import it.m4.spring.ticket_platform.model.User;
import it.m4.spring.ticket_platform.model.UserDto;
import it.m4.spring.ticket_platform.repository.RoleRepository;
import it.m4.spring.ticket_platform.repository.TicketRepository;
import it.m4.spring.ticket_platform.repository.UserRepository;
import it.m4.spring.ticket_platform.security.DatabaseUserDetails;
import it.m4.spring.ticket_platform.service.CategoriaService;
import it.m4.spring.ticket_platform.service.TicketService;
import it.m4.spring.ticket_platform.service.UserService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/ticket")
public class TicketController {

    private final TicketService ticketService;
    private final UserService userService;
    private final CategoriaService categoriaService;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    public TicketController(TicketService ticketService, UserService userService, CategoriaService categoriaService) {
        this.ticketService = ticketService;
        this.userService = userService;
        this.categoriaService = categoriaService;
    }

    @GetMapping
    public String index(Model model, @AuthenticationPrincipal DatabaseUserDetails userDetails, @RequestParam(value="keyword", required=false) String titolo){
        List<Ticket> result = new ArrayList<>(); // aggiunta di new ArrayList<>()
        User user = userService.findById(userDetails.getId());
        if (user.hasRole("ADMIN")) {
        result = (titolo != null && !titolo.isBlank())
                ? ticketRepository.findByTitoloContainingIgnoreCase(titolo)
                : ticketService.findAll();
            } else if (user.hasRole("OPERATORE")) {
                result = (titolo != null && !titolo.isBlank())
                        ? ticketRepository.findByTitoloContainingIgnoreCaseAndOperatore(titolo, user)
                        : ticketService.findByOperatore(user);
            } else {
            model.addAttribute("errorMsg", "Pagina non trovata");
            return "ticket/error_page";
            }
        model.addAttribute("list", result);
        model.addAttribute("user", user);
        return "ticket/index";
    }

    @GetMapping("/show_id/{id}")
    public String show_id(@PathVariable("id") Integer id, Model model, @AuthenticationPrincipal DatabaseUserDetails userDetails) {
        Optional<Ticket> optTicket = ticketRepository.findById(id);
        User user = userService.findById(userDetails.getId());
        if(optTicket.isPresent()){
            model.addAttribute("tckt", optTicket.get());
            model.addAttribute("user", user);
            return "ticket/show_id";
        }
        model.addAttribute("errorMsg", "Pagina non trovata");
        model.addAttribute("user", user);
        return "ticket/error_page";
    }

    @GetMapping("/register")
    public String register(Model model){
        model.addAttribute("user", new UserDto());
        model.addAttribute("role", roleRepository.findAll());
        return "register";
    }

    @GetMapping("/login")
     public String loginPage() {
     return "login"; 
   }

   @GetMapping("/creaTicket")
      public String createTicket(Model model, @AuthenticationPrincipal DatabaseUserDetails userDetails){
        model.addAttribute("nuovoTicket", new Ticket()); // ticket vuoto
        List<User> operatoriDisponibili = userService.findOperatoriDisponibili("OPERATORE"); 
        model.addAttribute("categorie", categoriaService.findAll()); // categorie
        model.addAttribute("operatori", operatoriDisponibili); // operatori disponibili
        User currentUser = userService.findById(userDetails.getId());
        boolean isOperatore = currentUser.hasRole("OPERATORE");
        List<Ticket> tickets;
        if (isOperatore) {
        tickets = ticketService.findByOperatoreId(currentUser.getId()); 
         } else {
           tickets = ticketService.getAllTickets(); 
        }
        model.addAttribute("list", tickets); // lista dei ticket visibili all'utente loggato
        return "ticket/creaTicket";
      }
    
   @PostMapping("/creaTicket")
   public String storeTicket(@Valid @ModelAttribute("nuovoTicket") Ticket formTicket, @AuthenticationPrincipal DatabaseUserDetails userDetails, BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes){
    if(bindingResult.hasErrors()) {
        model.addAttribute("operatori", userService.findOperatoriDisponibili("OPERATORE"));
        model.addAttribute("categorie", categoriaService.findAll());
        return "ticket/creaTicket";
        }
        // Recupero l'admin loggato
        User admin = userService.findById(userDetails.getId());
        formTicket.setAdmin(admin); // Associo l’admin che ha creato il ticket.
        // Cerco operatori disponibili
        List<User> operatoriDisponibili = userService.findOperatoriDisponibili("OPERATORE");
        if (operatoriDisponibili.isEmpty()) {
            model.addAttribute("error", "Nessun operatore disponibile al momento"); //Se non ci sono operatori disponibili, blocco il salvataggio.
            return "ticket/form";
        }
        // imposto le date
        LocalDate now = LocalDate.now();
        formTicket.setDataCreazione(now);
        formTicket.setDataAggiornamento(now);
        ticketService.save(formTicket);
        redirectAttributes.addFlashAttribute("successMessage", "Ticket creato con successo!");
        return "redirect:/ticket";
   }

   @GetMapping("/edit/{id}") // recupero un ticket e lo mostro nel form di modifica
    public String edit(@PathVariable("id") Integer id, Model model, @AuthenticationPrincipal DatabaseUserDetails userDetails, RedirectAttributes redirectAttributes) {
    Optional<Ticket> optTicket = ticketService.getTicketById(id);
     User user = userService.findById(userDetails.getId());
    if (optTicket.isEmpty()) {
        redirectAttributes.addFlashAttribute("errorMessage", "Ticket non trovato."); //Verifico che il ticket esista
        return "redirect:/ticket";
    }
    Ticket ticket = optTicket.get();
    model.addAttribute("modificaticket", ticket);
    model.addAttribute("categorie", categoriaService.findAll());
    model.addAttribute("operatori", userService.findOperatoriDisponibili("OPERATORE")); //Passo alla view anche le categorie e gli operatori.
    model.addAttribute("user", user);
    return "ticket/edit";
    }

    @PostMapping("/edit/{id}") // Salvo le modifiche al ticket
    public String edit(@PathVariable("id") Integer id, @ModelAttribute("modificaticket") Ticket formTicket, @AuthenticationPrincipal DatabaseUserDetails userDetails, RedirectAttributes redirectAttributes) {
    Optional<Ticket> optTicket = ticketService.getTicketById(id);
    if (optTicket.isEmpty()) {
        redirectAttributes.addFlashAttribute("errorMessage", "Ticket non trovato.");
        return "redirect:/ticket";
    }
    Ticket ticket = optTicket.get();
    User currentUser = userService.findById(userDetails.getId());
    // Operatore può modificare solo lo stato del ticket a lui assegnato
    if (currentUser.hasRole("OPERATORE")) { 
        if (ticket.getOperatore() != null && !ticket.getOperatore().getId().equals(currentUser.getId())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Non sei autorizzato a modificare questo ticket.");
            return "redirect:/ticket";
        }
        ticket.setStato(formTicket.getStato());
    }
    // Admin può modificare tutto
    else if (currentUser.hasRole("ADMIN")) {
        ticket.setTitolo(formTicket.getTitolo());
        ticket.setDescrizione(formTicket.getDescrizione());
        ticket.setStato(formTicket.getStato());
        ticket.setCategoria(formTicket.getCategoria());
        ticket.setOperatore(formTicket.getOperatore());
        ticket.setAdmin(currentUser); // oppure formTicket.getAdmin() se gestito da select
    }
    ticket.setDataAggiornamento(LocalDate.now());
    ticketService.save(ticket);
    redirectAttributes.addFlashAttribute("successMessage", "Ticket aggiornato con successo.");
    return "redirect:/ticket";
    }

    @PostMapping("delete/{id}") //Elimino un ticket solo se non ha note collegate. Altrimenti mostro messaggio d'errore.
    public String delete(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes ){
    Ticket ticket = ticketRepository.findById(id).get();
    if(!ticket.getNota().isEmpty()){
        redirectAttributes.addFlashAttribute("errorMessage", "Ci sono note collegate a questo ticket, non si può cancellare!");
        return "redirect:/ticket";
    }
    ticketRepository.deleteById(id);
    return "redirect:/ticket";
   }

}
