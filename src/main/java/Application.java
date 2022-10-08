import java.io.BufferedReader;
import java.io.FileReader;
import java.time.Duration;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.averagingInt;
import static java.util.stream.Collectors.groupingBy;

public class Application implements IApplication {
    private final List<Record> recordList;

    public Application() {
        recordList = loadRecords();
    }

    public static void main(String... args) {
        Application application = new Application();
        application.loadRecords();

        application.executeQuery01();
        application.executeQuery02();
        application.executeQuery03();
        application.executeQuery04();
        application.executeQuery05();
        application.executeQuery06();
        application.executeQuery07();
        application.executeQuery08();
        application.executeQuery09();
        application.executeQuery10();
        application.executeQuery11();
        application.executeQuery12();
        application.executeQuery13();
        application.executeQuery14();
    }

    public List<Record> loadRecords() {
        List<Record> result = new ArrayList<>();

        //folder does not contain data
        //import data records.csv to main/java

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(Configuration.INSTANCE.dataPath + "records.csv")); //change path to main/java/records.csv
            String line;

            while ((line = bufferedReader.readLine()) != null) {
                String[] entries = line.split(";");
                int id = Integer.parseInt(entries[0]);
                int entrance = Integer.parseInt(entries[1]);
                String carSize = entries[2];
                int floor = Integer.parseInt(entries[3]);
                int parkingSpot = Integer.parseInt(entries[4]);
                int durationInDays = Integer.parseInt(entries[5]);
                String payment = entries[6];
                result.add(new Record(id, entrance, carSize, floor, parkingSpot, durationInDays, payment));
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return result;
    }

    public long executeQuery01() {
        System.out.println("--- query 01 (count)");
        System.out.println("SELECT COUNT(*) FROM data");
        long result = recordList.size();
        System.out.println("result : " + result);
        System.out.println();

        return result;
    }

    public void executeQuery02() {
        System.out.println("--- executeQuery02 ---");
        Predicate<Record> testAll=r->r.getEntrance()==2&&r.getCarSize().equals("m")&&r.getFloor()==3&& r.getDurationInDays()>=10;
        System.out.println("result: "+ recordList.stream().filter(testAll).toList().size());
    }

    public void executeQuery03() {
        System.out.println("--- executeQuery03 ---");
        Predicate<Record> entranceIsOne = record -> record.getEntrance() == 1;
        Predicate<Record> carSizeIsL = record -> record.getCarSize().equals("l");
        Predicate<Record> carSizeIsM = record -> record.getCarSize().equals("m");
        Predicate<Record> floorIsTwo = record -> record.getFloor() == 2;
        Predicate<Record> durationIsSmallerThanFour = record -> record.getDurationInDays() <= 3;
        Predicate<Record> paymentIsCash = record -> record.getPayment().equals("cash");
        List<Record> filteredList03 = recordList.stream().filter(entranceIsOne)
                .filter(carSizeIsL.or(carSizeIsM))
                .filter(floorIsTwo)
                .filter(durationIsSmallerThanFour)
                .filter(paymentIsCash)
                .toList();
        System.out.println(filteredList03.size());
        System.out.println();
    }

    public void executeQuery04() {
        System.out.println("--- executeQuery04 ---");
        Predicate<Record> testAll = r -> r.getEntrance() == 2&&r.getCarSize().equals("l")&&r.getFloor()!=3&&r.getFloor()!=5&&r.getDurationInDays()<=3&&r.getPayment().equals("credit");
        System.out.println(recordList.stream().filter(testAll).toList().size());
        System.out.println();

    }

    public void executeQuery05() {
        System.out.println("--- executeQuery05 ---");
        System.out.println();
    }

    public void executeQuery06() {
        Predicate<Record> testFloor=r->r.getFloor()!=1&&r.getFloor()!=3&&r.getFloor()!=5;
        Predicate<Record> testOthers=r-> r.getCarSize().equals("l")&&r.getParkingSpot()<=5;
        System.out.println("--- executeQuery06 ---");
        System.out.println("result: "+recordList.stream().filter(testOthers).filter(testFloor).collect(Collectors.averagingInt(Record::getDurationInDays)).intValue());
    }

