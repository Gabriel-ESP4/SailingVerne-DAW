package cat.institutmarianao.sailing.ws.model;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/* JPA annotations */
@Entity
@Table(name = "actions")
/* Mapping JPA Indexes */
/* JPA Inheritance strategy is single table */
/*
 * Maps different JPA objects depDONEing on his type attribute (Opening,
 * Assignment, Intervention or Close)
 */
/* Lombok */
@Data
@RequiredArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public abstract class Action implements Serializable {

	private static final long serialVersionUID = 1L;

	/* Values for type - MUST be constants */
	public static final String BOOKING = "BOOKING";
	public static final String RESCHEDULING = "RESCHEDULING";
	public static final String CANCELLATION = "CANCELLATION";
	public static final String DONE = "DONE";

	public enum Type {
		BOOKING, RESCHEDULING, CANCELLATION, DONE
	}

	/* Validation */
	@NotNull
	/* JPA */
	@Id
	/* Lombok */
	@EqualsAndHashCode.Include
	protected Long id;

	/* Validation */
	@NotNull
	/* Lombok */
	@NonNull
	/* JPA */
	@Enumerated(EnumType.STRING)
	protected Type type;

	/* Validation */
	@NotNull
	/* JPA */
	@ManyToOne(cascade = CascadeType.ALL)
	protected User performer;

	/* JPA */
	protected Date date = new Date();

	/* Validation */
	@NotNull
	/* JPA */
	@ManyToOne(cascade = CascadeType.ALL)
	/* JSON */
	protected Trip trip;

	private String comments;
}
