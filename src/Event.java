import java.time.LocalDateTime;
import java.util.Scanner;

/**
 *
 * @author krawi
 * @version 1
 *
 */
public class Event {



    int eventId;
    private boolean requiresDataProjector;
    private Venue venue;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public void load(Scanner infile) {
        setEventId(infile.nextInt());
        setStartAndEndTime(readDateTime(infile),readDateTime(infile));
        setDataProjectorRequired(infile.nextBoolean());
    }

    private LocalDateTime readDateTime(Scanner infile) {
        String dateTime = infile.next();
        return LocalDateTime.parse(dateTime);
    }

    public void setStartAndEndTime(LocalDateTime startTime, LocalDateTime endTime) throws IllegalArgumentException {
        if (startTime.compareTo(endTime) >= 0){
            throw new IllegalArgumentException("start time: " + startTime + " must be before end time: " + endTime);
        }
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public void setEventId(int eventId) {
        this.eventId = eventId;
    }
    public int getEventId() {
        return eventId;
    }

    public boolean isRequiresDataProjector() {
        return requiresDataProjector;
    }

    public void setDataProjectorRequired(boolean dataProjectorRequired) {
        if (venue != null && (dataProjectorRequired && !venue.hasDataProjector())){
            System.err.println("Event currently has a venue " +
                    venue.getName() + " that does not have a data projector. Change the venue first");
        } else {
            this.requiresDataProjector = dataProjectorRequired;
        }
    }


    public void setVenue(Venue venue) {
        // Only allow this if the venue spec matches the
        // the meeting requirement
        if (requiresDataProjector && !venue.hasDataProjector()) {
            System.err.println("Meeting requires a data projector. " +
                    "Venue " + venue.getName() + " does not have one");
        } else {
            this.venue = venue;
        }
    }

    public Venue getVenue() {
        return venue;
    }


    public LocalDateTime getStartTime(){
        return startTime;
    }

    public LocalDateTime getEndTime(){
        return endTime;
    }

}
