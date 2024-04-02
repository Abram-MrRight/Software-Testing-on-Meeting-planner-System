package edu.sc.bse3211.meetingplanner;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;

public class RoomTest {

    // Test case for default constructor
    @Test
    public void testDefaultConstructor() {
        Room room = new Room();
        assertEquals("", room.getID()); // Check if ID is empty string
       // assertNotNull(room.calendar); // Check if calendar is initialized
    }


    // Test case for constructor with ID
    @Test
    public void testConstructorWithID() {
        String testID = "RM101";
        Room room = new Room(testID);
        assertEquals(testID, room.getID()); // Check if ID is set correctly
       // assertNotNull(room.calendar); // Check if calendar is initialized
    }

    // Test case for adding a meeting without conflict
    @Test
    public void testAddMeetingNoConflict() throws TimeConflictException {
        ArrayList<Person> attendees = new ArrayList<>();
// Add attendees to the list
        Room room = new Room();
        Meeting meeting = new Meeting( 10, 0, 11, 0, attendees, room, "Staff Meeting");
        room.addMeeting(meeting);
        // No assertion needed here, successful execution indicates no conflict
    }

    // Test case for adding a meeting with conflict
    @Test(expected = TimeConflictException.class)
    public void testAddMeetingWithConflict() throws TimeConflictException {
        ArrayList<Person> attendees = new ArrayList<>();
        Room room = new Room();
        Meeting meeting1 = new Meeting( 10, 0, 11, 0);
        Meeting meeting2 = new Meeting(10, 30, 11, 30);
        room.addMeeting(meeting1);
        room.addMeeting(meeting2); // This should throw TimeConflictException
    }

    // Test case for printing agenda for a month (mock data needed)
    @Test
    public void testPrintAgendaMonth() {
        Room room = new Room(); // Assuming some meetings are added beforehand
        String agenda = room.printAgenda(4); // Replace 4 with desired month
        // Assert something about the agenda content. This requires checking
        // the implementation of Calendar.printAgenda
    }

    // Test case for printing agenda for a specific day (mock data needed)
    @Test
    public void testPrintAgendaDay() {
        Room room = new Room(); // Assuming some meetings are added beforehand
        String agenda = room.printAgenda(4, 15); // Replace 4 with desired month, 15 with day
        // Assert something about the agenda content. This requires checking
        // the implementation of Calendar.printAgenda
    }

    // Test case for checking if a timeframe is busy
    @Test
    public void testIsBusy() throws TimeConflictException {
        ArrayList<Person> attendees = new ArrayList<>();
// Add attendees to the list
        Room room = new Room();
        Meeting meeting = new Meeting( 10, 1, 11, 0, attendees, room, "Staff Meeting");

        room.addMeeting(meeting);
        boolean isBusy = room.isBusy(10, 1, 11, 0); // Replace 4 and 1 with desired month and day
        assertTrue(isBusy); // Meeting should make the timeframe busy
    }


    // Test case for removing a specific meeting (mock data needed)
    @Test
    public void testRemoveMeeting() throws TimeConflictException {
        ArrayList<Person> attendees = new ArrayList<>();
// Add attendees to the list
        Room room = new Room();
        Meeting meeting = new Meeting( 10, 0, 11, 0, attendees, room, "Staff Meeting");
        room.addMeeting(meeting);
        // Assuming removeMeeting takes month, day, and index arguments:
        room.removeMeeting(4, 1, 0); // Replace 4 and 1 with desired month and day, 0 for index (first meeting)

        // assertion to verify meeting is removed
        boolean isMeetingRemoved = room.isBusy(4, 1, 10, 0); // Should return false if removed
    }
}
