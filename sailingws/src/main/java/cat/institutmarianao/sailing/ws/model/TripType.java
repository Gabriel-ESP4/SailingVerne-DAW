package cat.institutmarianao.sailing.ws.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/* JPA annotations */
@Entity
@Table(name = "trip_types")

/* Lombok */
@Data
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class TripType implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final String GROUP = "GROUP";
	public static final String PRIVATE = "PRIVATE";

	public enum Category {
		GROUP, PRIVATE
	}

	/* Validation */
	/* JPA */
	@Id
	/* Lombok */
	@EqualsAndHashCode.Include
	private Long id;

	/* Validation */
	@NotNull
	@NotEmpty
	private String title;

	/* Validation */
	@NotNull
	@NotEmpty
	/* JPA */
	@Enumerated(EnumType.STRING)
	private Category category;

	/* Validation */
	@NotNull
	@NotEmpty
	private String description;

	/* Validation */
	@NotNull
	@NotEmpty
	private double price;

	/* JPA */
	@ElementCollection
	@CollectionTable
	@Column(name="departure")
	@Temporal(TemporalType.TIME)
	private List<Date> departures;

	/* Validation */
	@NotNull
	@NotEmpty
	private int duration;

	/* Validation */
	@NotNull
	@NotEmpty
	/* JPA */
	private int maxPlaces;
}
