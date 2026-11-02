package com.fot.tms.ui.common;

import com.fot.tms.util.UIHelper;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.awt.Component;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

/**
 * Helper to export tables and reports to CSV files.
 */
public class ReportExportHelper {

    private ReportExportHelper() {}

    public static void exportTableToCSV(Component parent, JTable table, String defaultFileName) {
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File(defaultFileName));
        int res = chooser.showSaveDialog(parent);
        if (res == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            if (!file.getName().toLowerCase().endsWith(".csv")) {
                file = new File(file.getParentFile(), file.getName() + ".csv");
            }
            try (PrintWriter pw = new PrintWriter(new FileWriter(file))) {
                TableModel model = table.getModel();
                int colCount = model.getColumnCount();
                int rowCount = model.getRowCount();

                // Headers
                for (int c = 0; c < colCount; c++) {
                    pw.print("\"" + model.getColumnName(c) + "\"");
                    if (c < colCount - 1) pw.print(",");
                }
                pw.println();

                // Rows
                for (int r = 0; r < rowCount; r++) {
                    for (int c = 0; c < colCount; c++) {
                        Object val = model.getValueAt(r, c);
                        String s = val != null ? val.toString().replace("\"", "\"\"") : "";
                        pw.print("\"" + s + "\"");
                        if (c < colCount - 1) pw.print(",");
                    }
                    pw.println();
                }

                UIHelper.showInfo(parent, "Report exported successfully to:\n" + file.getAbsolutePath(), "Export Complete");
            } catch (Exception e) {
                UIHelper.showError(parent, "Failed to export report: " + e.getMessage(), "Export Error");
            }
        }
    }
}
