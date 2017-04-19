package wdsr.exercise3.hr;

import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import wdsr.exercise3.hr.ws.EmployeeType;
import wdsr.exercise3.hr.ws.HolidayRequest;
import wdsr.exercise3.hr.ws.HolidayResponse;
import wdsr.exercise3.hr.ws.HolidayType;
import wdsr.exercise3.hr.ws.HumanResource;
import wdsr.exercise3.hr.ws.HumanResourceService;

// TODO Complete this class to book holidays by issuing a request to Human Resource web service.
// In order to see definition of the Human Resource web service:
// 1. Run HolidayServerApp.
// 2. Go to http://localhost:8090/holidayService/?wsdl
public class HolidayClient {
	/**
	 * Creates this object
	 * @param wsdlLocation URL of the Human Resource web service WSDL
	 */
	private HumanResourceService humanResourceService;
	
	public HolidayClient(URL wsdlLocation) {
		humanResourceService = new HumanResourceService(wsdlLocation);
	}
	
	/**
	 * Sends a holiday request to the HumanResourceService.
	 * @param employeeId Employee ID
	 * @param firstName First name of employee
	 * @param lastName Last name of employee
	 * @param startDate First day of the requested holiday
	 * @param endDate Last day of the requested holiday
	 * @return Identifier of the request, if accepted.
	 * @throws ProcessingException if request processing fails.
	 */
	public int bookHoliday(int employeeId, String firstName, String lastName, Date startDate, Date endDate) throws ProcessingException {
		HolidayRequest holidayRequest = new HolidayRequest();
		
		HolidayType holidayType = new HolidayType();
		holidayType.setEndDate(endDate);
		holidayType.setStartDate(startDate);
		holidayRequest.setHoliday(holidayType);
		
		EmployeeType employeeType = new EmployeeType();
		employeeType.setFirstName(firstName);
		employeeType.setLastName(lastName);
		employeeType.setNumber(employeeId);
		holidayRequest.setEmployee(employeeType);
		
		HumanResource humanResource = humanResourceService.getHumanResourcePort();
		try {
			HolidayResponse holidayResponse;
			holidayResponse = humanResource.holiday(holidayRequest);
			return holidayResponse.getRequestId();
		} catch (Exception e) {
			throw new ProcessingException();
		}
		
	}
	
	public XMLGregorianCalendar getCalendarValue(Date date) throws ParseException, DatatypeConfigurationException
	{
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.setFirstDayOfWeek(Calendar.MONDAY);
		calendar.set(Calendar.MONTH, 6);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.YEAR, 2013);
		
		 XMLGregorianCalendar calendarToReturn = DatatypeFactory.newInstance()
		            .newXMLGregorianCalendar(calendar);
		    
		return calendarToReturn;
	}
}
