package cat.institutmarianao.sailing.services.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import cat.institutmarianao.sailing.model.Action;
import cat.institutmarianao.sailing.model.BookedPlace;
import cat.institutmarianao.sailing.model.Trip;
import cat.institutmarianao.sailing.model.TripType;
import cat.institutmarianao.sailing.services.TripService;
import jakarta.validation.constraints.NotNull;

@Service
@PropertySource("classpath:application.properties")
public class TripServiceImpl implements TripService {

	private static final String TRIP_SERVICE = "/trips";
	private static final String TRIP_FIND_ALL = TRIP_SERVICE + "/booked";

	private static final String TRIPTYPE_SERVICE = "/triptypes";
	private static final String TRIPTYPE_FIND_ALL = TRIPTYPE_SERVICE + "/find/all";
	private static final String TRIPTYPE_FIND_ALL_GROUP = TRIPTYPE_FIND_ALL + "/group";
	private static final String TRIPTYPE_FIND_ALL_PRIVATE = TRIPTYPE_FIND_ALL + "/private";

	@Value("${webService.host}")
	private String webServiceHost;

	@Value("${webService.port}")
	private String webServicePort;

	@Autowired
	private RestTemplate restTemplate;

	@Override
	public List<Trip> findAll() {
		final String uri = webServiceHost + ":" + webServicePort + TRIP_FIND_ALL;

		ResponseEntity<Trip[]> response = restTemplate.getForEntity(uri, Trip[].class);
		return Arrays.asList(response.getBody());
	}

	@Override
	public List<Trip> findAllByClientUsername(String username) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Trip save(Trip trip) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<BookedPlace> findBookedPlacesByTripIdAndDate(@NotNull Long id, @NotNull Date date) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<TripType> getAllTripTypes() {
		final String uri = webServiceHost + ":" + webServicePort + TRIPTYPE_FIND_ALL;

		ResponseEntity<TripType[]> response = restTemplate.getForEntity(uri, TripType[].class);
		return Arrays.asList(response.getBody());
	}

	@Override
	public List<TripType> getAllGroupTripTypes() {
		final String uri = webServiceHost + ":" + webServicePort + TRIPTYPE_FIND_ALL_GROUP;

		ResponseEntity<TripType[]> response = restTemplate.getForEntity(uri, TripType[].class);
		return Arrays.asList(response.getBody());
	}

	@Override
	public List<TripType> getAllPrivateTripTypes() {
		final String uri = webServiceHost + ":" + webServicePort + TRIPTYPE_FIND_ALL_PRIVATE;

		ResponseEntity<TripType[]> response = restTemplate.getForEntity(uri, TripType[].class);
		return Arrays.asList(response.getBody());
	}

	@Override
	public TripType getTripTypeById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Action> findTrackingById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Action track(Action action) {
		// TODO Auto-generated method stub
		return null;
	}

}
