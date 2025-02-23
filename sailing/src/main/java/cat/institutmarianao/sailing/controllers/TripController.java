package cat.institutmarianao.sailing.controllers;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
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
import java.util.Calendar;
import java.text.DateFormat;
import java.text.SimpleDateFormat;


@Controller
@SessionAttributes({ "trip", "tripType", "freePlaces", "tripFreePlaces" })
@RequestMapping(value = "/trips")
public class TripController {

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

	@PostMapping("/book/book_places")
	public String bookSelectPlaces(@Validated(OnTripCreateDeparture.class) @ModelAttribute("trip") Trip trip,
			BindingResult result, @SessionAttribute("tripType") TripType tripType,
			@SessionAttribute("freePlaces") Map<Date, Long> freePlaces,
			@SessionAttribute("tripFreePlaces") Long tripFreePlaces, ModelMap modelMap) {
		
		Date tripDeparture = trip.getDeparture();
		
		// Si modificando el HTML ha conseguido poner una hora con 0 places, le retornamos la página para hackers
		for (Map.Entry<Date, Long> entry : freePlaces.entrySet()) {
			Date freePlaceDate = entry.getKey();
            Long freePlacePlaces = entry.getValue();
			
			if (freePlaceDate == tripDeparture) {
				if (freePlacePlaces == 0) {
					return "hackers";
				}
				break;
			}
		}
		
		modelMap.addAttribute("tripFreePlaces", Long.valueOf(trip.getPlaces()));
		return "book_places";
		
	}

	@PostMapping("/book/book_save")
	public String bookSave(@Validated(OnTripCreate.class) @ModelAttribute("trip") Trip trip, BindingResult result,
			@SessionAttribute("tripType") TripType tripType, @SessionAttribute("freePlaces") Map<Date, Long> freePlaces,
			@SessionAttribute("tripFreePlaces") Long tripFreePlaces, ModelMap modelMap, SessionStatus sessionStatus) {

		// TODO - Saves a booking
		return null;
	}

	@GetMapping("/booked")
	public ModelAndView booked() {
		ModelAndView trips = new ModelAndView("trips");
		List<Trip> allTrips = tripService.findAll();
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
