package assignment.utils.csv;

import java.util.Map;
import java.util.Set;

public interface CsvRecord {

    Map<String, String> getColumnData();

    String getValue(final String columnName);

    Set<String> columnNames();

}
