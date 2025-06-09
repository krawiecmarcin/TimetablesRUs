import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Represents a timetable. Keeps track of timetable meetings and possible venues
 *
 * @author Chris Loftus
 * @version 1 (27th February 2020)
 */
public class Timetable {

    private ArrayList<Event> events;
    private ArrayList<Venue> venues;


    /**
     * Initialises the timetable meetings and venues
     */
    public Timetable() {
        events = new ArrayList<>();
        venues = new ArrayList<>();

    }

    /**
     * Add a timetabled meeting.
     *
     * @param event A non-null, unique meeting object.
     * @return true if meeting added else false if the meeting already exists
     * @throws IllegalArgumentException is thrown if meeting is null
     */
    public boolean add(Event event) throws IllegalArgumentException {
        boolean success = false;
        if (event == null) {
            throw new IllegalArgumentException("The event must not be null");
        }
        if (!events.contains(event)) {
            events.add(event);
            success = true;
        }
        return success;
    }

    /**
     * Add a venue to the timetable system.
     *
     * @param venue A non-null, unique venue object.
     * @return true if venue added else false if the venue already exists
     * @throws IllegalArgumentException is thrown if venue is null
     */
    public boolean add(Venue venue) {
        boolean success = false;
        if (venue == null) {
            throw new IllegalArgumentException("The venue must not be null");
        }
        if (!venues.contains(venue)) {
            venues.add(venue);
            success = true;
        }
        return success;
    }

    /**
     * Removes the meeting from the timetable
     *
     * @param eventId The ID of timetabled meeting to be removed
     * @return true if removed else false if not found
     */
    public boolean removeEvent(int eventId) {
        // Search for the meeting by name
        Event which = null;
        for (Event event : events) {
            if (event.getEventId() == eventId) {
                which = event;
                break;
            }
        }
        if (which != null) {
            events.remove(which); // Requires that Meeting has an equals method
            return true;
        } else {
            return false;
        }
    }

    /**
     * Obtains a copy of the timetabled meetings
     *
     * @return A copy of the timetabled meetings
     */
    public Event[] obtainAllEvents() {
        Event[] result = new Event[events.size()];
        result = events.toArray(result);
        return result;
    }

    /**
     * Obtains a copy of the timetable system venues
     *
     * @return A copy of the timetable system venues
     */
    public Venue[] obtainAllVenues() {
        Venue[] result = new Venue[venues.size()];
        result = venues.toArray(result);
        return result;


    }

    /**
     * Searches for a given timetabled meeting
     *
     * @param eventId the meeting to search for
     * @return The found meeting or else null if not found
     */
    public Event searchForEvent(int eventId) {
        Event result = null;
        Event[] events = obtainAllEvents();

        for (Event m : events) {
            if (m.getEventId() == eventId)
                result = m;
        }
        return result;


    }

    /**
     * Searches for the given venue
     *
     * @param name The name of the venue. Must not be null.
     * @return The venue if found else null
     */
    public Venue searchForVenue(String name) {
        Venue result = null;
        Venue[] venues = obtainAllVenues();

        for (Venue v : venues) {
            if (v.getName().equals(name))
                result = v;
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuilder resultsBuilder = new StringBuilder("Venues in timetable system are: ");
        for (Venue venue : venues) {
            resultsBuilder.append(venue).append("\n");
        }
        String results = resultsBuilder.toString();
        results += "Event in timetable are:\n";
        StringBuilder resultsBuilder1 = new StringBuilder(results);
        for (Event event : events) {
            resultsBuilder1.append(event).append("\n");
        }
        results = resultsBuilder1.toString();
        return results;
    }

    /**
     * Loads the timetable data from the given file
     *
     * @param filename The text file. Must exist.
     * @throws FileNotFoundException thrown if the file does not exist
     * @throws IOException           thrown if some other kind of IO error occurs
     */
    public void load(String filename) throws FileNotFoundException, IOException {
        // Using try-with-resource (see my slides from workshop 15)
        try (FileReader fr = new FileReader(filename);
             BufferedReader br = new BufferedReader(fr);
             Scanner infile = new Scanner(br)) {

            events.clear();
            venues.clear();

            infile.useDelimiter("\r?\n|\r"); // End of line characters on Unix or DOS or others OSs

            // Read in the venues first
            Event event = null;
            Venue venue = null;
            int numVenues = infile.nextInt();

            for (int i = 0; i < numVenues; i++) {
                String venueName = infile.next();
                boolean hasDataProjector = infile.nextBoolean();

                venue = new Venue(venueName);
                venue.setHasDataProjector(hasDataProjector);
                venues.add(venue);
            }

            while (infile.hasNext()) {
                if (infile.next().equals(EventType.LECTURE.toString())) {
                    event = new Lecture(infile);

                    // Read the venue data
                    //String bin = infile.next();
                    // String next = infile.next();
                    String venueName = infile.next();
                    Venue theVenue = searchForVenue(venueName);
                    event.setVenue(theVenue);  // Currently not working

                } else {
                    event = new Meeting(infile);

                    // Read the venue data
                    //String next = infile.next();
                    String venueName = infile.next();
                    Venue theVenue = searchForVenue(venueName);
                    event.setVenue(theVenue);  // Currently not working

                }


                events.add(event);
            }
        }
    }

    /**
     * Saves the timetabled data to a text file
     *
     * @param outfileName The file. Will create a new file if it does not exist. Will overwrite an
     *                    existing file.
     * @throws IOException Thrown if some IO problem occurs.
     */
    public void save(String outfileName) throws IOException {
        try (FileWriter fw = new FileWriter(outfileName);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter outfile = new PrintWriter(bw)) {

            // Start by saving the list of allowable venues.
            // We save the number of venues first so that later on
            // we know how many to read in
            outfile.println(venues.size());
            Venue venue;
            for (Venue value : venues) {
                venue = value;
                outfile.println(venue.getName());
                outfile.println(venue.hasDataProjector());
            }

            Event event;
            for (int pos = 0; pos < events.size(); pos++) {
                event = events.get(pos);
                if (event instanceof Lecture) {
                    Lecture lecture = (Lecture) event;
                    lecture.save(outfile);
                } else {
                    Meeting meeting = (Meeting) event;
                    meeting.save(outfile);
                }


                venue = event.getVenue();

                outfile.print(venue.getName());

                // Only print a newline if we're not at the end of the list. Don't want a trailing blank line
                if (pos < events.size() - 1) {
                    outfile.println();
                }
            }
        }
    }


    public ArrayList<Event> getEvents() {
        return events;
    }

    public ArrayList<Venue> getVenues() {
        return venues;
    }
}