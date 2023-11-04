package application;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class FileManager {
	private static final String ccyfilepath = "C:\\Users\\IanFong\\Desktop\\CSIA\\ccytypes.csv";
	private static final String stratscodefilepath = "C:\\Users\\IanFong\\Desktop\\CSIA\\stratscodetypes.csv";
	
	public static void writeCryptoTypes(ObservableList<String> cryptoTypes) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(ccyfilepath))) {
            for (String cryptoType : cryptoTypes) {
                writer.writeNext(new String[]{cryptoType});
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	public static ObservableList<String> readCryptoTypes() {
        ObservableList<String> cryptoTypes = FXCollections.observableArrayList();
        try (CSVReader reader = new CSVReader(new FileReader(ccyfilepath))) {
            List<String[]> lines = reader.readAll();
            for (String[] line : lines) {
                cryptoTypes.add(line[0]);
            }
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
        return cryptoTypes;
    }
	public static void writeStratscodeTypes(ObservableList<String> stratscodeTypes) {
        try (CSVWriter writer = new CSVWriter(new FileWriter(stratscodefilepath))) {
            for (String stratscodeType : stratscodeTypes) {
                writer.writeNext(new String[]{stratscodeType});
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	public static ObservableList<String> readStratscodeTypes() {
        ObservableList<String> cryptoTypes = FXCollections.observableArrayList();
        try (CSVReader reader = new CSVReader(new FileReader(stratscodefilepath))) {
            List<String[]> lines = reader.readAll();
            for (String[] line : lines) {
                cryptoTypes.add(line[0]);
            }
        } catch (IOException | CsvException e) {
            e.printStackTrace();
        }
        return cryptoTypes;
    }
}
