import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class DataGenerator {
    private final ArrayList<String> carSizeList;
    private final ArrayList<String> paymentList;
    private final ArrayList<Record> recordList;

    public DataGenerator() {
        carSizeList = new ArrayList<>();
        paymentList = new ArrayList<>();
        recordList = new ArrayList<>();
    }

    /* PLEASE DO NOT MODIFY
    public static void main(String... args) {
        DataGenerator dataGenerator = new DataGenerator();
        dataGenerator.initCarSizeList();
        dataGenerator.initPaymentList();
        dataGenerator.generateData();
        dataGenerator.generateToCSVFile();
    }*/

    public void initCarSizeList() {
        carSizeList.add("s");
        carSizeList.add("m");
        carSizeList.add("l");
    }

    public void initPaymentList() {
        paymentList.add("cash");
        paymentList.add("credit");
        paymentList.add("debit");
    }

    public void generateData() {
        for (int i = 0; i < Configuration.INSTANCE.maximumNumberOfRecords; i++) {
            int randomCarSizeIndex = Configuration.INSTANCE.randomNumberGenerator.nextInt(0, carSizeList.size() - 1);
            int randomPaymentIndex = Configuration.INSTANCE.randomNumberGenerator.nextInt(0, paymentList.size() - 1);
            Record record = new Record(i + 1, Configuration.INSTANCE.randomNumberGenerator.nextInt(1, 3),
                    carSizeList.get(randomCarSizeIndex), Configuration.INSTANCE.randomNumberGenerator.nextInt(1, 5),
                    Configuration.INSTANCE.randomNumberGenerator.nextInt(1, 450),
                    Configuration.INSTANCE.randomNumberGenerator.nextInt(1, 21),
                    paymentList.get(randomPaymentIndex));
            recordList.add(record);
        }
    }

    public void generateToCSVFile() {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(Configuration.INSTANCE.dataPath + "records.csv"));

            for (Record record : recordList) {
                bufferedWriter.write(record.toString() + Configuration.INSTANCE.lineSeparator);
            }

            bufferedWriter.close();
        } catch (IOException ioe) {
            System.out.println(ioe.getMessage());
        }
    }
}