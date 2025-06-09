import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Scanner;

/**
 * Represents a meeting
 * @author Chris Loftus
 * @version 1 (27th February 2020)
 */
public class Meeting extends Event {
    private String meetingName;
    private String organiser;
    private MeetingType meetingType;

    /**
     * Constructor building object from file
     * @param infile file data
     */
    public Meeting(Scanner infile){
        load(infile);
    }

    /**
     * Empty constructor used in adding meetings
     */
    public Meeting(){

    }

    /**
     * Build a meeting with given values.
     *
     * @param meetingName The name of the meeting
     * @param organiser Who organised the meeting
     * @param meetingType The kind of meeting
     */
    public Meeting(String meetingName, String organiser, MeetingType meetingType){

        this.meetingName = meetingName;
        this.meetingType = meetingType;
        this.organiser = organiser;
    }


    @Override
    public int hashCode() {
        return Objects.hash(super.getVenue(), super.getStartTime(), super.getEndTime());
    }

    /**
     * Loads meeting data from the given text file
     * @param infile An open scanner on the text file
     * @exception IllegalArgumentException thrown if infile is null
     */
    public void load(Scanner infile) throws IllegalArgumentException{
        if (infile == null) {
            throw new IllegalArgumentException("infile must not be null");
        }

        super.load(infile);
        meetingName = infile.next();
        organiser = infile.next();
        String meetingTypeStr = infile.next();
        meetingType = MeetingType.valueOf(meetingTypeStr);
    }

    /**
     * Saves meeting data to the given text file
     * @param outfile An open scanner on the text file
     * @exception IllegalArgumentException thrown if outfile is null
     */
    public void save(PrintWriter outfile) {
        if (outfile == null) {
            throw new IllegalArgumentException("outfile must not be null");
        }
        outfile.println(EventType.MEETING);
        outfile.println(eventId);
        writeDateTime(outfile, getStartTime());
        writeDateTime(outfile, getEndTime());

        outfile.println(isRequiresDataProjector());
        outfile.println(meetingName);
        outfile.println(organiser);
        outfile.println(meetingType);
    }

    @Override
    public String toString() {
        return "Meeting{" +
                "meetingName='" + meetingName + '\'' +
                ", organiser='" + organiser + '\'' +
                ", meetingType=" + meetingType +
                ", eventId=" + eventId +
                ", requiresDataProjector=" + isRequiresDataProjector() +
                ", venue=" + getVenue() +
                ", startTime=" + getStartTime() +
                ", endTime=" + getEndTime() +
                '}';
    }


    private LocalDateTime readDateTime(Scanner infile){
        String dateTime = infile.next();
        return LocalDateTime.parse(dateTime);
    }

    private void writeDateTime(PrintWriter outfile, LocalDateTime dateTime){
        outfile.println(dateTime);
    }

    public void setMeetingName(String meetingName){
        this.meetingName = meetingName;
    }

    public void setOrganiser(String organiser){
        this.organiser = organiser;
    }

    public void setMeetingType(MeetingType meetingType){
        this.meetingType = meetingType;
    }

    public String getMeetingName(){
        return meetingName;
    }

    public String getOrganiser(){
        return organiser;
    }



}
