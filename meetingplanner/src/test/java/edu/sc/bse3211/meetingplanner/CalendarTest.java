package edu.sc.bse3211.meetingplanner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;

import org.junit.Test;

public class CalendarTest {

    @Test
    public void testAddMeeting_holiday() {
        Calendar calendar = new Calendar();
        try {
            Meeting janan = new Meeting(2, 16, "Janan Luwum");
            calendar.addMeeting(janan);
            boolean added = calendar.isBusy(2, 16, 0, 23);
            assertTrue("Janan Luwum Day should be marked as busy on the calendar", added);
        } catch (TimeConflictException e) {
            fail("Should not throw exception: " + e.getMessage());
        }
    }

    @Test
    public void testIsBusy_FreeTimeSlot() {
        Calendar calendar = new Calendar();
        try {
            boolean isFree = calendar.isBusy(3, 15, 10, 11);
            assertTrue("Time slot should be free", !isFree);
        } catch (TimeConflictException e) {
            fail("Should not throw exception: " + e.getMessage());
        }
    }

    @Test
    public void testIsBusy_OccupiedTimeSlot() {
        Calendar calendar = new Calendar();
        try {
            Meeting meeting = new Meeting(3, 20, 13, 14);
            calendar.addMeeting(meeting);
            boolean isOccupied = calendar.isBusy(3, 20, 13, 14);
            assertTrue("Time slot should be occupied", isOccupied);
        } catch (TimeConflictException e) {
            fail("Should not throw exception: " + e.getMessage());
        }
    }

    @Test
    public void testIsBusy_PartialOverlap() {
        Calendar calendar = new Calendar();
        try {
            Meeting meeting = new Meeting(3, 20, 9, 12);
            calendar.addMeeting(meeting);
            boolean isPartiallyOccupied = calendar.isBusy(3, 20, 10, 13);
            assertTrue("Time slot should be partially occupied", isPartiallyOccupied);
        } catch (TimeConflictException e) {
            fail("Should not throw exception: " + e.getMessage());
        }
    }

    @Test(expected = TimeConflictException.class)
    public void testIsBusy_TimeConflictException() throws TimeConflictException {
        Calendar calendar = new Calendar();
        calendar.isBusy(2, 33, 8, 9);
    }

    @Test(expected = TimeConflictException.class)
    public void testIsBusy_NegativeTimeInputs() throws TimeConflictException {
        Calendar calendar = new Calendar();
        calendar.isBusy(3, 10, -2, -1);
    }

    @Test(expected = TimeConflictException.class)
    public void testIsBusy_EndTimeBeforeStartTime() throws TimeConflictException {
        Calendar calendar = new Calendar();
        calendar.isBusy(3, 15, 14, 12);
    }

    @Test
    public void testAddMeeting_AddingMeeting() {
        Calendar calendar = new Calendar();
        try {
            ArrayList<Person> employees = new ArrayList<Person>();
            employees.add(new Person("Namugga Martha"));
            employees.add(new Person("Shema Collins"));
            Meeting meeting = new Meeting(3, 15, 9, 12, employees, new Room("LLT1B"), "Team Meeting");
            calendar.addMeeting(meeting);
            Meeting addedMeeting = calendar.getMeeting(3, 15, 0);
            assertEquals("Team Meeting", addedMeeting.getDescription());
        } catch (TimeConflictException e) {
            fail("Should not throw exception: " + e.getMessage());
        }
    }

    @Test(expected = TimeConflictException.class)
    public void testAddMeeting_AddingMeetingWithConflict() throws TimeConflictException {
        Calendar calendar = new Calendar();
        ArrayList<Person> employees = new ArrayList<Person>();
        employees.add(new Person("Namugga Martha"));
        employees.add(new Person("Shema Collins"));
        Meeting existingMeeting = new Meeting(3, 15, 9, 12, employees, new Room("LLT1B"), "Interview");
        calendar.addMeeting(existingMeeting);
        Meeting conflictingMeeting = new Meeting(3, 15, 9, 12, employees, new Room("LLT1B"), "Team Meeting");
        calendar.addMeeting(conflictingMeeting);
    }

    @Test(expected = TimeConflictException.class)
    public void testAddMeeting_InvalidDate() throws TimeConflictException {
        Calendar calendar = new Calendar();
        Meeting invalidDateMeeting = new Meeting(13, 32, 10, 11);
        calendar.addMeeting(invalidDateMeeting);
    }

    @Test(expected = TimeConflictException.class)
    public void testAddMeeting_InvalidTime() throws TimeConflictException {
        Calendar calendar = new Calendar();
        Meeting invalidTimeMeeting = new Meeting(3, 15, -2, -1);
        calendar.addMeeting(invalidTimeMeeting);
    }

    @Test(expected = TimeConflictException.class)
    public void testAddMeeting_EndBeforeStart() throws TimeConflictException {
        Calendar calendar = new Calendar();
        Meeting endBeforeStartMeeting = new Meeting(3, 15, 14, 12);
        calendar.addMeeting(endBeforeStartMeeting);
    }

    @Test
    public void testClearSchedule_ClearOnValidDay() {
        Calendar calendar = new Calendar();
        calendar.clearSchedule(3, 15);
        assertTrue("No meetings should be scheduled", calendar.printAgenda(3, 15).equals("Agenda for "+3+"/"+15+":\n"));
    }

