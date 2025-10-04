package flatManagement;

public class DateCalculator {


     public static int[] GetEndDate (int begDay, int begMonth, int begYear, int noOfDays){

        int endDay = begDay;
        int endMonth = begMonth;
        int endYear = begYear;

        for (int i = noOfDays; i > 0; i--){

            switch (endMonth){
                case 1, 3, 5, 7, 8, 10 -> { //months with 31 days

                    if (endDay == 31){
                        endDay = 1;
                        endMonth++;
                    }
                    else
                        endDay++;

                }
                case 4, 6, 9, 11 -> { //months with 30 days

                    if (endDay == 30){
                        endDay = 1;
                        endMonth++;
                    }
                    else
                        endDay++;

                }
                case 12 -> {    //dec

                    if (endDay == 31){
                        endDay = 1;
                        endMonth = 1;
                        endYear++;
                    }
                    else
                        endDay++;

                }
                case 2 ->{      //feb

                    if (endDay == 28 && endYear % 4 != 0 || endDay == 28  && endYear % 100 != 0 || endDay == 29){
                        endDay = 1;
                        endMonth = 3;
                    } else
                        endDay++;

                }

            }

        }

        return new int[]{endDay, endMonth, endYear};

    }

    public static int[] GetEndDate (int begDay, int begMonth, int begYear, int noOfMonths, int noOfYears) {

        int endDay;
        int endMonth;
        int endYear;

        endMonth = (begMonth + noOfMonths) % 12;
        if (endMonth == 0)
            endMonth = 12;
        endYear = (begYear + (begMonth + noOfMonths - 1) / 12 + noOfYears);

        if (endYear % 4 == 0 && endYear % 100 != 0 || endYear % 400 == 0){      //if the endYear is a leap year

            if (endMonth == 2){

                if (begDay == 31 || begDay == 30)
                    endDay = 29;
                else
                    endDay = begDay;

            } else
                endDay = begDay;


        } else {

            if (endMonth == 2){

                if (begDay == 31 || begDay == 30 || begDay == 29 )
                    endDay = 28;
                else
                    endDay = begDay;

            } else
                endDay = begDay;

        }

        return new int[]{endDay, endMonth, endYear};

    }

    public static int[] GenRandomDate (int minYear, int maxYear){


         int Year = minYear + (int)(Math.random() * (maxYear - minYear + 1));
         int Month = 1+ (int)(Math.random() * 12);
         int Day;

         switch (Month){
             case 1, 3, 5, 7, 8, 10, 12 -> Day = 1 + (int)(Math.random() * 31);
             case 2 ->{

                 if (Year % 4 == 0 && Year % 100 != 0 || Year % 400 == 0)
                     Day = 1 + (int)(Math.random() * 29);
                 else
                     Day = 1 + (int)(Math.random() * 28);

             }
             default -> Day = 1 + (int)(Math.random() * 30);
         }

         return new int[]{ Day, Month, Year};

    }

    public static boolean CheckDate (int day, int month, int year, int minYear, int maxYear){

         boolean check = true;

         if (year < minYear || year > maxYear)
             check = false;

         if (month < 1 || month > 12)
             check = false;

         if (day < 1)
             check = false;

         switch (month){
            case 1, 3, 5, 7, 8, 10, 12 -> {

                if (day > 31)
                    check = false;

            }
            case 2 ->{

                if ((year % 4 == 0 && year % 100 != 0 || year % 400 == 0) && day > 29)
                    check = false;
                else if (day > 28)
                    check = false;

            }
            default -> {

                if (day > 30)
                    check = false;

            }
        }

        return check;

    }

    public static boolean IsDate2Greater (int[] date1, int[] date2){

         //dateX[0] - DayX, dateX[1] - MonthX, dateX[2] - YearX


         boolean isGreater = false;

         if (date2[2] > date1[2])
             isGreater = true;
         else if (date2[2] == date1[2]){

             if (date2[1] > date1[1])
                 isGreater = true;
             else if (date2[1] == date1[1] && date2[0] > date1[0])
                 isGreater = true;

         }

         return isGreater;

    }

}
