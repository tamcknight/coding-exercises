import java.util.HashMap;
import java.util.Map;

/**
 * Created by tim on 5/1/17.
 */
public class Dates {


    public static void main( String args [] ){

        System.out.print(numDaysBetween(2009, 03, 15, 2014,8,4));

    }

    public static Integer numDaysBetween(Integer startYear, Integer startMonth, Integer startDay,
                                         Integer endYear ,Integer endMonth, Integer endDay){
        Integer daysInbetween = null;


        //first you get the 'bookends'. the days from the first date to teh end of the year and
        // and days up to the date in the last year.
        if (startYear.intValue() != endYear.intValue()) {
            Integer daysInStartYear = getDaysinYear(startYear) - getDayOfYear(startMonth, startDay);
            Integer daysInEndYear = getDayOfYear(endMonth, endDay);

            daysInbetween = daysInStartYear + daysInEndYear;

            //now we need to add all the years in the middle
            for (Integer year = startYear+1;  year < endYear; year++){
                daysInbetween = daysInbetween+ getDaysinYear(year);
            }
        } else {
            //take the day of the later date and subtract it from the earlier date to get the days between
            daysInbetween = getDayOfYear(endMonth, endDay) - getDayOfYear(startMonth, startDay);
        }
        return daysInbetween;
    }


    public static Integer numDaysInMonth(Integer year, Integer month){


        Map<Integer, Integer> map = new HashMap<Integer, Integer>();

        map.put(1,31);
        map.put(2,28);
        map.put(3,31);
        map.put(4,30);
        map.put(5,31);
        map.put(6,30);
        map.put(7,31);
        map.put(8,31);
        map.put(9,30);
        map.put(10,31);
        map.put(11,30);
        map.put(12,31);

        return map.get(month);
    }

    public static Integer getDayOfYear(Integer month, Integer day){
        //initialize on the day of the month for the month requested.  We will add to this.
        Integer dayOfYear = day;
        //start in january

        Integer startMonth = 1;
        while (startMonth < month  && startMonth < 13){
            dayOfYear = dayOfYear+numDaysInMonth(null, startMonth);
            startMonth++;
        }
        return dayOfYear;
    }

    public static Integer getDaysinYear(Integer year) {

        Integer daysInYear = 0;

        for (int i = 1; i <= 12; i++) {
            daysInYear = daysInYear + numDaysInMonth(year, i);
        }
        return daysInYear;
    }
}