    @Test
    public void testClearSchedule_ClearOnDayWithNoMeetings() {
        Calendar calendar = new Calendar();
        calendar.clearSchedule(3, 20);
        assertTrue("No meetings should be scheduled", calendar.printAgenda(3, 20).equals("Agenda for "+3+"/"+20+":\n"));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testClearSchedule_ClearOnInvalidDay() {
        Calendar calendar = new Calendar();
        calendar.clearSchedule(2, 32);
    }

    // @Test
    // public void testPrintAgenda_PrintForMonth() {
    //     Calendar calendar = new Calendar();
    //     String agenda = calendar.printAgenda(3);
    //     assertTrue("Agenda should not be empty", agenda.isEmpty());
    // }

    // @Test
    // public void testPrintAgenda_PrintForDay() {
    //     Calendar calendar = new Calendar();
    //     String agenda = calendar.printAgenda(3, 20);
    //     assertTrue("Agenda should not be empty", agenda.isEmpty());
    // }

    @Test
    public void testPrintAgenda_PrintForDayWithNoMeetings() {
        Calendar calendar = new Calendar();
        String agenda = calendar.printAgenda(3, 25);
        assertTrue("Agenda should be empty", agenda.equals("Agenda for "+3+"/"+25+":\n"));
    }

    // @Test(expected = TimeConflictException.class)
    // public void testPrintAgenda_PrintForNonExistentDay() throws TimeConflictException {
    //     Calendar calendar = new Calendar();
    //     calendar.printAgenda(3, 32);
    // }

    @Test
    public void testGetMeeting_RetrieveExistingMeeting() {
        Calendar calendar = new Calendar();
        try {
            Meeting meeting = new Meeting(3, 15, "Team Meeting");
            calendar.addMeeting(meeting);
            Meeting retrievedMeeting = calendar.getMeeting(3, 15, 0);
            assertEquals("Team Meeting", retrievedMeeting.getDescription());
        } catch (TimeConflictException e) {
            fail("Should not throw exception: " + e.getMessage());
        }
    }

    // @Test
    // public void testGetMeeting_RetrieveFromEmptyDay() {
    //     Calendar calendar = new Calendar();
    //     Meeting retrievedMeeting = calendar.getMeeting(3, 20, 0);
    //     assertEquals(null, retrievedMeeting);
    // }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetMeeting_RetrieveWithInvalidIndex() {
        Calendar calendar = new Calendar();
        calendar.getMeeting(3, 25, 5);
    }

    // @Test(expected = TimeConflictException.class)
    // public void testGetMeeting_RetrieveFromNonExistentDay() throws TimeConflictException {
    //     Calendar calendar = new Calendar();
    //     calendar.getMeeting(2, 30, 0);
    // }

    @Test
    public void testRemoveMeeting_RemoveExistingMeeting() {
        Calendar calendar = new Calendar();
        try {
            Meeting meeting = new Meeting(3, 15, 10, 11);
            calendar.addMeeting(meeting);
            calendar.removeMeeting(3, 15, 0);
            assertTrue("No meetings should be scheduled", calendar.printAgenda(3, 15).equals("Agenda for "+3+"/"+15+":\n"));
        } catch (TimeConflictException e) {
            fail("Should not throw exception: " + e.getMessage());
        }
    }

    @Test
    public void testRemoveMeeting_RemoveNonExistingMeeting() {
        Calendar calendar = new Calendar();
        calendar.removeMeeting(3, 25, 0);
        assertTrue("No meetings should be scheduled", calendar.printAgenda(3, 25).equals("Agenda for "+3+"/"+25+":\n"));
    }

    // @Test(expected = TimeConflictException.class)
    // public void testRemoveMeeting_RemoveFromNonExistentDay() throws TimeConflictException {
    //     Calendar calendar = new Calendar();
    //     calendar.removeMeeting(2, 30, 0);
    // }

    @Test
    public void testCheckTimes_ValidDateTime() {
        try {
            Calendar.checkTimes(3, 15, 10, 11);
        } catch (TimeConflictException e) {
            fail("Should not throw exception: " + e.getMessage());
        }
    }

    // @Test(expected = TimeConflictException.class)
    // public void testCheckTimes_InvalidDay() throws TimeConflictException {
    //     Calendar.checkTimes(2, 30, 10, 11);
    // }

    @Test(expected = TimeConflictException.class)
    public void testCheckTimes_InvalidTime_Negative() throws TimeConflictException {
        Calendar.checkTimes(3, 15, -2, -1);
    }

    @Test(expected = TimeConflictException.class)
    public void testCheckTimes_InvalidTime_EndBeforeStart() throws TimeConflictException {
        Calendar.checkTimes(3, 15, 14, 12);
    }

    @Test(expected = TimeConflictException.class)
    public void testCheckTimes_InvalidMonth() throws TimeConflictException {
        Calendar.checkTimes(0, 15, 10, 11);
    }

    @Test(expected = TimeConflictException.class)
    public void testCheckTimes_InvalidMonth_BeyondRange() throws TimeConflictException {
        Calendar.checkTimes(13, 15, 10, 11);
    }
}
