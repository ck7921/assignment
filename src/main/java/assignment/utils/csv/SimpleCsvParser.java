package assignment.utils.csv;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * Apache Commons CSV should be used instead of this Helper.
 */
public final class SimpleCsvParser implements Closeable {

    private Charset parserDefaultCharset = StandardCharsets.UTF_8;
    private String parserDefaultLineSeparator = "\n";
    private boolean parseHeadersFromFile = true;
    private boolean trimValues = true;
    private String columnSeparator = ",";

    private InputStream sourceStream;
    private Map<Integer,String> columnNames;

    public SimpleCsvParser() {
        this.columnNames = new HashMap<>();
    }

    public SimpleCsvParser withHeaders(final String... names) {
        this.columnNames = IntStream.range(0, names.length)
                .collect(HashMap::new, (headerMap,counter) -> headerMap.put(counter, processValue(names[counter])), Map::putAll);
        return this;
    }

    public Stream<CsvRecord> parse(final InputStream sourceStream) {
        if(this.sourceStream!=null) {
            throw new IllegalStateException("parser not closed properly");
        }
        this.sourceStream = sourceStream;
        final InputStreamReader reader = new InputStreamReader(sourceStream, parserDefaultCharset);
        final Scanner scanner = new Scanner(reader).useDelimiter(parserDefaultLineSeparator);
        if(parseHeadersFromFile) {
            withHeaders(scanner.nextLine().split(columnSeparator));
        }
        final Stream<String> lineStream = scanner.tokens();
        return lineStream
                .map(token -> new CsvRecordImpl(createColumnData(token)));
    }

    private Map<String,String> createColumnData(final String line) {
        final String[] columnValues = line.split(columnSeparator);
        return IntStream.range(0, columnValues.length)
                .collect(HashMap::new,
                        (headerMap,counter) -> headerMap.put(columnNames.get(counter),
                                processValue(columnValues[counter])),
                        Map::putAll);
    }

    private String processValue(final String rawValue) {
        return rawValue!=null && trimValues ? rawValue.trim() : rawValue;
    }

    @Override
    public void close() throws IOException {
        if(sourceStream!=null) {
            sourceStream.close();
            sourceStream = null;
        }
        columnNames.clear();
    }

    public static void main(String[] args) throws IOException {

        SimpleCsvParser parser = new SimpleCsvParser();

        String data = "col1 ,col2 \n  1  , 2 \n\n";
        ByteArrayInputStream bais = new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8));
        parser.parse(bais).forEach(System.out::println);
        parser.close();


    }

    private static class CsvRecordImpl implements CsvRecord {

        private final Map<String,String> columnData;

        private CsvRecordImpl(final Map<String, String> columnData) {
            this.columnData = Collections.unmodifiableMap(columnData);
        }

        @Override
        public Map<String, String> getColumnData() {
            return columnData;
        }

        @Override
        public String getValue(final String columnName) {
            return columnData.get(columnName);
        }

        @Override
        public Set<String> columnNames() {
            return columnData.keySet();
        }

        @Override
        public String toString() {
            return columnData.toString();
        }
    }
}
