import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * The timetable system
 *
 * @author Chris Loftus
 * @version 1 (27th February 2020)
 */
public class TimetableApp {

    private String filename;
    private Scanner scan;
    private Timetable timetable;
    private Venue venue;
    private Event event;

    private TimetableApp() {
        scan = new Scanner(System.in);
        System.out.print("Please enter the filename of timetable information: ");
        filename = scan.next();

        timetable = new Timetable();
    }

    /*
     * initialise() method runs from the main and reads from a file
     */
    private void initialise() {
        System.out.println("Using file " + filename);

        try {
            timetable.load(filename);
        } catch (FileNotFoundException e) {
            System.err.println("The file: " + " does not exist. Assuming first use and an empty file." +
                    " If this is not the first use then have you accidentally deleted the file?");
        } catch (IOException e) {
            System.err.println("An unexpected error occurred when trying to open the file " + filename);
            System.err.println(e.getMessage());
        }
    }

    /*
     * runMenu() method runs from the main and allows entry of data etc
     */
    private void runMenu() {
        String response;
        do {
            printMenu();
            System.out.println("What would you like to do:");
            scan = new Scanner(System.in);
            response = scan.nextLine().toUpperCase();
            switch (response) {
                case "1":
                    addMeeting();
                    break;
                case "2":
                    addLecture();
                    break;
                case "3":
                    searchForEvent();
                    break;
                case "4":
                    removeEvent();
                    break;
                case "5":
                    addVenue();
                    break;
                case "6":
                    printAll();
                    break;
                case "Q":
                    break;
                default:
                    System.out.println("Try again");
            }
        } while (!(response.equals("Q")));
    }

    private void printMenu() {
        System.out.println("1 -  add a new Meeting slot");
        System.out.println("2 -  add a new Lecture slot");
        System.out.println("3 -  search for a booked timetable event");
        System.out.println("4 -  remove a timetable event");
        System.out.println("5 -  add a venue");
        System.out.println("6 -  display everything");
        System.out.println("q -  Quit");
    }

    private void addMeeting() {
        System.out.println("Enter meeting name: ");
        String meetingName = scan.nextLine();

        System.out.println("Enter organiser name: ");
        String organiser = scan.nextLine();

        MeetingType meetingType = getMeetingType();

        Meeting meeting = new Meeting();

        meeting.setMeetingName(meetingName);
        meeting.setOrganiser(organiser);
        meeting.setMeetingType(meetingType);

        populateAndAddToTimetable(meeting);
    }

    private void addLecture() {
        System.out.println("Enter module code");
        String lectureCode = scan.nextLine();

        System.out.println("Enter lecturer name: ");
        String lecturerName = scan.nextLine();

        System.out.println("Enter lecturer phone number");
        String lecturerPhonenumber = scan.nextLine();

        System.out.println("Is adjustable seating is needed?(Y/N)");
        String answer = scan.nextLine().toUpperCase();
        boolean seating = true;
        if (answer.equals("N")) {
            seating = false;
        }


        Lecture lecture = new Lecture();

        lecture.setModuleCode(lectureCode);
        lecture.setLecturerName(lecturerName);
        lecture.setPhoneNumber(lecturerPhonenumber);
        lecture.setAdjustableSeating(seating);

        populateAndAddToTimetable(lecture);
    }

    private void searchForEvent() {
        System.out.println("Enter unique meeting ID?");
        int eventId = scan.nextInt(); // STUDENTS MUST HANDLE ILLEGAL INPUT
        scan.nextLine();

        Event event = timetable.searchForEvent(eventId);
        if (event != null) {
            System.out.println(event);
        } else {
            System.out.println("Could not find booked timetable event: " + eventId);
        }
    }

    private void removeEvent() {
        System.out.println("Which booked event do you want to remove? Enter its ID: ");
        int eventId = scan.nextInt(); // STUDENTS MUST HANDLE BAD INPUT HERE
        scan.nextLine();
        boolean removed = timetable.removeEvent(eventId);
        if (removed) {
            System.out.println("Removed event " + eventId);
        } else {
            System.out.println("Unable to find event " + eventId);
        }
    }

    private void addVenue() {
        Venue venue;
        String venueName;

        while (true) {
            System.out.println("Enter the venue name");
            venueName = scan.nextLine();
            venue = timetable.searchForVenue(venueName);
            if (venue != null) {
                System.out.println("This venue already exists. Give it a different name");
            } else {
                break;
            }
        }

        System.out.println("Does it have a data projector?(Y/N)");
        String answer = scan.nextLine().toUpperCase();
        boolean hasDataProjector = answer.equals("Y");

        venue = new Venue(venueName);
        venue.setHasDataProjector(hasDataProjector);

        timetable.add(venue);
    }

