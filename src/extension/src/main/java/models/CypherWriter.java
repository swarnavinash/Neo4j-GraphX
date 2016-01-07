package models;

import translation.Writer;

import java.io.BufferedWriter;
import java.util.List;

public class CypherWriter extends ParallelWriterConfiguration<Map<String, Object>> {

    private List<String> columns;

    public CypherWriter(int start, int length, BufferedWriter bufferedWriter, Integer fullSize, Integer reportBlockSize) {
        super(start, length, bufferedWriter, fullSize, reportBlockSize);
    }

    public CypherWriter(int start, int length, BufferedWriter bufferedWriter, Integer fullSize, Integer reportBlockSize, List<String> columns) {
        super(start, length, bufferedWriter, fullSize, reportBlockSize);
        this.columns = columns;
    }

    @Override
    public void writeBlock(Map<String, Object> block) {
        try {
            String line = "";

            for (String column : columns) {
                line += block.get(column) + ",";
            }

            bufferedWriter.write(line.replaceFirst("[,]$", "\n"));
            Writer.counter++;
            if (Writer.counter % reportBlockSize == 0) {
                // Report status
                System.out.println("Records exported: " + Writer.counter);
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    public List<String> getColumns() {
        return columns;
    }

    public void setColumns(List<String> columns) {
        this.columns = columns;
    }

    @Override
    public ParallelWriterConfiguration<Map<String, Object>> clone() throws CloneNotSupportedException {
        return new CypherWriter(start, length, bufferedWriter, fullSize, reportBlockSize);
    }
}
