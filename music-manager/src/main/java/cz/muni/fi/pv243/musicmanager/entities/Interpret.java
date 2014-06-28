package cz.muni.fi.pv243.musicmanager.entities;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

import org.hibernate.search.annotations.Analyze;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.ProvidedId;
import org.hibernate.validator.constraints.NotBlank;

/**
 * @author Roman Macor
 * */

@Indexed(index="InterpretIndex")
@ProvidedId
public class Interpret implements Serializable {
	
	private static final long serialVersionUID = 2928763700219231596L;

	public enum Genre{
		POP, ROCK, METAL, PUNK, CLASSIC
	}
	@Field
	private String id;
	
	@Field (analyze=Analyze.NO)
	@NotNull
	@NotBlank
	
	private String name;
	
	@Field
	private String country;
	
	@Field
	private Genre genre;

	public Interpret(){};

	public Interpret(String id, String name, String country, Genre genre) {
		super();
		this.id = id;
		this.name = name;
		this.country = country;
		this.genre = genre;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Genre getGenre() {
		return genre;
	}

	public void setGenre(Genre genre) {
		this.genre = genre;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((country == null) ? 0 : country.hashCode());
		result = prime * result + ((genre == null) ? 0 : genre.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Interpret other = (Interpret) obj;
		if (country == null) {
			if (other.country != null)
				return false;
		} else if (!country.equals(other.country))
			return false;
		if (genre != other.genre)
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return name + ", " + genre;
	}
	
	
}