    private void printAll() {


        ArrayList<Event> events = timetable.getEvents();
        ArrayList<Venue> venues = timetable.getVenues();

        for (int i = 0; i < venues.size(); i++) {
            for (int j = i + 1; j < venues.size(); j++) {
                if (venues.get(i).getName().compareTo(venues.get(j).getName()) > 0) {
                    Venue temp1 = venues.get(i);
                    Venue temp2 = venues.get(j);
                    venues.set(i, temp2);
                    venues.set(j, temp1);
                }
            }
        }

        for (int i = 0; i < events.size(); i++) {
            for (int j = i + 1; j < events.size(); j++) {
                long millis = events.get(i).getStartTime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
                long millis2 = events.get(j).getStartTime().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
                if (millis > millis2) {
                    Event event = events.get(i);
                    Event event1 = events.get(j);
                    events.set(i, event1);
                    events.set(j, event);
                } else if (millis == millis2) {
                    String name = events.get(i).getVenue().getName();
                    String name1 = events.get(j).getVenue().getName();
                    if (name.compareTo(name1) > 0) {
                        Event event = events.get(i);
                        Event event1 = events.get(j);
                        events.set(i, event1);
                        events.set(j, event);
                    }
                }
            }
        }

        System.out.println(timetable.toString());
    }

    private void populateAndAddToTimetable(Event event) {
        System.out.println("Enter the unique timetable event identifier: (unique number)");
        ArrayList<Event> events = timetable.getEvents();


        List<Integer> ids = new ArrayList<>();
        for (Event element : events) {
            ids.add(element.getEventId());
        }

        int eventId = scan.nextInt();
        do {
            if (ids.contains(eventId)) {
                System.err.println("Incorrect ID try again");
                eventId = scan.nextInt();
            }
        } while (ids.contains(eventId));


        scan.nextLine();

        inputStartEndTime(event);

        System.out.println("Is a data projector required?(Y/N)");
        String answer = scan.nextLine().toUpperCase();
        boolean projectorRequired = true;
        if (answer.equals("N")) {
            projectorRequired = false;
        }

        Venue venue = null;
        while (true) {
            System.out.println("Enter venue name");
            String venueName = scan.nextLine();
            venue = timetable.searchForVenue(venueName);
            if (venue != null) {
                if (projectorRequired && !venue.hasDataProjector()) {
                    System.out.println("Selected venue does not have a data projector. Choose a different venue");
                } else {
                    event.setEventId(eventId);
                    event.setDataProjectorRequired(projectorRequired);

                    event.setVenue(venue);

                    timetable.add(event);
                    break; // out of the loop
                }
            } else {
                System.out.println("Venue " + venue + " does not exist. Try a different venue? (Y/N)");
                answer = scan.nextLine().toUpperCase();
                if (!answer.equals("Y")) break; // if not Y then break out of the loop
            }
        }
    }

    private void inputStartEndTime(Event event) {
        while (true) {
            System.out.println("Enter start time for timetable meeting");
            LocalDateTime startTime = getDateTime();
            System.out.println("Enter end time for timetable meeting");
            LocalDateTime endTime = getDateTime();
            if (startTime.compareTo(endTime) < 0) {
                event.setStartAndEndTime(startTime, endTime);
                break;
            } else {
                System.out.println("Start time: " + startTime + " must be before end time: " + endTime);
            }
        }
    }

    private LocalDateTime getDateTime() {
        LocalDateTime result = null;


        while (true) {
            try {
                System.out.println("On one line (numbers): year month day hour minutes");

                // Note that an InputMismatchException is thrown if an
                // illegal value is entered. For simplicity, we will pretend that won't happen.
                // STUDENTS MUST HANDLE THIS SITUATION.
                int year = validateInput("year", 2020, 2022);
                int month = validateInput("month", 1, 12);
                YearMonth yearMonth = YearMonth.of(year, month);
                int day = validateInput("day", 1, yearMonth.lengthOfMonth());
                int hour = validateInput("hour", 0, 24);
                int minutes = validateInput("minute", 0, 59);

                scan.nextLine(); // Clear the end of line character

                result = LocalDateTime.of(year, month, day, hour, minutes);
                break; // break out of the loop
            } catch (DateTimeException dte) {
                System.out.println("Try again. " + dte.getMessage());
            }
        }

        System.out.println("The date/time you entered was: " + result);
        return result;
    }

    private int validateInput(String valueType, int minVal, int maxVal) {
        int input;
        do {
            System.out.println(String.format("Enter %s:", valueType));
            input = scan.nextInt();
            if (input < minVal || input > maxVal) {
                System.err.println(String.format("Incorrect %s, try again", valueType));
            }
        }
        while (input < minVal || input > maxVal);
        return input;
    }

    private MeetingType getMeetingType() {
        MeetingType meetingType = MeetingType.OTHER; // Make the default
        System.out.println("Meeting type, enter the number (1 - staff meeting \n 2 - learning and teaching meeting \n 3 - subject panel meeting \n 4 - other kind of meeting");
        String answer = scan.nextLine();
        switch (answer) {
            case "1":
                meetingType = MeetingType.STAFF;
                break;
            case "2":
                meetingType = MeetingType.LTC;
                break;
            case "3":
                meetingType = MeetingType.SUBJECT_PANEL;
                break;
        }

        System.out.println("Meeting type selected: " + meetingType);
        return meetingType;
    }

    private void save() {
        try {
            timetable.save(filename);
        } catch (IOException e) {
            System.err.println("Problem when trying to write to file: " + filename);
        }
    }

    // /////////////////////////////////////////////////
    public static void main(String args[]) {
        System.out.println("**********HELLO***********");

        TimetableApp app = new TimetableApp();
        app.initialise();
        app.runMenu();
        app.printAll();
        app.save();

        System.out.println("***********GOODBYE**********");
    }
}
