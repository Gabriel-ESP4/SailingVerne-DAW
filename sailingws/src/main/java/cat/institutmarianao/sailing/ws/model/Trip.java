package cat.institutmarianao.sailing.ws.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import org.hibernate.annotations.Formula;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.Setter;

/* JPA annotations */
@Entity
@Table(name = "trips")
/* Lombok */
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Trip implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final int MAX_DESCRIPTION = 500;

	public static final String RESERVED = "RESERVED";
	public static final String RESCHEDULED = "RESCHEDULED";
	public static final String CANCELLED = "CANCELLED";
	public static final String DONE = "DONE";

	public enum Status {
		RESERVED, RESCHEDULED, CANCELLED, DONE
	}

	/* Validation */
	@NotNull
	/* JPA */
	@Id
	/* Lombok */
	@EqualsAndHashCode.Include
	/* JSON */
	private Long id;

	/* JPA */
	@OneToOne
	private TripType type;

	/* Validation */
	@NotNull
	/* JPA */
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "client_username", referencedColumnName = "username")
	private Client client;

	private int places;

	/* Validation */
	@NotNull
	/* JPA */

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "trip", orphanRemoval = true)
	private List<@Valid Action> tracking;
	@Enumerated(EnumType.STRING)
	/* Hibernate */
	@Formula("(SELECT CASE a.type WHEN '" + Action.BOOKING + "' THEN '" + Trip.RESERVED + "' WHEN '"
			+ Action.RESCHEDULING + "' THEN '" + Trip.RESCHEDULED + "' WHEN '" + Action.CANCELLATION + "' THEN '"
			+ Trip.CANCELLED + "' WHEN '" + Action.DONE + "' THEN '" + Trip.DONE + "' ELSE NULL END FROM actions a "
			+ " WHERE a.date=( SELECT MAX(last_action.date) FROM actions last_action "
			+ " WHERE last_action.trip_id=a.trip_id AND last_action.trip_id=id ))")
	// Lombok
	@Setter(AccessLevel.NONE)
	private Status status;

	/* Validation */
	@NotNull
	/* JPA */
	@Temporal(TemporalType.DATE)
	private Date date;

	/* JPA */
	@Column(name = "departure")
	@Temporal(TemporalType.TIME)
	private Date departure;
}
