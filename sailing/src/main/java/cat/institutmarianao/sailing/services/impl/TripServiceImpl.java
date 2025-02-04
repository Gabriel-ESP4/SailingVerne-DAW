package cat.institutmarianao.sailing.services.impl;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import cat.institutmarianao.sailing.model.Action;
import cat.institutmarianao.sailing.model.BookedPlace;
import cat.institutmarianao.sailing.model.Trip;
import cat.institutmarianao.sailing.model.TripType;
import cat.institutmarianao.sailing.services.TripService;
import jakarta.validation.constraints.NotNull;

@Service
@PropertySource("classpath:application.properties")
public class TripServiceImpl implements TripService {

	private static final String USERNAME = "username";
	private static final String ID = "id";
	private static final String DATE = "date";

	private static final String TRIP_SERVICE = "/trips";
	private static final String TRIP_FIND_ALL = TRIP_SERVICE + "/booked";
	private static final String TRIP_FIND_ALL_BY_USERNAME = TRIP_SERVICE + "/find/all/by/client/username/{" + USERNAME
			+ "}";
	private static final String TRIP_SAVE = TRIP_SERVICE + "/save";
	private static final String TRIP_SAVE_ACTION = TRIP_SAVE + "/action";
	private static final String TRIP_FIND_ALL_BY_TRIPTYPE_ID = TRIP_SERVICE + "/bookedPlaces/{" + ID + "}/{" + DATE
			+ "}";
	private static final String TRIP_ACTIONS_FIND_ALL_BY_ID = TRIP_SERVICE + "/find/tracking/by/id/{" + ID + "}";

	private static final String TRIPTYPE_SERVICE = "/triptypes";
	private static final String TRIPTYPE_FIND_ALL = TRIPTYPE_SERVICE + "/find/all";
	private static final String TRIPTYPE_FIND_ALL_GROUP = TRIPTYPE_FIND_ALL + "/group";
	private static final String TRIPTYPE_FIND_ALL_PRIVATE = TRIPTYPE_FIND_ALL + "/private";
	private static final String TRIPTYPE_FIND_BY_ID = TRIPTYPE_SERVICE + "/get/by/id/{" + ID + "}";

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
		final String baseUri = webServiceHost + ":" + webServicePort + TRIP_FIND_ALL_BY_USERNAME;
		UriComponentsBuilder uriTemplate = UriComponentsBuilder.fromHttpUrl(baseUri);

		Map<String, String> uriVariables = new HashMap<>();
		uriVariables.put(USERNAME, username);
		ResponseEntity<Trip[]> response = restTemplate
				.getForEntity(uriTemplate.buildAndExpand(uriVariables).toUriString(), Trip[].class);
		return Arrays.asList(response.getBody());
	}

	@Override
	public Trip save(Trip trip) {
		final String uri = webServiceHost + ":" + webServicePort + TRIP_SAVE;

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<Trip> request = new HttpEntity<>(trip, headers);

		return restTemplate.postForObject(uri, request, Trip.class);
	}

	@Override
	public List<BookedPlace> findBookedPlacesByTripIdAndDate(@NotNull Long id, @NotNull Date date) {

		final String baseUri = webServiceHost + ":" + webServicePort + TRIP_FIND_ALL_BY_TRIPTYPE_ID;
		UriComponentsBuilder uriTemplate = UriComponentsBuilder.fromHttpUrl(baseUri);

		Map<String, String> uriVariables = new HashMap<>();
		uriVariables.put(ID, Long.toString(id));
		uriVariables.put(DATE, new SimpleDateFormat("yyyy-MM-dd").format(date));

		ResponseEntity<BookedPlace[]> response = restTemplate
				.getForEntity(uriTemplate.buildAndExpand(uriVariables).toUriString(), BookedPlace[].class);
		return Arrays.asList(response.getBody());
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
		final String baseUri = webServiceHost + ":" + webServicePort + TRIPTYPE_FIND_BY_ID;

		UriComponentsBuilder uriTemplate = UriComponentsBuilder.fromHttpUrl(baseUri);

		Map<String, String> uriVariables = new HashMap<>();
		uriVariables.put(ID, Long.toString(id));

		TripType response = restTemplate.getForObject(uriTemplate.buildAndExpand(uriVariables).toUriString(),
				TripType.class);
		return response;

	}

	@Override
	public List<Action> findTrackingById(Long id) {
		final String baseUri = webServiceHost + ":" + webServicePort + TRIP_ACTIONS_FIND_ALL_BY_ID;
		UriComponentsBuilder uriTemplate = UriComponentsBuilder.fromHttpUrl(baseUri);

		Map<String, String> uriVariables = new HashMap<>();
		uriVariables.put(ID, Long.toString(id));

		ResponseEntity<Action[]> response = restTemplate
				.getForEntity(uriTemplate.buildAndExpand(uriVariables).toUriString(), Action[].class);
		return Arrays.asList(response.getBody());

	}

	@Override
	public Action track(Action action) {
		final String uri = webServiceHost + ":" + webServicePort + TRIP_SAVE_ACTION;

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<Action> request = new HttpEntity<>(action, headers);

		return restTemplate.postForObject(uri, request, Action.class);
	}

}
