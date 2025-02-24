package cat.institutmarianao.sailing.controllers;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import cat.institutmarianao.sailing.model.BookedPlace;
import cat.institutmarianao.sailing.model.Cancellation;
import cat.institutmarianao.sailing.model.Done;
import cat.institutmarianao.sailing.model.Rescheduling;
import cat.institutmarianao.sailing.model.Trip;
import cat.institutmarianao.sailing.model.TripType;
import cat.institutmarianao.sailing.services.TripService;
import cat.institutmarianao.sailing.validation.groups.OnActionCreate;
import cat.institutmarianao.sailing.validation.groups.OnTripCreate;
import cat.institutmarianao.sailing.validation.groups.OnTripCreateDate;
import cat.institutmarianao.sailing.validation.groups.OnTripCreateDeparture;
import jakarta.validation.constraints.Positive;

@Controller
@SessionAttributes({ "trip", "tripType", "freePlaces", "tripFreePlaces" })
@RequestMapping(value = "/trips")
public class TripController {
	private String REDIRECTHACKERS = "redirect:https://iili.io/3HbEUjj.gif";

	@Autowired
	private TripService tripService;

	@ModelAttribute("trip")
	private Trip setuoTrip() {
		return new Trip();
	}

	@ModelAttribute("tripType")
	private TripType setupTripType() {
		return new TripType();
	}

	@ModelAttribute("freePlaces")
	private Map<Date, Long> setupFreePlaces() {
		return new HashMap<>();
	}

	@ModelAttribute("tripFreePlaces")
	private Long setupTripFreePlaces() {
		return 0l;
	}

	@GetMapping("/book/{trip_type_id}")
	public ModelAndView bookSelectDate(@PathVariable(name = "trip_type_id", required = true) Long tripTypeId) {

		ModelAndView bookDate = new ModelAndView("book_date");
		TripType tripType = tripService.getTripTypeById(tripTypeId);
		bookDate.getModelMap().addAttribute("tripType", tripType);
		Trip trip = new Trip();

		trip.setTypeId(tripTypeId);

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		trip.setClientUsername(authentication.getName());

		bookDate.getModelMap().addAttribute("trip", trip);

		return bookDate;
	}

	@PostMapping("/book/book_departure")
	public String bookSelectDeparture(@Validated(OnTripCreateDate.class) @ModelAttribute("trip") Trip trip,
			BindingResult result, @SessionAttribute("tripType") TripType tripType,
			@SessionAttribute("freePlaces") Map<Date, Long> freePlaces, ModelMap modelMap) {

		List<BookedPlace> departures;

		try {
			if (trip.getDate() == null) {
				throw new java.lang.NullPointerException("El usuario no ha introducido la fecha.");
			}
			departures = tripService.findBookedPlacesByTripIdAndDate(tripType.getId(), trip.getDate());
		} catch (java.lang.NullPointerException e) {
			modelMap.addAttribute("dateNull", true);
			return "book_date";
		}

		for (Date departureHours : tripType.getDepartures()) {
			freePlaces.put(departureHours, tripType.getMaxPlaces());
		}

		for (BookedPlace bookedPlace : departures) {
			Date departure = bookedPlace.getDeparture();
			Long places = bookedPlace.getBookedPlaces();
			freePlaces.replace(departure, (freePlaces.get(departure) - places));
		}

		return "book_departure";
	}

	@SuppressWarnings("unlikely-arg-type")
	@PostMapping("/book/book_places")
	public String bookSelectPlaces(@Validated(OnTripCreateDeparture.class) @ModelAttribute("trip") Trip trip,
			BindingResult result, @SessionAttribute("tripType") TripType tripType,
			@SessionAttribute("freePlaces") Map<Date, Long> freePlaces,
			@SessionAttribute("tripFreePlaces") Long tripFreePlaces, ModelMap modelMap) {

		Date tripDeparture = trip.getDeparture();
		Long selectedPlaces = freePlaces.get(tripDeparture);

		// Si modificando el HTML y ha hecho algo de esto:
		// -- Ha pasado sin poner la hora.
		// -- Ha puesto una hora no existente (selectedPlaces es null si tripDeparture
		// no se encuentra en freePlaces).
		// -- Ha conseguido poner una hora con 0 o negativos places.
		if (tripDeparture == null || selectedPlaces == null || selectedPlaces <= 0) {
			return REDIRECTHACKERS;
		}

		modelMap.addAttribute("tripFreePlaces", selectedPlaces);
		return "book_places";

	}

	@PostMapping("/book/book_save")
	public String bookSave(@Validated(OnTripCreate.class) @ModelAttribute("trip") Trip trip, BindingResult result,
			@SessionAttribute("tripType") TripType tripType, @SessionAttribute("freePlaces") Map<Date, Long> freePlaces,
			@SessionAttribute("tripFreePlaces") Long tripFreePlaces, ModelMap modelMap, SessionStatus sessionStatus) {

		// Si modificando el HTML y ha hecho algo de esto:
		// -- Ha conseguido poner una hora con 0 o negativos places.
		// -- Ha conseguido poner una hora con más places de los permitidos.
		if (trip.getPlaces() <= 0 || trip.getPlaces() > tripFreePlaces) {
			return REDIRECTHACKERS;
		}

		tripService.save(trip);

		return "redirect:/trips/booked";
	}

	@GetMapping("/booked")
	public ModelAndView booked() {

		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

		ModelAndView trips = new ModelAndView("trips");

		List<Trip> allTrips;

		if (authorities.stream().anyMatch(ga -> ga.getAuthority().equals("ROLE_ADMIN"))) {
			allTrips = tripService.findAll();
		} else {

			String username = authentication.getName();

			allTrips = tripService.findAllByClientUsername(username);
		}

		trips.getModelMap().addAttribute("trips", allTrips);

		return trips;
	}

	@PostMapping("/cancel")
	public String cancelTrip(@Validated Cancellation cancellation) {

		// TODO - Cancel a trip (add a CANCELLATION action to its tracking)
		return null;
	}

	@PostMapping("/done")
	public String doneTrip(@Validated(OnActionCreate.class) Done done) {

		// TODO - Do a trip (add a DONE action to its tracking)
		return null;
	}

	@PostMapping("/reschedule")
	public String saveAction(@Validated(OnActionCreate.class) Rescheduling rescheduling) {

		// TODO - Reschedule a trip (add a RESCHEDULE action to its tracking)
		return null;
	}

	@GetMapping("/tracking/{id}")
	public String showContentPart(@PathVariable(name = "id", required = true) @Positive Long id, ModelMap modelMap) {

		// TODO - Retrieve the tracking for the trip id with id id
		return null;
	}
}
