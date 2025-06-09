import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.Scanner;

/**
 * @author krawi
 * @version 1 (31.03.2020)
 */
public class Lecture extends Event {
    private String moduleCode;
    private String lecturerName;
    private String phoneNumber;
    private boolean adjustableSeating;


    public Lecture(String moduleCode, String lecturerName, String phoneNumber, boolean adjustableSeating) {

        this.moduleCode = moduleCode;
        this.lecturerName = lecturerName;
        this.phoneNumber = phoneNumber;
        this.adjustableSeating = adjustableSeating;
    }

    /**
     * Constructor building object Lecture from file
     * @param infile file data
     */
    public Lecture(Scanner infile) {
        load(infile);

    }
    /**
     * Empty constructor used in adding lectures
     */
    public Lecture(){

    }

    public void load(Scanner infile) throws IllegalArgumentException {
        if (infile == null) {
            throw new IllegalArgumentException("infile must not be null");
        }

        super.load(infile);

        moduleCode = infile.next();
        lecturerName = infile.next();
        phoneNumber = infile.next();
        adjustableSeating = infile.nextBoolean();
    }

    public void save(PrintWriter outfile) {
        if (outfile == null) {
            throw new IllegalArgumentException("outfile must not be null");
        }
        outfile.println(EventType.LECTURE);
        outfile.println(eventId);
        writeDateTime(outfile, getStartTime());
        writeDateTime(outfile, getEndTime());

        outfile.println(isAdjustableSeating());
        outfile.println(moduleCode);
        outfile.println(lecturerName);
        outfile.println(phoneNumber);

        outfile.println(isAdjustableSeating());

    }


    private void writeDateTime(PrintWriter outfile, LocalDateTime dateTime) {
        outfile.println(dateTime);
    }

    @Override
    public String toString() {
        return "Lecture{" +
                "moduleCode='" + moduleCode + '\'' +
                ", lecturerName='" + lecturerName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", eventId=" + getEventId() +
                ", adjustableSeating=" + adjustableSeating +
                ", venue=" + getVenue() +
                ", startTime=" + getStartTime() +
                ", endTime=" + getEndTime() +
                '}';
    }


    public void setAdjustableSeating(boolean adjustableSeating) {
        this.adjustableSeating = adjustableSeating;
    }

    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode;
    }

    public void setLecturerName(String lecturerName) {
        this.lecturerName = lecturerName;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isAdjustableSeating() {
        return adjustableSeating;
    }

    public String getModuleCode() {
        return moduleCode;
    }

    public String getLecturerName() {
        return lecturerName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }



}