    public void executeQuery07() {
        System.out.println("--- executeQuery07 ---");
        Predicate<Record> entranceIsOne = record -> record.getEntrance() == 1;
        Predicate<Record> entranceIsThree = record -> record.getEntrance() == 3;
        Predicate<Record> carSizeIsS = record -> record.getCarSize().equals("s");
        Predicate<Record> floorIsTwo = record -> record.getFloor() == 2;
        Predicate<Record> parkingSpotIsSmallerThanFour = record -> record.getParkingSpot() <= 3;
        Predicate<Record> durationIsGreaterThanOne = record -> record.getDurationInDays() <= 5;
        Predicate<Record> durationIsSmallerThanSix = record -> record.getDurationInDays() >= 2;
        Predicate<Record> paymentIsCash = record -> record.getPayment().equals("credit");
        Stream<Record> filteredStream07 = recordList.stream()
                .filter(entranceIsOne.or(entranceIsThree))
                .filter(carSizeIsS)
                .filter(floorIsTwo)
                .filter(parkingSpotIsSmallerThanFour)
                .filter(durationIsGreaterThanOne)
                .filter(durationIsSmallerThanSix)
                .filter(paymentIsCash);
        Comparator<Record> descendingDurationComparator = (Record record01, Record record02) -> (record02.getDurationInDays() - record01.getDurationInDays());
        List<Integer> filteredList07 = filteredStream07.sorted(descendingDurationComparator)
                .limit(3)
                .map(x -> x.getId())
                .toList();
        System.out.println(filteredList07);
        System.out.println();
    }

    public void executeQuery08() {
        System.out.println("--- executeQuery08 ---");
        Predicate<Record> testAll=r->r.getEntrance()==3&&(r.getCarSize().equals("s")||r.getCarSize().equals("l"))&&r.getFloor()==4&&r.getParkingSpot()>=4&&r.getParkingSpot()<=5&&r.getDurationInDays()<=5&&r.getPayment().equals("debit");
        recordList.stream().filter(testAll).sorted(Comparator.comparing(Record::getDurationInDays).reversed()).toList().forEach(r->System.out.println(r.getId()));;

    }

    public void executeQuery09() {
        System.out.println("--- executeQuery09 ---");
        Map<Integer,List<Record>>sizeMap=recordList.stream().collect(Collectors.groupingBy(Record::getFloor));
        sizeMap.forEach((k,v)->System.out.println(k+": "+v.size()));
        System.out.println();
    }

    public void executeQuery10() {
        System.out.println("--- executeQuery10 ---");
        Predicate<Record> testAll=r->r.getEntrance()==1&&r.getCarSize().equals("l")&&r.getParkingSpot()>=4&&r.getDurationInDays()<=5;
        Map<String,List<Record>> paymentMap=recordList.stream().filter(testAll).collect(Collectors.groupingBy(Record::getPayment));
        paymentMap.forEach((k,v)->System.out.println(k+": "+ v.size()));
        System.out.println();
    }

    public void executeQuery11() {
        System.out.println("--- executeQuery11 ---");
        Predicate<Record> testAll=r->r.getParkingSpot()>=4&&r.getDurationInDays()<=10&&r.getPayment().equals("credit");
        Map<String,List<Record>>sizeMap=recordList.stream().filter(testAll).collect(Collectors.groupingBy(Record::getCarSize));
        sizeMap.forEach((k,v)->System.out.println(k+": "+v.size()));
        System.out.println();
    }

    public void executeQuery12() {
        System.out.println("--- executeQuery12 ---");
        Predicate<Record> testAll=r->r.getEntrance()==3&& Objects.equals(r.getCarSize(), "m") &&r.getParkingSpot()!=2&&r.getParkingSpot()!=4&&r.getDurationInDays()<=5&& Objects.equals(r.getPayment(), "debit");
        Map<Integer,List<Record>>sizeMap=recordList.stream().filter(testAll).collect(Collectors.groupingBy(Record::getDurationInDays));
        sizeMap.forEach((k,v)->System.out.println(k+": "+v.size()));
        System.out.println();
    }

    public void executeQuery13() {
        System.out.println("--- executeQuery13 ---");
        System.out.println();
    }

    public void executeQuery14() {
        System.out.println("--- executeQuery14 ---");
        Predicate<Record> testAll=r->(r.getEntrance()==1||r.getEntrance()==3)&&(r.getCarSize().equals("s")||r.getCarSize().equals("m"))&&r.getParkingSpot()>=50&&r.getParkingSpot()<=100;
        Map<String,List<Record>> myMap=recordList.stream().filter(testAll).collect(groupingBy(Record::getCarSize));
        myMap.forEach((k,v)->System.out.println(k+"["+"average duration: "+v.stream().collect(Collectors.averagingInt(Record::getDurationInDays)).intValue()+" count: "+v.size()+"]"));

        System.out.println();
    }
}