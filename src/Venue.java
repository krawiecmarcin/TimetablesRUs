import java.util.Objects;
/**
 * Represents a venue with a name and possible data projector facilities
 * @author Chris Loftus
 * @version 1 (27th February 2020)
 */
public class Venue implements Comparable<Venue> {
    private String name;
    private boolean hasDataProjector;

    public Venue() {
    }

    public Venue(String name) {
        this.name = name;
    }
    public void setHasDataProjector(boolean hasDataProjector) {
        this.hasDataProjector = hasDataProjector;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Venue venue = (Venue) o;
        return name.equals(venue.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public String toString() {
        return "Venue{" +
                "name='" + name + '\'' +
                ", hasDataProjector=" + hasDataProjector +
                '}';
    }

    @Override
    public int compareTo(Venue other) {
        return this.name.compareTo(other.name);
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean hasDataProjector() {
        return hasDataProjector;
    }

}
